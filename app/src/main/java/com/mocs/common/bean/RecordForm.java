package com.mocs.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 存储FormActivity中填写的数据,不包含选择的图片，用于向服务器发送创建Record
 */
public class RecordForm implements Parcelable {
    private long createdTime;//创建时间
    private int userId;
    private int type;
    private String description;
    private double latitude;
    private double longitude;
    private String address;
    private String country;
    private String city;
    private String district;
    private String street;

    public RecordForm() {
    }


    protected RecordForm(Parcel in) {
        createdTime = in.readLong();
        userId = in.readInt();
        type = in.readInt();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        country = in.readString();
        city = in.readString();
        district = in.readString();
        street = in.readString();
        streetNum = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createdTime);
        dest.writeInt(userId);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(street);
        dest.writeString(streetNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordForm> CREATOR = new Creator<RecordForm>() {
        @Override
        public RecordForm createFromParcel(Parcel in) {
            return new RecordForm(in);
        }

        @Override
        public RecordForm[] newArray(int size) {
            return new RecordForm[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    private String streetNum;

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
}
