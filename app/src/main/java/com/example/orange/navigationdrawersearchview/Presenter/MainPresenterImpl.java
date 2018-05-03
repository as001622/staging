package com.example.orange.navigationdrawersearchview.Presenter;

import android.util.Log;

import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractorImpl;
import com.example.orange.navigationdrawersearchview.DetailsActivity.DetailsView;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;
import com.example.orange.navigationdrawersearchview.MainView;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.NavRecyclerView.RecyclerViewListener;
import com.example.orange.navigationdrawersearchview.NavRecyclerView.BaseAdapter;

import org.json.JSONException;

import java.util.List;

import io.realm.Realm;

public class MainPresenterImpl implements Presenter,
                                        Constants,
                                        ApiInteractor.OnLoadDataFinished,
        RecyclerViewListener {

    private MainView mMainView;
    private ApiInteractorImpl mApiInteractorImpl;
    private DatabaseInteractorImpl mDatabaseInteractorImpl;
    private BaseAdapter mBaseAdapter;
    private BaseAdapter mMainAdapter;
    private List<GitHubUser> mNavUserList;
    private List<GitHubUser> mMainUserList;
    private String mGitHubLoginToDoSomeInDatabase;
    private String mLoginForLoginnedUser;
    private String mPasswordForLoginnedUser;
    private Realm mRealm;
    private boolean loading=false;
    private String mNavSearchQuery;


    public MainPresenterImpl(MainView mainView, Realm realm) {
        mDatabaseInteractorImpl=new DatabaseInteractorImpl(realm);
        mMainView = mainView;
        mRealm=realm;
    }

    public void navSearchViewDataChanged(String navSearchQuery) {
        mNavSearchQuery=navSearchQuery;
        if (mNavSearchQuery==null)
        {
            mMainView.setNavRecyclerViewAdapter(null);
        }
        else if (navSearchQuery.length()>minimumNavTextSearchSize) {
            ApiInteractorImpl.mCurrentPage=firstPage;
            Log.v("currentpage"," "+(String.valueOf(ApiInteractorImpl.mCurrentPage)));
            mApiInteractorImpl.firstTimeLoadData(mNavSearchQuery, this);
        }
        else {
            mMainView.setNavRecyclerViewAdapter(null);
        }
    }

    public void needMoreData(){
        if (!loading) {
            loading=true;
            Log.v("currentpage", " " + (String.valueOf(ApiInteractorImpl.mCurrentPage)));
            mApiInteractorImpl.loadMoreData(mNavSearchQuery, this);
        }
    }

    public void mainSearchViewDataChanged(String mainSearchQuery) {
        if (mainSearchQuery.length()>minimumMainTextSearchSize) {
            mMainUserList=mDatabaseInteractorImpl.getListForMainSearchView(mainSearchQuery);
            mMainAdapter = new BaseAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
            mMainView.setMainRecyclerViewAdapter(mMainAdapter);
        }
        else setMainAdapter(mLoginForLoginnedUser);
    }

    @Override
    public void OnLoadDataResponseIsSuccesfull(List<GitHubUser> userList,Integer total_count,String TAG) {
        if ((mNavUserList==null)|(TAG==LOAD_DATA_FIRST_TIME_TAG)) {
            mNavUserList=userList;

        }
        else {
            if (TAG==LOAD_MORE_DATA_TAG) {

                mNavUserList.addAll(userList);
            }
        }
        ApiInteractorImpl.mCurrentPage++;
        setNavAdapter();
    }

    @Override
    public void OnLoadDataResponseIsNotSuccesfull(String message) {
        loading=false;
        mMainView.showToast(RESPONSE_UNSUCCESFULL + message);

    }

    @Override
    public void OnLoadDataFailure(String message) {
        mMainView.showToast(RESPONSE_FAILURE + message);
    }

    public void navLogoutPressed(){
        mMainView.setUserAsGuest();
        mLoginForLoginnedUser =GUEST_LOGIN;
        mDatabaseInteractorImpl.setLogin(mLoginForLoginnedUser);
        setMainAdapter(GUEST_LOGIN);

    }

    public void navLoginPressed(){
        mMainView.createLoginDialog();
    }

    public void saveData(String login){

        Log.v("dadd",mLoginForLoginnedUser);
        mDatabaseInteractorImpl.setLogin(mLoginForLoginnedUser);
        mDatabaseInteractorImpl.saveData(mNavUserList);
    }

    public void restoreData(String login){
        mDatabaseInteractorImpl.setLogin(mLoginForLoginnedUser);
        mNavUserList=mDatabaseInteractorImpl.restoreData();
        setNavAdapter();
    }

    public void activityStarted(String login,String password, String avatarArl) {
        if (login == null) {
            mLoginForLoginnedUser =GUEST_LOGIN;
            mDatabaseInteractorImpl.setLogin(mLoginForLoginnedUser);
            mMainView.setUserAsGuest();
            mMainView.setNavRecyclerViewAdapter(null);
            setMainAdapter(mLoginForLoginnedUser);
            mApiInteractorImpl = new ApiInteractorImpl(null,null);
        }
        else{
            mLoginForLoginnedUser =login;
            mDatabaseInteractorImpl.setLogin(mLoginForLoginnedUser);
            mPasswordForLoginnedUser=password;
            mApiInteractorImpl = new ApiInteractorImpl(mLoginForLoginnedUser,mPasswordForLoginnedUser);
            mMainView.setUser(login, avatarArl);
            setMainAdapter(mLoginForLoginnedUser);
        }
    }

    private void setMainAdapter(String mainLogin){
        mMainUserList=mDatabaseInteractorImpl.getListForMainRecyclerView();
        mMainAdapter = new BaseAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);
    }

    private void setNavAdapter(){
        mNavUserList=mDatabaseInteractorImpl.getListForNavRecyclerView(mNavUserList);
        mBaseAdapter = new BaseAdapter(mNavUserList, this,NAV_RECYCLERVIEW_TAG);
        mMainView.setNavRecyclerViewAdapter(mBaseAdapter);
        loading=false;
    }

    @Override
    public void onNavAddButtonClick(String userToAddLogin) {
        mGitHubLoginToDoSomeInDatabase =userToAddLogin;
        //mMainView.showToast("Adding");
        mMainView.showAlertDialog(addDialogTitle,addNewDataToDatabase,NAV_ADD_DIALOG_TAG);
    }

    @Override
    public void onNavDeleteButtonClick(String userToDeleteLogin) {
        mGitHubLoginToDoSomeInDatabase =userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,deleteDataFromTheList,NAV_DELETE_DIALOG_TAG);
    }

    @Override
    public void onMainDeleteButtonClick(String userToDeleteLogin) {
        mGitHubLoginToDoSomeInDatabase =userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,mainDialogMessage,MAIN_RECYCLERVIEW_TAG);
    }

    public void confirmationClicked(String tag){
        if (tag==NAV_ADD_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mGitHubLoginToDoSomeInDatabase);
            mMainView.showToast(ADDING_USER_TEXT+ mGitHubLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewUser(gitHubUser);
            setMainAdapter(mLoginForLoginnedUser);
        }
        else if (tag==NAV_DELETE_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mGitHubLoginToDoSomeInDatabase);
            if (!mDatabaseInteractorImpl.getListForMainRecyclerView().contains(gitHubUser))
            {
                mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser);
                setNavAdapter();
                setMainAdapter(mLoginForLoginnedUser);
                mMainView.showToast(DELETING_USER_TEXT + mGitHubLoginToDoSomeInDatabase);
            }
            else{
                mMainView.showAlertDialog(collisionDialogTitle, collsionDialogMessage, COLLISION_DIALOG_TAG);
            }
        }
        else if (tag==COLLISION_DIALOG_TAG)
        {
            GitHubUser gitHubUser= getUser(mGitHubLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.deleteUser(gitHubUser);
            mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser);
            setNavAdapter();
            setMainAdapter(mLoginForLoginnedUser);
        }
        else if (tag==MAIN_RECYCLERVIEW_TAG)
        {
            GitHubUser gitHubUser= getUser(mGitHubLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.deleteUser(gitHubUser);
            setMainAdapter(mLoginForLoginnedUser);
            mMainView.showToast(mainDialogMessage+" "+ mGitHubLoginToDoSomeInDatabase);
        }
    }

    private GitHubUser getUser(String login){
        boolean found=false;
        if (mNavUserList!=null)
            for (GitHubUser user: mNavUserList){
                if(login == user.getLogin()) {
                    found=true;
                    return user;
                }
            }
        if(!found) {
            for (GitHubUser user : mMainUserList) {
                if (login == user.getLogin()) {
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void onMainRecyclerViewListClick(String login) {
        mMainView.gitHubUserActivityStart(login);
    }

}


