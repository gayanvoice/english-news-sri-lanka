package com.example.demo.service;

import com.example.demo.model.PostsModel;
import com.example.demo.model.constant.HostEnum;
import com.example.demo.model.data.FeedModel;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jdom2.Element;
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

    public PostsModel parse(SyndEntry entry, String hostName) {
        PostsModel postsModel = new PostsModel();
        postsModel.setPostId(getPostId());
        postsModel.setTitle(entry.getTitle());
        postsModel.setContent(getContent(entry.getLink(),hostName).substring(0, 512));
        postsModel.setPublishTime(getCurrentTime());
        postsModel.setUrl(entry.getLink());
        postsModel.setSite(hostName);
        postsModel.setPost("null");
        return postsModel;
    }

    private Document requestDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla");
        connection.timeout(15000);
        return connection.get();
    }

    public List<FeedModel> getFeedModel(){
        List<FeedModel> feedModelList = new ArrayList<>();
        feedModelList.add(new FeedModel("https://economynext.com/feed/", HostEnum.EconomyNext));
        return feedModelList;
    }

    public String getContent(String url, String hostName) {
        String content;
        try {
            Document document = requestDocument(url);
            if (hostName.equals(HostEnum.EconomyNext.getValue())) {
                content = document.getElementById("article-content").getElementsByTag("p").text();
            } else if (hostName.equals(HostEnum.BBCNews.getValue())) {
                content = document.getElementsByTag("main").text();
            } else if (hostName.equals(HostEnum.NewsFirst.getValue())) {
                content = document.getElementsByClass("editor-styles").text();
            } else if (hostName.equals(HostEnum.NethNews.getValue())) {
                content = document.getElementsByClass("td-post-content").text();
            } else if (hostName.equals(HostEnum.Silumina.getValue())) {
                content = document.getElementsByClass("field-name-body").text();
            } else if (hostName.equals(HostEnum.ITN.getValue())) {
                content = document.getElementsByClass("column9").text();
            } else if (hostName.equals(HostEnum.ColomboTimes.getValue())) {
                content = document.getElementsByClass("post-content").text();
            } else if (hostName.equals(HostEnum.Dinamina.getValue())) {
                content = document.getElementsByClass("content").text();
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