package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The Main Choose Activity for the Application
 * @author Ebru Özcelik,Fanni Marosi
 * @version 1.0
 * This is the Screen the user sees after successful Login
 */
public class MainChooseActivity extends AppCompatActivity {

    /**
     * Buttons for the user to see
     */
    private Button btSearch, btCreateAd, btTest;

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(MainChooseActivity.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(MainChooseActivity.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(MainChooseActivity.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent3 = new Intent(MainChooseActivity.this, UserSetting.class);
                        startActivity(intent3);
                        return true;

                }
                return false;
            }
        });


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

    }
}
