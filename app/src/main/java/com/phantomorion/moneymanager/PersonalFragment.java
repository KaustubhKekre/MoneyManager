package com.phantomorion.moneymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {
    Button income,expense,investment;
    TextView mIncome,mInvestment,mExpense,mBalance;
    Intent intent;
    long dincome=0,dexpense=0,dinvestment=0;
    ArrayList<transaction>tran;
    long total;
    FirebaseFirestore fdb=FirebaseFirestore.getInstance();
    FirebaseFirestore fdb2=FirebaseFirestore.getInstance();
    SharedPreferences pref;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        mIncome = view.findViewById(R.id.text_view_income);
        mInvestment = view.findViewById(R.id.text_view_investment);
        mExpense = view.findViewById(R.id.text_view_expense);
        mBalance = view.findViewById(R.id.text_view_balance);
        income=view.findViewById(R.id.button_add_income);
        expense=view.findViewById(R.id.button_add_expense);
        investment=view.findViewById(R.id.button_add_investment);
        intent=new Intent(getContext(),MoneySpendings.class);
        transaction tran1=new transaction(""+0,""+0,""+0,""+0);
        pref=getContext().getSharedPreferences("userName",MODE_PRIVATE);
        String t1 =pref.getString("userName","adit");
        fdb.collection(pref.getString("userName","vbnmjkl")).document("initial").set(tran1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        fdb2.collection(pref.getString("userName","vbnmjkl")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tran=(ArrayList<transaction>)queryDocumentSnapshots.toObjects(transaction.class);
                total=0;
                for(transaction t : tran) {
                    if(t.type.equals("investment"))
                    {dinvestment+=Long.parseLong(t.amount);
                        total=total-Long.parseLong(t.amount);

                    }
                    else if(t.type.equals("expense"))
                    {  dexpense+=Long.parseLong(t.amount);
                        total=total-Long.parseLong(t.amount);

                    }
                    else if(t.type.equals("income"))
                    {  dincome+=Long.parseLong(t.amount);
                        total=total+Long.parseLong(t.amount);

                    }
                    mInvestment.setText(""+dinvestment);
                    mExpense.setText(""+dexpense);
                    mIncome.setText(""+dincome);
                    mBalance.setText(""+total);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("spendings","income");
                intent.putExtra("total",total);
                intent.putExtra("income",dincome);
                startActivity(intent);

            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("spendings","expense");
                intent.putExtra("total",total);
                intent.putExtra("income",dincome);
                startActivity(intent);

            }
        });
        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("spendings","investment");
                intent.putExtra("total",total);
                intent.putExtra("income",dincome);
                startActivity(intent);

            }
        });


        return view;
    }

}