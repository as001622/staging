package com.example.orange.navigationdrawersearchview.NavRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orange.navigationdrawersearchview.R;
import com.example.orange.navigationdrawersearchview.Presenter.MainPresenterImpl;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView mUserAvatarImage;
    public TextView mUserLogin;
    public ImageButton mDeleteButton;
    public ProgressBar mProgressBar;
    public ImageButton mAddButton;
    public RecyclerViewListener mListener;
    private String mAdapterToUseTag;

    public BaseViewHolder(View itemView, RecyclerViewListener listener, final String adapterToUseTag) {
        super(itemView);
        mAdapterToUseTag=adapterToUseTag;
        mListener=listener;
        mUserAvatarImage = itemView.findViewById(R.id.avatar_url);
        mUserLogin=itemView.findViewById(R.id.login_text_view);
        mProgressBar =itemView.findViewById(R.id.search_progress_bar);
        mDeleteButton = itemView.findViewById(R.id.delete_image);
        mAddButton =  itemView.findViewById(R.id.add_image);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapterToUseTag== MainPresenterImpl.MAIN_RECYCLERVIEW_TAG) {
                    mListener.onMainDeleteButtonClick(mUserLogin.getText().toString());
                }
                else {
                    mListener.onNavDeleteButtonClick(mUserLogin.getText().toString());
                }
            }
        });
        if (mAdapterToUseTag== MainPresenterImpl.MAIN_RECYCLERVIEW_TAG)
            mAddButton.setVisibility(View.GONE);
        else {
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNavAddButtonClick(mUserLogin.getText().toString());
                }
            });
        }

        if (mAdapterToUseTag== MainPresenterImpl.MAIN_RECYCLERVIEW_TAG)
            itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        mListener.onMainRecyclerViewListClick(this.mUserLogin.getText().toString());
    }


}