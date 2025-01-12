//package com.gengzi.ui.config;
//
//import com.alibaba.fastjson.JSON;
//import com.alibabacloud.intellij.cosy.common.BuildFeature;
//import com.alibabacloud.intellij.cosy.common.CosyCacheKeys;
//import com.alibabacloud.intellij.cosy.common.CosyConfig;
//import com.alibabacloud.intellij.cosy.common.CosySetting;
//import com.alibabacloud.intellij.cosy.common.VpcFeature;
//import com.alibabacloud.intellij.cosy.constants.LingmaUrls;
//import com.alibabacloud.intellij.cosy.core.Cosy;
//import com.alibabacloud.intellij.cosy.core.CosyStartupAdapter;
//import com.alibabacloud.intellij.cosy.core.CosyStartupListener;
//import com.alibabacloud.intellij.cosy.core.lsp.model.model.AuthStateEnum;
//import com.alibabacloud.intellij.cosy.core.lsp.model.model.AuthStatus;
//import com.alibabacloud.intellij.cosy.core.lsp.model.model.GlobalConfig;
//import com.alibabacloud.intellij.cosy.core.lsp.model.model.GlobalEndpointConfig;
//import com.alibabacloud.intellij.cosy.core.lsp.model.model.UpdateConfigResult;
//import com.alibabacloud.intellij.cosy.core.lsp.model.params.LoginParams;
//import com.alibabacloud.intellij.cosy.search.enums.CodeCompletionCandidateEnum;
//import com.alibabacloud.intellij.cosy.search.enums.CodeCompletionModeEnum;
//import com.alibabacloud.intellij.cosy.search.enums.CompletionGenerateLengthLevelEnum;
//import com.alibabacloud.intellij.cosy.search.enums.DocumentOpenModeEnum;
//import com.alibabacloud.intellij.cosy.search.enums.ExceptionResolveModeEnum;
//import com.alibabacloud.intellij.cosy.search.enums.LoginModeEnum;
//import com.alibabacloud.intellij.cosy.search.enums.MethodQuickSwitchEnum;
//import com.alibabacloud.intellij.cosy.search.enums.ModelPowerLevelEnum;
//import com.alibabacloud.intellij.cosy.search.enums.ProxyModeEnum;
//import com.alibabacloud.intellij.cosy.search.enums.UpgradeChecklEnum;
//import com.alibabacloud.intellij.cosy.service.UserAuthService;
//import com.alibabacloud.intellij.cosy.ui.notifications.NotificationFactory;
//import com.alibabacloud.intellij.cosy.ui.search.color.CosyColor;
//import com.alibabacloud.intellij.cosy.ui.search.location.CosyBundle;
//import com.alibabacloud.intellij.cosy.ui.search.topic.AuthLoginNotifier;
//import com.alibabacloud.intellij.cosy.ui.search.topic.AuthLogoutNotifier;
//import com.alibabacloud.intellij.cosy.ui.statusbar.CosyStatusBarWidget;
//import com.alibabacloud.intellij.cosy.util.ColorUtil;
//import com.alibabacloud.intellij.cosy.util.CosyBrowserUtil;
//import com.alibabacloud.intellij.cosy.util.IDEUtil;
//import com.alibabacloud.intellij.cosy.util.KeyboardUtil;
//import com.alibabacloud.intellij.cosy.util.LingmaAgentUtil;
//import com.alibabacloud.intellij.cosy.util.LoginUtil;
//import com.alibabacloud.intellij.cosy.util.ThreadUtil;
//import com.alibabacloud.intellij.cosy.util.UrlUtil;
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectUtil;
//import com.intellij.openapi.ui.ComboBox;
//import com.intellij.openapi.ui.MessageType;
//import com.intellij.openapi.ui.TextFieldWithBrowseButton;
//import com.intellij.openapi.util.SystemInfo;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.ui.JBColor;
//import com.intellij.ui.TitledSeparator;
//import com.intellij.ui.components.JBTextField;
//import com.intellij.util.Consumer;
//import com.intellij.util.messages.MessageBusConnection;
//import com.intellij.util.ui.SwingHelper;
//import com.intellij.util.ui.UIUtil;
//import icons.LingmaIcons;
//import java.awt.Component;
//import java.awt.Cursor;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//import java.util.Objects;
//import java.util.stream.Collectors;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.DefaultListCellRenderer;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JPanel;
//import javax.swing.JPasswordField;
//import javax.swing.JRadioButton;
//import javax.swing.JSlider;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
//import org.apache.commons.lang3.BooleanUtils;
//import org.apache.commons.lang3.StringUtils;
//
//public class ConfigMainForm implements Disposable {
//    private static final int MAX_ORG_NAME_DISPLAY_LENGTH = 24;
//    private JPanel mainPanel;
//    private JRadioButton completionAutoMode;
//    private JRadioButton completionSpeedMode;
//    private JRadioButton completionWholeLineMode;
//    private JComboBox<String> candidateNumberSelector;
//    private JRadioButton enableGenerateModeRadioButton;
//    private JButton btnLogin;
//    private JButton btnLogout;
//    private JLabel labelLoginState;
//    private JCheckBox localModelCheckBox;
//    private JCheckBox cloudModelCheckBox;
//    private JComboBox<KeyValue> autoTriggerLengthComboBox;
//    private JComboBox<KeyValue> manualTriggerLengthComboBox;
//    private JPanel cloudModelPanel;
//    private JPanel localModelPanel;
//    private JLabel privacyLabel;
//    private JLabel cloudManualShortcutLabel;
//    private JLabel loginWarnLabel;
//    private JPanel exceptionPanel;
//    private JPanel methodQuickPanel;
//    private JPanel auxiliaryPanel;
//    private TitledSeparator auxiliaryTitleSeparator;
//    private JCheckBox autoUpdateCheckBox;
//    private JTextField proxyUrlTextField;
//    private JRadioButton systemProxyConfigurationRadioButton;
//    private JRadioButton manualProxyConfigurationRadioButton;
//    private JPanel manualProxyUrlPanel;
//    private JRadioButton loggedInByAccountRadioButton;
//    private JRadioButton loggedInByAKRadioButton;
//    private JTextField akTextField;
//    private JPasswordField skTextField;
//    private JButton akLogInButton;
//    private JPanel akConfigPanel;
//    private JPanel loginModeConfigPanel;
//    private JLabel getAkLabel;
//    private JCheckBox showInlineCheckBox;
//    private JComboBox<Object> orgIdComboBox;
//    private JTextField langTriggerTextField;
//    private JRadioButton logInByAccessTokenRadioButton;
//    private JPanel tokenConfigPanel;
//    private JTextField accessTokenTextField;
//    private JComboBox<Object> orgIdTokenComboBox;
//    private JButton tokenLogInButton;
//    private JPanel tokenConfigHeaderPanel;
//    private JPanel akConfigHeaderPanel;
//    private JPanel privacyPanel;
//    private TitledSeparator privacyTitleLabel;
//    private TextFieldWithBrowseButton localStoragePathField;
//    private JRadioButton aliyunAccountRadioButton;
//    private JRadioButton dedicatedDomainRadioButton;
//    private JTextField dedicatedUrlField;
//    private JButton dedicatedLoginBtn;
//    private JPanel accountPanel;
//    private JLabel dedicatedLabel;
//    private JPanel dedicatedPanel;
//    private JPanel dedicatedConfigPanel;
//    private JLabel aliyunAccountRadioLabel;
//    private JCheckBox exceptionSolvingAssistantCheckBox;
//    private JCheckBox inlineCommandsAboveMethodCheckBox;
//    private JRadioButton chineseChatRadioButton;
//    private JRadioButton englishChatRadioButton;
//    private JRadioButton chineseCommitMessageRadioButton;
//    private JRadioButton englishCommitMessageRadioButton;
//    private JRadioButton displaySystemDefaultLanguageRadioButton;
//    private JRadioButton displayChineseRadioButton;
//    private JRadioButton displayEnglishRadioButton;
//    private JLabel preferredLanguageLabelEmpty;
//    private JLabel commitMessageLabel;
//    private ConfigAccessKeyUI configAccessKeyUI;
//    private MessageBusConnection messageBusConnection;
//    private static final String systemDefaultLang = Locale.getDefault().getLanguage();
//    private static final Logger log = Logger.getInstance(ConfigMainForm.class);
//
//    public JPanel getMainPanel() {
//        return this.mainPanel;
//    }
//
//    public ConfigMainForm() {
//        this.autoTriggerLengthComboBox.setModel(new DefaultComboBoxModel((KeyValue[])autoGenLengthList.toArray(new KeyValue[autoGenLengthList.size()])));
//        this.manualTriggerLengthComboBox.setModel(new DefaultComboBoxModel((KeyValue[])manualGenLengthList.toArray(new KeyValue[manualGenLengthList.size()])));
//        this.configCombox(this.autoTriggerLengthComboBox);
//        this.configCombox(this.manualTriggerLengthComboBox);
//        Project project = ProjectUtil.guessCurrentProject(this.getMainPanel());
//        this.messageBusConnection = project.getMessageBus().connect();
//        this.messageBusConnection.subscribe(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION, this::notifyLoginAuth);
//        this.messageBusConnection.subscribe(AuthLogoutNotifier.AUTH_LOGOUT_NOTIFICATION, this::notifyLogoutAuth);
//        this.initLoginPanel(project);
//        this.privacyLabel.setForeground(ColorUtil.getLinkForegroundColor());
//        this.privacyLabel.setCursor(Cursor.getPredefinedCursor(12));
//        this.privacyLabel.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                CosyBrowserUtil.browse(LingmaUrls.PRIVACY_URL);
//            }
//        });
//        this.localModelCheckBox.addChangeListener((e) -> {
//            if (this.localModelCheckBox.isSelected()) {
//                this.localModelPanel.setVisible(true);
//                this.localModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
//            } else {
//                this.localModelPanel.setVisible(false);
//                this.localModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
//            }
//
//        });
//        this.cloudModelCheckBox.addChangeListener((e) -> {
//            if (this.cloudModelCheckBox.isSelected()) {
//                this.cloudModelPanel.setVisible(true);
//                this.cloudModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
//            } else {
//                this.cloudModelPanel.setVisible(false);
//                this.cloudModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
//            }
//
//        });
//        this.exceptionSolvingAssistantCheckBox.addChangeListener((e) -> {
//            if (this.exceptionSolvingAssistantCheckBox.isSelected()) {
//                this.enableGenerateModeRadioButton.setEnabled(true);
//            } else {
//                this.enableGenerateModeRadioButton.setEnabled(false);
//            }
//
//        });
//        String triggerShortcut = KeyboardUtil.getShortcutText("TriggerInlayCompletionAction");
//        this.cloudManualShortcutLabel.setText(String.format(CosyBundle.message("settings.cloud.manual.shortcut.text", new Object[0]), triggerShortcut));
//        if (IDEUtil.IDE_ONLY_WITH_METHOD_QUICK_PANEL.stream().anyMatch((ide) -> CosyConfig.IDE_NAME.toLowerCase(Locale.ROOT).contains(ide))) {
//            this.exceptionPanel.setVisible(false);
//        } else if (!CosyConfig.IDE_NAME.toLowerCase(Locale.ROOT).contains("idea")) {
//            this.auxiliaryPanel.setVisible(false);
//            this.auxiliaryTitleSeparator.setVisible(false);
//        }
//
//        if (CosyConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
//            this.privacyPanel.setVisible(false);
//            this.privacyTitleLabel.setVisible(false);
//            this.exceptionPanel.setVisible(false);
//            List<String> features = new ArrayList();
//            List<String> var6 = CosyConfig.getFeature(BuildFeature.VPC_ENABLED_FEATURES.getKey(), features);
//            if (!var6.contains(VpcFeature.FEATURE_COMMIT_MESSAGE.getFeature())) {
//                this.setCommitMessageConfigVisible(false);
//            }
//        }
//
//        this.initProxyPanel(project);
//        this.initLanguageSettings();
//        this.setUiText();
//    }
//
//    private void initLanguageSettings() {
//        this.initDisplayLanguage();
//        this.initChatLanguage();
//        this.initCommitMessageLanguage();
//    }
//
//    public void initDisplayLanguage() {
//        CosySetting settings = CosyPersistentSetting.getInstance().getState();
//        if (settings != null) {
//            this.setDisplayLanguage(settings.getDisplayLanguage());
//        } else {
//            this.setDisplayLanguage(Locale.getDefault().getLanguage());
//        }
//
//        this.displaySystemDefaultLanguageRadioButton.addActionListener((e) -> this.setDisplayLanguage("default"));
//        this.displayChineseRadioButton.addActionListener((e) -> this.setDisplayLanguage(Locale.CHINESE.getLanguage()));
//        this.displayEnglishRadioButton.addActionListener((e) -> this.setDisplayLanguage(Locale.ENGLISH.getLanguage()));
//    }
//
//    private void initChatLanguage() {
//        CosySetting settings = CosyPersistentSetting.getInstance().getState();
//        if (settings != null) {
//            this.setChatLanguage(settings.getChatLanguage());
//        } else {
//            this.setChatLanguage(Locale.CHINESE.getLanguage());
//        }
//
//        this.chineseChatRadioButton.addActionListener((e) -> this.setChatLanguage(Locale.CHINESE.getLanguage()));
//        this.englishChatRadioButton.addActionListener((e) -> this.setChatLanguage(Locale.ENGLISH.getLanguage()));
//    }
//
//    private void initCommitMessageLanguage() {
//        CosySetting settings = CosyPersistentSetting.getInstance().getState();
//        if (settings != null) {
//            this.setCommitMessageLanguage(settings.getCommitMessageLanguage());
//        } else {
//            this.setCommitMessageLanguage(Locale.CHINESE.getLanguage());
//        }
//
//        this.chineseCommitMessageRadioButton.addActionListener((e) -> this.setCommitMessageLanguage(Locale.CHINESE.getLanguage()));
//        this.englishCommitMessageRadioButton.addActionListener((e) -> this.setCommitMessageLanguage(Locale.ENGLISH.getLanguage()));
//    }
//
//    private void setCommitMessageConfigVisible(boolean flag) {
//        this.preferredLanguageLabelEmpty.setVisible(flag);
//        this.commitMessageLabel.setVisible(flag);
//        this.chineseCommitMessageRadioButton.setVisible(flag);
//        this.englishCommitMessageRadioButton.setVisible(flag);
//    }
//
//    private void initProxyPanel(final Project project) {
//        this.systemProxyConfigurationRadioButton.addActionListener((e) -> this.manualProxyUrlPanel.setVisible(false));
//        this.manualProxyConfigurationRadioButton.addActionListener((e) -> this.manualProxyUrlPanel.setVisible(true));
//        CosyStartupListener startupListener = new CosyStartupAdapter() {
//            public void onStartup() {
//                ConfigMainForm.log.debug("start to get global config");
//                if (Cosy.INSTANCE.getLanguageService(project) != null) {
//                    GlobalConfig globalConfig = Cosy.INSTANCE.getLanguageService(project).getGlobalConfig(2000L);
//                    if (globalConfig != null) {
//                        CosyCacheKeys.KEY_GLOBAL_CONFIG.set(project, globalConfig);
//                        SwingUtilities.invokeLater(() -> {
//                            ConfigMainForm.log.debug("get global config:" + globalConfig);
//                            ConfigMainForm.this.setProxyMode(globalConfig.getProxyMode());
//                            ConfigMainForm.this.setProxyUrl(globalConfig.getHttpProxy());
//                        });
//                    }
//
//                    GlobalEndpointConfig endpointConfig = Cosy.INSTANCE.getLanguageService(project).getEndpointConfig(2000L);
//                    if (endpointConfig != null) {
//                        CosyCacheKeys.KEY_ENDPOINT_CONFIG.set(project, endpointConfig);
//                        SwingUtilities.invokeLater(() -> {
//                            ConfigMainForm.log.debug("get endpoint config:" + endpointConfig);
//                            ConfigMainForm.this.setEndpoint(endpointConfig.getEndpoint());
//                        });
//                    } else {
//                        LingmaAgentUtil.addDefaultEndpointToCache(project);
//                    }
//                }
//
//            }
//        };
//        if (Cosy.INSTANCE.checkCosy(project, true, List.of(startupListener))) {
//            log.debug("get global config directly");
//            startupListener.onStartup();
//        } else {
//            GlobalConfig globalConfig = (GlobalConfig)CosyCacheKeys.KEY_GLOBAL_CONFIG.get(project);
//            if (globalConfig != null) {
//                log.debug("get global config from cache:" + globalConfig);
//                this.setProxyMode(globalConfig.getProxyMode());
//                this.setProxyUrl(globalConfig.getHttpProxy());
//            }
//        }
//
//    }
//
//    private void configCombox(JComboBox<KeyValue> comboBox) {
//        comboBox.setRenderer(new DefaultListCellRenderer() {
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                Component com = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                KeyValue pair = (KeyValue)value;
//                this.setText(pair.getLabel());
//                return com;
//            }
//        });
//    }
//
//    private void initModelSlider(JSlider modeLevel, JSlider lengthLevel, boolean isManual) {
//        modeLevel.addChangeListener((e) -> {
//            if (modeLevel.getValue() == ModelPowerLevelEnum.SMALL.getLevel()) {
//                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//                lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//                lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//            } else if (modeLevel.getValue() == ModelPowerLevelEnum.MIDDLE.getLevel()) {
//                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//                if (isManual) {
//                    lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_2.getLevel());
//                } else {
//                    lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_1.getLevel());
//                }
//
//                lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//            } else if (modeLevel.getValue() == ModelPowerLevelEnum.LARGE.getLevel()) {
//                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//                lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_3.getLevel());
//                if (isManual) {
//                    lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LEVEL_1.getLevel());
//                } else {
//                    lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
//                }
//            }
//
//        });
//    }
//
//    private void notifyLoginAuth(AuthStatus status) {
//        log.info("get login auth object:" + status);
//        CosyCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
//        if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
//            this.accountPanel.setVisible(false);
//            this.dedicatedDomainRadioButton.setVisible(false);
//            this.dedicatedConfigPanel.setVisible(false);
//            this.dedicatedLoginBtn.setVisible(false);
//            this.btnLogin.setVisible(false);
//            this.akLogInButton.setVisible(false);
//            this.btnLogout.setVisible(true);
//            this.cloudModelCheckBox.setEnabled(true);
//            this.cloudModelPanel.setVisible(true);
//            this.loginWarnLabel.setVisible(false);
//            this.labelLoginState.setForeground(CosyColor.GREEN_TEXT_COLOR);
//            String whiteListText = LoginUtil.getWhitelistText(status);
//            String loginInfo = String.format(CosyBundle.message("settings.login.account.tips.logged", new Object[0]), status.getName(), status.getId(), whiteListText);
//            if (CosyConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
//                loginInfo = String.format(CosyBundle.message("settings.login.account.tips.logged.vpc", new Object[0]), status.getName(), whiteListText);
//            }
//
//            String orgName = status.getOrgName();
//            if (StringUtils.isNotBlank(orgName)) {
//                if (orgName.length() > 24) {
//                    orgName = orgName.substring(0, 24) + "...";
//                }
//
//                loginInfo = loginInfo + String.format(CosyBundle.message("settings.login.account.tips.org.label", new Object[0]), orgName);
//            }
//
//            this.labelLoginState.setText(loginInfo);
//            this.loginModeConfigPanel.setVisible(false);
//            this.tokenLogInButton.setVisible(false);
//        } else {
//            this.accountPanel.setVisible(true);
//            this.dedicatedDomainRadioButton.setVisible(true);
//            this.aliyunAccountRadioButton.setVisible(true);
//            this.labelLoginState.setForeground(CosyColor.GRAY_TXT_COLOR);
//            this.labelLoginState.setText(CosyBundle.messageVpc("settings.login.account.tips.not.logged", new Object[0]));
//            CosySetting settings = CosyPersistentSetting.getInstance().getState();
//            if (settings != null && LoginModeEnum.DEDICATED.getLabel().equalsIgnoreCase(settings.getLoginMode())) {
//                this.dedicatedLoginBtn.setVisible(true);
//                this.akLogInButton.setVisible(false);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.loginModeConfigPanel.setVisible(false);
//                this.dedicatedConfigPanel.setVisible(true);
//            } else if (settings != null && LoginModeEnum.ACCESS_KEY.getLabel().equals(settings.getLoginMode())) {
//                this.akLogInButton.setVisible(true);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.dedicatedLoginBtn.setVisible(false);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            } else if (settings != null && LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(settings.getLoginMode())) {
//                this.tokenLogInButton.setVisible(true);
//                this.akLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.dedicatedLoginBtn.setVisible(false);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            } else {
//                this.akLogInButton.setVisible(false);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(true);
//                this.dedicatedLoginBtn.setVisible(false);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            }
//
//            this.btnLogout.setVisible(false);
//            this.cloudModelCheckBox.setEnabled(false);
//            this.cloudModelPanel.setVisible(false);
//            this.loginWarnLabel.setVisible(true);
//        }
//
//        this.btnLogin.setEnabled(true);
//        this.dedicatedLoginBtn.setEnabled(true);
//        this.btnLogout.setEnabled(true);
//        this.configAccessKeyUI.checkAkskLoginButtonState();
//    }
//
//    private void notifyLogoutAuth(AuthStatus status) {
//        log.info("get logout auth object:" + status);
//        CosyCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
//        if (status != null && (status.getStatus() == null || status.getStatus() == AuthStateEnum.NOT_LOGIN.getValue())) {
//            this.labelLoginState.setForeground(CosyColor.GRAY_TXT_COLOR);
//            this.labelLoginState.setText(CosyBundle.messageVpc("settings.login.account.tips.not.logged", new Object[0]));
//            CosySetting settings = CosyPersistentSetting.getInstance().getState();
//            if (settings != null && LoginModeEnum.DEDICATED.getLabel().equals(settings.getLoginMode())) {
//                this.dedicatedLoginBtn.setVisible(true);
//                this.akLogInButton.setVisible(false);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.loginModeConfigPanel.setVisible(false);
//                this.dedicatedConfigPanel.setVisible(true);
//            } else if (settings != null && LoginModeEnum.ACCESS_KEY.getLabel().equals(settings.getLoginMode())) {
//                this.akLogInButton.setVisible(true);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            } else if (settings != null && LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(settings.getLoginMode())) {
//                this.tokenLogInButton.setVisible(true);
//                this.akLogInButton.setVisible(false);
//                this.btnLogin.setVisible(false);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            } else {
//                this.akLogInButton.setVisible(false);
//                this.tokenLogInButton.setVisible(false);
//                this.btnLogin.setVisible(true);
//                this.loginModeConfigPanel.setVisible(true);
//                this.dedicatedConfigPanel.setVisible(false);
//            }
//
//            this.accountPanel.setVisible(true);
//            this.dedicatedDomainRadioButton.setVisible(true);
//            this.aliyunAccountRadioButton.setVisible(true);
//            this.btnLogout.setVisible(false);
//            this.cloudModelCheckBox.setEnabled(false);
//            this.cloudModelPanel.setVisible(false);
//            this.loginWarnLabel.setVisible(true);
//        } else {
//            this.btnLogout.setVisible(true);
//            this.cloudModelCheckBox.setEnabled(true);
//            this.cloudModelPanel.setVisible(true);
//            this.loginWarnLabel.setVisible(false);
//            this.loginModeConfigPanel.setVisible(false);
//        }
//
//        this.btnLogin.setEnabled(true);
//        this.btnLogout.setEnabled(true);
//        this.dedicatedLoginBtn.setEnabled(true);
//        this.configAccessKeyUI.checkAkskLoginButtonState();
//    }
//
//    private void initLoginPanel(Project project) {
//        if (CosyConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
//            this.configAccessKeyUI = new ConfigAccessKeyUI(project, this.accessTokenTextField, (JPasswordField)null, this.orgIdTokenComboBox, this.tokenLogInButton, LoginModeEnum.PERSONAL_TOKEN.getLabel());
//            this.dedicatedPanel.setVisible(false);
//            this.aliyunAccountRadioButton.setText(CosyBundle.messageVpc("settings.login.account.panel.title.vpc", new Object[0]));
//            this.aliyunAccountRadioLabel.setText("");
//        } else {
//            this.configAccessKeyUI = new ConfigAccessKeyUI(project, this.akTextField, this.skTextField, this.orgIdComboBox, this.akLogInButton, LoginModeEnum.ACCESS_KEY.getLabel());
//            this.dedicatedPanel.setVisible(true);
//        }
//
//        this.accountPanel.setVisible(true);
//        this.loginModeConfigPanel.setVisible(true);
//        this.akConfigPanel.setVisible(false);
//        this.loggedInByAccountRadioButton.setSelected(true);
//        this.loggedInByAKRadioButton.setSelected(false);
//        this.logInByAccessTokenRadioButton.setSelected(false);
//        this.loggedInByAccountRadioButton.addActionListener((e) -> {
//            this.akConfigPanel.setVisible(false);
//            this.tokenConfigPanel.setVisible(false);
//            this.btnLogin.setVisible(true);
//            this.btnLogin.setEnabled(true);
//            this.loggedInByAccountRadioButton.setSelected(true);
//            this.loggedInByAKRadioButton.setSelected(false);
//            this.logInByAccessTokenRadioButton.setSelected(false);
//            this.dedicatedDomainRadioButton.setSelected(false);
//        });
//        this.loggedInByAKRadioButton.addActionListener((e) -> {
//            this.akConfigPanel.setVisible(true);
//            this.btnLogin.setVisible(false);
//            this.akLogInButton.setVisible(true);
//            this.checkAndChangeEndpointWhenSelectAkLogin(project);
//            this.configAccessKeyUI.checkAkskLoginButtonState();
//            this.loggedInByAKRadioButton.setSelected(true);
//            this.loggedInByAccountRadioButton.setSelected(false);
//            this.logInByAccessTokenRadioButton.setSelected(false);
//            this.dedicatedDomainRadioButton.setSelected(false);
//        });
//        this.logInByAccessTokenRadioButton.addActionListener((e) -> {
//            this.tokenConfigPanel.setVisible(true);
//            this.btnLogin.setVisible(false);
//            this.akLogInButton.setVisible(false);
//            this.tokenLogInButton.setVisible(true);
//            this.logInByAccessTokenRadioButton.setSelected(true);
//            this.loggedInByAccountRadioButton.setSelected(false);
//            this.loggedInByAKRadioButton.setSelected(false);
//            this.dedicatedDomainRadioButton.setSelected(false);
//        });
//        this.akLogInButton.addActionListener((e) -> {
//            if (project != null) {
//                CosySetting setting = CosyPersistentSetting.getInstance().getState();
//                if (setting != null) {
//                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
//                    setting.setLoginMode(LoginModeEnum.ACCESS_KEY.getLabel());
//                } else {
//                    log.warn("Setting is null when ak login button entered.");
//                }
//
//                this.akLogInButton.setEnabled(false);
//                this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.logging.in", new Object[0]));
//                LoginParams loginParams = LoginParams.fromAccessKey(this.getLoginAccessKey(), this.getLoginSecretKey(), this.getOrganizationId());
//                UserAuthService.getInstance().login(project, this.labelLoginState, loginParams);
//            }
//
//        });
//        this.tokenLogInButton.addActionListener((e) -> {
//            if (project != null) {
//                CosySetting setting = CosyPersistentSetting.getInstance().getState();
//                if (setting != null) {
//                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
//                    setting.setLoginMode(LoginModeEnum.PERSONAL_TOKEN.getLabel());
//                } else {
//                    log.warn("Setting is null when token login button entered.");
//                }
//
//                this.tokenLogInButton.setEnabled(false);
//                this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.logging.in", new Object[0]));
//                LoginParams loginParams = LoginParams.fromPersonalToken(this.getLoginAccessToken(), this.getOrganizationIdForToken());
//                UserAuthService.getInstance().login(project, this.labelLoginState, loginParams);
//            }
//
//        });
//        this.getAkLabel.setForeground(ColorUtil.getLinkForegroundColor());
//        this.getAkLabel.setCursor(Cursor.getPredefinedCursor(12));
//        this.getAkLabel.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                CosyBrowserUtil.browse(LingmaUrls.GET_ACCESS_KEY_URL);
//            }
//        });
//        this.configAccessKeyUI.setup();
//        this.btnLogin.addActionListener((e) -> {
//            if (project != null) {
//                CosySetting setting = CosyPersistentSetting.getInstance().getState();
//                if (setting != null) {
//                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
//                    setting.setLoginMode(LoginModeEnum.ALIYUN_ACCOUNT.getLabel());
//                } else {
//                    log.warn("Setting is null when token login button entered.");
//                }
//
//                this.btnLogin.setEnabled(false);
//                this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.logging.in", new Object[0]));
//                UserAuthService.getInstance().login(project, this.labelLoginState);
//            }
//
//        });
//        this.btnLogout.addActionListener((e) -> {
//            if (project != null) {
//                this.btnLogout.setEnabled(false);
//                this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.logging.out", new Object[0]));
//                UserAuthService.getInstance().logout(project, this.btnLogout);
//            }
//
//        });
//        this.btnLogin.setVisible(false);
//        this.btnLogout.setVisible(false);
//        this.cloudModelPanel.setVisible(false);
//        this.loginWarnLabel.setForeground(ColorUtil.getInactiveForegroundColor());
//        this.loginWarnLabel.setIcon(LingmaIcons.WarningIcon);
//        this.loginWarnLabel.setText(CosyBundle.message("settings.cloud.login.require.tips", new Object[0]));
//        this.loginWarnLabel.setVisible(true);
//        this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.fetch.user", new Object[0]));
//        if (CosyConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
//            this.tokenConfigHeaderPanel.setVisible(true);
//            this.akConfigHeaderPanel.setVisible(false);
//        } else {
//            this.tokenConfigHeaderPanel.setVisible(false);
//            this.akConfigHeaderPanel.setVisible(true);
//        }
//
//        this.aliyunAccountRadioButton.setSelected(true);
//        this.dedicatedDomainRadioButton.setSelected(false);
//        this.dedicatedConfigPanel.setVisible(false);
//        CosySetting cosySetting = CosyPersistentSetting.getInstance().getState();
//        if (cosySetting != null && StringUtils.isNotBlank(cosySetting.getDedicatedDomainUrl())) {
//            this.setDedicatedUrlField(cosySetting.getDedicatedDomainUrl());
//        }
//
//        this.aliyunAccountRadioButton.addActionListener((e) -> {
//            this.accountPanel.setVisible(true);
//            this.loginModeConfigPanel.setVisible(true);
//            this.aliyunAccountRadioButton.setSelected(true);
//            this.dedicatedConfigPanel.setVisible(false);
//            this.dedicatedDomainRadioButton.setSelected(false);
//            if (this.loggedInByAccountRadioButton.isSelected()) {
//                this.btnLogin.setVisible(true);
//            } else if (this.loggedInByAKRadioButton.isSelected()) {
//                this.loggedInByAccountRadioButton.setSelected(true);
//                this.loggedInByAKRadioButton.setSelected(false);
//                this.akConfigPanel.setVisible(false);
//                this.btnLogin.setVisible(true);
//                this.tokenLogInButton.setVisible(false);
//            } else if (this.logInByAccessTokenRadioButton.isSelected()) {
//                this.btnLogin.setVisible(false);
//                this.akLogInButton.setVisible(false);
//                this.tokenLogInButton.setVisible(true);
//            }
//
//        });
//        this.dedicatedDomainRadioButton.addActionListener((e) -> {
//            this.dedicatedLoginBtn.setEnabled(true);
//            this.loginModeConfigPanel.setVisible(false);
//            this.dedicatedConfigPanel.setVisible(true);
//            this.aliyunAccountRadioButton.setSelected(false);
//            this.dedicatedDomainRadioButton.setSelected(true);
//            this.dedicatedLoginBtn.setVisible(true);
//        });
//        this.dedicatedLoginBtn.addActionListener((e) -> {
//            CosySetting setting = CosyPersistentSetting.getInstance().getState();
//            this.dedicatedLoginBtn.setEnabled(false);
//            this.labelLoginState.setText(CosyBundle.message("settings.login.account.tips.state.logging.in", new Object[0]));
//            String dedicatedDomainUrl = this.getDedicatedUrlField(true);
//            if (StringUtils.isNotBlank(dedicatedDomainUrl)) {
//                if (setting != null) {
//                    GlobalEndpointConfig endpointConfig = LingmaAgentUtil.getGlobalEndpointConfigDirectly(project);
//                    boolean differentEndpointUrl = endpointConfig != null && endpointConfig.getEndpoint() != null && !endpointConfig.getEndpoint().equals(dedicatedDomainUrl);
//                    if (differentEndpointUrl) {
//                        log.info("Log in by dedicated login button. Detected dedicated domain url changed, update lingma endpoint config.");
//                        this.updateLingmaEndpointConfig(project);
//                    } else {
//                        log.info("Log in by dedicated login button. Don't need update endpoint, directly login.");
//                    }
//
//                    setting.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
//                    setting.setDedicatedDomainUrl(dedicatedDomainUrl);
//                }
//
//                if (project != null) {
//                    UserAuthService.getInstance().login(project, this.labelLoginState, LoginParams.fromDedicatedDomain());
//                }
//            } else {
//                this.dedicatedLoginBtn.setEnabled(true);
//            }
//
//        });
//        AuthStatus status = UserAuthService.getInstance().getState(project);
//        if (status != null) {
//            this.notifyLoginAuth(status);
//        }
//
//    }
//
//    private void checkAndChangeEndpointWhenSelectAkLogin(Project project) {
//        if (project == null) {
//            log.warn("project is null when checkAndChangeEndpointWhenSelectAkLogin");
//        } else {
//            CosySetting setting = CosyPersistentSetting.getInstance().getState();
//            if (setting != null) {
//                log.info("checkAndChangeEndpointWhenSelectAkLogin");
//                this.checkEndpointConfigWhenAliyunLogin(project, setting);
//                setting.setLoginMode(LoginModeEnum.ACCESS_KEY.getLabel());
//            }
//
//        }
//    }
//
//    private boolean checkEndpointConfigWhenAliyunLogin(Project project, CosySetting setting) {
//        GlobalEndpointConfig endpointConfig = LingmaAgentUtil.getGlobalEndpointConfigDirectly(project);
//        boolean hasEndpointConfig = endpointConfig != null && StringUtils.isNotBlank(endpointConfig.getEndpoint());
//        if (hasEndpointConfig) {
//            log.info("hasEndpointConfig when aliyun login, clear all endpoint settings.");
//            if (setting != null) {
//                setting.setDedicatedDomainUrl("");
//            }
//
//            this.dedicatedUrlField.setText("");
//            this.updateLingmaEndpointConfig(project);
//        } else {
//            log.info("No endpoint config, login directly.");
//        }
//
//        return hasEndpointConfig;
//    }
//
//    public void updateLingmaGlobalConfig(Project project) {
//        if (project != null) {
//            CosyStatusBarWidget.updateStatusBar((Consumer)null, project);
//            if (Cosy.INSTANCE.checkCosy(project)) {
//                GlobalConfig config = new GlobalConfig();
//                if (this.getProxyMode().getType().equals(ProxyModeEnum.MANUAL.getType())) {
//                    config.setProxyMode(this.getProxyMode().getType());
//                    config.setHttpProxy(this.getProxyUrl());
//                } else {
//                    config.setProxyMode(this.getProxyMode().getType());
//                }
//
//                ThreadUtil.execute(() -> Cosy.INSTANCE.getLanguageService(project).updateGlobalConfig(config, 2000L));
//            }
//        }
//
//    }
//
//    public void updateLingmaEndpointConfig(Project project) {
//        if (project != null) {
//            CosyStatusBarWidget.updateStatusBar((Consumer)null, project);
//            if (Cosy.INSTANCE.checkCosy(project)) {
//                GlobalEndpointConfig config = new GlobalEndpointConfig();
//                if (LoginModeEnum.DEDICATED.getLabel().equals(this.getLoginMode())) {
//                    config.setEndpoint(this.getDedicatedUrlField(false));
//                } else {
//                    config.setEndpoint("");
//                }
//
//                CosyCacheKeys.KEY_ENDPOINT_CONFIG.set(project, config);
//                UpdateConfigResult result = Cosy.INSTANCE.getLanguageService(project).updateEndpoint(config, 2000L);
//                if (!BooleanUtils.isTrue(result.getSuccess())) {
//                    Logger var10000 = log;
//                    String var10001 = JSON.toJSONString(config);
//                    var10000.warn("UpdateEndpointConfig error. config=" + var10001 + ", result=" + JSON.toJSONString(result));
//                } else {
//                    log.info("UpdateEndpointConfig success.");
//                }
//            }
//        }
//
//    }
//
//    public String getLoginMode() {
//        if (this.dedicatedDomainRadioButton.isSelected()) {
//            return LoginModeEnum.DEDICATED.getLabel();
//        } else {
//            return this.loggedInByAKRadioButton.isSelected() ? LoginModeEnum.ACCESS_KEY.getLabel() : LoginModeEnum.ALIYUN_ACCOUNT.getLabel();
//        }
//    }
//
//    public void setLoginMode(String mode) {
//        if (LoginModeEnum.ACCESS_KEY.getLabel().equals(mode)) {
//            this.loggedInByAKRadioButton.setSelected(true);
//            this.akConfigPanel.setVisible(true);
//            this.akLogInButton.setVisible(true);
//            this.btnLogin.setVisible(false);
//            this.tokenLogInButton.setVisible(false);
//            this.tokenConfigPanel.setVisible(false);
//            this.aliyunAccountRadioButton.setSelected(true);
//            this.dedicatedDomainRadioButton.setSelected(false);
//            this.dedicatedConfigPanel.setVisible(false);
//            this.loggedInByAccountRadioButton.setSelected(false);
//            this.loginModeConfigPanel.setVisible(true);
//        } else if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(mode)) {
//            this.logInByAccessTokenRadioButton.setSelected(true);
//            this.akConfigPanel.setVisible(false);
//            this.akLogInButton.setVisible(false);
//            this.btnLogin.setVisible(false);
//            this.tokenLogInButton.setVisible(true);
//            this.tokenConfigPanel.setVisible(true);
//            this.aliyunAccountRadioButton.setSelected(true);
//            this.dedicatedDomainRadioButton.setSelected(false);
//            this.dedicatedConfigPanel.setVisible(false);
//            this.loggedInByAccountRadioButton.setSelected(false);
//            this.loginModeConfigPanel.setVisible(true);
//        } else if (LoginModeEnum.DEDICATED.getLabel().equals(mode)) {
//            this.aliyunAccountRadioButton.setSelected(false);
//            this.dedicatedDomainRadioButton.setSelected(true);
//            this.dedicatedConfigPanel.setVisible(true);
//            this.loginModeConfigPanel.setVisible(false);
//            this.akConfigPanel.setVisible(false);
//            this.akLogInButton.setVisible(false);
//            this.btnLogin.setVisible(false);
//            this.tokenLogInButton.setVisible(false);
//            this.tokenConfigPanel.setVisible(false);
//            this.dedicatedLoginBtn.setVisible(true);
//        } else {
//            this.loggedInByAccountRadioButton.setSelected(true);
//            this.akConfigPanel.setVisible(false);
//            this.akLogInButton.setVisible(false);
//            this.btnLogin.setVisible(true);
//            this.tokenLogInButton.setVisible(false);
//            this.tokenConfigPanel.setVisible(false);
//            this.aliyunAccountRadioButton.setSelected(true);
//            this.dedicatedDomainRadioButton.setSelected(false);
//            this.dedicatedConfigPanel.setVisible(false);
//            this.loggedInByAKRadioButton.setSelected(false);
//            this.logInByAccessTokenRadioButton.setSelected(false);
//            this.loginModeConfigPanel.setVisible(true);
//        }
//
//        AuthStatus status = (AuthStatus)CosyCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
//        if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
//            this.loginModeConfigPanel.setVisible(false);
//            this.dedicatedConfigPanel.setVisible(false);
//        }
//
//    }
//
//    public String getLoginAccessKey() {
//        return this.akTextField.getText();
//    }
//
//    public String getLoginAccessToken() {
//        return this.accessTokenTextField.getText() == null ? "" : this.accessTokenTextField.getText();
//    }
//
//    public String getLoginSecretKey() {
//        return this.skTextField.getPassword() == null ? null : new String(this.skTextField.getPassword());
//    }
//
//    public String getOrganizationId() {
//        if (this.orgIdComboBox.getSelectedItem() != null && this.orgIdComboBox.getSelectedItem() instanceof AuthOrgItem) {
//            AuthOrgItem kv = (AuthOrgItem)this.orgIdComboBox.getSelectedItem();
//            return kv.getKey();
//        } else {
//            return null;
//        }
//    }
//
//    public String getOrganizationIdForToken() {
//        if (this.orgIdTokenComboBox.getSelectedItem() != null && this.orgIdTokenComboBox.getSelectedItem() instanceof AuthOrgItem) {
//            AuthOrgItem kv = (AuthOrgItem)this.orgIdTokenComboBox.getSelectedItem();
//            return kv.getKey();
//        } else {
//            return null;
//        }
//    }
//
//    public void setLoginAccessKey(String ak) {
//        this.akTextField.setText(ak);
//    }
//
//    public void setLoginSecretKey(String sk) {
//        this.skTextField.setText(sk);
//    }
//
//    public String getCodeCompletionMode() {
//        if (this.completionAutoMode.isSelected()) {
//            return CodeCompletionModeEnum.AUTO.mode;
//        } else {
//            return this.completionSpeedMode.isSelected() ? CodeCompletionModeEnum.SPEED.mode : CodeCompletionModeEnum.LENGTH.mode;
//        }
//    }
//
//    public void setCodeCompletionMode(String mode) {
//        if (CodeCompletionModeEnum.AUTO.mode.equals(mode)) {
//            this.completionAutoMode.setSelected(true);
//        } else if (CodeCompletionModeEnum.SPEED.mode.equals(mode)) {
//            this.completionSpeedMode.setSelected(true);
//        } else {
//            this.completionWholeLineMode.setSelected(true);
//        }
//
//    }
//
//    public int getDefaultCodeCompletionCandidateMaxNum() {
//        try {
//            if (this.candidateNumberSelector.getSelectedItem() != null) {
//                int valueOfCandidateSettings = Integer.parseInt((String)this.candidateNumberSelector.getSelectedItem());
//                if (valueOfCandidateSettings < CodeCompletionCandidateEnum.MIN.num) {
//                    return CodeCompletionCandidateEnum.MIN.num;
//                }
//
//                if (valueOfCandidateSettings > CodeCompletionCandidateEnum.MAX.num) {
//                    return CodeCompletionCandidateEnum.MAX.num;
//                }
//
//                if (valueOfCandidateSettings != CodeCompletionCandidateEnum.DEFAULT.num) {
//                    return valueOfCandidateSettings;
//                }
//            }
//        } catch (NumberFormatException nfe) {
//            log.warn(String.format("Invalid candidate maximum number input: %s, error message: %s: ", this.candidateNumberSelector.getSelectedItem(), nfe.getMessage()));
//        }
//
//        return CodeCompletionCandidateEnum.DEFAULT.num;
//    }
//
//    public void setDefaultCodeCompletionCandidateMaxNum(int candidateNumber) {
//        this.candidateNumberSelector.setSelectedItem(String.valueOf(candidateNumber));
//    }
//
//    public DocumentOpenModeEnum getDocumentOpenMode() {
//        return DocumentOpenModeEnum.OPEN_IN_IDEA;
//    }
//
//    public void setDocumentOpenMode(DocumentOpenModeEnum mode) {
//    }
//
//    public boolean getCodeCompletionSwitch() {
//        return this.localModelCheckBox.isSelected();
//    }
//
//    public void setCodeCompletionSwitch(Boolean enabled) {
//        if (enabled != null && !enabled) {
//            this.localModelCheckBox.setSelected(false);
//            this.localModelPanel.setVisible(false);
//            this.localModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
//        } else {
//            this.localModelCheckBox.setSelected(true);
//            this.localModelPanel.setVisible(true);
//            this.localModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
//        }
//
//    }
//
//    public boolean getCloudCodeCompletionSwitch() {
//        return this.cloudModelCheckBox.isSelected();
//    }
//
//    public void setCloudCodeCompletionSwitch(Boolean enabled) {
//        if (enabled != null && !enabled) {
//            this.cloudModelCheckBox.setSelected(false);
//            this.cloudModelPanel.setVisible(false);
//            this.cloudModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
//        } else {
//            this.cloudModelCheckBox.setSelected(true);
//            if (this.cloudModelCheckBox.isEnabled()) {
//                this.cloudModelPanel.setVisible(true);
//            }
//
//            this.cloudModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
//        }
//
//    }
//
//    public boolean getEnableCloudAutoTrigger() {
//        KeyValue pair = (KeyValue)this.autoTriggerLengthComboBox.getSelectedItem();
//        return !pair.getKey().equals(CompletionGenerateLengthLevelEnum.NO.getLabel());
//    }
//
//    public void setEnableCloudAutoTrigger(Boolean enabled, String levelLabel) {
//        KeyValue pair = null;
//        if (enabled != null && !enabled) {
//            pair = new KeyValue(CompletionGenerateLengthLevelEnum.NO.getLabel());
//        } else {
//            pair = new KeyValue(levelLabel);
//        }
//
//        this.autoTriggerLengthComboBox.setSelectedItem(pair);
//    }
//
//    public String getUpgradeCheckValue() {
//        return this.autoUpdateCheckBox.isSelected() ? UpgradeChecklEnum.AUTO_INSTALL.getLabel() : UpgradeChecklEnum.FORBID_CHECK.getLabel();
//    }
//
//    public void setUpgradeCheckValue(String key) {
//        if (UpgradeChecklEnum.AUTO_INSTALL.getLabel().equals(key)) {
//            this.autoUpdateCheckBox.setSelected(true);
//        } else {
//            this.autoUpdateCheckBox.setSelected(false);
//        }
//
//    }
//
//    public ModelPowerLevelEnum getCloudAutoModelPower() {
//        return ModelPowerLevelEnum.LARGE;
//    }
//
//    public void setCloudAutoModelPower(String levelLabel) {
//    }
//
//    public CompletionGenerateLengthLevelEnum getCloudAutoGenLengthLevel() {
//        KeyValue pair = (KeyValue)this.autoTriggerLengthComboBox.getSelectedItem();
//        return pair.getKey().equals(CompletionGenerateLengthLevelEnum.NO.getLabel()) ? CompletionGenerateLengthLevelEnum.LEVEL_1 : CompletionGenerateLengthLevelEnum.getLevelEnum(pair.getKey());
//    }
//
//    public void setCloudAutoGenLengthLevel(String levelLabel) {
//        CompletionGenerateLengthLevelEnum level = CompletionGenerateLengthLevelEnum.getLevelEnum(levelLabel);
//        if (level != null) {
//            this.autoTriggerLengthComboBox.setSelectedItem(new KeyValue(levelLabel));
//        }
//
//    }
//
//    public ModelPowerLevelEnum getCloudManualModelPower() {
//        return ModelPowerLevelEnum.LARGE;
//    }
//
//    public void setCloudManualModelPower(String levelLabel) {
//    }
//
//    public CompletionGenerateLengthLevelEnum getCloudManualGenLength() {
//        KeyValue pair = (KeyValue)this.manualTriggerLengthComboBox.getSelectedItem();
//        return CompletionGenerateLengthLevelEnum.getLevelEnum(pair.getKey());
//    }
//
//    public void setCloudManualGenLength(String levelLabel) {
//        CompletionGenerateLengthLevelEnum level = CompletionGenerateLengthLevelEnum.getLevelEnum(levelLabel);
//        if (level != null) {
//            this.manualTriggerLengthComboBox.setSelectedItem(new KeyValue(levelLabel));
//        }
//
//    }
//
//    public ExceptionResolveModeEnum getExceptionResolveMode() {
//        return this.exceptionSolvingAssistantCheckBox.isSelected() ? ExceptionResolveModeEnum.USE_GENERATE : ExceptionResolveModeEnum.DISABLED;
//    }
//
//    public void setExceptionResolveMode(ExceptionResolveModeEnum mode) {
//        if (ExceptionResolveModeEnum.DISABLED.getType().equals(mode.getType())) {
//            this.exceptionSolvingAssistantCheckBox.setSelected(false);
//        } else {
//            this.exceptionSolvingAssistantCheckBox.setSelected(true);
//            if (ExceptionResolveModeEnum.USE_SEARCH.getType().equals(mode.getType())) {
//                this.enableGenerateModeRadioButton.setSelected(false);
//            } else {
//                this.enableGenerateModeRadioButton.setSelected(true);
//            }
//        }
//
//    }
//
//    public MethodQuickSwitchEnum getMethodQuickSwitchEnum() {
//        return this.inlineCommandsAboveMethodCheckBox.isSelected() ? MethodQuickSwitchEnum.ENABLED : MethodQuickSwitchEnum.DISABLED;
//    }
//
//    public void setMethodQuickSwitch(MethodQuickSwitchEnum mode) {
//        if (MethodQuickSwitchEnum.ENABLED.getType().equals(mode.getType())) {
//            this.inlineCommandsAboveMethodCheckBox.setSelected(true);
//        } else {
//            this.inlineCommandsAboveMethodCheckBox.setSelected(false);
//        }
//
//    }
//
//    public ProxyModeEnum getProxyMode() {
//        return this.systemProxyConfigurationRadioButton.isSelected() ? ProxyModeEnum.SYSTEM : ProxyModeEnum.MANUAL;
//    }
//
//    public void setProxyMode(String mode) {
//        if (ProxyModeEnum.SYSTEM.getType().equals(mode)) {
//            this.systemProxyConfigurationRadioButton.setSelected(true);
//            this.manualProxyConfigurationRadioButton.setSelected(false);
//            this.manualProxyUrlPanel.setVisible(false);
//        } else {
//            this.manualProxyConfigurationRadioButton.setSelected(true);
//            this.systemProxyConfigurationRadioButton.setSelected(false);
//            this.manualProxyUrlPanel.setVisible(true);
//        }
//
//    }
//
//    public void setEndpoint(String endpoint) {
//        if (StringUtils.isNotBlank(endpoint)) {
//            CosySetting setting = CosyPersistentSetting.getInstance().getState();
//            if (setting != null) {
//                setting.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
//                this.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
//            }
//        }
//
//    }
//
//    public String getProxyUrl() {
//        return this.proxyUrlTextField.getText();
//    }
//
//    public void setProxyUrl(String url) {
//        this.proxyUrlTextField.setText(url);
//    }
//
//    public void setShowInlineSuggestions(Boolean show) {
//        if (show != null && show) {
//            this.showInlineCheckBox.setSelected(true);
//        } else {
//            this.showInlineCheckBox.setSelected(false);
//        }
//
//    }
//
//    public Boolean getShowInlineSuggestions() {
//        return this.showInlineCheckBox.isSelected();
//    }
//
//    public List<String> getDisableLanguages() {
//        String text = this.langTriggerTextField.getText();
//        return StringUtils.isBlank(text) ? Collections.emptyList() : (List)Arrays.stream(text.split(",")).map(String::trim).collect(Collectors.toList());
//    }
//
//    public void setDisableLanguages(List<String> languages) {
//        if (languages != null && !languages.isEmpty()) {
//            this.langTriggerTextField.setText(StringUtils.join(languages, ','));
//        } else {
//            this.langTriggerTextField.setText("");
//        }
//
//    }
//
//    public void setLocalStoragePath(String path) {
//        this.localStoragePathField.setText(path);
//    }
//
//    public String getLocalStoragePath(boolean verify) {
//        String path = this.localStoragePathField.getText();
//        if (!verify) {
//            return path;
//        } else if (StringUtils.isBlank(path)) {
//            NotificationFactory.showToast(this.localStoragePathField, MessageType.ERROR, CosyBundle.message("notifications.local.storage.path.empty", new Object[0]));
//            return "";
//        } else {
//            File localStoragePathFile = new File(path);
//            if (!localStoragePathFile.exists() && !localStoragePathFile.mkdirs()) {
//                NotificationFactory.showToast(this.localStoragePathField, MessageType.ERROR, CosyBundle.message("notifications.local.storage.path.invalid", new Object[0]));
//                return "";
//            } else {
//                try {
//                    File testFile = new File(localStoragePathFile, "testLingmaPermission.txt");
//                    testFile.createNewFile();
//                    if (!testFile.exists()) {
//                        testFile.delete();
//                        NotificationFactory.showToast(this.localStoragePathField, MessageType.ERROR, CosyBundle.message("notifications.local.storage.path.no.permission", new Object[0]));
//                        return "";
//                    } else {
//                        testFile.delete();
//                        return path;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    NotificationFactory.showToast(this.localStoragePathField, MessageType.ERROR, CosyBundle.message("notifications.local.storage.path.no.permission", new Object[0]));
//                    return "";
//                }
//            }
//        }
//    }
//
//    public void setDedicatedUrlField(String url) {
//        this.dedicatedUrlField.setText(url);
//    }
//
//    public String getDisplayLanguage() {
//        if (this.displaySystemDefaultLanguageRadioButton.isSelected()) {
//            return "default";
//        } else {
//            return this.displayChineseRadioButton.isSelected() ? Locale.CHINESE.getLanguage() : Locale.ENGLISH.getLanguage();
//        }
//    }
//
//    public void setDisplayLanguage(String displayLanguage) {
//        if (displayLanguage.equals("default")) {
//            this.displaySystemDefaultLanguageRadioButton.setSelected(true);
//        } else if (displayLanguage.equals(Locale.CHINESE.getLanguage())) {
//            this.displayChineseRadioButton.setSelected(true);
//        } else if (displayLanguage.equals(Locale.ENGLISH.getLanguage())) {
//            this.displayEnglishRadioButton.setSelected(true);
//        } else {
//            this.displaySystemDefaultLanguageRadioButton.setSelected(true);
//        }
//
//    }
//
//    public boolean isUseDefaultLang() {
//        return this.displaySystemDefaultLanguageRadioButton.isSelected();
//    }
//
//    public static String getDefaultLang() {
//        return systemDefaultLang;
//    }
//
//    public String getChatLanguage() {
//        return this.chineseChatRadioButton.isSelected() ? Locale.CHINESE.getLanguage() : Locale.ENGLISH.getLanguage();
//    }
//
//    public void setChatLanguage(String chatLanguage) {
//        if (chatLanguage.equals(Locale.CHINESE.getLanguage())) {
//            this.chineseChatRadioButton.setSelected(true);
//        } else if (chatLanguage.equals(Locale.ENGLISH.getLanguage())) {
//            this.englishChatRadioButton.setSelected(true);
//        } else {
//            this.chineseChatRadioButton.setSelected(true);
//        }
//
//    }
//
//    public String getCommitMessageLanguage() {
//        return this.chineseCommitMessageRadioButton.isSelected() ? Locale.CHINESE.getLanguage() : Locale.ENGLISH.getLanguage();
//    }
//
//    public void setCommitMessageLanguage(String commitMessageLanguage) {
//        if (commitMessageLanguage.equals(Locale.CHINESE.getLanguage())) {
//            this.chineseCommitMessageRadioButton.setSelected(true);
//        } else if (commitMessageLanguage.equals(Locale.ENGLISH.getLanguage())) {
//            this.englishCommitMessageRadioButton.setSelected(true);
//        } else {
//            this.chineseCommitMessageRadioButton.setSelected(true);
//        }
//
//    }
//
//    public String getDedicatedUrlField(boolean verify) {
//        String url = this.dedicatedUrlField.getText();
//        if (url != null) {
//            url = url.replaceAll("[\\p{Cntrl}\\u200B-\\u200F\\uFEFF]", "");
//            url = url.trim();
//        }
//
//        if (!verify) {
//            return url;
//        } else if (StringUtils.isBlank(url)) {
//            NotificationFactory.showToast(this.dedicatedUrlField, MessageType.ERROR, CosyBundle.message("notifications.dedicated.url.empty", new Object[0]));
//            return "";
//        } else {
//            boolean isUrlValid = UrlUtil.verifyDomain(url);
//            if (!isUrlValid) {
//                NotificationFactory.showToast(this.dedicatedUrlField, MessageType.ERROR, CosyBundle.message("notifications.dedicated.url.invalid", new Object[0]));
//                return "";
//            } else {
//                return url;
//            }
//        }
//    }
//
//    protected GlobalEndpointConfig getEndpointConfig(Project project) {
//        GlobalEndpointConfig globalConfig = (GlobalEndpointConfig)CosyCacheKeys.KEY_ENDPOINT_CONFIG.get(project);
//        if (globalConfig == null) {
//            if (!Cosy.INSTANCE.checkCosy(project)) {
//                return null;
//            }
//
//            globalConfig = Cosy.INSTANCE.getLanguageService(project).getEndpointConfig(2000L);
//            if (globalConfig != null) {
//                CosyCacheKeys.KEY_ENDPOINT_CONFIG.set(project, globalConfig);
//            } else {
//                LingmaAgentUtil.addDefaultEndpointToCache(project);
//            }
//        }
//
//        return globalConfig;
//    }
//
//    private void createUIComponents() {
//        String[] numbers = new String[CodeCompletionCandidateEnum.MAX.num];
//
//        for(int i = CodeCompletionCandidateEnum.MIN.num; i <= CodeCompletionCandidateEnum.MAX.num; ++i) {
//            numbers[i - 1] = String.valueOf(i);
//        }
//
//        this.candidateNumberSelector = new ComboBox(numbers);
//        this.candidateNumberSelector.setSelectedItem(String.valueOf(CodeCompletionCandidateEnum.DEFAULT.num));
//        this.proxyUrlTextField = new JBTextField();
//        ((JBTextField)this.proxyUrlTextField).getEmptyText().setText(CosyBundle.message("settings.proxy.panel.tips", new Object[0]));
//        this.langTriggerTextField = new JBTextField();
//        ((JBTextField)this.langTriggerTextField).getEmptyText().setText(CosyBundle.message("settings.completion.cloud.language.placeholder", new Object[0]));
//        this.localStoragePathField = new TextFieldWithBrowseButton();
//        SwingHelper.installFileCompletionAndBrowseDialog((Project)null, this.localStoragePathField, "Local Storage Path Selector", FileChooserDescriptorFactory.createSingleFolderDescriptor().withShowHiddenFiles(SystemInfo.isUnix).withFileFilter(VirtualFile::isDirectory));
//    }
//
//
//    public void dispose() {
//        if (this.messageBusConnection != null) {
//            this.messageBusConnection.disconnect();
//        }
//    }
//}
