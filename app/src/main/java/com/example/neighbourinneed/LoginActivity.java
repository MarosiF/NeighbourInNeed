package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
 * The Login Activity for the Application
 * @author Ebru Özcelik,Fanni Marosi
 * @version 1.0
 * This is the Screen the user sees after clicking the Login button in the Main Activity
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Views and Buttons for the user to see
     */
    private EditText inputLoginUsername, inputLoginPassword;
    private Button submitButton;
    private ProgressDialog loadingBar;
    private CheckBox chkBoxRememberMe;

    /**
     * Database access parameter
     */
    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLoginUsername = (EditText) findViewById(R.id.inputLoginUsername);
        inputLoginPassword = (EditText) findViewById(R.id.inputLoginPassword);
        submitButton = (Button) findViewById(R.id.submitButton);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.checkbox_remember_me);
        Paper.init(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }


    /**
     * The Method will get the stored userkeys from the EditTexts and will be initiate a login function.
     */
    private void loginUser() {
        String username = inputLoginUsername.getText().toString();
        String password = inputLoginPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please type in your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please type in your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username, password);
        }

    }

    /**
     * The Method will access the userdate in firebase-database and
     * if the  userdata is correct it will store the userdate in Paper and Prevalent
     * and  get the user to the nex Activity.
     * @param username stored username
     * @param password stored password
     */
    private void AllowAccessToAccount(final String username, final String password) {

        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.usernamekey, username);
            Paper.book().write(Prevalent.userpasswordkey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(username).exists()) {
                    Users userData = dataSnapshot.child(parentDbName).child(username).getValue(Users.class);
                    System.out.println(dataSnapshot.child(parentDbName).child(username).getValue(Users.class));

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
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, MainChooseActivity.class);

                            System.out.println(Prevalent.currentUser);
                            Prevalent.currentUser = userData;
                            System.out.println(Prevalent.currentUser.getName());

                            startActivity(intent);
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "An account with this " + username + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
