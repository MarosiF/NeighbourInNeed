package com.example.neighbourinneed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.neighbourinneed.Prevalent.Prevalent;
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

public class CreateAdActivity extends AppCompatActivity {

    private Button createAdButtonSubmit;
    private ImageView createAdImage;
    private EditText createAdName, createAdDays, createAdDate, createAdShipping, createAdDescription, createAdCity;
    private ProgressDialog loadingBar;
    final private String parentDbName = "Advertisements";
    private static final int galleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private String downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);

        createAdButtonSubmit = (Button) findViewById(R.id.create_ad_button_submit);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImages");
        createAdImage = (ImageView) findViewById(R.id.create_ad_image);
        createAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            OpenGallery();
            }
        });

        createAdName = (EditText) findViewById(R.id.create_ad_name);
        createAdDays = (EditText) findViewById(R.id.create_ad_days);
        createAdDate = (EditText) findViewById(R.id.create_ad_date);
        createAdShipping  = (EditText) findViewById(R.id.create_ad_shipping);
        createAdDescription  = (EditText) findViewById(R.id.create_ad_description);
        createAdCity  = (EditText) findViewById(R.id.create_ad_city);
        loadingBar = new ProgressDialog(this);


        createAdButtonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createAdvertisement();
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
            ImageUri = data.getData();
            createAdImage.setImageURI(ImageUri);
        }
    }

    private void createAdvertisement() {

        String name = createAdName.getText().toString().trim();
        String days = createAdDays.getText().toString().trim();
        String date = createAdDate.getText().toString().trim();
        String shipping = createAdShipping.getText().toString().trim();
        String description = createAdDescription.getText().toString().trim();
        String city = createAdCity.getText().toString().trim();

        if(ImageUri == null){
            Toast.makeText(this, "You need to add an Image first!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name) || name.length() < 8 || TextUtils.isEmpty(days) || days.length() < 8 ||
                TextUtils.isEmpty(date) || date.length() < 8 || TextUtils.isEmpty(shipping) || shipping.length() <8 ||
                TextUtils.isEmpty(description) || description.length() < 8 || TextUtils.isEmpty(city) || city.length() < 2) {
            Toast.makeText(CreateAdActivity.this, "All input fields must contain at least 8 letters, city may " +
                            "only contain 2 or more!", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Create Advertisement");
            loadingBar.setMessage("Please wait while we are creating your advertisement");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            prepareAdd(name, days, date, shipping, description, city);
        }

    }

    private void prepareAdd(final String name, final String days, final String date, final String shipping, final String description, final String city) {
        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(CreateAdActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreateAdActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(CreateAdActivity.this, "getting Product image Url successfully!", Toast.LENGTH_SHORT).show();

                            persistAdvertisement(name, days, date, shipping, description, city);
                        }
                    }
                });
            }
        });

    }


    private void persistAdvertisement(final String name, final String days, final String date, final String shipping, final String description, final String city) {


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
                    userdataMap.put("city", city);
                    userdataMap.put("image", downloadImageUrl);
                    userdataMap.put("user", Prevalent.currentUser.getName());

                    rootRef.child("Advertisement").child(name).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAdActivity.this, "Advertisement has been created!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        createOwnedAdPosition(name, Prevalent.currentUser.getName() );

                                        Intent intent = new Intent(CreateAdActivity.this, MainChooseActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(CreateAdActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
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
                System.out.println(databaseError);
                Toast.makeText(CreateAdActivity.this, "Sorry, there was an error with the database connection", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    private void createOwnedAdPosition(final String adName, final String username) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        final String id = adName+username;


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(parentDbName);
                if (!(ds.child(id).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("id", id);
                    userdataMap.put("adName", adName);
                    userdataMap.put("username", username);

                    rootRef.child("ConnectionTableOwned").child(id).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAdActivity.this, "Connection has been created!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(CreateAdActivity.this, MainChooseActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(CreateAdActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CreateAdActivity.this, "This name " + id + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
                Toast.makeText(CreateAdActivity.this, "Sorry, there was an error with the database connection", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }
}

