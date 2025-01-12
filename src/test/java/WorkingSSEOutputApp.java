import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkingSSEOutputApp extends JFrame {
    private final JTextPane textPane;
    private final JScrollPane scrollPane;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JLabel loadingLabel;
    private boolean isCodeBlock = false;
    private final StyleContext styleContext;
    private final Style regularStyle;
    private final Style codeStyle;

    public WorkingSSEOutputApp() {
        // 初始化UI
        setTitle("可用的SSE流式输出演示");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 初始化文本样式
        styleContext = new StyleContext();
        regularStyle = styleContext.addStyle("Regular", null);
        StyleConstants.setFontFamily(regularStyle, "SansSerif");
        StyleConstants.setFontSize(regularStyle, 14);

        codeStyle = styleContext.addStyle("Code", null);
        StyleConstants.setFontFamily(codeStyle, "Monospaced");
        StyleConstants.setFontSize(codeStyle, 14);
        StyleConstants.setBackground(codeStyle, new Color(245, 245, 245));
        StyleConstants.setForeground(codeStyle, new Color(51, 51, 51));

        // 主输出区域
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane = new JScrollPane(textPane);
        
        // 输入区域
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        sendButton = new JButton("发送");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 加载动画
        loadingLabel = new JLabel("加载中...");
        loadingLabel.setVisible(false);

        // 复制按钮
        JButton copyButton = new JButton("复制代码");
        copyButton.addActionListener(e -> copyCodeToClipboard());

        // 布局
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(copyButton, BorderLayout.EAST);
        bottomPanel.add(loadingLabel, BorderLayout.WEST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件处理
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        initCodeStyle();
        // 模拟SSE数据流
        startSSEStream();
    }

    private void startSSEStream() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::processSSEData, 0, 500, TimeUnit.MILLISECONDS);
    }
    private int currentIndex = 0;
    private void processSSEData() {
        String[] sseData = {
            "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"```\"},\"logprobs\":null,\"finish_reason\":null}]}",
            "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"java\"},\"logprobs\":null,\"finish_reason\":null}]}",
            "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"\n\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"public\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"```\n\n\"},\"logprobs\":null,\"finish_reason\":null}]}"
        };


        for (String data : sseData) {
            String content = extractContent(data);
            if (content != null) {
                SwingUtilities.invokeLater(() -> appendText(content));
                try {
                    Thread.sleep(500); // 模拟延迟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String extractContent(String sseData) {
        try {
            // 使用正则表达式匹配content字段
            String pattern = "\"content\":\"(.*?)(?<!\\\\)\"";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(sseData);

            if (m.find()) {
                // 获取匹配的内容并处理转义字符
                String content = m.group(1);
                // 处理常见的转义字符
                content = content.replace("\\\"", "\"")
                        .replace("\\n", "\n")
                        .replace("\\t", "\t")
                        .replace("\\\\", "\\");
                return content;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
//            appendText("\n用户: " + message + "\n", regularStyle);
            inputField.setText("");
            showLoading(true);
            
            // 模拟处理延迟
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // 模拟2秒处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> showLoading(false));
            }).start();
        }
    }

    private void showLoading(boolean show) {
        loadingLabel.setVisible(show);
    }

    private void copyCodeToClipboard() {
        try {
            String selectedText = textPane.getSelectedText();
            if (selectedText == null || selectedText.isEmpty()) {
                selectedText = textPane.getText();
            }
            StringSelection stringSelection = new StringSelection(selectedText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(this, "代码已复制到剪贴板");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "复制失败: " + e.getMessage());
        }
    }

//    private void appendText(String text, Style style) {
//        try {
//            textPane.getDocument().insertString(
//                textPane.getDocument().getLength(),
//                text,
//                style
//            );
//            textPane.setCaretPosition(textPane.getDocument().getLength());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
private String currentCodeLanguage = "";
    private void appendText(String text) {
        try {
            Document doc = textPane.getDocument();
            int length = doc.getLength();

            // 处理代码块开始
            if (text.trim().startsWith("```")) {
                if (!isCodeBlock) {
                    // 开始代码块
                    isCodeBlock = true;
                    currentCodeLanguage = text.trim().substring(3).trim(); // 获取语言类型
                    // doc.insertString(length, "\n```" + currentCodeLanguage + "\n", codeStyle);

                } else {
                    // 结束代码块
                    isCodeBlock = false;
                    doc.insertString(length, "\n```\n", codeStyle);
                    currentCodeLanguage = "";
                }
                return;
            }

            // 插入内容
            if (isCodeBlock) {
                // 在代码块中，保留原始格式
                doc.insertString(length, text, codeStyle);
            } else {
                // 普通文本
                doc.insertString(length, text, regularStyle);
            }

            // 自动滚动
            textPane.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化代码样式
    private void initCodeStyle() {
        // 代码块样式
        StyleConstants.setFontFamily(codeStyle, "Monospaced");
        StyleConstants.setFontSize(codeStyle, 14);
        StyleConstants.setBackground(codeStyle, new Color(240, 240, 240));
        StyleConstants.setForeground(codeStyle, new Color(0, 0, 128));
        StyleConstants.setBold(codeStyle, false);

        // 添加边框和缩进
        SimpleAttributeSet border = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(border, 0.2f);
        StyleConstants.setLeftIndent(border, 20);
        StyleConstants.setRightIndent(border, 20);
        StyleConstants.setSpaceAbove(border, 10);
        StyleConstants.setSpaceBelow(border, 10);
//        StyleConstants.setBorder(border, BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)
//        ));
        codeStyle.addAttributes(border);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WorkingSSEOutputApp app = new WorkingSSEOutputApp();
            app.setVisible(true);
        });
    }
}