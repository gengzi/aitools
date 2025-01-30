package com.gengzi.ui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtil {
    // 创建一个线程安全的 Gson 实例
    private static final Gson GSON;

    static {
        // 可以根据需要配置 Gson，例如设置日期格式等
        GSON = new GsonBuilder()
               .setDateFormat("yyyy-MM-dd HH:mm:ss") 
               .create();
    }

    private GsonUtil() {
        // 私有构造函数，防止实例化工具类
    }

    /**
     * 将对象转换为 JSON 字符串
     * @param obj 要转换的对象
     * @return 转换后的 JSON 字符串
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * @param json JSON 字符串
     * @param clazz 目标对象的类类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为指定类型的列表
     * @param json JSON 字符串
     * @param <T> 泛型类型
     * @return 转换后的列表
     */
    public static <T> List<T> fromJsonToList(String json) {
        Type type = new TypeToken<List<T>>() {}.getType();
        return GSON.fromJson(json, type);
    }
}