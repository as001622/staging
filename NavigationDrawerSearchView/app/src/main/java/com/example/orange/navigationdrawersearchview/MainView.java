package com.example.orange.navigationdrawersearchview;

import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.BaseAdapter;

public interface MainView {
    void showToast(String text);
    public void createLoginDialog();
    void setNavSearchView();
    void setUser(String imageUrl,String login);
    void setUserAsGuest();
    void setNavRecyclerViewAdapter(BaseAdapter adapter);
    void showAlertDialog(String addDialogTitle,String addNewDataToDatabase,String tag);
    void setMainRecyclerViewAdapter (BaseAdapter adapter);
    void setMainSearchViewListeners();
    interface LoginDialogClosed{
        void onLoginDialogClosed(String login, String password, String avatarUrl);
    }
}
