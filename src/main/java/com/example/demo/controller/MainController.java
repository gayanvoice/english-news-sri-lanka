package com.example.demo.controller;

import com.example.demo.model.data.FeedModel;
import com.example.demo.service.ImageService;
import com.example.demo.service.ParseService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import java.util.List;

@Controller
@RequestMapping(path="/")
public class MainController {

    private final ParseService parseService;
    private final ImageService imageService;

    public MainController(ParseService parseService, ImageService imageService) {
        this.parseService = parseService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping(path="/status")
    public @ResponseBody String getStatus() {
        return "true";
    }

    @GetMapping(path="/getSites")
    public @ResponseBody List<FeedModel> getAllSites() {
        return parseService.getFeedModel();
    }

    @GetMapping(value = "/{post}/image.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable(value = "post") String post) throws Exception {
        return IOUtils.toByteArray(imageService.createInputStream(post));
    }
}