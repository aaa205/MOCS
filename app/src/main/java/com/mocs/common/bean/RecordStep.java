package com.mocs.common.bean;

/**时间线中的每一步*/
public class RecordStep {
    private long createdTime;//发布的时间
    private String description;//回复内容

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
