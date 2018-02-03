package com.project.astroavi.hellonotif.Adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.astroavi.hellonotif.Activity.FirstScreen;
import com.project.astroavi.hellonotif.R;
import com.project.astroavi.hellonotif.Utils.AppCheckListener;
import com.project.astroavi.hellonotif.Utils.NotifDataBaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Avinash on 01/05/17.
 */

public class AppsAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;
    private AppCheckListener appCheckListener;
    private boolean[] checkedItems;
    private boolean[] alreadyCheckedItems;
    private NotifDataBaseHelper notifDataBaseHelper;
    private CheckBox checkBox;
    private boolean check;

    public AppsAdapter(Context context, int textViewResourceId,
                       List<ApplicationInfo> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
        appCheckListener = (FirstScreen) context;
        checkedItems = new boolean[appsList.size()];
        alreadyCheckedItems = new boolean[appsList.size()];
        notifDataBaseHelper = new NotifDataBaseHelper(context);
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            final LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.single_app_layout, null);
        }

        final ApplicationInfo applicationInfo = appsList.get(position);
        if (applicationInfo != null) {
            final TextView appName = (TextView) view.findViewById(R.id.app_name);
            final ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
            checkBox = (CheckBox) view.findViewById(R.id.app_checkbox);

            appName.setText(applicationInfo.loadLabel(packageManager));
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                    if (checked) {
                        checkedItems[position] = true;
                        appCheckListener.setApps(applicationInfo.packageName, true, (String) applicationInfo.loadLabel(packageManager), getDrawableInByte(applicationInfo.loadIcon(packageManager)));
                    } else {
                        if (compoundButton.isPressed()) {
                            checkedItems[position] = false;
                            appCheckListener.setApps(applicationInfo.packageName, false, (String) applicationInfo.loadLabel(packageManager), getDrawableInByte(applicationInfo.loadIcon(packageManager)));
                            if (notifDataBaseHelper.checkIfAppSelected(applicationInfo.packageName)) {
                                check = true;
                            }
                            checkBox.setOnCheckedChangeListener(null);
                        }
                    }
                }
            });

            checkBox.setChecked(checkedItems[position]);

            if (notifDataBaseHelper.checkIfAppSelected(applicationInfo.packageName) && !check) {
                checkAlreadySelectedApps(applicationInfo.packageName, position);
            }
        }
        return view;
    }

    private void checkAlreadySelectedApps(String packageName, int position) {
        if (notifDataBaseHelper.getNotifDataCount() > 0) {
            if (notifDataBaseHelper.checkIfAppSelected(packageName)) {
                alreadyCheckedItems[position] = true;
                checkBox.setChecked(alreadyCheckedItems[position]);
            }
        }
    }

    private byte[] getDrawableInByte(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
