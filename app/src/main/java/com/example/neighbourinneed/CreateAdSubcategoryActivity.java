package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The Create Ad Subcategory Search Activity for the Application
 * @author Ebru Özcelik,Fanni Marosi
 * @version 1.0
 * This is the Screen the user sees after clicking the Create Ad button in the Main Choose Activity
 */
public class CreateAdSubcategoryActivity extends AppCompatActivity {

    private Button btSearchAd, btOfferAd;

    private static final String search = "Search";
    private static final String offer = "Offer";

    private BottomNavigationView bottomNavigationView;

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createad_subcategory);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigation();

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

    /**
     * The Method for the user to interact with the bottomnavigation.
     */
    private void bottomNavigation() {
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(CreateAdSubcategoryActivity.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(CreateAdSubcategoryActivity.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(CreateAdSubcategoryActivity.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_home:
                        Intent intent3 = new Intent(CreateAdSubcategoryActivity.this, MainChooseActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent4 = new Intent(CreateAdSubcategoryActivity.this, UserSetting.class);
                        startActivity(intent4);
                        return true;

                }
                return false;
            }
        });
    }
}
