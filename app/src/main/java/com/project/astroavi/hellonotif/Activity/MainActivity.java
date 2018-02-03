package com.project.astroavi.hellonotif.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.astroavi.hellonotif.Adapter.NotifAdapter;
import com.project.astroavi.hellonotif.Model.NotifData;
import com.project.astroavi.hellonotif.R;
import com.project.astroavi.hellonotif.Utils.NotifDataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {

    private static final long lifeTime = 259200000;

    private RecyclerView recyclerView;
    private TextView noNotifText;
    private NotifDataBaseHelper db = new NotifDataBaseHelper(this);
    private TextView actionBarHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noNotifText = (TextView) findViewById(R.id.no_notif_text);
        setupActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActionBarHeaderTitle(getIntent().getStringExtra("appName"));
        ArrayList<NotifData> notifDataArrayList = new ArrayList<>();
        CopyOnWriteArrayList<NotifData> notifDataList = db.getNotifDataFromPackage(getIntent().getStringExtra("packageName"));
        notifDataArrayList.addAll(notifDataList);
        for (NotifData notifData : notifDataList) {
            if ((System.currentTimeMillis() - Long.valueOf(notifData.getNotifTimeStamp())) >= lifeTime) {
                db.deleteAppData(notifData);
                notifDataList.remove(notifData);
            }
        }
        if (notifDataList.size() != 0) {
            Collections.reverse(notifDataArrayList);
            final NotifAdapter notifAdapter = new NotifAdapter(this, notifDataArrayList);
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            recyclerView.setAdapter(notifAdapter);
            notifAdapter.notifyDataSetChanged();
        } else {
            noNotifText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch (item.getItemId()) {
            case R.id.menu_edit: {
                Intent intent = new Intent(MainActivity.this, FirstScreen.class);
                intent.putExtra("EditApps", true);
                startActivity(intent);
                break;
            }
            case R.id.about: {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
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

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_layout);
        actionBarHeader = (TextView) actionBar.getCustomView().findViewById(R.id.actionBarText);
        actionBar.setElevation(0);
    }

    private void setActionBarHeaderTitle(String name) {
        actionBarHeader.setText(name);
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
