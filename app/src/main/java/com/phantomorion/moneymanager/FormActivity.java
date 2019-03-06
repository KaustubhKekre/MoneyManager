package com.phantomorion.moneymanager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
public class FormActivity extends AppCompatActivity {
    AutoCompleteTextView name,age,savings;
    Button done;
    RadioGroup genderGroup;
    String gender;
    FirebaseFirestore fdb;
    String sname,sage,ssavings,sgender;
    SharedPreferences spref,pref,spref2;
    SharedPreferences.Editor edit,editor,edit2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_actvity);
        final boolean[] cancel = {false};
        final View[] focusView = {null};
        fdb=FirebaseFirestore.getInstance();
        spref=getApplicationContext().getSharedPreferences("fcode",Context.MODE_PRIVATE);
        edit=spref.edit();
        pref=getApplicationContext().getSharedPreferences("userName",Context.MODE_PRIVATE);
        editor=pref.edit();
        spref2=getApplicationContext().getSharedPreferences("transactionID",Context.MODE_PRIVATE);
        edit2=spref2.edit();
        name=findViewById(R.id.register_name);
        if(!pref.getString("userName","246808642").equals("246808642")) {
            name.setText(pref.getString("userName","246808642"));
        }
        age=findViewById(R.id.register_age);

        savings=findViewById(R.id.register_savings);
        done=findViewById(R.id.done);
        genderGroup=findViewById(R.id.radio_group_gender);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname=name.getText().toString();
                editor.putString("userName",sname);
                editor.commit();
                sage=age.getText().toString();

                ssavings=savings.getText().toString();

                if(Integer.parseInt(ssavings)<15){
                    savings.setError("Savings should be greater than 15");
                    focusView[0] = savings;
                    cancel[0] = true;
                }
                else {


                    int i = genderGroup.getCheckedRadioButtonId();
                    if (i == R.id.male) {
                        gender = "Male";
                    } else {
                        gender = "Female";
                    }
                    UserInfo user = new UserInfo(sname, sage, ssavings, gender);
                    if (spref.getString("fcode", "123456789987654321").equals("123456789987654321")) {
                        edit.putString("fcode", sname);
                        edit.commit();
                        fdb.collection("users").document("families").collection(sname).document(sname).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FormActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        fdb.collection("users").document("families").collection(spref.getString("fcode", "123456789987654321")).document(sname).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FormActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FormActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    edit2.putInt("transactionID", 0);
                    edit2.commit();
                    startActivity(new Intent(FormActivity.this, BottomNavigationActivity.class));
                    finish();
                }

            }
        });

    }
}