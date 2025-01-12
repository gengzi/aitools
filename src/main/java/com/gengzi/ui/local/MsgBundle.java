package com.gengzi.ui.local;

import com.intellij.DynamicBundle;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * 获取messagebundle的配置信息
 *
 */
public class MsgBundle extends DynamicBundle {
    private static final Logger LOG = Logger.getInstance(MsgBundle.class);
    private static MsgBundle instance = new MsgBundle();

    private MsgBundle() {
        super("messages");
    }


    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = "messages.MyBundle") String key, @NotNull Object... params) {
        if (key == null) {
            return null;
        }
        if (params == null) {
            return null;
        }
        String fullName = instance.getMessage("cosy.plugin.name", new Object[0]);
        String simpleName = instance.getMessage("cosy.plugin.simple.name", new Object[0]);
        String value = instance.getMessage(key, params);
        String result = value.replace("<PLUGIN_NAME>", fullName).replace("<SIMPLE_NAME>", simpleName);
        if (result == null) {
            return null;
        }
        return result;
    }

}
