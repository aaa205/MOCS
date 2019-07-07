package com.mocs.record.bean;

import java.util.List;

/**
 * 记录每个提交的问题的信息
 */
public class Record {
    private long recordId;
    private String time;
    private String type;
    private String description;
    private List<RecordStep> recordStepList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecordStep> getRecordStepList() {
        return recordStepList;
    }

    public void setRecordStepList(List<RecordStep> recordStepList) {
        this.recordStepList = recordStepList;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
}
