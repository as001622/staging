package com.example.orange.navigationdrawersearchview;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.orange.navigationdrawersearchview.Login.LoginDialogFragment;
import com.example.orange.navigationdrawersearchview.NavRecyclerViewPackage.NavigationAdapter;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements MainView,
                                                               NavigationView.OnNavigationItemSelectedListener,
                                                               MainView.LoginDialogClosed{


    final public static String QUERY_HINT="Search new Users!!!!!";
    final public static String NAVIGATION_SEARCH_TAG ="Navigation search";
    final public static String MAIN_SEARCH_TAG="Main search";
    final public static String NAVIGATION_SCROLL_STATE="NAVIGATION RECYCLERVIEW SCROLL STATE";
    final public static String MAIN_RECYCLERVIEW_POSITION="MAIN_RECYCLERVIEW_POSITION";
    final public static String NAVIGATION_RECYCLERVIEW_POSITION="NAVIGATION_RECYCLERVIEW_POSITION";

    private DrawerLayout mDrawerLayout;
    private SearchPresenterImpl mSearchPresenterImpl;
    private RecyclerView mNavRecyclerView;
    private NavigationView mNavigationView;
    private SearchView mNavSearchView;
    private LoginDialogFragment mLoginDialogFragment;
    private ImageView mNavigationViewImageView;
    private TextView mNavigationViewTextView;
    private String mLogin;
    private String mAvatarUrl;
    private String mNavSearchText;
    private String mMainSearchText;
    private int mNavRecyclerViewScrollPosition;
    private int FLAG;
    private SearchView mMainSearchView;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private RecyclerView mMainRecyclerView;
    private MenuItem logoutItem;
    private MenuItem loginItem;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavRecyclerViewScrollPosition=0;
        FLAG =0;
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

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
        mSearchPresenterImpl = new SearchPresenterImpl(this, realm);

        if (savedInstanceState==null) {
            createLoginDialog();
        }
        else{

            if (getSupportFragmentManager().findFragmentByTag(getText(R.string.dialog_tag).toString())==null)
                onLoginDialogClosed(mLogin, mAvatarUrl);
        }

    }

    public void setMainSearchViewListeners() {
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
                mSearchPresenterImpl.mainSearchViewDataChanged(newText);
                return false;
            }
        });
    }

    public void setNavSearchView() {
        if (mNavSearchView==null){
            SearchView searchView = mNavigationView.getHeaderView(0).findViewById(R.id.nav_search);
           // if (mNavSearchText!=null)
            mSearchPresenterImpl.navSearchViewDataChanged(mNavSearchText);
            if (searchView != null) {
                mNavSearchView = searchView;
                mNavSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        hideKeyboard();
                        mSearchPresenterImpl.navSearchViewDataChanged(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mNavSearchText = newText;
                        mSearchPresenterImpl.navSearchViewDataChanged(mNavSearchText);
                        return false;
                    }
                });
            }
        }
        if ((mNavSearchText!=null)&(mNavSearchText!="")) {
            mNavSearchView.setQuery(mNavSearchText, false);
            mNavSearchView.setIconified(false);
            mNavSearchView.clearFocus();
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
        if ((mMainSearchText!=null)&(mMainSearchText!="")) {
            mMainSearchView.setQuery(mMainSearchText, false);
            mMainSearchView.setIconified(false);
            if (mDrawerLayout.isDrawerOpen(mNavigationView))
                mMainSearchView.clearFocus();
        }
        setMainSearchViewListeners();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        setNavigationViewItems();

        switch (item.getItemId()){
            case R.id.nav_logout:
                mSearchPresenterImpl.navLogoutPressed();
                return true;
            case R.id.nav_login:
                mSearchPresenterImpl.navLoginPressed();
                return true;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Login",mLogin);
        outState.putString("Avatar",mAvatarUrl);
        outState.putString(NAVIGATION_SEARCH_TAG, mNavSearchText);
        outState.putString(MAIN_SEARCH_TAG,mMainSearchText);
        int scrollPositionIng;
        if (mNavRecyclerView!=null) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)mNavRecyclerView.getLayoutManager();
            scrollPositionIng =  linearLayoutManager.findFirstVisibleItemPosition();
            mNavRecyclerViewScrollPosition = scrollPositionIng;
            outState.putInt(NAVIGATION_RECYCLERVIEW_POSITION, mNavRecyclerViewScrollPosition);
        }

        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLogin=savedInstanceState.getString("Login");
        mAvatarUrl=savedInstanceState.getString("Avatar");
        mNavSearchText=savedInstanceState.getString(NAVIGATION_SEARCH_TAG);
        mMainSearchText=savedInstanceState.getString(MAIN_SEARCH_TAG);
        mNavRecyclerViewScrollPosition=savedInstanceState.getInt(NAVIGATION_RECYCLERVIEW_POSITION);

        if (mNavSearchText!=null) {
            mSearchPresenterImpl.navSearchViewDataChanged(mNavSearchText);
        }
        if (mMainSearchText!=null){
            if (mMainSearchText!="") {
                mSearchPresenterImpl.mainSearchViewDataChanged(mMainSearchText);
            }
            else {
                mMainSearchText = null;
                onLoginDialogClosed(mLogin,mAvatarUrl);
            }
        }

    }

    @Override
    public void setNavRecyclerViewAdapter(NavigationAdapter adapter) {
        mNavRecyclerView =findViewById(R.id.nav_recyclerview);
        mNavRecyclerView.setBackgroundColor(0x303F9F);
        if (adapter==null) {
            showToast("ByebyeNavRecycler");
            mNavRecyclerView.setVisibility(View.GONE);
            return;
        }
        setNavRecyclerViewPosition();
        mNavRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNavRecyclerView.setAdapter(adapter);
        mNavRecyclerView.scrollToPosition(mNavRecyclerViewScrollPosition);
        mNavRecyclerView.setVisibility(View.VISIBLE);
    }

    public void setMainRecyclerViewAdapter (NavigationAdapter adapter) {
        mMainRecyclerView = findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainRecyclerView.setAdapter(adapter);
        mMainRecyclerView.setVisibility(View.VISIBLE);
    }

   public void setNavRecyclerViewPosition(){
        SearchView navSearch=mNavigationView.getHeaderView(0).findViewById(R.id.nav_search);
        float y = navSearch.getY()+navSearch.getHeight();
        mNavRecyclerView.setY(y+10);
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
        mLogin=null;
        mDrawerLayout.closeDrawers();
        mNavigationViewImageView.setImageResource(R.drawable.user);
        mNavigationViewTextView.setText("Guest");
    }

    private void setNavigationViewItems(){
        logoutItem = (MenuItem)mNavigationView.getMenu().findItem(R.id.nav_logout);
        loginItem = (MenuItem)mNavigationView.getMenu().findItem(R.id.nav_login);
        mNavigationViewImageView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_image);
        mNavigationViewTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_text);
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
        boolean result;
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mSearchPresenterImpl.confirmationClicked(tag);
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
        mLogin=login;
        mAvatarUrl=avatarUrl;
        mSearchPresenterImpl.activityStarted(login,avatarUrl);
    }
}
