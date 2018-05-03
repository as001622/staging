package com.example.orange.navigationdrawersearchview.Presenter;

public interface Constants {

    public static final String QUERY_HINT="Search new Users!!!!!";
    public static final String NAVIGATION_SEARCH_TAG ="Navigation search";
    public static final String MAIN_SEARCH_TAG="Main search";
    public static final String LOGIN_TAG ="Login";
    public static final String AVATAR_TAG="Avatar";
    public static final String PASSWORD_TAG="Password";
    public static final String NAVIGATION_SCROLL_STATE="NAVIGATION RECYCLERVIEW SCROLL STATE";
    public static final String MAIN_RECYCLERVIEW_POSITION="MAIN_RECYCLERVIEW_POSITION";
    public static final String NAVIGATION_RECYCLERVIEW_POSITION="NAVIGATION_RECYCLERVIEW_POSITION";
    public static final String addNewDataToDatabase="Do you want to add this Github user to database?";
    public static final String deleteDataFromTheList="Do you want to delete this Github user from this list forever?";
    public static final String collisionDialogTitle ="Are you sure?";
    public static final String collsionDialogMessage ="This user is in your main list! Do you want to delete user from both lists anyway???";
    public static final String addDialogTitle="Adding";
    public static final String deleteDialogTitle="Deleting";
    public static final String NAV_ADD_DIALOG_TAG="NAV_ADD_DIALOG_TAG";
    public static final String NAV_DELETE_DIALOG_TAG="NAV_DELETE_DIALOG_TAG";
    public static final String mainDialogMessage="Do you want to delete this Github user from this list?";
    public static final String MAIN_DELETE_DIALOG_TAG="MAIN_DELETE_DIALOG_TAG";
    public static final String COLLISION_DIALOG_TAG="COLLISION DIALOG TAG";
    public static final String NAV_RECYCLERVIEW_TAG="NAV_RECYCLERVIEW_TAG";
    public static final String MAIN_RECYCLERVIEW_TAG="MAIN_RECYCLERVIEW_TAG";
    public static final String USER_LOGIN="USER LOGIN";
    public static final String GUEST_LOGIN="GUESTGUESTGUESTGUESTGUEST";
    public static final String GITHUBUSER_LOGIN="GITHUBUSERLOGIN";
    public static final String GUEST_TEXT="GUEST";
    public static final String EMPTY_STRING="";
    public static final String RESPONSE_UNSUCCESFULL="Response is unsuccesfull: ";
    public static final String RESPONSE_FAILURE="Response failure: ";
    public static final String ADDING_USER_TEXT="Adding confirmation ";
    public static final String DELETING_USER_TEXT="Deleting confirmation ";
    public static final String TOTAL_COUNT_MESSAGE=" results found for query ";
    public static final int searchDelay=600;
    public static final int headerStartIndex=0;
    public static final int recyclerViewOffsetY=10;
    public static final int minimumNavTextSearchSize=1;
    public static final int minimumMainTextSearchSize=0;


    //DialogConstants
    public final String DIALOG_USERNAME_WARNING="Enter login!!!";
    public final String DIALOG_PASSWORD_WARNING ="Enter password!!!!";
    public final String DIALOG_WRONG="Check your username or password!!!";
    public final String DIALOG_FAILURE="Login failure!!!";
    public final String DIALOG_LOGIN_SUCCESFULL="You have logged in!!!";

    //Api Constants
    public static final String BASE_URL = "https://api.github.com/";
    public static final String URL_START="search/users?q=";
    public static final String URL_FINISH="+in:login";
    public static final String HEADER_STATUS_FIELD="Status";
    public static final String STATUS_FIELD_CONTAINS_403ERROR="403";
    public static final String ON403_MESSAGE="Forbidden! Rate limit exceeded!!!";
    public static final Integer loadPerPage=20;
    public static final Integer firstPage=1;
    public static final String LOAD_MORE_DATA_TAG="LOAD_MORE_DATA_TAG";
    public static final String LOAD_DATA_FIRST_TIME_TAG="LOAD_DATA_FIRST_TIME_TAG";

}
