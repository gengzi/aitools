//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gengzi.ui.markdown;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import javax.swing.JViewport;

public interface MarkdownComponent {
    void updateText(String var1, String var2, boolean var3);

    Component getComponent();

    default int handleTextDragEvent(Project project, MouseEvent e, int lastMouseY, JBScrollPane scrollPane) {
        if (scrollPane == null) {
            return lastMouseY;
        } else {
            JViewport viewport = scrollPane.getViewport();
            JTextPane textPane = (JTextPane)e.getComponent();
            Rectangle viewRect = viewport.getViewRect();
            if (e.getX() >= 0 && e.getY() >= 0 && e.getY() <= textPane.getHeight()) {
                Point viewPosition = viewport.getViewPosition();
                int maxScrollAmount = 10;
                int scrollAmount = e.getY() - lastMouseY;
                lastMouseY = e.getY();
                if (scrollAmount > maxScrollAmount) {
                    e.translatePoint(0, maxScrollAmount - scrollAmount);
                    scrollAmount = maxScrollAmount;
                } else if (scrollAmount < -maxScrollAmount) {
                    e.translatePoint(0, scrollAmount + maxScrollAmount);
                    scrollAmount = -maxScrollAmount;
                }

                int newY = viewPosition.y + scrollAmount;
                viewport.setViewPosition(new Point(viewRect.x, newY));
                return lastMouseY;
            } else {
                lastMouseY = e.getY();
                return lastMouseY;
            }
        }
    }
}
