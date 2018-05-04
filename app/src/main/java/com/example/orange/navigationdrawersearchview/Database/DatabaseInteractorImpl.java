package com.example.orange.navigationdrawersearchview.Database;

import android.util.Log;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DatabaseInteractorImpl implements DatabaseInteractor{
    private Realm mRealm;
    private String mLogin;

    public static final String mUserLoginField="mUserLogin";
    public static final String mAddedUserLoginField ="ownersOfAdded.mUserLogin";
    public static final String mDeletedUserLoginField ="ownersOfDeleted.mUserLogin";
    public static final String mUserGitHubLoginField ="mGitHubLogin";


    public DatabaseInteractorImpl(Realm realm) {

        mRealm=realm;
    }

    private void getAllData(String message){
        RealmResults<ApplicationUser> result = mRealm.where(ApplicationUser.class)
                .findAllAsync();
        result.load();
        if (result.size()!=0)
        for(ApplicationUser appUser:result)
            Log.v(message,appUser.toString());
    }

    public List<GitHubUser> getListForMainRecyclerView(){
        List<GitHubUser> gitHubUserList = new ArrayList<>();
        ApplicationUser appUser = mRealm.where(ApplicationUser.class)
                .equalTo(mUserLoginField,mLogin)
                .findFirst();

        if (appUser!=null)
           for(User user:appUser.getAddedUsers())
               gitHubUserList.add(getGitHubUser(user));

        return gitHubUserList;
    }

    public List<GitHubUser> getListForMainSearchView(String query){

        List<GitHubUser> gitHubUserList = new ArrayList<>();
        RealmResults<User> addedUsers = mRealm.where(User.class)
                .equalTo(mAddedUserLoginField,mLogin)
                .and()
                .beginsWith(mUserGitHubLoginField,query, Case.INSENSITIVE)
                .findAllAsync();
        addedUsers.load();
        if (addedUsers.size()!=0) {
                for (User user:addedUsers)
                    gitHubUserList.add(getGitHubUser(user));
            }
        return gitHubUserList;
    }

    public List<GitHubUser> getListForNavRecyclerView(List<GitHubUser> gitHubUserList){

        List<GitHubUser> deletedGitHubUserList = new ArrayList<>();
        ApplicationUser appUser = mRealm
                .where(ApplicationUser.class)
                .equalTo(mUserLoginField,mLogin)
                .findFirst();
        if (appUser!=null)
            appUser.load();

        if (appUser!=null)
            for(User user:appUser.getDeletedUsers())
                deletedGitHubUserList.add(getGitHubUser(user));
        gitHubUserList.removeAll(deletedGitHubUserList);
        return gitHubUserList;
    }

    public void insertNewUser(GitHubUser gitHubUser){

        User checkUser= mRealm.where(User.class)
                .equalTo(mAddedUserLoginField,mLogin)
                .and()
                .equalTo(mUserGitHubLoginField,gitHubUser.getLogin())
                .findFirst();
        if (checkUser==null) {
            RealmList<User> addedUserList;
            ApplicationUser appUser = mRealm
                    .where(ApplicationUser.class)
                    .equalTo(mUserLoginField, mLogin)
                    .findFirst();
            if (appUser == null) {
                appUser = new ApplicationUser();
                appUser.setUserLogin(mLogin);
                addedUserList = new RealmList<User>();
            } else addedUserList = appUser.getAddedUsers();
            mRealm.beginTransaction();
            addedUserList.add(setUser(gitHubUser));
            appUser.setAddedUsers(addedUserList);
            mRealm.insertOrUpdate(appUser);
            mRealm.commitTransaction();

        }


    }
    public void insertNewDeletedUser(GitHubUser gitHubUser){

        User checkUser= mRealm.where(User.class)
                .equalTo(mAddedUserLoginField,mLogin)
                .and()
                .equalTo(mUserGitHubLoginField,gitHubUser.getLogin())
                .findFirst();
        if (checkUser==null) {
            RealmList<User> deletedUserList;
            ApplicationUser appUser = mRealm
                    .where(ApplicationUser.class)
                    .equalTo(mUserLoginField, mLogin)
                    .findFirst();
            if (appUser == null) {
                appUser = new ApplicationUser();
                appUser.setUserLogin(mLogin);
                deletedUserList = new RealmList<User>();
            } else deletedUserList = appUser.getDeletedUsers();

            mRealm.beginTransaction();
            deletedUserList.add(setUser(gitHubUser));
            appUser.setDeletedUsers(deletedUserList);
            mRealm.insertOrUpdate(appUser);
            mRealm.commitTransaction();
        }

    }

    private User setUser(GitHubUser gitHubUser){
        User user = new User();
        user.setGitHubLogin(gitHubUser.getLogin());
        user.setGitHubReposUrl(gitHubUser.getReposUrl());
        user.setGitHubAvatarUrl(gitHubUser.getAvatarUrl());
        user.setGitHubId(gitHubUser.getId());
        return user;
    }

    public void deleteUser(final GitHubUser gitHubUser)
    {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm
                        .where(User.class)
                        .equalTo(mAddedUserLoginField,mLogin)
                        .and()
                        .equalTo(mUserGitHubLoginField,gitHubUser.getLogin())
                        .findFirst()
                        .deleteFromRealm();
            }
        });

    }

    private GitHubUser getGitHubUser(User user){
        GitHubUser gitHubUser = new GitHubUser();
        gitHubUser.setAvatarUrl(user.getGitHubAvatarUrl());
        gitHubUser.setId(user.getGitHubId());
        gitHubUser.setLogin(user.getGitHubLogin());
        gitHubUser.setReposUrl(user.getGitHubReposUrl());
            return gitHubUser;
    }

    public void saveData(List<GitHubUser> savedGitHubUsersList){
        if (savedGitHubUsersList!=null) {
            RealmList<User> savedUserList;
            ApplicationUser appUser = mRealm
                    .where(ApplicationUser.class)
                    .equalTo(mUserLoginField, mLogin)
                    .findFirst();
            if (appUser == null) {
                appUser = new ApplicationUser();
                appUser.setUserLogin(mLogin);
                savedUserList = new RealmList<>();
            } else savedUserList = appUser.getSavedUsers();
            //clearing previous data
            if (savedUserList != null) {
                mRealm.beginTransaction();
                savedUserList.clear();
                mRealm.commitTransaction();
            }

            //inserting new data
            mRealm.beginTransaction();
            for (GitHubUser gitHubUser : savedGitHubUsersList) {
                savedUserList.add(setUser(gitHubUser));
            }
            appUser.setSavedUsers(savedUserList);
            mRealm.insertOrUpdate(appUser);
            mRealm.commitTransaction();
        }


    }

    public List<GitHubUser> restoreData(){

        List<GitHubUser> gitHubUserList = new ArrayList<>();
        ApplicationUser appUser = mRealm.where(ApplicationUser.class)
                .equalTo(mUserLoginField,mLogin)
                .findFirst();
        if (appUser!=null)
            for(User user:appUser.getSavedUsers())
                gitHubUserList.add(getGitHubUser(user));

        return gitHubUserList;
    }


    public void clearSavedData(){
        ApplicationUser appUser = mRealm.where(ApplicationUser.class)
                .equalTo(mUserLoginField,mLogin)
                .findFirst();
        if (appUser!=null) {
            mRealm.beginTransaction();
            appUser.setSavedUsers(null);
            mRealm.commitTransaction();
        }
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }
}
