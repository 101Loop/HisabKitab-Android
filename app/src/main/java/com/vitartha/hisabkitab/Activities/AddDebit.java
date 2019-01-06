package com.vitartha.hisabkitab.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import java.util.Calendar;

import android.support.design.button.MaterialButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddDebit extends AppCompatActivity {

    EditText name, amt, comment;
    TextView date;
    private int Year, Month, Day;
    Button submit;
    RadioGroup radioGroupmode;
    Boolean isname =false, isamt = false, isdate = false, ismode = false;
    int mode;
    ProgressDialog progressDialog;
    SharedPreference spAdap;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        spAdap = new SharedPreference(AddDebit.this);

        if(spAdap.getString("category").equals("C"))
            toolbar.setTitle("Add Credit Transaction");
        else
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
                String nametxt = name.getText().toString();
                if (nametxt.length() == 0) {
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
                String amttxt = amt.getText().toString();
                if (amttxt.length() == 0) {
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
                String datetxt = date.getText().toString();
                if (datetxt.length() <= 0) {
                    date.setError("Specify Date");
                    isdate = false;
                } else
                    isdate = true;
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
                if(!ismode){
                    Toast.makeText(AddDebit.this, "Select Transaction Mode", Toast.LENGTH_SHORT).show();
                }
                else if (isname && isdate && isamt) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put(key.transactions.key_category, spAdap.getString("category"));
                        object.put(key.transactions.Key_contact, name.getText().toString());
                        object.put(key.transactions.key_amount, amt.getText().toString());
                        object.put(key.transactions.key_comments, comment.getText().toString());
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
        }, new HisabKitabErrorListener(progressDialog, AddDebit.this), this);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,0,1f));
        requestQueue.add(request);
    }

    public void verifytransactiondata(JSONObject resp) throws Exception {
        progressDialog.dismiss();
        Toast.makeText(this, "Transaction Added Successfully", Toast.LENGTH_SHORT).show();
     /*   Intent i = new Intent(AddDebit.this, TransactionHistory.class);
        i.putExtra("filter_url", "");
        startActivity(i);*/
        finish();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    /**Radio Group Listener for Modes*/
    public void radioListerner() {
        radioGroupmode = findViewById(R.id.radiogroup);
        radioGroupmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cash:
                        mode = 1;
                        ismode = true;
                        break;
                    case R.id.cheque:
                        mode = 2;
                        ismode = true;
                        break;
                    case R.id.acct:
                        mode = 3;
                        ismode = true;
                        break;
                    case R.id.card:
                        mode = 5;
                        ismode = true;
                        break;
                    default: ismode = false;
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        finish();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        return true;
    }
}
