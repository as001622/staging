package com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.orange.navigationdrawersearchview.GitHubApi.Interactor;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationViewHolder> {
    private List<GitHubUser> mUserList;
    NavRecyclerViewListener mListener;

    public NavigationAdapter(List<GitHubUser> userList, NavRecyclerViewListener listener){
        mUserList=userList;
        mListener=listener;
    }

    @NonNull
    @Override
    public NavigationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navigation_recyclerview_layout, parent, false);
        return new NavigationViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final NavigationViewHolder holder, final int position) {

        final GitHubUser singleUser = mUserList.get(position);
        Picasso.get().load(singleUser.getAvatarUrl()).into(holder.mUserAvatarImage);
        holder.mUserLogin.setText(singleUser.getLogin().toString());


    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}