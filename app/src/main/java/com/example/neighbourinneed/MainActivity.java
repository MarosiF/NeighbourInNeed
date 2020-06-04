package com.example.neighbourinneed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button RegisterSubmitButton;
    private EditText InputName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        RegisterSubmitButton = (Button) findViewById(R.id.button);
        InputName = (EditText) findViewById(R.id.editText);


        RegisterSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount(){
        String name = InputName.getText().toString();
    }


}
