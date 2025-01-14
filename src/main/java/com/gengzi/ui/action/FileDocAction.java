package com.gengzi.ui.action;

import com.gengzi.ui.local.Constant;
import com.gengzi.ui.openai.Curl;
import com.gengzi.ui.request.ApiRequestExample;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * 解释文件内容，生成文档信息
 *
 * @author gengzi
 */
public class FileDocAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
//        DataContext dataContext = e.getDataContext();
//        System.out.println("dataContext = " + dataContext);
//        String text = e.getPresentation().getText();
//        System.out.println("text = " + text);
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
        // 2. 获取当前文件的 VirtualFile
        VirtualFile virtualFile = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) {
            return;
        }
        // 3. 获取 PsiFile 对象
        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile == null) {
            return;
        }
        // 获取文档实例，文档包含了编辑器中的所有文本内容
        Document document = editor.getDocument();
        String text = document.getText();

        FileType fileType1 = psiFile.getFileType();
        String name = fileType1.getName();
        String text1 = psiFile.getText();

        MySettings state = MySettings.getInstance().getState();
        String apikey = state.componentStates.get(Constant.API_KEY);
        String message = "根据以下代码文件内容，生成文档注释:\n";
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append("#file:\n");
        sb.append(text1 + "\n");

        // 请求大模型获取文档描述

        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiRequestExample.fileDoc(apikey, sb.toString(), project, editor);
            }
        }).start();


//        // 写入当前位置
//
//        // 3. 获取 CaretModel 并确定当前 Caret 位置
//        CaretModel caretModel = editor.getCaretModel();
//        int offset = caretModel.getOffset();
//        // 5. 插入文本内容
//        String textToInsert = "这是要插入的内容";
//        document.insertString(offset, textToInsert);

    }
}
