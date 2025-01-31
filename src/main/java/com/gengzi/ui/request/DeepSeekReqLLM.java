package com.gengzi.ui.request;

import com.gengzi.ui.entity.Messages;
import com.gengzi.ui.entity.OpenAiChatReq;
import com.gengzi.ui.entity.RequestEntityBase;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.intellij.openapi.ui.playback.PlaybackRunner.StatusCallback.Type.message;

public class DeepSeekReqLLM extends RequestLLMAbs{
    @Override
    public Stream<String> req(RequestEntityBase requestEntity) {
        List<Messages> userMessages = requestEntity.getMessages();

        try {
            ArrayList<Messages> messageList = new ArrayList<Messages>();
            URL url = new URL("https://api.deepseek.com/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + "sk-f7af6993c62840b9a19b20d2e7063511");
            connection.setDoOutput(true);

//            message = message.replace("\n", "");
            // 构建请求的JSON数据
            OpenAiChatReq openAiChatReq = new OpenAiChatReq();
            openAiChatReq.setModel("deepseek-chat");
            openAiChatReq.setStream(true);
            messageList.addAll(userMessages);
            if (messageList.stream().filter(v -> v.getRole().equals("system")).count() < 1) {
                Messages sysMessages = new Messages();
                sysMessages.setRole("system");
                sysMessages.setContent("你是一个智能编码助手，提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。#file代表用户添加文件内容，" +
                        "回复要求：" +
                        "返回的每个代码块前添加[文件名称]标记，后面紧跟修改后的代码内容" +
                        "回复格式：" +
                        "先回答所有修改的代码文件，文件名：[例如xx.java] [修改后的代码]" +
                        "再回复代码修改的内容。");
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
                InputStream inputStream = connection.getInputStream();
                if(inputStream == null){
                    throw new RuntimeException("请求大模型失败");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                Stream<String> lines = reader.lines();
                return lines;
            } else {
                System.out.println("请求失败，响应码: " + responseCode + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
