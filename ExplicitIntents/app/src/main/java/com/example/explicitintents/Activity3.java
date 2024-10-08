package com.example.explicitintents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class Activity3 extends AppCompatActivity {
    Button btn4;
    EditText etSurName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);


        btn4 = findViewById(R.id.btn4);
        etSurName = findViewById(R.id.etSurName);

    }
}
