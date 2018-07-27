package com.vitartha.hisabkitab.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import java.util.List;

public class DebitT_RecyclerView extends RecyclerView.Adapter<DebitT_RecyclerView.MyViewHolder> implements View.OnClickListener{
    private List<DebitDetails> debitDetailsList;

      public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView uname, amnt, pmode, pdate, pcomment;
        Button delbtn;

        public MyViewHolder(View view) {
            super(view);
            uname = (TextView) view.findViewById(R.id.namedetail);
            amnt = (TextView)view.findViewById(R.id.amountdetail);
            pmode = (TextView)view.findViewById(R.id.modedetail);
            pdate = (TextView)view.findViewById(R.id.transactiondetail);
            pcomment = (TextView) view.findViewById(R.id.commentdetail);

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

    public DebitT_RecyclerView(List<DebitDetails> Debitdetails) {
        this.debitDetailsList = Debitdetails;
    }

    @Override
    public DebitT_RecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_debitt_recyclerview, parent, false);

        return new DebitT_RecyclerView.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DebitT_RecyclerView.MyViewHolder holder, int position) {
        DebitDetails details = debitDetailsList.get(position);
        holder.uname.setText(details.getName());
        holder.pcomment.setText(details.getComment());
        holder.pdate.setText(details.getDate());
        holder.pmode.setText(details.getMode());
        holder.amnt.setText(details.getAmount());
    }

    @Override
    public int getItemCount() {
        return this.debitDetailsList.size();
    }
    @Override
    public void onClick(View v) {

    }

}
