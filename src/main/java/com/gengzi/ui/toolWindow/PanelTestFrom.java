package com.gengzi.ui.toolWindow;

import com.gengzi.ui.filecode.UnifiedDiffViewerWithButtons;
import com.intellij.diff.*;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.editor.DiffEditorTabFilesManager;
import com.intellij.diff.editor.DiffVirtualFile;
import com.intellij.diff.editor.SimpleDiffVirtualFile;
import com.intellij.diff.impl.DiffRequestProcessor;
import com.intellij.diff.impl.DiffWindow;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.tools.fragmented.UnifiedDiffTool;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.diff.tools.util.base.DiffViewerBase;
import com.intellij.diff.util.DiffUtil;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.ide.util.gotoByName.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.WindowWrapper;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vcs.history.VcsDiffUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.Consumer;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PanelTestFrom implements Disposable {
    private JPanel panel;
    private JBScrollPane sc;
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JTextArea textArea1;
    private JButton diffbtn;
    private Project project;
    private ChooseByNamePanel chooseByNamePanel;

    private final @Nullable FileType myFileType;

//    private Project project;

    public JPanel getPanel() {
        return panel;
    }

    public PanelTestFrom(Project project) {
        this.project = project;
        myFileType = null;
//        this.project = project;
//        chooseByNamePanel =
//                new ChooseByNamePanel(project, new MyGotoFileModel(), "", true, null){
//
//
//
//
//
//
//                    @Override
//                    protected void initUI(ChooseByNamePopupComponent.Callback callback, ModalityState modalityState, boolean allowMultipleSelection) {
//                        super.initUI(callback, modalityState, allowMultipleSelection);
//                        panel.add(chooseByNamePanel.getPanel(), BorderLayout.CENTER);
//                    }
//
//                    @Override
//                    protected void showTextFieldPanel() {
//                        System.out.println("重写");
//                        //super.showTextFieldPanel();
//                    }
//
//                    @Override
//                    protected @NotNull List<Object> getChosenElements() {
//                        List<Object> chosenElements = super.getChosenElements();
//                        return chosenElements;
//                    }
//
//                    @Override
//                    public @Nullable Object getChosenElement() {
//                        Object chosenElement = super.getChosenElement();
//                        if (chosenElement != null) {
//                            System.out.println(chosenElement.toString());
//                        }
//
//                        return super.getChosenElement();
//                    }
//                };
////        chooseByNamePanel.getPanel().setVisible(true);
////        panel.setPreferredSize(new Dimension(500, 500));
//        SwingUtilities.invokeLater(() -> chooseByNamePanel.invoke(new MyCallback(), ModalityState.stateForComponent(panel), true));
////
//
//
//
//        Object chosenElement = chooseByNamePanel.getChosenElement();



//        JComponent preferredFocusedComponent = chooseByNamePanel.getPreferredFocusedComponent();
//        panel.add(preferredFocusedComponent, BorderLayout.CENTER);
//        panel.add(chooseByNamePanel.getPanel(), BorderLayout.EAST);
        diffbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
//                // 创建差异内容
//                DiffContentFactory contentFactory = DiffContentFactory.getInstance();
//                LightVirtualFile modifiedVirtualFileOld  = new LightVirtualFile("xx.java", "//TIP 要<b>运行</b>代码，请按 <shortcut actionId=\"Run\"/> 或\n" +
//                        "// 点击装订区域中的 <icon src=\"AllIcons.Actions.Execute\"/> 图标。\n" +
//                        "public class Main {\n" +
//                        "    public static void main(String[] args) {\n" +
//                        "        //TIP 当文本光标位于高亮显示的文本处时按 <shortcut actionId=\"ShowIntentionActions\"/>\n" +
//                        "        // 查看 IntelliJ IDEA 建议如何修正。\n" +
//                        "        System.out.printf(\"Hello and welcome!\");\n" +
//                        "\n" +
//                        "    }\n" +
//                        "}");
//                // 原始文件内容
//                var originalContent = contentFactory.create(project, modifiedVirtualFileOld);
//                // 修改后的文件内容
//                LightVirtualFile modifiedVirtualFile = new LightVirtualFile("xx.java", "//TIP 要<b>运行</b>代码，请按 <shortcut actionId=\"Run\"/> 或\n" +
//                        "// 点击装订区域中的 <icon src=\"AllIcons.Actions.Execute\"/> 图标。\n" +
//                        "public class Main {\n" +
//                        "    public static void main(String[] args) {\n" +
//                        "        //TIP 当文本光标位于高亮显示的文本处时按 <shortcut actionId=\"ShowIntentionActions\"/>\n" +
//                        "        // 查看 IntelliJ IDEA 建议如何修正。\n" +
//                        "        System.out.printf(\"Hello and welcome!\");\n" +
//                        "\n" +
//                        "        for (int i = 1; i <= 5; i++) {\n" +
//                        "            //TIP 按 <shortcut actionId=\"Debug\"/> 开始调试代码。我们已经设置了一个 <icon src=\"AllIcons.Debugger.Db_set_breakpoint\"/> 断点\n" +
//                        "            // 但您始终可以通过按 <shortcut actionId=\"ToggleLineBreakpoint\"/> 添加更多断点。\n" +
//                        "            System.out.println(\"i = \" + i);\n" +
//                        "        }\n" +
//                        "    }\n" +
//                        "}");
//                var modifiedContentObj = contentFactory.create(project, modifiedVirtualFile);
//
//                // 创建差异请求
//                DiffRequest diffRequest = new SimpleDiffRequest(
//                        "代码对比差异",
//                        originalContent,
//                        modifiedContentObj,
//                        "Original",
//                        "Modified"
//                );
//
//
//
                // 创建 Diff 内容
                DiffContentFactory contentFactory = DiffContentFactory.getInstance();
                DiffContent content1 = contentFactory.create("Original text");
                DiffContent content2 = contentFactory.create("Modified text");

                // 创建 Diff 请求
                DiffRequest request = new SimpleDiffRequest("Diff Example", content1, content2, "Left", "Right");
//                // 创建一个 diff窗口
                UnifiedDiffViewerWithButtons diffViewer = new UnifiedDiffViewerWithButtons(new DiffContext() {
                    @Override
                    public @Nullable Project getProject() {
                        return project;
                    }

                    @Override
                    public boolean isWindowFocused() {
                        return false;
                    }

                    @Override
                    public boolean isFocusedInWindow() {
                        return false;
                    }

                    @Override
                    public void requestFocusInWindow() {

                    }
                }, request);
//
//
//
////                DiffManager diffManager = ServiceManager.getService(DiffManager.class);
////                diffManager.showDiff(project, diffViewer.getRequest());
//
//
////                DiffVirtualFile diffFile = new DiffVirtualFile("diff.txt") {
////                    @Override
////                    public @NotNull DiffRequestProcessor createProcessor(@NotNull Project project) {
////                        return null;
////                    }
////                };
//
//
//                SimpleDiffVirtualFile simpleDiffVirtualFile = new SimpleDiffVirtualFile(diffRequest);
//
//
//                DiffEditorTabFilesManager.getInstance(project).showDiffFile(simpleDiffVirtualFile,true);
//
//                DiffManager.getInstance().showDiff();
//                FileEditorManager.getInstance(project).openFile(diffFile, true);

//                diffViewer.rediff();


//                DiffManager.getInstance()




//
//                // 获取 Diff 工具并创建 DiffViewer
//                DiffManager diffManager = DiffManager.getInstance();
//                UnifiedDiffTool unifiedDiffTool = new UnifiedDiffTool();
//                FrameDiffTool.DiffViewer diffViewer1 = unifiedDiffTool.createComponent(diffViewer.getContext(), request);
//
//
//
//                if (diffViewer1 instanceof DiffViewerBase) {
//                    DiffViewerBase baseViewer = (DiffViewerBase) diffViewer1;
//                    // 获取 Diff 窗口的组件
//                    JComponent component = baseViewer.getComponent();
//
//                    // 创建同意和拒绝按钮
//                    JButton agreeButton = new JButton("同意");
//                    JButton rejectButton = new JButton("拒绝");
//
//                    // 为按钮添加点击事件
//                    agreeButton.addActionListener(event -> {
//                        // 处理同意操作
//                        System.out.println("用户点击了同意按钮");
//                    });
//                    rejectButton.addActionListener(event -> {
//                        // 处理拒绝操作
//                        System.out.println("用户点击了拒绝按钮");
//                    });
//
//                    // 创建一个面板来放置按钮
//                    JPanel buttonPanel = new JPanel();
//                    buttonPanel.setLayout(new FlowLayout());
//                    buttonPanel.add(agreeButton);
//                    buttonPanel.add(rejectButton);
//
//                    // 将按钮面板添加到 Diff 窗口的底部
//                    component.add(buttonPanel, BorderLayout.SOUTH);
//                    component.revalidate();
//                    component.repaint();
//                }
//
//                // 将 DiffViewer 集成到当前编辑窗口的工具窗口中
//                ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//                String toolWindowId = "DiffViewerToolWindow";
//                ToolWindow toolWindow = toolWindowManager.getToolWindow(toolWindowId);
//                if (toolWindow == null) {
//                    toolWindow = toolWindowManager.registerToolWindow(toolWindowId, false, ToolWindowAnchor.BOTTOM);
//                }
//
//                ContentFactory contentFactoryInstance = ContentFactory.getInstance();
//                Content content = contentFactoryInstance.createContent( diffViewer.getComponent(), "", false);
//                toolWindow.getContentManager().removeAllContents(true);
//                toolWindow.getContentManager().addContent(content);
//                toolWindow.show();


                // 创建 Diff 上下文
//                DiffContext context = new DiffContext(project);

                // 创建自定义的 DiffViewerProvider
//                DiffViewerProvider viewerProvider = viewer -> {
//                    if (viewer instanceof DiffViewerBase) {
//                        DiffViewerBase baseViewer = (DiffViewerBase) viewer;
//                        // 获取 Diff 窗口的根组件
//                        JComponent rootComponent = baseViewer.getComponent();
//
//                        // 创建同意和拒绝按钮
//                        JBButton agreeButton = new JBButton("同意");
//                        JBButton rejectButton = new JBButton("拒绝");
//
//                        // 为按钮添加点击事件
//                        agreeButton.addActionListener(event -> {
//                            // 处理同意操作
//                            System.out.println("用户点击了同意按钮");
//                        });
//                        rejectButton.addActionListener(event -> {
//                            // 处理拒绝操作
//                            System.out.println("用户点击了拒绝按钮");
//                        });
//
//                        // 创建一个面板来放置按钮
//                        JPanel buttonPanel = new JPanel();
//                        buttonPanel.setLayout(new FlowLayout());
//                        buttonPanel.add(agreeButton);
//                        buttonPanel.add(rejectButton);
//
//                        // 将按钮面板添加到 Diff 窗口的底部
//                        rootComponent.add(buttonPanel, BorderLayout.SOUTH);
//                        rootComponent.revalidate();
//                        rootComponent.repaint();
//                    }
//                    return viewer;
//                };

                // 创建同意和拒绝按钮
                JButton agreeButton = new JButton("同意");
                JButton rejectButton = new JButton("拒绝");

                // 为按钮添加点击事件
                agreeButton.addActionListener(event -> {
                    // 处理同意操作
                    System.out.println("用户点击了同意按钮");
                });
                rejectButton.addActionListener(event -> {
                    // 处理拒绝操作
                    System.out.println("用户点击了拒绝按钮");
                });

                // 创建一个面板来放置按钮
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                buttonPanel.add(agreeButton);
                buttonPanel.add(rejectButton);

                // 定义对窗口的自定义操作
                Consumer<WindowWrapper> windowConsumer = wrapper -> {
                    Window window = wrapper.getWindow();
                    if (window instanceof JFrame) {
                        JFrame dialog = (JFrame) window;
                        // 获取对话框的内容面板
                        Container contentPane = dialog.getContentPane();
                        // 将按钮面板添加到内容面板的底部
                        contentPane.add(buttonPanel, BorderLayout.SOUTH);
                        // 重新验证和重绘面板以更新布局
                        contentPane.revalidate();
                        contentPane.repaint();
                    }
                };

                // 创建带有窗口自定义操作的 DiffDialogHints
                DiffDialogHints hints = new DiffDialogHints(DiffDialogHints.DEFAULT.getMode(), null, windowConsumer);


                // 创建带有自定义 DiffViewerProvider 的 DiffDialogHints
//                DiffDialogHints hints = DiffDialogHints.DEFAULT;
                DiffManager instance = DiffManager.getInstance();
                JFrame jFrame = new JFrame();
                Component add = jFrame.add(diffViewer.getComponent());
//                DiffRequestPanel requestPanel = instance.createRequestPanel(project, project, jFrame);



                // 展示 Diff 窗口
//                DiffManager.getInstance().showDiff(project, request, hints);
                DiffManager.getInstance().showDiff(project, request);







            }
        });
    }

    @Override
    public void dispose() {

    }

    private final class MyCallback extends ChooseByNamePopupComponent.Callback {

        @Override
        public void elementChosen(final Object element) {
            System.out.println("1"+element.toString());
        }


    }


    private final class MyGotoFileModel implements ChooseByNameModel, DumbAware {
        private final int myMaxSize = WindowManagerEx.getInstanceEx().getFrame(project).getSize().width;

        @Override
        public Object @NotNull [] getElementsByName(final @NotNull String name, final boolean checkBoxState, final @NotNull String pattern) {
            GlobalSearchScope scope = true ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
            final PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, name, scope);
            return filterFiles(psiFiles);
        }

        @Override
        public String getPromptText() {
            return IdeBundle.message("prompt.filechooser.enter.file.name");
        }

        @Override
        public String getCheckBoxName() {
            return null;
        }


        @Override
        public @NotNull String getNotInMessage() {
            return "";
        }

        @Override
        public @NotNull String getNotFoundMessage() {
            return "";
        }

        @Override
        public boolean loadInitialCheckBoxState() {
            return true;
        }

        @Override
        public void saveInitialCheckBoxState(final boolean state) {
        }

        @Override
        public @NotNull PsiElementListCellRenderer getListCellRenderer() {
            return new GotoFileCellRenderer(myMaxSize);
        }

        @Override
        public String @NotNull [] getNames(final boolean checkBoxState) {
            final String[] fileNames;
            if (myFileType != null) {
                GlobalSearchScope scope = true ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
                Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(myFileType, scope);
                fileNames = ContainerUtil.map2Array(virtualFiles, String.class, file -> file.getName());
            }
            else {
                fileNames = FilenameIndex.getAllFilenames(project);
            }
            Set<String> array = new HashSet<>(fileNames.length);
            Collections.addAll(array, fileNames);
            String[] result = ArrayUtilRt.toStringArray(array);
            Arrays.sort(result);
            return result;
        }

        @Override
        public boolean willOpenEditor() {
            return true;
        }

        @Override
        public String getElementName(final @NotNull Object element) {
            if (!(element instanceof PsiFile)) return null;
            return ((PsiFile)element).getName();
        }

        @Override
        public @Nullable String getFullName(final @NotNull Object element) {
            if (element instanceof PsiFile) {
                final VirtualFile virtualFile = ((PsiFile)element).getVirtualFile();
                return virtualFile != null ? virtualFile.getPath() : null;
            }

            return getElementName(element);
        }

        @Override
        public String getHelpId() {
            return null;
        }

        @Override
        public String @NotNull [] getSeparators() {
            return new String[] {"/", "\\"};
        }

        @Override
        public boolean useMiddleMatching() {
            return false;
        }


    }


    private Object[] filterFiles(final Object[] list) {
        Condition<PsiFile> condition = psiFile -> {
//            if (myFilter != null && !myFilter.accept(psiFile)) {
//                return false;
//            }
            boolean accepted = myFileType == null || psiFile.getFileType() == myFileType;
            VirtualFile virtualFile = psiFile.getVirtualFile();
            if (virtualFile != null && !accepted) {
                accepted = FileTypeRegistry.getInstance().isFileOfType(virtualFile, myFileType);
            }
            return accepted;
        };
        final List<Object> result = new ArrayList<>(list.length);
        for (Object o : list) {
            final PsiFile psiFile;
            if (o instanceof PsiFile) {
                psiFile = (PsiFile)o;
            }
            else if (o instanceof PsiFileNode) {
                psiFile = ((PsiFileNode)o).getValue();
            }
            else {
                psiFile = null;
            }
            if (psiFile != null && !condition.value(psiFile)) {
                continue;
            }
            else {
                if (o instanceof ProjectViewNode projectViewNode) {
                    if (!projectViewNode.canHaveChildrenMatching(condition)) {
                        continue;
                    }
                }
            }
            result.add(o);
        }
        return ArrayUtil.toObjectArray(result);
    }
}
