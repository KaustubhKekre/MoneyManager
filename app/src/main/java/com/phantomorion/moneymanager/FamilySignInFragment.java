package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FamilySignInFragment extends Fragment {
    Button mGenerate,mSubmit,gSubmit;
    AutoCompleteTextView mregister;
    TextView code,displayStart;
    String fcode,acode;
    SharedPreferences spref;
    SharedPreferences.Editor edit;
    FirebaseFirestore fdb;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_family_sign_in, container, false);
        fdb=FirebaseFirestore.getInstance();
        spref=view.getContext().getSharedPreferences("fcode",Context.MODE_PRIVATE);
        edit=spref.edit();
        displayStart=view.findViewById(R.id.displayStart);
        gSubmit=view.findViewById(R.id.gSubmit);
        mGenerate = view.findViewById(R.id.button_generate_code);
        mSubmit=view.findViewById(R.id.button_submit_code);
        mregister=view.findViewById(R.id.register_code);
        code=view.findViewById(R.id.display);
        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mregister.setVisibility(View.GONE);
                mSubmit.setVisibility(View.GONE);
                fcode=FamilyCodeGenerator.getAlphaNumericString();
                edit.putString("fcode",fcode);
                edit.commit();
                displayStart.setVisibility(View.VISIBLE);
                code.setText(fcode);
                gSubmit.setVisibility(View.VISIBLE);
                gSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),FormActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acode=mregister.getText().toString();
                fdb.collection("users").document("families").collection(acode).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(view.getContext(), "No such family please try again or generate a new family code", Toast.LENGTH_LONG).show();
                                } else {
                                    edit.putString("fcode", acode);
                                    edit.commit();
                                    Intent intent = new Intent(getContext(), FormActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
        return view;
    }
}