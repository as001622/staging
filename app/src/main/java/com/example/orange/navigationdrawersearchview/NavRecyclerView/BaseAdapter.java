package com.example.orange.navigationdrawersearchview.NavRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<GitHubUser> mUserList;
    RecyclerViewListener mListener;
    String mAdapterToUseTag;

    public BaseAdapter(List<GitHubUser> userList, RecyclerViewListener listener, String adapterToUseTag){
        mUserList=userList;
        mAdapterToUseTag = adapterToUseTag;
        mListener=listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navigation_recyclerview_layout, parent, false);
        return new BaseViewHolder(itemView, mListener, mAdapterToUseTag);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        final GitHubUser singleUser = mUserList.get(position);
        Picasso.get().load(singleUser.getAvatarUrl()).into(holder.mUserAvatarImage);
        holder.mUserLogin.setText(singleUser.getLogin().toString());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}