package com.gengzi.ui.config;

import com.gengzi.ui.enums.ModelTypeEnum;
import com.gengzi.ui.local.Constant;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 配置页面
 * 配置大模型相关参数
 */
public class ConfigFrom implements SearchableConfigurable {

    private JPanel settingPanel;
    private JBRadioButton localModelRadio;
    private JBRadioButton severModelRadio;
    private JBTextField modelNameTextField;
    private JBTextField qryUrlTextField;
    private JBPasswordField apiKeyPassWrodField;
    private JButton commitButton;
    private JTextArea textArea1;

    public ConfigFrom() {
        // 获取已保持的配置信息
        init();

        commitButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = qryUrlTextField.getText();
                String apiKeyText = apiKeyPassWrodField.getText();
                String model = modelNameTextField.getText();
                MySettings settings = MySettings.getInstance();
                settings.componentStates.put(Constant.API_KEY, apiKeyText);
                settings.componentStates.put(Constant.URL_kEY, url);
                settings.componentStates.put(Constant.MODEL_KEY, model);
                boolean located = localModelRadio.isSelected();
                boolean server = severModelRadio.isSelected();
                if (located) {
                    settings.componentStates.put(Constant.MODEL_TYPE, ModelTypeEnum.LOCAL_MODEL.getDescription());
                }
                if (server) {
                    settings.componentStates.put(Constant.MODEL_TYPE, ModelTypeEnum.SERVER_MODEL.getDescription());
                }
                settings.loadState(settings);
            }
        });
    }

    private void init() {
        MySettings settings = MySettings.getInstance();
        final String urlText = settings.componentStates.get(Constant.URL_kEY);
        final String apiKeyText = settings.componentStates.get(Constant.API_KEY);
        final String modelKeyText = settings.componentStates.get(Constant.MODEL_KEY);
        final String modelTypeText = settings.componentStates.get(Constant.MODEL_TYPE);
        if (urlText != null) {
            qryUrlTextField.setText(urlText);
        }
        if (apiKeyText != null) {
            apiKeyPassWrodField.setText(apiKeyText);
        }
        if (modelKeyText != null) {
            modelNameTextField.setText(modelKeyText);
        }
        if (modelTypeText != null) {
            ModelTypeEnum modelTypeEnum = ModelTypeEnum.valueOf(modelTypeText);
            if(ModelTypeEnum.LOCAL_MODEL.equals(modelTypeEnum)){
                localModelRadio.setSelected(true);
            }
            if(ModelTypeEnum.SERVER_MODEL.equals(modelTypeEnum)){
                severModelRadio.setSelected(true);
            }
        }
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "config";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "配置";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this.settingPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
