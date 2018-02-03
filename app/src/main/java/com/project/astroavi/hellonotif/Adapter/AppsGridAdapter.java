package com.project.astroavi.hellonotif.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.astroavi.hellonotif.Activity.MainActivity;
import com.project.astroavi.hellonotif.Model.NotifAppData;
import com.project.astroavi.hellonotif.R;

import java.util.List;

/**
 * Created by Avinash on 04/06/17.
 */

public class AppsGridAdapter extends RecyclerView.Adapter<AppsGridAdapter.MyViewHolder> {

    private List<NotifAppData> notifDataList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public AppsGridAdapter(Context context, List<NotifAppData> notifDataList) {
        this.notifDataList = notifDataList;
        this.context = context;
    }

    @Override
    public AppsGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apps_grid_item_layout, parent, false);

        return new AppsGridAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppsGridAdapter.MyViewHolder holder, int position) {
        final NotifAppData notifData = notifDataList.get(position);
        holder.title.setText(notifData.getAppName());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("packageName", notifData.getAppPackageName());
                intent.putExtra("appName",notifData.getAppName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifDataList != null ? notifDataList.size() : 0;
    }

}
