package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.neighbourinneed.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.paperdb.Paper;
/**
 * The OfferList Activity for the Application
 * @author Ebru Özcelik,Fanni Marosi
 * @version 1.0
 * This is the Screen the user sees after clicking the Search button in the Main Choose Activity
 */
public class OfferListActivity extends AppCompatActivity implements AdapterClass.OnAdvertisementListener {

    /**
     * Views and Buttons for the user to see
     */
    private DatabaseReference ref;
    private ArrayList<Advertisement> list;
    /**
     * Views for the user to see
     */
    private RecyclerView recyclerView;

    /**
     * Bottom navigation
     */
    private BottomNavigationView bottomNavigationView;

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigation();

        ref = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        recyclerView = findViewById(R.id.recyclerViewOfferList);

    }

    /**
     * The Method for the user to interact with the bottomnavigation.
     */
    private void bottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_list_offer_icon);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(OfferListActivity.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(OfferListActivity.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_home:
                        Intent intent2 = new Intent(OfferListActivity.this, MainChooseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent3 = new Intent(OfferListActivity.this, UserSetting.class);
                        startActivity(intent3);
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        showOwnAdvertisements();
    }

    private void showOwnAdvertisements() {
        final String username = Prevalent.currentUser.getName();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<Advertisement>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (Objects.requireNonNull(ds.child("user").getValue()).toString().equals(username)) {
                                list.add(ds.getValue(Advertisement.class));
                            }
                        }
                        AdapterClass adapterClass = new AdapterClass(list, OfferListActivity.this);
                        recyclerView.setAdapter(adapterClass);
                        if (list.size() == 0) {
                            Toast.makeText(OfferListActivity.this, "You haven't created any advertisements yet", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError);
                    Toast.makeText(OfferListActivity.this, "Sorry, there was an error with the database connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onAdvertisementClick(int position, String nameAd) {

        Intent intent = new Intent(OfferListActivity.this, AdActivity.class);
        intent.putExtra("advertisement", nameAd);
        intent.putExtra("callingActivity", "OfferList");
        startActivity(intent);
    }
}