package com.example.orange.navigationdrawersearchview.Login;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;

public class LoginDialogPresenterImpl implements LoginDialogPresenter, Constants,
        ApiInteractor.OnLoginProccesed {

    private LoginDialogView mDialogView;
    private ApiInteractorImpl mApiInteractorImpl;


    public LoginDialogPresenterImpl(LoginDialogView dialogView){
        mDialogView=dialogView;
        mApiInteractorImpl = new ApiInteractorImpl(null,null);
    }

    public void loginPositiveButtonClicked(String username,String password){

        if (username==EMPTY_STRING) {
            mDialogView.showToast(DIALOG_USERNAME_WARNING);
        }
        else if (password==EMPTY_STRING) {
            mDialogView.showToast(DIALOG_PASSWORD_WARNING);
        }
        else{
            mApiInteractorImpl.doLogin(username,password, this);
        }

    }

    @Override
    public void loginNegativeButtonClicked() {
        mDialogView.loginDialogClose(null,null, null);

    }

    @Override
    public void OnLoginResponseIsSuccesfull(GitHubUser gitHubUser,String password) {
        mDialogView.showToast(DIALOG_LOGIN_SUCCESFULL);
        mDialogView.setResponseSent(false);
        mDialogView.loginDialogClose(gitHubUser.getLogin(),password,gitHubUser.getAvatarUrl());
    }

    @Override
    public void OnLoginResponseIsNotSuccesfull() {
        mDialogView.showToast(DIALOG_WRONG);
        mDialogView.setResponseSent(false);

    }

    @Override
    public void OnLoginFailure(Throwable t) {
        mDialogView.showToast(DIALOG_FAILURE+" "+t.getMessage());
        mDialogView.setResponseSent(false);
    }
}
