package com.example.orange.navigationdrawersearchview.DetailsActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.R;
import com.squareup.picasso.Picasso;

public class GitHubUserDetailsActivity extends AppCompatActivity implements Constants, DetailsView {

    private DetailsPresenterImpl mDetailsPresenterImpl;
    private String mGitHubUserLogin;
    private ImageView mDetailsAvatarImage;
    private TextView mDetailsUserLogin;
    private TextView mDetailsEmail;
    private TextView mDetailsUserName;
    private TextView mDetailsBlog;
    private TextView mDetailsFollowers;
    private String unssetled="not specified";




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

        Intent intent=getIntent();
        mGitHubUserLogin = intent.getStringExtra(GITHUBUSER_LOGIN);
        setDetailsUserLogin(mGitHubUserLogin);
        mDetailsPresenterImpl =new DetailsPresenterImpl(this,mGitHubUserLogin);
    }

    public void setDetailsAvatarImage(String avatarUrl){
        if (avatarUrl!=null)
            Picasso.get().load(avatarUrl).into(mDetailsAvatarImage);
        else mDetailsAvatarImage.setImageResource(R.drawable.githublogo);
    }

    public void setDetailsEmail(String email) {
        if (email!=null)
            mDetailsEmail.setText(email);
        else mDetailsEmail.setText(unssetled);
    }

    public void setDetailsFollowers(String followersCount) {
        if (followersCount!=null)
            mDetailsFollowers.setText(followersCount);
        else mDetailsFollowers.setText(unssetled);
    }

    public void setDetailsUserName(String userName) {
        if (userName!=null)
            mDetailsUserName.setText(userName);
        else mDetailsUserName.setText(unssetled);
    }

    public void setDetailsBlog(String detailsBlog) {
        if (detailsBlog!=null)
            mDetailsBlog.setText(detailsBlog);
        else mDetailsBlog.setText(unssetled);
    }

    public void setDetailsCompany(String detailsCompany) {
        if (detailsCompany!=null)
            mDetailsCompany.setText(detailsCompany);
        else mDetailsCompany.setText(unssetled);
    }
    public void setDetailsUserLogin(String login){
        if (login!=null)
            mDetailsUserLogin.setText(login);
        else mDetailsUserLogin.setText(unssetled);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void detailsOnOkButtonClick(View view) {
        this.finish();
    }
}
