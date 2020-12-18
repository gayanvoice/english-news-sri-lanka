package com.example.demo.service;

import com.example.demo.model.data.ImageModel;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Service
public class ImageService {

    private final PostService postService;

    public ImageService(PostService postService) {
        this.postService = postService;
    }

    public InputStream createInputStream(String postId) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(getClass().getResourceAsStream("/static/image/template.jpg"));

        postService.findByPostId(postId).ifPresent(postModel -> {
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
        lineLocation = renderLines(graphics2D, imageModel.getTitle(), false,28, 20, 30, lineLocation) + 10;
        lineLocation = renderLines(graphics2D, imageModel.getDescription(), true, 18, 40, 20, lineLocation) + 10;
        renderLines(graphics2D, "Source: " + imageModel.getSource(), false,  18, 50, 20, lineLocation);
        return graphics2D;
    }

    private int renderLines(Graphics2D graphics2D, String text, boolean fontFamily, int fontSize, int lineSize, int linePaddingTop, int lineLocation) {
        if(fontFamily){
            try{
                graphics2D.setFont(Font.createFont(
                        Font.TRUETYPE_FONT,getClass().getResourceAsStream("/static/font/Roboto-Light.ttf"))
                        .deriveFont(Font.PLAIN, fontSize));
            } catch (Exception ignored) {  }
        } else {
            try{
                graphics2D.setFont(Font.createFont(
                        Font.TRUETYPE_FONT,getClass().getResourceAsStream("/static/font/Roboto-Regular.ttf"))
                        .deriveFont(Font.PLAIN, fontSize));
            } catch (Exception ignored) {  }
        }

        for (String item:addLinebreaks(text, lineSize)) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawString(item, 20, lineLocation);
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

            output.append(word);
            lineLen += word.length();

            if(numberOfLines >= 8){
                if(word.contains(".")){
                    break;
                } else {
                    output.append(" ");
                }
            } else {
                output.append(" ");
            }
        }
        listArray.add(output.toString());
        return listArray;
    }
}
