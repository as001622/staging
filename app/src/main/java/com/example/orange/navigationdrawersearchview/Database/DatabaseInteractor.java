package com.example.orange.navigationdrawersearchview.Database;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.List;

public interface DatabaseInteractor {
    List<GitHubUser> getListForMainRecyclerView();
    List<GitHubUser> getListForNavRecyclerView(List<GitHubUser> gitHubUserList);
    List<GitHubUser> getListForMainSearchView(String query);
    List<GitHubUser> restoreData();
    void saveData(List<GitHubUser> savedGitHubUsersList);
    void insertNewDeletedUser(GitHubUser gitHubUser);
    void insertNewUser(GitHubUser gitHubUser);
    void deleteUser(GitHubUser gitHubUser);
    void clearSavedData();
    void setLogin(String login);

}
