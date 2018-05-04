package com.example.orange.navigationdrawersearchview.Login;

public interface LoginDialogView {
    public void setOnButtonClickListeners();
    public void showToast(String text);
    public void loginDialogClose(String login, String avatarUrl);
    void setResponseSent(Boolean responseSent);
}
