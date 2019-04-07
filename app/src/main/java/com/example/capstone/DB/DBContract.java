package com.example.capstone.DB;

import android.provider.BaseColumns;

 final class DBContract {
    private DBContract(){

    }

    //creating tables

     static class Vendor implements BaseColumns {
         static final String TABLE_NAME = "vendor";
         static final String COL_ID = "vendor_id";
         static final String COL_NAME = "vendor_name";
         static final String COL_ADDRESS = "address2";
         static final String COL_CITY = "city2";
         static final String COL_EMAIL = "email";
         static final String COL_PASS = "password";
         static final String COL_IMAGE = "image";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASS + " TEXT, " +
                COL_IMAGE + " TEXT " + ")";
    }

     static class Customer implements BaseColumns {
         static final String TABLE_NAME = "customer";
         static final String COL_ID = "customer_id";
         static final String COL_FNAME = "first_name";
         static final String COL_LNAME = "last_name";
         static final String COL_ADDRESS = "address1";
         static final String COL_CITY = "city";
         static final String COL_PHONE = "no_hp";
         static final String COL_EMAIL = "email";
         static final String COL_PASS = "password";
         static final String COL_IMAGE = "image";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FNAME + " TEXT, " +
                COL_LNAME + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASS + " TEXT, " +
                COL_IMAGE + " TEXT " + ")";
    }

     static class Category implements BaseColumns {
         static final String TABLE_NAME = "category";
         static final String COL_ID = "category_id";
         static final String COL_NAME = "category_name";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT" + ")";

        static final String POPULATE_CATEGORY = "INSERT INTO " + TABLE_NAME +
                "( " + COL_NAME + " ) " +
                " VALUES ('Entertainment')," +
                "('Alat Musik')," +
                "('Sound System')," +
                "('Food & Beverages')," +
                "('Catering')," +
                "('Cake')," +
                "('Logistik')," +
                "('Photography & Videography')," +
                "('Decoration');";
    }

     static class Product implements BaseColumns {
         static final String TABLE_NAME = "product";
         static final String COL_ID = "product_id";
         static final String COL_NAME = "product_name";
         static final String COL_CAT_ID = "category_id";
         static final String COL_QTY = "quantity";
         static final String COL_PRICE = "price";
         static final String COL_PRODUCT_DESC = "product_desc";
         static final String COL_VENDOR_ID = "vendor_id";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
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

    static class ProductImages implements  BaseColumns {
        static final String TABLE_NAME = "product_images";
        static final String COL_ID = "product_id";
        static final String COL_IMAGE = "product_images";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COL_ID + " INTEGER, " +
                COL_IMAGE + " TEXT, " +
                "FOREIGN KEY ( " + COL_ID + " ) REFERENCES " +
                Product.TABLE_NAME + " ( " + Product.COL_ID + " ) " + " ) ";
    }

     static class Orders implements BaseColumns {
         static final String TABLE_NAME = "orders";
         static final String COL_ID = "order_id";
         static final String COL_CUST_ID = "customer_id";
         static final String COL_DATE = "order_date";
         static final String COL_TOTAL = "total_price";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUST_ID + " INTEGER, " +
                COL_DATE + " INTEGER, " +
                COL_TOTAL + " INTEGER, " +
                "FOREIGN KEY(" + COL_CUST_ID + ") REFERENCES " +
                Customer.TABLE_NAME + "(" + Customer.COL_ID + " ) " + " )";
    }

     static class Payment implements BaseColumns {
         static final String TABLE_NAME = "payment";
         static final String COL_ID = "payment_id";
         static final String COL_ORDER_ID = "order_id";
         static final String COL_PAY_TYPE = "payment_type";
         static final String COL_DATE = "payment_date";
         static final String COL_STATUS = "payment_status";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ID + " INTEGER, " +
                COL_PAY_TYPE + " TEXT, " +
                COL_DATE + " INTEGER, " +
                COL_STATUS + " BOOLEAN, " +
                "FOREIGN KEY(" + COL_ORDER_ID + ") REFERENCES " +
                Orders.TABLE_NAME + "(" + Orders.COL_ID + " ) " + " )";
    }

     static class ProdReview implements BaseColumns {
         static final String TABLE_NAME = "product_review";
         static final String COL_ID = "review_id";
         static final String COL_PROD_ID = "product_id";
         static final String COL_CUST_ID = "customer_id";
         static final String COL_PAY_ID = "payment_id";
         static final String COL_COMMENT = "isi_comment";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
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

     static class OrderList implements BaseColumns {
         static final String TABLE_NAME = "order_list";
         static final String COL_ORDER_ID = "order_id";
         static final String COL_PROD_ID = "product_id";
         static final String COL_QTY = "quantity";
         static final String COL_TOTAL = "total";

         static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
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
