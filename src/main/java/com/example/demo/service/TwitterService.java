package com.example.demo.service;

import com.example.demo.model.PostModel;
import com.example.demo.model.constant.HostEnum;
import com.example.demo.model.data.FeedModel;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TwitterService {
    @Value("${twitter.consumerKey}")
    private String twitterConsumerKey;

    @Value("${twitter.consumerSecret}")
    private String twitterConsumerSecret;

    @Value("${twitter.accessToken}")
    private String twitterAccessToken;

    @Value("${twitter.accessTokenSecret}")
    private String twitterAccessTokenSecret;


    public TwitterService() {
    }

    public Optional<String> postStatus(String message) {
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setDebugEnabled(true)
                    .setOAuthConsumerKey(twitterConsumerKey)
                    .setOAuthConsumerSecret(twitterConsumerSecret)
                    .setOAuthAccessToken(twitterAccessToken)
                    .setOAuthAccessTokenSecret(twitterAccessTokenSecret);
            TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
            Twitter twitter = twitterFactory.getInstance();
            Status status = twitter.updateStatus(message);
            return Optional.of(String.valueOf(status.getId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}