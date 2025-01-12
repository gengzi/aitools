package com.gengzi.ui.entity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造请求参数
 */
public class OpenAiChatReq {

    private String model;

    private List<Messages> messages;


    private boolean stream;


    public String getJsonReq(List<Messages> messages) {
        ArrayList list = (ArrayList) this.messages;
        list.addAll(messages);
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Messages> getMessage() {
        return messages;
    }

    public void setMessage(List<Messages> message) {
        this.messages = message;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
