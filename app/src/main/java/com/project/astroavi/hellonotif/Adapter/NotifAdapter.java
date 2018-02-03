package com.project.astroavi.hellonotif.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.astroavi.hellonotif.Model.NotifData;
import com.project.astroavi.hellonotif.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Avinash on 19/03/17.
 */

public class NotifAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotifData> notifDataList;
    private PackageManager packageManager = null;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notifTitle, notifContent, notifTimeStamp;

        public MyViewHolder(View view) {
            super(view);
            notifTitle = (TextView) view.findViewById(R.id.notif_title);
            notifContent = (TextView) view.findViewById(R.id.notif_content);
            notifTimeStamp = (TextView) view.findViewById(R.id.notif_timeStamp);
        }
    }

    public NotifAdapter(Context context, List<NotifData> notifDataList) {
        this.notifDataList = notifDataList;
        this.context = context;
        packageManager = context.getPackageManager();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            NotifAdapter.MyViewHolder myViewHolder = (MyViewHolder) holder;
            final NotifData notifData = notifDataList.get(position);
            myViewHolder.notifTitle.setText(notifData.getNotifTitle());
            myViewHolder.notifContent.setText(notifData.getNotifMessage());
            myViewHolder.notifTimeStamp.setText(getDate(Long.parseLong(notifData.getNotifTimeStamp())));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = packageManager
                            .getLaunchIntentForPackage(notifData.getNotifPackageName());

                    if (null != intent) {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notifDataList != null ? notifDataList.size() : 0;
    }

    private String getDate(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("MMM. dd");
            Date netDate = new Date(timeStamp);
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

}
