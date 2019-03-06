package com.phantomorion.moneymanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences pref,spref;
    static SharedPreferences.Editor editor,edit;
    boolean connection;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel nc=new NotificationChannel("MoneyManager","MoneyManager",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(nc);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spref=getApplicationContext().getSharedPreferences("fcode",MODE_PRIVATE);
        pref = getApplicationContext().getSharedPreferences("userName", MODE_PRIVATE);
        editor = pref.edit();
        edit=spref.edit();
        mAuth= FirebaseAuth.getInstance();
        connection = haveNetworkConnection();
        if (!connection) {
            Toast.makeText(MainActivity.this, "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, noInternet.class);
            startActivity(intent);
            finish();
        } else {
            if(mAuth.getCurrentUser() == null && pref.getString("username","12121212121212nmnmn1").equals("12121212121212nmnmn1")){
                startActivity(new Intent(MainActivity.this,signInOptions.class));
                finish();
            }
            else if(spref.getString("fcode","1357997531").equals("1357997531")) {
                startActivity(new Intent(MainActivity.this,SingleFamilySignInActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(MainActivity.this,BottomNavigationActivity.class));
                finish();
            }
        }


    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())

                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}