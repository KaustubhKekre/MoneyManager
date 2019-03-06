package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SpendingsListFragment extends Fragment {
   RecyclerView spendingsRecycler;
   ArrayList<transaction> transactions;
    FirebaseFirestore fdb=FirebaseFirestore.getInstance();
    SharedPreferences spref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_spendings_list, container, false);
        spref=getActivity().getSharedPreferences("userName",Context.MODE_PRIVATE);
        spendingsRecycler=v.findViewById(R.id.spendingsRecycler);
        fdb.collection(spref.getString("userName","qweasdzxc")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                transactions=(ArrayList<transaction>)queryDocumentSnapshots.toObjects(transaction.class);
                spendingsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                spendingsRecycler.setAdapter(new SpendingsAdapter(transactions));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }


}
