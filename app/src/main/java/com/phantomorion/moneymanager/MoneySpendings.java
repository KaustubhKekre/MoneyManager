package com.phantomorion.moneymanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MoneySpendings extends AppCompatActivity {
    ArrayList<transaction>tran;
    AutoCompleteTextView mAmount,mDate;
    Button mSubmit,mDatePicker;
    String month;
    private int mYear, mMonth, mDay;
    String savings;
    long total,income1;
    String total1;
    Spinner mSpin;
    String amt,description,type,date;
    FirebaseFirestore fdb=FirebaseFirestore.getInstance();
    FirebaseFirestore fdb2=FirebaseFirestore.getInstance();
    FirebaseFirestore fdb3=FirebaseFirestore.getInstance();
    String spendings;
    SharedPreferences pref,spref2,spref3;
    SharedPreferences.Editor edit,edit3;
    String[] income = {"salary","gifts"};
    String[] expense = {"Eating Out","Shopping","Movie","Travel","Health","Fuel","General"};
    String[] investment = {"Mutual funds","property"};
    UserInfo user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_spendings);
        pref=getApplicationContext().getSharedPreferences("userName",MODE_PRIVATE);
        spref2=getApplicationContext().getSharedPreferences("transactionID",MODE_PRIVATE);
        spref3=getApplicationContext().getSharedPreferences("fcode",MODE_PRIVATE);
        edit=spref2.edit();
        mSubmit = findViewById(R.id.submit);
        mSpin = findViewById(R.id.spinner);
        mAmount = findViewById(R.id.amount);
        mDate = findViewById(R.id.date);
        mDatePicker = findViewById(R.id.date_picker);
        fdb3.collection("users").document("families").collection(spref3.getString("fcode","mnvtdhjvmj"))
                .document(pref.getString("userName","87585876tytuy")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user=documentSnapshot.toObject(UserInfo.class);
                savings=user.getSavings();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MoneySpendings.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar c2 = Calendar.getInstance();
        mYear = c2.get(Calendar.YEAR);
        mMonth = c2.get(Calendar.MONTH);
        mDay = c2.get(Calendar.DAY_OF_MONTH);
        final String formattedDate = df.format(c);
        mDate.setText(formattedDate);
        spendings=getIntent().getStringExtra("spendings");

        total=getIntent().getLongExtra("total",0);
        income1=getIntent().getLongExtra("income",0);
        if(spendings.equals("investment"))
        {
            mSpin.setAdapter(new ArrayAdapter<>(MoneySpendings.this,android.R.layout.simple_spinner_dropdown_item,investment));
        }
        else if(spendings.equals("expense"))
        {
            mSpin.setAdapter(new ArrayAdapter<>(MoneySpendings.this,android.R.layout.simple_spinner_dropdown_item,expense));
        }
        else if(spendings.equals("income"))
        {
            mSpin.setAdapter(new ArrayAdapter<>(MoneySpendings.this,android.R.layout.simple_spinner_dropdown_item,income));
        }
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(MoneySpendings.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                switch (monthOfYear+1){

                                    case 1: month = "Jan";
                                        break;
                                    case 2: month = "Feb";
                                        break;
                                    case 3: month = "Mar";
                                        break;
                                    case 4: month = "Apr";
                                        break;
                                    case 5: month = "May";
                                        break;
                                    case 6: month = "Jun";
                                        break;
                                    case 7: month = "Jul";
                                        break;
                                    case 8: month = "Aug";
                                        break;
                                    case 9: month = "Sep";
                                        break;
                                    case 10: month = "Oct";
                                        break;
                                    case 11: month = "Nov";
                                        break;
                                    case 12: month = "Dec";
                                        break;

                                }

                                mDate.setText(dayOfMonth + "-" + month + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt=mAmount.getText().toString();
                date=mDate.getText().toString();
                if(spendings.equals("investment"))
                {
                    total=total-Long.parseLong(amt);
                    if(total<0 || total < (Long.parseLong(savings)*income1)/100)
                    {
                        Toast.makeText(MoneySpendings.this,"Transaction cannot be completed due to insufficient funds",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MoneySpendings.this,BottomNavigationActivity.class));
                        finish();
                    }
                    description=investment[(int)mSpin.getSelectedItemPosition()];
                    type="investment";
                }
                else if(spendings.equals("expense"))
                {
                    total=total-Long.parseLong(amt);
                    if(total<0 || total < (Long.parseLong(savings)*income1)/100)
                    {
                        Toast.makeText(MoneySpendings.this,"Transaction cannot be completed due to insufficient funds",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MoneySpendings.this,BottomNavigationActivity.class));
                        finish();
                    }
                    description=expense[(int)mSpin.getSelectedItemPosition()];
                    type="expense";
                }
                else if(spendings.equals("income"))
                {
                    type="income";
                    description=income[(int)mSpin.getSelectedItemPosition()];
                    transaction transaction = new transaction(amt, date, type, description);
                    int i = spref2.getInt("transactionID", 0);
                    i++;
                    edit.putInt("transactionID", i);
                    edit.commit();
                    fdb.collection(pref.getString("userName", "vbnmjkl")).document("" + i).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MoneySpendings.this, "Transaction Added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MoneySpendings.this, BottomNavigationActivity.class));
                            finish();
                        }
                    });
                }
                if(total>=0&&!spendings.equals("income") && total >= (Long.parseLong(savings)*income1)/100) {
                    transaction transaction = new transaction(amt, date, type, description);
                    int i = spref2.getInt("transactionID", 0);
                    i++;
                    edit.putInt("transactionID", i);
                    edit.commit();
                    fdb.collection(pref.getString("userName", "vbnmjkl")).document("" + i).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MoneySpendings.this, "Transaction Added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MoneySpendings.this, BottomNavigationActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
    }
}