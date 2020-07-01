package com.example.neighbourinneed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAdSubcategoryActivityOfferActivity extends AppCompatActivity {

    private Button btGift, btLend, btOfferHelp;

    private static final String gift = "Gift";
    private static final String borrowLend = "BorrowOrLend";
    private static final String searchHelp = "Help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createad_subcategory_offer);

        btGift = (Button) findViewById(R.id.createad_subcategory_offer_button_gift);
        btLend = (Button) findViewById(R.id.createad_subcategory_offer_button_lend);
        btOfferHelp = (Button) findViewById(R.id.createad_subcategory_offer_button_offer);

        final String callingActivity = getIntent().getStringExtra("callingActivity");

        btGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivityOfferActivity.this, CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", gift);
                startActivity(intent);
            }
        });

        btLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivityOfferActivity.this,CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", borrowLend);
                startActivity(intent);
            }
        });

        btOfferHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivityOfferActivity.this, CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", searchHelp);
                startActivity(intent);
            }
        });
    }
}
