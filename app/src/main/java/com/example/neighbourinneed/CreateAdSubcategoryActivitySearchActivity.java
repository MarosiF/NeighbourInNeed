package com.example.neighbourinneed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAdSubcategoryActivitySearchActivity extends AppCompatActivity {

    private Button btSearchGift, btBorrow, btSearchHelp;

    private static final String gift = "Gift";
    private static final String borrowLend = "Loan";
    private static final String searchHelp = "Help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createad_subcategory_search);

        btSearchGift = (Button) findViewById(R.id.createad_subcategory_search_button_search_gift);
        btBorrow = (Button) findViewById(R.id.createad_subcategory_search_button_borrow);
        btSearchHelp = (Button) findViewById(R.id.createad_subcategory_search_button_search_help);

        final String callingActivity = getIntent().getStringExtra("callingActivity");
        System.out.println("Calling activity " + callingActivity);

        btSearchGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivitySearchActivity.this, CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", gift);
                startActivity(intent);
            }
        });

        btBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivitySearchActivity.this,CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", borrowLend);
                startActivity(intent);
            }
        });

        btSearchHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdSubcategoryActivitySearchActivity.this, CreateAdActivity.class);
                intent.putExtra("mainCategory", callingActivity);
                intent.putExtra("subcategory", searchHelp);
                startActivity(intent);
            }
        });
    }
}
