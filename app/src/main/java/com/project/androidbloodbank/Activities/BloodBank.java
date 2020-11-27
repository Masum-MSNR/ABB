package com.project.androidbloodbank.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class BloodBank extends AppCompatActivity {

    ProgressDialog progressDialog;
    LinearLayout ll;
    ImageView backIv;
    private TextView apTv, anTv, bpTv, bnTv, opTv, onTv, abpTv, abnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_blood_bank);

        apTv = findViewById(R.id.ap);
        anTv = findViewById(R.id.an);
        bpTv = findViewById(R.id.bp);
        bnTv = findViewById(R.id.bn);
        opTv = findViewById(R.id.op);
        onTv = findViewById(R.id.on);
        abpTv = findViewById(R.id.abp);
        abnTv = findViewById(R.id.abn);
        ll = findViewById(R.id.ll1);
        backIv = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_animation);
        showNumber("A+");
        showNumber("A-");
        showNumber("B+");
        showNumber("B-");
        showNumber("O+");
        showNumber("O-");
        showNumber("AB+");
        showNumber("AB-");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void textSetter(String number, String bloodGroup) {
        switch (bloodGroup) {
            case "A+":
                apTv.setText(bloodGroup + " :      " + number);
                break;
            case "A-":
                anTv.setText(bloodGroup + " :      " + number);
                break;
            case "B+":
                bpTv.setText(bloodGroup + " :      " + number);
                break;
            case "B-":
                bnTv.setText(bloodGroup + " :      " + number);
                break;
            case "O+":
                opTv.setText(bloodGroup + " :      " + number);
                break;
            case "O-":
                onTv.setText(bloodGroup + " :      " + number);
                break;
            case "AB+":
                abpTv.setText(bloodGroup + " :    " + number);
                break;
            case "AB-":
                abnTv.setText(bloodGroup + " :    " + number);
                ll.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                break;
        }
    }

    private void showNumber(final String bloodGroup) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.availableDonorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    textSetter(response, bloodGroup);
                } else {
                    Toast.makeText(BloodBank.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BloodBank.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bloodGroup", bloodGroup);
                return params;
            }
        };
        VolleySingleton.getInstance(BloodBank.this).addToRequestQueue(stringRequest);
    }
}