package com.example.capstone.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rencara_database";

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
        db.execSQL(DBContract.Cart.CREATE_TABLE);
        db.execSQL(DBContract.CartList.CREATE_TABLE);
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
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Cart.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.CartList.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Product.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.ProductImages.TABLE_NAME);
            db.execSQL(DBContract.Category.POPULATE_CATEGORY);
        }
        onCreate(db);
    }

    // Add Operations Start Here

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
        values.put(DBContract.Vendor.COL_PHONE,vendor.getPhone());
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

    public void addProductImages(ProductImages prodImg,String imgpath) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
                values.put(DBContract.ProductImages.COL_ID,prodImg.getId());
                values.put(DBContract.ProductImages.COL_IMAGE,imgpath);
                db.insert(DBContract.ProductImages.TABLE_NAME,null, values);

        db.close();
    }

    public long addCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Cart.COL_CUST_ID, cart.getCust_id());

        return db.insert(DBContract.Cart.TABLE_NAME, null, values);
    }

    public boolean addCartList(CartList cl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.CartList.COL_CART_ID, cl.getCart_id());
        values.put(DBContract.CartList.COL_PROD_ID, cl.getProd_id());
        values.put(DBContract.CartList.COL_ORDER_ID,cl.getOrder_id());
        values.put(DBContract.CartList.COL_QTY, cl.getQty());
        values.put(DBContract.CartList.COL_TOTAL, cl.getTotal());
        values.put(DBContract.CartList.COL_ORDER_DATE, cl.getOrder_date());

        if(db.insert(DBContract.CartList.TABLE_NAME, null, values) > 0) {
            db.close();
            return  true;
        } else {
            db.close();
            return  false;
        }
    }

    public String addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insertQuery = "INSERT INTO " +
                DBContract.Orders.TABLE_NAME + "(" +
                DBContract.Orders.COL_CART_ID + "," + DBContract.Orders.COL_CUST_ID + "," + DBContract.Orders.COL_TOTAL + "," + DBContract.Orders.COL_ORDER_MADE_DATE + ") " +
                " VALUES (?,?,?,datetime('now','localtime'))";

        String[] values = new String[]{order.getCart_id(),order.getCust_id(),order.getTotal_price()};
        db.execSQL(insertQuery,values);

        Cursor res = db.rawQuery("SELECT last_insert_rowid() AS orderid FROM " + DBContract.Orders.TABLE_NAME,null);

        if(res != null && res.moveToFirst()) {
            String  order_id = res.getString(res.getColumnIndex("orderid"));

            res.close();

            return order_id;
        }

        res.close();
        return null;
    }

    public String addPayment(Payment pay) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insertQuery = "INSERT INTO " +
                DBContract.Payment.TABLE_NAME + "(" +
                DBContract.Payment.COL_ORDER_ID + "," + DBContract.Payment.COL_PAY_TYPE + "," + DBContract.Payment.COL_DATE + "," + DBContract.Payment.COL_STATUS + ") " +
                " VALUES (?,?,datetime('now','localtime'),?)";

        String[] values = new String[]{pay.getOrder_id(),pay.getType(),pay.getStatus()};
        db.execSQL(insertQuery,values);

        Cursor res = db.rawQuery("SELECT last_insert_rowid() AS payid FROM " + DBContract.Payment.TABLE_NAME,null);

        if(res != null && res.moveToFirst()) {
            String pay_id = res.getString(res.getColumnIndex("payid"));

            res.close();

            return pay_id;
        }

        res.close();

        return null;

    }

    public boolean addReview(Review rev) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.ProdReview.COL_PROD_ID,rev.getProd_id());
        values.put(DBContract.ProdReview.COL_CUST_ID,rev.getCust_id());
        values.put(DBContract.ProdReview.COL_PAY_ID,rev.getPay_id());
        values.put(DBContract.ProdReview.COL_COMMENT,rev.getComment());

        return db.insert(DBContract.ProdReview.TABLE_NAME, null, values) > 0;
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

            return cursorSecondCount > 0;
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

            if(secondCursor != null && secondCursor.moveToFirst()) {
                db.close();
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
            String phone = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_PHONE));
            String image = vendorCursor.getString(vendorCursor.getColumnIndex(DBContract.Vendor.COL_IMAGE));
            Vendor vendor = new Vendor(id,emailUser,name,address,city,phone,image);

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
                DBContract.Vendor.COL_ID + " = ?",
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
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_PHONE)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Vendor.COL_IMAGE)),
                        null

                );
                vendorlist.add(vendor);
            }
        }
        return vendorlist;
    }

    public Cart getCart(String cust_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cart cart = new Cart();

        Cursor cursor = db.query(
                DBContract.Cart.TABLE_NAME,
                null,
                DBContract.Cart.COL_CUST_ID + " = ?",
                new String[] {cust_id},
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToLast()) {
            cart = new Cart(
                    cursor.getString(cursor.getColumnIndex(DBContract.Cart.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Cart.COL_CUST_ID))
            );

            cursor.close();
            db.close();

            return cart;
        }

        cursor.close();
        db.close();

        return cart;
    }

    public CartList getCartList(String prod_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        CartList cl = new CartList();

        Cursor cursor = db.query(
                DBContract.CartList.TABLE_NAME,
                null,
                DBContract.CartList.COL_PROD_ID + " = ?",
                new String[]{prod_id},
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()) {
            cl = new CartList(
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_CART_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_PROD_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_QTY)),
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_TOTAL)),
                    cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_ORDER_DATE))
            );
            cursor.close();
            db.close();
            return cl;
        }
        cursor.close();
        db.close();
        return cl;
    }

    public List<Cart> getAllParticularCarts(String cust_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Cart> cList = new ArrayList<>();

        Cursor cursor = db.query(
                DBContract.Cart.TABLE_NAME,
                null,
                DBContract.Cart.COL_CUST_ID + "= ?",
                new String[]{cust_id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Cart cart = new Cart(
                        cursor.getString(cursor.getColumnIndex(DBContract.Cart.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Cart.COL_CUST_ID))
                );
                cList.add(cart);
            }
        }
        cursor.close();
        db.close();

        return cList;
    }

    public List<CartList> getAllParticularCartList(String id, int type) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CartList> cList = new ArrayList<>();
        String selection = null;

        switch(type) {
            case 1 :
                selection = DBContract.CartList.COL_CART_ID + " = ?";
                break;
            case 2 :
                selection = DBContract.CartList.COL_ORDER_ID + " = ?";
                break;
            case 3:
                selection = DBContract.CartList.COL_PROD_ID + " = ?";
                break;
        }

        Cursor cursor = db.query(
                DBContract.CartList.TABLE_NAME,
                null,
                selection,
                new String[]{id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                CartList cl = new CartList(
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_CART_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_PROD_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_QTY)),
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_TOTAL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.CartList.COL_ORDER_DATE))
                );
                cList.add(cl);
            }
            cursor.close();
            db.close();
            return cList;
        }
        return null;
    }

    public boolean checkCartListItem(String cart_id, String prod_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " +
                DBContract.CartList.TABLE_NAME +
                " WHERE " + DBContract.CartList.COL_CART_ID + " = ?" +
                " AND " + DBContract.CartList.COL_PROD_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cart_id,prod_id});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Order getOrder(String id, int type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = new Order();
        String selection = null;

        switch(type){
            case 1:
                selection = DBContract.Orders.COL_CUST_ID + " =?";
                break;
            case 2:
                selection = DBContract.Orders.COL_ID + " = ?";
                break;
        }

        Cursor cursor = db.query(
                DBContract.Orders.TABLE_NAME,
                null,
                selection,
                new String[] {id},
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()) {
            order = new Order(
                    cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_CUST_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_CART_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_TOTAL)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_ORDER_MADE_DATE))
            );
            cursor.close();
            db.close();

            return order;
        }
        cursor.close();
        db.close();

        return order;
    }

    public List<Order> getAllParticularOrders(String cust_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Order> oList = new ArrayList<>();

        Cursor cursor = db.query(
                DBContract.Orders.TABLE_NAME,
                null,
                DBContract.Orders.COL_CUST_ID + "= ?",
                new String[]{cust_id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Order order = new Order(
                        cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_CUST_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_CART_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_TOTAL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.Orders.COL_ORDER_MADE_DATE))
                );
                oList.add(order);
            }
        }
        cursor.close();
        db.close();

        return oList;
    }

    public Payment getStatusPayment(String order_id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Payment pay = new Payment();

        Cursor cursor = db.query(
                DBContract.Payment.TABLE_NAME,
                null,
                DBContract.Payment.COL_ORDER_ID + " = ? AND " + DBContract.Payment.COL_STATUS + " = ?",
                new String[]{order_id,status},
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()) {
            pay = new Payment(
                    cursor.getString(cursor.getColumnIndex(DBContract.Payment.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Payment.COL_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Payment.COL_PAY_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Payment.COL_DATE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Payment.COL_STATUS))
            );

            cursor.close();
            db.close();
            return pay;
        }
        cursor.close();
        db.close();

        return null;
    }

    public List<Review> getAllParticularReview(String prod_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Review> rList = new ArrayList<>();

        Cursor cursor = db.query(
                DBContract.ProdReview.TABLE_NAME,
                null,
                DBContract.ProdReview.COL_PROD_ID + "= ?",
                new String[]{prod_id},
                null,
                null,
                null
        );

        if(cursor != null) {
            while(cursor.moveToNext()) {
                Review rev  = new Review(
                        cursor.getString(cursor.getColumnIndex(DBContract.ProdReview.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.ProdReview.COL_PROD_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.ProdReview.COL_CUST_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.ProdReview.COL_PAY_ID)),
                        cursor.getString(cursor.getColumnIndex(DBContract.ProdReview.COL_COMMENT))
                );
                rList.add(rev);
            }
        }
        cursor.close();
        db.close();

        return rList;
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

    public boolean updateVendorPassword(String pass, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Vendor.COL_PASS,pass);
        String where = DBContract.Vendor.COL_ID + "= ?";

        db.update(
                DBContract.Vendor.TABLE_NAME,
                values,
                where,
                new String[] {id}
        );
        return true;
    }

    public boolean updateVendorProfile(Vendor vendor, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Vendor.COL_EMAIL,vendor.getEmail());
        values.put(DBContract.Vendor.COL_NAME,vendor.getName());
        values.put(DBContract.Vendor.COL_ADDRESS,vendor.getAddress());
        values.put(DBContract.Vendor.COL_CITY,vendor.getCity());
        values.put(DBContract.Vendor.COL_PHONE,vendor.getPhone());

        String where = DBContract.Vendor.COL_ID + "= ?";

        db.update(
                DBContract.Vendor.TABLE_NAME,
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

    public void updateCartListOrderId(String cart_id, String order_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.CartList.COL_ORDER_ID,order_id);

        db.update(
          DBContract.CartList.TABLE_NAME,
          values,
          DBContract.CartList.COL_CART_ID + " = ?",
          new String[]{cart_id}
        );
    }

    public void updateQtyAndTotalCartList(String cart_id, String qty, String total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(DBContract.CartList.COL_QTY,qty);
        values.put(DBContract.CartList.COL_TOTAL,total);

        db.update(
                DBContract.CartList.TABLE_NAME,
                values,
                DBContract.CartList.COL_CART_ID + " =?",
                new String[] {cart_id}
        );
    }

    public void updatePayStatus(String pay_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Payment.COL_STATUS,status);

        db.update(
                DBContract.Payment.TABLE_NAME,
                values,
                DBContract.Payment.COL_ID + " = ?",
                new String[] {pay_id}
        );

    }

    public boolean deleteProduct(String prod_id) {
        return this.getWritableDatabase().delete(
                DBContract.Product.TABLE_NAME,
                DBContract.Product.COL_ID + "= ?",
                new String[]{prod_id}
        ) > 0;
    }

    public boolean deleteCartList(String cart_id) {
        return this.getWritableDatabase().delete(
                DBContract.CartList.TABLE_NAME,
                DBContract.CartList.COL_CART_ID + "= ?",
                new String[] {cart_id}
        ) > 0;
    }

    public boolean deleteCart(String cart_id) {
        return this.getWritableDatabase().delete(
                DBContract.Cart.TABLE_NAME,
                DBContract.Cart.COL_ID + "= ?",
                new String[] {cart_id}
        ) > 0;
    }
}
