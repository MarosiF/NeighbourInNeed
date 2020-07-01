package com.example.neighbourinneed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAdSubcategoryActivity extends AppCompatActivity {

    private Button btSearchAd, btOfferAd;

    private static final String search = "Search";
    private static final String offer = "Offer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createad_subcategory);

        btSearchAd = (Button) findViewById(R.id.createad_subcategory_button_search_help);
        btOfferAd = (Button) findViewById(R.id.createad_subcategory_button_offer_help);

        btSearchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivity.this, CreateAdSubcategoryActivitySearchActivity.class);
                intent.putExtra("callingActivity", search);
                startActivity(intent);
            }
        });

        btOfferAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivity.this, CreateAdSubcategoryActivityOfferActivity.class);
                intent.putExtra("callingActivity", offer);
                startActivity(intent);
            }
        });

    }
}
