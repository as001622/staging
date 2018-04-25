package com.example.orange.navigationdrawersearchview.Presenter;

import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;
import com.example.orange.navigationdrawersearchview.MainView;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavRecyclerViewListener;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.BaseAdapter;

import org.json.JSONException;

import java.util.List;

import io.realm.Realm;

public class SearchPresenterImpl implements Presenter,Constants,
                                        ApiInteractor.OnLoadDataFinished,
                                        NavRecyclerViewListener {

    private MainView mMainView;
    private ApiInteractorImpl mApiInteractorImpl;
    private DatabaseInteractorImpl mDatabaseInteractorImpl;
    private BaseAdapter mBaseAdapter;
    private BaseAdapter mMainAdapter;
    private List<GitHubUser> mNavUserList;
    private List<GitHubUser> mMainUserList;
    private String mLoginToDoSomeInDatabase;
    private String mLoginForLoginnedUser;
    private String mPasswordForLoginnedUser;
    private Realm mRealm;
    private String mNavSearchQuery;


    public SearchPresenterImpl(MainView mainView,Realm realm) {
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

            mApiInteractorImpl.loadData(navSearchQuery, this);
        //   mDatabaseInteractorImpl.clearNavData(mLoginForLoginnedUser);
        }
        else {
            mMainView.setNavRecyclerViewAdapter(null);
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
    public void OnLoadDataResponseIsSuccesfull(List<GitHubUser> userList,Integer total_count) {
        mMainView.showToast(total_count.toString()+TOTAL_COUNT_MESSAGE+mNavSearchQuery);
        //mDatabaseInteractorImpl.insertDataForNavList(mLoginForLoginnedUser)
        mNavUserList =userList;
        setNavAdapter();
    }

    @Override
    public void OnLoadDataResponseIsNotSuccesfull(String message) {
        mMainView.showToast(RESPONSE_UNSUCCESFULL + message);

    }

    @Override
    public void OnLoadDataFailure(String message) throws JSONException {
        mMainView.showToast(RESPONSE_FAILURE + message);
    }

    public void navLogoutPressed(){
        mMainView.setUserAsGuest();
        setMainAdapter(GUEST_LOGIN);

    }

    public void navLoginPressed(){
        mMainView.createLoginDialog();

    }

    public void activityStarted(String login,String password, String avatarArl) {
        if (login == null)
        {
            mLoginForLoginnedUser =GUEST_LOGIN;
            mMainView.setUserAsGuest();
            setMainAdapter(mLoginForLoginnedUser);
            mApiInteractorImpl = new ApiInteractorImpl(null,null);
        }
        else{
            mLoginForLoginnedUser =login;
            mPasswordForLoginnedUser=password;
            mApiInteractorImpl = new ApiInteractorImpl(mLoginForLoginnedUser,mPasswordForLoginnedUser);
            mMainView.setUser(login, avatarArl);
            setMainAdapter(mLoginForLoginnedUser);
        }
    }
    private void setMainAdapter(String mainLogin){
        mMainUserList=mDatabaseInteractorImpl.getListForMainRecyclerView(mainLogin);
        mMainAdapter = new BaseAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);
    }

    private void setNavAdapter(){
        mNavUserList=mDatabaseInteractorImpl.getListForNavRecyclerView(mLoginForLoginnedUser, mNavUserList);
        mBaseAdapter = new BaseAdapter(mNavUserList, this,NAV_RECYCLERVIEW_TAG);
        mMainView.setNavRecyclerViewAdapter(mBaseAdapter);
    }

    @Override
    public void onNavAddButtonClick(String userToAddLogin) {
        mLoginToDoSomeInDatabase =userToAddLogin;
        //mMainView.showToast("Adding");
        mMainView.showAlertDialog(addDialogTitle,addNewDataToDatabase,NAV_ADD_DIALOG_TAG);
    }

    @Override
    public void onNavDeleteButtonClick(String userToDeleteLogin) {
        mLoginToDoSomeInDatabase =userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,deleteDataFromTheList,NAV_DELETE_DIALOG_TAG);
    }

    @Override
    public void onMainDeleteButtonClick(String userToDeleteLogin) {
        mLoginToDoSomeInDatabase=userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,mainDialogMessage,MAIN_RECYCLERVIEW_TAG);

    }


    public void confirmationClicked(String tag){
        if (tag==NAV_ADD_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mMainView.showToast(ADDING_USER_TEXT+ mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewUser(gitHubUser, mLoginForLoginnedUser);
            setMainAdapter(mLoginForLoginnedUser);

        }
        else if (tag==NAV_DELETE_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            if (!mDatabaseInteractorImpl.getListForMainRecyclerView(mLoginForLoginnedUser).contains(gitHubUser))
            {
                mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser, mLoginForLoginnedUser);
                setNavAdapter();
                setMainAdapter(mLoginForLoginnedUser);
                mMainView.showToast(DELETING_USER_TEXT + mLoginToDoSomeInDatabase);
            }
            else{
                mMainView.showAlertDialog(collisionDialogTitle, collsionDialogMessage, COLLISION_DIALOG_TAG);
            }
        }
        else if (tag==COLLISION_DIALOG_TAG)
        {
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser, mLoginForLoginnedUser);
            setNavAdapter();
            setMainAdapter(mLoginForLoginnedUser);

        }
        else if (tag==MAIN_RECYCLERVIEW_TAG)
        {

            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.deleteUser(gitHubUser);
            setMainAdapter(mLoginForLoginnedUser);
            mMainView.showToast(mainDialogMessage+" "+mLoginToDoSomeInDatabase);

        }

    }

    public GitHubUser getUser(String login){
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
}


