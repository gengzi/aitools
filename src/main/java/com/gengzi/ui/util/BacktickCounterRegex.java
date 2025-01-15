package com.gengzi.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BacktickCounterRegex {
    public static int countBackticksWithRegex(String input) {
        // 定义正则表达式，匹配 ```
        String regex = "```";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        int count = 0;
        // 循环查找匹配项
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        String str1 = "This is a string ``` with some ``` backticks.";
        String str2 = "This is ``` a string ``` without ``` any ``` backticks.";
        String str3 = "No backticks here.";

        System.out.println(countBackticksWithRegex(str1)); // 输出 2
        System.out.println(countBackticksWithRegex(str2)); // 输出 4
        System.out.println(countBackticksWithRegex(str3)); // 输出 0
    }
}