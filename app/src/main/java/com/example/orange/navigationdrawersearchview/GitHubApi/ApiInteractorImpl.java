package com.example.orange.navigationdrawersearchview.GitHubApi;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiInteractorImpl {

    private List<GitHubUser> mUserList;
    private ApiService service;

    public ApiInteractorImpl(){
        service = new ApiBuilder().getService();
    }


    public void loadData(final String query, final ApiInteractor.OnLoadDataFinished listener){
        final Call<GitHubUsersFeed> usersListCall = service.search("search/users?q="+query+"+in:login");
        usersListCall.enqueue(new Callback<GitHubUsersFeed>() {
            @Override
            public void onResponse(Call<GitHubUsersFeed> call, Response<GitHubUsersFeed> response) {
                if (response.isSuccessful()) {

                    listener.onLoadDataResponseIsSuccesfull(response.body().getItems());
                }
                else{
                    if (response.headers().get("Status").contains("403"));

                    else
                    listener.onLoadDataResponseIsNotSuccesfull("Forbidden!!!!");
                }
            }

            @Override
            public void onFailure(Call<GitHubUsersFeed> call, Throwable t) {

                try {
                    listener.onLoadDataFailure( t.getMessage().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void doLogin(String username, String password, final ApiInteractor.OnLoginProccesed listener){
            service.getUserDetails(Credentials.basic(username, password))
                .enqueue(new Callback<GitHubUser>() {
                    @Override
                    public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {

                        if (!response.isSuccessful()){

                            listener.onLoginResponseIsNotSuccesfull();

                        }
                        else {
                            listener.onLoginResponseIsSuccesfull(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<GitHubUser> call, Throwable t) {

                        listener.onLoginFailure(t);
                    }
                });
    }
}
