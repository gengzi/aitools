package com.gengzi.entity;

import java.util.ArrayList;
import java.util.List;

// 组合模式：代码元素基类
public abstract class CodeElement {
    /**
     * 文件名
     */
    protected String name;
    /**
     * 文件doc
     */
    protected String doc;
    /**
     * 多个子元素
     */
    protected List<CodeElement> children = new ArrayList<>();

}