package com.example.orange.navigationdrawersearchview.DetailsActivity;

import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;

public class DetailsPresenterImpl implements ApiInteractor.OnUserDetailsRecieved {

    private DetailsView mDetailsView;
    private String mGitHubUserLogin;
    private ApiInteractorImpl mApiInteractorImpl;

    public DetailsPresenterImpl(DetailsView detailsView, String gitHubUserLogin){
        mDetailsView=detailsView;
        mGitHubUserLogin=gitHubUserLogin;
        mApiInteractorImpl=new ApiInteractorImpl();
        mApiInteractorImpl.getUserDetails(mGitHubUserLogin, this);
    }

    @Override
    public void OnUserDetailsRecievedIsSuccesfull(GitHubUser gitHubUser) {
        mDetailsView.setDetailsEmail(gitHubUser.getEmail());
        mDetailsView.setDetailsUserName(gitHubUser.getName());
        mDetailsView.setDetailsBlog(gitHubUser.getBlog());
        mDetailsView.setDetailsCompany(gitHubUser.getCompany());
        mDetailsView.setDetailsAvatarImage(gitHubUser.getAvatarUrl());
        mDetailsView.setDetailsFollowers(gitHubUser.getFollowers());
        mDetailsView.showDetailsLayout();
    }

    @Override
    public void OnUserDetailsRecievedIsNotSuccesfull(String message) {
        mDetailsView.showToast(message);
        mDetailsView.showBackButton();

    }

    @Override
    public void OnUserDetailsRecievedFailure(String message) {
        mDetailsView.showToast(message);
        mDetailsView.showBackButton();
    }
}
