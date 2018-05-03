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
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements Constants,
                                                               MainView,
                                                               NavigationView.OnNavigationItemSelectedListener,
                                                               MainView.LoginDialogClosed{



    private DrawerLayout mDrawerLayout;
    private MainPresenterImpl mMainPresenterImpl;
    private RecyclerView mNavRecyclerView;
    private NavigationView mNavigationView;
    private SearchView mNavSearchView;
    private LoginDialogFragment mLoginDialogFragment;
    private ImageView mNavigationViewImageView;
    private TextView mNavigationViewTextView;
    private String mLogin;
    private String mPassword;
    private String mAvatarUrl;
    private String mNavSearchText;
    private String mMainSearchText;
    private int mNavRecyclerViewScrollPosition;
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

        mNavRecyclerViewScrollPosition=0;
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
        mMainPresenterImpl = new MainPresenterImpl(this, realm);

        if (savedInstanceState==null) {
            createLoginDialog();
        }
        else{
            if (getSupportFragmentManager().findFragmentByTag(getText(R.string.dialog_tag).toString())==null)
                onLoginDialogClosed(mLogin,mPassword, mAvatarUrl);
        }

    }

    public void setMainSearchView() {
        mMainSearchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mMainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideKeyboard();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mMainSearchText=newText;
                mMainPresenterImpl.mainSearchViewDataChanged(newText);
                return false;
            }
        });
    }

    public void setNavSearchView() {
        if (mNavSearchView==null){
            SearchView searchView = mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_search);
            if (searchView != null) {
                mNavSearchView = searchView;
                if (mNavSearchText!=null)
                    if(!mNavSearchText.isEmpty()) {
                        mNavSearchView.setQuery(mNavSearchText, false);
                        mNavSearchView.setIconified(false);
                        mNavSearchView.clearFocus();
                        mMainPresenterImpl.restoreData(mLogin);
                    }
                    else{
                        mNavSearchView.setQuery(null,true);
                        mNavSearchView.setIconified(true);
                        mNavSearchView.clearFocus();
                    }
                mNavSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        hideKeyboard();
                        mMainPresenterImpl.navSearchViewDataChanged(query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mNavSearchText = newText;
                        mNavRecyclerViewScrollPosition=0;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMainPresenterImpl.navSearchViewDataChanged(mNavSearchText);
                            }
                        },searchDelay);
                        return false;
                    }
                });
            }
        }
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
        showToast("nav click");
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
        mMainSearchView =
                (SearchView) searchItem.getActionView();
        //need this to hide keyboard on roatation and clear focus on this SearchView
        if (mMainSearchText!=null)
            if(!mMainSearchText.isEmpty()){
                mMainSearchView.setQuery(mMainSearchText, false);
                mMainSearchView.setIconified(false);
            if (mDrawerLayout.isDrawerOpen(mNavigationView))
                mMainSearchView.clearFocus();
        }
        setMainSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        setNavigationViewItems();

        switch (item.getItemId()){
            case R.id.nav_logout:
                mMainPresenterImpl.navLogoutPressed();
                return true;
            case R.id.nav_login:
                mMainPresenterImpl.navLoginPressed();
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
        outState.putString(PASSWORD_TAG,mPassword);

        //saving navRecyclerViewScrollPosition
        int scrollPositionIng;
        if (mNavRecyclerView!=null) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)mNavRecyclerView.getLayoutManager();
            if (linearLayoutManager!=null) {
                scrollPositionIng = linearLayoutManager.findFirstVisibleItemPosition();
                mNavRecyclerViewScrollPosition = scrollPositionIng;
                mMainPresenterImpl.saveData(mLogin);
            }
            outState.putInt(NAVIGATION_RECYCLERVIEW_POSITION, mNavRecyclerViewScrollPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPassword=savedInstanceState.getString(mPassword);
        mLogin=savedInstanceState.getString(LOGIN_TAG);
        mAvatarUrl=savedInstanceState.getString(AVATAR_TAG);
        mNavSearchText=savedInstanceState.getString(NAVIGATION_SEARCH_TAG);
        mMainSearchText=savedInstanceState.getString(MAIN_SEARCH_TAG);
        mNavRecyclerViewScrollPosition=savedInstanceState.getInt(NAVIGATION_RECYCLERVIEW_POSITION);
        onLoginDialogClosed(mLogin,mPassword,mAvatarUrl);
        if (mMainSearchText!=null){
            if (!mMainSearchText.isEmpty()) {
                mMainPresenterImpl.mainSearchViewDataChanged(mMainSearchText);
            }
            else {
                mMainSearchText = null;
                onLoginDialogClosed(mLogin,mPassword,mAvatarUrl);
            }
        }
    }

    @Override
    public void setNavRecyclerViewAdapter(BaseAdapter adapter) {
        Parcelable recyclerViewState=null;
        LinearLayoutManager linearLayoutManager;
        if(mNavRecyclerView==null) {
            linearLayoutManager = new LinearLayoutManager(this);
        }
        else linearLayoutManager=(LinearLayoutManager)mNavRecyclerView.getLayoutManager();

        mNavRecyclerView =mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_recyclerview);
        mNavRecyclerView.setLayoutManager(linearLayoutManager);
        if (adapter==null) {
            mNavRecyclerViewScrollPosition=0;
            mNavRecyclerView.setVisibility(View.GONE);
            return;
        }
        recyclerViewState = linearLayoutManager.onSaveInstanceState();//save
        mNavRecyclerView.setAdapter(adapter);
        if (recyclerViewState!=null)
            linearLayoutManager.onRestoreInstanceState(recyclerViewState);//restore
        if(mNavRecyclerViewScrollPosition!=0)
            mNavRecyclerView.scrollToPosition(mNavRecyclerViewScrollPosition);
        mNavRecyclerView.setVisibility(View.VISIBLE);
        mNavRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                
                if (!recyclerView.canScrollVertically(1)) {
                    mMainPresenterImpl.needMoreData();
                    showToast("fetching more data");
                }
            }
        });
    }

    public void setMainRecyclerViewAdapter (BaseAdapter adapter) {
        mMainRecyclerView = findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainRecyclerView.setAdapter(adapter);
        mMainRecyclerView.setVisibility(View.VISIBLE);
    }

   public void setNavRecyclerViewPosition(){
        SearchView navSearch=mNavigationView.getHeaderView(headerStartIndex).findViewById(R.id.nav_search);
        float y = navSearch.getY()+navSearch.getHeight();
        mNavRecyclerView.setY(y+recyclerViewOffsetY);
    }

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
        loginItem.setVisible(true);
        logoutItem.setVisible(false);
        mAvatarUrl=null;
        mPassword=null;
        mLogin=null;
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
                mMainPresenterImpl.confirmationClicked(tag);
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
    public void onLoginDialogClosed(String login, String password,String avatarUrl) {

            mLogin = login;
            mPassword = password;
            mAvatarUrl = avatarUrl;
            mMainPresenterImpl.activityStarted(login, password, avatarUrl);

    }

    public void gitHubUserActivityStart (String login){
        Intent intent = new Intent(this, GitHubUserDetailsActivity.class);
        intent.putExtra(GITHUBUSER_LOGIN,login);
        startActivity(intent);

    }
}
