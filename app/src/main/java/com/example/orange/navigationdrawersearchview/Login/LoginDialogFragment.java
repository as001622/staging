package com.example.orange.navigationdrawersearchview.Login;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orange.navigationdrawersearchview.MainView;
import com.example.orange.navigationdrawersearchview.R;
import com.example.orange.navigationdrawersearchview.SearchPresenterImpl;

public class LoginDialogFragment extends DialogFragment implements LoginDialogView {


    private LayoutInflater mLayoutInflater;
    private AlertDialog.Builder builder;
    private View mView;


    private String retrofitTag="check";
    private Boolean mResponseSent=false;
    private LoginDialogPresenterImpl mLoginDialogPresenterImpl;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        Log.v(retrofitTag,"OnCreateDialog");
        mLoginDialogPresenterImpl =new LoginDialogPresenterImpl(this);

        mView = mLayoutInflater.inflate(R.layout.dialogfragment_login, null);

        builder.setMessage(R.string.dialog_title)
                .setView(mLayoutInflater.inflate(R.layout.dialogfragment_login,null))
                .setPositiveButton(R.string.dialog_confirm_button,null)
                .setNegativeButton(R.string.dialog_cancel_button,null);
        setCancelable(false);

        return builder.create();

    }


    @Override
    public void onResume() {
        super.onResume();
        setOnButtonClickListeners();
    }


    public void setResponseSent(Boolean responseSent) {
        mResponseSent = responseSent;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("check","Dismiss dialog");
    }
    public void setOnButtonClickListeners() {

        AlertDialog alertDialog = (AlertDialog) getDialog();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mResponseSent) {
                    mResponseSent=true;

                    Dialog dialogView = getDialog();

                    EditText userTextView = (EditText) dialogView.findViewById(R.id.username);
                    EditText passwordTextView = (EditText) dialogView.findViewById(R.id.password);

                    String username = userTextView.getText().toString();
                    String password = passwordTextView.getText().toString();
                    mLoginDialogPresenterImpl.loginPositiveButtonClicked(username, password);
                }

            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialogPresenterImpl.loginNegativeButtonClicked();
            }
        });

    }

    public void showToast(String text){
        Toast.makeText(getContext(),
                text,
                Toast.LENGTH_SHORT).show();

    }

    public void loginDialogClose(String login, String avatarUrl){
        MainView.LoginDialogClosed listener = (MainView.LoginDialogClosed) getActivity();
        listener.onLoginDialogClosed(login, avatarUrl);
        dismiss();
    }

    

}

