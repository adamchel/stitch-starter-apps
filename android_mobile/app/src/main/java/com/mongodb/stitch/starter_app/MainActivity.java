package com.mongodb.stitch.starter_app;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.starter_app.model.FriendList;
import com.mongodb.stitch.starter_app.model.objects.Friend;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FriendList _friendList;

    private Button btnLogonAnon;
    private Button btnLogonEmail;
    private Button btnLogout;
    private Button btnGetRemoteData;
    private Button btnCopyLocal;
    private Button btnCallFunc;
    private TextView tvLogin;
    private TextView tvRemoteData;
    private TextView tvLocalData;
    private TextView tvFuncResult;
    private ConstraintLayout loggedInView;

    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _friendList = new FriendList(this);

        btnLogonAnon = findViewById(R.id.btnAnon);
        btnLogonEmail = findViewById(R.id.btnEmail);
        btnLogout = findViewById(R.id.btnLogout);
        tvLogin = findViewById(R.id.login_text);
        loggedInView = findViewById(R.id.loggedInView);
        btnGetRemoteData = findViewById(R.id.getRemoteData);
        btnCopyLocal = findViewById(R.id.btnCopyLocal);
        tvRemoteData = findViewById(R.id.remoteData);
        tvLocalData = findViewById(R.id.localData);
        btnCallFunc = findViewById(R.id.btnCallFunc);
        tvFuncResult = findViewById(R.id.tvFuncResult);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);

        //Check if we're already logged in; if so, set up UI
        if (_friendList.isLoggedIn()) {
            SetIsLoggedInScreen();
        }
        btnLogonAnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.displayToastIfTaskFails(
                        MainActivity.this,
                        _friendList.loginAnonymously(),
                        "Failed to login."
                ).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) SetIsLoggedInScreen();
                    }
                });
            }
        });

        btnLogonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etEmail = findViewById(R.id.tvEmail);
                EditText etPassword = findViewById(R.id.tvPassword);

                Utils.displayToastIfTaskFails(
                        MainActivity.this,
                        _friendList.loginEmail(etEmail.getText().toString(), etPassword.getText().toString()),
                        "Failed to login."
                ).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) SetIsLoggedInScreen();
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     _friendList.logout();
                     SetLoginScreen();
                 }
            }
        );

        btnGetRemoteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.displayToastIfTaskFails(
                        MainActivity.this,
                        _friendList.getRemoteData(),
                        "Error getting remote data."
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            tvRemoteData.setText(formatFriends(_friendList.getCachedRemoteItems()));
                    }
                });
            }
        });

        btnCopyLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCopyLocal.getText() == "Delete Local Data"){
                    Utils.displayToastIfTaskFails(
                            MainActivity.this,
                            _friendList.deleteLocal(),
                            "Error deleting local data."
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                tvLocalData.setText(formatFriends(_friendList.getCachedLocalItems()));
                                btnCopyLocal.setText("Copy to Local");
                            }
                        }
                    });
                } else {
                    Utils.displayToastIfTaskFails(
                            MainActivity.this,
                            _friendList.copyToLocal(),
                            "Error copying remote data to local DB."
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                tvLocalData.setText(formatFriends(_friendList.getCachedLocalItems()));
                                btnCopyLocal.setText("Delete Local Data");
                            }

                        }
                    });
                }
            }
        });

        btnCallFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.displayToastIfTaskFails(
                        MainActivity.this,
                        _friendList.callFunction("SayHello"),
                        "Failed to call the function \"SayHello\"."
                ).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    tvFuncResult.setText(task.getResult().toString());
                                }
                            }
                        });
            }
        });
    }

    public void SetLoginScreen() {
        tvLogin.setText("You are not logged in. Log in with:");
        btnLogout.setVisibility(View.INVISIBLE);
        loggedInView.setVisibility(View.INVISIBLE);
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        ll3.setVisibility(View.VISIBLE);
        tvRemoteData.setText("---");
        tvFuncResult.setText("");
    }

    public void SetIsLoggedInScreen() {
        ll1.setVisibility(View.INVISIBLE);
        ll2.setVisibility(View.INVISIBLE);
        ll3.setVisibility(View.INVISIBLE);
        loggedInView.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);

        StitchUser currentUser = _friendList.getStitchClientInfo();
        String logonText = "You are logged in with the " + currentUser.getLoggedInProviderName() + " provider.";
        if (currentUser.getLoggedInProviderName().contains("userpass")) {
            logonText += "\nYou are user " + currentUser.getProfile().getEmail();
        }
        tvLogin.setText(logonText);
    }

    public String formatFriends(List<Friend> friends) {
        StringBuilder result = new StringBuilder();
        for (Friend f : friends) {
            result.append(f.getFriendName() + ", added on " + f.getDateAdded() + "\n");
        }
        return result.toString();
    }

}
