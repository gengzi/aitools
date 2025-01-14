package com.gengzi.ui.openai;

import com.gengzi.ui.entity.Messages;
import com.gengzi.ui.entity.OpenAiChatReq;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Curl {


    public static Stream<String> getModelResponseTextStream(String apiUrl, String apiKey, String message) throws IOException {
        InputStream inputStream = getModelResponseStream(apiUrl,apiKey, message);
        if(inputStream == null){
            throw new RuntimeException("请求大模型失败");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> lines = reader.lines();
//        lines.filter(line -> line.contains("特定关键词"))
//                .map(String::toUpperCase)
//                .forEach(System.out::println);
        return lines;
    }


    public static InputStream getModelResponseStream(String apiUrl, String apiKey, String message) {
        try {
            ArrayList<Messages> messageList = new ArrayList<Messages>();
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

//            message = message.replace("\n", "");
            // 构建请求的JSON数据
            OpenAiChatReq openAiChatReq = new OpenAiChatReq();
            openAiChatReq.setModel("deepseek-chat");
            openAiChatReq.setStream(true);
            Messages userMessages = new Messages();
            userMessages.setRole("user");
            userMessages.setContent(message);
            messageList.add(userMessages);
            if (messageList.stream().filter(v -> v.getRole().equals("system")).count() < 1) {
                Messages sysMessages = new Messages();
                sysMessages.setRole("system");
                sysMessages.setContent("你是一个智能编码助手，提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。 你的回复不要使用markdown格式，直接提供答案");
                messageList.add(sysMessages);
            }
            openAiChatReq.setMessage(messageList);
            Gson gson = new Gson();
            String json = gson.toJson(openAiChatReq);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            } else {
                System.out.println("请求失败，响应码: " + responseCode + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
