import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SSEOutputApp extends JFrame {
    private final JTextPane textPane;
    private final JScrollPane scrollPane;
    private boolean isCodeBlock = false;
    private final StyleContext styleContext;
    private final Style regularStyle;
    private final Style codeStyle;

    public SSEOutputApp() {
        // 初始化UI
        setTitle("SSE流式输出演示");
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

        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        // 模拟SSE数据流
        startSSEStream();
    }

    private void startSSEStream() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::processSSEData, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void processSSEData() {
        // 模拟你提供的SSE数据
        String[] sseData = {
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"当然\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"可以\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"！\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"以下\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"是一个\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"简单的\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Java\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"代码\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"示例\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"，\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"它\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"定义\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"了一个\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"`\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Hello\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"World\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"`\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"类\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"，\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"并在\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"`\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"main\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"`\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"方法\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"中\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"打印\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"出\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"“\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"Hello\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\",\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\" World\"},\"logprobs\":null,\"finish_reason\":null}]}",
                "data: {\"id\":\"7a492994-e4f3-4432-841a-d6694ff632fb\",\"object\":\"chat.completion.chunk\",\"created\":1736436505,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_3a5770e1b4\",\"choices\":[{\"index\":0,\"delta\":{\"content\":\"!”\"},\"logprobs\":null,\"finish_reason\":null}]}"
        };

        // ... 接上部分代码 ...

        // 处理SSE数据
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
    // 从SSE数据中提取content字段
    try {
        int start = sseData.indexOf("\"content\":\"") + 10;
        int end = sseData.indexOf("\"", start);
        return sseData.substring(start, end);
    } catch (Exception e) {
        return null;
    }
}

private void appendText(String text) {
    try {
        // 处理代码块
        if (text.contains("`")) {
            if (!isCodeBlock) {
                // 开始代码块
                isCodeBlock = true;
                textPane.getDocument().insertString(
                        textPane.getDocument().getLength(),
                        "\n",
                        codeStyle
                );
            } else {
                // 结束代码块
                isCodeBlock = false;
                textPane.getDocument().insertString(
                        textPane.getDocument().getLength(),
                        "\n",
                        regularStyle
                );
                return;
            }
        }

        // 插入文本
        Style style = isCodeBlock ? codeStyle : regularStyle;
        textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                text,
                style
        );

        // 自动滚动
        textPane.setCaretPosition(textPane.getDocument().getLength());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new SSEOutputApp().setVisible(true));
}
}
