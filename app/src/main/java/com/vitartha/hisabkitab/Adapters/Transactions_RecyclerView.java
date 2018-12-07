package com.vitartha.hisabkitab.Adapters;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Activities.Dashboard;
import com.vitartha.hisabkitab.Activities.TransactionHistory;
import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Transactions_RecyclerView extends RecyclerView.Adapter<Transactions_RecyclerView.MyViewHolder> implements View.OnClickListener {
    private static List<DebitDetails> debitDetailsList;
    private Context mcontext;
    private ProgressDialog progressDialog;
    public int TransactionId;
    SharedPreference spAdap;
    private int Year, Month, Day, mode;
    private OnBottomReachListener onBottomReachListener;


    // on bottom reach listener*
    public void setOnBottomReachListener(OnBottomReachListener onBottomReachListener){
        this.onBottomReachListener =onBottomReachListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView uname, amnt, pmode, pdate, pcomment, nameHint;
        ImageView delbtn, trans_icon;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            uname = (TextView) view.findViewById(R.id.namedetail);
            amnt = (TextView) view.findViewById(R.id.amountdetail);
            pmode = (TextView) view.findViewById(R.id.modedetail);
            pdate = (TextView) view.findViewById(R.id.transactiondetail);
            pcomment = (TextView) view.findViewById(R.id.commentdetail);
            trans_icon = view.findViewById(R.id.namehint);
            delbtn = view.findViewById(R.id.delbtn);
            cardView = view.findViewById(R.id.card_view);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(mcontext, "Long press", Toast.LENGTH_SHORT).show();
            return true;
        }


    }

    public Transactions_RecyclerView(List<DebitDetails> Debitdetails, Context context) {
        debitDetailsList = Debitdetails;
        this.mcontext = context;
    }

    @Override
    public Transactions_RecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_debitt_recyclerview, parent, false);

        return new Transactions_RecyclerView.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(Transactions_RecyclerView.MyViewHolder holder, final int position) {
        DebitDetails details = debitDetailsList.get(position);
        progressDialog = new ProgressDialog(mcontext);
        holder.uname.setText(details.getName());
        holder.pcomment.setText(details.getComment());
        holder.pdate.setText(details.getDate());
        holder.pmode.setText(details.getMode());
       // holder.nameHint.setText(details.getName().substring(0,1));
        holder.amnt.setText(details.getAmount());
        if(details.getCategory().equals("C")) {
            holder.trans_icon.setImageResource(R.drawable.money_credit);
            holder.cardView.setBackgroundColor(Color.parseColor("#DCEAEF"));
        }
        else {
            holder.trans_icon.setImageResource(R.drawable.money_debit);
            holder.cardView.setBackgroundColor(Color.parseColor("#1Af44336"));
        }

        spAdap = new SharedPreference(mcontext);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(mcontext);
                TransactionId = debitDetailsList.get(position).getId();

                LayoutInflater inflater = LayoutInflater.from(mcontext);
                final View dialogView = inflater.inflate(R.layout.details_alert, null);
                alertdialog.setView(dialogView);

                alertdialog.setMessage("Transaction Details");
                final TextView date = dialogView.findViewById(R.id.date);
                final EditText name = dialogView.findViewById(R.id.contactname);
                final EditText amt = dialogView.findViewById(R.id.amount);
                final EditText comment = dialogView.findViewById(R.id.comment);
                final RadioGroup radioGroup = dialogView.findViewById(R.id.radiogroup);

                name.setText(debitDetailsList.get(position).getName());
                amt.setText(debitDetailsList.get(position).getAmount());
                comment.setText(debitDetailsList.get(position).getComment());
                date.setText(debitDetailsList.get(position).getDate());

                switch (debitDetailsList.get(position).getMode()) {
                    case "Cash":
                        radioGroup.check(R.id.cash);
                        mode = 1;
                        break;
                    case "Account Transfer":
                        radioGroup.check(R.id.acct);
                        mode = 3;
                        break;
                    case "Cheque":
                        radioGroup.check(R.id.cheque);
                        mode = 2;
                        break;
                    case "Card":
                        radioGroup.check(R.id.card);
                        mode = 5;
                        break;
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.cash:
                                mode = 1;
                                break;
                            case R.id.cheque:
                                mode = 2;
                                break;
                            case R.id.acct:
                                mode = 3;
                                break;
                            case R.id.card:
                                mode = 5;
                                break;
                        }
                    }
                });

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        Year = calendar.get(Calendar.YEAR);
                        Month = calendar.get(Calendar.MONTH);
                        Day = calendar.get(Calendar.DAY_OF_MONTH);

                        date.setInputType(InputType.TYPE_NULL);
                        date.requestFocus();

                        DatePickerDialog pickerDialog = new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }, Year, Month, Day);
                        pickerDialog.show();

                    }
                });
                alertdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put(key.transactions.Key_contact, name.getText().toString());
                            object.put(key.transactions.key_amount, amt.getText().toString());
                            object.put(key.transactions.key_comments, comment.getText().toString());
                            object.put(key.transactions.key_mode, mode);
                            object.put(key.transactions.key_date, date.getText().toString());

                            String urlobj = key.transactions.transaction_url + TransactionId + "/update/";

                            if(mcontext instanceof TransactionHistory)
                                ((TransactionHistory) mcontext).updatetransaction(object, urlobj);
                            else if(mcontext instanceof Dashboard)
                                ((Dashboard)mcontext).updatetransaction(object, urlobj);
                            notifyItemRangeChanged(position, debitDetailsList.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mcontext, "Some error occured while updating transaction! Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = alertdialog.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1295c9"));

            }
        });

        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertdialog = new AlertDialog.Builder(mcontext);
                alertdialog.setMessage("Delete Transaction?");
                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Deleting...");
                        progressDialog.show();
                        TransactionId = debitDetailsList.get(position).getId();
                        removeAt(position, TransactionId, v);
                    }
                });
                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alertdialog.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1295c9"));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#f44336"));
            }
        });

        if(position==debitDetailsList.size()-1){
            onBottomReachListener.loadMoreData(position);
        }
    }

    public void reloadData(List<DebitDetails> ddetails) {
        debitDetailsList = ddetails;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return debitDetailsList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public void removeAt(int position, int Trans_ID, View view) {
        debitDetailsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, debitDetailsList.size());
        String urlobj = key.transactions.transaction_url + Trans_ID + "/delete/";
        if(mcontext instanceof  TransactionHistory){
            ((TransactionHistory)mcontext).deletefromAPI(urlobj, progressDialog);
            // ((TransactionHistory)mcontext).fetchtransaction(key.transactions.show_url + "?&category=" + spAdap.getString("category"));
            ((TransactionHistory)mcontext).onResume();
        }
         else  if(mcontext instanceof Dashboard) {
            ((Dashboard) mcontext).deletefromAPI(urlobj, progressDialog);
           // ((Dashboard)mcontext).fetchtransaction(key.transactions.show_url);
            ((Dashboard)mcontext).onResume();
        }
        progressDialog.dismiss();
        Toast.makeText(view.getContext(), "Transaction deleted successfully!", Toast.LENGTH_SHORT).show();

    }

    //bottom reach listener interface
    public interface OnBottomReachListener{
        void loadMoreData(int position);
    }
}
