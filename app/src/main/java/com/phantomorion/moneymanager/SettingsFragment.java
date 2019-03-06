package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends android.support.v4.app.Fragment {
    private static String TAG = "MainActivity";
    Button mFamilyChangePieChart;
    long dincome=0,dexpense=0,dinvestment=0;
    long[] yData =new long[3];
    String[] xData = {"Remaining","Expense","Investment"};
    PieChart pieChart;
    FirebaseFirestore fdb=FirebaseFirestore.getInstance();
    SharedPreferences spref,spref2;
    ArrayList<transaction>tran;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        pieChart = view.findViewById(R.id.idPieChart);

        mFamilyChangePieChart = view.findViewById(R.id.button_change_family);


        pieChart.setDescription("Amount (In Rupees) ");
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleAlpha(0);
        //pieChart.setCenterText("Super Cool Chart");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();
        mFamilyChangePieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),FamilyPieChartActivity.class));
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
                Toast.makeText(getContext(), "Amount " + employee + "\n" +   sales + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return view;

    }
    private void addDataSet() {
        spref=getActivity().getSharedPreferences("userName",Context.MODE_PRIVATE);
        spref2=getActivity().getSharedPreferences("fcode",Context.MODE_PRIVATE);
        if(!spref.getString("userName","nmjkloiy").equals(spref2.getString("fcode","qwxctyvb")))
        {
            mFamilyChangePieChart.setVisibility(View.VISIBLE);
        }
        fdb.collection(spref.getString("userName","vbnmjkl")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tran=(ArrayList<transaction>)queryDocumentSnapshots.toObjects(transaction.class);

                for(transaction t : tran) {
                    if(t.type.equals("investment"))
                    {
                        dinvestment+=Long.parseLong(t.amount);
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
//                legend.setEnabled(true);
//                legend.setCustom(Color.WHITE,new String[]{"Remaining","Expense","Investment"});
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
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}