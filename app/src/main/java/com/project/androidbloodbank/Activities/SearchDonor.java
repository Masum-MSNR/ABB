package com.project.androidbloodbank.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.project.androidbloodbank.Adapters.DonorSearchAdapter;
import com.project.androidbloodbank.R;
import com.project.androidbloodbank.Utils.Urls;
import com.project.androidbloodbank.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SearchDonor extends AppCompatActivity {

    TextView resultsTv, notFoundTv;
    Spinner bloodGroupSp, divisionSp;
    Button searchBt, searchAgainBt;
    ImageView backIv;
    LinearLayout ll;
    RelativeLayout rl;
    String bloodGroup, division;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_search_donor);

        resultsTv = findViewById(R.id.search_result);
        notFoundTv = findViewById(R.id.notFound);
        bloodGroupSp = findViewById(R.id.blood_group_spinner);
        divisionSp = findViewById(R.id.division_spinner);
        searchBt = findViewById(R.id.search);
        searchAgainBt = findViewById(R.id.search_again);
        backIv = findViewById(R.id.back);
        ll = findViewById(R.id.ll1);
        rl = findViewById(R.id.rl1);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);


        ArrayAdapter<String> adapterBg = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.bloodgroups));
        adapterBg.setDropDownViewResource(R.layout.spinner_dropdown);
        bloodGroupSp.setAdapter(adapterBg);
        ArrayAdapter<String> adapterDiv = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.division_list));
        adapterDiv.setDropDownViewResource(R.layout.spinner_dropdown);
        divisionSp.setAdapter(adapterDiv);

        bloodGroupSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        divisionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                division = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notFoundTv.setVisibility(View.GONE);
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                if (bloodGroup.equals("__Select One__")) {
                    progressDialog.dismiss();
                    Toast.makeText(SearchDonor.this, "Select blood group!", Toast.LENGTH_SHORT).show();
                } else if (division.equals("__Select One__")) {
                    progressDialog.dismiss();
                    Toast.makeText(SearchDonor.this, "Select division!", Toast.LENGTH_SHORT).show();
                } else {
                    search();
                }

            }
        });
        searchAgainBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                resultsTv.setVisibility(View.GONE);
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void search() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.searchDonorUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    progressDialog.dismiss();
                    notFoundTv.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    resultsTv.setVisibility(View.VISIBLE);
                    DonorSearchAdapter adapter = new DonorSearchAdapter(SearchDonor.this, response);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
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
                        Toast.makeText(SearchDonor.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bloodGroup", bloodGroup);
                params.put("division", division);
                return params;
            }
        };
        VolleySingleton.getInstance(SearchDonor.this).addToRequestQueue(stringRequest);
    }
}