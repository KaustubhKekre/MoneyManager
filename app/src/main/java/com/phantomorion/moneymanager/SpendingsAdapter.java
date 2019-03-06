package com.phantomorion.moneymanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;



public class SpendingsAdapter extends RecyclerView.Adapter<SpendingsAdapter.ViewHolder> {

    ArrayList<transaction> transactionDetails;

    public SpendingsAdapter(ArrayList<transaction> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    @NonNull
    @Override
    public SpendingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflate = LayoutInflater.from(viewGroup.getContext());
        View view = inflate.inflate(R.layout.spendings_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final SpendingsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.type.setText(transactionDetails.get(i).getType());
        viewHolder.description.setText(transactionDetails.get(i).getDescription());
        viewHolder.amount.setText(transactionDetails.get(i).getAmount());
        viewHolder.date.setText(transactionDetails.get(i).getDate());
        if(transactionDetails.get(i).getDate().equals("0"))
        {
            viewHolder.card.setVisibility(View.GONE);
        }
        else {
            if (transactionDetails.get(i).getType().equals("investment")) {
              viewHolder.card.setBackgroundColor(Color.rgb(176,224,230));

            } else if (transactionDetails.get(i).getType().equals("expense")) {
                viewHolder.card.setBackgroundColor(Color.rgb(249,221,216));
            } else {
                viewHolder.card.setBackgroundColor(Color.rgb(211,251,211));
            }
        }
    }

        @Override
        public int getItemCount () {
            return transactionDetails.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView type, amount, description, date;
            android.support.v7.widget.CardView card;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                type = itemView.findViewById(R.id.type);
                amount = itemView.findViewById(R.id.amount);
                description = itemView.findViewById(R.id.description);
                date = itemView.findViewById(R.id.date);
                card = itemView.findViewById(R.id.card);
            }
        }

}