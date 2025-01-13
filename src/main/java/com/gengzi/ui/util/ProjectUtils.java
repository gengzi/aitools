package com.gengzi.ui.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public class ProjectUtils {


    /**
     * 获取这个工程的所有文件信息
     *
     * @param project
     * @return
     */
    public static List<VirtualFile> getProjectAllFiles(Project project) {
        VirtualFile baseDir = project.getBaseDir();
        List<VirtualFile> allFiles = new ArrayList<>();
        collectFiles(baseDir, allFiles);
        return allFiles;
    }

    private static void collectFiles(VirtualFile virtualFile, List<VirtualFile> allFiles) {
        if (virtualFile.isDirectory()) {
            VirtualFile[] children = virtualFile.getChildren();
            for (VirtualFile child : children) {
                collectFiles(child, allFiles);
            }
        } else {
            allFiles.add(virtualFile);
        }
    }


}
