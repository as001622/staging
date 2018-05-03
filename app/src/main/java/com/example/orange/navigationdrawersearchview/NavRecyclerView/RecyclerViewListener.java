package com.example.orange.navigationdrawersearchview.NavRecyclerView;

import android.view.View;

public interface RecyclerViewListener {
    void onNavAddButtonClick(String userToAddLogin);
    void onNavDeleteButtonClick(String userToDeleteLogin);
    void onMainDeleteButtonClick(String userToDeleteLogin);
    void onMainRecyclerViewListClick(String userToClickLogin);
}
