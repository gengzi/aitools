package com.gengzi.ui.extensions;

import com.gengzi.ui.filecode.UnifiedDiffViewerWithButtons;
import com.intellij.diff.*;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffTool;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.diff.tools.util.base.DiffViewerBase;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

// 自定义 FrameDiffTool
public class CustomFrameDiffTool implements FrameDiffTool, SuppressiveDiffTool {

    @Override
    public @NotNull DiffViewer createComponent(@NotNull DiffContext context, com.intellij.diff.requests.@NotNull DiffRequest request) {
        DiffViewer viewer = new UnifiedDiffViewerWithButtons(context,request);
        return viewer;
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getName() {
        return SimpleDiffTool.INSTANCE.getName();
    }

    @Override
    public boolean canShow(@NotNull DiffContext context, com.intellij.diff.requests.@NotNull DiffRequest request) {
        return true;
    }

    @Override
    public List<Class<? extends DiffTool>> getSuppressedTools() {
        return Collections.<Class<? extends DiffTool>>singletonList(SimpleDiffTool.class);
    }
}