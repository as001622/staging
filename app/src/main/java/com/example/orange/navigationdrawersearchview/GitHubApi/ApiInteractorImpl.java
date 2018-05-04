package com.example.orange.navigationdrawersearchview.GitHubApi;

import android.util.Log;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiInteractorImpl implements ApiInteractor,Constants{

    private List<GitHubUser> mUserList;
    private ApiService service;
    public static int mCurrentPage=firstPage;
    private int mLoadPerPage=loadPerPage;

    public ApiInteractorImpl(){
        service = new ApiBuilder().getService();
    }

    @Override
    public void firstTimeLoadData(final String query, final ApiInteractor.OnLoadDataFinished listener){
        String mainQuery=URL_START+query+URL_FINISH;
        final Call<GitHubUsersFeed> usersListCall = service.searchUsers(mainQuery,mCurrentPage,mLoadPerPage);
        usersListCall.enqueue(new Callback<GitHubUsersFeed>() {
            @Override
            public void onResponse(Call<GitHubUsersFeed> call, Response<GitHubUsersFeed> response) {
                if (response.isSuccessful()) {
                    listener.OnLoadDataResponseIsSuccesfull(
                            response.body().getItems(),
                            response.body().getTotalCount(),
                            LOAD_DATA_FIRST_TIME_TAG);
                }
                else{
                    if (response.headers().get(HEADER_STATUS_FIELD).contains(STATUS_FIELD_CONTAINS_403ERROR))
                        listener.OnLoadDataResponseIsNotSuccesfull(ON403_MESSAGE);
                    else
                        listener.OnLoadDataResponseIsNotSuccesfull(response.message());
                }
            }
            @Override
            public void onFailure(Call<GitHubUsersFeed> call, Throwable t) {

                    listener.OnLoadDataFailure( t.getMessage().toString());

            }
        });
    }
    @Override
    public void loadMoreData(final String query, final ApiInteractor.OnLoadDataFinished listener){
        String mainQuery=URL_START+query+URL_FINISH;
        final Call<GitHubUsersFeed> usersListCall = service.searchUsers(mainQuery,mCurrentPage,mLoadPerPage);
        usersListCall.enqueue(new Callback<GitHubUsersFeed>() {
            @Override
            public void onResponse(Call<GitHubUsersFeed> call, Response<GitHubUsersFeed> response) {
                if (response.isSuccessful()) {
                    listener.OnLoadDataResponseIsSuccesfull(
                            response.body().getItems(),
                            response.body().getTotalCount(),
                            LOAD_MORE_DATA_TAG);
                }
                else{
                    if (response.headers().get(HEADER_STATUS_FIELD).contains(STATUS_FIELD_CONTAINS_403ERROR))
                        listener.OnLoadDataResponseIsNotSuccesfull(ON403_MESSAGE);
                    else
                    {
                        listener.OnLoadDataResponseIsNotSuccesfull(response.message());
                    }
                }
            }
            @Override
            public void onFailure(Call<GitHubUsersFeed> call, Throwable t) {

                    listener.OnLoadDataFailure( t.getMessage().toString());


            }
        });
    }
    @Override
    public void doLogin(String username, final String password, final ApiInteractor.OnLoginProccesed listener){
            service.authorithation(Credentials.basic(username, password))
                .enqueue(new Callback<GitHubUser>() {
                    @Override
                    public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {

                        if (!response.isSuccessful()){

                            listener.OnLoginResponseIsNotSuccesfull();

                        }
                        else {
                            listener.OnLoginResponseIsSuccesfull(response.body(),password);
                        }
                    }

                    @Override
                    public void onFailure(Call<GitHubUser> call, Throwable t) {

                        listener.OnLoginFailure(t);
                    }
                });
    }

   @Override
    public void getUserDetails(String githubuserLogin, final ApiInteractor.OnUserDetailsRecieved listener) {
        service.getUserDetails(githubuserLogin).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                    if (response.isSuccessful())
                    {
                        listener.OnUserDetailsRecievedIsSuccesfull(response.body());
                    }
                    else{
                        listener.OnUserDetailsRecievedIsNotSuccesfull(response.message());
                    }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                listener.OnUserDetailsRecievedFailure(t.getMessage());

            }
        });
    }
}
