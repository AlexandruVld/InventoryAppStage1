package com.example.vlada.inventoryappstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vlada.inventoryappstage1.data.ProductContract.ProductEntry;
import com.example.vlada.inventoryappstage1.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ProductDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // Temporary helper method to display information in the onscreen TextView about the state of
    // the products database.
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
                ProductEntry.COLUMN_PRODUCT_AVAILABILITY,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE
        };

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // Products table in the database).
            TextView displayView = findViewById(R.id.text_view_product);
            displayView.setText(getString(R.string.table_content) + cursor.getCount() + getString(R.string.products));
            displayView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER + " - " +
                    ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " - " +
                    ProductEntry.COLUMN_PRODUCT_AVAILABILITY + " - " +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + "\n");

            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            int availabilityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhoneNumber = cursor.getInt(supplierPhoneColumnIndex);
                int currentAvailability = cursor.getInt(availabilityColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                float currentPrice = cursor.getFloat(priceColumnIndex);
                displayView.append("\n" + currentID + " - " + currentName + " - " + currentSupplier +
                        " - " + currentPhoneNumber + " - " + currentAvailability + " - " + currentQuantity + " - " + currentPrice);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertProduct() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, getResources().getString(R.string.dummy_product_name));
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, getResources().getString(R.string.dummy_supplier_name));
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, getResources().getString(R.string.dummy_supplier_phone_no));
        values.put(ProductEntry.COLUMN_PRODUCT_AVAILABILITY, ProductEntry.AVAILABILITY_IN_STOCK);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 1);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 15);

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId == -1){
            Toast.makeText(this, getResources().getString(R.string.saving_product_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.product_row_id) + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
