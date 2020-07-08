package com.example.neighbourinneed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainChooseActivity extends AppCompatActivity {

    private Button btSearch, btCreateAd, btTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        btSearch = (Button) findViewById(R.id.main_choose_button_search);
        btCreateAd = (Button) findViewById(R.id.main_choose_button_createad);
        btTest = (Button) findViewById(R.id.main_choose_button_test);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, SearchSubcategoryActivity.class);
                startActivity(intent);
            }
        });

        btCreateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, CreateAdSubcategoryActivity.class);
                startActivity(intent);
            }
        });

        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, AdActivity.class);
                startActivity(intent);
            }
        });
       // in der CreateAdSubcategoryActivity ich bekomme ein Nullpointer auf dem button ->
       // btSearchAd = (Button) findViewById(R.id.createad_subcategory_button_search_help);
       // aber es ist so in dem xml drinnen ><


        /*btCreateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, CreateAdSubcategoryActivityOfferActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
