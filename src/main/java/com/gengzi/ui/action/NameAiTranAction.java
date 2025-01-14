package com.gengzi.ui.action;

import com.gengzi.ui.local.Constant;
import com.gengzi.ui.openai.Curl;
import com.gengzi.ui.save.JsonContentExtractor;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class NameAiTranAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前项目实例
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        // 获取编辑器实例
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        // 获取文档实例，文档包含了编辑器中的所有文本内容
        Document document = editor.getDocument();

        // 获取所有的光标位置（通常在有多个选中区域时会有多个光标）
        List<Caret> allCarets = editor.getCaretModel().getAllCarets();
        for (Caret caret : allCarets) {
            // 获取当前光标下的选中区域范围
            TextRange selectedTextRange = caret.getSelectionRange();
            if (!selectedTextRange.isEmpty()) {
                // 获取选中的原始文本
                String selectedText = document.getText(selectedTextRange);
                // 在这里对选中的文本进行修改，例如将其转换为大写
//                String modifiedText = selectedText.toUpperCase();
                // 将修改后的文本替换原来选中的文本
                Application application = ApplicationManager.getApplication();
                application.runWriteAction(() -> {
                    MySettings state = MySettings.getInstance().getState();
                    String url = state.componentStates.get(Constant.URL_kEY);
                    String apikey = state.componentStates.get(Constant.API_KEY);
                    try {
                        StringBuilder req = new StringBuilder();
                        req.append("如下是一个字段名、类名、方法名等或者其他名称，请根据输入名称，输出一个合理准确的英文名称作为代码的一种名称：直接给我答案不要解释\n");
                        req.append(selectedText);
                        Stream<String> modelResponseTextStream = Curl.getModelResponseTextStream(url, apikey, req.toString());
                        StringBuilder sb = new StringBuilder();
                        modelResponseTextStream.forEach(v -> {
                            if ("data: [DONE]".equalsIgnoreCase(v)) {
                                return;
                            }
                            if (!"".equals(v)) {
                                String parseInputLine = JsonContentExtractor.parse(formatResponse(v));
                                sb.append(parseInputLine);
                            }
                        });
                        CommandProcessor.getInstance().executeCommand(project, () -> {
                            document.replaceString(selectedTextRange.getStartOffset(), selectedTextRange.getEndOffset(), sb.toString());
                        }, "My Command", null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                });
            }
        }


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
