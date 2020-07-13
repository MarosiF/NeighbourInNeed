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

public class OfferListActivity extends AppCompatActivity implements AdapterClass.OnAdvertisementListener {
    private DatabaseReference ref;
    private ArrayList<Advertisement> list;
    private RecyclerView recyclerView;

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        ref = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        recyclerView = findViewById(R.id.recyclerViewOfferList);

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
                        //String username = Prevalent.getCurrentUser().getName();
                        //String username = Paper.book().read(Prevalent.usernamekey);
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
        startActivity(intent);
    }
}