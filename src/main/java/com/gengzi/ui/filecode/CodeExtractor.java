package com.gengzi.ui.filecode;

import java.util.*;
import java.util.regex.*;

public class CodeExtractor {
    
    // 匹配格式：
    // 文件名：[filename]
    // 代码块标记
// 修改正则表达式为：
    private static final Pattern PATTERN = Pattern.compile(
            "文件名\\s*：\\s*\\[(?<filename>.+?)\\][\\s\\S]*?```(?<lang>\\w+)\\n(?<code>[\\s\\S]*?)\\n```",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    public static Map<String, String> parseContent(String input) {
        Map<String, String> result = new LinkedHashMap<>();
        Matcher matcher = PATTERN.matcher(input);
        
        while (matcher.find()) {
            String filename = sanitizeFilename(matcher.group("filename"));
            String code = matcher.group("code").trim();
            
            if (!filename.isEmpty() && !code.isEmpty()) {
                result.put(filename, code);
            }
        }
        return result;
    }

    private static String sanitizeFilename(String rawName) {
        // 移除非法字符并标准化路径
        return rawName.replaceAll("[\\\\/:*?\"<>|]", "_")
                     .replaceAll("\\.\\.", "_")
                     .trim();
    }

    // 测试用例
    public static void main(String[] args) {
        String input = "文件名：[Main.java]\n" +
                      "```java\n" +
                      "public class Main {\n" +
                      "    public static void main(String[] args) {\n" +
                      "        //TIP 当文本光标位于高亮显示的文本处时按 <shortcut actionId=\"ShowIntentionActions\"/>\n" +
                      "        // 查看 IntelliJ IDEA 建议如何修正。\n" +
                      "    }\n" +
                      "}\n" +
                      "```\n\n" +
                      "代码修改的内容：\n" +
                      "1. 在 `Main` 类中添加了一个新的方法...";

        Map<String, String> codeMap = parseContent(input);
        codeMap.forEach((k, v) -> {
            System.out.println("提取到文件: " + k);
            System.out.println("代码内容:\n" + v);
            System.out.println("----");
        });
    }
}