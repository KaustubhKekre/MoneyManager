package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class FamilyPieChartActivity extends AppCompatActivity {
Button personal;
    private static String TAG = "MainActivity";
    long dincome=0,dexpense=0,dinvestment=0;
    long[] yData =new long[3];

    String[] xData = {"Remaining","Expense","Investment"};
    PieChart pieChart;
    FirebaseFirestore fdb2=FirebaseFirestore.getInstance(),fdb3=FirebaseFirestore.getInstance();
    SharedPreferences spref,spref2;
    ArrayList<transaction> tran;
    ArrayList<UserInfo> fam;
    ArrayList<String> names=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_pie_chart);
        pieChart = findViewById(R.id.idPieChart);
        spref=getApplicationContext().getSharedPreferences("userName",Context.MODE_PRIVATE);
        spref2=getApplicationContext().getSharedPreferences("fcode",Context.MODE_PRIVATE);
        fdb2.collection("users").document("families").collection(spref2.getString("fcode","qweghbnmk")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                fam=(ArrayList<UserInfo>)queryDocumentSnapshots.toObjects(UserInfo.class);
                for(UserInfo u:fam)
                {
                    names.add(u.getName());
                }
                for(String n:names) {
                    fdb3.collection(n).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            tran = (ArrayList<transaction>) queryDocumentSnapshots.toObjects(transaction.class);
                            for(transaction t:tran) {
                                if(t.type.equals("investment"))
                                {dinvestment+=Long.parseLong(t.amount);

                                }
                                else if(t.type.equals("expense"))
                                {  dexpense+=Long.parseLong(t.amount);

                                }
                                else if(t.type.equals("income"))
                                {  dincome+=Long.parseLong(t.amount);

                                }
                            }
                            yData[0]=dincome-dinvestment-dexpense;
                            yData[1]=dexpense;
                            yData[2]=dinvestment;
                            pieChart.setDescription("Amount (In Rupees) ");
                            pieChart.setRotationEnabled(true);

                            pieChart.setHoleRadius(0);
                            pieChart.setTransparentCircleAlpha(0);

                            pieChart.setCenterTextSize(10);



                            ArrayList<PieEntry> yEntrys = new ArrayList<>();
                            ArrayList<String> xEntrys = new ArrayList<>();

                            for(int i = 0; i < 3; i++){
                                yEntrys.add(new PieEntry(yData[i] , i));
                            }

                            for(int i = 0; i < 3; i++){
                                xEntrys.add(xData[i]);
                            }

                            //create the data set
                            PieDataSet pieDataSet = new PieDataSet(yEntrys, "Financial distribution");
                            pieDataSet.setSliceSpace(3);
                            pieDataSet.setValueTextSize(12);

                            //add colors to dataset
                            ArrayList<Integer> colors = new ArrayList<>();
                            colors.add(Color.rgb(88,252,70));
                            colors.add(Color.rgb(253,83,83));
                            colors.add(Color.rgb(252,252,70));


                            pieDataSet.setColors(colors);

                            //add legend to chart
                            Legend legend = pieChart.getLegend();
                            legend.setForm(Legend.LegendForm.CIRCLE);
                            legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                            //create pie data object
                            PieData pieData = new PieData(pieDataSet);
                            pieChart.setData(pieData);
                            pieChart.invalidate();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FamilyPieChartActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FamilyPieChartActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });


        personal=findViewById(R.id.button_change_family);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FamilyPieChartActivity.super.onBackPressed();
            }
        });
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for(int i = 0; i <3; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(FamilyPieChartActivity.this, "Amount " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

}

