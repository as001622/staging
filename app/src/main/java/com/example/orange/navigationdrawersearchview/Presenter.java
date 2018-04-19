package com.example.orange.navigationdrawersearchview;

public interface Presenter {

    void navSearchViewDataChanged(String text);

    void activityStarted(String login, String avatarArl);

    void confirmationClicked(String dialogTag);

    void navLogoutPressed();

    void navLoginPressed();
}
