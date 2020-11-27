package com.project.androidbloodbank.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText nameEt, mobileNumberEt, dateOfBirthEt, passwordEt, areaEt, lastDonationDateEt;
    ImageView backIv;
    TextInputLayout passwordIl, lastDonationDateIl;
    Spinner divisionSp, districtSp, bloodGroupSp;
    Button signUpBt;
    RadioGroup radioGroup;
    RadioButton radioButton;
    CheckBox beDonorCb;
    LinearLayout ll, ll1;
    ScrollView sv;
    ProgressDialog progressDialog;

    String beDonor = "false", division = "", district = "", bloodGroup = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_sign_up);

        backIv = findViewById(R.id.back);
        nameEt = findViewById(R.id.name);
        mobileNumberEt = findViewById(R.id.mobile_number);
        dateOfBirthEt = findViewById(R.id.date_of_birth);
        passwordEt = findViewById(R.id.password);
        lastDonationDateEt = findViewById(R.id.last_donation_date);
        areaEt = findViewById(R.id.area_village);
        districtSp = findViewById(R.id.district);
        divisionSp = findViewById(R.id.division);
        bloodGroupSp = findViewById(R.id.blood_group);
        signUpBt = findViewById(R.id.signup);
        beDonorCb = findViewById(R.id.bedonor);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.rbid1);
        passwordIl = findViewById(R.id.passwordIl);
        lastDonationDateIl = findViewById(R.id.lastDonationDateIl);
        ll = findViewById(R.id.ll);
        ll1 = findViewById(R.id.ll1);
        sv = findViewById(R.id.sv);

        progressDialog = new ProgressDialog(this, R.style.StyledDialog);
        progressDialog.setCancelable(false);

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
        final ArrayAdapter<String> adapterBg = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.bloodgroups));
        adapterBg.setDropDownViewResource(R.layout.spinner_dropdown);
        bloodGroupSp.setAdapter(adapterBg);


        divisionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                division = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Select_One));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 1) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Dhaka));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 2) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Rajshahi));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 3) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Barishal));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 4) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Khulna));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 5) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Sylhet));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 6) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Rangpur));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 7) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Chattogram));
                    dis.setDropDownViewResource(R.layout.spinner_dropdown);
                    districtSp.setAdapter(dis);
                } else if (position == 8) {
                    ArrayAdapter<String> dis = new ArrayAdapter<>(SignUp.this, R.layout.custom_spinner, getResources().getStringArray(R.array.Mymensingh));
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
        bloodGroupSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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


        passwordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordIl.setPasswordVisibilityToggleEnabled(true);
                passwordEt.setError(null);
                return false;
            }
        });


        dateOfBirthEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyBoard();
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    DatePickerDialog dialog = new DatePickerDialog(
                            SignUp.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    month++;
                                    dateOfBirthEt.setText(dayOfMonth + "/" + month + "/" + year);
                                }
                            }, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
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
                            SignUp.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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


        beDonorCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beDonorCb.isChecked()) {
                    beDonor = "true";
                    lastDonationDateIl.setVisibility(View.VISIBLE);
                    hideKeyBoard();
                } else {
                    beDonor = "false";
                    lastDonationDateIl.setVisibility(View.GONE);
                    lastDonationDateEt.setText("");
                    hideKeyBoard();
                }
            }
        });


        bloodGroupSp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return false;
            }
        });
        divisionSp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return false;
            }
        });
        districtSp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return false;
            }
        });


        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordIl.setPasswordVisibilityToggleEnabled(true);
            }
        });


        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorNull();
                progressDialog.show();
                progressDialog.setContentView(R.layout.loading_animation);
                String name = "", mobileNumber = "", dateOfBirth = "", area = "", gender = "", password = "", lastDonationDate = "";
                gender = radioButton.getText().toString();
                name = nameEt.getText().toString();
                mobileNumber = mobileNumberEt.getText().toString();
                dateOfBirth = dateOfBirthEt.getText().toString();
                area = areaEt.getText().toString();
                password = passwordEt.getText().toString();
                lastDonationDate = lastDonationDateEt.getText().toString();
                if (lastDonationDate.isEmpty()) {
                    lastDonationDate = "0";
                }
                if (isValid(name, mobileNumber, dateOfBirth, division, district, area, bloodGroup, password)) {
                    hideKeyBoard();
                    signUp(name, mobileNumber, dateOfBirth, division, district, area, bloodGroup, gender, password, beDonor, lastDonationDate);
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

    private void setErrorNull() {
        nameEt.setError(null);
        mobileNumberEt.setError(null);
        dateOfBirthEt.setError(null);
        areaEt.setError(null);
        passwordEt.setError(null);
        lastDonationDateEt.setError(null);
    }

    private void hideKeyBoard() {
        nameEt.clearFocus();
        mobileNumberEt.clearFocus();
        dateOfBirthEt.clearFocus();
        areaEt.clearFocus();
        passwordEt.clearFocus();
        lastDonationDateEt.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ll.getWindowToken(), 0);
    }

    private void signUp(final String name, final String mobileNumber, final String dateOfBirth, final String division, final String district,
                        final String area, final String bloodGroup, final String gender, final String password, final String beDonor, final String lastDonationDate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.signUpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(SignUp.this, SecurityQuestion.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    startActivity(intent);
                    SignUp.this.finish();
                } else if (response.equals("Number is already in use!")) {
                    progressDialog.dismiss();
                    mobileNumberEt.requestFocus();
                    mobileNumberEt.setError(response);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Error!:(", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(SignUp.this, "Error!:(", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("mobileNumber", mobileNumber);
                params.put("dateOfBirth", dateOfBirth);
                params.put("division", division);
                params.put("district", district);
                params.put("area", area);
                params.put("bloodGroup", bloodGroup);
                params.put("gender", gender);
                params.put("password", password);
                params.put("beDonor", beDonor);
                params.put("lastDonationDate", lastDonationDate);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name, String mobileNumber, String dateOfBirth, String division, String district,
                            String area, String bloodGroup, String password) {
        if (name.isEmpty()) {
            progressDialog.dismiss();
            nameEt.setError("Name is empty!");
            nameEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(nameEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (mobileNumber.length() != 11) {
            progressDialog.dismiss();
            mobileNumberEt.setError("Invalid mobile number!");
            mobileNumberEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mobileNumberEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (dateOfBirth.isEmpty()) {
            progressDialog.dismiss();
            dateOfBirthEt.setError("Enter your birth date!");
            dateOfBirthEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(dateOfBirthEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (area.isEmpty()) {
            progressDialog.dismiss();
            areaEt.setError("Enter Area/Village!");
            areaEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(areaEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else if (division.equals("__Select One__")) {
            progressDialog.dismiss();
            Toast.makeText(SignUp.this, "Select A Division!", Toast.LENGTH_SHORT).show();
            divisionSp.requestFocus();
            return false;
        } else if (district.equals("__Select One__")) {
            progressDialog.dismiss();
            Toast.makeText(SignUp.this, "Select A District!", Toast.LENGTH_SHORT).show();
            districtSp.requestFocus();
            return false;
        } else if (bloodGroup.equals("__Select One__")) {
            progressDialog.dismiss();
            Toast.makeText(SignUp.this, "Select Blood Group!", Toast.LENGTH_SHORT).show();
            bloodGroupSp.requestFocus();
            return false;
        } else if (password.length() < 8) {
            progressDialog.dismiss();
            passwordIl.setPasswordVisibilityToggleEnabled(false);
            passwordEt.setError("Password must be at least 8 character!");
            passwordEt.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(passwordEt, InputMethodManager.SHOW_FORCED);
            return false;
        } else {
            return true;
        }
    }

    public void radioButtonClicked(View view) {
        int radioid = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioid);
        hideKeyBoard();
    }
}