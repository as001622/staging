package com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orange.navigationdrawersearchview.R;
import com.example.orange.navigationdrawersearchview.SearchPresenterImpl;

public class NavigationViewHolder extends RecyclerView.ViewHolder{
    public ImageView mUserAvatarImage;
    public TextView mUserLogin;
    public ImageButton mDeleteButton;
    public ProgressBar mProgressBar;
    public ImageButton mAddButton;
    public NavRecyclerViewListener mListener;

    public NavigationViewHolder(View itemView,NavRecyclerViewListener listener,final String tag) {
        super(itemView);
        mListener=listener;
        mUserAvatarImage =(ImageView) itemView.findViewById(R.id.avatar_url);
        mUserLogin=(TextView)itemView.findViewById(R.id.login_text_view);
        mDeleteButton =(ImageButton) itemView.findViewById(R.id.delete_image);
        mAddButton = (ImageButton) itemView.findViewById(R.id.add_image);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag==SearchPresenterImpl.MAIN_RECYCLERVIEW_TAG) {
                    mListener.onMainDeleteButtonClick(mUserLogin.getText().toString());
                }
                else {
                    mListener.onNavDeleteButtonClick(mUserLogin.getText().toString());
                }
            }
        });
        if (tag==SearchPresenterImpl.MAIN_RECYCLERVIEW_TAG)
            mAddButton.setVisibility(View.GONE);
        else {
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNavAddButtonClick(mUserLogin.getText().toString());
                }
            });
        }

        mProgressBar =(ProgressBar)itemView.findViewById(R.id.search_progress_bar);
    }
}