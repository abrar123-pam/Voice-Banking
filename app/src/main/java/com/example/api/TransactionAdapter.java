package com.example.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.viewHolder> {

    List<TransactionModel> list;
    Context context;

    public TransactionAdapter(List<TransactionModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder,int position) {

        holder.amounttxt.setText(list.get(position).getAmount());
        holder.nicknametxt.setText(list.get(position).getNickname());
        holder.datetxt.setText(list.get(position).getDate());
        holder.timetxt.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView amounttxt,nicknametxt,datetxt,timetxt;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            amounttxt = itemView.findViewById(R.id.amount);
            nicknametxt = itemView.findViewById(R.id.nicnametransaction);
            datetxt = itemView.findViewById(R.id.date);
            timetxt = itemView.findViewById(R.id.time);
            context = itemView.getContext();

        }
    }


}
