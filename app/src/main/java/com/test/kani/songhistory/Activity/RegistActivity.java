package com.test.kani.songhistory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.utility.FireStoreCallbackListener;
import com.test.kani.songhistory.utility.FireStoreConnectionPool;
import com.test.kani.songhistory.utility.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends AppCompatActivity
{
    EditText idEditText, passwordEditText;
    Button signUpBtn, cancelBtn;

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
        setContentView(R.layout.activity_regist);

        this.bindUI();
    }

    private void bindUI()
    {
        this.idEditText = findViewById(R.id.id_editText);
        this.passwordEditText = findViewById(R.id.password_editText);
        this.signUpBtn = findViewById(R.id.sign_up_btn);
        this.cancelBtn = findViewById(R.id.cancel_btn);

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int ID_EXISTED = 0;
            final int ID_SHORT = 1;
            final int PASSWORD_SHORT = 2;
            final int TASK_FAILURE = 3;

            boolean idValidate = false;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case ID_EXISTED:
                        Log.d("RegistActivity", "This ID is existed");
//                        Toast.makeText(getApplicationContext(), "This ID is already existed.", Toast.LENGTH_SHORT).show();
                        idEditText.setError("This ID is already existed.");
                        idEditText.selectAll();
                        idEditText.requestFocus();
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
                        Log.d("RegistActivity", "Task is not successful");
                        break;
                    default:
                        break;
                }

                if( loadingDialog.isShowing() )
                    loadingDialog.dismiss();
            }

            @Override
            public void doNext(boolean isSuccesful, Object obj)
            {
                if( loadingDialog != null && loadingDialog.isShowing() )
                    loadingDialog.dismiss();

                if( !isSuccesful )
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                if( !idValidate && obj == null )
                {
                    loadingDialog.show("Regist User...");

                    Map<String, Object> map = new HashMap<> ();
                    map.put("password", passwordEditText.getText().toString().trim());
                    map.put("admin", false);
                    FireStoreConnectionPool.getInstance().insert(fireStoreCallbackListener, map, "user",
                            idEditText.getText().toString().trim());
                    idValidate = true;
                    return;
                }
                else if( !idValidate && obj != null )
                {
                    occurError(ID_EXISTED);
                    return;
                }

                Toast.makeText(getApplicationContext(), "Congratulation!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        this.signUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (loadingDialog == null)
                    loadingDialog = new LoadingDialog(RegistActivity.this);

                if( idEditText.getText().toString().trim().length() < 5 )
                {
                    fireStoreCallbackListener.occurError(1);
                    return;
                }

                if( passwordEditText.getText().toString().trim().length() < 5 )
                {
                    fireStoreCallbackListener.occurError(2);
                    return;
                }

                loadingDialog.show("ID Validating...");
                FireStoreConnectionPool.getInstance().selectOne(fireStoreCallbackListener,
                        "user", idEditText.getText().toString().trim());
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
