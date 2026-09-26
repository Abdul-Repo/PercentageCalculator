package com.example.demoutil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private EditText vPart;
    private EditText vTotal;
    private EditText vPercentage;
    private Switch percentValueSwt;
    private ToggleButton affectValueSwt;
    private TextView affectValuetxt;

    // About Me interactive controls
    private MaterialButton aboutButton;
    private MaterialCardView aboutCard;
    private MaterialButton closeAboutButton;

    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        vTotal = findViewById(R.id.vTotal);
        vPart = findViewById(R.id.vPart);
        vPercentage = findViewById(R.id.vPercentage);
        percentValueSwt = findViewById(R.id.percentValueSwt);
        affectValueSwt = findViewById(R.id.affectValueSwt);
        affectValuetxt = findViewById(R.id.affectValuetxt);

        // Find and set up About Me views
        aboutButton = findViewById(R.id.AboutButton);
        aboutCard = findViewById(R.id.AboutCard);
        closeAboutButton = findViewById(R.id.CloseAboutButton);

        if (aboutButton != null && aboutCard != null) {
            aboutButton.setOnClickListener(v -> {
                aboutCard.setVisibility(View.VISIBLE);
            });
        }

        if (closeAboutButton != null && aboutCard != null) {
            closeAboutButton.setOnClickListener(v -> {
                aboutCard.setVisibility(View.GONE);
            });
        }

        // Keep percentage enabled as default
        if (vTotal.getText().toString().trim().isEmpty()) {
            disablePercentValueAff();
        } else {
            enablePercentDisableValue();
        }

        percentValueSwt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (vTotal.getText().toString().trim().isEmpty()) {
                disablePercentValueAff();
            } else if (isChecked) {
                enablePercentDisableValue();
            } else {
                enableValueDisablePercent();
            }
        });

        vPart.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating || percentValueSwt.isChecked()) return;
                
                float vTotalM = PercentageUtils.handleStringToFloat(vTotal.getText().toString());
                float vPartM = PercentageUtils.handleStringToFloat(s.toString());
                
                if (vTotalM != 0) {
                    float ans = (vPartM / vTotalM) * 100;
                    updateTextSilently(vPercentage, PercentageUtils.handleFloatToString(ans) + "%");
                }
                updateResult();
            }
        });

        vPercentage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating || !percentValueSwt.isChecked()) return;

                float vTotalM = PercentageUtils.handleStringToFloat(vTotal.getText().toString());
                float vPercentageM = PercentageUtils.handleStringToFloat(s.toString());
                
                float ans = (vTotalM * vPercentageM) / 100;
                updateTextSilently(vPart, PercentageUtils.handleFloatToString(ans));
                updateResult();
            }
        });

        vTotal.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value = PercentageUtils.handleStringToFloat(s.toString());
                if (value == 0) {
                    vPercentage.setEnabled(false);
                    vPart.setEnabled(false);
                    vTotal.setError("Total cannot be zero");
                } else {
                    vTotal.setError(null);
                    if (percentValueSwt.isChecked()) {
                        enablePercentDisableValue();
                    } else {
                        enableValueDisablePercent();
                    }
                }
                updateResult();
            }
        });

        affectValueSwt.setOnClickListener(v -> updateResult());
    }

    private void updateResult() {
        float vTotalM = PercentageUtils.handleStringToFloat(vTotal.getText().toString());
        float vPartM = PercentageUtils.handleStringToFloat(vPart.getText().toString());
        
        if (vTotal.getText().toString().isEmpty() || vPart.getText().toString().isEmpty()) {
            affectValuetxt.setText("---");
            affectValueSwt.setEnabled(false);
            return;
        }
        
        affectValueSwt.setEnabled(true);
        float result = affectValueSwt.isChecked() ? (vTotalM + vPartM) : (vTotalM - vPartM);
        affectValuetxt.setText(PercentageUtils.handleFloatToString(result));
    }

    private void updateTextSilently(EditText editText, String text) {
        isUpdating = true;
        editText.setText(text);
        isUpdating = false;
    }

    private void enableValueDisablePercent() {
        vPercentage.setEnabled(false);
        vPart.setEnabled(true);
    }

    private void enablePercentDisableValue() {
        vPercentage.setEnabled(true);
        vPart.setEnabled(false);
    }

    private void disablePercentValueAff() {
        vPercentage.setEnabled(false);
        vPart.setEnabled(false);
        affectValueSwt.setEnabled(false);
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}
