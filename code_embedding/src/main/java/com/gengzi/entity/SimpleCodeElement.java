package com.gengzi.entity;

import java.util.List;
import java.util.Map;

/**
 * 一个简单的代码元素实体对象，用于存储解析的每个Java文件的内容
 *
 */
public class SimpleCodeElement {


    /**
     * 类名称（接口，抽象类）
     */
    private String className;

    /**
     * 整个代码内容
     */
    private String body;


    /**
     * 方法参数
     */
    private List<String> methodCodes;


    /**
     * 属性参数
     */
    private List<String> attributes;


    /**
     * 内部类
     */
    private SimpleCodeElement innerClass;

    /**
     * 导包
     */
    private List<String> improts;

    public List<String> getImprots() {
        return improts;
    }

    public void setImprots(List<String> improts) {
        this.improts = improts;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getMethodCodes() {
        return methodCodes;
    }

    public void setMethodCodes(List<String> methodCodes) {
        this.methodCodes = methodCodes;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public SimpleCodeElement getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(SimpleCodeElement innerClass) {
        this.innerClass = innerClass;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
