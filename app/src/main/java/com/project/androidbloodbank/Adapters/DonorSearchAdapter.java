package com.project.androidbloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.project.androidbloodbank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CALL_PHONE;

public class DonorSearchAdapter extends RecyclerView.Adapter<DonorSearchAdapter.ViewHolder> {

    String response;
    Context context;
    JSONArray array;

    public DonorSearchAdapter(Context context, String response) {
        this.context = context;
        this.response = response;
        try {
            array = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final JSONObject object = array.getJSONObject(position);
            holder.nameTv.setText("Name: " + object.get("name").toString());
            holder.addressTv.setText("Address: " + object.get("area").toString() + ", " + object.get("district").toString() + ", " + object.get("division").toString() + ", ");
            holder.mobileNumberTv.setText(object.get("mobileNumber").toString());
            holder.mobileNumberTv.setPaintFlags(holder.mobileNumberTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.mobileNumberTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionChecker.checkSelfPermission(context, CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        try {
                            intent.setData(Uri.parse("tel:" + object.get("mobileNumber").toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                        }
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return array.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, mobileNumberTv, addressTv;

        ViewHolder(final View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            mobileNumberTv = itemView.findViewById(R.id.mobile_number);
            addressTv = itemView.findViewById(R.id.address);
        }

    }
}
