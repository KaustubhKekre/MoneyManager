package com.phantomorion.moneymanager;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.viewHolder> {
    ArrayList<String> author;
    ArrayList<String> message;
    public chatAdapter(ArrayList<String> author,ArrayList<String> message){
        this.author=author;
        this.message=message;
    }
    @NonNull
    @Override
    public chatAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflate = LayoutInflater.from(viewGroup.getContext());
        View view = inflate.inflate(R.layout.chat_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatAdapter.viewHolder viewHolder, int i) {
        String bot=author.get(i);
        if(author.get(i).equals("bot"))
        {
            viewHolder.card.setCardBackgroundColor(Color.WHITE);

        }
        else
        {
            viewHolder.card.setCardBackgroundColor(Color.rgb(211,251,211));
        }
    viewHolder.author.setText(author.get(i));
    viewHolder.message.setText(message.get(i));
    }

    @Override
    public int getItemCount() {
        return author.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView author,message;
        CardView card;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.author);
            message=itemView.findViewById(R.id.message);
            card=itemView.findViewById(R.id.card);
        }
    }
}
