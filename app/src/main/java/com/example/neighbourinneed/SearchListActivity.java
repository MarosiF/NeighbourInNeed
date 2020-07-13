package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchListActivity extends AppCompatActivity {

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_list_search_icon);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(SearchListActivity.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SearchListActivity.this, MainChooseActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(SearchListActivity.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent3 = new Intent(SearchListActivity.this, UserSetting.class);
                        startActivity(intent3);
                        return true;

                }
                return false;
            }
        });

    }
}