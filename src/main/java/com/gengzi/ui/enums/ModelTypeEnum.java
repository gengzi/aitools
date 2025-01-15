package com.gengzi.ui.enums;

public enum ModelTypeEnum {

    LOCAL_MODEL("LOCAL_MODEL"),
    SERVER_MODEL("SERVER_MODEL");

    private final String description;

    ModelTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
