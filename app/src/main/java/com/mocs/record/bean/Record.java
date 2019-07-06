package com.mocs.record.bean;

import java.util.List;
import java.util.Map;
/**记录每个提交的问题的信息*/
public class Record {
    private String time;
    private String type;
    private String description;
    private Map<String,String> stepMap;//记录了时间戳和处理步骤
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
}
