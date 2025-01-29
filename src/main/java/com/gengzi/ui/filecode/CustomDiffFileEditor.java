package com.gengzi.ui.filecode;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffContext;
import com.intellij.diff.editor.DiffEditorBase;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.CheckedDisposable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.intellij.ide.SelectInManager.getProject;

// 自定义差异文件编辑器
public class CustomDiffFileEditor extends DiffEditorBase {

    public CustomDiffFileEditor(@NotNull VirtualFile file, @NotNull JComponent component, @NotNull CheckedDisposable contentDisposable, @NotNull DiffContext context) {
        super(file, component, contentDisposable, context);
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }
}