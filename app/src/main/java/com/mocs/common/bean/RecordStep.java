package com.mocs.common.bean;

/**时间线中的每一步*/
public class RecordStep {
    private int recordId;
    private long time;//发布的时间
    private String reportText;//回复内容
    private int state;//状态

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
