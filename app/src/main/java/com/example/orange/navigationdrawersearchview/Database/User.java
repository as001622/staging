package com.example.orange.navigationdrawersearchview.Database;


import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {

    @PrimaryKey
    private String mGitHubLogin;
    private Integer mGitHubId;
    private String mGitHubAvatarUrl;
    private String mGitHubReposUrl;

    @LinkingObjects("mAddedUsers") // <-- !
    private final RealmResults<ApplicationUser> ownersOfAdded = null;

    @LinkingObjects("mDeletedUsers") // <-- !
    private final RealmResults<ApplicationUser> ownersOfDeleted = null;

    @LinkingObjects("mSavedUsers") // <-- !
    private final RealmResults<ApplicationUser> ownersOfSaved = null;


    public RealmResults<ApplicationUser> getOwnersOfAdded() {
        return ownersOfAdded;
    }

    public RealmResults<ApplicationUser> getOwnersOfDeleted() {
        return ownersOfDeleted;
    }

    public RealmResults<ApplicationUser> getOwnersOfSaved() {
        return ownersOfSaved;
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