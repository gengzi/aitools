import com.gengzi.ui.local.Constant;
import com.gengzi.ui.markdown.HtmlContentReader;
import com.gengzi.ui.markdown.MarkdownEntity;
import com.gengzi.ui.request.ApiRequestExample;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefBrowser;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * ToolFrom类用于处理Markdown文本的转换和显示。它提供了将Markdown文本转换为HTML格式的功能，并在UI组件中显示。
 * 主要功能包括：
 * - 将Markdown文本转换为HTML格式。
 * - 在JEditorPane中显示HTML内容。
 * - 通过JBCefBrowser加载和显示HTML内容。
 * - 处理用户输入并触发相关操作。
 *
 * 重要方法：
 * - convertMarkdownToHtml(String markdown): 将Markdown文本转换为HTML格式。
 * - ofContent(String content): 将Markdown文本转换为MarkdownEntity对象，包含HTML和CSS样式。
 * - parse(String content): 将Markdown文本解析为HTML内容。
 * - editImpl(MySettings state, String msg, Project project): 处理编辑操作，调用API请求并更新UI。
 * - markdownImpl(String htmlContent): 在UI中显示HTML内容。
 */

public class ToolFrom {
    private static final Logger log = Logger.getInstance(ToolFrom.class);
    private JTextArea textArea1;
    private JBScrollPane scrollPane1;
    private JPanel panel1;
    private JPanel panel2;
    //    private JBCefOsrComponent JBCefOsrComponent1;
    private JEditorPane editorPane1;
    private JPanel jpanel3;
    private JComboBox comboBox1;
    private JButton button;
    private Project project;
    // private JTextField textField1;
//    private JList list1;

    private static String MD_CSS = null;

    static {
        try {
            MD_CSS = HtmlContentReader.getHtmlContentFromResources("github-markdown.css");
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }


    public JPanel getPanel2() {
        log.info(panel2.toString());
        return panel2;
    }

    public ToolFrom(Project project) {
        this.project = project;
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char keyChar = e.getKeyChar();
                if (keyChar == '\n') {
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JBCefBrowser browser = new JBCefBrowser();
                            JComponent component = browser.getComponent();
                            String htmlContentFromResources = HtmlContentReader.getHtmlContentFromResources("content.html");
                            System.out.println(htmlContentFromResources);
                            ;
                            browser.loadHTML(htmlContentFromResources);
                            // 请求
                            System.out.println("请求中..." + state.componentStates.get(Constant.API_KEY) + msg);
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
                            editImpl(state, msg, project);


                        }
                    }).start();
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

        ApiRequestExample.req(state.componentStates.get(Constant.API_KEY), msg, scrollPane1, project,"");
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
}
