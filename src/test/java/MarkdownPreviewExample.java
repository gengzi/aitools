import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;

public class MarkdownPreviewExample extends JFrame {
    private JTextArea markdownTextArea;
    private JEditorPane previewPane;

    public MarkdownPreviewExample() {
        setTitle("Markdown Preview Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        markdownTextArea = new JTextArea();
        markdownTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePreview();
            }
        });

        previewPane = new JEditorPane();
        previewPane.setEditable(false);
        previewPane.setEditorKit(new HTMLEditorKit());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(markdownTextArea), new JScrollPane(previewPane));
        splitPane.setDividerLocation(0.5);

        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void updatePreview() {
        String markdownText = markdownTextArea.getText();
        String htmlText = convertMarkdownToHtml(markdownText);
        try {
            HTMLDocument htmlDocument = (HTMLDocument) previewPane.getDocument();
            htmlDocument.setInnerHTML(htmlDocument.getDefaultRootElement(), htmlText);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    // 这里只是一个简单的示例，实际的Markdown转HTML需要使用专门的库，如commonmark-java等
    private String convertMarkdownToHtml(String markdownText) {
        // 简单替换一些Markdown元素为HTML元素
        markdownText = markdownText.replaceAll("# (.*)", "<h1>$1</h1>");
        markdownText = markdownText.replaceAll("## (.*)", "<h2>$1</h2>");
        markdownText = markdownText.replaceAll("### (.*)", "<h3>$1</h3>");
        markdownText = markdownText.replaceAll("- (.*)", "<li>$1</li>");
        markdownText = markdownText.replaceAll("``` (.*)", "<pre><code>$1</code><pre>");
        markdownText = markdownText.replaceAll("\n", "<br>");
        return "<html><body>" + markdownText + "</body></html>";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarkdownPreviewExample());
    }
}