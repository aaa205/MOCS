package com.mocs.common.bean;


import java.io.Serializable;

/**
 * id ： 服务器返回的用户id
 * nickname ： 用户名称
 * accessToken ： 调用服务器api的token
 * qqOpenId ：用户的qq的id
 * qqAccessToken ： 调用qq的api的token
 */
public class User implements Serializable {
    private String nickname;
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }
}
