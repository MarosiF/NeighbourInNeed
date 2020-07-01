package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    private Button registerSubmitButton;
    private EditText inputName, inputPassword, inputEmail, inputCity, inputPostcode;
    private ProgressDialog loadingBar;
    final private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerSubmitButton = (Button) findViewById(R.id.submitButton);
        inputName = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputCity  = (EditText) findViewById(R.id.inputCity);
        inputPostcode  = (EditText) findViewById(R.id.inputPostCode);
        loadingBar = new ProgressDialog(this);



        registerSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();
        String email = inputEmail.getText().toString();
        String city = inputCity.getText().toString();
        String postcode = inputPostcode.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) || TextUtils.isEmpty(city) || TextUtils.isEmpty(postcode)){
            Toast.makeText(RegisterActivity.this, "Enter things!", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, credentials bla blah");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateEmail(name, password, email, city, postcode);
        }


    }

    private void ValidateEmail(final String name, final String password, final String email, final String city, final String postcode ){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(parentDbName);
                if(!(ds.child(name).exists())){
                    Iterable<DataSnapshot> user = ds.getChildren();
                    for(DataSnapshot data : user) {
                        String emailInDB = data.child("email").getValue().toString();
                        System.out.println(ds);
                        System.out.println("Email: " + emailInDB);
                        if (!(email.equals(emailInDB))) {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("name", name);
                            userdataMap.put("password", password);
                            userdataMap.put("email", email);
                            userdataMap.put("city", city);
                            userdataMap.put("postcode", postcode);

                            rootRef.child("Users").child(name).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "Account has been created!", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                loadingBar.dismiss();
                                                Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "This Email " + email+ " already exists", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "This name " + name + " already exsits", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
