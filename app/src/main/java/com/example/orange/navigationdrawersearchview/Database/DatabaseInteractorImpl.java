package com.example.orange.navigationdrawersearchview.Database;

import android.util.Log;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
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
            //    .and()
           //     .equalTo("Deleted",false)
                .findAllAsync();
        result.load();
        Log.v("Sortier",result.toString());
        if (result!=null)
            for (User user:result){
                GitHubUser gitHubUser = new GitHubUser();
                gitHubUser.setAvatarUrl(user.getGitHubAvatarUrl());
                gitHubUser.setId(user.getGitHubId());
                gitHubUser.setLogin(user.getGitHubLogin());
                gitHubUser.setReposUrl(user.getGitHubReposUrl());
                gitHubUserList.add(gitHubUser);
            }
        return gitHubUserList;
    }

    public void insertNewUser(GitHubUser gitHubUser, String mainLogin,Boolean deleted){

        mRealm.beginTransaction();
        User user = new User();
        user.setGitHubLogin(gitHubUser.getLogin());
        user.setGitHubReposUrl(gitHubUser.getReposUrl());
        user.setGitHubAvatarUrl(gitHubUser.getAvatarUrl());
        user.setGitHubId(gitHubUser.getId());
        user.setUserLogin(mainLogin);
        mRealm.insertOrUpdate(user);
        mRealm.commitTransaction();

    }
}
