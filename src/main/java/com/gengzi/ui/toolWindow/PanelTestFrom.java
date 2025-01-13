//package com.gengzi.ui.toolWindow;
//
//import com.intellij.ide.IdeBundle;
//import com.intellij.ide.projectView.ProjectViewNode;
//import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
//import com.intellij.ide.util.PsiElementListCellRenderer;
//import com.intellij.ide.util.TreeFileChooser;
//import com.intellij.ide.util.TreeFileChooserDialog;
//import com.intellij.ide.util.gotoByName.*;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.application.ModalityState;
//import com.intellij.openapi.fileTypes.FileType;
//import com.intellij.openapi.fileTypes.FileTypeRegistry;
//import com.intellij.openapi.project.DumbAware;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Condition;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.openapi.wm.ToolWindowEP;
//import com.intellij.openapi.wm.ex.WindowManagerEx;
//import com.intellij.psi.PsiFile;
//import com.intellij.psi.search.FileTypeIndex;
//import com.intellij.psi.search.FilenameIndex;
//import com.intellij.psi.search.GlobalSearchScope;
//import com.intellij.util.ArrayUtil;
//import com.intellij.util.ArrayUtilRt;
//import com.intellij.util.containers.ContainerUtil;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.util.*;
//import java.util.List;
//
//public class PanelTestFrom {
//    private JPanel panel;
//    private Project project;
//    private ChooseByNamePanel chooseByNamePanel;
//
//    private final @Nullable FileType myFileType;
//
//    public JPanel getPanel() {
//        return panel;
//    }
//
//    public PanelTestFrom(Project project) {
//        myFileType = null;
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
//
//
//
////        JComponent preferredFocusedComponent = chooseByNamePanel.getPreferredFocusedComponent();
////        panel.add(preferredFocusedComponent, BorderLayout.CENTER);
////        panel.add(chooseByNamePanel.getPanel(), BorderLayout.EAST);
//    }
//
//    private final class MyCallback extends ChooseByNamePopupComponent.Callback {
//
//        @Override
//        public void elementChosen(final Object element) {
//            System.out.println("1"+element.toString());
//        }
//
//
//    }
//
//
//    private final class MyGotoFileModel implements ChooseByNameModel, DumbAware {
//        private final int myMaxSize = WindowManagerEx.getInstanceEx().getFrame(project).getSize().width;
//
//        @Override
//        public Object @NotNull [] getElementsByName(final @NotNull String name, final boolean checkBoxState, final @NotNull String pattern) {
//            GlobalSearchScope scope = true ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
//            final PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, name, scope);
//            return filterFiles(psiFiles);
//        }
//
//        @Override
//        public String getPromptText() {
//            return IdeBundle.message("prompt.filechooser.enter.file.name");
//        }
//
//        @Override
//        public String getCheckBoxName() {
//            return null;
//        }
//
//
//        @Override
//        public @NotNull String getNotInMessage() {
//            return "";
//        }
//
//        @Override
//        public @NotNull String getNotFoundMessage() {
//            return "";
//        }
//
//        @Override
//        public boolean loadInitialCheckBoxState() {
//            return true;
//        }
//
//        @Override
//        public void saveInitialCheckBoxState(final boolean state) {
//        }
//
//        @Override
//        public @NotNull PsiElementListCellRenderer getListCellRenderer() {
//            return new GotoFileCellRenderer(myMaxSize);
//        }
//
//        @Override
//        public String @NotNull [] getNames(final boolean checkBoxState) {
//            final String[] fileNames;
//            if (myFileType != null) {
//                GlobalSearchScope scope = true ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
//                Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(myFileType, scope);
//                fileNames = ContainerUtil.map2Array(virtualFiles, String.class, file -> file.getName());
//            }
//            else {
//                fileNames = FilenameIndex.getAllFilenames(project);
//            }
//            Set<String> array = new HashSet<>(fileNames.length);
//            Collections.addAll(array, fileNames);
//            String[] result = ArrayUtilRt.toStringArray(array);
//            Arrays.sort(result);
//            return result;
//        }
//
//        @Override
//        public boolean willOpenEditor() {
//            return true;
//        }
//
//        @Override
//        public String getElementName(final @NotNull Object element) {
//            if (!(element instanceof PsiFile)) return null;
//            return ((PsiFile)element).getName();
//        }
//
//        @Override
//        public @Nullable String getFullName(final @NotNull Object element) {
//            if (element instanceof PsiFile) {
//                final VirtualFile virtualFile = ((PsiFile)element).getVirtualFile();
//                return virtualFile != null ? virtualFile.getPath() : null;
//            }
//
//            return getElementName(element);
//        }
//
//        @Override
//        public String getHelpId() {
//            return null;
//        }
//
//        @Override
//        public String @NotNull [] getSeparators() {
//            return new String[] {"/", "\\"};
//        }
//
//        @Override
//        public boolean useMiddleMatching() {
//            return false;
//        }
//
//
//    }
//
//
//    private Object[] filterFiles(final Object[] list) {
//        Condition<PsiFile> condition = psiFile -> {
////            if (myFilter != null && !myFilter.accept(psiFile)) {
////                return false;
////            }
//            boolean accepted = myFileType == null || psiFile.getFileType() == myFileType;
//            VirtualFile virtualFile = psiFile.getVirtualFile();
//            if (virtualFile != null && !accepted) {
//                accepted = FileTypeRegistry.getInstance().isFileOfType(virtualFile, myFileType);
//            }
//            return accepted;
//        };
//        final List<Object> result = new ArrayList<>(list.length);
//        for (Object o : list) {
//            final PsiFile psiFile;
//            if (o instanceof PsiFile) {
//                psiFile = (PsiFile)o;
//            }
//            else if (o instanceof PsiFileNode) {
//                psiFile = ((PsiFileNode)o).getValue();
//            }
//            else {
//                psiFile = null;
//            }
//            if (psiFile != null && !condition.value(psiFile)) {
//                continue;
//            }
//            else {
//                if (o instanceof ProjectViewNode projectViewNode) {
//                    if (!projectViewNode.canHaveChildrenMatching(condition)) {
//                        continue;
//                    }
//                }
//            }
//            result.add(o);
//        }
//        return ArrayUtil.toObjectArray(result);
//    }
//}
