package com.gengzi.ui.filecode;

import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

public class DiffContentRemover {
    public static void removeDiffContent(SimpleDiffViewer viewer, Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        for (String toolWindowId : toolWindowManager.getToolWindowIds()) {
            ToolWindow toolWindow = toolWindowManager.getToolWindow(toolWindowId);
            if (toolWindow != null) {
                ContentManager contentManager = toolWindow.getContentManager();
                Content[] contents = contentManager.getContents();
                for (Content content : contents) {
                    if (content.getComponent() == viewer.getComponent()) {
                        contentManager.removeContent(content, true);
                        return;
                    }
                }
            }
        }
    }
}