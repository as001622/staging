package com.example.orange.navigationdrawersearchview;

import com.example.orange.navigationdrawersearchview.NavRecyclerView.BaseAdapter;

public interface MainView {
    void showToast(String text);
    public void createLoginDialog();
    public void newUserLoggedIn();
    void setNavSearchView();
    void setUser(String imageUrl,String login);
    void setUserAsGuest();
    void setNavRecyclerViewAdapter(BaseAdapter adapter);
    void showAlertDialog(String addDialogTitle,String addNewDataToDatabase,String tag);
    void setMainRecyclerViewAdapter (BaseAdapter adapter);
    void setMainSearchView();
    void gitHubUserActivityStart(String login);
    interface LoginDialogClosed{
        void onLoginDialogClosed(String login, String avatarUrl);
    }
}
