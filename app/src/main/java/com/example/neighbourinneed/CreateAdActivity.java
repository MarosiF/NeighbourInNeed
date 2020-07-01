package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateAdActivity extends AppCompatActivity {

    private Button createAdButtonSubmit;
    private EditText createAdName, createAdDays, createAdDate, createAdShipping, createAdDescription;
    private ProgressDialog loadingBar;
    final private String parentDbName = "Advertisements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);

        createAdButtonSubmit = (Button) findViewById(R.id.create_ad_button_submit);

        createAdName = (EditText) findViewById(R.id.create_ad_name);
        createAdDays = (EditText) findViewById(R.id.create_ad_days);
        createAdDate = (EditText) findViewById(R.id.create_ad_date);
        createAdShipping  = (EditText) findViewById(R.id.create_ad_shipping);
        createAdDescription  = (EditText) findViewById(R.id.create_ad_description);
        loadingBar = new ProgressDialog(this);


        createAdButtonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createAdvertisement();
            }
        });
    }

    private void createAdvertisement() {

        String name = createAdName.getText().toString();
        String days = createAdDays.getText().toString();
        String date = createAdDate.getText().toString();
        String shipping = createAdShipping.getText().toString();
        String description = createAdDescription.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(days) || TextUtils.isEmpty(date) || TextUtils.isEmpty(shipping) || TextUtils.isEmpty(description)){
            Toast.makeText(CreateAdActivity.this, "Enter things!", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Create Advertisement");
            loadingBar.setMessage("Please wait while we are creating your advertisement");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            persistAdvertisement(name, days, date, shipping, description);
        }

    }


    private void persistAdvertisement(final String name, final String days, final String date, final String shipping, final String description) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        final String mainCategory = getIntent().getStringExtra("mainCategory");
        final String subCategory = getIntent().getStringExtra("subcategory");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(parentDbName);
                if (!(ds.child(name).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("days", days);
                    userdataMap.put("date", date);
                    userdataMap.put("shipping", shipping);
                    userdataMap.put("description", description);
                    userdataMap.put("mainCategory", mainCategory);
                    userdataMap.put("subCategory", subCategory);

                    rootRef.child("Advertisement").child(name).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAdActivity.this, "Advertisement has been created!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(CreateAdActivity.this, MainChooseActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(CreateAdActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    Toast.makeText(CreateAdActivity.this, "This name " + name + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

