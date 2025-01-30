package com.gengzi.ui.toolWindow;

import com.gengzi.ui.entity.RequestEntityBase;
import com.gengzi.ui.local.Constant;
import com.gengzi.ui.markdown.HtmlContentReader;
import com.gengzi.ui.markdown.MarkdownEntity;
import com.gengzi.ui.request.ApiRequestExample;
import com.gengzi.ui.request.ModelRequestFactory;
import com.gengzi.ui.request.RequestLLMInterface;
import com.gengzi.ui.save.JsonContentExtractor;
import com.gengzi.ui.save.MySettings;
import com.gengzi.ui.swing.JBListByPsiFile;
import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.ide.util.TreeFileChooser;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.ide.util.gotoByName.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefBrowser;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.lang3.StringUtils;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToolFrom {
    private static final Logger log = Logger.getInstance(ToolFrom.class);
    private JTextArea textArea1;
    //    private JScrollPane scrollPane1;
    private JPanel panel1;
    private JPanel panel2;
    //    private JBCefOsrComponent JBCefOsrComponent1;
    private JEditorPane editorPane1;
    private JPanel jpanel3;
    private JPanel file;
    private JButton addFileBtn;
    private JPanel filePanel;
    private JBList fileList;
    private JBScrollPane fileScrollPane;
    private JBScrollPane scrollPane1;
    private JButton reqButton;
    private JPanel reqPanel;
    private JButton newChatButton;
    private JCheckBox changeFileCheckBox;
    private JButton button;
    private Project project;
    // private JTextField textField1;
//    private JList list1;

    private static String MD_CSS = null;
    private Thread thread;

    private final Lock lock = new ReentrantLock();

    //    ConcurrentHashMap<String, PsiFile> fileConcurrentHashMap = new ConcurrentHashMap<String, PsiFile>();
    DefaultListModel<Object> objectDefaultListModel = new DefaultListModel<>();

//    static {
//        try {
//            MD_CSS = HtmlContentReader.getHtmlContentFromResources("github-markdown.css");
//            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
//        } catch (Exception e) {
//            MD_CSS = "";
//        }
//    }


    public JPanel getPanel2() {
        log.info(panel2.toString());
        return panel2;
    }


    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.addActionListener(e -> {
            Object selectedItem = fileList.getSelectedValue();
            if (selectedItem != null) {
                objectDefaultListModel.removeElement(selectedItem);
            }
        });
        popupMenu.add(deleteItem);
        return popupMenu;
    }

    public ToolFrom(Project project) {
        this.project = project;
        fileList.setModel(objectDefaultListModel);
        JBListByPsiFile.cellRendererToPsiFile(fileList);
        // 添加右键菜单
        fileList.setComponentPopupMenu(createPopupMenu());
        fileList.setEmptyText("无文件，文件将在此展示");
        textArea1.setName("aiToolsInputArea");
        reqPanel.setVisible(false);


        // 获取当前project 文件信息

        // 初始化选项栏
//        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
//        for (VirtualFile projectAllFile : ProjectUtils.getProjectAllFiles(project)) {
//            defaultComboBoxModel.addElement(projectAllFile.getName());
//        }
//        comboBox1.setModel(defaultComboBoxModel);


        ChooseByNamePanel chooseByNamePanel = new ChooseByNamePanel(project, new GotoFileModel(project), "选择", true, null) {

            @Override
            protected void initUI(final ChooseByNamePopupComponent.Callback callback,
                                  final ModalityState modalityState,
                                  boolean allowMultipleSelection) {
                super.initUI(callback, modalityState, allowMultipleSelection);
                file.add(this.getPanel(), BorderLayout.CENTER);
            }
        };


        //ChooseByNamePanel chooseByNamePanel = new ChooseByNamePanel(project, new GotoFileModel(project), "选择一个文件", true, null);
//        panel2.setVisible(true);
//        ApplicationManager.getApplication().invokeLater(() -> {
//            file.add(chooseByNamePanel.getPanel());
//        });
//        file.add(chooseByNamePanel.getPanel(), BorderLayout.CENTER);
//
//        file.updateUI();

//        ChooseByNamePopupComponent chooseByNamePopupComponent = ChooseByNameFactory.getInstance(project).createChooseByNamePopupComponent(new GotoFileModel(project));
//        file.add(chooseByNamePopupComponent);


        //  comboBox1.add(chooseByNamePanel.getPanel());


        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char keyChar = e.getKeyChar();
                if (keyChar == '\n') {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 判断是否正在输出内容
                            if (lock.tryLock()) {
//                                lock.lock();
                                try {
                                    reqPanel.setVisible(true);
                                    log.info("回车");
                                    log.info(textArea1.getText());
                                    // sk-f7af6993c62840b9a19b20d2e7063511
                                    MySettings state = MySettings.getInstance().getState();
                                    String msg = textArea1.getText().trim();
//                    textArea1.setText("");

                                    int length = textArea1.getText().length();
                                    String content = textArea1.getText();
                                    content = content.replace("\n", "");
                                    content = content.replace("\r", "");
                                    // 使用replaceRange方法将整个文本范围替换为空字符串
                                    textArea1.setText(content);
                                    textArea1.replaceRange("", 0, content.length());
                                    // 将光标位置设置为0，使光标回到第一行开头
                                    SwingUtilities.invokeLater(() -> {
                                        textArea1.setCaretPosition(0);
                                    });

                                    panel1.updateUI();

                                    JBCefBrowser browser = new JBCefBrowser();
                                    JComponent component = browser.getComponent();
                                    String htmlContentFromResources = HtmlContentReader.getHtmlContentFromResources("content.html");
                                    System.out.println(htmlContentFromResources);
                                    ;
                                    browser.loadHTML(htmlContentFromResources);
                                    // 请求
                                    System.out.println("请求中..." + state.componentStates.get(Constant.API_KEY) + msg);
                                    if (StringUtils.isBlank(state.componentStates.get(Constant.API_KEY))) {
                                        // 需要先配置秘钥
                                        JOptionPane.showMessageDialog(null, "请先配置秘钥", "提示", JOptionPane.INFORMATION_MESSAGE);
                                        return;
                                    }
//                            editorPane1.setContentType("text/html");
//                            Document document = editorPane1.getDocument();
//                            MarkdownJCEFHtmlPanel markdownJCEFHtmlPanel = new MarkdownJCEFHtmlPanel();
//                            ApiRequestExample.req(state.componentStates.get(Constant.API_KEY), msg, markdownJCEFHtmlPanel);
//
//
//
//
//                            JComponent component1 = markdownJCEFHtmlPanel.getComponent();
//
//                            scrollPane1.add(component1);
//                            scrollPane1.setViewportView(component1);
//                            document.insertString(document.getLength(),"",null);

//                            MarkdownToHtmlConverter markdownToHtmlConverter = new MarkdownToHtmlConverter(new CommonMarkFlavourDescriptor());
//                            String markdown = markdownToHtmlConverter.convertMarkdownToHtml(parse,null);
//                            editorPane1.setContentType("text/html");
//                            editorPane1.setText(markdown);
//                            scrollPane1.updateUI();
//                            scrollPane1.revalidate();
//                            scrollPane1.repaint();



                                    if(changeFileCheckBox.isSelected()){
                                        RequestLLMInterface deepseek = ModelRequestFactory.createRequestLLMInterface("deepseek");
                                        RequestEntityBase requestEntityBase = new RequestEntityBase();
                                        Stream<String> stringStream = deepseek.requestLlm(requestEntityBase);

                                        StringBuilder sb = new StringBuilder();
                                        stringStream.forEach(v -> {
                                            if ("data: [DONE]".equalsIgnoreCase(v)) {
                                                return;
                                            }
                                            if (!"".equals(v)) {
                                                String parseInputLine = JsonContentExtractor.parse(formatResponse(v));
                                                sb.append(parseInputLine);
                                            }
                                        });
                                        // 创建 Diff 内容
                                        DiffContentFactory contentFactory = DiffContentFactory.getInstance();
                                        DiffContent content1 = contentFactory.create("Public class xx{}");
                                        DiffContent content2 = contentFactory.create(sb.toString());
                                        // 创建 Diff 请求
                                        DiffRequest request = new SimpleDiffRequest("xx.java对比", content1, content2, "Left", "Right");
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                DiffManager.getInstance().showDiff(project, request);
                                            }
                                        });


                                    }else{
                                        editImpl(state, msg, project);
                                    }



                                    SwingUtilities.invokeLater(() -> {
                                        reqPanel.setVisible(false);
                                    });
                                } finally {
                                    lock.unlock();
                                }
                            } else {
                                new Thread(() -> {
//                                    GotItTooltip tooltip = new GotItTooltip("提示", "回答中...", null).withTimeout(5000);
//                                    tooltip.show(reqButton, GotItTooltip.BOTTOM_MIDDLE);

                                    Balloon balloon = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("回答中",
                                            MessageType.INFO, new DefaultHyperlinkListener(reqButton)).setFadeoutTime(7500L).createBalloon();
                                    balloon.show(RelativePoint.getCenterOf(reqButton), Balloon.Position.below);
                                }).start();

//                        JOptionPane.showMessageDialog(null, "正在请求中，请勿重复点击", "提示", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    });
                    thread.start();
                }

            }

        });
//
//
//        button.addActionListener(new ActionListener() {
//            /**
//             * Invoked when an action occurs.
//             *
//             * @param e the event to be processed
//             */
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String text = editorPane1.getText();
//                String replace = text.replace("\r", "");
//
//                VirtualFile lightVirtualFile = new LightVirtualFile("temp.md", replace);
////                MarkdownToHtmlConverter markdownToHtmlConverter = new MarkdownToHtmlConverter(new CommonMarkFlavourDescriptor());
////                String html = markdownToHtmlConverter.convertMarkdownToHtml(text,null);
////                String htmlContent = convertMarkdownToHtml(text);
//                // String html = MarkdownUtil.INSTANCE.generateMarkdownHtml(lightVirtualFile, text, project);
//                String html = ReadAction.nonBlocking(() -> {
//                            return MarkdownUtil.INSTANCE.generateMarkdownHtml(lightVirtualFile, replace, project);
//                        })
//                        .executeSynchronously();
//
//                System.out.println(html);
//                markdownImpl(html);
////                JBCefBrowser browser = new JBCefBrowser();
////                JComponent component = browser.getComponent();
////                browser.loadHTML(htmlContent);
////                scrollPane1.setViewportView(component);
//            }
//        });
        addFileBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeFileChooserFactory treeFileChooserFactory = TreeFileChooserFactory.getInstance(project);
                TreeFileChooser fileChooser = treeFileChooserFactory.createFileChooser("选择文件", null, null, null);
                fileChooser.showDialog();
                PsiFile selectedFile = fileChooser.getSelectedFile();
                System.out.println(selectedFile);
                // 添加到后面
                if (selectedFile != null) {
                    FileType fileType = selectedFile.getFileType();
                    VirtualFile virtualFile = selectedFile.getVirtualFile();
//                    String s = VirtualFileUtil.readText(virtualFile);
//                    fileConcurrentHashMap.put(virtualFile.getPath(), selectedFile);

                    objectDefaultListModel.addElement(selectedFile);

//                    filePanel.add(new JButton(selectedFile.toString()));
                }
            }
        });
        reqButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                reqPanel.setVisible(false);
                if (thread != null) {
                    boolean alive = thread.isAlive();
                    if (alive) {
                        thread.stop();
                    }
                }
            }
        });
        // 新对话
        newChatButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // 新建一个对话，移出之前的内容
                ApiRequestExample.clear();

            }
        });
    }

    private static class DefaultHyperlinkListener implements HyperlinkListener {
        JComponent component;

        private DefaultHyperlinkListener(JComponent component) {
            this.component = component;
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            }

        }
    }

    public static String convertMarkdownToHtml(String markdown) {
//        Parser parser = Parser.builder().build();
//        Node document = parser.parse(markdown);
//        HtmlRenderer renderer = HtmlRenderer.builder().nodeRendererFactory(new HtmlNodeRendererFactory() {
//                    /**
//                     * Create a new node renderer for the specified rendering context.
//                     *
//                     * @param context the context for rendering (normally passed on to the node renderer)
//                     * @return a node renderer
//                     */
//                    @Override
//                    public NodeRenderer create(HtmlNodeRendererContext context) {
//                        return new IndentedCodeBlockNodeRenderer(context);
//
//                    }
//                }).build();
//        return renderer.render(document);  // "<p>This is <em>Markdown</em></p>\n"
        String parse = parse(markdown);
        MarkdownEntity markdownEntity = ofContent(parse);
        return markdownEntity.toString();
    }


    private void markdownImpl(String htmlContent) {
        MarkdownJCEFHtmlPanel markdownJCEFHtmlPanel = new MarkdownJCEFHtmlPanel();
        markdownJCEFHtmlPanel.getJBCefClient().getCefClient().removeRequestHandler();
        JComponent component1 = markdownJCEFHtmlPanel.getComponent();
        markdownJCEFHtmlPanel.setHtml(htmlContent, 0);
        scrollPane1.setViewportView(component1);
    }


    private void editImpl(MySettings state, String msg, Project project) {
        editorPane1.setEditable(false);
        editorPane1.setEditorKit(new HTMLEditorKit());
//        editorPane1.setContentType("text/plain");
        Document document = editorPane1.getDocument();

        // 添加文件相关内容
        String fileStr = "\n#file\n" +
                "文件名称:%s\n" +
                "文件类型:%s\n" +
                "文件内容:%s\n";
        StringBuilder sb = new StringBuilder();
        ListModel model = fileList.getModel();

        Application application = ApplicationManager.getApplication();
        application.runReadAction(() -> {
            Arrays.stream(objectDefaultListModel.toArray()).forEach(
                    v -> {
                        PsiFile psiFile = (PsiFile) v;
                        String format = String.format(fileStr, psiFile.getName(), psiFile.getFileType().getName(), psiFile.getText());
                        sb.append(format);
                    }
            );
        });

        sb.append(msg);
//        for (PsiFile value : fileConcurrentHashMap.values()) {
//            String format = String.format(fileStr, value.getName(), value.getFileType().getName(), value.getText());
//            sb.append(format);
//        }
        String otherMsg = sb.toString();
        System.out.println(msg);

        ApiRequestExample.req(state.componentStates.get(Constant.API_KEY), msg, scrollPane1, project, otherMsg);
//        scrollPane1.updateUI();
//        scrollPane1.setViewportView(editorPane1);
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }


    /**
     * markdown to image
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }


    public void show(String content) {
//        MarkdownPreviewFileEditor
//                MarkdownPreviewFileEditor editor = new MarkdownPreviewFileEditor();
//        JComponent component = editor.getComponent();

    }

    private static String formatResponse(String response) {
        // 这里可以对响应数据进行格式化处理，比如去掉多余的空格
        if (response.startsWith("data: ")) {
            String jsonData = response.substring(6);
            return jsonData;
        } else {
            return "";
        }
    }

}
