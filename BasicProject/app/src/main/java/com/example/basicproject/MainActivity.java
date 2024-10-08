package com.example.basicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etID ;
    Button btnSubmit;
    TextView tvResults;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 0003128540  / random egn



        etID = findViewById(R.id.etID);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvResults = findViewById(R.id.tvResults);

        tvResults.setVisibility(View.GONE);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNumber =  etID.getText().toString().trim();

                String mesec = idNumber.substring(3,4);
                String den = idNumber.substring(5,6);
                String rnum = idNumber.substring(7,10);



                String oldDate;

                int date = Integer.parseInt(idNumber.substring(0,1));



                if (date!=0){
                    oldDate =getString(R.string.oldDate);
                }else{
                    oldDate = getString(R.string.yDate);

                }



                String text = (oldDate + date +"\n"+getString(R.string.Mesec)+mesec+"\n"+getString(R.string.Dden)+den+getString(R.string.Rnum)+rnum);

                tvResults.setText(text);

                tvResults.setVisibility(View.VISIBLE);




            }
        });

        //da vzeme i ot telefona

    }
}
