package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class LogOutFragment extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    ImageButton send;
    EditText edt;
    ArrayList<String> message,author;
    SharedPreferences spref;
    FirebaseFirestore fdb;
    ArrayList<transaction> tran;
    long investment,income,expense,balance;
    String msg,bmsg;
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_out, container, false);
        fdb=FirebaseFirestore.getInstance();
        send=view.findViewById(R.id.sendButton);
        recyclerView=view.findViewById(R.id.chat_recycler_view);
        edt=view.findViewById(R.id.messageInput);
        message=new ArrayList<String>();
        author=new ArrayList<String>();
        spref=getActivity().getSharedPreferences("userName",Context.MODE_PRIVATE);
        fdb.collection(spref.getString("userName","xsndllksdlkjsdlkjlk")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tran = (ArrayList<transaction>) queryDocumentSnapshots.toObjects(transaction.class);

                for(transaction t : tran) {
                    if(t.type.equals("investment"))
                    {investment+=Long.parseLong(t.amount);


                    }
                    else if(t.type.equals("expense"))
                    {  expense+=Long.parseLong(t.amount);

                    }
                    else if(t.type.equals("income"))
                    {  income+=Long.parseLong(t.amount);


                    }

                }
                balance = income - expense-investment;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=edt.getText().toString();
                edt.setText("");
                message.add(msg);
                author.add(spref.getString("userName","ewwfwlklkwejl"));


                String botResponse = chatBot(msg);



                message.add(botResponse);
                author.add("bot");
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new chatAdapter(author,message));
                recyclerView.scrollToPosition(author.size() - 1);
            }
        });
        return view;
    }
    public String chatBot(String message)
    {
        String[] words = message.split(" ");


        for(int i=0; i<words.length;i++)
        {
            if(words[i].equalsIgnoreCase("hi")||words[i].equalsIgnoreCase("yo")||words[i].equalsIgnoreCase("hello")||words[i].equalsIgnoreCase("hola")||words[i].equalsIgnoreCase("hey"))
            {
                bmsg = "Hey! How can I help you?";
                break;
            }
            else if(words[i].equalsIgnoreCase("invest")||words[i].equalsIgnoreCase("investment"))
            {
                if(investment>15000 && investment<100000){
                    bmsg = "I suggest you to invest in gold or precious stones";
                    break;
                }
                else if(investment>100000 ){
                    bmsg = "Investing in properties is best for you";
                    break;
                }
                else {
                    bmsg = "You should fix deposit in a bank";
                    break;
                }
            }
            else if(words[i].equalsIgnoreCase("name")||words[i].equalsIgnoreCase("Who")){
                bmsg = "Everyone calls me a bot";
                break;
            }
            else if(words[i].equalsIgnoreCase("how")){
                bmsg = "I am all good, thank you";
                break;
            }
            else if(words[i].equalsIgnoreCase("old")){
                bmsg = "I am too young now";
                break;
            }
            else if (words[i].equalsIgnoreCase("gender")){
                bmsg = "I am female";
                break;
            }
            else if(words[i].equalsIgnoreCase("balance"))
            {
                if(balance>1000 && balance<10000)
                {
                    bmsg = "Your Balance is " + balance +" and is satisfactory";
                    break;
                }
                else if(balance<=1000)
                {
                    bmsg = balance + " ,Balance too low. Work on it";
                    break;
                }
                else
                {
                    bmsg = balance + " ,Excellent balance!";
                    break;
                }
            }
            else if(words[i].equalsIgnoreCase("expense"))
            {
                if(expense<10000){
                    bmsg = expense + " ,Your spending is satisfactory";
                    break;
                }
                else {
                    bmsg = expense + " ,You are spending a lot";
                    break;
                }
            }
            else if(words[i].equalsIgnoreCase("bye")||words[i].equalsIgnoreCase("goodbye")){
                bmsg = "Goodbye, have a nice day";
                break;
            }
            else
            {
                bmsg = "I didn't get you";
            }
        }
        return bmsg;
    }
}