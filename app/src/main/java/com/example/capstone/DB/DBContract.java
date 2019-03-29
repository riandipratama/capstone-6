package com.example.capstone.DB;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract(){

    }

    //creating tables

    public static class Vendor implements BaseColumns {
        public static final String TABLE_NAME = "vendor";
        public static final String COL_ID = "vendor_id";
        public static final String COL_NAME = "vendor_name";
        public static final String COL_ADDRESS = "address2";
        public static final String COL_CITY = "city2";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASS = "password";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASS + " TEXT " + ")";
    }

    public static class Customer implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        public static final String COL_ID = "customer_id";
        public static final String COL_FNAME = "first_name";
        public static final String COL_LNAME = "last_name";
        public static final String COL_ADDRESS = "address1";
        public static final String COL_CITY = "city";
        public static final String COL_PHONE = "no_hp";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASS = "password";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FNAME + " TEXT, " +
                COL_LNAME + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASS + " TEXT " + ")";
    }

    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COL_ID = "category_id";
        public static final String COL_NAME = "category_name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT" + ")";
    }

    public static class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COL_ID = "product_id";
        public static final String COL_NAME = "product_name";
        public static final String COL_CAT_ID = "category_id";
        public static final String COL_QTY = "quantity";
        public static final String COL_PRICE = "price";
        public static final String COL_PRODUCT_DESC = "product_desc";
        public static final String COL_VENDOR_ID = "vendor_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_CAT_ID + " INTEGER, " +
                COL_QTY + " INTEGER, " +
                COL_PRICE + " INTEGER, " +
                COL_PRODUCT_DESC + " TEXT, " +
                COL_VENDOR_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_CAT_ID + ") REFERENCES " +
                Category.TABLE_NAME + "(" + Category.COL_ID + " ), " +
                "FOREIGN KEY(" + COL_VENDOR_ID + ") REFERENCES " +
                Vendor.TABLE_NAME + "(" + Vendor.COL_ID + ") " + ")";
    }

    public static class Orders implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COL_ID = "order_id";
        public static final String COL_CUST_ID = "customer_id";
        public static final String COL_DATE = "order_date";
        public static final String COL_TOTAL = "total_price";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUST_ID + " INTEGER, " +
                COL_DATE + " INTEGER, " +
                COL_TOTAL + " INTEGER, " +
                "FOREIGN KEY(" + COL_CUST_ID + ") REFERENCES " +
                Customer.TABLE_NAME + "(" + Customer.COL_ID + " ) " + " )";
    }

    public static class Payment implements BaseColumns {
        public static final String TABLE_NAME = "payment";
        public static final String COL_ID = "payment_id";
        public static final String COL_ORDER_ID = "order_id";
        public static final String COL_PAY_TYPE = "payment_type";
        public static final String COL_DATE = "payment_date";
        public static final String COL_STATUS = "payment_status";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ID + " INTEGER, " +
                COL_PAY_TYPE + " TEXT, " +
                COL_DATE + " INTEGER, " +
                COL_STATUS + " BOOLEAN, " +
                "FOREIGN KEY(" + COL_ORDER_ID + ") REFERENCES " +
                Orders.TABLE_NAME + "(" + Orders.COL_ID + " ) " + " )";
    }

    public static class ProdReview implements BaseColumns {
        public static final String TABLE_NAME = "product_review";
        public static final String COL_ID = "review_id";
        public static final String COL_PROD_ID = "product_id";
        public static final String COL_CUST_ID = "customer_id";
        public static final String COL_PAY_ID = "payment_id";
        public static final String COL_COMMENT = "isi_comment";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_ID + " INTEGER, " +
                COL_CUST_ID + " INTEGER, " +
                COL_PAY_ID + " INTEGER, " +
                COL_COMMENT + " TEXT, " +
                "FOREIGN KEY(" + COL_PROD_ID + ") REFERENCES " +
                Product.TABLE_NAME + "(" + Product.COL_ID + " ), " +
                "FOREIGN KEY(" + COL_PAY_ID + ") REFERENCES " +
                Payment.TABLE_NAME + "(" + Payment.COL_ID + " ), " +
                "FOREIGN KEY(" + COL_CUST_ID + ") REFERENCES " +
                Customer.TABLE_NAME + "(" + Customer.COL_ID + " ) " + " )";
    }

    public static class OrderList implements BaseColumns {
        public static final String TABLE_NAME = "order_list";
        public static final String COL_ORDER_ID = "order_id";
        public static final String COL_PROD_ID = "product_id";
        public static final String COL_QTY = "quantity";
        public static final String COL_TOTAL = "total";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ORDER_ID + " INTEGER NOT NULL, " +
                COL_PROD_ID + " INTEGER NOT NULL, " +
                COL_QTY + " INTEGER, " +
                COL_TOTAL + " INTEGER, " +
                "PRIMARY KEY ( " + COL_ORDER_ID + " , " + COL_PROD_ID + " ), " +
                "FOREIGN KEY(" + COL_PROD_ID + ") REFERENCES " +
                Product.TABLE_NAME + "(" + Product.COL_ID + " ), " +
                "FOREIGN KEY(" + COL_ORDER_ID + ") REFERENCES " +
                Orders.TABLE_NAME + "(" + Orders.COL_ID + " ) " + " )";
    }

}
