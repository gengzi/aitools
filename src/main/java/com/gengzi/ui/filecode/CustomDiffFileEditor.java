package com.gengzi.ui.filecode;

import com.intellij.diff.DiffContext;
import com.intellij.diff.editor.DiffEditorBase;
import com.intellij.openapi.util.CheckedDisposable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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