package com.example.vlada.inventoryappstage1.data;

import android.provider.BaseColumns;

public final class ProductContract {

    private ProductContract() {}

    // Inner class that defines constant values for the products database table.
    // Each entry in the table represents a single product.
    public static final class ProductEntry implements BaseColumns {

        // Name of database table for products
        public final static String TABLE_NAME = "products";

        // Unique ID for the product
        // Integer type
        public final static String _ID = BaseColumns._ID;

        // Name of the product
        // Text type
        public final static String COLUMN_PRODUCT_NAME = "name";

        // Name of the supplier
        // Text type
        public final static String COLUMN_PRODUCT_SUPPLIER = "supplier";

        // Phone number of the supplier
        // Integer type
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "phone";

        // Availability of the product
        // Integer type
        public final static String COLUMN_PRODUCT_AVAILABILITY = "availability";

        // Quantity of the product
        // Integer type
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        // Product price
        // Real type
        public final static String COLUMN_PRODUCT_PRICE = "price";

        //Possible values for the availability of the product
        public static final int AVAILABILITY_UNKNOWN = 0;
        public static final int AVAILABILITY_IN_STOCK = 1;
        public static final int AVAILABILITY_OUT_OF_STOCK = 2;
    }
}
