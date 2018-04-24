package com.example.orange.navigationdrawersearchview;

import com.example.orange.navigationdrawersearchview.Database.DatabaseInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractorImpl;
import com.example.orange.navigationdrawersearchview.GitHubApi.ApiInteractor;
import com.example.orange.navigationdrawersearchview.Model.GitHubUser;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavRecyclerViewListener;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavigationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

public class SearchPresenterImpl implements Presenter,
                                        ApiInteractor.OnLoadDataFinished,
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
    private final String deleteDataFromTheList="Do you want to delete this Github user from this list forever?";
    private final String collisionDialogTitle ="Are you sure?";
    private final String collsionDialogMessage ="This user is in your main list! Do you want to delete user from both lists anyway???";
    private final String addDialogTitle="Adding";
    private final String deleteDialogTitle="Deleting";
    private final String NAV_ADD_DIALOG_TAG="NAV_ADD_DIALOG_TAG";
    private final String NAV_DELETE_DIALOG_TAG="NAV_DELETE_DIALOG_TAG";
    private final String mainDialogMessage="Do you want to delete this Github user from this list?";
    private final String MAIN_DELETE_DIALOG_TAG="MAIN_DELETE_DIALOG_TAG";
    private final String COLLISION_DIALOG_TAG="COLLISION DIALOG TAG";
    public static final String NAV_RECYCLERVIEW_TAG="NAV_RECYCLERVIEW_TAG";
    public static final String MAIN_RECYCLERVIEW_TAG="MAIN_RECYCLERVIEW_TAG";
    public final String USER_LOGIN="USER LOGIN";
    public final String GUEST_LOGIN="GUESTGUESTGUESTGUESTGUEST";



    public SearchPresenterImpl(MainView mainView,Realm realm) {
        mApiInteractorImpl = new ApiInteractorImpl();
        mDatabaseInteractorImpl=new DatabaseInteractorImpl(realm);
        mMainView = mainView;
        mRealm=realm;
    }

    public void navSearchViewDataChanged(String text) {
        if (text==null)
        {
            mMainView.setNavRecyclerViewAdapter(null);
        }
        else if (text.length()>1) {
            mApiInteractorImpl.loadData(text, this);
        }
        else {
            mMainView.setNavRecyclerViewAdapter(null);
        }
    }

    public void mainSearchViewDataChanged(String query) {
        if (query.length()>0) {
            mMainUserList=mDatabaseInteractorImpl.getListForMainSearchView(query);
            mMainAdapter = new NavigationAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
            mMainView.setMainRecyclerViewAdapter(mMainAdapter);
        }
        else setMainAdapter(mMainLogin);
    }

    @Override
    public void onLoadDataResponseIsSuccesfull(List<GitHubUser> userList) {
        mMainView.showToast("Data loaded");
        mNavUserList =userList;
        setNavAdapter();
    }

    @Override
    public void onLoadDataResponseIsNotSuccesfull(String message) {
        mMainView.showToast("Response is unsuccesfull:" + message);

    }

    @Override
    public void onLoadDataFailure(String message) throws JSONException {
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
            //mMainView.showToast(GUEST_LOGIN);
            setMainAdapter(mMainLogin);
        }
        else{
            mMainLogin=login;
            //mMainView.showToast(login+login+login);
            mMainView.setUser(login, avatarArl);
            setMainAdapter(mMainLogin);
        }
    }
    private void setMainAdapter(String mainLogin){
        mMainUserList=mDatabaseInteractorImpl.getListForMainRecyclerView(mainLogin);
        mMainAdapter = new NavigationAdapter(mMainUserList, this,MAIN_RECYCLERVIEW_TAG);
        mMainView.setMainRecyclerViewAdapter(mMainAdapter);
    }

    private void setNavAdapter(){
        mNavUserList=mDatabaseInteractorImpl.getListForNavRecyclerView(mMainLogin, mNavUserList);
        mNavigationAdapter = new NavigationAdapter(mNavUserList, this,NAV_RECYCLERVIEW_TAG);
        mMainView.showToast(String.valueOf(mNavigationAdapter.getItemCount()));
        mMainView.setNavRecyclerViewAdapter(mNavigationAdapter);
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
       // mMainView.showToast("Deleting");
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
            mMainView.showToast("Adding confirmation "+ mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewUser(gitHubUser,mMainLogin);
            setMainAdapter(mMainLogin);

        }
        else if (tag==NAV_DELETE_DIALOG_TAG){
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            if (!mDatabaseInteractorImpl.getListForMainRecyclerView(mMainLogin).contains(gitHubUser))
            {
                mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser, mMainLogin);
                setNavAdapter();
                setMainAdapter(mMainLogin);
                mMainView.showToast("Deleting confirmation " + mLoginToDoSomeInDatabase);
            }
            else{
                mMainView.showAlertDialog(collisionDialogTitle, collsionDialogMessage, COLLISION_DIALOG_TAG);
            }
        }
        else if (tag==COLLISION_DIALOG_TAG)
        {
            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.insertNewDeletedUser(gitHubUser,mMainLogin);
            setNavAdapter();
            setMainAdapter(mMainLogin);

        }
        else if (tag==MAIN_RECYCLERVIEW_TAG)
        {

            GitHubUser gitHubUser= getUser(mLoginToDoSomeInDatabase);
            mDatabaseInteractorImpl.deleteUser(gitHubUser);
            setMainAdapter(mMainLogin);
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


