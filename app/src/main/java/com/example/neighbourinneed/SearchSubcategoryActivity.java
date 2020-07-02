package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchSubcategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    ArrayList<Advertisement> list;
    RecyclerView recyclerView;
    SearchView searchView;

    String [] mainCategories = {"Search", "Offer"};
    String [] subCategories = {"All", "Search for a gift", "Borrow something", "Search for help"};

    private String currentSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subcategory);

        ref = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        Spinner spin = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
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
                        //switch()
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String subcategoryInDB = ds.child("subCategory").getValue().toString();
                            System.out.println(subcategoryInDB);
                            if (currentSubCategory == null || currentSubCategory.equals("All")) {
                                list.add(ds.getValue(Advertisement.class));
                            }
                            else if (subcategoryInDB.equals(currentSubCategory)) {
                                list.add(ds.getValue(Advertisement.class));
                            }
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);
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
        ArrayList<Advertisement> myList = new ArrayList<Advertisement>();

        for (Advertisement ad : list) {
            if (ad.getDescription().toLowerCase().contains(s.toLowerCase())) {
                myList.add(ad);

            }
        }

        AdapterClass adapterClass = new AdapterClass(myList);
        recyclerView.setAdapter(adapterClass);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSubCategory = subCategories[position];
        showAdvertisements();
        System.out.println(subCategories[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //show all
    }
}
