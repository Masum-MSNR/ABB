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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.project.androidbloodbank.Adapters.UserRequestsAdapter;
import com.project.androidbloodbank.R;
import com.project.androidbloodbank.Utils.Urls;
import com.project.androidbloodbank.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BloodRequest extends AppCompatActivity {

    EditText requestTextEt;
    Button requestBt;
    TextView pendingReqMessageTv;
    LinearLayout ll;
    ImageView backIv;
    ProgressDialog progressDialog;
    String mobileNumber;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_blood_request);

        requestTextEt = findViewById(R.id.request_text);
        requestBt = findViewById(R.id.request);
        ll = findViewById(R.id.ll);
        pendingReqMessageTv = findViewById(R.id.pendingReqMessage);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        backIv = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_animation);
        userRequests();

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobileNumber");

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });

        requestBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorNull();
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                final String request = requestTextEt.getText().toString();
                if (request.length() < 30) {
                    progressDialog.dismiss();
                    requestTextEt.setError("Too small!");
                    requestTextEt.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(requestTextEt, InputMethodManager.SHOW_FORCED);
                } else {
                    hideKeyBoard();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.bloodRequestUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("Success")) {
                                requestTextEt.setText("");
                                Toast.makeText(BloodRequest.this, "Request send successfully.", Toast.LENGTH_SHORT).show();
                                userRequests();
                            } else {
                                Toast.makeText(BloodRequest.this, "Something went wrong!\nPlease use plain text.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(BloodRequest.this, "Error!:(", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("request", request);
                            params.put("mobileNumber", mobileNumber);
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(BloodRequest.this).addToRequestQueue(stringRequest);
                }
            }
        });


    }

    public void userRequests() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.userRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("not found!")) {
                    pendingReqMessageTv.setVisibility(View.VISIBLE);
                } else {
                    pendingReqMessageTv.setVisibility(View.GONE);
                    UserRequestsAdapter adapter = new UserRequestsAdapter(BloodRequest.this, response);
                    recyclerView.setAdapter(adapter);
                }
                progressDialog.dismiss();
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
                        Toast.makeText(BloodRequest.this, "Error!:(", Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(BloodRequest.this).addToRequestQueue(stringRequest);
    }

    public void setErrorNull() {
        requestTextEt.setError(null);
    }

    public void hideKeyBoard() {
        requestTextEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }
}