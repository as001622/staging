package com.example.orange.navigationdrawersearchview.Database;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

import java.util.List;

public interface DatabaseInteractor {
    public List<GitHubUser> getListForMainRecyclerView(String login);
}
