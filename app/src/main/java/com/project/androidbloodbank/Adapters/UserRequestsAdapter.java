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

public class UserRequestsAdapter extends RecyclerView.Adapter<UserRequestsAdapter.ViewHolder> {

    String response;
    Context context;
    JSONArray array;

    public UserRequestsAdapter(Context context, String response) {
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
                .inflate(R.layout.user_requests_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final JSONObject object = array.getJSONObject(position);
            holder.requestTv.setText(object.get("request").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return array.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView requestTv;

        ViewHolder(final View itemView) {
            super(itemView);
            requestTv=itemView.findViewById(R.id.request);
        }

    }
}
