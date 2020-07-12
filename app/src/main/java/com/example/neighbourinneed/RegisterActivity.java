package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerSubmitButton;
    private EditText inputName, inputPassword, inputEmail, inputCity, inputPostcode;
    private ProgressDialog loadingBar;
    final private String parentDbName = "Users";
    private ImageView registerImage;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private StorageReference userImagesRef;
    private String downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userImagesRef= FirebaseStorage.getInstance().getReference().child("UserImages");

        registerImage = (ImageView) findViewById(R.id.register_image);
        registerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
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
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            registerImage.setImageURI(imageUri);
        }
    }

    private void createAccount(){
        String name = inputName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String city = inputCity.getText().toString().trim();
        String postcode = inputPostcode.getText().toString().trim();

        if(imageUri == null){
            Toast.makeText(this, "You need to add an Image first!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name) || name.length() < 8 || TextUtils.isEmpty(password) || password.length() < 8 ||
                TextUtils.isEmpty(email) || email.length() < 8 || TextUtils.isEmpty(city) || city.length() < 2 ||
                TextUtils.isEmpty(postcode) || postcode.length() < 5){
            Toast.makeText(RegisterActivity.this, "All input fields must contain at least 8 letters, city may only " +
                    "contain 2 or more, postcode should be 5 letters long!", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while creating your account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            prepareUser(name, password, email, city, postcode);

        }


    }

    private void prepareUser(final String name, final String password, final String email, final String city, final String postcode) {
        final StorageReference filePath = userImagesRef.child(imageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(RegisterActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(RegisterActivity.this, "getting Product image Url successfully!", Toast.LENGTH_SHORT).show();

                            ValidateEmail(name, password, email, city, postcode);

                        }
                    }
                });
            }
        });

    }


    private void ValidateEmail(final String name, final String password, final String email, final String city, final String postcode ){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        final String description = "default";

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
                            userdataMap.put("description", description);
                            userdataMap.put("image", downloadImageUrl);

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
