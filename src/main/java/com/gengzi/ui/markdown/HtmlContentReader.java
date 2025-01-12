package com.gengzi.ui.markdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HtmlContentReader {
    public static String getHtmlContentFromResources(String fileName) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = HtmlContentReader.class.getClassLoader().getResourceAsStream("html/"+fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine())!= null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void main(String[] args) {
        String htmlContent = getHtmlContentFromResources("content.html");
        System.out.println(htmlContent);
    }
}