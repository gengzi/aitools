package com.gengzi.dataexc.entity;

import java.util.Map;

public class VectorItemWithMetadata {

    /**
     * id，与其他原文和元数据信息关联
     */
    private String id;
    /**
     * 向量空间
     */
    private float[] vector;

    /**
     * 原文内容
     */
    private String originalText;
    /**
     * 元数据信息
     */
    private Map<String, Object> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
