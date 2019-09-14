package com.mocs.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * RecordFragment 中展示的 Record的简单信息
 */
public class RecordInfo implements Parcelable {
    private int recordId;
    private long createdTime;//创建时间
    private int userId;
    private int type;
    private String description;
    private String address;
    private int state;//状态

    protected RecordInfo(Parcel in) {
        recordId = in.readInt();
        createdTime = in.readLong();
        userId = in.readInt();
        type = in.readInt();
        description = in.readString();
        address = in.readString();
        state = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recordId);
        dest.writeLong(createdTime);
        dest.writeInt(userId);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeInt(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordInfo> CREATOR = new Creator<RecordInfo>() {
        @Override
        public RecordInfo createFromParcel(Parcel in) {
            return new RecordInfo(in);
        }

        @Override
        public RecordInfo[] newArray(int size) {
            return new RecordInfo[size];
        }
    };

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
