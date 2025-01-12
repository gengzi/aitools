package com.gengzi.ui.config;

import com.gengzi.ui.local.Constant;
import com.gengzi.ui.save.MySettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigFrom implements SearchableConfigurable {
    private JPanel panel1;
    private JTextField qry_url;
    private JLabel label;
    private JButton commit;
    private JTextField apiKey;

    public ConfigFrom() {
        init();
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = qry_url.getText();
                String apiKeyText = apiKey.getText();
                System.out.println("url:" + url + " apiKey:" + apiKeyText);
                MySettings settings = MySettings.getInstance();
                settings.componentStates.put(Constant.API_KEY, apiKeyText);
                settings.componentStates.put(Constant.URL_kEY, url);
                settings.loadState(settings);
            }
        });
    }

    private void init() {
        MySettings settings = MySettings.getInstance();
        String url = settings.componentStates.get(Constant.URL_kEY);
        String apiKeyText = settings.componentStates.get(Constant.API_KEY);
        if (url != null) {
            qry_url.setText(url);
        }
        if (apiKeyText != null) {
            apiKey.setText(apiKeyText);
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
        return this.panel1;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
