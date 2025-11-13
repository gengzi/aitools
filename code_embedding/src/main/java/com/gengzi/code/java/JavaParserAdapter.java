package com.gengzi.code.java;


import com.gengzi.code.Parser;
import com.gengzi.entity.SimpleCodeElement;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * java 解析对象
 */
public class JavaParserAdapter implements Parser {

    private JavaParser javaParser;

    public JavaParserAdapter() {
        this.javaParser = new JavaParser();
    }

    @Override
    public SimpleCodeElement parse(String code) {
        SimpleCodeElement simpleCodeElement = new SimpleCodeElement();
        // 解析并赋值
        ParseResult<CompilationUnit> compilationUnitParseResult = javaParser.parse(code);

        compilationUnitParseResult.ifSuccessful(
                cu -> {
                    // 获取导入的包信息
                    ArrayList<String> imports = new ArrayList<>();

                    for (ImportDeclaration importDeclaration : cu.getImports()) {
                        imports.add(importDeclaration.toString());
                    }
                    simpleCodeElement.setImprots(imports);
                    List<ClassOrInterfaceDeclaration> classDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
                    if(!classDeclarations.isEmpty()){
                        ClassOrInterfaceDeclaration mainClass  = classDeclarations.get(0);
                        // 获取属性信息
                        ArrayList<String> fields = new ArrayList<>();
                        List<FieldDeclaration> fieldDeclarations = mainClass.getFields();
                        for (FieldDeclaration field : fieldDeclarations) {
                            String fieldCode = field.toString();
                            String fieldComment = getComment(field);
                            String fieldInfo = fieldComment.isBlank() ? fieldCode : "//" + fieldComment + "\n" + fieldCode;
                            fields.add(fieldInfo);
                        }
                        simpleCodeElement.setAttributes(fields);
                        // 获取一般方法
                        // 查找方法声明
                        ArrayList<String> methods = new ArrayList<>();
                        List<MethodDeclaration> methodDeclarations = mainClass.getMethods();
                        for (MethodDeclaration method : methodDeclarations) {
                            String methodCode = method.toString();
                            String methodComment = getComment(method);
                            methods.add(methodComment.isBlank()? methodCode :"//"+ methodCode + "\n" + methodComment);
                        }
                        // 查找构造方法声明
                        List<ConstructorDeclaration> constructorDeclarations = mainClass.getConstructors();
                        for (ConstructorDeclaration constructor : constructorDeclarations) {
                            String constructorCode = constructor.toString();
                            String constructorComment = getComment(constructor);
                            methods.add(constructorComment.isBlank()? constructorCode :"//"+ constructorCode + "\n" + constructorComment);
                        }
                        simpleCodeElement.setMethodCodes(methods);

                        // 获取的那个类名
                        SimpleName name = mainClass.getName();
                        String body = mainClass.toString();

                        simpleCodeElement.setClassName(name.asString());
                        simpleCodeElement.setBody(body);
                    }
                    // 获取内部类
                }
        );


        return simpleCodeElement;
    }

    private static String getComment(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.getComment().isPresent() && bodyDeclaration.getComment().get() instanceof JavadocComment) {
            return bodyDeclaration.getComment().get().getContent();
        }
        return "";
    }

    public static void main(String[] args) {
        JavaParserAdapter javaParserAdapter = new JavaParserAdapter();

        SimpleCodeElement parse = javaParserAdapter.parse("package com.gengzi.ui.util;\n" +
                "\n" +
                "import java.util.regex.Matcher;\n" +
                "import java.util.regex.Pattern;\n" +
                "\n" +
                "public class BacktickCounterRegex {\n" +
                "    public static int countBackticksWithRegex(String input) {\n" +
                "        // 定义正则表达式，匹配 ```\n" +
                "        String regex = \"```\";\n" +
                "        Pattern pattern = Pattern.compile(regex);\n" +
                "        Matcher matcher = pattern.matcher(input);\n" +
                "        int count = 0;\n" +
                "        // 循环查找匹配项\n" +
                "        while (matcher.find()) {\n" +
                "            count++;\n" +
                "        }\n" +
                "        return count;\n" +
                "    }\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        String str1 = \"This is a string ``` with some ``` backticks.\";\n" +
                "        String str2 = \"This is ``` a string ``` without ``` any ``` backticks.\";\n" +
                "        String str3 = \"No backticks here.\";\n" +
                "\n" +
                "        System.out.println(countBackticksWithRegex(str1)); // 输出 2\n" +
                "        System.out.println(countBackticksWithRegex(str2)); // 输出 4\n" +
                "        System.out.println(countBackticksWithRegex(str3)); // 输出 0\n" +
                "    }\n" +
                "}");


        System.out.printf(parse.toString());


    }




}
