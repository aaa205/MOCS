package com.mocs.common.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * userId ： 服务器返回的用户id
 * nickname ： 用户名称
 * accessToken ： 调用服务器api的token
 * qqOpenId ：用户的qq的id
 * qqAccessToken ： 调用qq的api的token
 */
public class User implements Parcelable {
    private String nickname;
    private int userId;
    private String qqOpenId;
    private String qqAccessToken;
    private String accessToken;
    private String avatarImageUrl;//头像url

    public User() {
        nickname="unknown";
        qqOpenId="unknown";
        qqAccessToken="";
        accessToken="";
        avatarImageUrl ="";
    }
    protected User(Parcel in) {
        nickname = in.readString();
        userId = in.readInt();
        qqOpenId = in.readString();
        qqAccessToken = in.readString();
        accessToken = in.readString();
        avatarImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeInt(userId);
        dest.writeString(qqOpenId);
        dest.writeString(qqAccessToken);
        dest.writeString(accessToken);
        dest.writeString(avatarImageUrl);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getQqAccessToken() {
        return qqAccessToken;
    }

    public void setQqAccessToken(String qqAccessToken) {
        this.qqAccessToken = qqAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

}
