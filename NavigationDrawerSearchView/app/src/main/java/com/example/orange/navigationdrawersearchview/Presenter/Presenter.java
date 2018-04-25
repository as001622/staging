package com.example.orange.navigationdrawersearchview.Presenter;

public interface Presenter {

    void navSearchViewDataChanged(String text);

    void activityStarted(String login,String password, String avatarArl);

    void confirmationClicked(String dialogTag);

    void navLogoutPressed();

    void navLoginPressed();

    void mainSearchViewDataChanged(String text);
}
