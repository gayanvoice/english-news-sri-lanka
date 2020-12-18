package com.example.demo.component;

import com.example.demo.model.PostModel;
import com.example.demo.model.data.FeedModel;
import com.example.demo.model.facebook.FacebookResponseModel;
import com.example.demo.service.*;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class ScheduledTaskComponent {

    @Value("${mode}")
    private String mode;

    @Value("${app.url}")
    private String appUrl;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskComponent.class);
    private final RestService restService;
    private final ParseService parseService;
    private final PostService postService;
    private final FacebookService facebookService;

    public ScheduledTaskComponent(RestService restService,
                                  ParseService parseService,
                                  PostService postService,
                                  FacebookService facebookService) {
        this.restService = restService;
        this.parseService = parseService;
        this.postService = postService;
        this.facebookService = facebookService;
    }

    @Scheduled(fixedRate = 600000)
    public void setAlive() {
        if (mode.equals("prod")) {
            if (restService.setAlive(appUrl + "status")) {
                log.info("set alive ok " + getCurrentTime());
            } else {
                log.error("set alive null " + getCurrentTime());
            }
        } else {
            log.error("set alive dev " + getCurrentTime());
        }
    }

    @Scheduled(fixedDelay = 900000)
    public void fetchFeedUrlList() {
        if (mode.equals("prod")) {
            for (FeedModel feedModel : parseService.getFeedModel()) {
                log.info("getFeedModel " + feedModel.getHostName());
                try {
                    URL feedSource = new URL(feedModel.getUrl());
                    SyndFeedInput syndFeedInput = new SyndFeedInput();
                    try {
                        URLConnection urlConnection = feedSource.openConnection();
                        urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0");
                        SyndFeed feed = syndFeedInput.build(new XmlReader(urlConnection));
                        List<SyndEntry> entries = feed.getEntries();
                        int numberOfPostsExists = 0;
                        for (SyndEntry syndEntry : entries) {
                            log.info("getEntry()");
                            if (numberOfPostsExists <= 3) {
                                if (postService.checkPostExists(syndEntry.getLink())) {
                                    numberOfPostsExists = numberOfPostsExists + 1;
                                    log.info("postExists() " + syndEntry.getTitle());
                                } else {
                                    PostModel postModel = parseService.parse(syndEntry, feedModel.getHostName().getValue());
                                    if (postModel.getContent() != null) {
                                        postService.addPost(postModel);
                                        log.info("addPost() " + postModel.getTitle());
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("send request to xml " + e);
                    }
                } catch (Exception e) {
                    System.out.println("url error " + e);
                }
            }
            log.info("getFeedModel()");
        } else {
            log.error("fetchFeedUrlList dev");
        }
    }

    @Scheduled(fixedDelay = 1800000)
    public void postFaceBookFeed() {
        if (mode.equals("prod")) {
            if (!isTimeBetweenRange()) {
                log.info("postFaceBookFeed()");
                postService.findTopNullPost().ifPresent(postModel -> {
                    log.info(postModel.getTitle() + " " + postModel.getPostId());
                    if (getRandomBoolean()) {
                        try {
                            Optional<FacebookResponseModel> facebookResponseModel = Optional
                                    .ofNullable(facebookService.postFaceBookFeedRequest(
                                            postModel.getTitle() +
                                                    "\n\n" + "#englishnewssrilanka " + "#" + postModel.getSite().toLowerCase() + "\n#news #englishnews #lk #lka #srilanka",
                                            postModel.getUrl()));
                            if (facebookResponseModel.isPresent()) {
                                postModel.setPost(facebookResponseModel.get().getId());
                                postService.updatePost(postModel);
                                log.info("updatePost()");
                            }
                        } catch (Exception ignored) {
                        }
                    } else {
                        try {
                            Optional<FacebookResponseModel> facebookResponseModel = Optional
                                    .ofNullable(facebookService.postFaceBookPhotosRequest(
                                            postModel.getTitle() +
                                                    "\n\n" + "#englishnewssrilanka " + "#" + postModel.getSite().toLowerCase() + "\n#news #englishnews #lk #lka #srilanka" +
                                                    "\n\n" + "\uD83D\uDD17 " + postModel.getUrl(),
                                            postModel.getPostId()));
                            if (facebookResponseModel.isPresent()) {
                                postModel.setPost(facebookResponseModel.get().getId());
                                postService.updatePost(postModel);
                                log.info("updatePost()");
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        } else {
            log.error("postFaceBookFeed dev");
        }
    }

    public boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    private boolean isTimeBetweenRange() {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime stop = LocalTime.of(6, 0);
        ZoneId zoneId = ZoneId.of("Asia/Colombo");
        LocalTime now = LocalTime.now(zoneId);
        return (!now.isBefore(start)) && now.isBefore(stop);

    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Colombo"));
    }
}