package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateAdSubcategoryActivityOfferActivity extends AppCompatActivity {

    private Button btGift, btLend, btOfferHelp;

    private static final String gift = "Gift";
    private static final String borrowLend = "Loan";
    private static final String searchHelp = "Help";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createad_subcategory_offer);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigation();

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

    private void bottomNavigation() {
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(CreateAdSubcategoryActivityOfferActivity.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(CreateAdSubcategoryActivityOfferActivity.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(CreateAdSubcategoryActivityOfferActivity.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_home:
                        Intent intent3 = new Intent(CreateAdSubcategoryActivityOfferActivity.this, MainChooseActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent4 = new Intent(CreateAdSubcategoryActivityOfferActivity.this, UserSetting.class);
                        startActivity(intent4);
                        return true;

                }
                return false;
            }
        });
    }
}
