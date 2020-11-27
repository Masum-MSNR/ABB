package com.project.androidbloodbank.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donor {

@SerializedName("name")
@Expose
private String name;
@SerializedName("mobileNumber")
@Expose
private String mobileNumber;
@SerializedName("address")
@Expose
private String address;
@SerializedName("division")
@Expose
private String division;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getMobileNumber() {
return mobileNumber;
}

public void setMobileNumber(String mobileNumber) {
this.mobileNumber = mobileNumber;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getDivision() {
return division;
}

public void setDivision(String division) {
this.division = division;
}

}