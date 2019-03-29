package com.example.capstone.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rencara_database";

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.Vendor.CREATE_TABLE);
        db.execSQL(DBContract.Customer.CREATE_TABLE);
        db.execSQL(DBContract.Category.CREATE_TABLE);
        db.execSQL(DBContract.Orders.CREATE_TABLE);
        db.execSQL(DBContract.Payment.CREATE_TABLE);
        db.execSQL(DBContract.OrderList.CREATE_TABLE);
        db.execSQL(DBContract.ProdReview.CREATE_TABLE);
        db.execSQL(DBContract.Product.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( oldVersion == 0 && newVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Vendor.TABLE_NAME);
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Vendor.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Customer.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Category.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Orders.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Payment.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.OrderList.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Product.TABLE_NAME);
        }
        onCreate(db);
    }

    public void addCust(Customer cust) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Customer.COL_EMAIL,cust.getEmail());
        values.put(DBContract.Customer.COL_PASS,cust.getPass());
        values.put(DBContract.Customer.COL_FNAME,cust.getFname());
        values.put(DBContract.Customer.COL_LNAME,cust.getLname());
        values.put(DBContract.Customer.COL_ADDRESS,cust.getAddress());
        values.put(DBContract.Customer.COL_CITY,cust.getCity());
        values.put(DBContract.Customer.COL_PHONE,cust.getPhone());

        db.insert(DBContract.Customer.TABLE_NAME,null,values);
        db.close();
    }

    public void addVendor(Vendor vendor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Vendor.COL_EMAIL,vendor.getEmail());
        values.put(DBContract.Vendor.COL_PASS,vendor.getPass());
        values.put(DBContract.Vendor.COL_NAME,vendor.getName());
        values.put(DBContract.Vendor.COL_ADDRESS,vendor.getAddress());
        values.put(DBContract.Vendor.COL_CITY,vendor.getCity());

        db.insert(DBContract.Vendor.TABLE_NAME,null,values);
        db.close();
    }

    public boolean isUserExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] firstColumns = { DBContract.Customer.COL_EMAIL };

        String firstSelection = DBContract.Customer.COL_EMAIL + " = ? ";

        String[] selectionArgs = {email};

        Cursor firstCursor = db.query(
                DBContract.Customer.TABLE_NAME,
                firstColumns,
                firstSelection,
                selectionArgs,
                null,
                null,
                null);

        int cursorFirstCount = firstCursor.getCount();
        firstCursor.close();

        if(cursorFirstCount > 0 ) {
            return true;
        } else if (cursorFirstCount == 0) {

            String[] secondColumns = { DBContract.Vendor.COL_EMAIL };

            String secondSelection = DBContract.Vendor.COL_EMAIL + " = ? ";

            Cursor secondCursor = db.query(
                    DBContract.Vendor.TABLE_NAME,
                    secondColumns,
                    secondSelection,
                    selectionArgs,
                    null,
                    null,
                    null);

            int cursorSecondCount = secondCursor.getCount();
            secondCursor.close();
            db.close();

            if(cursorSecondCount > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean authenticateUser(String email, String pass) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] firstColumns = { DBContract.Customer.COL_EMAIL, DBContract.Customer.COL_PASS };

        String firstSelection = DBContract.Customer.COL_EMAIL + " = ? AND " + DBContract.Customer.COL_PASS + " = ? ";

        String[] selectionArgs = {email,pass};

        Cursor firstCursor = db.query(
                DBContract.Customer.TABLE_NAME,
                firstColumns,
                firstSelection,
                selectionArgs,
                null,
                null,
                null);

        int cursorFirstCount = firstCursor.getCount();
        firstCursor.close();

        if(cursorFirstCount > 1 ) {
            return true;
        } else if (cursorFirstCount == 0 ) {

            String[] secondColumns = { DBContract.Vendor.COL_EMAIL, DBContract.Vendor.COL_PASS };

            String secondSelection = DBContract.Vendor.COL_EMAIL + " = ? AND " + DBContract.Vendor.COL_PASS + " = ? ";

            Cursor secondCursor = db.query(
                    DBContract.Vendor.TABLE_NAME,
                    secondColumns,
                    secondSelection,
                    selectionArgs,
                    null,
                    null,
                    null);

            int cursorSecondCount = secondCursor.getCount();
            secondCursor.close();
            db.close();

            if(cursorSecondCount == 1 ) {
                return true;
            }
        }

        return false;
    }
}
