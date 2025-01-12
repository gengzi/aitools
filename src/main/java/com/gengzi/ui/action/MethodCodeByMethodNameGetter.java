package com.gengzi.ui.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;

import java.util.Arrays;
import java.util.Optional;

public class MethodCodeByMethodNameGetter {
    public static Optional<PsiElement> findMethodByName(AnActionEvent actionEvent, String methodName) {
        Project project = actionEvent.getProject();
        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return Optional.empty();
        }
        Document document = editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        if (virtualFile == null) {
            return Optional.empty();
        }
        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile == null) {
            return Optional.empty();
        }



//        // 这里可以通过不同方式查找方法元素，比如遍历文件中的所有方法等
//        return Arrays.stream(psiFile.getChildren())
//               .filter(PsiElement::isValid)
//               .filter(element -> element instanceof PsiMethod && ((PsiMethod) element).getName().equals(methodName))
//               .findFirst();
        return null;
    }

//    public static String getMethodCode(Optional<PsiElement> methodElementOpt) {
//        if (methodElementOpt.isEmpty()) {
//            return null;
//        }
//        PsiElement methodElement = methodElementOpt.get();
//        if (!(methodElement instanceof PsiMethod)) {
//            return null;
//        }
//        return methodElement.getText();
//    }
}