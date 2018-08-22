package com.vitartha.hisabkitab.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.R;

public class FilterActivity extends AppCompatActivity {

    Button filter;
    EditText byname, byamt, minamt, maxamt;
    TextView bydate;
    private int Year, Month, Day;
    String category_value;
    SharedPreference spAdap;
    CheckBox cash, cheque, acct, card;
    StringBuilder filterurl = new StringBuilder(key.transactions.show_url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Filter");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        /* For back button */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        filter = findViewById(R.id.filterbtn);
        byname = findViewById(R.id.searchName);
        byamt = findViewById(R.id.searchAmount);
        bydate = findViewById(R.id.searchDate);
        minamt = findViewById(R.id.minRange);
        maxamt = findViewById(R.id.maxRange);
        cash = findViewById(R.id.checkcash);
        card = findViewById(R.id.checkcard);
        cheque = findViewById(R.id.checkcheque);
        acct = findViewById(R.id.checkacct);
        spAdap = new SharedPreference(FilterActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category_value = extras.getString("category");
            //The key argument here must match that used in the other activity
        }
        filterurl.append("?category=").append(spAdap.getString("category"));

        bydate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);

                bydate.setInputType(InputType.TYPE_NULL);
                bydate.requestFocus();

                DatePickerDialog pickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bydate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day);
                pickerDialog.show();

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(byname.length() > 0)
                    filterurl.append("&search=").append(byname.getText().toString());
                if(byamt.length() > 0)
                    filterurl.append("&amount=").append(byamt.getText().toString());
                if(minamt.length() > 0)
                    filterurl.append("&start_amount=").append(minamt.getText().toString());
                if(maxamt.length() > 0)
                    filterurl.append("&end_amount=").append(maxamt.getText().toString());
                if(bydate.length() > 0)
                    filterurl.append("&transaction_date=").append(bydate.getText().toString());
                if(cash.isChecked())
                    filterurl.append("&mode=").append("1");
                if(cheque.isChecked())
                    filterurl.append("&mode=").append("2");
                if(acct.isChecked())
                    filterurl.append("&mode=").append("3");
                if(card.isChecked())
                    filterurl.append("&mode=").append("4");

                Intent i = new Intent(FilterActivity.this, TransactionHistory.class);
                i.putExtra("filter_url", String.valueOf(filterurl));
                startActivity(i);
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
        return true;
    }
}
