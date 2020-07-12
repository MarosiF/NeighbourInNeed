package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.neighbourinneed.Model.Users;
import com.example.neighbourinneed.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserSetting extends AppCompatActivity {

    private Button saveButton;
    private CircleImageView userImage;
    private TextView userName;
    private EditText userPw, userMail, userCity, userPostcode, userDescription;
    private ProgressDialog loadingBar;
    DatabaseReference  usersRef;
    private StorageReference userImagesRef;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_profile_icon);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_loupe:
                        Intent intent = new Intent(UserSetting.this, SearchSubcategoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_list_search_icon:
                        Intent intent1 = new Intent(UserSetting.this, SearchListActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_list_offer_icon:
                        Intent intent2 = new Intent(UserSetting.this, OfferListActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_home:
                        Intent intent3 = new Intent(UserSetting.this, MainChooseActivity.class);
                        startActivity(intent3);
                        return true;

                }
                return false;
            }
        });

        userImagesRef = FirebaseStorage.getInstance().getReference().child("UserImages");
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");

        saveButton = (Button) findViewById(R.id.user_setting_button_save);
        userImage = (CircleImageView) findViewById(R.id.user_setting_image);
        userName = (TextView) findViewById(R.id.user_setting_name);
        userPw = (EditText) findViewById(R.id.user_setting_pw);
        userMail= (EditText) findViewById(R.id.user_setting_email);
        userCity= (EditText) findViewById(R.id.user_setting_city);
        userPostcode= (EditText) findViewById(R.id.user_setting_postcode);
        userDescription= (EditText) findViewById(R.id.user_setting_description);

        //username = Prevalent.getCurrentUser().getName();
        username = Paper.book().read(Prevalent.usernamekey);

        userInfoDisplay(username);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserUpdates();
            }
        });


    }

    private void saveUserUpdates() {
    }

    private void userInfoDisplay(String username) {

        //DatabaseReference currentAdvertisementRef = advertisementsRef.child(Prevalent.getCurrentAdName());

        DatabaseReference currentUserRef = usersRef.child(username);
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users user = dataSnapshot.getValue(Users.class);

                    System.out.println("User Setting test");
                    System.out.println("Username: " + user.getName());
                    System.out.println("Password: " + user.getPassword());
                    System.out.println("EMail: " + user.getEmail());
                    System.out.println("City: " + user.getCity());
                    System.out.println("Postcode: " + user.getPostcode());
                    System.out.println("Description: " + user.getDescription());
                    System.out.println("Image: " + user.getImage());

                    // Picasso? adImage.setImageURI(advertisement.getImage());
                    //Picasso.get().load(user.getImage()).into(userImage);

                    userName.setText(user.getName());
                    userPw.setText(user.getPassword());
                    userMail.setText(user.getEmail());
                    userCity.setText(user.getCity());
                    userPostcode.setText(user.getPostcode());
                    //userDescription.setText(user.getDescription());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}