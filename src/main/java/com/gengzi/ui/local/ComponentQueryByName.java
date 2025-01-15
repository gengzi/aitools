package com.gengzi.ui.local;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;


import javax.swing.*;
import java.awt.*;


public class ComponentQueryByName {
    public static Component findComponentByName(Project project, String toolWindowId, String componentName) {
        // 获取 ToolWindowManager
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        // 获取 ToolWindow
        ToolWindow toolWindow = toolWindowManager.getToolWindow(toolWindowId);
        if (toolWindow == null) {
            return null;
        }

        // 获取 ContentManager
        ContentManager contentManager = toolWindow.getContentManager();
        if (contentManager == null) {
            return null;
        }


        // 遍历 ContentManager 中的 Content
        for (int i = 0; i < contentManager.getContentCount(); i++) {
            Content content = contentManager.getContent(i);
            if (content!= null) {
                Component component = findComponentByNameRecursive(content.getComponent(), componentName);
                if (component!= null) {
                    return component;
                }
            }
        }


        return null;
    }


    private static Component findComponentByNameRecursive(Component component, String componentName) {
        if (component!= null && component.getName()!= null && component.getName().equals(componentName)) {
            return component;
        }


        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (Component child : children) {
                Component foundComponent = findComponentByNameRecursive(child, componentName);
                if (foundComponent!= null) {
                    return foundComponent;
                }
            }
        }


        return null;
    }
}