package com.phantomorion.moneymanager;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SingleFamilySignInActivity extends AppCompatActivity {

    FragmentManager fm;
    Button mSingle, mFamily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_family_sign_in);
        fm = getSupportFragmentManager();
        mSingle = findViewById(R.id.button_single);
        mFamily = findViewById(R.id.button_family);

        mSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleFamilySignInActivity.this,FormActivity.class);
                startActivity(intent);
            }
        });

        mFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSingle.setVisibility(View.GONE);
                mFamily.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.frame_family_code, new FamilySignInFragment()).addToBackStack(null).commit();

            }
        });
    }
}