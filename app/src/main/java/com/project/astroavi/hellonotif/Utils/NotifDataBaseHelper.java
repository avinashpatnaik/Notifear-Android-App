package com.project.astroavi.hellonotif.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.astroavi.hellonotif.Model.NotifAppData;
import com.project.astroavi.hellonotif.Model.NotifData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Avinash on 07/04/17.
 */

public class NotifDataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;

    private static final String DATABASE_NAME = "updatedhelloNotifManager";

    private static final String TABLE_FIELDS = "updatednotifMessageData";
    private static final String TABLE_PACKAGES = "updatednotifPackageNames";

    private static final String KEY_ID = "updatedid";
    private static final String KEY_TITLE = "updatedtitle";
    private static final String KEY_MESSAGE = "updatedmessage";
    private static final String TIMESTAMP = "updatedtimestamp";
    private static final String PACKAGE_NAME = "updatedpackagename";
    private static final String PACKAGE_LIST = "updatedpackagelist";
    private static final String APP_NAME = "updatedappname";
    private static final String APP_IMAGE = "updatedappimage";

    public NotifDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NOTIF_TABLE = "CREATE TABLE " + TABLE_FIELDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_MESSAGE + " TEXT," + PACKAGE_NAME + " TEXT," + TIMESTAMP + " TEXT" + ")";
        String CREATE_PACKAGE_TABLE = "CREATE TABLE " + TABLE_PACKAGES + "("
                + PACKAGE_LIST + " TEXT," + APP_NAME + " TEXT," + APP_IMAGE + " BLOB" + ")";
        sqLiteDatabase.execSQL(CREATE_NOTIF_TABLE);
        sqLiteDatabase.execSQL(CREATE_PACKAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);
        onCreate(sqLiteDatabase);
    }

    public void addNotifData(NotifData notifData) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!checkIsDataAlreadyInDBorNot(TABLE_FIELDS, KEY_MESSAGE, notifData.getNotifMessage()) && checkIfAppSelected(notifData.getNotifPackageName())) {
            final ContentValues values = new ContentValues();
            values.put(KEY_TITLE, notifData.getNotifTitle());
            values.put(KEY_MESSAGE, notifData.getNotifMessage());
            values.put(PACKAGE_NAME, notifData.getNotifPackageName());
            values.put(TIMESTAMP, notifData.getNotifTimeStamp());
            db.insert(TABLE_FIELDS, null, values);
            db.close();
        }
    }

    public void addAppsSelected(List<NotifAppData> selectedApps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (NotifAppData s : selectedApps) {
            if (!checkIsDataAlreadyInDBorNot(TABLE_PACKAGES, PACKAGE_LIST, s.getAppPackageName())) {
                values.put(PACKAGE_LIST, s.getAppPackageName());
                values.put(APP_NAME, s.getAppName());
                values.put(APP_IMAGE, s.getAppImage());
                db.insert(TABLE_PACKAGES, null, values);
            }
        }
        db.close();
    }

    public CopyOnWriteArrayList<NotifData> getNotifDataFromPackage(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        CopyOnWriteArrayList<NotifData> notifDataList = new CopyOnWriteArrayList<>();
        String Query = "Select * from " + TABLE_FIELDS + " where " + PACKAGE_NAME + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{packageName});
        if (cursor.moveToFirst()) {
            do {
                NotifData notifData = new NotifData(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                notifDataList.add(notifData);
            } while (cursor.moveToNext());
        }
        return notifDataList;
    }

    public boolean checkIfAppSelected(String packageName) {

        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_PACKAGES + " where " + PACKAGE_LIST + " = ?";
        Cursor cursor = sqldb.rawQuery(Query, new String[]{packageName});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<NotifData> getAllNotifData() {
        List<NotifData> notifDataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FIELDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NotifData notifData = new NotifData();
                //notifData.setId(Integer.parseInt(cursor.getString(0)));
                notifData.setNotifTitle(cursor.getString(1));
                notifData.setNotifMessage(cursor.getString(2));
                notifDataList.add(notifData);
            } while (cursor.moveToNext());
        }
        return notifDataList;
    }

    public List<NotifAppData> getAllNotifAppData() {
        List<NotifAppData> notifDataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PACKAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NotifAppData notifAppData = new NotifAppData();
                //notifData.setId(Integer.parseInt(cursor.getString(0)));
                notifAppData.setAppPackageName(cursor.getString(0));
                notifAppData.setAppName(cursor.getString(1));
                notifAppData.setAppImage(cursor.getBlob(2));
                notifDataList.add(notifAppData);
            } while (cursor.moveToNext());
        }
        return notifDataList;
    }

    private boolean checkIsDataAlreadyInDBorNot(String TableName,
                                                String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = ?";
        Cursor cursor = sqldb.rawQuery(Query, new String[]{fieldValue});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getNotifDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PACKAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public int getNotifAppsDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FIELDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public void deleteNotifData(NotifAppData notifAppData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PACKAGES, PACKAGE_LIST + " = ?",
                new String[]{String.valueOf(notifAppData.getAppPackageName())});
        db.close();
    }

    public void deleteAppData(NotifData notifData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIELDS, KEY_MESSAGE + " = ?",
                new String[]{String.valueOf(notifData.getNotifMessage())});
        db.close();
    }
}
