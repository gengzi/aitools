package com.gengzi.ui.enums;

public enum ModelLLMType {
    DEEP_SEEK_MODEL("Deepseek"),
    DEFAULT_MODEL("default");

    private final String modeName;

   ModelLLMType(String description) {
        this.modeName = description;
    }

    public String getDescription() {
        return modeName;
    }
}
