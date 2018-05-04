package com.example.orange.navigationdrawersearchview.Presenter;

public interface Presenter {

    void navSearchViewDataChanged(String text);

    void activityStarted(String login,String avatarArl);

    void confirmationClicked(String dialogTag);

    void navLogoutPressed();

    void navLoginPressed();

    void mainSearchViewDataChanged(String text);

    void saveData();

    void restoreData();

    void needMoreData();

    void setNavSearchQuery(String mNavSearchText);

    void setMainSearchViewText(String mainSearchViewText);

    void setLoginForApplication(String login);
}
