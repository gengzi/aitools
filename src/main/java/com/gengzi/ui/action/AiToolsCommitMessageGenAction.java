package com.gengzi.ui.action;


import com.gengzi.ui.local.Constant;
import com.gengzi.ui.request.ApiRequestExample;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diff.impl.patch.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.changes.CurrentContentRevision;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.project.ProjectKt;
import com.intellij.util.ui.UIUtil;
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 根据提交文件ai动态生成提交信息
 */
public class AiToolsCommitMessageGenAction extends AnAction {
    private static final Long MAX_PATCH_LEN = 70000L;

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            return;
        }

//        // 查找 Git 提交页面的 DialogWrapper 组件
//        DialogWrapper dialogWrapper = UIUtil.getParentOfType(DialogWrapper.class, e.getInputEvent().getComponent());
//        if (dialogWrapper == null) {
//            return;
//        }
//
//        // 查找 CheckinProjectPanel 组件
//        CheckinProjectPanel checkinProjectPanel = dialogWrapper.getUserData(CheckinProjectPanel.DATA_KEY);
//        if (checkinProjectPanel == null) {
//            return;
//        }
//
//        // 生成提交日志
//        generateCommitLog(checkinProjectPanel);


        // 1,获取提交文件所有文件信息
        DataContext dataContext = e.getDataContext();
        CommitMessage commitMessage = (CommitMessage) VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(dataContext);


        // 获取diff部分内容，找的之前和之后的不同


        // 2,调用大模型获取提交内容信息
        List<String> diff = getDiff(e);
        MySettings state = MySettings.getInstance().getState();
        String apikey = state.componentStates.get(Constant.API_KEY);
        String message = "如下是本次所有提交的不同代码文件差异,换行输出git commit信息 :\n";
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        for (String s : diff) {
            sb.append("#diff:\n");
            sb.append(s + "\n");
        }


        // 3,将大模型输出输入到提交信息框中

        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiRequestExample.commitMsg(apikey, sb.toString(), project, commitMessage);
            }
        }).start();

    }


    private List<String> getDiff(AnActionEvent anActionEvent) {
        Object workflowHandler = anActionEvent.getDataContext().getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER);
        if (workflowHandler == null) {
            return new ArrayList();
        } else {
            List<Change> changeList = new ArrayList();
            if (workflowHandler instanceof AbstractCommitWorkflowHandler) {
                List<Change> includedChanges = ((AbstractCommitWorkflowHandler) workflowHandler).getUi().getIncludedChanges();
                if (CollectionUtils.isNotEmpty(includedChanges)) {
                    changeList.addAll(includedChanges);
                }

                List<FilePath> filePaths = ((AbstractCommitWorkflowHandler) anActionEvent.getDataContext().getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER)).getUi().getIncludedUnversionedFiles();
                if (CollectionUtils.isNotEmpty(filePaths)) {
                    for (FilePath filePath : filePaths) {
                        Change change = new Change((ContentRevision) null, new CurrentContentRevision(filePath));
                        changeList.add(change);
                    }
                }

                List<String> includedChangesStr = new ArrayList<>();
                AtomicLong totalLength = new AtomicLong(0L);

                for (Change change : changeList) {
                    try {
                        Boolean isValid = this.checkIfValidChange(change);
                        if (BooleanUtils.isTrue(isValid)) {
                            List<FilePatch> patches = IdeaTextPatchBuilder.buildPatch(anActionEvent.getProject(), Arrays.asList(change), Path.of(anActionEvent.getProject().getBasePath()), false, false);
                            if (CollectionUtils.isEmpty(patches)) {
                                String fileName = change.getAfterRevision() != null ? change.getAfterRevision().getFile().getName() : (change.getBeforeRevision() != null ? change.getBeforeRevision().getFile().getName() : "");
                                if (!StringUtils.isBlank(fileName)) {
                                    includedChangesStr.add(fileName + " change mod");
                                    if (totalLength.get() >= MAX_PATCH_LEN || includedChanges.size() >= 50) {
                                        break;
                                    }
                                }
                            } else {
                                Boolean isValidChange = this.checkIfChangeLengthTooLarge(patches, totalLength);
                                if (BooleanUtils.isTrue(isValidChange)) {
                                    StringWriter writer = new StringWriter();

                                    try {
                                        UnifiedDiffWriter.write(anActionEvent.getProject(), ProjectKt.getStateStore(anActionEvent.getProject()).getProjectBasePath(), patches, writer, "\n", (CommitContext) null, List.of());
                                        if (StringUtils.isNotBlank(writer.toString())) {
                                            includedChangesStr.add(writer.toString());
                                        }

                                        if (includedChangesStr.size() >= 50 || totalLength.get() >= MAX_PATCH_LEN) {
                                            break;
                                        }
                                    } finally {
                                        writer.close();
                                    }
                                }
                            }
                        }
                    } catch (VcsException e) {

                    } catch (IOException e) {

                    }
                }

                return includedChangesStr;
            } else {
                return new ArrayList();
            }
        }
    }


    private Boolean checkIfValidChange(Change change) {
        Boolean isBinary = change.getAfterRevision() != null ? change.getAfterRevision().getFile().getFileType().isBinary() : change.getBeforeRevision().getFile().getFileType().isBinary();
        if (isBinary) {
            return false;
        } else {
            ContentRevision contentRevision = change.getAfterRevision() != null ? change.getAfterRevision() : change.getBeforeRevision();
            if (contentRevision == null) {
                return false;
            } else {
                String content = null;

                try {
                    content = contentRevision.getContent();
                } catch (VcsException e) {
                }

                return StringUtils.isNotBlank(content) && !content.contains("\n") && !content.contains("\r") && content.length() > 300 ? false : true;
            }
        }
    }

    private Boolean checkIfChangeLengthTooLarge(List<FilePatch> patches, AtomicLong totalLength) {
        Long lengthOfChange = 0L;

        for (FilePatch patch : patches) {
            if (!(patch instanceof TextFilePatch)) {
                return false;
            }

            List<PatchHunk> patchHunks = ((TextFilePatch) patch).getHunks();
            if (CollectionUtils.isEmpty(patchHunks)) {
                return false;
            }

            if (patchHunks.size() == 1) {
                PatchHunk patchHunk = (PatchHunk) patchHunks.get(0);
                if (patchHunk.getLines().size() == 1 && patchHunk.getText().length() > 300) {
                    return false;
                }
            }

            for (PatchHunk patchHunk : patchHunks) {
                lengthOfChange = lengthOfChange + (long) patchHunk.getText().length();
            }
        }

        if (totalLength.get() + lengthOfChange > MAX_PATCH_LEN) {
            return false;
        } else {
            totalLength.addAndGet(lengthOfChange);
            return true;
        }
    }


    private void generateCommitLog(CheckinProjectPanel checkinProjectPanel) {
        StringBuilder commitLog = new StringBuilder();
        commitLog.append("提交日志生成：\n");

        // 获取要提交的文件列表
        Collection<VirtualFile> virtualFiles = checkinProjectPanel.getVirtualFiles();
        for (VirtualFile file : virtualFiles) {
            try (InputStream inputStream = file.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                commitLog.append("文件：").append(file.getPath()).append("\n");
                commitLog.append("内容：\n");
                while ((line = reader.readLine()) != null) {
                    commitLog.append(line).append("\n");
                }
            } catch (IOException ex) {
                commitLog.append("文件：").append(file.getPath()).append(" 读取失败：").append(ex.getMessage()).append("\n");
            }
        }

        // 将生成的提交日志设置到提交信息中
        checkinProjectPanel.setCommitMessage(commitLog.toString());
    }


}
