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
import java.util.Arrays;
import java.util.List;

public class SearchSubcategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    ArrayList<Advertisement> list;
    RecyclerView recyclerView;
    SearchView searchView;

    String [] mainCategories = {"All", "Search", "Offer"};
    String [] subCategories = {"All", "Gift", "Loan", "Help"};

    private String currentMainCategory = "All";
    private String currentSubCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subcategory);

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
                                if (currentSubCategory.equals("All") && currentMainCategory.equals("All")) {
                                    list.add(ds.getValue(Advertisement.class));
                                }
                                else if (mainCategoryInDB.equals(currentMainCategory) && subcategoryInDB.equals(currentSubCategory)) {
                                    list.add(ds.getValue(Advertisement.class));
                                }
                            }
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);
                        /*if (list.size() == 0) {
                            Toast.makeText(SearchSubcategoryActivity.this, "No advertisements found", Toast.LENGTH_SHORT).show();
                        }*/
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
        ArrayList<Advertisement> searchList = new ArrayList<Advertisement>();
        try {
            for (Advertisement ad : list) {
                if (ad.getCity().toLowerCase().contains(s.toLowerCase())) {
                    searchList.add(ad);
                }
            }
        } catch(NullPointerException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        AdapterClass adapterClass = new AdapterClass(searchList);
        recyclerView.setAdapter(adapterClass);
        /*if (searchList.size() == 0) {
            Toast.makeText(SearchSubcategoryActivity.this, "No advertisements found", Toast.LENGTH_SHORT).show();
        }*/
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
}
