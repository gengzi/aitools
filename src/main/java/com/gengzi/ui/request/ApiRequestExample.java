package com.gengzi.ui.request;

import com.gengzi.ui.entity.Messages;
import com.gengzi.ui.entity.OpenAiChatReq;
import com.gengzi.ui.local.Constant;
import com.gengzi.ui.save.JsonContentExtractor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.gengzi.ui.save.MySettings;
import com.gengzi.ui.util.EmojiToUnicode;
import com.google.gson.Gson;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.components.JBScrollPane;
import org.intellij.plugins.markdown.ui.preview.html.MarkdownUtil;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;
//import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;

public class ApiRequestExample {

    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    private static final ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<String, String>();
    static MarkdownJCEFHtmlPanel markdownJCEFHtmlPanel = new MarkdownJCEFHtmlPanel();
    static StringBuffer sb = new StringBuffer();
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    static ArrayList<Messages> messageList = new ArrayList<Messages>();


    public static void req(String apiKey, String message, JBScrollPane jEditorPane, Project project, String otherMsg) {
        hashMap.put("thread-1", sb.toString());
        try {
            MySettings settings = MySettings.getInstance();
            JComponent component1 = markdownJCEFHtmlPanel.getComponent();
//            JPanel panel = new JPanel();
//            panel.setLayout(new BorderLayout());
//            panel.add(component1, BorderLayout.CENTER);
            jEditorPane.setViewportView(component1);
//            Document document = jEditorPane.getDocument();
            String mePicture = settings.componentStates.get(Constant.ME_PICTURE);
            mePicture = StringUtil.isEmpty(mePicture) ? "\uD83D\uDC40" : EmojiToUnicode.unicodeToEmoji(mePicture);
            sb.append("##  " + mePicture + "  ` 我：" + message + " ` \n");

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
            userMessages.setContent(message + otherMsg);
            messageList.add(userMessages);
            if (messageList.stream().filter(v -> v.getRole().equals("system")).count() < 1) {
                Messages sysMessages = new Messages();
                sysMessages.setRole("system");
                sysMessages.setContent("你是一个智能编码助手，提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。 你的回复需要使用markdown格式，分点描述时保持简明扼要。 目前开发者所处的工作空间中包含的代码语言类型如下：java等，回答需要参考该信息但不要泄露。 回答中非代码的解释性内容请使用中文回答。 我可能提供给你一组上下文，上下文通过#标签在下文中声明，比如 #file, #selectedCode, #image 等标签，请结合上下文来回答问题。 上下文有许多种类型，下面列举一些场景的上下文和处理方式： #file 表示用户所看到的文件内容，可以用于参考问答用户问题 #selectedCode 表示用户在编辑器框选的文件内容，可以用于参考问答用户问题 #image 表示用户上传到上下文的图片，请理解图片描述的内容，如果图片是一个UI截图，请按模块理解和描述图片，再结合用户问题给出回答 #gitCommit 表示用户的一次git历史代码提交，包含了提交描述和代码改动列表 #codeChanges 表示用户的一组git暂存区的代码改动列表" +
                        "声明的上下文仅作参考，只选择必要的上下文用于回答用户问题。 如果上下文和问题无关，请忽略上下文，不要表达和上下文相关的信息，直接回答问题。你的回复内容的最后加上表情符号。");
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
                    String aiPicture = settings.componentStates.get(Constant.AI_PICTURE);
                    aiPicture = StringUtil.isEmpty(aiPicture) ? "\uD83E\uDDE0" : EmojiToUnicode.unicodeToEmoji(aiPicture);
                    sb.append("## " + aiPicture + " AI程序员：\n");
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

    private static void showHtml(JBScrollPane jEditorPane, Project project) throws BadLocationException, IOException {
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
        markdownJCEFHtmlPanel.setHtml(html, html.length());

//        Dimension preferredSize = markdownJCEFHtmlPanel.getComponent().getPreferredSize();
//        JViewport viewport = jEditorPane.getViewport();
//        viewport.setViewPosition(new Point(0,preferredSize.height + html.length()));
//        jEditorPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        JScrollBar verticalScrollBar = jEditorPane.getVerticalScrollBar();
//        if (verticalScrollBar != null) {
//            verticalScrollBar.setValue((verticalScrollBar.getValue() + 2));
//        }
//        jEditorPane.repaint();
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


    public static void commitMsg(String apiKey, String message, Project project, CommitMessage commitMessage) {

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            message = message.replace("\n", "");
            ArrayList<Messages> messageList = new ArrayList<Messages>();
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
                String prompt = "你是一个智能编码助手，提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。" +
                        "用户会提供一个 git 提交文件的差异上下文，比如 #diff标签，代表git diff 相关信息。" +
                        "根据 'git diff --staged' 的输出以 `- [新增/修改/删除](类名/文件名): 改动描述。` 的格式编写一个简洁的提交消息。" +
                        "使用现在时和主动语态，分点换行输出,每行最多 120 个字符，不使用代码块。\n" +
                        "例如：两个git diff --staged输出 \n" +
                        "- 描述：修改了xx功能，存在两个文件修改,如下:\n" +
                        "- [新增](Test.java): 修改了xx内容。\n" +
                        "- [删除](test.xml): 完善了xx内容。\n ";
                sysMessages.setContent(prompt);
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
            StringBuilder sb = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        // 这里可以对每一行流式数据进行处理，比如直接打印或者进一步解析等
                        System.out.println("流式输出数据: " + inputLine);
                        if ("".equalsIgnoreCase(inputLine)) {
                            continue;
                        }
                        if ("data: [DONE]".equalsIgnoreCase(inputLine)) {
//                            document.insertString(document.getLength(), " \n -------------------------------- \n", null);
                            break;
                        }
                        // 如果有更复杂的业务逻辑，可以在这里添加相应代码来处理每一行数据
                        String parseInputLine = JsonContentExtractor.parse(formatResponse(inputLine));
                        sb.append(parseInputLine);
                        SCHEDULED_EXECUTOR.schedule(new Runnable() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    // 由于getText获取的是去除前后换行空格的内容，所以有问题
//                                    String text = commitMessage.getText();
                                    commitMessage.setText(sb.toString());
                                });
                            }
                        }, 10, TimeUnit.MILLISECONDS);


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


    public static void fileDoc(String apiKey, String message, Project project, Editor editor) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            message = message.replace("\n", "");
            ArrayList<Messages> messageList = new ArrayList<Messages>();
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
                String prompt = "你是一个智能编码助手，提供行级/函数级实时续写、自然语言生成代码、单元测试生成、代码注释生成、代码解释、研发智能问答、异常报错排查等能力。" +
                        "用户会提供一个代码文件内容，比如 #file标签，代表代码文件内容等相关信息。" +
                        "根据'代码文件内容'分析编写一个简洁准确的文档注释," +
                        "要求：\n" +
                        "1.使用现在时和主动语态，每行最多 120 个字符，可以使用代码块。\n" +
                        "2.禁止返回markdown语法格式的内容，注释必须按照代码文件类型注释语法返回。\n" +
                        "下面是一个示例，请参考示例返回：如果java类型的文件\n" +
                        "    /**\n" +
                        "     * \n 此类描述了xxx的内容，重要的方法有xx()" +
                        "     * 方法示例：\n" +
                        "     * \n" +
                        "     */";
                sysMessages.setContent(prompt);
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
            CaretModel caretModel = editor.getCaretModel();
            Document document = editor.getDocument();
            Application application = ApplicationManager.getApplication();
            AtomicInteger offindex = new AtomicInteger();
            application.runReadAction(() -> {
                AtomicInteger offset = new AtomicInteger(caretModel.getOffset());
                offindex.set(offset.get());
            });

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        // 这里可以对每一行流式数据进行处理，比如直接打印或者进一步解析等
                        System.out.println("流式输出数据: " + inputLine);
                        if ("".equalsIgnoreCase(inputLine)) {
                            continue;
                        }
                        if ("data: [DONE]".equalsIgnoreCase(inputLine)) {
//                            document.insertString(document.getLength(), " \n -------------------------------- \n", null);
                            break;
                        }
                        // 如果有更复杂的业务逻辑，可以在这里添加相应代码来处理每一行数据
                        String parseInputLine = JsonContentExtractor.parse(formatResponse(inputLine));

                        if (parseInputLine.startsWith("```")) {
                            continue;
                        }

                        SwingUtilities.invokeLater(() -> {
                            application.invokeLater(() -> {
                                application.runWriteAction(() -> {
                                    WriteCommandAction.runWriteCommandAction(project, () -> {
                                        // 5. 插入文本内容
                                        document.insertString(offindex.get(), parseInputLine);
                                        offindex.set(offindex.get() + parseInputLine.length());
                                    });
                                });
                            }, ModalityState.defaultModalityState());
                        });


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


}