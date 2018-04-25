package com.example.orange.navigationdrawersearchview.GitHubApi;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import org.json.JSONException;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiInteractorImpl implements Constants{

    private List<GitHubUser> mUserList;
    private ApiService service;

    public ApiInteractorImpl(String login,String password){
        service = new ApiBuilder(login,password).getService();
    }


    public void loadData(final String query, final ApiInteractor.OnLoadDataFinished listener){
        String mainQuery=URL_START+query+URL_FINISH;
        final Call<GitHubUsersFeed> usersListCall = service.searchUsers(mainQuery,1,loadPerPage);
        usersListCall.enqueue(new Callback<GitHubUsersFeed>() {
            @Override
            public void onResponse(Call<GitHubUsersFeed> call, Response<GitHubUsersFeed> response) {
                if (response.isSuccessful()) {

                    listener.OnLoadDataResponseIsSuccesfull(response.body().getItems(),response.body().getTotalCount());
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

                try {
                    listener.OnLoadDataFailure( t.getMessage().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void doLogin(String username, final String password, final ApiInteractor.OnLoginProccesed listener){
            service.getUserDetails(Credentials.basic(username, password))
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
}
