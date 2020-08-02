package com.example.crickettermometer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText etChirps;
    Button btnCalculate;
    TextView tvResults;
    DecimalFormat formmater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        etChirps = findViewById(R.id.etChirps);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResults = findViewById(R.id.tvResults);

        tvResults.setVisibility(View.GONE);

        formmater = new DecimalFormat("#0.0");
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etChirps.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                }else{
                        int chips = Integer.parseInt(etChirps.getText().toString().trim());
                        double temp = (chips / 3.0) + 4;
                        String results = getString(R.string.resultsT) + formmater.format(temp) + getString(R.string.degC);
                        tvResults.setText(results);
                        tvResults.setVisibility(View.VISIBLE);


                }
            }
        });




    }
}
