package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchSubcategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterClass.OnAdvertisementListener{
    private DatabaseReference ref;
    private ArrayList<Advertisement> list;
    private ArrayList<Advertisement> searchList;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private String [] mainCategories = {"All", "Search", "Offer"};
    private String [] subCategories = {"All", "Gift", "Loan", "Help"};
    private String all = "All";

    private String currentMainCategory = "All";
    private String currentSubCategory = "All";

    /**
     * Initialize activity
     * @param savedInstanceState The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subcategory);

        //nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_loupe);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(SearchSubcategoryActivity.this, MainChooseActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(SearchSubcategoryActivity.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(SearchSubcategoryActivity.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_profile_icon:
                        Intent intent3 = new Intent(SearchSubcategoryActivity.this, UserSetting.class);
                        startActivity(intent3);
                        return true;

                }
                return false;
            }
        });


        ref = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        Spinner spin = findViewById(R.id.spinner);
        Spinner spin2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainCategories);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        spin2.setAdapter(adapter2);
        spin2.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {

        super.onStart();
        showAdvertisements();
    }

    private void showAdvertisements() {

        if (ref != null) {

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<Advertisement>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String mainCategoryInDB = ds.child("mainCategory").getValue().toString();
                            String subcategoryInDB = ds.child("subCategory").getValue().toString();
                            if (mainCategoryInDB != null && subcategoryInDB != null) {
                                if (currentSubCategory.equals(all) && currentMainCategory.equals(all)) {
                                    list.add(ds.getValue(Advertisement.class));
                                }
                                else if (currentMainCategory.equals(all) && !(currentSubCategory.equals(all))) {
                                    if (subcategoryInDB.equals(currentSubCategory)) {
                                        list.add(ds.getValue(Advertisement.class));
                                    }
                                }
                                else if (currentSubCategory.equals(all) && !(currentMainCategory.equals(all))) {
                                    if (mainCategoryInDB.equals(currentMainCategory)) {
                                        list.add(ds.getValue(Advertisement.class));
                                    }
                                }
                                else if (mainCategoryInDB.equals(currentMainCategory) && subcategoryInDB.equals(currentSubCategory)) {
                                    list.add(ds.getValue(Advertisement.class));
                                }
                            }
                        }
                        AdapterClass adapterClass = new AdapterClass(list, SearchSubcategoryActivity.this);
                        recyclerView.setAdapter(adapterClass);
                        if (list.size() == 0) {
                            Toast.makeText(SearchSubcategoryActivity.this, "No advertisements found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SearchSubcategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }

    private void search(String s) {
        searchList = new ArrayList<Advertisement>();
        try {
            for (Advertisement ad : list) {
                if (ad.getCity().toLowerCase().contains(s.toLowerCase())) {
                    searchList.add(ad);
                }
            }
            AdapterClass adapterClass = new AdapterClass(searchList, this);
            recyclerView.setAdapter(adapterClass);
        } catch(NullPointerException e) {
          System.out.println(Arrays.toString(e.getStackTrace()));
        }
        if (searchList.size() == 0) {
            Toast.makeText(SearchSubcategoryActivity.this, "No advertisements found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId)
        {
            case R.id.spinner:
                currentMainCategory = mainCategories[position];
                break;
            case R.id.spinner2:
                currentSubCategory = subCategories[position];
                break;
        }
        showAdvertisements();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //show all
    }

    @Override
    public void onAdvertisementClick(int position, String nameAd) {

        Intent intent = new Intent(SearchSubcategoryActivity.this, AdActivity.class);
        intent.putExtra("advertisement", nameAd);
        startActivity(intent);
    }
}
