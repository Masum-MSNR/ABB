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
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.project.androidbloodbank.R;
import com.project.androidbloodbank.Utils.Urls;
import com.project.androidbloodbank.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PasswordRecover extends AppCompatActivity {

    EditText mobileNumberEt, answerEt, passwordEt;
    Button nextBt, nextBt1, setPasswordBt;
    TextView questionTv, messageTv, titleTv;
    ProgressDialog progressDialog;
    LinearLayout ll, ll1;
    ScrollView sv;
    ImageView backIv;
    TextInputLayout til1, til2, til3;
    String gAnswer, gMobileNumber;
    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_password_recover);

        mobileNumberEt = findViewById(R.id.mobileNumber);
        answerEt = findViewById(R.id.answer);
        passwordEt = findViewById(R.id.newPassword);
        questionTv = findViewById(R.id.question);
        messageTv = findViewById(R.id.message);
        titleTv = findViewById(R.id.title);
        nextBt = findViewById(R.id.next);
        nextBt1 = findViewById(R.id.next1);
        setPasswordBt = findViewById(R.id.setPassword);
        backIv = findViewById(R.id.back);
        til1 = findViewById(R.id.til1);
        til2 = findViewById(R.id.til2);
        til3 = findViewById(R.id.til3);

        Intent intent = getIntent();
        bool = intent.getBooleanExtra("bool", false);

        if (bool) {
            gMobileNumber = intent.getStringExtra("mobileNumber");
            til1.setVisibility(View.GONE);
            nextBt.setVisibility(View.GONE);
            til3.setVisibility(View.VISIBLE);
            setPasswordBt.setVisibility(View.VISIBLE);
            titleTv.setText("Change Password");
        }

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);
        sv = findViewById(R.id.sv);


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

        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);


        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorNull();
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                String mobileNumber = mobileNumberEt.getText().toString();
                if (mobileNumber.length() == 11) {
                    gMobileNumber = mobileNumber;
                    findQuestionAnswer(mobileNumber);
                } else {
                    progressDialog.dismiss();
                    mobileNumberEt.setError("Invalid mobile number!");
                    mobileNumberEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        nextBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answerEt.getText().toString();
                if (answer.isEmpty()) {
                    answerEt.setError("Please enter your answer!");
                    answerEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(answerEt, InputMethodManager.SHOW_FORCED);
                } else if (gAnswer.equals(answer)) {
                    questionTv.setVisibility(View.GONE);
                    til2.setVisibility(View.GONE);
                    nextBt1.setVisibility(View.GONE);
                    til3.setVisibility(View.VISIBLE);
                    setPasswordBt.setVisibility(View.VISIBLE);
                } else {
                    answerEt.setError("Invalid answer!");
                    answerEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(answerEt, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        setPasswordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                if (passwordEt.getText().toString().length() < 8) {
                    progressDialog.dismiss();
                    passwordEt.setError("Password must be at least 8 character!");
                    passwordEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(passwordEt, InputMethodManager.SHOW_FORCED);
                } else {
                    setPassword(passwordEt.getText().toString());
                }
            }
        });


    }

    public void findQuestionAnswer(final String mobileNumber) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.recoverPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    progressDialog.dismiss();
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject object = array.getJSONObject(0);
                        if (object.get("securityQuestion").toString().equals("null")) {
                            Toast.makeText(PasswordRecover.this, "Security question not found!", Toast.LENGTH_SHORT).show();
                        } else {
                            questionTv.setText(object.get("securityQuestion").toString());
                            gAnswer = object.get("answer").toString();
                            til1.setVisibility(View.GONE);
                            til2.setVisibility(View.VISIBLE);
                            nextBt.setVisibility(View.GONE);
                            nextBt1.setVisibility(View.VISIBLE);
                            questionTv.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    mobileNumberEt.setError("Mobile number not found!");
                    mobileNumberEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
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
                        Toast.makeText(PasswordRecover.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", mobileNumber);
                return params;
            }
        };
        VolleySingleton.getInstance(PasswordRecover.this).addToRequestQueue(stringRequest);
    }

    public void setPassword(final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.setPasswordUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    if (bool) {
                        progressDialog.dismiss();
                        Toast.makeText(PasswordRecover.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                        PasswordRecover.this.finish();
                    } else {
                        progressDialog.dismiss();
                        til3.setVisibility(View.GONE);
                        setPasswordBt.setVisibility(View.GONE);
                        messageTv.setVisibility(View.VISIBLE);
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PasswordRecover.this, "Something went wrong!\nPlease try again.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PasswordRecover.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", gMobileNumber);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(PasswordRecover.this).addToRequestQueue(stringRequest);
    }

    private void setErrorNull() {

        mobileNumberEt.setError(null);
        passwordEt.setError(null);
        answerEt.setError(null);

    }

    public void hideKeyBoard() {
        mobileNumberEt.clearFocus();
        passwordEt.clearFocus();
        answerEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }
}