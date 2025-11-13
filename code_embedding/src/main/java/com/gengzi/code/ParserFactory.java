package com.gengzi.code;


/**
 * 抽象工厂
 */
public interface ParserFactory {
    /**
     * 创建代码解析器
     * @return
     */
    Parser createParser();

}