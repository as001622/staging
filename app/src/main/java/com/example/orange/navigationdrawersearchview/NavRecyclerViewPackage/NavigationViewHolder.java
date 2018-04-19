package com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orange.navigationdrawersearchview.GitHubApi.Interactor;
import com.example.orange.navigationdrawersearchview.R;

public class NavigationViewHolder extends RecyclerView.ViewHolder{
    public ImageView mUserAvatarImage;
    public TextView mUserLogin;
    public ImageButton mDeleteButton;
    public ProgressBar mProgressBar;
    public ImageButton mAddButton;
    public NavRecyclerViewListener mListener;

    public NavigationViewHolder(View itemView,NavRecyclerViewListener listener) {
        super(itemView);
        mListener=listener;
        mUserAvatarImage =(ImageView) itemView.findViewById(R.id.avatar_url);
        mUserLogin=(TextView)itemView.findViewById(R.id.login_text_view);
        mDeleteButton =(ImageButton) itemView.findViewById(R.id.delete_image);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteButtonClick(mUserLogin.getText().toString());
            }
        });
        mAddButton = (ImageButton)itemView.findViewById(R.id.add_image);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddButtonClick(mUserLogin.getText().toString());
            }
        });

        mProgressBar =(ProgressBar)itemView.findViewById(R.id.search_progress_bar);
    }
}