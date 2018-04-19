package com.example.orange.navigationdrawersearchview;

import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.Interactor;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavRecyclerViewListener;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavigationAdapter;

import java.util.List;

import io.realm.Realm;

public class SearchPresenterImpl implements Presenter,
                                        Interactor.OnLoadDataFinished,
                                        NavRecyclerViewListener {

    private MainView mMainView;
    private ApiInteractorImpl mApiInteractorImpl;
    private DatabaseInteractorImpl mDatabaseInteractorImpl;
    private NavigationAdapter mNavigationAdapter;
    private NavigationAdapter mMainAdapter;
    private List<GitHubUser> mNavUserList;
    private List<GitHubUser> mMainUserList;
    private String mLoginToDoSomeInDatabase;
    private String mMainLogin;
    private Realm mRealm;

    private final String addNewDataToDatabase="Do you want to add this Github user to database?";
    private final String deleteDataFromTheList="Do you want ot delete this Github user from this list forever?";
    private final String addDialogTitle="Adding";
    private final String deleteDialogTitle="Deleting";
    private final String NAV_ADD_DIALOG_TAG="NAV_ADD_DIALOG_TAG";
    private final String NAV_DELETE_DIALOG_TAG="NAV_DELETE_DIALOG_TAG";
    public final String USER_LOGIN="USER LOGIN";
    public final String GUEST_LOGIN="GUESTGUESTGUESTGUESTGUEST";



    public SearchPresenterImpl(MainView mainView,Realm realm) {
        mApiInteractorImpl = new ApiInteractorImpl();
        mDatabaseInteractorImpl=new DatabaseInteractorImpl(realm);
        mMainView = mainView;
        mRealm=realm;
    }

    public void navSearchViewDataChanged(String text) {
        if (text.length() > 2)
            mApiInteractorImpl.loadData(text, this);

    }


    @Override
    public void onLoadDataResponseIsSuccesfull(List<GitHubUser> userList) {
        mNavUserList =userList;
        mNavigationAdapter = new NavigationAdapter(userList, this);
        mMainView.setNavRecyclerViewAdapter(mNavigationAdapter);
    }

    @Override
    public void onLoadDataResponseIsNotSuccesfull(String message) {
        mMainView.showToast("Response is unsuccesfull:" + message);

    }

    @Override
    public void onLoadDataFailure(String message) {
        mMainView.showToast("Response failure: " + message);
    }

    public void navLogoutPressed(){
        mMainView.setUserAsGuest();
        setMainAdapter(GUEST_LOGIN);

    }

    public void navLoginPressed(){
        mMainView.createLoginDialog();

    }

    public void activityStarted(String login, String avatarArl) {
        if (login == null)
        {
            mMainLogin=GUEST_LOGIN;
            mMainView.setUserAsGuest();
            mMainView.showToast(GUEST_LOGIN);
            setMainAdapter(mMainLogin);
        }
        else{
            mMainLogin=login;
            mMainView.showToast(login+login+login);
            mMainView.setUser(login, avatarArl);
            setMainAdapter(mMainLogin);
        }
    }
    private void setMainAdapter(String mainLogin){
        mMainUserList=mDatabaseInteractorImpl.getListForMainRecyclerView(mainLogin);
        mMainAdapter = new NavigationAdapter(mMainUserList, this);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);

    }

    @Override
    public void onAddButtonClick(String userToAddLogin) {
        mLoginToDoSomeInDatabase =userToAddLogin;
        //mMainView.showToast("Adding");
        mMainView.showAlertDialog(addDialogTitle,addNewDataToDatabase,NAV_ADD_DIALOG_TAG);
    }

    @Override
    public void onDeleteButtonClick(String userToDeleteLogin) {
        mLoginToDoSomeInDatabase =userToDeleteLogin;
       // mMainView.showToast("Deleting");
        mMainView.showAlertDialog(deleteDialogTitle,deleteDataFromTheList,NAV_DELETE_DIALOG_TAG);
    }


    public void confirmationClicked(String tag){
        if (tag==NAV_ADD_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mMainView.showToast("Adding confirmation "+ mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewUser(gitHubUser,mMainLogin, false);

        }
        else if (tag==NAV_DELETE_DIALOG_TAG){
            mNavUserList.remove(getUser(mLoginToDoSomeInDatabase));
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewUser(gitHubUser,mMainLogin, true);
            mMainView.showToast("Deleting confirmation "+ mLoginToDoSomeInDatabase);
        }
    }

    public GitHubUser getUser(String login){
        for (GitHubUser user: mNavUserList){
            if(login==user.getLogin())
               return user;
        }
        return null;
    }
}


