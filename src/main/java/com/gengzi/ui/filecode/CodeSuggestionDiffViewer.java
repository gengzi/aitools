//package com.gengzi.ui.filecode;
//
//import com.intellij.codeInsight.inline.completion.session.InlineCompletionContext;
//import com.intellij.codeInsight.inline.completion.session.InlineCompletionSession;
//import com.intellij.diff.DiffContentFactory;
//import com.intellij.diff.DiffContext;
//import com.intellij.diff.requests.DiffRequest;
//import com.intellij.diff.requests.SimpleDiffRequest;
//import com.intellij.diff.tools.combined.COMBINED_DIFF_MAIN_UI;
//import com.intellij.diff.tools.fragmented.UnifiedDiffChange;
//import com.intellij.diff.tools.fragmented.UnifiedDiffViewer;
//import com.intellij.diff.util.DiffUtil;
//import com.intellij.ide.plugins.newui.TagComponent;
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.actionSystem.ActionManager;
//import com.intellij.openapi.components.ServiceManager;
//import com.intellij.openapi.editor.Document;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.editor.LogicalPosition;
//import com.intellij.openapi.editor.event.DocumentEvent;
//import com.intellij.openapi.editor.event.DocumentListener;
//import com.intellij.openapi.editor.event.VisibleAreaEvent;
//import com.intellij.openapi.editor.event.VisibleAreaListener;
//import com.intellij.openapi.keymap.KeymapUtil;
//import com.intellij.openapi.observable.util.ObservableUtil;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.ui.popup.JBPopup;
//import com.intellij.openapi.ui.popup.JBPopupFactory;
//import com.intellij.openapi.util.Key;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.openapi.util.UserDataHolder;
//import com.intellij.openapi.util.UserDataHolderBase;
//import com.intellij.testFramework.LightVirtualFile;
//import com.intellij.ui.components.JBLabel;
//import com.intellij.ui.components.JBScrollPane;
//import com.intellij.util.concurrency.annotations.RequiresEdt;
//import com.intellij.util.ui.JBUI;
//import com.intellij.util.ui.components.BorderLayoutPanel;
//import ee.carlrobert.codegpt.CodeGPTBundle;
//import ee.carlrobert.codegpt.CodeGPTKeys;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//public class CodeSuggestionDiffViewer extends UnifiedDiffViewer implements Disposable {
//    private final JBPopup popup;
//    private final Editor mainEditor;
//    private final boolean isManuallyOpened;
//    private final VisibleAreaListener visibleAreaListener;
//    private final DocumentListener documentListener;
//    private boolean applyInProgress = false;
//
//    public CodeSuggestionDiffViewer(DiffRequest request, Editor mainEditor, boolean isManuallyOpened) {
//        super(new MyDiffContext(mainEditor.getProject()), request);
//        this.mainEditor = mainEditor;
//        this.isManuallyOpened = isManuallyOpened;
//        this.popup = createSuggestionDiffPopup(getComponent());
//        this.visibleAreaListener = getVisibleAreaListener();
//        this.documentListener = getDocumentListener();
//        setupDiffEditor();
//        mainEditor.getScrollingModel().addVisibleAreaListener(visibleAreaListener);
//        mainEditor.getDocument().addDocumentListener(documentListener, this);
//        ObservableUtil.whenDisposed(popup, this::clearListeners);
//    }
//
//    @Override
//    public void dispose() {
//        popup.dispose();
//        super.dispose();
//    }
//
//    @Override
//    protected void onAfterRediff() {
//        Optional<UnifiedDiffChange> changeOptional = getClosestChange();
//        if (changeOptional.isPresent()) {
//            UnifiedDiffChange change = changeOptional.get();
//            getEditor().getComponent().setPreferredSize(
//                    new Dimension(
//                            mainEditor.getComponent().getWidth() / 2,
//                            getEditor().getLineHeight() * change.getChangedLinesCount()
//                    )
//            );
//            adjustPopupSize(popup, getEditor());
//
//            int changeOffset = change.getLineFragment().getStartOffset1();
//            Point adjustedLocation = getAdjustedPopupLocation(popup, mainEditor, changeOffset);
//
//            if (popup.isVisible()) {
//                popup.setLocation(adjustedLocation);
//            } else {
//                popup.showInScreenCoordinates(mainEditor.getComponent(), adjustedLocation);
//            }
//
//            scrollToChange(change);
//        }
//    }
//
//    public void applyChanges() {
//        List<UnifiedDiffChange> changes = getDiffChanges();
//        if (changes == null) {
//            changes = List.of();
//        }
//        Optional<UnifiedDiffChange> changeOptional = getClosestChange();
//        if (changeOptional.isPresent()) {
//            UnifiedDiffChange change = changeOptional.get();
//            if (isStateIsOutOfDate() || !isEditable(getMasterSide(), true)) {
//                return;
//            }
//            Document document = getDocument(getMasterSide());
//            DiffUtil.executeWriteCommand(document, getProject(), null, () -> {
//                applyInProgress = true;
//                try {
//                    replaceChange(change, getMasterSide());
//                } finally {
//                    applyInProgress = false;
//                }
//                moveCaretToChange(change, document);
//                scheduleRediff();
//            });
//            rediff(true);
//
//            if (changes.size() == 1) {
//                popup.dispose();
//            }
//        }
//    }
//
//    public boolean isVisible() {
//        return popup.isVisible();
//    }
//
//    private void setupDiffEditor() {
//        getEditor().getSettings().setAdditionalLinesCount(0);
//        getEditor().getSettings().setFoldingOutlineShown(false);
//        getEditor().getSettings().setCaretRowShown(false);
//        getEditor().getSettings().setBlinkCaret(false);
//        getEditor().getSettings().setDndEnabled(false);
//        getEditor().getSettings().setIndentGuidesShown(false);
//        getEditor().getGutterComponentEx().setVisible(false);
//        getEditor().getGutterComponentEx().getParent().setVisible(false);
//        ((JBScrollPane) getEditor().getScrollPane()).getHorizontalScrollBar().setOpaque(false);
//
//        setupStatusLabel();
//    }
//
//    private void clearListeners() {
//        mainEditor.putUserData(CodeGPTKeys.EDITOR_PREDICTION_DIFF_VIEWER, null);
//        mainEditor.getScrollingModel().removeVisibleAreaListener(visibleAreaListener);
//        mainEditor.getDocument().removeDocumentListener(documentListener);
//    }
//
//    private Optional<UnifiedDiffChange> getClosestChange() {
//        List<UnifiedDiffChange> changes = getDiffChanges();
//        if (changes == null) {
//            changes = List.of();
//        }
//        int cursorOffset = mainEditor.getCaretModel().getOffset();
//        return changes.stream()
//                .min((c1, c2) -> Integer.compare(
//                        Math.abs(c1.getLineFragment().getStartOffset1() - cursorOffset),
//                        Math.abs(c2.getLineFragment().getStartOffset1() - cursorOffset)
//                ));
//    }
//
//    private JComponent getTagPanel() {
//        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
//        tagPanel.setOpaque(false);
//
//        TagComponent openTag = new TagComponent("Open: " + getShortcutText("OpenPredictionAction.ID"));
//        openTag.setFont(JBUI.Fonts.smallFont());
//        openTag.setListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ServiceManager.getService(PredictionService.class).openDirectPrediction(
//                        mainEditor,
//                        getContent2().getDocument().getText()
//                );
//                popup.dispose();
//            }
//        }, getComponent());
//        tagPanel.add(openTag);
//        tagPanel.add(Box.createHorizontalStrut(6));
//
//        TagComponent acceptTag = new TagComponent("Accept: " + getShortcutText("AcceptNextPredictionRevisionAction.ID"));
//        acceptTag.setFont(JBUI.Fonts.smallFont());
//        acceptTag.setListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                applyChanges();
//                popup.dispose();
//            }
//        }, getComponent());
//        tagPanel.add(acceptTag);
//
//        return tagPanel;
//    }
//
//    private void setupStatusLabel() {
//        ((JBScrollPane) getEditor().getScrollPane()).setStatusComponent(
//                new BorderLayoutPanel()
//                        .andTransparent()
//                        .withBorder(JBUI.Borders.empty(4))
//                        .addToRight(getTagPanel())
//        );
//
//        String footerText;
//        if (isManuallyOpened) {
//            footerText = CodeGPTBundle.get("shared.escToCancel");
//        } else {
//            footerText = "Trigger manually: " + getShortcutText("OpenPredictionAction.ID") + " · " + CodeGPTBundle.get("shared.escToCancel");
//        }
//
//        getEditor().getComponent().add(
//                new BorderLayoutPanel()
//                        .addToRight(new JBLabel(footerText).apply(
//                                label -> label.setFont(JBUI.Fonts.miniFont())
//                        ))
//                        .apply(
//                                panel -> {
//                                    panel.setBackground(getEditor().getBackgroundColor());
//                                    panel.setBorder(JBUI.Borders.empty(4));
//                                }
//                        ),
//                BorderLayout.SOUTH
//        );
//    }
//
//    private VisibleAreaListener getVisibleAreaListener() {
//        return new VisibleAreaListener() {
//            @Override
//            public void visibleAreaChanged(VisibleAreaEvent event) {
//                Optional<UnifiedDiffChange> changeOptional = getClosestChange();
//                if (changeOptional.isPresent()) {
//                    UnifiedDiffChange change = changeOptional.get();
//                    Point adjustedLocation = getAdjustedPopupLocation(
//                            popup,
//                            mainEditor,
//                            change.getLineFragment().getStartOffset1()
//                    );
//
//                    if (popup.isVisible() && !popup.isDisposed()) {
//                        adjustPopupSize(popup, getEditor());
//                        popup.setLocation(adjustedLocation);
//                    }
//                }
//            }
//        };
//    }
//
//    private DocumentListener getDocumentListener() {
//        return new DocumentListener() {
//            @Override
//            public void documentChanged(DocumentEvent event) {
//                if (applyInProgress) {
//                    return;
//                }
//                popup.setUiVisible(false);
//                dispose();
//            }
//        };
//    }
//
//    private void scrollToChange(UnifiedDiffChange change) {
//        Point pointToScroll = getEditor().logicalPositionToXY(new LogicalPosition(change.getLine1(), 0));
//        pointToScroll.y -= getEditor().getLineHeight();
//        DiffUtil.scrollToPoint(getEditor(), pointToScroll, false);
//    }
//
//    private void moveCaretToChange(UnifiedDiffChange change, Document document) {
//        int changeEndOffset = change.getLineFragment().getEndOffset2();
//        String previousChar = document.getText(new TextRange(changeEndOffset - 1, changeEndOffset));
//        int offset = previousChar.equals("\n") ? changeEndOffset - 1 : changeEndOffset;
//
//        mainEditor.getCaretModel().moveToOffset(Math.max(offset, 0));
//
//        Point offsetPosition = mainEditor.offsetToXY(offset);
//        Rectangle visibleArea = mainEditor.getScrollingModel().getVisibleArea();
//        boolean offsetVisible = visibleArea.contains(offsetPosition);
//        if (!offsetVisible) {
//            DiffUtil.scrollToCaret(mainEditor, false);
//        }
//    }
//
//    private static class MyDiffContext extends DiffContext {
//        private final Project project;
//        private final UserDataHolder ownContext = new UserDataHolderBase();
//
//        public MyDiffContext(Project project) {
//            this.project = project;
//        }
//
//        private Object getMainUi() {
//            return getUserData(COMBINED_DIFF_MAIN_UI);
//        }
//
//        @Override
//        public Project getProject() {
//            return project;
//        }
//
//        @Override
//        public boolean isFocusedInWindow() {
//            Object mainUi = getMainUi();
//            return mainUi != null && ((DiffContext) mainUi).isFocusedInWindow();
//        }
//
//        @Override
//        public boolean isWindowFocused() {
//            Object mainUi = getMainUi();
//            return mainUi != null && ((DiffContext) mainUi).isWindowFocused();
//        }
//
//        @Override
//        public void requestFocusInWindow() {
//            Object mainUi = getMainUi();
//            if (mainUi != null) {
//                ((DiffContext) mainUi).requestFocusInWindow();
//            }
//        }
//
//        @Override
//        public <T> T getUserData(Key<T> key) {
//            return ownContext.getUserData(key);
//        }
//
//        @Override
//        public <T> void putUserData(Key<T> key, T value) {
//            ownContext.putUserData(key, value);
//        }
//    }
//
//    public static void displayInlineDiff(Editor editor, String nextRevision, boolean isManuallyOpened) {
//        if (editor.getVirtualFile() == null || editor.isViewer()) {
//            return;
//        }
//        CodeSuggestionDiffViewer existingViewer = editor.getUserData(CodeGPTKeys.EDITOR_PREDICTION_DIFF_VIEWER);
//        if (existingViewer != null) {
//            existingViewer.dispose();
//        }
//        editor.putUserData(CodeGPTKeys.REMAINING_EDITOR_COMPLETION, null);
//        InlineCompletionSession session = InlineCompletionSession.getOrNull(editor);
//        if (session != null && session.isActive()) {
//            InlineCompletionContext context = InlineCompletionContext.getOrNull(editor);
//            if (context != null) {
//                context.clear();
//            }
//        }
//
//        DiffRequest diffRequest = createSimpleDiffRequest(editor, nextRevision);
//        CodeSuggestionDiffViewer diffViewer = new CodeSuggestionDiffViewer(diffRequest, editor, isManuallyOpened);
//        editor.putUserData(CodeGPTKeys.EDITOR_PREDICTION_DIFF_VIEWER, diffViewer);
//        diffViewer.rediff(true);
//    }
//
//    public static SimpleDiffRequest createSimpleDiffRequest(Editor editor, String nextRevision) {
//        Project project = editor.getProject();
//        LightVirtualFile virtualFile = editor.getVirtualFile();
//        LightVirtualFile tempDiffFile = new LightVirtualFile(virtualFile.getName(), nextRevision);
//        DiffContentFactory diffContentFactory = DiffContentFactory.getInstance();
//        return new SimpleDiffRequest(
//                null,
//                diffContentFactory.create(project, virtualFile),
//                diffContentFactory.create(project, tempDiffFile),
//                null,
//                null
//        );
//    }
//
//    public static int getChangedLinesCount(UnifiedDiffChange change) {
//        int insertedLines = change.getInsertedRange().getEnd() - change.getInsertedRange().getStart();
//        int deletedLines = change.getDeletedRange().getEnd() - change.getDeletedRange().getStart();
//        return deletedLines + insertedLines + 2;
//    }
//
//    public static Point getAdjustedPopupLocation(JBPopup popup, Editor editor, int changeOffset) {
//        Point pointInEditor = editor.offsetToXY(changeOffset);
//        if (!editor.getComponent().isShowing()) {
//            Point point = new Point(pointInEditor);
//            SwingUtilities.convertPointToScreen(point, editor.getComponent());
//            return point;
//        }
//
//        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
//        Point editorLocationOnScreen = editor.getComponent().getLocationOnScreen();
//        boolean isOffsetVisible = visibleArea.contains(pointInEditor);
//        int popupY;
//        if (isOffsetVisible) {
//            popupY = editorLocationOnScreen.y + pointInEditor.y - editor.getScrollingModel().getVerticalScrollOffset();
//        } else {
//            if (pointInEditor.y < visibleArea.y) {
//                popupY = editorLocationOnScreen.y;
//            } else {
//                popupY = editorLocationOnScreen.y + visibleArea.height - popup.getSize().height;
//            }
//        }
//        int popupX = editorLocationOnScreen.x + editor.getComponent().getWidth() - popup.getSize().width;
//        return new Point(popupX, popupY - editor.getLineHeight());
//    }
//
//    public static void adjustPopupSize(JBPopup popup, Editor editor) {
//        int newWidth = editor.getComponent().getPreferredSize().width;
//        int newHeight = editor.getComponent().getPreferredSize().height;
//        popup.setSize(new Dimension(newWidth, newHeight));
//        popup.getContent().revalidate();
//        popup.getContent().repaint();
//    }
//
//    public static String getShortcutText(String actionId) {
//        return KeymapUtil.getFirstKeyboardShortcutText(
//                ActionManager.getInstance().getAction(actionId)
//        );
//    }
//
//    public static JBPopup createSuggestionDiffPopup(JComponent content) {
//        return JBPopupFactory.getInstance().createComponentPopupBuilder(content, null)
//                .setNormalWindowLevel(true)
//                .setCancelOnClickOutside(false)
//                .setRequestFocus(false)
//                .setFocusable(true)
//                .setMovable(true)
//                .setResizable(true)
//                .setShowBorder(true)
//                .setCancelKeyEnabled(true)
//                .setCancelOnWindowDeactivation(false)
//                .setCancelOnOtherWindowOpen(false)
//                .createPopup();
//    }
//}