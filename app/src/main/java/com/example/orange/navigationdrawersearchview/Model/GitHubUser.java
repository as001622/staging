package com.example.orange.navigationdrawersearchview.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class GitHubUser {

    @SerializedName("login")
    @Expose
    private String mLogin;
    @SerializedName("id")
    @Expose
    private Integer mId;
    @SerializedName("avatar_url")
    @Expose
    private String mAvatarUrl;
    @SerializedName("url")
    @Expose
    private String mUrl;
    @SerializedName("html_url")
    @Expose
    private String mHtmlUrl;
    @SerializedName("repos_url")
    @Expose
    private String mReposUrl;

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        mHtmlUrl = htmlUrl;
    }

    public String getReposUrl() {
        return mReposUrl;
    }

    public void setReposUrl(String reposUrl) {
        mReposUrl = reposUrl;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {

        return getId().toString()+" "+getAvatarUrl().toString()
                +" "+getLogin().toString()+" "+
                getReposUrl().toString();
    }
}