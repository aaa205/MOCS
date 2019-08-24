package com.mocs.common.bean;

/**
 * RecordFragment 中展示的 Record的简单信息
 */
public class RecordInfo {
    private int recordId;
    private long createdTime;//创建时间
    private int userId;
    private int type;
    private String description;
    private String address;
    private int state;//状态

    @Override
    public String toString() {
        return "RecordInfo{" +
                "recordId=" + recordId +
                ", createdTime=" + createdTime +
                ", userId=" + userId +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", state=" + state +
                '}';
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
