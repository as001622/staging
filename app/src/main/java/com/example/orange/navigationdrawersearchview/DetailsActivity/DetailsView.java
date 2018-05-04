package com.example.orange.navigationdrawersearchview.DetailsActivity;

public interface DetailsView {
    public void setDetailsEmail(String email);
    public void setDetailsUserName(String userName);
    public void setDetailsBlog(String detailsBlog);
    public void setDetailsCompany(String detailsCompany);
    public void setDetailsAvatarImage(String avatarUrl);
    public void setDetailsUserLogin(String login);
    public void setDetailsFollowers(String folowersCount);
    public void showDetailsLayout();
    public void showBackButton();
    void showToast(String message);
}
