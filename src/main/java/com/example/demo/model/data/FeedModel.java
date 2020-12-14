package com.example.demo.model.data;

import com.example.demo.model.constant.HostEnum;

public class FeedModel {
    private String url;
    private HostEnum hostName;

    public FeedModel(String url, HostEnum hostName) {
        this.url = url;
        this.hostName = hostName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HostEnum getHostName() {
        return hostName;
    }

    public void setHostName(HostEnum hostName) {
        this.hostName = hostName;
    }
}