package com.example.orange.navigationdrawersearchview.Login;

import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.GitHubApi.Interactor;
import com.example.orange.navigationdrawersearchview.SearchPresenterImpl;

public class LoginDialogPresenterImpl implements LoginDialogPresenter,
        Interactor.OnLoginProccesed {

    private LoginDialogView mDialogView;
    private ApiInteractorImpl mApiInteractorImpl;

    public final String DIALOG_USERNAME_WARNING="Enter login!!!";
    public final String DIALOG_PASSWORD_WARNING ="Enter password!!!!";
    public final String DIALOG_WRONG="Check your username or password!!!";
    public final String DIALOG_FAILURE="Login failure!!!";
    public final String DIALOG_LOGIN_SUCCESFULL="You have logged in!!!";

    public LoginDialogPresenterImpl(LoginDialogView dialogView, SearchPresenterImpl searchPresenterImpl){
        mDialogView=dialogView;
        mApiInteractorImpl = new ApiInteractorImpl();
    }

    public void loginPositiveButtonClicked(String username,String password){

        if (username.length()==0) {
            mDialogView.showToast(DIALOG_USERNAME_WARNING);
        }
        else if (password.length()==0) {
            mDialogView.showToast(DIALOG_PASSWORD_WARNING);
        }
        else{
            mApiInteractorImpl.doLogin(username,password, this);
        }

    }

    @Override
    public void loginNegativeButtonClicked() {
        mDialogView.loginDialogClose(null,null);

    }

    @Override
    public void onLoginResponseIsSuccesfull(GitHubUser gitHubUser) {
        mDialogView.showToast(DIALOG_LOGIN_SUCCESFULL);
        mDialogView.loginDialogClose(gitHubUser.getLogin(),gitHubUser.getAvatarUrl());
    }

    @Override
    public void onLoginResponseIsNotSuccesfull() {
        mDialogView.showToast(DIALOG_WRONG);

    }

    @Override
    public void onLoginFailure(Throwable t) {
        mDialogView.showToast(DIALOG_FAILURE+" "+t.getMessage());
    }
}
