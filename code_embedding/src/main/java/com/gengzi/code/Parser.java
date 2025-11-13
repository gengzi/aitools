package com.gengzi.code;


import com.gengzi.entity.SimpleCodeElement;

// 解析器接口
public interface Parser {
    SimpleCodeElement parse(String code);
}