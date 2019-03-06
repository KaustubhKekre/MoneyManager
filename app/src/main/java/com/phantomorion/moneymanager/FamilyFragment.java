package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FamilyFragment extends android.support.v4.app.Fragment {
    SharedPreferences spref,pref;
    Button generate;
    TextView text,code;
    ImageView line;
    String fcode;
    ArrayList<String> names=new ArrayList<String>();
    FirebaseFirestore fdb=FirebaseFirestore.getInstance();
    FirebaseFirestore fdb2=FirebaseFirestore.getInstance();
    FirebaseFirestore fdb3=FirebaseFirestore.getInstance();
    SharedPreferences.Editor edit;
    UserInfo uinfo;
    String s;
    LinearLayout top,bottom;
    ArrayList<UserInfo> fam;
    ArrayList<transaction>tran;
    TextView savings,expenses,income,balance;
    long dinvestment=0,dincome=0,dexpense=0;
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

        pref=getActivity().getSharedPreferences("userName",Context.MODE_PRIVATE);
        spref=getActivity().getSharedPreferences("fcode",Context.MODE_PRIVATE);
        generate=view.findViewById(R.id.generate);
        text=view.findViewById(R.id.text);
        code=view.findViewById(R.id.code);
        top=view.findViewById(R.id.top);
        line=view.findViewById(R.id.line);
        income=view.findViewById(R.id.text_view_income);
        expenses=view.findViewById(R.id.text_view_expense);
        balance=view.findViewById(R.id.text_view_balance);
        savings=view.findViewById(R.id.text_view_savings);
        bottom=view.findViewById(R.id.bottom);
        edit=spref.edit();


        if(pref.getString("userName","qwertyuio").equals(spref.getString("fcode","asdfghjk")))
        {
            generate.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            s=spref.getString("fcode","zxcvb");
            fdb.collection("users").document("families").collection(s).document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    uinfo=documentSnapshot.toObject(UserInfo.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
            generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fdb.collection("users").document("families").collection(spref.getString("fcode","zxcvb")).document(spref.getString("fcode","zxcvb")).delete();
                    fcode=FamilyCodeGenerator.getAlphaNumericString();
                    edit.putString("fcode",fcode);
                    edit.commit();
                    text.setText("Your family code is -");
                    text.setVisibility(View.VISIBLE);
                    code.setText(fcode);
                    code.setVisibility(View.VISIBLE);
                    generate.setVisibility(View.GONE);
                    fdb.collection("users").document("families").collection(fcode).document(uinfo.getName()).set(uinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"You were added to a family !!",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }
        else
        {   line.setVisibility(View.VISIBLE);
            top.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
            fdb2.collection("users").document("families").collection(spref.getString("fcode","asdfghjk"))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    fam=(ArrayList<UserInfo>)queryDocumentSnapshots.toObjects(UserInfo.class);

                    for (UserInfo u:fam)
                    {
                        String name=u.getName();
                        names.add(name);
                    }
                    for (String name : names)
                    {
                        fdb3.collection(name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                tran=(ArrayList<transaction>)queryDocumentSnapshots.toObjects(transaction.class);

                                for(transaction t : tran) {
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
                                expenses.setText(""+dexpense);
                                income.setText(""+dincome);
                                savings.setText(""+dinvestment);
                                balance.setText(""+(dincome-dinvestment-dexpense));



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }


        return view;
    }
}