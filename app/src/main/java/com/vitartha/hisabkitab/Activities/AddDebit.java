package com.vitartha.hisabkitab.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDebit extends AppCompatActivity {

    EditText name, amt, comment, date;
    private int Year, Month, Day;
    Button submit;
    RadioGroup radioGroupmode;
    Boolean isname, isamt, isdate, ismode;
    int mode;
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Debit Transaction");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        /**For back button**/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        date = findViewById(R.id.date);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.contactname);
        amt = findViewById(R.id.amount);
        comment = findViewById(R.id.comment);
        progressDialog = new ProgressDialog(this);

        radioListerner();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (name.getText().toString().length() < 0) {
                    name.setError("Enter name");
                    isname = false;
                } else
                    isname = true;
            }
        });

        amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (amt.length() < 0) {
                    amt.setError("Enter amount");
                    isamt = false;
                } else
                    isamt = true;
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (date.length() < 0) {
                    date.setError("Specify Date");
                    isdate = false;
                } else
                    isdate = true;
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);
                Month = calendar.get(Calendar.MONTH);
                Day = calendar.get(Calendar.DAY_OF_MONTH);

                date.setInputType(InputType.TYPE_NULL);
                date.requestFocus();

                DatePickerDialog pickerDialog = new DatePickerDialog(AddDebit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day);
                pickerDialog.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isname && isdate && isamt) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put(key.transactions.key_category, "D");
                        object.put(key.transactions.Key_contact, name.getText().toString());
                        object.put(key.transactions.key_amount, amt.getText().toString());
                        object.put(key.transactions.key_mode, mode);
                        object.put(key.transactions.key_date, date.getText().toString());

                        senddata(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AddDebit.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else if (!isname)
                    name.setError("Enter name");
                else if (!isamt)
                    amt.setError("Enter amount");
                else if (!isdate)
                    date.setError("Specify date");
            }
        });
    }

    public void senddata(JSONObject jsonObject) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        HisabKitabJSONRequest request = new HisabKitabJSONRequest(Request.Method.POST,
                key.transactions.add_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verifytransactiondata(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddDebit.this, "Error while sending data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, this), this);
        requestQueue.add(request);
    }

    public void verifytransactiondata(JSONObject resp) throws Exception {
        progressDialog.dismiss();
        Toast.makeText(this, "Transaction Added Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddDebit.this, TransactionHistory.class);
        startActivity(i);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    /**Radio Group Listener*/
    public void radioListerner() {
        radioGroupmode = findViewById(R.id.radiogroup);
        radioGroupmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cash:
                        mode = 1;
                        Toast.makeText(AddDebit.this, "Cash:"+ mode, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.cheque:
                        mode = 2;
                        Toast.makeText(AddDebit.this, "Cheque:"+ mode, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.acct:
                        mode = 3;
                        Toast.makeText(AddDebit.this, "Account:"+mode, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.card:
                        mode = 4;
                        Toast.makeText(AddDebit.this, "Card:" + mode, Toast.LENGTH_SHORT).show();
                        break;
                }
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
