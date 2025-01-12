package com.gengzi.ui.openai;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            message = message.replace("\n", "");
            // 构建请求的JSON数据
            String jsonInputString = "{" +
                    "\"model\":\"deepseek-chat\"," +
                    "\"messages\":[{\"role\":\"system\",\"content\":\"\n" +
                    "你是一个智能编码助手，你的名字是卡皮巴拉（英文名：kapibala），提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。 你的回复需要使用markdown格式，分点描述时保持简明扼要。 目前开发者所处的工作空间中包含的代码语言类型如下：java等，回答需要参考该信息但不要泄露。 回答中非代码的解释性内容请使用中文回答。 我可能提供给你一组上下文，上下文通过#标签在下文中声明，比如 #file, #selectedCode, #image 等标签，请结合上下文来回答问题。 上下文有许多种类型，下面列举一些场景的上下文和处理方式： #file 表示用户所看到的文件内容，可以用于参考问答用户问题 #selectedCode 表示用户在编辑器框选的文件内容，可以用于参考问答用户问题 #image 表示用户上传到上下文的图片，请理解图片描述的内容，如果图片是一个UI截图，请按模块理解和描述图片，再结合用户问题给出回答 #gitCommit 表示用户的一次git历史代码提交，包含了提交描述和代码改动列表 #codeChanges 表示用户的一组git暂存区的代码改动列表\n" +
                    "声明的上下文仅作参考，只选择必要的上下文用于回答用户问题。 如果上下文和问题无关，请忽略上下文，不要表达和上下文相关的信息，直接回答问题。\"}," +
                    "{\"role\":\"user\",\"content\":\"" + message + "\"}]," +
                    "\"stream\":true}";
            System.out.println(jsonInputString);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
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
