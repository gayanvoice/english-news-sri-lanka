package com.example.demo.service;

import com.example.demo.model.PostModel;
import com.example.demo.model.constant.HostEnum;
import com.example.demo.model.data.FeedModel;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParseService {

    public PostModel parse(SyndEntry entry, String hostName) {
        PostModel postModel = new PostModel();
        postModel.setPostId(getPostId());
        postModel.setTitle(entry.getTitle());
        postModel.setContent(limitContent(getContent(entry.getLink(),hostName)));
        postModel.setPublishTime(getCurrentTime());
        postModel.setUrl(entry.getLink());
        postModel.setSite(hostName);
        postModel.setTweet("null");
        postModel.setPost("null");
        return postModel;
    }

    private String limitContent(String content){
        try {
            if(content.length() >= 512){
                try{
                    return content.substring(0, 512);
                } catch (Exception ignored) {
                    return "null";
                }
            } else {
                return content;
            }
        } catch (Exception ignores) {
            return "null";
        }
    }

    private Document requestDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla");
        connection.timeout(15000);
        return connection.get();
    }

    public List<FeedModel> getFeedModel(){
        List<FeedModel> feedModelList = new ArrayList<>();
        feedModelList.add(new FeedModel("https://www.newsfirst.lk/feed/", HostEnum.NewsFirst));
        feedModelList.add(new FeedModel("http://www.sundayobserver.lk/taxonomy/term/5/all/feed", HostEnum.SundayOberver));
        feedModelList.add(new FeedModel("http://www.adaderana.lk/rss.php", HostEnum.AdaDerana));
        return feedModelList;
    }

    public String getContent(String url, String hostName) {
        String content;
        try {
            Document document = requestDocument(url);
            if (hostName.equals(HostEnum.NewsFirst.getValue())) {
                content = document.getElementsByClass("editor-styles").text();
            } else if (hostName.equals(HostEnum.SundayOberver.getValue())) {
                content = document.getElementsByClass("field-name-body").text();
            } else if (hostName.equals(HostEnum.AdaDerana.getValue())) {
                content = document.getElementsByClass("news-content").text();
            } else {
                content = null;
            }
        } catch (Exception e) {
            content = null;
        }
        return content;
    }

    private String getPostId() {
        return String.valueOf(System.nanoTime());
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}