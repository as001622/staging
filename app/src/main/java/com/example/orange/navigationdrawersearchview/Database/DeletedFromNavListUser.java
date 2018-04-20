package com.example.orange.navigationdrawersearchview.Database;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeletedFromNavListUser extends RealmObject{

    @PrimaryKey
    private String mGitHubLogin;
    private Integer mGitHubId;
    private String mGitHubAvatarUrl;
    private String mGitHubReposUrl;
    private String mUserLogin;

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

    public void setGitHubReposUrl(String gitHubReposUrl) {
        mGitHubReposUrl = gitHubReposUrl;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(String userLogin) {
        mUserLogin = userLogin;
    }
}
