package com.gengzi.ui.action;

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

import java.util.List;

public class AitoolsEditAction extends AnAction{

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
                // 将修改后的文本替换原来选中的文本
                Application application = ApplicationManager.getApplication();

                application.runWriteAction(() -> {
                    CommandProcessor.getInstance().executeCommand(project, () -> {
                        document.replaceString(selectedTextRange.getStartOffset(), selectedTextRange.getEndOffset(), modifiedText);
                    }, "My Command", null);
                });

//                CommandProcessor.getInstance().executeCommand(project, () -> {
//                    document.replaceString(selectedTextRange.getStartOffset(), selectedTextRange.getEndOffset(), modifiedText);
//                }, "My Command", null);
            }
        }

    }








}
