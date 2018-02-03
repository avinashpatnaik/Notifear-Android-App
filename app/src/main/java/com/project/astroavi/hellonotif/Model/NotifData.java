package com.project.astroavi.hellonotif.Model;

/**
 * Created by a1274544 on 19/03/17.
 */

public class NotifData {

    private int id;
    private String notifTitle;
    private String notifMessage;
    private String notifPackageName;
    private String notifTimeStamp;

    public NotifData() {

    }

    public NotifData(String notifTitle, String notifMessage, String notifPackageName, String notifTimeStamp) {
        this.notifTitle = notifTitle;
        this.notifMessage = notifMessage;
        this.notifPackageName = notifPackageName;
        this.notifTimeStamp = notifTimeStamp;
    }

    public NotifData(int id, String notifTitle, String notifMessage) {
        this.id = id;
        this.notifTitle = notifTitle;
        this.notifMessage = notifMessage;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public String getNotifMessage() {
        return notifMessage;
    }

    public void setNotifMessage(String notifMessage) {
        this.notifMessage = notifMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotifPackageName() {
        return notifPackageName;
    }

    public void setNotifPackageName(String notifPackageName) {
        this.notifPackageName = notifPackageName;
    }

    public String getNotifTimeStamp() {
        return notifTimeStamp;
    }

    public void setNotifTimeStamp(String notifTimeStamp) {
        this.notifTimeStamp = notifTimeStamp;
    }
}
