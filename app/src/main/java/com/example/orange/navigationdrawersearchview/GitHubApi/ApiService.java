package com.example.orange.navigationdrawersearchview.GitHubApi;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.Model.GitHubUsersFeed;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService
{
    @GET("users")
    Call<List<GitHubUser>> getUsers();

    @GET("user")
    Call<GitHubUser> authorithation(@Header("Authorization") String credentials);


    //https://api.github.com/search/users?q=100+in:login
    @GET("users/{user}")
    Call<GitHubUser> getUserDetails(@Path("user") String userLogin);

    @GET()
    Call<GitHubUsersFeed> search(@Url String url);

    @GET()
    Call<GitHubUsersFeed> searchUsers(@Url String url,@Query("page") Integer page,@Query("per_page") Integer per_page);
}