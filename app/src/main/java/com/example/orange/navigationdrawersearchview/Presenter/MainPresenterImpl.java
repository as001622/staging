package com.example.orange.navigationdrawersearchview.Presenter;

import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractor;
import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;
import com.example.orange.navigationdrawersearchview.MainView;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.NavRecyclerView.RecyclerViewListener;
import com.example.orange.navigationdrawersearchview.NavRecyclerView.BaseAdapter;

import java.util.List;

import io.realm.Realm;

public class MainPresenterImpl implements Presenter,
                                        Constants,
                                        ApiInteractor.OnLoadDataFinished,
        RecyclerViewListener {

    private MainView mMainView;
    private ApiInteractor mApiInteractor;
    private DatabaseInteractor mDatabaseInteractor;
    private BaseAdapter mBaseAdapter;
    private BaseAdapter mMainAdapter;
    private List<GitHubUser> mNavUserList;
    private List<GitHubUser> mMainUserList;
    private String mGitHubLoginForDatabase;
    private String mLoginForApplication;
    private Realm mRealm;
    private boolean mLoading =false;
    private String mNavSearchQuery;
    private String mMainSearchViewText;


    public MainPresenterImpl(MainView mainView, Realm realm) {
        mDatabaseInteractor=new DatabaseInteractorImpl(realm);
       // mDatabaseInteractor.clearSavedData();
        mMainView = mainView;
        mRealm=realm;
    }

    public void activityStarted(String login, String avatarArl) {
        if (login == GUEST_LOGIN) {
            if (mLoginForApplication==null)
                mLoginForApplication=GUEST_LOGIN;
            else if (mLoginForApplication!=GUEST_LOGIN) {
                mMainView.newUserLoggedIn();
                mLoginForApplication=GUEST_LOGIN;
            }
            mDatabaseInteractor.setLogin(mLoginForApplication);
            mMainView.setUserAsGuest();
            setMainAdapter();
            mApiInteractor = new ApiInteractorImpl();
        }
        else{
            if (mLoginForApplication==null)
                mLoginForApplication=login;
            else if (mLoginForApplication!=login) {
                mMainView.newUserLoggedIn();
                mLoginForApplication=login;
            }
            mDatabaseInteractor.setLogin(mLoginForApplication);
            mApiInteractor = new ApiInteractorImpl();
            mMainView.setUser(login, avatarArl);
            setMainAdapter();
        }
    }

    public void navSearchViewDataChanged(String navSearchQuery) {
        mNavSearchQuery=navSearchQuery;
        if (mNavSearchQuery==null)
        {
            mMainView.setNavRecyclerViewAdapter(null);
        }
        else if (navSearchQuery.length()>minimumNavTextSearchSize) {
            ApiInteractorImpl.mCurrentPage=firstPage;
            mNavSearchQuery=navSearchQuery;
            mApiInteractor.firstTimeLoadData(mNavSearchQuery, this);
        }
        else {
            mMainView.setNavRecyclerViewAdapter(null);
        }
    }

    public void needMoreData(){
        if (!mLoading) {
            mLoading =true;
            mApiInteractor.loadMoreData(mNavSearchQuery, this);
        }
    }

    public void setNavSearchQuery(String navSearchQuery) {
        mNavSearchQuery = navSearchQuery;
    }

    public void mainSearchViewDataChanged(String mainSearchQuery) {
        setMainSearchViewText(mainSearchQuery);;
        if (mMainSearchViewText.length()>minimumMainTextSearchSize) {
            setMainAdapterWithSearchQuery();
        }
        else setMainAdapter();
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
        mLoading =false;
        mMainView.showToast(RESPONSE_UNSUCCESFULL + message);

    }

    public void setLoginForApplication(String login){
        mLoginForApplication=login;
    }

    @Override
    public void OnLoadDataFailure(String message) {
        mMainView.showToast(RESPONSE_FAILURE + message);
    }

    public void navLogoutPressed(){
        mMainView.setUserAsGuest();
        mMainView.newUserLoggedIn();
        mLoginForApplication =GUEST_LOGIN;
        mDatabaseInteractor.setLogin(mLoginForApplication);
        setMainAdapter();

    }

    public void navLoginPressed(){
        mMainView.createLoginDialog();
    }

    public void saveData(){

        mDatabaseInteractor.setLogin(mLoginForApplication);
        mDatabaseInteractor.saveData(mNavUserList);
    }

    public void restoreData(){
        mDatabaseInteractor.setLogin(mLoginForApplication);
        mNavUserList=mDatabaseInteractor.restoreData();
        setNavAdapter();
        mDatabaseInteractor.clearSavedData();
    }

    private void setMainAdapter(){
        mMainUserList=mDatabaseInteractor.getListForMainRecyclerView();
        mMainAdapter = new BaseAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);
    }

    private void setMainAdapterWithSearchQuery(){
        mMainUserList=mDatabaseInteractor.getListForMainSearchView(mMainSearchViewText);
        mMainAdapter = new BaseAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);
    }

    private void setNavAdapter(){
        mNavUserList=mDatabaseInteractor.getListForNavRecyclerView(mNavUserList);
        mBaseAdapter = new BaseAdapter(mNavUserList, this,NAV_RECYCLERVIEW_TAG);
        mMainView.setNavRecyclerViewAdapter(mBaseAdapter);
        mLoading =false;
    }

    @Override
    public void onNavAddButtonClick(String userToAddLogin) {
        mGitHubLoginForDatabase =userToAddLogin;
        mMainView.showAlertDialog(addDialogTitle,addNewDataToDatabase,NAV_ADD_DIALOG_TAG);
    }

    @Override
    public void onNavDeleteButtonClick(String userToDeleteLogin) {
        mGitHubLoginForDatabase =userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,deleteDataFromTheList,NAV_DELETE_DIALOG_TAG);
    }

    @Override
    public void onMainDeleteButtonClick(String userToDeleteLogin) {
        mGitHubLoginForDatabase =userToDeleteLogin;
        mMainView.showAlertDialog(deleteDialogTitle,mainDialogMessage,MAIN_RECYCLERVIEW_TAG);
    }

    public void confirmationClicked(String tag){
        if (tag==NAV_ADD_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mGitHubLoginForDatabase);
            mMainView.showToast(ADDING_USER_TEXT+ mGitHubLoginForDatabase);
            mDatabaseInteractor.insertNewUser(gitHubUser);
            if (mMainSearchViewText==null)
                setMainAdapter();
            else setMainAdapterWithSearchQuery();
        }
        else if (tag==NAV_DELETE_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mGitHubLoginForDatabase);
            if (!mDatabaseInteractor.getListForMainRecyclerView().contains(gitHubUser))
            {
                mDatabaseInteractor.insertNewDeletedUser(gitHubUser);
                setNavAdapter();
                setMainAdapter();
                mMainView.showToast(DELETING_USER_TEXT + mGitHubLoginForDatabase);
            }
            else{
                mMainView.showAlertDialog(collisionDialogTitle, collsionDialogMessage, COLLISION_DIALOG_TAG);
            }
        }
        else if (tag==COLLISION_DIALOG_TAG)
        {
            GitHubUser gitHubUser= getUser(mGitHubLoginForDatabase);
            mDatabaseInteractor.deleteUser(gitHubUser);
            mDatabaseInteractor.insertNewDeletedUser(gitHubUser);
            setNavAdapter();
            setMainAdapter();
        }
        else if (tag==MAIN_RECYCLERVIEW_TAG)
        {
            GitHubUser gitHubUser= getUser(mGitHubLoginForDatabase);
            mDatabaseInteractor.deleteUser(gitHubUser);
            setMainAdapter();
            mMainView.showToast(mainDialogMessage+" "+ mGitHubLoginForDatabase);
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

    public void setMainSearchViewText(String mainSearchViewText) {
        mMainSearchViewText = mainSearchViewText;
    }

    @Override
    public void onMainRecyclerViewListClick(String login) {
        mMainView.gitHubUserActivityStart(login);
    }

}


