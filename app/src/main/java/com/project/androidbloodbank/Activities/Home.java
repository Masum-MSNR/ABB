package com.project.androidbloodbank.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.project.androidbloodbank.Classes.AboutDialog;
import com.project.androidbloodbank.R;
import com.project.androidbloodbank.Utils.Urls;
import com.project.androidbloodbank.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    CardView searchDonorCv, bloodBankCv, bloodRequestCv, myProfileCv, aboutUsCv, logoutCv;
    ImageView backIv;
    String mobileNumber;
    ProgressDialog progressDialog;
    TextView pendingReqMessageTv;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_home);
        searchDonorCv = findViewById(R.id.search_donor_cv);
        bloodBankCv = findViewById(R.id.blood_bank_cv);
        bloodRequestCv = findViewById(R.id.blood_request_cv);
        myProfileCv = findViewById(R.id.my_profile_cv);
        aboutUsCv = findViewById(R.id.about_us_cv);
        logoutCv = findViewById(R.id.logout_cv);
        backIv = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        pendingReqMessageTv = findViewById(R.id.pendingReqMessage);
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

        searchDonorCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SearchDonor.class);
                startActivity(intent);
            }
        });
        bloodBankCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, BloodBank.class);
                startActivity(intent);
            }
        });
        bloodRequestCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, BloodRequest.class);
                intent.putExtra("mobileNumber", mobileNumber);
                startActivity(intent);
            }
        });
        myProfileCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyProfile.class);
                intent.putExtra("mobileNumber", mobileNumber);
                startActivity(intent);
            }
        });
        aboutUsCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.setCancelable(false);
                aboutDialog.show(getSupportFragmentManager(), null);
            }
        });
        logoutCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this, R.style.StyledDialog1);
                dialog.setMessage("Are you sure to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
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
                    UserRequestsAdapter adapter = new UserRequestsAdapter(Home.this, response);
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
                        Toast.makeText(Home.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", "all");
                return params;
            }
        };
        VolleySingleton.getInstance(Home.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.StyledDialog1);
        dialog.setMessage("Do you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}