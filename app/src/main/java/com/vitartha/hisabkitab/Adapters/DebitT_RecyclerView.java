package com.vitartha.hisabkitab.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vitartha.hisabkitab.Activities.DebitHistory;
import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DebitT_RecyclerView extends RecyclerView.Adapter<DebitT_RecyclerView.MyViewHolder> implements View.OnClickListener{
    private static ArrayList<DebitHistory> debitDetails;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView uname, amnt, pmode, pdate, pcomment;
        Button delbtn;
        public MyViewHolder(View view) {
            super(view);
            uname = view.findViewById(R.id.namedetail);
            amnt = view.findViewById(R.id.amountdetail);
            pmode = view.findViewById(R.id.modedetail);
            pdate = view.findViewById(R.id.transactiondetail);
            pcomment = view.findViewById(R.id.commentdetail);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "You clicked here!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


    }

    public DebitT_RecyclerView(ArrayList<DebitHistory> Debitdetails) {
        this.debitDetails = Debitdetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_debitt_recyclerview, parent, false);

        return new DebitT_RecyclerView.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.debitDetails.size();
    }

    @Override
    public void onClick(View v) {

    }

}
