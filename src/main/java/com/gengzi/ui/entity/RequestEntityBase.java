package com.gengzi.ui.entity;


import java.util.List;

public class RequestEntityBase {

    /**
     * model
     */
    private String model;

    /**
     * 请求消息
     */
    private List<Messages> messages;

    /**
     * 是否流示返回
     */
    private boolean stream;

    public RequestEntityBase() {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
