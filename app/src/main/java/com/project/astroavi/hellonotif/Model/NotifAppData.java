package com.project.astroavi.hellonotif.Model;

import java.util.Arrays;

/**
 * Created by a1274544 on 04/06/17.
 */

public class NotifAppData {

    private String appName;
    private byte[] appImage;
    private String appPackageName;

    public NotifAppData() {

    }

    public NotifAppData(String appName, byte[] appImage, String appPackageName) {
        this.appName = appName;
        this.appImage = appImage;
        this.appPackageName = appPackageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public byte[] getAppImage() {
        return appImage;
    }

    public void setAppImage(byte[] appImage) {
        this.appImage = appImage;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NotifAppData)) {
            return false;
        }
        NotifAppData other = (NotifAppData) obj;
        return appName.equals(other.appName) && appPackageName.equals(other.appPackageName) && Arrays.equals(appImage, other.appImage);
    }
}
