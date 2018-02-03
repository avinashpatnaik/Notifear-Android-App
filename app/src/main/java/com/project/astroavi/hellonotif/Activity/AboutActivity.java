package com.project.astroavi.hellonotif.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.project.astroavi.hellonotif.R;

/**
 * Created by a1274544 on 17/09/17.
 */

public class AboutActivity extends AppCompatActivity {

    private TextView actionBarHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        setupActionBar();
        setActionBarHeaderTitle("About");
    }

    private void setActionBarHeaderTitle(String name) {
        actionBarHeader.setText(name);
    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_layout);
        actionBarHeader = (TextView) actionBar.getCustomView().findViewById(R.id.actionBarText);
        actionBar.setElevation(0);
    }
}
