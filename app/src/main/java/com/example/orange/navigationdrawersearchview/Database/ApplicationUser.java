package com.example.orange.navigationdrawersearchview.Database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ApplicationUser extends RealmObject{

    @PrimaryKey
    private String mUserLogin;
    private RealmList<User> mAddedUsers;
    private RealmList<User> mDeletedUsers;
    private RealmList<User> mSavedUsers;
    boolean fff;

    public String getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(String userLogin) {
        mUserLogin = userLogin;
    }

    public RealmList<User> getAddedUsers() {
        return mAddedUsers;
    }

    public void setAddedUsers(RealmList<User> addedUsers) {
        mAddedUsers = addedUsers;
    }

    public RealmList<User> getDeletedUsers() {
        return mDeletedUsers;
    }

    public void setDeletedUsers(RealmList<User> deletedUsers) {
        mDeletedUsers = deletedUsers;
    }

    public RealmList<User> getSavedUsers() {
        return mSavedUsers;
    }

    public void setSavedUsers(RealmList<User> savedUsers) {
        mSavedUsers = savedUsers;
    }

    @Override
    public String toString() {
        StringBuilder message=new StringBuilder();
        message.append("userLogin "+getUserLogin()+" ");
        message.append("Added users: ");
        if (getAddedUsers()!=null)
        for(User user:getAddedUsers())
        {
            message.append(user.getGitHubAvatarUrl()+" ");
            message.append(user.getGitHubId()+" ");
            message.append(user.getGitHubLogin()+" ");
            message.append(user.getGitHubReposUrl()+" ");
        }
        message.append("Deleted users: ");
        if (getDeletedUsers()!=null)
        for(User user:getDeletedUsers())
        {
             message.append(user.getGitHubAvatarUrl()+" ");
             message.append(user.getGitHubId()+" ");
             message.append(user.getGitHubLogin()+" ");
             message.append(user.getGitHubReposUrl()+" ");
        }
        message.append("Saved users: ");
        if (getSavedUsers()!=null)
            for(User user:getSavedUsers())
            {
                message.append(user.getGitHubAvatarUrl()+" ");
                message.append(user.getGitHubId()+" ");
                message.append(user.getGitHubLogin()+" ");
                message.append(user.getGitHubReposUrl()+" ");
            }
        return message.toString();
    }
}
