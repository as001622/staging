package com.example.orange.navigationdrawersearchview.DetailsActivity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.R;
import com.squareup.picasso.Picasso;

public class GitHubUserDetailsActivity extends AppCompatActivity implements Constants, DetailsView {

    private DetailsPresenterImpl mDetailsPresenterImpl;
    private ConstraintLayout mConstraintLayout;
    private String mGitHubUserLogin;
    private ImageView mDetailsAvatarImage;
    private TextView mDetailsUserLogin;
    private TextView mDetailsEmail;
    private TextView mDetailsUserName;
    private TextView mDetailsBlog;
    private TextView mDetailsFollowers;
    private TextView mDetailsMessage;
    private Button mDetailsBackButton;
    private String mDetailsAvatarImageUrl;
    private boolean mLoaded=false;




    private TextView mDetailsCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_user_details);

        mDetailsAvatarImage=findViewById(R.id.details_avatar_image);
        mDetailsUserLogin=findViewById(R.id.details_user_login);
        mDetailsEmail=findViewById(R.id.details_email);
        mDetailsUserName=findViewById(R.id.details_user_name);
        mDetailsBlog=findViewById(R.id.details_blog);
        mDetailsCompany=findViewById(R.id.details_company);
        mDetailsFollowers =findViewById(R.id.details_followers);
        mDetailsMessage =findViewById(R.id.details_message);
        mDetailsBackButton=findViewById(R.id.details_back_button);
        mConstraintLayout=findViewById(R.id.details_layout);

        if (!mLoaded) {
            Intent intent = getIntent();
            mGitHubUserLogin = intent.getStringExtra(GITHUBUSER_LOGIN);
            setDetailsUserLogin(mGitHubUserLogin);
            mDetailsPresenterImpl = new DetailsPresenterImpl(this, mGitHubUserLogin);
        }
    }

    public void setDetailsAvatarImage(String avatarUrl){
        mLoaded=true;
        if (avatarUrl!=null) {
            Picasso.get().load(avatarUrl).into(mDetailsAvatarImage);
            mDetailsAvatarImageUrl=avatarUrl;
        }
        else mDetailsAvatarImage.setImageResource(R.drawable.githublogo);
    }

    public void setDetailsEmail(String email) {
        if (email!=null)
            mDetailsEmail.setText(email);
        else mDetailsEmail.setText(UNSSETLED_MESSAGE);
    }

    public void setDetailsFollowers(String followersCount) {
        if (followersCount!=null)
            mDetailsFollowers.setText(followersCount);
        else mDetailsFollowers.setText(UNSSETLED_MESSAGE);
    }

    public void setDetailsUserName(String userName) {
        if (userName!=null)
            mDetailsUserName.setText(userName);
        else mDetailsUserName.setText(UNSSETLED_MESSAGE);
    }

    public void setDetailsBlog(String detailsBlog) {
        if ((detailsBlog!=null)|(detailsBlog.isEmpty()))
            mDetailsBlog.setText(detailsBlog);
        else mDetailsBlog.setText(UNSSETLED_MESSAGE);
    }

    public void setDetailsCompany(String detailsCompany) {
        if (detailsCompany!=null)
            mDetailsCompany.setText(detailsCompany);
        else mDetailsCompany.setText(UNSSETLED_MESSAGE);
    }
    public void setDetailsUserLogin(String login){
        if (login!=null)
            mDetailsUserLogin.setText(login);
        else mDetailsUserLogin.setText(UNSSETLED_MESSAGE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ((mDetailsAvatarImageUrl!=null)&(mLoaded)){
            outState.putString(DETAILS_AVATAR_IMAGE_TAG,mDetailsAvatarImageUrl);
            outState.putString(DETAILS_BLOG_TAG,mDetailsBlog.getText().toString());
            outState.putString(DETAILS_COMPANY_TAG,mDetailsCompany.getText().toString());
            outState.putString(DETAILS_EMAIL_TAG,mDetailsEmail.getText().toString());
            outState.putString(DETAILS_FOLLOWERS_TAG,mDetailsFollowers.getText().toString());
            outState.putString(DETAILS_USER_LOGIN_TAG,mDetailsUserLogin.getText().toString());
            outState.putString(DETAILS_USERNAME_TAG,mDetailsUserName.getText().toString());
        }
        outState.putBoolean(DETAILS_LOADED_TAG,mLoaded);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDetailsAvatarImageUrl=savedInstanceState.getString(DETAILS_AVATAR_IMAGE_TAG);
        mLoaded=savedInstanceState.getBoolean(DETAILS_LOADED_TAG);
        if (mDetailsAvatarImageUrl!=null)
        {
            setDetailsAvatarImage(mDetailsAvatarImageUrl);
            mDetailsBlog.setText(savedInstanceState.getString(DETAILS_BLOG_TAG));
            mDetailsCompany.setText(savedInstanceState.getString(DETAILS_COMPANY_TAG));
            mDetailsEmail.setText(savedInstanceState.getString(DETAILS_EMAIL_TAG));
            mDetailsFollowers.setText(savedInstanceState.getString(DETAILS_FOLLOWERS_TAG));
            mDetailsUserLogin.setText(savedInstanceState.getString(DETAILS_USER_LOGIN_TAG));
            mDetailsUserName.setText(savedInstanceState.getString(DETAILS_USERNAME_TAG));
            showDetailsLayout();
        }
    }

    public void showDetailsLayout(){
        mDetailsMessage.setVisibility(View.GONE);
        mConstraintLayout.setVisibility(View.VISIBLE);
        mDetailsBackButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBackButton() {
        mDetailsMessage.setVisibility(View.GONE);
        mDetailsBackButton.setVisibility(View.VISIBLE);

    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void detailsOnOkButtonClick(View view) {
        this.finish();
    }
}

