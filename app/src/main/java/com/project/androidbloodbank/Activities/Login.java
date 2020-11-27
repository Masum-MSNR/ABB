package com.project.androidbloodbank.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.project.androidbloodbank.R;
import com.project.androidbloodbank.Utils.Urls;
import com.project.androidbloodbank.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText mobileNumberEt, passwordEt;
    Button login;
    TextView forPassTv, signUpTv;
    LinearLayout ll, ll1;
    ScrollView sv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        mobileNumberEt = findViewById(R.id.mobile_number);
        passwordEt = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forPassTv = findViewById(R.id.forgotten_password);
        signUpTv = findViewById(R.id.signUp);
        sv = findViewById(R.id.sv);
        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);
        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });

        forPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
                mobileNumberEt.clearFocus();
                passwordEt.clearFocus();
                startActivity(new Intent(Login.this, PasswordRecover.class));
            }
        });

        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
                mobileNumberEt.clearFocus();
                passwordEt.clearFocus();
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                mobileNumberEt.setError(null);
                passwordEt.setError(null);
                String mobileNumber, password;
                mobileNumber = mobileNumberEt.getText().toString();
                password = passwordEt.getText().toString();
                if (isValid(mobileNumber, password)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
                    mobileNumberEt.clearFocus();
                    passwordEt.clearFocus();
                    login(mobileNumber, password);
                }
            }
        });

    }

    private boolean isValid(String mobileNumber, String password) {
        if (mobileNumber.length() != 11) {
            progressDialog.dismiss();
            mobileNumberEt.setError("Invalid Number!");
            mobileNumberEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (password.isEmpty()) {
            progressDialog.dismiss();
            passwordEt.setError("Empty Password!");
            passwordEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(passwordEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else {
            return true;
        }
    }

    private void login(final String mobileNumber, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    startActivity(intent);
                    Login.this.finish();
                } else if (response.equals("Number Not Found!")) {
                    progressDialog.dismiss();
                    mobileNumberEt.requestFocus();
                    mobileNumberEt.setError(response);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
                } else if (response.equals("Invalid Password!")) {
                    progressDialog.dismiss();
                    passwordEt.setError(response);
                    passwordEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(passwordEt, InputMethodManager.SHOW_FORCED);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        progressDialog.dismiss();
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException | JSONException e1) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", mobileNumber);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void hideKeyBoard() {
        mobileNumberEt.clearFocus();
        passwordEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }

}