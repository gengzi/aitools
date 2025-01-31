package com.gengzi.ui.filecode;

import com.intellij.diff.DiffContext;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.contents.FileContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.tools.fragmented.UnifiedDiffViewer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UnifiedDiffViewerWithButtons extends UnifiedDiffViewer {

    private JComponent component;

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
                Project project = context.getProject();
                if (project == null) {
                    return;
                }
                SimpleDiffRequest myRequest = (SimpleDiffRequest) request;

                List<DiffContent> contents = myRequest.getContents();
                VirtualFile targetFile;
                String modifiedContent;

                // 假设第二个内容是修改后的内容
                if (contents.size() >= 2) {
                    DiffContent originalContent = contents.get(0);
                    DiffContent modifiedDiffContent = contents.get(1);

                    // 尝试从原始内容中获取 VirtualFile
                    if (originalContent instanceof FileContent) {
                        targetFile = ((FileContent) originalContent).getFile();
                    } else {
                        targetFile = null;
                    }


                    // 获取修改后的内容
                        modifiedContent =  ((DocumentContent) modifiedDiffContent).getDocument().getText();

                } else {
                   throw new RuntimeException("目标文件不存在");
                }

                if (targetFile != null && modifiedContent != null) {
                    // 在写入安全的上下文中执行文件覆盖操作
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            // 获取文件文档
                            Document doc = FileDocumentManager.getInstance().getDocument(targetFile);
                            if (doc != null) {
                                // 将修改后的内容写入文档
                                doc.setText(modifiedContent);
                                // 保存文档更改
                                FileDocumentManager.getInstance().saveDocument(doc);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }


                Window window = SwingUtilities.getWindowAncestor(component);
                if (window != null) {
                    window.dispose();
                }

//                document.setText(modifiedContent);
//                FileDocumentManager.getInstance().saveDocument(document);


            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理拒绝操作，例如忽略差异
                System.out.println("点击了拒绝按钮");


                Window window = SwingUtilities.getWindowAncestor(component);
                if (window != null) {
                    window.dispose();
                }






            }
        });

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        getComponent().add(buttonPanel, BorderLayout.SOUTH);

        component = this.getComponent();

//        // 将按钮面板添加到视图中
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        Content content = contentFactory.createContent(buttonPanel, "", false);
//        getContentManager().addContent(content);
    }

}