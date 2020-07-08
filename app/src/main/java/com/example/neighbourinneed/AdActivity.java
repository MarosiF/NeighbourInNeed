package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neighbourinneed.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdActivity extends AppCompatActivity {

    private Button adButtonContact;
    private CircleImageView adImage;
    private TextView adName, adDays, adDate, adShipping, adDescription, adCity, adUser;
    private ProgressDialog loadingBar;
    DatabaseReference advertisementsRef, usersRef;
    private StorageReference productImagesRef;
    private String ownerEmail, owner;

    //for test ad id (name)
    private String advertisementID = "test0807test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        //waiting advertisememt ID/Name from Search
        //advertisememtID = getIntent().getStringExtra("advertisememtName");

        productImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImages");
        advertisementsRef = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");

        adButtonContact = (Button) findViewById(R.id.ad_button_submit);
        adImage = (CircleImageView) findViewById(R.id.ad_image);
        adName = (TextView) findViewById(R.id.ad_name);
        adDays = (TextView) findViewById(R.id.ad_days);
        adDate = (TextView) findViewById(R.id.ad_date);
        adShipping  = (TextView) findViewById(R.id.ad_shipping);
        adDescription  = (TextView) findViewById(R.id.ad_description);
        adCity  = (TextView) findViewById(R.id.ad_city);

        //just for test
        //Prevalent.currentAdName = advertisementID;

        advertisementInfoDisplay(advertisementID);

        adButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });


    }

    private void advertisementInfoDisplay(String advertisementID) {

        //DatabaseReference currentAdvertisementRef = advertisementsRef.child(Prevalent.getCurrentAdName());

        DatabaseReference currentAdvertisementRef = advertisementsRef.child(advertisementID);
        currentAdvertisementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Advertisement advertisement = dataSnapshot.getValue(Advertisement.class);

                    // Picasso? adImage.setImageURI(advertisement.getImage());
                    Picasso.get().load(advertisement.getImage()).into(adImage);

                    adName.setText(advertisement.getName());
                    adDays.setText(advertisement.getDays());
                    adDate.setText(advertisement.getDate());
                    adShipping.setText(advertisement.getShipping());
                    adDescription.setText(advertisement.getDescription());
                    adCity.setText(advertisement.getCity());
                    owner = advertisement.getUser();
                    getOwnerEmail();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOwnerEmail() {
        DatabaseReference currentUserRef = usersRef.child(owner);
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users user = dataSnapshot.getValue(Users.class);

                    ownerEmail = user.getEmail();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + ownerEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


}