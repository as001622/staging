package com.example.orange.navigationdrawersearchview.Database;

import android.util.Log;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseInteractorImpl implements DatabaseInteractor{
    private Realm mRealm;
    public DatabaseInteractorImpl(Realm realm) {
        mRealm=realm;
    }


    public List<GitHubUser> getListForMainRecyclerView(String login){
        List<GitHubUser> gitHubUserList = new ArrayList<>();
        RealmResults<User> result = mRealm.where(User.class)
                .equalTo("mUserLogin",login)
                .and()
                .equalTo("mFoundForNavList",false)
                .and()
                .equalTo("mDeletedFromNavList",false)
                .findAllAsync();
        result.load();
        if (result.size()!=0)
            for (User user:result){
                gitHubUserList.add(getGitHubUser(user));
            }

        return gitHubUserList;
    }

    public List<GitHubUser> getListForMainSearchView(String query){
        List<GitHubUser> gitHubUserList = new ArrayList<>();
        RealmResults<User> result = mRealm.where(User.class)
                .beginsWith("mGitHubLogin",query, Case.INSENSITIVE)
                .and()
                .equalTo("mFoundForNavList",false)
                .and()
                .equalTo("mDeletedFromNavList",false)
                .findAllAsync();
        result.load();
        if (result.size()!=0)
            for (User user:result){
                gitHubUserList.add(getGitHubUser(user));
            }
        return gitHubUserList;
    }
    public void clearNavData(String login){
        RealmResults<User> result = mRealm.where(User.class)
                .equalTo("mDeletedFromNavList",true)
                .and()
                .equalTo("mUserLogin",login)
                .findAllAsync();
        result.deleteAllFromRealm();

    }

    public List<GitHubUser> getListForNavRecyclerView(String login,List<GitHubUser> gitHubUserList){
        List<GitHubUser> deletedGitHubUserList = new ArrayList<>();

        RealmResults<User> result = mRealm.where(User.class)
                .equalTo("mUserLogin",login)
                .and()
                .equalTo("mDeletedFromNavList",true)
                .findAllAsync();
        result.load();

        if (result.size()!=0)
            for (User user:result){
                deletedGitHubUserList.add(getGitHubUser(user));
            }
        gitHubUserList.removeAll(deletedGitHubUserList);
        Log.v("ZHZH first", result.toString());
        return gitHubUserList;
    }

    public void insertNewUser(GitHubUser gitHubUser, String login){

        mRealm.beginTransaction();
        User user = new User();
        user.setGitHubLogin(gitHubUser.getLogin());
        user.setGitHubReposUrl(gitHubUser.getReposUrl());
        user.setGitHubAvatarUrl(gitHubUser.getAvatarUrl());
        user.setGitHubId(gitHubUser.getId());
        user.setFoundForNavList(false);
        user.setDeletedFromNavList(false);
        user.setUserLogin(login);
        mRealm.insertOrUpdate(user);
        mRealm.commitTransaction();

    }
    public void insertNewDeletedUser(GitHubUser gitHubUser, String login){

        mRealm.beginTransaction();
        User deletedUser = new User();;
        deletedUser.setGitHubLogin(gitHubUser.getLogin());
        deletedUser.setGitHubReposUrl(gitHubUser.getReposUrl());
        deletedUser.setGitHubAvatarUrl(gitHubUser.getAvatarUrl());
        deletedUser.setGitHubId(gitHubUser.getId());
        deletedUser.setDeletedFromNavList(true);
        deletedUser.setFoundForNavList(false);
        deletedUser.setUserLogin(login);
        mRealm.insertOrUpdate(deletedUser);
        mRealm.commitTransaction();
    }

    public void deleteUser(final GitHubUser gitHubUser)
    {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                        Log.v("ZHZH delete", gitHubUser.toString());
                        mRealm.where(User.class)
                        .equalTo("mGitHubLogin",gitHubUser.getLogin())
                        .findFirst().deleteFromRealm();
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
}
