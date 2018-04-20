package com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage;

import android.view.View;

public interface NavRecyclerViewListener {
    void onNavAddButtonClick(String userToAddLogin);
    void onNavDeleteButtonClick(String userToDeleteLogin);
    void onMainDeleteButtonClick(String userToDeleteLogin);
}
