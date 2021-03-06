package com.example.vlada.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.vlada.inventoryappstage1.data.ProductContract.ProductEntry;
import com.example.vlada.inventoryappstage1.data.ProductDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;

    // EditText field to enter the supplier name
    private EditText mSupplierEditText;

    // EditText field to enter the supplier phone number
    private EditText mSupplierPhoneEditText;

    // EditText field to enter the product availability
    private Spinner mAvailabilitySpinner;

    // EditText field to enter the product quantity
    private EditText mQuantityEditText;

    //EditText field to enter the product price
    private EditText mPriceEditText;

    // Availability of the product. The possible values are in ProductContract.java file
    private int mAvailability = ProductEntry.AVAILABILITY_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_product_name);
        mSupplierEditText = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone_number);
        mAvailabilitySpinner = findViewById(R.id.spinner_availability);
        mQuantityEditText = findViewById(R.id.edit_product_quantity);
        mPriceEditText = findViewById(R.id.edit_product_price);

        setupSpinner();
    }

    // Setup the dropdown spinner that allows the user to select the availability of the product
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter availabilitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_availability_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        availabilitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mAvailabilitySpinner.setAdapter(availabilitySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mAvailabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.availability_yes))) {
                        mAvailability = ProductEntry.AVAILABILITY_IN_STOCK;
                    } else if (selection.equals(getString(R.string.availability_no))) {
                        mAvailability = ProductEntry.AVAILABILITY_OUT_OF_STOCK;
                    } else {
                        mAvailability = ProductEntry.AVAILABILITY_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAvailability = ProductEntry.AVAILABILITY_UNKNOWN;
            }
        });
    }

    private void insertProduct(){
        String nameString = mNameEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneEditText.getText().toString().trim();
        int supplierPhoneNumber = Integer.parseInt(supplierPhoneNumberString);
        String quantityString = mQuantityEditText.getText().toString().trim();
        int productQuantity = Integer.parseInt(quantityString);
        String priceString = mPriceEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(priceString);

        ProductDbHelper mDbHelper = new ProductDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierString);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);
        values.put(ProductEntry.COLUMN_PRODUCT_AVAILABILITY, mAvailability);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId == -1){
            Toast.makeText(this, getResources().getString(R.string.saving_product_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.product_row_id) + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertProduct();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
