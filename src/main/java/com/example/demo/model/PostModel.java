package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "post")
public class PostModel {

    @Id
    private String postId;
    private String title;
    private String content;
    private LocalDateTime publishTime;
    private String url;
    private String site;
    private String tweet;
    private String post;


    public PostModel() {
    }

    public PostModel(String postId, String title, String content, LocalDateTime publishTime, String url, String site, String tweet, String post) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.publishTime = publishTime;
        this.url = url;
        this.site = site;
        this.tweet = tweet;
        this.post = post;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
