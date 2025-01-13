////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package com.gengzi.ui.util;
//
//import com.alibabacloud.intellij.cosy.common.CosyCacheKeys;
//import com.alibabacloud.intellij.cosy.common.CosyConfig;
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
//import com.intellij.codeInsight.daemon.impl.HighlightInfo;
//import com.intellij.execution.filters.ExceptionWorker;
//import com.intellij.lang.Language;
//import com.intellij.lang.LanguageUtil;
//import com.intellij.lang.annotation.HighlightSeverity;
//import com.intellij.lang.jvm.JvmModifier;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.editor.CaretModel;
//import com.intellij.openapi.editor.Document;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.editor.LogicalPosition;
//import com.intellij.openapi.editor.SelectionModel;
//import com.intellij.openapi.fileEditor.FileDocumentManager;
//import com.intellij.openapi.fileTypes.FileType;
//import com.intellij.openapi.fileTypes.FileTypes;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Computable;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.psi.PsiClass;
//import com.intellij.psi.PsiComment;
//import com.intellij.psi.PsiDocumentManager;
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiErrorElement;
//import com.intellij.psi.PsiField;
//import com.intellij.psi.PsiFile;
//import com.intellij.psi.PsiInvalidElementAccessException;
//import com.intellij.psi.PsiJavaFile;
//import com.intellij.psi.PsiJavaToken;
//import com.intellij.psi.PsiLiteralValue;
//import com.intellij.psi.PsiMethod;
//import com.intellij.psi.PsiModifierListOwner;
//import com.intellij.psi.PsiPlainText;
//import com.intellij.psi.PsiType;
//import com.intellij.psi.PsiWhiteSpace;
//import com.intellij.psi.TokenType;
//import com.intellij.psi.impl.source.tree.CompositeElement;
//import com.intellij.psi.javadoc.PsiDocComment;
//import com.intellij.psi.search.FilenameIndex;
//import com.intellij.psi.search.GlobalSearchScope;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import com.intellij.psi.util.PsiTreeUtil;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
//
//public class PsiUtils {
//    private static final Logger log = Logger.getInstance(PsiUtils.class);
//    public static final String IDENTIFIER = "IDENTIFIER";
//    public static final String DEFAULT_CLASS_NAME = "DemoClass";
//    private static final List<String> commentFlags = Arrays.asList("/", "#", "\"\"\"", "'''", "/*", "*");
//    private static final TokenSet INVALID_COMMON_TOKENSET;
//    private static final String[] FILE_TYPE_CLASSES;
//    private static Cache<String, Boolean> classInstanceTypeCache;
//
//    public PsiUtils() {
//    }
//
//    public static String parsePsiType(PsiType psiType) {
//        return psiType == null ? "" : psiType.getPresentableText();
//    }
//
//    public static boolean isPrivateModifier(PsiModifierListOwner psiModifierListOwner) {
//        return psiModifierListOwner != null && psiModifierListOwner.getModifierList() != null ? psiModifierListOwner.getModifierList().hasModifierProperty("private".trim()) : false;
//    }
//
//    public static boolean isPublicModifier(PsiModifierListOwner psiModifierListOwner) {
//        return psiModifierListOwner != null && psiModifierListOwner.getModifierList() != null ? psiModifierListOwner.getModifierList().hasModifierProperty("public".trim()) : false;
//    }
//
//    public static boolean isProtectedModifier(PsiModifierListOwner psiModifierListOwner) {
//        return psiModifierListOwner != null && psiModifierListOwner.getModifierList() != null ? psiModifierListOwner.getModifierList().hasModifierProperty("protected".trim()) : false;
//    }
//
//    public static PsiMethod[] getMethodsFromPsiClass(PsiClass psiClass, boolean isStatic, String prefix, Predicate<PsiModifierListOwner> visibilityPredicator) {
//        if (psiClass == null) {
//            return new PsiMethod[0];
//        } else {
//            if (prefix == null) {
//                prefix = "";
//            }
//
//            String finalPrefix = prefix;
//            PsiMethod[] methods = new PsiMethod[0];
//
//            try {
//                methods = (PsiMethod[])((List)Arrays.stream(psiClass.getAllMethods()).filter((psiMethod) -> {
//                    if (!psiMethod.getName().toLowerCase().startsWith(finalPrefix)) {
//                        return false;
//                    } else {
//                        return !isStatic || psiMethod.hasModifier(JvmModifier.STATIC);
//                    }
//                }).filter(visibilityPredicator).collect(Collectors.toList())).toArray(new PsiMethod[0]);
//            } catch (PsiInvalidElementAccessException psiInvalidElementAccessException) {
//                log.warn("Get all methods encountered PsiInvalidElementAccessException" + psiInvalidElementAccessException.getMessage());
//            }
//
//            return methods;
//        }
//    }
//
//    public static PsiField[] getFieldsFromPsiClass(PsiClass psiClass, boolean isStatic, String prefix, Predicate<PsiModifierListOwner> visibilityPredicator) {
//        if (psiClass == null) {
//            return new PsiField[0];
//        } else {
//            if (prefix == null) {
//                prefix = "";
//            }
//
//            PsiField[] fields = (PsiField[])((List)Arrays.stream(psiClass.getAllFields()).filter((psiField) -> {
//                if (!psiField.getName().toLowerCase().startsWith(prefix)) {
//                    return false;
//                } else {
//                    return !isStatic || psiField.hasModifier(JvmModifier.STATIC);
//                }
//            }).filter(visibilityPredicator).collect(Collectors.toList())).toArray(new PsiField[0]);
//            return fields;
//        }
//    }
//
//    public static PsiElement skipEmptyAndJavaTokenForward(PsiElement psiElement) {
//        if (psiElement == null) {
//            return null;
//        } else {
//            for(PsiElement next = psiElement.getNextSibling(); next != null; next = next.getNextSibling()) {
//                boolean notIdentifierToken = next instanceof PsiJavaToken && !"IDENTIFIER".equals(((PsiJavaToken)next).getTokenType().toString());
//                boolean isEmpty = next.getText().isEmpty();
//                boolean isWhitespace = next instanceof PsiWhiteSpace;
//                boolean isComment = next instanceof PsiComment;
//                if (!isEmpty && !isWhitespace && !isComment && !notIdentifierToken) {
//                    return next;
//                }
//            }
//
//            return null;
//        }
//    }
//
//    public static PsiClass getContainingClass(PsiElement psiElement) {
//        return (PsiClass)PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
//    }
//
//    public static String getPackageName(PsiClass psiClass) {
//        PsiFile psiFile = psiClass.getContainingFile();
//        return psiFile instanceof PsiJavaFile ? ((PsiJavaFile)psiFile).getPackageName() : "";
//    }
//
//    public static Predicate<PsiModifierListOwner> generateVisibilityPredicator(PsiClass psiClass, PsiClass contextClass) {
//        Predicate<PsiModifierListOwner> defaultPublicPredicator = new Predicate<PsiModifierListOwner>() {
//            public boolean test(PsiModifierListOwner psiModifierListOwner) {
//                return PsiUtils.isPublicModifier(psiModifierListOwner);
//            }
//        };
//        if (psiClass != null && contextClass != null) {
//            String psiClassName = psiClass.getQualifiedName();
//            String psiClassPackage = getPackageName(psiClass);
//            String contextClassName = contextClass.getQualifiedName();
//            String contextClassPackage = getPackageName(contextClass);
//            final boolean hasEmpty = StringUtils.isEmpty(psiClassPackage) || StringUtils.isEmpty(contextClassPackage) || StringUtils.isEmpty(psiClassName) || StringUtils.isEmpty(contextClassName);
//            final boolean isPackageMatch = contextClassPackage.equals(psiClassPackage);
//            final boolean isClassNameMatch = contextClassPackage.equals(psiClassName);
//            Predicate<PsiModifierListOwner> predicator = new Predicate<PsiModifierListOwner>() {
//                public boolean test(PsiModifierListOwner psiModifierListOwner) {
//                    if (!hasEmpty && isPackageMatch) {
//                        if (isPackageMatch && !isClassNameMatch) {
//                            return !PsiUtils.isPrivateModifier(psiModifierListOwner);
//                        } else {
//                            return true;
//                        }
//                    } else {
//                        return PsiUtils.isPublicModifier(psiModifierListOwner);
//                    }
//                }
//            };
//            return predicator;
//        } else {
//            return defaultPublicPredicator;
//        }
//    }
//
//    public static boolean isNotInClassElement(PsiElement element, @NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(0);
//        }
//
//        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
//        if (virtualFile != null) {
//            String filePath = virtualFile.getPath();
//            if (!filePath.endsWith(".java")) {
//                return false;
//            }
//        }
//
//        if (instanceOf(element, (String[])("com.intellij.psi.PsiIdentifier")) && element.getParent() instanceof PsiErrorElement) {
//            PsiErrorElement el = (PsiErrorElement)element.getParent();
//            if (el.getErrorDescription().contains("'class' or 'interface' expected")) {
//                return true;
//            }
//        }
//
//        if (element instanceof PsiWhiteSpace && instanceOf(element.getParent(), (String[])("com.intellij.psi.PsiJavaFile"))) {
//            String text = editor.getDocument().getText();
//            int offset = editor.getCaretModel().getOffset();
//            int classIdx = text.lastIndexOf("class", offset);
//            int bracketIdx = text.indexOf("}", offset);
//            return classIdx <= -1 || bracketIdx <= -1;
//        } else {
//            return false;
//        }
//    }
//
//    public static boolean checkCaretAround(Editor editor) {
//        int offset = editor.getCaretModel().getOffset();
//        LogicalPosition logicalPosition = editor.getCaretModel().getLogicalPosition();
//        int lineStartOffset = editor.getDocument().getLineStartOffset(logicalPosition.line);
//        int lineEndOffset = editor.getDocument().getLineEndOffset(logicalPosition.line);
//        int caretOffset = offset - lineStartOffset;
//        String lineText = editor.getDocument().getText(new TextRange(lineStartOffset, lineEndOffset));
//        if (caretOffset > 0 && caretOffset < lineText.length()) {
//            char afterChar = lineText.charAt(caretOffset);
//            char beforeChar = lineText.charAt(caretOffset - 1);
//            if (isValidCodeTokenChar(afterChar) && (isValidCodeTokenChar(beforeChar) || beforeChar == '(')) {
//                log.info("invalid position in word middle");
//                return false;
//            }
//
//            if (caretOffset > 1) {
//                char moreBeforeChar = lineText.charAt(caretOffset - 2);
//                if (isValidCodeTokenChar(afterChar) && (beforeChar == '=' || beforeChar == ' ' && moreBeforeChar == '=')) {
//                    log.info("invalid position after = xxxx");
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    public static boolean isJavaMethodNewLine(Editor editor, PsiElement element, int newOffset, int previousOffset) {
//        if (element.getPrevSibling() != null && instanceOf(element.getPrevSibling(), (String[])("com.intellij.psi.impl.source.tree.java.MethodElement", "com.intellij.psi.impl.source.PsiMethodImpl"))) {
//            TextRange range = element.getPrevSibling().getTextRange();
//            if (previousOffset >= range.getEndOffset() && previousOffset > 0 && newOffset > previousOffset) {
//                String text = editor.getDocument().getText(new TextRange(previousOffset - 1, newOffset));
//                if ("}".equals(text.trim())) {
//                    log.info("check method end of newline, not trigger.");
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    private static boolean isValidCodeTokenChar(char ch) {
//        return Character.isJavaIdentifierPart(ch) || ch == '_' || ch == '$';
//    }
//
//    public static boolean isPythonIdentifier(String name) {
//        try {
//            Class<?> clazz = ReflectUtil.classForName("com.jetbrains.python.PyNames");
//            if (clazz == null) {
//                return false;
//            }
//
//            Method method = clazz.getDeclaredMethod("isIdentifier", String.class);
//            Object obj = method.invoke((Object)null, name);
//            if (obj != null) {
//                return (Boolean)obj;
//            }
//        } catch (Exception e) {
//            log.error("fail to check isPythonIdentifier.", e);
//        }
//
//        return false;
//    }
//
//    public static boolean isValidCommenteElement(PsiElement element, @NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(1);
//        }
//
//        boolean isComment = isCommentElement(element, editor);
//        if (isComment) {
//            Document document = editor.getDocument();
//            int offset = editor.getCaretModel().getOffset();
//            int lineIndex = document.getLineNumber(offset);
//            TextRange lineSuffixRange = TextRange.create(offset, document.getLineEndOffset(lineIndex));
//            String lineSuffix = document.getText(lineSuffixRange);
//            return StringUtils.isBlank(lineSuffix);
//        } else {
//            return true;
//        }
//    }
//
//    public static boolean isCommentElement(PsiElement element, @NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(2);
//        }
//
//        if (!(element instanceof PsiComment) && !instanceOf(element, (String[])("com.goide.psi.impl.GoCommentImpl", "com.intellij.psi.impl.source.tree.PsiCommentImpl", "com.jetbrains.cidr.lang.editor.OCASTFactory$PsiCommentPlaceholder", "com.intellij.sql.dialects.base.SqlParserDefinitionBase$SqlCommentImpl"))) {
//            if (element.getParent() != null && instanceOf(element.getParent(), (String[])("com.intellij.psi.javadoc.PsiDocTag", "com.intellij.psi.PsiComment", "com.intellij.lang.javascript.psi.jsdoc.impl.JSDocCommentImpl"))) {
//                return true;
//            } else if (commentFlags.stream().anyMatch((e) -> element.getText().trim().startsWith(e))) {
//                return true;
//            } else {
//                if (element.getPrevSibling() != null && element.getPrevSibling() instanceof PsiComment) {
//                    TextRange range = element.getPrevSibling().getTextRange();
//                    if (range != null) {
//                        if (range.contains(editor.getCaretModel().getOffset())) {
//                            return true;
//                        }
//
//                        if (editor.getDocument().getLineNumber(range.getEndOffset()) == editor.getDocument().getLineNumber(editor.getCaretModel().getOffset())) {
//                            return true;
//                        }
//                    }
//                }
//
//                return element.getPrevSibling() != null && commentFlags.stream().anyMatch((e) -> element.getPrevSibling().getText().trim().startsWith(e)) && !instanceOf(element.getPrevSibling(), (String[])("com.intellij.psi.PsiMethod")) && editor.getDocument().getLineNumber(element.getPrevSibling().getTextOffset()) == editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
//            }
//        } else {
//            return true;
//        }
//    }
//
//    public static boolean isImportElement(PsiElement element, @NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(3);
//        }
//
//        if (element != null && instanceOf(element.getParent(), (String[])("com.intellij.psi.impl.source.PsiImportList", "com.goide.psi.impl.GoImportDeclarationImpl"))) {
//            return true;
//        } else {
//            String lineCode = getLineTextAtCaret(editor);
//            return lineCode.startsWith("import ") || lineCode.startsWith("from ") || lineCode.startsWith("using ");
//        }
//    }
//
//    public static String getLineTextAtCaret(@NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(4);
//        }
//
//        int caretPosition = editor.getCaretModel().getOffset();
//        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
//        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
//        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
//        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
//    }
//
//    public static String getLineTextAtCaret(@NotNull Editor editor, int caretPosition) {
//        if (editor == null) {
//            $$$reportNull$$$0(5);
//        }
//
//        if (caretPosition > editor.getDocument().getTextLength()) {
//            caretPosition = editor.getDocument().getTextLength();
//        }
//
//        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
//        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
//        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
//        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
//    }
//
//    public static boolean isLiteralElement(Editor editor, PsiElement element) {
//        if (element == null) {
//            return false;
//        } else if (instanceOf(element.getParent(), (String[])("com.intellij.lang.javascript.psi.ecma6.impl.JSXXmlLiteralExpressionImpl"))) {
//            return false;
//        } else if (instanceOf(element, (String[])("com.jetbrains.python.psi.PyStringElement", "com.intellij.psi.PsiLiteralValue"))) {
//            return true;
//        } else if (instanceOf(element.getParent(), (String[])("com.jetbrains.python.psi.PyLiteralExpression", "com.goide.psi.impl.GoStringLiteralImpl", "com.jetbrains.rider.languages.fileTypes.csharp.psi.impl.CSharpNonInterpolatedStringLiteralExpressionImpl", "com.jetbrains.cidr.lang.psi.impl.OCLiteralExpressionImpl", "com.intellij.lang.javascript.psi.impl.JSLiteralExpressionImpl"))) {
//            return true;
//        } else if (element.getParent() instanceof CompositeElement && ((CompositeElement)element.getParent()).getPsi() instanceof PsiLiteralValue) {
//            return true;
//        } else {
//            String text = element.getText();
//            if (StringUtils.isNotBlank(text) && text.startsWith("\"") && text.endsWith("\"")) {
//                return true;
//            } else {
//                Character typeChar = (Character)CosyCacheKeys.KEY_INPUT_TYPE_CHAR.get(editor);
//                if (typeChar == null || typeChar != '"' && typeChar != '\'') {
//                    return CompletionUtil.isFrontFile(editor.getDocument()) && instanceOf(element.getParent(), (String[])("com.intellij.lang.javascript.psi.e4x.impl.JSXmlAttributeValueImpl", "com.intellij.psi.impl.source.xml.XmlAttributeValueImpl"));
//                } else {
//                    return true;
//                }
//            }
//        }
//    }
//
//    public static PsiElement getCaratElement(Editor editor) {
//        if (editor.getProject() == null) {
//            return null;
//        } else {
//            CaretModel caretModel = editor.getCaretModel();
//            int offset = caretModel.getOffset();
//            PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
//            PsiElement psiElement = null;
//            if (psiFile != null && offset > 0) {
//                psiElement = findElementAtOffset(psiFile, offset);
//            }
//
//            return psiElement;
//        }
//    }
//
//    public static String getPsiMethodContent(Project project, PsiFile psiFile, SelectionModel selectionModel) {
//        String result = selectionModel.getSelectedText();
//        if (psiFile == null) {
//            return result;
//        } else {
//            try {
//                Class.forName("com.intellij.psi.PsiMethod");
//                return JavaPsiUtils.getPsiMethodContent(project, psiFile, selectionModel);
//            } catch (ClassNotFoundException var5) {
//                return result;
//            }
//        }
//    }
//
//    public static String getPsiClassName(PsiFile psiFile, SelectionModel selectionModel) {
//        if (psiFile == null) {
//            return "DemoClass";
//        } else {
//            try {
//                Class.forName("com.intellij.psi.PsiClass");
//                return JavaPsiUtils.getPsiClassName(psiFile, selectionModel);
//            } catch (ClassNotFoundException var3) {
//                return "DemoClass";
//            }
//        }
//    }
//
//    public static String getPsiDocContent(Project project, PsiFile psiFile, SelectionModel selectionModel) {
//        if (psiFile == null) {
//            return selectionModel.getSelectedText();
//        } else {
//            int start = selectionModel.getSelectionStart();
//            int end = selectionModel.getSelectionEnd();
//            PsiElement startElement = psiFile.findElementAt(start);
//            PsiElement endElement = psiFile.findElementAt(end);
//            PsiComment endComment = (PsiComment)PsiTreeUtil.getParentOfType(endElement, PsiComment.class, false);
//            PsiComment startComment = (PsiComment)PsiTreeUtil.getParentOfType(startElement, PsiComment.class, false);
//            if (endComment != null) {
//                try {
//                    Class.forName("com.intellij.psi.PsiMethod");
//                    return JavaPsiUtils.getFollowingMethodSignatureFromComment(endComment);
//                } catch (ClassNotFoundException var10) {
//                    return endComment.getText();
//                }
//            } else if (startComment != null) {
//                try {
//                    Class.forName("com.intellij.psi.PsiMethod");
//                    return JavaPsiUtils.getFollowingMethodSignatureFromComment(startComment);
//                } catch (ClassNotFoundException var11) {
//                    return startComment.getText();
//                }
//            } else {
//                return selectionModel.getSelectedText();
//            }
//        }
//    }
//
//    private static String getFullDocComment(PsiComment psiComment) {
//        if (psiComment == null) {
//            return "";
//        } else {
//            PsiDocComment psiDocComment = (PsiDocComment)PsiTreeUtil.getParentOfType(psiComment, PsiDocComment.class, true);
//            return ((PsiComment)Objects.requireNonNullElse(psiDocComment, psiComment)).getText();
//        }
//    }
//
//    public static Boolean containsCode(Project project, PsiFile psiFile, SelectionModel selectionModel) {
//        if (psiFile == null) {
//            return false;
//        } else {
//            int start = selectionModel.getSelectionStart();
//            int end = selectionModel.getSelectionEnd();
//
//            PsiElement curElement;
//            for(int pivot = start; pivot < end; pivot = curElement.getTextRange().getEndOffset()) {
//                curElement = psiFile.findElementAt(pivot);
//                if (curElement == null) {
//                    return false;
//                }
//
//                if (curElement.getText().length() > 1 && !isInvalidCodeElement(curElement)) {
//                    Logger var10000 = log;
//                    String var10001 = curElement.getText();
//                    var10000.info("Valid element: " + var10001 + ", type=" + curElement.getNode().getElementType());
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }
//
//    public static boolean isInvalidCodeElement(PsiElement curElement) {
//        try {
//            Class.forName("com.intellij.psi.PsiJavaToken");
//            return JavaPsiUtils.isInvalidCodeElement(curElement);
//        } catch (ClassNotFoundException var2) {
//            return INVALID_COMMON_TOKENSET.contains(curElement.getNode().getElementType()) || curElement instanceof PsiComment || curElement instanceof PsiWhiteSpace || curElement instanceof PsiPlainText || curElement instanceof PsiLiteralValue || curElement instanceof PsiErrorElement;
//        }
//    }
//
//    public static PsiElement findPrevAtOffset(PsiFile psiFile, int caretOffset, Class... toSkip) {
//        if (caretOffset < 0) {
//            return null;
//        } else {
//            int lineStartOffset = 0;
//            Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);
//            if (document != null) {
//                int lineNumber = document.getLineNumber(caretOffset);
//                lineStartOffset = document.getLineStartOffset(lineNumber);
//            }
//
//            PsiElement element;
//            do {
//                --caretOffset;
//                element = psiFile.findElementAt(caretOffset);
//            } while(caretOffset >= lineStartOffset && (element == null || instanceOf(element, (Class[])toSkip)));
//
//            return instanceOf(element, (Class[])toSkip) ? null : element;
//        }
//    }
//
//    public static PsiElement findNonWhitespaceAtOffset(PsiFile psiFile, int caretOffset) {
//        PsiElement element = findNextAtOffset(psiFile, caretOffset, PsiWhiteSpace.class);
//        if (element == null) {
//            element = findPrevAtOffset(psiFile, caretOffset - 1, PsiWhiteSpace.class);
//        }
//
//        return element;
//    }
//
//    public static PsiElement findElementAtOffset(PsiFile psiFile, int caretOffset) {
//        PsiElement element = findPrevAtOffset(psiFile, caretOffset);
//        if (element == null) {
//            element = findNextAtOffset(psiFile, caretOffset);
//        }
//
//        return element;
//    }
//
//    public static PsiElement findNextAtOffset(PsiFile psiFile, int caretOffset, Class... toSkip) {
//        PsiElement element = psiFile.findElementAt(caretOffset);
//        if (element == null) {
//            return null;
//        } else {
//            Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);
//            int lineEndOffset = 0;
//            if (document != null) {
//                int lineNumber = document.getLineNumber(caretOffset);
//                lineEndOffset = document.getLineEndOffset(lineNumber);
//            }
//
//            while(caretOffset < lineEndOffset && instanceOf(element, (Class[])toSkip)) {
//                ++caretOffset;
//                element = psiFile.findElementAt(caretOffset);
//            }
//
//            return instanceOf(element, (Class[])toSkip) ? null : element;
//        }
//    }
//
//    public static boolean instanceOf(Object obj, Class... possibleClasses) {
//        if (obj != null && possibleClasses != null) {
//            for(Class cls : possibleClasses) {
//                if (cls.isInstance(obj)) {
//                    return true;
//                }
//            }
//
//            return false;
//        } else {
//            return false;
//        }
//    }
//
//    public static boolean instanceOf(Object obj, String... possibleClassNames) {
//        if (obj != null && possibleClassNames != null) {
//            String allClassNames = (String)Arrays.stream(possibleClassNames).collect(Collectors.joining(","));
//            String cacheKey = String.format("%s:%s", obj.getClass().getName(), allClassNames);
//            Boolean result = (Boolean)classInstanceTypeCache.getIfPresent(cacheKey);
//            if (result != null) {
//                return result;
//            } else {
//                result = internalInstanceOf(obj, possibleClassNames);
//                classInstanceTypeCache.put(cacheKey, result);
//                return result;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    private static boolean internalInstanceOf(Object obj, String... possibleClassNames) {
//        String objClassName = obj.getClass().getName();
//
//        for(String className : possibleClassNames) {
//            try {
//                if (className.equals(objClassName)) {
//                    return true;
//                }
//
//                if (!className.contains("$")) {
//                    Class<?> clazz = ReflectUtil.classForName(className);
//                    if (clazz != null && clazz.isInstance(obj)) {
//                        return true;
//                    }
//                }
//            } catch (ClassNotFoundException var8) {
//            } catch (Exception var9) {
//                log.debug("fail to instanceOf Class:" + className);
//            }
//        }
//
//        return false;
//    }
//
//    public static FileType getFileType() {
//        FileType fileType = FileTypes.PLAIN_TEXT;
//
//        for(String fileTypeClass : FILE_TYPE_CLASSES) {
//            try {
//                Class<?> clazz = ReflectUtil.classForName(fileTypeClass);
//                if (clazz != null) {
//                    fileType = (FileType)ReflectUtil.getStaticField(clazz, "INSTANCE");
//                }
//            } catch (Exception var6) {
//            }
//        }
//
//        return fileType;
//    }
//
//    public static String getLanguageByIDE() {
//        if (CosyConfig.IDE_NAME.toLowerCase().contains("idea")) {
//            return "java";
//        } else if (CosyConfig.IDE_NAME.toLowerCase().contains("pycharm")) {
//            return "python";
//        } else if (CosyConfig.IDE_NAME.toLowerCase().contains("clion")) {
//            return "c";
//        } else if (CosyConfig.IDE_NAME.toLowerCase().contains("webstorm")) {
//            return "javascript";
//        } else {
//            return CosyConfig.IDE_NAME.toLowerCase().contains("goland") ? "go" : null;
//        }
//    }
//
//    public static String getLanguageByCurrentFile(Editor editor) {
//        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
//        VirtualFile virtualFile = fileDocumentManager.getFile(editor.getDocument());
//        if (virtualFile == null) {
//            return null;
//        } else {
//            String path = virtualFile.getPath();
//            int dotIndex = path.lastIndexOf(".");
//            return dotIndex > 0 ? path.substring(dotIndex + 1) : null;
//        }
//    }
//
//    public static String findErrorLineContent(Project project, Editor editor, int line) {
//        return findErrorLineContentByDefault(project, editor, line);
//    }
//
//    public static String findErrorLineContentByDefault(Project project, Editor editor, int line) {
//        while(line < editor.getDocument().getLineCount()) {
//            String lineContent = editor.getDocument().getText(new TextRange(editor.getDocument().getLineStartOffset(line), editor.getDocument().getLineEndOffset(line)));
//            ExceptionWorker.ParsedLine myInfo = ExceptionWorker.parseExceptionLine(lineContent);
//            if (myInfo != null && myInfo.fileName != null) {
//                String fileName = myInfo.fileName;
//                int documentLine = myInfo.lineNumber;
//                String classFullPath = lineContent.substring(myInfo.classFqnRange.getStartOffset(), myInfo.classFqnRange.getEndOffset());
//                List<VirtualFile> vFiles = new ArrayList(FilenameIndex.getVirtualFilesByName(project, fileName, GlobalSearchScope.projectScope(project)));
//                if (CollectionUtils.isEmpty(vFiles)) {
//                    ++line;
//                } else {
//                    VirtualFile vFile = findMostRelatedVirtualFile(vFiles, classFullPath);
//                    log.info("Find stacktrace related vfs " + vFile.getName());
//
//                    String var14;
//                    try {
//                        String content = new String(vFile.contentsToByteArray(true));
//                        Language language = LanguageUtil.getFileLanguage(vFile);
//                        String languageStr = null;
//                        if (language != null) {
//                            languageStr = language.getDisplayName().toLowerCase();
//                        }
//
//                        StringBuilder sb = getStringBuilder(content, documentLine, languageStr);
//                        var14 = sb.toString();
//                    } catch (IOException e) {
//                        log.error("vFile parse exception. ", e);
//                        continue;
//                    } finally {
//                        ++line;
//                    }
//
//                    return var14;
//                }
//            } else {
//                ++line;
//            }
//        }
//
//        return null;
//    }
//
//    public static VirtualFile findMostRelatedVirtualFile(List<VirtualFile> virtualFiles, String classFullPath) {
//        if (!CollectionUtils.isEmpty(virtualFiles) && classFullPath != null) {
//            for(VirtualFile virtualFile : virtualFiles) {
//                String vPath = virtualFile.getPath();
//                int extPos = vPath.lastIndexOf(".");
//                if (extPos > 0) {
//                    vPath = vPath.substring(0, extPos);
//                }
//
//                String vFileDotPath = vPath.replace("/", ".");
//                if (vFileDotPath.endsWith(classFullPath)) {
//                    return virtualFile;
//                }
//            }
//
//            return (VirtualFile)virtualFiles.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    public static @NotNull StringBuilder getStringBuilder(String content, int documentLine, String languageStr) {
//        String[] contentLines = content.split("\n");
//        StringBuilder sb = new StringBuilder();
//        sb.append("```");
//        if (StringUtils.isNotBlank(languageStr)) {
//            sb.append(languageStr);
//        }
//
//        sb.append("\n");
//        sb.append(findCompleteCodeBlock(contentLines, documentLine, "{", "}", 10));
//        sb.append("\n");
//        if (sb == null) {
//            $$$reportNull$$$0(6);
//        }
//
//        return sb;
//    }
//
//    public static String findCompleteCodeBlock(String[] contentLines, int documentLine, String blockStartSymbol, String blockEndSymbol, int maxSearchLine) {
//        int i = 0;
//
//        boolean found;
//        for(found = false; documentLine - i >= 0 && i < maxSearchLine && documentLine - i < contentLines.length; ++i) {
//            String line = contentLines[documentLine - i];
//            if (line.endsWith(blockStartSymbol)) {
//                found = true;
//                break;
//            }
//        }
//
//        int j = 0;
//        if (found) {
//            while(documentLine + j <= contentLines.length - 1 && j < maxSearchLine) {
//                String line = contentLines[documentLine + j];
//                if (line.endsWith(blockEndSymbol)) {
//                    break;
//                }
//
//                ++j;
//            }
//        } else {
//            j = maxSearchLine;
//        }
//
//        StringBuilder sb = new StringBuilder();
//
//        for(int k = Math.max(documentLine - i, 0); k <= Math.min(documentLine + j, contentLines.length - 1); ++k) {
//            sb.append(contentLines[k]);
//            sb.append("\n");
//        }
//
//        if (sb.length() > 1) {
//            sb.setLength(sb.length() - 1);
//        }
//
//        return sb.toString();
//    }
//
//    public static boolean hasErrors(final Project project, final Document document) {
//        return (Boolean)ApplicationManager.getApplication().runReadAction(new Computable<Boolean>() {
//            public Boolean compute() {
//                Collection<HighlightInfo> highlightInfos = DaemonCodeAnalyzerImpl.getHighlights(document, HighlightSeverity.ERROR, project);
//                return highlightInfos != null && highlightInfos.size() > 0;
//            }
//        });
//    }
//
//    static {
//        INVALID_COMMON_TOKENSET = TokenSet.create(new IElementType[]{TokenType.BAD_CHARACTER, TokenType.WHITE_SPACE, TokenType.NEW_LINE_INDENT, TokenType.ERROR_ELEMENT});
//        FILE_TYPE_CLASSES = new String[]{"com.intellij.ide.highlighter.JavaFileType", "com.jetbrains.python.PythonFileType", "com.goide.GoFileType"};
//        classInstanceTypeCache = Caffeine.newBuilder().maximumSize(3000L).build();
//    }
//}
