package com.example.orange.navigationdrawersearchview.GitHubApi;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder implements Constants {


    private Retrofit retrofit;

    public ApiBuilder() {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        }
        /*else{
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                            Credentials.basic(login, password));

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            }).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }*/

    public ApiService getService() {
        return retrofit.create(ApiService.class);
    }
}
