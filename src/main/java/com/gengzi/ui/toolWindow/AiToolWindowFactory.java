
package com.gengzi.ui.toolWindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Objects;

/**
 * Ai对话窗口
 */
final class AiToolWindowFactory implements ToolWindowFactory, DumbAware {


  /**
   * 点击后调用
   *
   * @param project
   * @param toolWindow
   */
  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    ToolFrom toolWindowContent = new ToolFrom(project);
    Content content = ContentFactory.getInstance().createContent(toolWindowContent.getPanel2(), "", false);
    toolWindow.getContentManager().addContent(content);
  }

}