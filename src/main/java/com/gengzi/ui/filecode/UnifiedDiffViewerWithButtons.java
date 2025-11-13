package com.gengzi.ui.filecode;

import com.intellij.diff.DiffContext;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.contents.FileContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * UnifiedDiffViewerWithButtons
 *
 * @author gengzi
 */
public class UnifiedDiffViewerWithButtons extends SimpleDiffViewer implements Disposable {

    private JComponent component;
    private UnifiedDiffViewerWithButtons unifiedDiffViewerWithButtons;
    private JBPopup diffPopup;

    public UnifiedDiffViewerWithButtons(DiffContext context, DiffRequest request) {
        super(context, request);
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton acceptButton = new JButton("同意");
        JButton rejectButton = new JButton("拒绝");


        unifiedDiffViewerWithButtons = this;

        diffPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(getComponent(), null)
                .setNormalWindowLevel(true)
                .setCancelOnClickOutside(false)
                .setRequestFocus(false)
                .setFocusable(true)
                .setMovable(true)
                .setResizable(true)
                .setShowBorder(true)
                .setCancelKeyEnabled(true)
                .setCancelOnWindowDeactivation(false)
                .setCancelOnOtherWindowOpen(false)
                .createPopup();

        diffPopup.showInScreenCoordinates(getComponent(), new Point(100, 100)); // 示例显示位置
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
                    modifiedContent = ((DocumentContent) modifiedDiffContent).getDocument().getText();

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

                diffPopup.dispose();
                Disposer.dispose(unifiedDiffViewerWithButtons);


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

        component = this.getComponent();

//        // 将按钮面板添加到视图中
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        Content content = contentFactory.createContent(buttonPanel, "", false);
//        getContentManager().addContent(content);
    }




}