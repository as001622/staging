package com.example.orange.navigationdrawersearchview.Database;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.List;

public interface DatabaseInteractor {
    List<GitHubUser> getListForMainRecyclerView(String login);
    List<GitHubUser> getListForNavRecyclerView(String login,List<GitHubUser> gitHubUserList);
    void insertNewUser(GitHubUser gitHubUser, String login);
    void deleteUser(GitHubUser gitHubUser);
}
