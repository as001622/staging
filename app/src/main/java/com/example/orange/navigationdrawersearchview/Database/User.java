package com.example.orange.navigationdrawersearchview.Database;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {

    @PrimaryKey
    private String mGitHubLogin;
    private Integer mGitHubId;
    private String mGitHubAvatarUrl;
    private String mGitHubReposUrl;
    private String mUserLogin;

    private Boolean mDeletedFromNavList;
    private Boolean mDeletedFromMainList;

    public Boolean getDeletedFromNavList() {
        return mDeletedFromNavList;
    }

    public void setDeletedFromNavList(Boolean deletedFromNavList) {
        mDeletedFromNavList = deletedFromNavList;
    }

    public Boolean getDeletedFromMainList() {
        return mDeletedFromMainList;
    }

    public void setDeletedFromMainList(Boolean deletedFromMainList) {
        mDeletedFromMainList = deletedFromMainList;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(String userLogin) {
        mUserLogin = userLogin;
    }

    public String getGitHubLogin() {
        return mGitHubLogin;
    }

    public void setGitHubLogin(String gitHubLogin) {
        mGitHubLogin = gitHubLogin;
    }

    public Integer getGitHubId() {
        return mGitHubId;
    }

    public void setGitHubId(Integer gitHubId) {
        mGitHubId = gitHubId;
    }

    public String getGitHubAvatarUrl() {
        return mGitHubAvatarUrl;
    }

    public void setGitHubAvatarUrl(String gitHubAvatarUrl) {
        mGitHubAvatarUrl = gitHubAvatarUrl;
    }

    public String getGitHubReposUrl() {
        return mGitHubReposUrl;
    }

    public void setGitHubReposUrl(String gitHUbReposUrl) {
        mGitHubReposUrl = gitHUbReposUrl;
    }
}