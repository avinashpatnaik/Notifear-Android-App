package com.project.astroavi.hellonotif.Activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.project.astroavi.hellonotif.Adapter.AppsGridAdapter;
import com.project.astroavi.hellonotif.Model.NotifAppData;
import com.project.astroavi.hellonotif.Model.NotifData;
import com.project.astroavi.hellonotif.R;
import com.project.astroavi.hellonotif.Service.NotifService;
import com.project.astroavi.hellonotif.Utils.NotifDataBaseHelper;

import java.util.List;

/**
 * Created by Avinash on 04/06/17.
 */

public class AppsGridScreen extends AppCompatActivity {

    private NotifDataBaseHelper notifDataBaseHelper;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_grid_layout);
        if (!isNotifServiceRunning()) {
            alertDialog(this);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("NotifMsg"));
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        notifDataBaseHelper = new NotifDataBaseHelper(this);
        List<NotifAppData> notifAppDataList = notifDataBaseHelper.getAllNotifAppData();
        final AppsGridAdapter appsGridAdapter = new AppsGridAdapter(this, notifAppDataList);

        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(appsGridAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNotifServiceRunning()) {
            startService(new Intent(this, NotifService.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private boolean isNLServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotifService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotifServiceRunning() {
        ComponentName cn = new ComponentName(this, NotifService.class);
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return enabled;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch (item.getItemId()) {
            case R.id.menu_edit: {
                Intent intent = new Intent(AppsGridScreen.this, FirstScreen.class);
                intent.putExtra("EditApps", true);
                startActivity(intent);
                break;
            }
            case R.id.about: {
                Intent intent = new Intent(AppsGridScreen.this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void alertDialog(final Context context) {
        final Resources resources = context.getResources();
        final String cancel = resources.getString(R.string.notification_cancel);
        final String settings = resources.getString(R.string.notification_settings);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("This app requires notification listener permissions");
        alert.setCancelable(false);
        alert.setTitle("Hey,There!");
        alert.setPositiveButton(cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(settings, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                } else {
                    startActivity(new Intent("Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            }
        });

        alertDialog = alert.create();
        alertDialog.show();
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String timeStamp = intent.getStringExtra("timeStamp");
            notifDataBaseHelper.addNotifData(new NotifData(title, text, pack, timeStamp));
        }
    };
}
