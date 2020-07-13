package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.neighbourinneed.Model.Users;
import com.example.neighbourinneed.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

/**
 * The Main Activity for the Application
 * @author Ebru Özcelik,Fanni Marosi
 * @version 1.0
 * This is the first Screen the user sees
 */
public class MainActivity extends AppCompatActivity {


    /**
     *Views and Buttons for the user to see
     */
    private Button loginButton, registerButton;
    private ProgressDialog loadingBar;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        automatedLogin();
    }

    /**
     * The Method will get the stored userkeys and will be initiate an automated login.
     */
    private void automatedLogin() {
        String usernamekey = Paper.book().read(Prevalent.usernamekey);
        String userpasswordkey = Paper.book().read(Prevalent.userpasswordkey);
        if(usernamekey != "" && userpasswordkey  != "" ){
            if(!TextUtils.isEmpty(usernamekey) && !TextUtils.isEmpty(userpasswordkey)){
                AllowAccess(usernamekey, userpasswordkey);
                loadingBar.setTitle("Already Logged in!");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }


    /**
     * The Method will acces the userdate in firebase-database and
     * if the stored userdata is correct it will automatically get the user to the nex Activity.
     * @param username stored username
     * @param password stored password
     */
    private void AllowAccess(final String username, final String password){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(username).exists()) {
                    Users userData = dataSnapshot.child("Users").child(username).getValue(Users.class);
                    System.out.println(dataSnapshot.child("Users").child(username).getValue(Users.class));
                    //String uNDB = dataSnapshot.child(parentDbName).child(username).child("name").getValue().toString();
                    //System.out.println(uNDB);
                    System.out.println("Username: " + userData.getName());
                    System.out.println("Password: " + userData.getPassword());
                    System.out.println("EMail: " + userData.getEmail());
                    System.out.println("City: " + userData.getCity());
                    System.out.println("Postcode: " + userData.getPostcode());

                    System.out.println("Username: " + username);
                    if (userData.getName().equals(username)) {
                        System.out.println("Success!");
                        if (userData.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, MainChooseActivity.class);
                            Prevalent.currentUser = userData;
                            startActivity(intent);
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "An account with this " + username + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
