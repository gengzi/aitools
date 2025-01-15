package com.gengzi.ui.action;

import com.gengzi.ui.local.ComponentQueryByName;
import com.gengzi.ui.local.Constant;
import com.gengzi.ui.openai.Curl;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * 解释方法描述
 * @author gengzi
 */
public class MethodDescAction extends AnAction {

//
//
//    public static @Nullable Editor getSelectedEditorSafely(@NotNull Project project) {
//        try {
//            if (SwingUtilities.isEventDispatchThread()) {
//                FileEditorManager editorManager = FileEditorManager.getInstance(project);
//                return editorManager != null ? editorManager.getSelectedTextEditor() : null;
//            } else {
//                AtomicReference<Editor> editor = new AtomicReference();
//                SwingUtilities.invokeAndWait(() -> {
//                    try {
//                        FileEditorManager editorManager = FileEditorManager.getInstance(project);
//                        editor.set(editorManager != null ? editorManager.getSelectedTextEditor() : null);
//                    } catch (Exception e1) {
////                        LOGGER.warn("Error to get editor", e1);
//                        System.out.println(e1);
//                    }
//
//                });
//                return (Editor)editor.get();
//            }
//        } catch (Exception e) {
////            LOGGER.warn("Error to get editor first time", e);
//            return null;
//        }
//    }


    @Override
    public void actionPerformed(AnActionEvent e) {

        DataContext dataContext = e.getDataContext();
        System.out.println("dataContext = " + dataContext);
        String text = e.getPresentation().getText();
        System.out.println("text = " + text);


        // 获取当前项目实例
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 获取编辑器实例
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        StringBuilder codeSb = new StringBuilder();
        // 获取文档实例，文档包含了编辑器中的所有文本内容
        Document document = editor.getDocument();
        // 获取所有的光标位置（通常在有多个选中区域时会有多个光标）
        List<Caret> allCarets = editor.getCaretModel().getAllCarets();
        for (Caret caret : allCarets) {
            // 获取当前光标下的选中区域范围
            TextRange selectedTextRange = caret.getSelectionRange();
            if (!selectedTextRange.isEmpty()) {
                // 获取选中的原始文本
                String selectedText = document.getText(selectedTextRange);
                // 在这里对选中的文本进行修改，例如将其转换为大写
                String modifiedText = selectedText.toUpperCase();
                codeSb.append(modifiedText);
            }
        }

        // 获取 ToolWindowManager
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        // 查找你想要的 ToolWindow，这里假设 ToolWindow 的 ID 是 "MyToolWindow"
        ToolWindow toolWindow = toolWindowManager.getToolWindow("AiTools");
        if (toolWindow == null) {
            return;
        }

        // 显示 ToolWindow
        toolWindow.show();
        // 获取 ToolWindow 的内容管理器
        ContentManager contentManager = toolWindow.getContentManager();
        if (contentManager == null) {
            return;
        }

        Component componentByName = ComponentQueryByName.findComponentByName(project, "AiTools", "aiToolsInputArea");
        if(componentByName instanceof JTextArea){
            JTextArea jTextArea = (JTextArea) componentByName;
            StringBuilder sb = new StringBuilder();
            sb.append("解释如下这段代码逻辑:\n");
            sb.append(codeSb.toString());
            jTextArea.setText(sb.toString());

            // 手动触发 KeyListener
            KeyEvent keyEvent = new KeyEvent(jTextArea, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
            for (KeyListener listener : jTextArea.getKeyListeners()) {
                listener.keyPressed(keyEvent);
            }
        }
    }
}
