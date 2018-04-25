package com.example.orange.navigationdrawersearchview.GitHubApi;

import android.view.View;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;

import java.util.List;

public interface ApiInteractor {
    interface OnLoadDataFinished {
        void OnLoadDataResponseIsSuccesfull(List<GitHubUser> userList,Integer total_count);

        void OnLoadDataResponseIsNotSuccesfull(String message);

        void OnLoadDataFailure(String message) throws JSONException;
    }

    interface OnLoginProccesed {
        void OnLoginResponseIsSuccesfull(GitHubUser gitHubUser,String password);
        void OnLoginResponseIsNotSuccesfull();
        void OnLoginFailure(Throwable t);

    }

    void loadData(final String query, final ApiInteractor.OnLoadDataFinished listener);
    void doLogin(String username, final String password, final ApiInteractor.OnLoginProccesed listener);

}
