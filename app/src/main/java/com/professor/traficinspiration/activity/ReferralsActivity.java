package com.professor.traficinspiration.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.professor.traficinspiration.ApplicationContext;
import com.professor.traficinspiration.R;

public class ReferralsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView)findViewById(R.id.header_title)).setText("Рефералы");

        ((TextView) findViewById(R.id.text_referrals_count)).setText(String.valueOf(ApplicationContext.getUser().getReferralsCount()));
        ((TextView) findViewById(R.id.text_referral_code)).setText(String.valueOf(ApplicationContext.getUser().getId()));
        ((TextView) findViewById(R.id.text_referral_income)).setText(String.valueOf(ApplicationContext.getUser().getReferralIncome()));

        tv = findViewById(R.id.text_referral_code);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Скопировано", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.back_button) {
            finish();
        }
    }
}
