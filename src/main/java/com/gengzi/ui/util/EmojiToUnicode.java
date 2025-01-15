package com.gengzi.ui.util;

public class EmojiToUnicode {
    public static String emojiToUnicode(String emoji) {
        if (emoji.startsWith("\\u") || emoji.startsWith("\\t")) {
            return emoji;
        }

        StringBuilder result = new StringBuilder();
        for (char c : emoji.toCharArray()) {
            // 将字符转换为 Unicode 编码，并添加到结果 StringBuilder 中
            result.append("\\u").append(String.format("%04X", (int) c));
        }
        return result.toString();
    }

    public static String unicodeToEmoji(String unicode) {
        StringBuilder result = new StringBuilder();
        String[] hexArray = unicode.split("\\\\u");
        for (int i = 1; i < hexArray.length; i++) {
            int hexValue = Integer.parseInt(hexArray[i], 16);
            result.append((char) hexValue);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String emoji = "😃";
        String unicode = emojiToUnicode(emoji);
        System.out.println(unicode);
    }
}