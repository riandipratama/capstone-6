package com.example.capstone.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.capstone.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        db.execSQL(DBContract.ProductImages.CREATE_TABLE);
        db.execSQL(DBContract.Category.POPULATE_CATEGORY);
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
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.ProductImages.TABLE_NAME);
            db.execSQL(DBContract.Category.POPULATE_CATEGORY);
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
        values.put(DBContract.Customer.COL_IMAGE,cust.getImage());

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
        values.put(DBContract.Vendor.COL_IMAGE,vendor.getImage());

        db.insert(DBContract.Vendor.TABLE_NAME,null,values);
        db.close();
    }

    public long addProduct(Product prod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Product.COL_NAME, prod.getName());
        values.put(DBContract.Product.COL_CAT_ID, prod.getCat_id());
        values.put(DBContract.Product.COL_QTY, prod.getQty());
        values.put(DBContract.Product.COL_PRICE, prod.getPrice());
        values.put(DBContract.Product.COL_PRODUCT_DESC, prod.getDesc());
        values.put(DBContract.Product.COL_VENDOR_ID, prod.getVendor_id());

        return db.insert(DBContract.Product.TABLE_NAME,null,values);
    }

    public void addProductImages(ProductImages prodImg,List<String> imgpath) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i<imgpath.size(); i++) {
                values.put(DBContract.ProductImages.COL_ID,prodImg.getId());
                values.put(DBContract.ProductImages.COL_IMAGE,imgpath.get(i));
                db.insert(DBContract.ProductImages.TABLE_NAME,null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public boolean isUserExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] firstColumns = { DBContract.Customer.COL_ID };

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

            String[] secondColumns = { DBContract.Vendor.COL_ID };

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

    public String[] authenticateUser(String email, String pass) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] firstColumns = { DBContract.Customer.COL_ID };

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

        if(firstCursor != null && firstCursor.moveToFirst()) {
            return new String[]{"isCustomer",firstCursor.getString(firstCursor.getColumnIndex(DBContract.Customer.COL_ID))};
        } else if (!firstCursor.moveToLast()) {

            String[] secondColumns = { DBContract.Vendor.COL_ID };

            String secondSelection = DBContract.Vendor.COL_EMAIL + " = ? AND " + DBContract.Vendor.COL_PASS + " = ? ";

            Cursor secondCursor = db.query(
                    DBContract.Vendor.TABLE_NAME,
                    secondColumns,
                    secondSelection,
                    selectionArgs,
                    null,
                    null,
                    null);

            db.close();

            if(secondCursor != null && secondCursor.moveToFirst()) {
                return new String[]{"isVendor",secondCursor.getString(secondCursor.getColumnIndex(DBContract.Vendor.COL_ID))};
            }
        }

        return null;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> cat_names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionAll = {DBContract.Category.COL_ID, DBContract.Category.COL_NAME};
        Cursor cursor = db.query(
                DBContract.Category.TABLE_NAME,
                selectionAll,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Category cat = new Category(
                        cursor.getString(cursor.getColumnIndex(DBContract.Category.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Category.COL_NAME))
                );
                cat_names.add(cat);
            }
            cursor.close();
            db.close();
        }

        return  cat_names;
    }

    public Product getProduct(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product prod = new Product();

        Cursor cursor = db.query(
                DBContract.Product.TABLE_NAME,
                null,
                DBContract.Product.COL_ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()) {
            prod = new Product(
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_CAT_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_QTY)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_PRICE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_PRODUCT_DESC)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_VENDOR_ID))
            );
        }

        cursor.close();
        db.close();

        return prod;
    }

    public ArrayList<Product> getAllProducts(String type, String id) {
       ArrayList<Product> prods = new ArrayList<>();
       SQLiteDatabase db = this.getReadableDatabase();
       Product prod;
       String selection = null;

       if(type.equals("category")) {
            selection = DBContract.Category.COL_ID + " = ?";
       } else if(type.equals("vendor")) {
           selection = DBContract.Vendor.COL_ID + " = ?";
       }

       Cursor cursor = db.query(
               DBContract.Product.TABLE_NAME,
               null,
               selection,
               new String[]{id},
               null,
               null,
               null
       );

       if(cursor != null) {
           while(cursor.moveToNext()) {
               prod = new Product(
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_ID)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_NAME)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_CAT_ID)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_QTY)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_PRICE)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_PRODUCT_DESC)),
                       cursor.getString(cursor.getColumnIndex(DBContract.Product.COL_VENDOR_ID))
               );
               prods.add(prod);
           }
       }

        return  prods;
    }

    public ProductImages getProductImages(String prod_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ProductImages prodImg = new ProductImages();

        Cursor cursor = db.query(
                DBContract.ProductImages.TABLE_NAME,
                null,
                DBContract.ProductImages.COL_ID + " = ?",
                new String[]{prod_id},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            prodImg = new ProductImages(
                    cursor.getString(cursor.getColumnIndex(DBContract.ProductImages.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.ProductImages.COL_IMAGE))
            );
        }
        return prodImg;
    }

    public List<ProductImages> getAllProductImages (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ProductImages> prodList = new ArrayList<>();
        ProductImages prodImg;

        Cursor cursor = db.query(
                DBContract.ProductImages.TABLE_NAME,
                null,
                DBContract.ProductImages.COL_ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()){
                prodImg = new ProductImages(
                        cursor.getString(cursor.getColumnIndex(DBContract.ProductImages.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.ProductImages.COL_IMAGE))
                );
                prodList.add(prodImg);
            }
        }
        return prodList;
    }

    public Customer getCustomer(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor custCursor = db.query(
                DBContract.Customer.TABLE_NAME,
                null,
                DBContract.Customer.COL_ID + "= ?",
                new String[]{id},
                null,
                null,
                null
        );

        if(custCursor != null && custCursor.moveToFirst()) {
            String emailUser = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_EMAIL));
            String fname = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_FNAME));
            String lname = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_LNAME));
            String address = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_ADDRESS));
            String city = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_CITY));
            String phone = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_PHONE));
            String image = custCursor.getString(custCursor.getColumnIndex(DBContract.Customer.COL_IMAGE));
            Customer cust = new Customer(id,emailUser,fname,lname,address,city,phone,image);

            custCursor.close();
            db.close();
            return cust;
        }

        custCursor.close();
        db.close();
        return null;
    }

    public Vendor getVendor (String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor vendorCursor = db.query(
                DBContract.Vendor.TABLE_NAME,
                null,
                DBContract.Vendor.COL_ID + "= ?",
                new String[]{id},
                null,
                null,
                null
        );

        if(vendorCursor != null && vendorCursor.moveToFirst()) {
            String emailUser = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_EMAIL));
            String name = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_NAME));
            String address = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_ADDRESS));
            String city = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_CITY));
            String image = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_IMAGE));
            Vendor vendor = new Vendor(id,emailUser,name,address,city,image);

            vendorCursor.close();
            db.close();
            return vendor;
        }

        vendorCursor.close();
        db.close();
        return null;
    }

    public ArrayList<Vendor> getAllVendors(String vendor_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Vendor> vendorlist = new ArrayList<>();

        Cursor cursor = db.query(
                DBContract.Vendor.TABLE_NAME,
                null,
                DBContract.Vendor.COL_ID + " + ?",
                new String[]{vendor_id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Vendor vendor = new Vendor(
                        vendor_id,
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_CITY)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_IMAGE))

                );
                vendorlist.add(vendor);
            }
        }
        return vendorlist;
    }

    public boolean updateCustPassword(String pass, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Customer.COL_PASS,pass);
        String where = DBContract.Customer.COL_ID + "= ?";

        db.update(
                DBContract.Customer.TABLE_NAME,
                values,
                where,
                new String[] {id}
        );
        return true;
    }

    public boolean updateCustProfile(Customer cust, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Customer.COL_EMAIL,cust.getEmail());
        values.put(DBContract.Customer.COL_FNAME,cust.getFname());
        values.put(DBContract.Customer.COL_LNAME,cust.getLname());
        values.put(DBContract.Customer.COL_ADDRESS,cust.getAddress());
        values.put(DBContract.Customer.COL_CITY,cust.getCity());
        values.put(DBContract.Customer.COL_PHONE,cust.getPhone());

        String where = DBContract.Customer.COL_ID + "= ?";

        db.update(
                DBContract.Customer.TABLE_NAME,
                values,
                where,
                new String[]{id}
        );

        return true;
    }

    public boolean updateCustProfilePicture(String imgpath, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Customer.COL_IMAGE,imgpath);
        String where = DBContract.Customer.COL_ID + "= ?";

        db.update(
                DBContract.Customer.TABLE_NAME,
                values,
                where,
                new String[] {id}
        );
        return true;
    }
}
