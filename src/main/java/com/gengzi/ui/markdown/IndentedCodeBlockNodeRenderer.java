//package com.gengzi.ui.markdown;
//
//
//import org.commonmark.node.IndentedCodeBlock;
//import org.commonmark.node.Node;
//import org.commonmark.renderer.NodeRenderer;
//import org.commonmark.renderer.html.HtmlNodeRendererContext;
//import org.commonmark.renderer.html.HtmlWriter;
//
//import java.util.Set;
//
//public class IndentedCodeBlockNodeRenderer implements NodeRenderer {
//
//    private final HtmlWriter html;
//
//    public IndentedCodeBlockNodeRenderer(HtmlNodeRendererContext context) {
//        this.html = context.getWriter();
//    }
//
//    @Override
//    public Set<Class<? extends Node>> getNodeTypes() {
//        // Return the node types we want to use this renderer for.
//        return Set.of(IndentedCodeBlock.class);
//    }
//
//    @Override
//    public void render(Node node) {
//        // We only handle one type as per getNodeTypes, so we can just cast it here.
//        IndentedCodeBlock codeBlock = (IndentedCodeBlock) node;
//        html.line();
//        html.tag("pre");
//        html.text(codeBlock.getLiteral());
//        html.tag("/pre");
//        html.line();
//    }
//}