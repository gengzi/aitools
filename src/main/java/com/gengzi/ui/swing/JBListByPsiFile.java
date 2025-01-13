package com.gengzi.ui.swing;

import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;

public class JBListByPsiFile {

    public static void cellRendererToPsiFile(JBList jList) {
        // 设置自定义的 ListCellRenderer 来显示 Person 对象的 name 属性
        jList.setCellRenderer(new ListCellRenderer<PsiFile>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends PsiFile> list, PsiFile value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                // 创建一个简单的组件，可以是 JLabel 或其他组件
                javax.swing.JLabel label = new javax.swing.JLabel();
                if (value != null) {
                    label.setText(value.getName());
                }
                // 根据是否选中设置不同的字体样式
                if (isSelected) {
                    label.setFont(new Font("Arial", Font.BOLD, 14));
                } else {
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                }
                return label;
            }
        });
    }
}
