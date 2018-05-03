package com.example.orange.navigationdrawersearchview.Database;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.List;

public interface DatabaseInteractor {
    List<GitHubUser> getListForMainRecyclerView();
    List<GitHubUser> getListForNavRecyclerView(List<GitHubUser> gitHubUserList);
    void insertNewUser(GitHubUser gitHubUser);
    void deleteUser(GitHubUser gitHubUser);
}
