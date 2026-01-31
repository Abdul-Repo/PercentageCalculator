package com.example.demoutil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static EditText vPart;
    private static EditText vTotal;
    private static EditText vPercentage;
    private static Switch percentValueSwt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vTotal = findViewById(R.id.vTotal);
        vPart = findViewById(R.id.vPart);
        vPercentage = findViewById(R.id.vPercentage);
        percentValueSwt = findViewById(R.id.percentValueSwt);



        enableValueDisablePercent();


        percentValueSwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (percentValueSwt.isChecked()) {
                    enablePercentDisableValue();
                } else {
                    enableValueDisablePercent();
                }


            }
        });

        vPart.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!percentValueSwt.isChecked()) {
                    vPercentage.setEnabled(false);
                    float vTotalM = PercentageUtils.handleStringToFloat(vTotal.getText().toString());
                    float vPartM = PercentageUtils.handleStringToFloat(vPart.getText().toString());
                    float ans = ((float) vPartM /vTotalM)*100;
                    vPercentage.setText(PercentageUtils.handleFloatToString(ans)+"%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vPercentage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (percentValueSwt.isChecked()) {
                    float vTotalM = PercentageUtils.handleStringToFloat(vTotal.getText().toString());
                    float vPercentageM = PercentageUtils.handleStringToFloat(vPercentage.getText().toString());
                    float ans = ((float) vTotalM * vPercentageM) / 100;
                    vPart.setText(PercentageUtils.handleFloatToString(ans));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        vTotal.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                float value = PercentageUtils.handleStringToFloat(s.toString());

                if(value==0){
                    vPercentage.setEnabled(false);
                    vPart.setEnabled(false);
                    vTotal.setError("Can't be zero");
                } else{
                    if(percentValueSwt.isChecked()){
                        vPercentage.setEnabled(true);
                    }else{
                        vPart.setEnabled(true);

                    }
                }

            }
        });


    }


    public static void enableValueDisablePercent(){
        vPercentage.setEnabled(false);
        vPart.setEnabled(true);
        vPercentage.getText().clear();
        vPart.getText().clear();

    }

    public static void enablePercentDisableValue(){
        vPercentage.setEnabled(true);
        vPart.setEnabled(false);
        vPercentage.getText().clear();
        vPart.getText().clear();
    }


}