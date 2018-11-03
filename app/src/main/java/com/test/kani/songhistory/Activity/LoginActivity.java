package com.test.kani.songhistory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.utility.FireStoreCallbackListener;
import com.test.kani.songhistory.utility.FireStoreConnectionPool;
import com.test.kani.songhistory.utility.LoadingDialog;
import com.test.kani.songhistory.utility.SharedPreferencesInstance;

import java.util.Map;

public class LoginActivity extends AppCompatActivity
{
    EditText idEditText, passwordEditText;
    CheckBox autoLoginCheckBox;
    Button signUpBtn, signInBtn;

    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.init();
        this.bindUI();
    }

    private void init()
    {
        SharedPreferencesInstance.getInstance().setSharedPreferencesContext(this, Context.MODE_PRIVATE);
        String id = (String) SharedPreferencesInstance.getInstance().get("id",
                SharedPreferencesInstance.Type.String);

        if( id != null )
        {
            MainActivity.id = id;
            MainActivity.admin = (boolean) SharedPreferencesInstance.getInstance().get("admin",
                    SharedPreferencesInstance.Type.Boolean);
            login(true);
            return;
        }

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int ID_NOT_EXISTED = 0;
            final int PASSWORD_NOT_MATCHED = 1;
            final int ID_SHORT = 2;
            final int PASSWORD_SHORT = 3;
            final int TASK_FAILURE = 4;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case ID_NOT_EXISTED:
                        Log.d("LoginActivity", "This ID is not existed");
                        idEditText.setError("This ID is not existed");
                        idEditText.selectAll();
                        idEditText.requestFocus();
//                        Toast.makeText(getApplicationContext(), "ID is not existed", Toast.LENGTH_SHORT).show();
                        break;
                    case PASSWORD_NOT_MATCHED:
                        Log.d("LoginActivity", "Password is not matched");
                        passwordEditText.setError("Password is not matched");
                        passwordEditText.selectAll();
                        passwordEditText.requestFocus();
//                        Toast.makeText(getApplicationContext(), "Password is not matched", Toast.LENGTH_SHORT).show();
                        break;
                    case ID_SHORT:
                        Log.d("RegistActivity", "This ID is short");
//                        Toast.makeText(getApplicationContext(), "This ID is already existed.", Toast.LENGTH_SHORT).show();
                        idEditText.setError("This ID is too short. You must input at least 5 lengths.");
                        idEditText.selectAll();
                        idEditText.requestFocus();
                        break;
                    case PASSWORD_SHORT:
                        Log.d("RegistActivity", "This Password is short");
//                        Toast.makeText(getApplicationContext(), "This ID is already existed.", Toast.LENGTH_SHORT).show();
                        passwordEditText.setError("This password is too short. You must input at least 5 lengths.");
                        passwordEditText.selectAll();
                        passwordEditText.requestFocus();
                        break;
                    case TASK_FAILURE:
                        Log.d("LoginActivity", "Task is not successful");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void doNext(boolean isSuccesful, Object obj)
            {
                if( loadingDialog != null )
                    loadingDialog.dismiss();

                if( !isSuccesful )
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                if (obj == null)
                    occurError(ID_NOT_EXISTED);
                else
                {
                    Map<String, Object> map = (Map<String, Object>) obj;

                    if( map.get("password").equals(passwordEditText.getText().toString().trim()) )
                    {
                        MainActivity.id = idEditText.getText().toString().trim();
                        MainActivity.admin = (boolean) map.get("admin");
                        login(false);
                    }
                    else
                    {
                        occurError(PASSWORD_NOT_MATCHED);
                        return;
                    }
                }
            }
        });
    }

    private void bindUI()
    {
        this.idEditText = findViewById(R.id.id_editText);
        this.passwordEditText = findViewById(R.id.password_editText);
        this.autoLoginCheckBox = findViewById(R.id.auto_login_checkBox);
        this.signUpBtn = findViewById(R.id.sign_up_btn);
        this.signInBtn = findViewById(R.id.sign_in_btn);

        this.signUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
            }
        });

        this.signInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( idEditText.getText().toString().trim().length() < 5 )
                {
                    fireStoreCallbackListener.occurError(2);
                    return;
                }

                if( passwordEditText.getText().toString().trim().length() < 5 )
                {
                    fireStoreCallbackListener.occurError(3);
                    return;
                }

                if( loadingDialog == null )
                    loadingDialog = new LoadingDialog(LoginActivity.this);

                loadingDialog.show("Login...");
                FireStoreConnectionPool.getInstance().selectOne(fireStoreCallbackListener,
                        "user", idEditText.getText().toString().trim());
            }
        });
    }

    private void login(boolean isAutoLogin)
    {
        if( !isAutoLogin && this.autoLoginCheckBox.isChecked() )
        {
            SharedPreferencesInstance.getInstance().setSharedPreferencesContext(this, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
            SharedPreferencesInstance.getInstance().set("id", MainActivity.id);
            SharedPreferencesInstance.getInstance().set("admin", MainActivity.admin);
        }

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }
}
