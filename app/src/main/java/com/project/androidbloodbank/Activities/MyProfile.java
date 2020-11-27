package com.project.androidbloodbank.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {

    EditText nameEt, mobileNumberEt, bloodGroupEt, genderEt, addressEt, areaEt, lastDonationDateEt;
    Spinner divisionSp, districtSp;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButton1, radioButton2;
    Button updateSecurityQuestionBt, changePasswordBt, editProfileBt, saveBt;
    LinearLayout ll, ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8, ll9;
    ScrollView sv;
    ProgressDialog progressDialog;
    ImageView backIv;

    String mobileNumber = "", division, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_my_profile);
        nameEt = findViewById(R.id.name);
        areaEt = findViewById(R.id.areaEt);
        addressEt = findViewById(R.id.addressEt);
        lastDonationDateEt = findViewById(R.id.lastDonationDateEt);
        mobileNumberEt = findViewById(R.id.mobileNumberEt);
        bloodGroupEt = findViewById(R.id.bloodGroupEt);
        genderEt = findViewById(R.id.genderEt);
        updateSecurityQuestionBt = findViewById(R.id.update_security_question);
        changePasswordBt = findViewById(R.id.change_password);
        editProfileBt = findViewById(R.id.edit_profile);
        saveBt = findViewById(R.id.save);
        districtSp = findViewById(R.id.districtSp);
        divisionSp = findViewById(R.id.divisionSp);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.rbid1);
        radioButton2 = findViewById(R.id.rbid2);
        backIv = findViewById(R.id.back);
        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);
        ll8 = findViewById(R.id.ll8);
        ll9 = findViewById(R.id.ll9);
        sv = findViewById(R.id.sv);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobileNumber");

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
        ll9.setOnClickListener(new View.OnClickListener() {
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

        final ArrayAdapter<String> adapterDiv = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.division_list));
        adapterDiv.setDropDownViewResource(R.layout.spinner_dropdown);
        divisionSp.setAdapter(adapterDiv);
        final ArrayAdapter<String> adapterDis = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.Select_One));
        adapterDis.setDropDownViewResource(R.layout.spinner_dropdown);
        districtSp.setAdapter(adapterDis);

        divisionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                division = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Select_One));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 1) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Dhaka));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 2) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Rajshahi));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 3) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Barishal));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 4) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Khulna));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 5) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Sylhet));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 6) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Rangpur));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 7) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Chattogram));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 8) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(MyProfile.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Mymensingh));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        districtSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lastDonationDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyBoard();
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    DatePickerDialog dialog = new DatePickerDialog(
                            MyProfile.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    month++;
                                    lastDonationDateEt.setText(dayOfMonth + "/" + month + "/" + year);
                                }
                            }, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_animation);
        setBackgroundNull();
        getDetails();

        editProfileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        changePasswordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyProfile.this, PasswordRecover.class);
                intent1.putExtra("mobileNumber", mobileNumber);
                intent1.putExtra("bool", true);
                startActivity(intent1);
            }
        });

        updateSecurityQuestionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyProfile.this, SecurityQuestion.class);
                intent1.putExtra("mobileNumber", mobileNumber);
                intent1.putExtra("bool", true);
                startActivity(intent1);
            }
        });
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                if (isValid()) {
                    setDetails();
                }
            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setBackgroundNull() {
        nameEt.setBackground(null);
        mobileNumberEt.setBackground(null);
        bloodGroupEt.setBackground(null);
        genderEt.setBackground(null);
        addressEt.setBackground(null);
        areaEt.setBackground(null);
        lastDonationDateEt.setBackground(null);
    }

    public void edit() {
        nameEt.setEnabled(true);
        nameEt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_edittext));
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        ll7.setVisibility(View.VISIBLE);
        ll8.setVisibility(View.VISIBLE);
        ll5.setVisibility(View.VISIBLE);
        areaEt.setEnabled(true);
        areaEt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_edittext));
        radioButton1.setEnabled(true);
        radioButton2.setEnabled(true);
        lastDonationDateEt.setEnabled(true);
        lastDonationDateEt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_edittext));
        editProfileBt.setVisibility(View.GONE);
        updateSecurityQuestionBt.setVisibility(View.VISIBLE);
        changePasswordBt.setVisibility(View.VISIBLE);
        saveBt.setVisibility(View.VISIBLE);
    }

    public void setDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.setDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    setBackgroundNull();
                    getDetails();
                    progressDialog.dismiss();
                    Toast.makeText(MyProfile.this, "Your profile updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MyProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MyProfile.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", mobileNumber);
                params.put("name", nameEt.getText().toString());
                params.put("division", division);
                params.put("district", district);
                params.put("area", areaEt.getText().toString());
                params.put("beDonor", radioButton.getText().toString().equals("Yes") ? "true" : "false");
                params.put("lastDonationDate", lastDonationDateEt.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance(MyProfile.this).addToRequestQueue(stringRequest);
    }

    public void getDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.getDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object = array.getJSONObject(0);
                    nameEt.setText(object.get("name").toString());
                    nameEt.setEnabled(false);
                    mobileNumberEt.setText(mobileNumber);
                    ll1.setVisibility(View.VISIBLE);
                    bloodGroupEt.setText(object.get("bloodGroup").toString());
                    ll2.setVisibility(View.VISIBLE);
                    genderEt.setText(object.get("gender").toString());
                    ll3.setVisibility(View.VISIBLE);
                    addressEt.setText(object.get("area").toString() + ", " + object.get("district").toString() + ", " + object.get("division").toString());
                    ll4.setVisibility(View.VISIBLE);
                    areaEt.setText(object.get("area").toString());
                    lastDonationDateEt.setText(object.get("lastDonationDate").toString());
                    ll6.setVisibility(View.VISIBLE);
                    lastDonationDateEt.setEnabled(false);
                    String beDonorT = object.get("beDonor").toString();
                    if (beDonorT.equals("true")) {
                        radioButton1.setChecked(true);
                        radioButtonClicked(radioButton1);
                        radioButton1.setEnabled(false);
                        radioButton2.setEnabled(false);
                    } else {
                        radioButton2.setChecked(true);
                        radioButtonClicked(radioButton2);
                        radioButton1.setEnabled(false);
                        radioButton2.setEnabled(false);
                    }
                    ll5.setVisibility(View.GONE);
                    ll7.setVisibility(View.GONE);
                    ll8.setVisibility(View.GONE);
                    updateSecurityQuestionBt.setVisibility(View.GONE);
                    saveBt.setVisibility(View.GONE);
                    changePasswordBt.setVisibility(View.GONE);
                    editProfileBt.setVisibility(View.VISIBLE);
                    ll9.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        Toast.makeText(MyProfile.this, "Error!:(", Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(MyProfile.this).addToRequestQueue(stringRequest);
    }

    public boolean isValid() {
        if (nameEt.getText().toString().isEmpty()) {
            progressDialog.dismiss();
            nameEt.setError("Name is empty!");
            nameEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(nameEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (division.equals("__Select One__")) {
            progressDialog.dismiss();
            Toast.makeText(MyProfile.this, "Select A Division!", Toast.LENGTH_SHORT).show();
            divisionSp.requestFocus();
            return false;
        } else if (district.equals("__Select One__")) {
            progressDialog.dismiss();
            Toast.makeText(MyProfile.this, "Select A District!", Toast.LENGTH_SHORT).show();
            districtSp.requestFocus();
            return false;
        } else if (areaEt.getText().toString().isEmpty()) {
            progressDialog.dismiss();
            areaEt.setError("Enter Area/Village!");
            areaEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(areaEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else {
            return true;
        }
    }

    public void radioButtonClicked(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioButton.getText().toString().equals("Yes")) {
            ll6.setVisibility(View.VISIBLE);
        } else {
            ll6.setVisibility(View.GONE);
        }
        hideKeyBoard();
    }

    private void hideKeyBoard() {
        nameEt.clearFocus();
        areaEt.clearFocus();
        lastDonationDateEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }

    private void setErrorNull() {
        nameEt.setError(null);
        areaEt.setError(null);
        lastDonationDateEt.setError(null);
    }
}