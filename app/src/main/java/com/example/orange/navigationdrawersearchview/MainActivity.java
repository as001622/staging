package com.example.orange.navigationdrawersearchview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orange.navigationdrawersearchview.DetailsActivity.GitHubUserDetailsActivity;
import com.example.orange.navigationdrawersearchview.Login.LoginDialogFragment;
import com.example.orange.navigationdrawersearchview.NavRecyclerView.BaseAdapter;
import com.example.orange.navigationdrawersearchview.Presenter.Constants;
import com.example.orange.navigationdrawersearchview.Presenter.MainPresenterImpl;
import com.example.orange.navigationdrawersearchview.Presenter.Presenter;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements Constants,
                                                               MainView,
                                                               NavigationView.OnNavigationItemSelectedListener,
                                                               MainView.LoginDialogClosed{



    private DrawerLayout mDrawerLayout;
    private Presenter mMainPresenter;
    private RecyclerView mNavRecyclerView;
    private NavigationView mNavigationView;
    private SearchView mNavSearchView;
    private LoginDialogFragment mLoginDialogFragment;
    private ImageView mNavigationViewImageView;
    private TextView mNavigationViewTextView;
    private boolean mLogged=false;
    private String mLogin;
    private String mAvatarUrl;
    private String mNavSearchText;
    private String mMainSearchText;
    private SearchView mMainSearchView;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private RecyclerView mMainRecyclerView;
    private MenuItem logoutItem;
    private MenuItem loginItem;
    private Handler mHandler;
    private Parcelable mNavRecyclerViewState;
    private Parcelable mMainRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mHandler = new Handler();
        
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(config);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        mMainPresenter = new MainPresenterImpl(this, realm);

        if (savedInstanceState==null) {
            createLoginDialog();
        }
        else{
            if (getSupportFragmentManager().findFragmentByTag(getText(R.string.dialog_tag).toString())==null)
                onLoginDialogClosed(mLogin, mAvatarUrl);
        }

    }


    private void  setMainSearchViewText(String text){
        if (text != null)
            if (!text.isEmpty()) {
                mMainSearchView.setQuery(mMainSearchText, false);
                mMainSearchView.setIconified(false);
                mMainPresenter.mainSearchViewDataChanged(mMainSearchText);
                if (mDrawerLayout.isDrawerOpen(mNavigationView))
                    mMainSearchView.clearFocus();
                Log.v("newUserLoggedIn","maintextisalive");
            }
        if(text==null){
            mMainSearchView.setQuery("", false);
            mMainSearchView.setIconified(true);
            mMainSearchView.clearFocus();
            Log.v("newUserLoggedIn","maintextempty");
        }
    }

    public void setMainSearchView() {
       if (mMainSearchView!=null) {
           Log.v("newUserLoggedIn","mainsearchviewsetting");
           hideKeyboard();
           mMainSearchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
           setMainSearchViewText(mMainSearchText);
           mMainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   hideKeyboard();
                   return false;
               }

               @Override
               public boolean onQueryTextChange(String newText) {
                   mMainSearchText = newText;
                   mMainPresenter.mainSearchViewDataChanged(newText);
                   return false;
               }
           });
       }
    }

    public void setNavSearchViewText(String text){
        if (text!=null)
            if(!text.isEmpty()) {
                mNavSearchView.setQuery(text, false);
                mNavSearchView.setIconified(false);
                mNavSearchView.clearFocus();
                Log.v("newUserLoggedIn","navtextalive "+mNavSearchText);
            }
         if(text==null){
                Log.v("newUserLoggedIn","navtextempty");
                mNavSearchView.setQuery("",false);
                mNavSearchView.setIconified(true);
                mNavSearchView.clearFocus();
            }
    }


    public void setNavSearchView() {
        if (mNavSearchView==null){
            SearchView searchView = mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_search);
            if (searchView != null) {
                mNavSearchView = searchView;
                mNavSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        hideKeyboard();
                        mNavSearchText=query;
                        mMainPresenter.navSearchViewDataChanged(mNavSearchText);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mNavSearchText = newText;
                        mNavRecyclerView=null;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMainPresenter.navSearchViewDataChanged(mNavSearchText);
                            }
                        },searchDelay);
                        return false;
                    }
                });
            }
        }
        if (mNavSearchText!=null)
        setNavSearchViewText(mNavSearchText);
    }

    public void hideKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setNavSearchView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                setNavSearchView();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        mMainSearchView = (SearchView) searchItem.getActionView();
        setMainSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        setNavigationViewItems();

        switch (item.getItemId()){
            case R.id.nav_logout:
                mMainPresenter.navLogoutPressed();
                return true;
            case R.id.nav_login:
                mMainPresenter.navLoginPressed();
                return true;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LOGIN_TAG,mLogin);
        outState.putString(AVATAR_TAG,mAvatarUrl);
        outState.putString(NAVIGATION_SEARCH_TAG, mNavSearchText);
        outState.putString(MAIN_SEARCH_TAG,mMainSearchText);
        outState.putBoolean(LOGGED_TAG,mLogged);
        if (mMainRecyclerView!=null) {
            LinearLayoutManager mainLinearLayout=(LinearLayoutManager) mMainRecyclerView.getLayoutManager();
            mMainRecyclerViewState=mainLinearLayout.onSaveInstanceState();
            outState.putParcelable(MAIN_RECYCLERVIEW_STATE_TAG,mMainRecyclerViewState);
        }
        if (mNavRecyclerView!=null) {
            LinearLayoutManager navLinearLayout=(LinearLayoutManager) mNavRecyclerView.getLayoutManager();
            mNavRecyclerViewState=navLinearLayout.onSaveInstanceState();
            outState.putParcelable(NAVIGATION_RECYCLERVIEW_POSITION,mNavRecyclerViewState);
        }
        if (mNavSearchText!=null)
            if(mNavSearchText.length()>minimumNavTextSearchSize)
                mMainPresenter.saveData();
       // outState.putInt(NAVIGATION_RECYCLERVIEW_POSITION, mNavRecyclerViewScrollPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLogin=savedInstanceState.getString(LOGIN_TAG);
        mAvatarUrl=savedInstanceState.getString(AVATAR_TAG);
        mNavSearchText=savedInstanceState.getString(NAVIGATION_SEARCH_TAG);
        mMainSearchText=savedInstanceState.getString(MAIN_SEARCH_TAG);
        mMainPresenter.setMainSearchViewText(mMainSearchText);
        //mNavRecyclerViewScrollPosition=savedInstanceState.getInt(NAVIGATION_RECYCLERVIEW_POSITION);
        mLogged=savedInstanceState.getBoolean(LOGGED_TAG);
        mMainRecyclerViewState=savedInstanceState.getParcelable(MAIN_RECYCLERVIEW_STATE_TAG);
        mNavRecyclerViewState=savedInstanceState.getParcelable(NAVIGATION_RECYCLERVIEW_POSITION);
        Log.v("onrestore",mNavSearchText+" "+mMainSearchText);
        if (mMainSearchText!=null){
            if (!mMainSearchText.isEmpty()) {
                mMainPresenter.mainSearchViewDataChanged(mMainSearchText);
            }
            else {
                mMainSearchText = null;
                mMainPresenter.setMainSearchViewText(mMainSearchText);
            }
        }
        mMainPresenter.setLoginForApplication(mLogin);
        onLoginDialogClosed(mLogin,mAvatarUrl);
        if (mNavSearchText!=null)
            if(mNavSearchText.length()>minimumNavTextSearchSize) {
                mMainPresenter.restoreData();
            }

    }

    @Override
    public void setNavRecyclerViewAdapter(BaseAdapter adapter) {
        LinearLayoutManager linearLayoutManager;
        if(mNavRecyclerView==null) {
            linearLayoutManager = new LinearLayoutManager(this);
            mNavRecyclerView =mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_recyclerview);
        }
        else{
            linearLayoutManager=(LinearLayoutManager)mNavRecyclerView.getLayoutManager();
            mNavRecyclerViewState=linearLayoutManager.onSaveInstanceState();
        }
        mNavRecyclerView.setLayoutManager(linearLayoutManager);

        if (adapter==null) {
            mNavRecyclerViewState=null;
            mNavRecyclerView.setVisibility(View.GONE);
            return;
        }

        mNavRecyclerView.setAdapter(adapter);
        if (mNavRecyclerViewState!=null)
            linearLayoutManager.onRestoreInstanceState(mNavRecyclerViewState);

        mNavRecyclerView.setVisibility(View.VISIBLE);
        mNavRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                
                if (!recyclerView.canScrollVertically(1)) {
                    mMainPresenter.setNavSearchQuery(mNavSearchText);
                    mMainPresenter.needMoreData();
                    showToast("fetching more data");
                }
            }
        });
    }

    public void setMainRecyclerViewAdapter (BaseAdapter adapter) {
        LinearLayoutManager linearLayoutManager;
        if (mMainRecyclerView==null) {
            linearLayoutManager= new LinearLayoutManager(this);
            mMainRecyclerView = findViewById(R.id.main_recyclerview);
            mMainRecyclerView.setLayoutManager(linearLayoutManager);
        }
        else{
            linearLayoutManager=(LinearLayoutManager) mMainRecyclerView.getLayoutManager();
            mMainRecyclerViewState=linearLayoutManager.onSaveInstanceState();
        }
        mMainRecyclerView.setAdapter(adapter);
        if (mMainRecyclerViewState!=null) {
            mMainRecyclerView.getLayoutManager().onRestoreInstanceState(mMainRecyclerViewState);
        }
        mMainRecyclerView.setVisibility(View.VISIBLE);
    }


 /*  public void setNavRecyclerViewPosition(){
        SearchView navSearch=mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_search);
        float y = navSearch.getY()+navSearch.getHeight();
        mNavRecyclerView.setY(y+recyclerViewOffsetY);
    }*/

    public void setUser(String login, String avatarUrl) {
        setNavigationViewItems();
        logoutItem.setVisible(true);
        loginItem.setVisible(false);
        mDrawerLayout.closeDrawers();
        Picasso.get().load(avatarUrl).into(mNavigationViewImageView);
        mNavigationViewTextView.setText(login);
    }

    public void setUserAsGuest(){
        setNavigationViewItems();
        logoutItem.setVisible(false);
        loginItem.setVisible(true);
        mAvatarUrl=null;
        mLogin=GUEST_LOGIN;
        mDrawerLayout.closeDrawers();
        mNavigationViewImageView.setImageResource(R.drawable.githublogo);
        mNavigationViewTextView.setText(GUEST_TEXT);
    }

    private void setNavigationViewItems(){
        logoutItem = (MenuItem)mNavigationView.getMenu().findItem(R.id.nav_logout);
        loginItem = (MenuItem)mNavigationView.getMenu().findItem(R.id.nav_login);
        mNavigationViewImageView = (ImageView) mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_image);
        mNavigationViewTextView = (TextView) mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_header_text);
    }

    @Override
    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void createLoginDialog(){
        mLoginDialogFragment = new LoginDialogFragment();
        mLoginDialogFragment.show(getSupportFragmentManager(),
                getText(R.string.dialog_tag).toString());
    }

    public void showAlertDialog(String title,String message,final String tag){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mMainPresenter.confirmationClicked(tag);
            }
        });
        builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onLoginDialogClosed(String login, String avatarUrl) {
        mLogged =true;
        mLogin = login;
        mAvatarUrl = avatarUrl;
        mMainPresenter.activityStarted(login, avatarUrl);
    }

    public void gitHubUserActivityStart (String login){
        Intent intent = new Intent(this, GitHubUserDetailsActivity.class);
        intent.putExtra(GITHUBUSER_LOGIN,login);
        startActivity(intent);
    }

    public void newUserLoggedIn(){
        mMainSearchText=null;
        mMainPresenter.setMainSearchViewText(mMainSearchText);
        mNavSearchText=null;
        setNavSearchView();
        setMainSearchView();
        setNavRecyclerViewAdapter(null);
    }
}
