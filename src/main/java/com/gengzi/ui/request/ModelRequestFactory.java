package com.gengzi.ui.request;

import com.gengzi.ui.enums.ModelLLMType;

public class ModelRequestFactory {


    public static final String DEEPSEEK = "deepseek";


    public static RequestLLMInterface createRequestLLMInterface(String modelName) {
        switch (modelName){
            case  DEEPSEEK:
                return new DeepSeekReqLLM();
            default:
                return new DefaultReqLLM();
        }
    }



}
