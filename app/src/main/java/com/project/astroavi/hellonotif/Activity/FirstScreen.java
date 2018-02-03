package com.project.astroavi.hellonotif.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.project.astroavi.hellonotif.Adapter.AppsAdapter;
import com.project.astroavi.hellonotif.Model.NotifAppData;
import com.project.astroavi.hellonotif.R;
import com.project.astroavi.hellonotif.Utils.AppCheckListener;
import com.project.astroavi.hellonotif.Utils.NotifDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avinash on 01/05/17.
 */

public class FirstScreen extends ListActivity implements AppCheckListener {

    private PackageManager packageManager = null;
    private AppsAdapter listadaptor = null;
    private List<NotifAppData> selectedAppsList = new ArrayList<>();
    private List<NotifAppData> deselectedAppsList = new ArrayList<>();
    private NotifDataBaseHelper db = new NotifDataBaseHelper(this);
    private ProgressDialog progress = null;
    private Button entryButton;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean checkIfEditAppsScreen = getIntent().getBooleanExtra("EditApps", false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if ((!prefs.getBoolean("firstTime", false)) || checkIfEditAppsScreen) {
            editor = prefs.edit();
            //Did this to avoid loading of startApp function if the user doesnt check any apps
            if (db.getNotifDataCount() > 0) {
                editor.putBoolean("firstTime", true).apply();
            }
        } else {
            startApp();
            return;
        }

        setContentView(R.layout.apps_list);
        entryButton = (Button) findViewById(R.id.enter);
        if (checkIfEditAppsScreen) {
            entryButton.setText("UPDATE");
        } else {
            entryButton.setText("ENTER");
        }

        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfEditAppsScreen) {
                    db.addAppsSelected(selectedAppsList);
                    for (NotifAppData notifAppData : deselectedAppsList) {
                        db.deleteNotifData(notifAppData);
                    }
                    final Intent intent = new Intent(FirstScreen.this, AppsGridScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    db.addAppsSelected(selectedAppsList);
                    startApp();
                }
                if (db.getNotifDataCount() > 0) {
                    editor.putBoolean("splash", true).apply();
                }
            }
        });

        packageManager = getPackageManager();
        new LoadApplications().execute();
    }

    private void startApp() {
        startActivity(new Intent(FirstScreen.this, AppsGridScreen.class));
        finish();
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applist;
    }

    @Override
    public void setApps(String app, boolean appChecked, String appName, byte[] appImage) {
        final NotifAppData notifAppData = new NotifAppData(appName, appImage, app);
        if (appChecked) {
            selectedAppsList.add(notifAppData);
            deselectedAppsList.remove(notifAppData);
            Log.d("checkItadded", app);
        } else {
            selectedAppsList.remove(notifAppData);
            deselectedAppsList.add(notifAppData);
            Log.d("checkItremoved", app);
        }
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<ApplicationInfo> applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadaptor = new AppsAdapter(FirstScreen.this,
                    R.layout.single_app_layout, applist);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            entryButton.setVisibility(View.VISIBLE);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(FirstScreen.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
