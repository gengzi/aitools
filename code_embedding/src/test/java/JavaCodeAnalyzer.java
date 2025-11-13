import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;

import java.io.File;
import java.io.IOException;

public class JavaCodeAnalyzer {
    public static void main(String[] args) {
        try {
            // 读取 Java 文件
            File file = new File("E:\\project\\javaplugin\\aitools\\src\\main\\java\\com\\gengzi\\ui\\filecode\\UnifiedDiffViewerWithButtons.java");
            JavaParser javaParser = new JavaParser();
            ParseResult<CompilationUnit> result = javaParser.parse(file);

            CompilationUnit cu = result.getResult().get();
            // 获取类声明
            cu.getClassByName("UnifiedDiffViewerWithButtons").ifPresent(classDeclaration -> {
                // 打印类名
                System.out.println("类名: " + classDeclaration.getNameAsString());

                // 获取类的注释
                if (classDeclaration.getComment().isPresent() && classDeclaration.getComment().get() instanceof JavadocComment) {
                    JavadocComment javadocComment = (JavadocComment) classDeclaration.getComment().get();
                    System.out.println("类注释: " + javadocComment.getContent());
                }

                // 获取类的属性
                System.out.println("类的属性:");
                for (FieldDeclaration field : classDeclaration.getFields()) {
                    System.out.println("  - " + field.getVariables().get(0).getNameAsString());
                }

                classDeclaration.getConstructors().forEach(
                        constructor -> {
                            System.out.println("  - 构造方法: " + constructor.getNameAsString());
                            System.out.println("    参数: " + constructor.getParameters());
                            System.out.println("    方法体: " + constructor.getBody());
                        }
                );


                // 获取类的方法
                System.out.println("类的方法:");
                for (MethodDeclaration method : classDeclaration.getMethods()) {
                    System.out.println("  - 方法名: " + method.getNameAsString());
                    System.out.println("    返回类型: " + method.getType().asString());

                    // 获取方法的注释
                    if (method.getComment().isPresent() && method.getComment().get() instanceof JavadocComment) {
                        JavadocComment javadocComment = (JavadocComment) method.getComment().get();
                        System.out.println("    方法注释: " + javadocComment.getContent());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}