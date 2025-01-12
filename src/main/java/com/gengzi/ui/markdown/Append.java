package com.gengzi.ui.markdown;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class Append {

    private void appendContent(JBCefBrowser browser, String currentHtml,  String newContent) {
        // 获取当前页面已有的HTML内容
        // 拼接新内容到合适位置，这里简单追加到<body>标签内末尾，实际可能需更精细处理
        String updatedHtml = appendToBody(currentHtml, newContent);
        browser.loadHTML(updatedHtml);
    }

    private String appendToBody(String html, String newContent) {
        int bodyIndex = html.indexOf("<body>");
        if (bodyIndex!= -1) {
            int bodyEndIndex = html.indexOf("</body>", bodyIndex);
            if (bodyEndIndex!= -1) {
                String beforeBodyEnd = html.substring(0, bodyEndIndex);
                String afterBodyEnd = html.substring(bodyEndIndex);
                return beforeBodyEnd + "<p>" + newContent + "</p>" + afterBodyEnd;
            }
        }
        return html;
    }
}