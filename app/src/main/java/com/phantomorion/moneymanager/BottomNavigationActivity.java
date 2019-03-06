package com.phantomorion.moneymanager;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class BottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PersonalFragment()).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment tempFragment = null;

                switch(item.getItemId()){
                    case R.id.personal:
                        tempFragment = new PersonalFragment();
                        break;
                    case R.id.family:
                        tempFragment = new FamilyFragment();
                        break;
                    case R.id.bill:
                        tempFragment = new SpendingsListFragment();
                        break;
                    case R.id.settings:
                        tempFragment = new SettingsFragment();
                        break;
                    case R.id.log_out:
                        tempFragment = new LogOutFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tempFragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
