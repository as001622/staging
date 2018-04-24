package com.example.orange.navigationdrawersearchview.GitHubApi;

import android.view.View;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;

import java.util.List;

public interface ApiInteractor {
    interface OnLoadDataFinished {
        void onLoadDataResponseIsSuccesfull(List<GitHubUser> userList);

        void onLoadDataResponseIsNotSuccesfull(String message);

        void onLoadDataFailure(String message) throws JSONException;
    }

    interface OnLoginProccesed {
        void onLoginResponseIsSuccesfull(GitHubUser gitHubUser);
        void onLoginResponseIsNotSuccesfull();
        void onLoginFailure(Throwable t);

    }

}
