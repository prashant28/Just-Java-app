package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked"
     */
    public void increment(View view) {

        if (quantity == 100) {
            // show an error message as a toast
            Toast.makeText(this, getString(R.string.more_than_limit_coffee), Toast.LENGTH_SHORT).show();
            //exit early because there is nothing to do here
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);

    }

    /**
     * This method is called when the mius button is clicked"
     */
    public void decrement(View view) {
        if (quantity == 1) {
            //// show an error message as a toast
            Toast toast = Toast.makeText(this, getString(R.string.less_than_limit_coffee), Toast.LENGTH_SHORT);
            toast.show();
            ////exit early because there is nothing to do here
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);

    }

    /***
     * Calculate the price of the order
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // price of 1 cup of coffee
        int basePrice = 5;

        // price for 1 cup of coffee if whipped cream is added
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        // price for 1 cup of coffee if chocolate is added
        if (addChocolate) {
            basePrice = basePrice + 2;
        }
        return basePrice * quantity;
    }

    /***
     * This method gives the order summary
     *
     * @param price           of the order
     * @param addWhippedCream adding the topping
     * @param addChocolate    adding the topping
     * @param name            name of the customer
     * @return order summary
     */

    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.total,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get the name of the customer
        EditText nameField = (EditText) findViewById(R.id.name_editText);
        String name = nameField.getText().toString();

        //figure out if the customer want whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // figure out if the customer wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        // Sending the order summary via email to customer
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email app should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}