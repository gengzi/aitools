package com.gengzi.ui.filecode;

import com.intellij.diff.DiffContext;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.tools.fragmented.UnifiedDiffViewer;
import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnifiedDiffViewerWithButtons extends UnifiedDiffViewer {

    public UnifiedDiffViewerWithButtons(DiffContext context, DiffRequest request) {
        super(context, request);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton acceptButton = new JButton("同意");
        JButton rejectButton = new JButton("拒绝");

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理同意操作，例如应用差异
                System.out.println("点击了同意按钮");
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理拒绝操作，例如忽略差异
                System.out.println("点击了拒绝按钮");
            }
        });

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);


        getComponent().add(buttonPanel, BorderLayout.SOUTH);

//        // 将按钮面板添加到视图中
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        Content content = contentFactory.createContent(buttonPanel, "", false);
//        getContentManager().addContent(content);
    }

}