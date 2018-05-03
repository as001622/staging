package com.example.orange.navigationdrawersearchview.GitHubApi;

import android.view.View;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;

import java.util.List;

public interface ApiInteractor {
    interface OnLoadDataFinished {
        void OnLoadDataResponseIsSuccesfull(List<GitHubUser> userList,Integer total_count, String Tag);

        void OnLoadDataResponseIsNotSuccesfull(String message);

        void OnLoadDataFailure(String message);
    }

    interface OnLoginProccesed {
        void OnLoginResponseIsSuccesfull(GitHubUser gitHubUser,String password);
        void OnLoginResponseIsNotSuccesfull();
        void OnLoginFailure(Throwable t);

    }

    interface OnUserDetailsRecieved{
        void OnUserDetailsRecievedIsSuccesfull(GitHubUser gitHubUser);

        void OnUserDetailsRecievedIsNotSuccesfull(String message);

        void OnUserDetailsRecievedFailure(String message);

    }
    void firstTimeLoadData(final String query, final ApiInteractor.OnLoadDataFinished listener);
    void doLogin(String username, final String password, final ApiInteractor.OnLoginProccesed listener);
    void getUserDetails(final String githubuserLogin,final ApiInteractor.OnUserDetailsRecieved listener);
    void loadMoreData(final String query, final ApiInteractor.OnLoadDataFinished listener);
}
