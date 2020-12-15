package com.example.demo.service;

import com.example.demo.model.facebook.FacebookResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URL;
import java.util.*;

@Service
public class FacebookService {

    @Value("${facebook.authentication.key}")
    private String facebookAuthenticationKey;

    @Value("${facebook.page.id}")
    private String facebookPageId;

    @Value("${app.url}")
    private String appUrl;

    private static final Logger log = LoggerFactory.getLogger(FacebookService.class);
    private final RestTemplate restTemplate;

    public FacebookService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public FacebookResponseModel postFaceBookFeedRequest(String message, String url) {
        try {
            ResponseEntity<FacebookResponseModel> responseEntity = getResponseEntity(
                    new URL("https://graph.facebook.com/" + facebookPageId + "/feed"),
                    new HttpEntity<>(getPostUrlHeaderMap(facebookAuthenticationKey, message, url), getHttpHeaders()));
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
    }

    public FacebookResponseModel postFaceBookPhotosRequest(String message, String postId) {
        try {
            ResponseEntity<FacebookResponseModel> responseEntity = getResponseEntity(
                    new URL("https://graph.facebook.com/" + facebookPageId + "/photos"),
                    new HttpEntity<>(getPostPhotoHeaderMap(facebookAuthenticationKey, message, postId), getHttpHeaders()));
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
    }


    private ResponseEntity<FacebookResponseModel> getResponseEntity(URL url, HttpEntity<Map<String, Object>> httpEntity){
        return this.restTemplate.postForEntity(url.toString(), httpEntity, FacebookResponseModel.class);
    }

    private Map<String, Object> getPostPhotoHeaderMap(String accessToken, String message, String postId){
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("access_token", accessToken);
        headerMap.put("message", message);
        headerMap.put("url", appUrl + postId + "/image.jpg");
        System.out.println(appUrl + postId + "/image.jpg");
        return headerMap;
    }

    private Map<String, Object> getPostUrlHeaderMap(String accessToken, String message, String url){
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("access_token", accessToken);
        headerMap.put("message", message);
        headerMap.put("link", url);
        return headerMap;
    }

    private HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("user-agent", "Mozilla/5.0 Firefox/26.0");
        return httpHeaders;
    }
}