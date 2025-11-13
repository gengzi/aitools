package com.gengzi.code.java;


import com.gengzi.code.Parser;
import com.gengzi.code.ParserFactory;

// Java解析工厂
public class JavaParserFactory implements ParserFactory {


    @Override
    public Parser createParser() {
        return new JavaParserAdapter();
    }
}