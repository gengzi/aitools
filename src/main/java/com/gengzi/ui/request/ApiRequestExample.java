package com.gengzi.ui.request;

import com.gengzi.ui.entity.Messages;
import com.gengzi.ui.entity.OpenAiChatReq;
import com.gengzi.ui.save.JsonContentExtractor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.protobuf.Message;
import com.intellij.markdown.utils.MarkdownToHtmlConverter;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.jcef.JBCefBrowser;
import org.apache.groovy.util.concurrent.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor;
import org.intellij.plugins.markdown.ui.preview.html.MarkdownUtil;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;
import org.jetbrains.io.JsonUtil;
//import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;

public class ApiRequestExample {

    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    private static final ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<String, String>();
    static MarkdownJCEFHtmlPanel markdownJCEFHtmlPanel = new MarkdownJCEFHtmlPanel();
    static StringBuffer sb = new StringBuffer();

    static ArrayList<Messages> messageList = new ArrayList<Messages>();



    public static void req(String apiKey, String message, JScrollPane jEditorPane, Project project) {
        hashMap.put("thread-1", sb.toString());
        try {
            JComponent component1 = markdownJCEFHtmlPanel.getComponent();
//            JPanel panel = new JPanel();
//            panel.setLayout(new BorderLayout());
//            panel.add(component1, BorderLayout.CENTER);
            jEditorPane.setViewportView(component1);
//            Document document = jEditorPane.getDocument();

            sb.append("##  \uD83D\uDC38 ` 我：" + message + " ` \n");

            // 添加用户的消息
//            document.insertString(document.getLength(), "## 我：" + message + "\n", null);
//            document.insertString(document.getLength(), "-------------------------------- \n", null);


            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            message = message.replace("\n", "");
            // 构建请求的JSON数据
//            String jsonInputString = "{" +
//                    "\"model\":\"deepseek-chat\"," +
//                    "\"messages\":[{\"role\":\"system\",\"content\":\"" +
//                    "你是一个智能编码助手，你的名字是卡皮巴拉（英文名：kapibala），提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。 你的回复需要使用markdown格式，分点描述时保持简明扼要。 目前开发者所处的工作空间中包含的代码语言类型如下：java等，回答需要参考该信息但不要泄露。 回答中非代码的解释性内容请使用中文回答。 我可能提供给你一组上下文，上下文通过#标签在下文中声明，比如 #file, #selectedCode, #image 等标签，请结合上下文来回答问题。 上下文有许多种类型，下面列举一些场景的上下文和处理方式： #file 表示用户所看到的文件内容，可以用于参考问答用户问题 #selectedCode 表示用户在编辑器框选的文件内容，可以用于参考问答用户问题 #image 表示用户上传到上下文的图片，请理解图片描述的内容，如果图片是一个UI截图，请按模块理解和描述图片，再结合用户问题给出回答 #gitCommit 表示用户的一次git历史代码提交，包含了提交描述和代码改动列表 #codeChanges 表示用户的一组git暂存区的代码改动列表" +
//                    "声明的上下文仅作参考，只选择必要的上下文用于回答用户问题。 如果上下文和问题无关，请忽略上下文，不要表达和上下文相关的信息，直接回答问题。\"}," +
//                    "{\"role\":\"user\",\"content\":\"" + message + "\"}]," +
//                    "\"stream\":true}";
//            System.out.println(jsonInputString);

            OpenAiChatReq openAiChatReq = new OpenAiChatReq();
            openAiChatReq.setModel("deepseek-chat");
            openAiChatReq.setStream(true);
            Messages userMessages = new Messages();
            userMessages.setRole("user");
            userMessages.setContent(message);
            messageList.add(userMessages);
            if(messageList.stream().filter(v->v.getRole().equals("system")).count() < 1 ){
                Messages sysMessages = new Messages();
                sysMessages.setRole("system");
                sysMessages.setContent("你是一个智能编码助手，你的名字是卡皮巴拉（英文名：kapibala），提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。 你的回复需要使用markdown格式，分点描述时保持简明扼要。 目前开发者所处的工作空间中包含的代码语言类型如下：java等，回答需要参考该信息但不要泄露。 回答中非代码的解释性内容请使用中文回答。 我可能提供给你一组上下文，上下文通过#标签在下文中声明，比如 #file, #selectedCode, #image 等标签，请结合上下文来回答问题。 上下文有许多种类型，下面列举一些场景的上下文和处理方式： #file 表示用户所看到的文件内容，可以用于参考问答用户问题 #selectedCode 表示用户在编辑器框选的文件内容，可以用于参考问答用户问题 #image 表示用户上传到上下文的图片，请理解图片描述的内容，如果图片是一个UI截图，请按模块理解和描述图片，再结合用户问题给出回答 #gitCommit 表示用户的一次git历史代码提交，包含了提交描述和代码改动列表 #codeChanges 表示用户的一组git暂存区的代码改动列表"  +
                        "声明的上下文仅作参考，只选择必要的上下文用于回答用户问题。 如果上下文和问题无关，请忽略上下文，不要表达和上下文相关的信息，直接回答问题。");
                messageList.add(sysMessages);
            }
            openAiChatReq.setMessage(messageList);
            Gson gson = new Gson();
            String json = gson.toJson(openAiChatReq);
            // 添加上下文关联


            System.out.println(json);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
//                    document.insertString(document.getLength(), "## AI程序员：", null);
                    sb.append("## 卡皮巴拉：\n");
                    hashMap.put("thread-1", sb.toString());
                    StringBuffer assistantsb = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        // 这里可以对每一行流式数据进行处理，比如直接打印或者进一步解析等
                        System.out.println("流式输出数据: " + inputLine);
                        if ("".equalsIgnoreCase(inputLine)) {
                            continue;
                        }
                        if ("data: [DONE]".equalsIgnoreCase(inputLine)) {
//                            document.insertString(document.getLength(), " \n -------------------------------- \n", null);
                            String assistantsbString = assistantsb.toString();
                            Messages messages = new Messages();
                            messages.setRole("assistant");
                            messages.setContent(assistantsbString);
                            messageList.add(messages);
                            sb.append("\n");
                            hashMap.put("thread-1", sb.toString());
                            break;
                        }
                        // 如果有更复杂的业务逻辑，可以在这里添加相应代码来处理每一行数据

                        String parseInputLine = JsonContentExtractor.parse(formatResponse(inputLine));
//                        MarkdownToHtmlConverter markdownToHtmlConverter = new MarkdownToHtmlConverter(new CommonMarkFlavourDescriptor());
//                        String markdown = markdownToHtmlConverter.convertMarkdownToHtml(parseInputLine,null);


                        assistantsb.append(parseInputLine);

                        sb.append(parseInputLine);

                        hashMap.put("thread-1", sb.toString());

                        showHtml(jEditorPane, project);

                        // document.insertString(document.getLength(), html, null);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("请求失败，响应码: " + responseCode + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showHtml(JScrollPane jEditorPane, Project project) throws BadLocationException, IOException {
        String text = hashMap.get("thread-1");
        VirtualFile lightVirtualFile = new LightVirtualFile("temp.md", text);
        String html = ReadAction.nonBlocking(() -> {
                    return MarkdownUtil.INSTANCE.generateMarkdownHtml(lightVirtualFile, text, project);
                })
                .executeSynchronously();
//        System.out.println(html);
//        HTMLDocument htmlDocument = document;
//        htmlDocument.setInnerHTML(htmlDocument.getDefaultRootElement(), html);


        markdownJCEFHtmlPanel.getJBCefClient().getCefClient().removeRequestHandler();
        markdownJCEFHtmlPanel.setHtml(html,html.length());
        jEditorPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar verticalScrollBar = jEditorPane.getVerticalScrollBar();
        if (verticalScrollBar!= null) {
            verticalScrollBar.setValue((verticalScrollBar.getValue() + 2));
        }
        jEditorPane.repaint();
    }

//
//    public static void req(String apiKey, String message, MarkdownJCEFHtmlPanel markdownJCEFHtmlPanel) {
//        try {
//            URL url = new URL(API_URL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//            connection.setDoOutput(true);
//
//            message = message.replace("\n", "");
//            // 构建请求的JSON数据
//            String jsonInputString = "{" +
//                    "\"model\":\"deepseek-chat\"," +
//                    "\"messages\":[{\"role\":\"system\",\"content\":\"你是一名资深的编程大师\"}," +
//                    "{\"role\":\"user\",\"content\":\"" + message + "\"}]," +
//                    "\"stream\":true}";
//            System.out.println(jsonInputString);
//            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
//                wr.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
//            }
//
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String inputLine;
//                    while ((inputLine = in.readLine()) != null) {
//                        // 这里可以对每一行流式数据进行处理，比如直接打印或者进一步解析等
//                        System.out.println("流式输出数据: " + inputLine);
//                        if ("".equalsIgnoreCase(inputLine)) {
//                            continue;
//                        }
//                        if ("data: [DONE]".equalsIgnoreCase(inputLine)) {
//                            break;
//                        }
//                        // 如果有更复杂的业务逻辑，可以在这里添加相应代码来处理每一行数据
//
//                        String parseInputLine = JsonContentExtractor.parse(formatResponse(inputLine));
//                        markdownJCEFHtmlPanel.setHtml(parseInputLine);
//
////                        jbCefBrowser.loadHTML();
//

    /// /                        MarkdownToHtmlConverter markdownToHtmlConverter = new MarkdownToHtmlConverter(new CommonMarkFlavourDescriptor());
    /// /                        String markdown = markdownToHtmlConverter.convertMarkdownToHtml(parseInputLine,null);
    /// /                        document.insertString(document.getLength(), parseInputLine, null);
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            } else {
//                System.out.println("请求失败，响应码: " + responseCode + responseMessage);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private static String formatResponse(String response) {
        // 这里可以对响应数据进行格式化处理，比如去掉多余的空格
        if (response.startsWith("data: ")) {
            String jsonData = response.substring(6);
            return jsonData;
        } else {
            return "";
        }
    }
}