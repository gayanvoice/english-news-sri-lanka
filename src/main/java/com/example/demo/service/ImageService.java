package com.example.demo.service;

import com.example.demo.model.data.ImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class ImageService {

    @Value("${app.name}")
    private String appName;

    private final PostsService postsService;

    public ImageService(PostsService postsService) {
        this.postsService = postsService;
    }

    public InputStream createInputStream(String postId) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(getClass().getResourceAsStream("/static/image/template.jpg"));

        postsService.findByPostId(postId).ifPresent(postModel -> {
            Graphics2D graphics2D = createGraphics2D(bufferedImage, new ImageModel().getImageModel(postModel));
            graphics2D.dispose();
        });
        ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private Graphics2D createGraphics2D(BufferedImage bufferedImage, ImageModel imageModel){
        int lineLocation = 40;
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        lineLocation = renderLines(graphics2D, imageModel.getTitle(), 28, Font.BOLD,20, 30, 20, lineLocation) + 10;
        lineLocation = renderLines(graphics2D, imageModel.getDescription(), 20, Font.PLAIN, 40, 20, 20,  lineLocation) + 10;
        lineLocation = renderLines(graphics2D, "Source: " + imageModel.getSource(), 18, Font.BOLD, 50, 20, 20,  lineLocation);
        lineLocation = renderLines(graphics2D, getTime() + " | " + getDate() + " | " + appName,16, Font.PLAIN, 50, 20, 80,  480);
        return graphics2D;
    }

    private int renderLines(Graphics2D graphics2D, String text, int fontSize, int fontType,  int lineSize, int linePaddingTop, int paddingLeft, int lineLocation) {
        graphics2D.setFont(new Font("Calibri", fontType, fontSize));
        for (String item:addLinebreaks(text, lineSize)) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawString(item, paddingLeft, lineLocation);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lineLocation = lineLocation + linePaddingTop;
        }
        return lineLocation;
    }

    private java.util.List<String> addLinebreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder();
        int numberOfLines = 0;
        List<String> listArray = new ArrayList<>();
        int lineLen = 0;
        while(tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (lineLen + word.length() > maxLineLength) {
                listArray.add(output.toString());
                output.setLength(0);
                lineLen = 0;
                numberOfLines = numberOfLines + 1;
            }

            output.append(word).append(" ");
            lineLen += word.length();

            if(numberOfLines >= 10){
                if(word.contains(".")){
                    break;
                }
            }
        }
        listArray.add(output.toString());
        return listArray;
    }

    private String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Colombo"));
        return dateTime.format(formatter);
    }

    private String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Colombo"));
        return dateTime.format(formatter);
    }
}
