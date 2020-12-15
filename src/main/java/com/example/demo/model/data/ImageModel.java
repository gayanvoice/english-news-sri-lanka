package com.example.demo.model.data;

import com.example.demo.model.PostModel;

public class ImageModel {
    private String url;
    private String title;
    private String description;
    private String time;
    private String date;
    private String source;

    public ImageModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ImageModel getImageModel(PostModel postModel){
        ImageModel imageModel = new ImageModel();
        imageModel.setUrl(postModel.getUrl());
        imageModel.setTitle(postModel.getTitle());
        imageModel.setDescription(postModel.getContent());
        imageModel.setTime(postModel.getPublishTime().toString());
        imageModel.setDate(postModel.getPublishTime().toString());
        imageModel.setSource(postModel.getSite());
        return imageModel;
    }
}
