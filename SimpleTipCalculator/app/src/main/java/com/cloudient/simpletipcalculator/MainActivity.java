package com.cloudient.simpletipcalculator;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    private TextView total_tv;
    private TextView tip_amt_tv;
    private TextView split_total_amt_tv;
    private Button reset_btn;

    private EditText subtotal_et;
    private EditText tax_et;
    private Button tip_minus_btn;
    private Button tip_add_btn;
    private TextView tip_percentage_amt_tv;
    private Button split_minus_btn;
    private Button split_add_btn;
    private TextView split_amt_tv;

    private double total = 0.0;
    private double tax = 0.0;
    private double subtotal = 0.0;
    private double tip = 0.0;
    private double tip_percentage = 0.15;
    private double split = 0.0;
    private int split_amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Full screen is set for the Window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-8625474222959230~6371735760");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Typeface avenirNextRegular = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.ttf");

        total_tv = findViewById(R.id.total_tv);
        tip_amt_tv = findViewById(R.id.tip_amt_tv);
        split_total_amt_tv = findViewById(R.id.split_total_amt_tv);
        reset_btn = findViewById(R.id.reset_btn);
        TextView tip_tv = findViewById(R.id.tip_tv);
        TextView split_total_tv = findViewById(R.id.split_total_tv);

        total_tv.setTypeface(avenirNextRegular);
        tip_amt_tv.setTypeface(avenirNextRegular);
        split_total_amt_tv.setTypeface(avenirNextRegular);
        tip_tv.setTypeface(avenirNextRegular);
        split_total_tv.setTypeface(avenirNextRegular);

        subtotal_et = findViewById(R.id.subtotal_et);
        tax_et = findViewById(R.id.tax_et);
        tip_minus_btn = findViewById(R.id.tip_minus_btn);
        tip_add_btn = findViewById(R.id.tip_add_btn);
        tip_percentage_amt_tv = findViewById(R.id.tip_percentage_amt_tv);
        split_minus_btn = findViewById(R.id.split_minus_btn);
        split_add_btn = findViewById(R.id.split_add_btn);
        split_amt_tv = findViewById(R.id.split_amt_tv);
        TextView subtotal_tv = findViewById(R.id.subtotal_tv);
        TextView tax_tv = findViewById(R.id.tax_tv);
        TextView tip_percentage_tv = findViewById(R.id.tip_percentage_tv);
        TextView split_tv = findViewById(R.id.split_tv);

        subtotal_et.setTypeface(avenirNextRegular);
        tax_et.setTypeface(avenirNextRegular);
        tip_percentage_amt_tv.setTypeface(avenirNextRegular);
        split_amt_tv.setTypeface(avenirNextRegular);
        subtotal_tv.setTypeface(avenirNextRegular);
        tax_tv.setTypeface(avenirNextRegular);
        tip_percentage_tv.setTypeface(avenirNextRegular);
        split_tv.setTypeface(avenirNextRegular);

        subtotal_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty() && isDouble(s.toString())) {
                    // Calculating the total and setting it to the view
                    subtotal = Double.parseDouble(s.toString());
                    tip = tip_percentage * Double.parseDouble(s.toString());
                    total = subtotal + tip + tax;
                    split = total/split_amount;
                } else {
                    subtotal = 0.0;
                    total = 0.0;
                    split = 0.0;
                    tip = 0.0;
                }
                setCost();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tax_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty() && isDouble(s.toString())) {
                    tax = Double.parseDouble(s.toString());
                } else {
                    tax = 0.0;
                }
                total = subtotal + tip + tax;
                split = total/split_amount;
                setCost();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tip_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double d = Math.round(tip_percentage * 100);
                int whole_percentage = (int) d;

                if(whole_percentage > 0) {
                    tip_percentage -= 0.01;
                    whole_percentage -= 1;
                } else if ( whole_percentage == 0 ) {
                    tip_percentage = 0;
                }
                recalculateTip(whole_percentage);
            }
        });

        tip_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double d = Math.round(tip_percentage * 100);
                int whole_percentage = (int) d;

                if(whole_percentage < 100) {
                    tip_percentage += 0.01;
                    whole_percentage += 1;
                } else if (whole_percentage == 100) {
                    tip_percentage = 1;
                }
                recalculateTip(whole_percentage);
            }
        });

        split_minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(split_amount > 1) {
                    split_amount -= 1;
                } else {
                    split_amount = 1;
                }
                recalculateSplit();
            }
        });

        split_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(split_amount < 1000) {
                    split_amount += 1;
                } else {
                    split_amount = 1000;
                }
                recalculateSplit();
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total = 0.0;
                tax = 0.0;
                subtotal = 0.0;
                tip = 0.0;
                tip_percentage = 0.15;
                split = 0.0;
                split_amount = 1;

                total_tv.setText(String.format(Locale.US, "%.2f", total));
                tip_amt_tv.setText("$0.00");
                split_total_amt_tv.setText("$0.00");
                tax_et.setText("");
                subtotal_et.setText("");
                tip_percentage_amt_tv.setText("15%");
                split_amt_tv.setText("1");
            }
        });
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void recalculateTip(int whole_percentage) {
        if (tip_percentage < 0) {
            tip_percentage = 0;
        }
        tip = tip_percentage * subtotal;
        total = subtotal + tip + tax;
        split = total / split_amount;
        setCost();
        String tip_percentage_with_percent = String.valueOf(whole_percentage) + "%";
        tip_percentage_amt_tv.setText(tip_percentage_with_percent);
    }

    private void recalculateSplit() {
        split_amt_tv.setText(Integer.toString(split_amount));
        split = total / split_amount;
        setCost();
    }

    private void setCost(){
        String tip_with_dollar = "$" + String.format(Locale.US, "%.2f", tip);
        String split_total_with_dollar = "$" + String.format(Locale.US, "%.2f", split);
        String total_with_dollar = "$" + String.format(Locale.US, "%.2f", total);

        tip_amt_tv.setText(tip_with_dollar);
        split_total_amt_tv.setText(split_total_with_dollar);
        total_tv.setText(total_with_dollar);
    }
}