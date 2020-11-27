package com.project.androidbloodbank.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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

public class SecurityQuestion extends AppCompatActivity {

    Spinner securityQuestionSp;
    EditText answerEt;
    Button setBt, maybeLaterBt;
    TextView titleTv;
    ImageView backIv;
    String securityQuestion, answer, mobileNumber;
    Boolean bool;

    LinearLayout ll, ll1;
    ScrollView sv;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_security_question);

        securityQuestionSp = findViewById(R.id.security_question);
        answerEt = findViewById(R.id.answer);
        setBt = findViewById(R.id.set);
        maybeLaterBt = findViewById(R.id.maybeLater);
        titleTv = findViewById(R.id.title);
        backIv = findViewById(R.id.back);

        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);

        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);
        sv = findViewById(R.id.sv);

        Intent intent = getIntent();

        mobileNumber = intent.getStringExtra("mobileNumber");
        bool = intent.getBooleanExtra("bool", false);

        if (bool) {
            titleTv.setText("Update Security Question");
            maybeLaterBt.setVisibility(View.GONE);
            backIv.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> adapterSq = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.Security_Question)
        );
        adapterSq.setDropDownViewResource(R.layout.spinner_dropdown);
        securityQuestionSp.setAdapter(adapterSq);


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

        securityQuestionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                securityQuestion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        maybeLaterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorNull();
                hideKeyBoard();
                Toast.makeText(SecurityQuestion.this, "Make sure to set security question later!", Toast.LENGTH_SHORT).show();
                SecurityQuestion.this.finish();
                Toast.makeText(SecurityQuestion.this, "Login with mobile number and password.", Toast.LENGTH_LONG).show();
            }
        });

        setBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorNull();
                hideKeyBoard();
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                answer = answerEt.getText().toString();
                if (securityQuestion.equals("__Select One__")) {
                    progressDialog.dismiss();
                    Toast.makeText(SecurityQuestion.this, "Select a question first!", Toast.LENGTH_SHORT).show();
                } else if (answer.isEmpty()) {
                    progressDialog.dismiss();
                    answerEt.setError("Write your answer here!");
                    answerEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(answerEt, InputMethodManager.SHOW_FORCED);
                } else {
                    hideKeyBoard();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.securityQuestionUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("Success")) {
                                progressDialog.dismiss();
                                if (bool) {
                                    Toast.makeText(SecurityQuestion.this, "Security question updated successfully!", Toast.LENGTH_SHORT).show();
                                    SecurityQuestion.this.finish();
                                } else {
                                    Toast.makeText(SecurityQuestion.this, "Security question added successfully!", Toast.LENGTH_SHORT).show();
                                    SecurityQuestion.this.finish();
                                    Toast.makeText(SecurityQuestion.this, "Now login with mobile number and password.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SecurityQuestion.this, "Something went wrong!Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String res = new String(response.data,
                                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException | JSONException e1) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SecurityQuestion.this, "Error!:(", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("securityQuestion", securityQuestion);
                            params.put("answer", answer);
                            params.put("mobileNumber", mobileNumber);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(SecurityQuestion.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }

    private void setErrorNull() {
        answerEt.setError(null);
    }

    private void hideKeyBoard() {
        answerEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }
}