package com.example.udhari;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class _7_Add_New_Transaction extends AppCompatActivity {
    Toolbar toolbar;
    RadioButton gave, got;
    RadioGroup radioGroup;
    EditText amount, note;
    TextView dateTextView, timeTextView,save;
    ImageView delete;
    public static _3_DataBase dbHelper;
    int index=0;
    int trasaction_index=0;
    int delete_flag=0;
    _1_CompleteInfo completeInfo;
    ArrayList<_2_TransactionInfo> transaction_list=new ArrayList<>();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_add_new_transaction);

        toolbar=findViewById(R.id.toolbar_transaction);
        delete=toolbar.findViewById(R.id.delete_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = findViewById(R.id.radioGroup);
        gave = findViewById(R.id.radioButton1);
        got = findViewById(R.id.radioButton2);
        amount=findViewById(R.id.amount_in_trasaction);
        note=findViewById(R.id.note_in_transaction);
        dateTextView = findViewById(R.id.date_in_transaction);
        timeTextView = findViewById(R.id.time_in_transaction);
        save=findViewById(R.id.save_in_transaction);
        dbHelper=new _3_DataBase(_7_Add_New_Transaction.this);
        amount.requestFocus();



        delete_flag=getIntent().getIntExtra("delete",0);

        if(delete_flag==0)
        {
            index= getIntent().getIntExtra("index",0);
            gave.setChecked(true);
            amount.setTextColor(Color.parseColor("#CD0909"));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            String currentDate = dateFormat.format(calendar.getTime());

            dateTextView.setText(currentDate);
            Calendar currentTime = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            timeTextView.setText(timeFormat.format(currentTime.getTime()).toUpperCase());
            completeInfo=MainActivity.list.get(index);

        }
        else
        {
            delete.setVisibility(View.VISIBLE);
            index= getIntent().getIntExtra("main_index",0);
            trasaction_index=getIntent().getIntExtra("transaction_index",0);
            completeInfo=MainActivity.list.get(index);
            _2_TransactionInfo transactionInfo=completeInfo.getTransaction().get(trasaction_index);
            note.setText(transactionInfo.getInfo());
            dateTextView.setText(transactionInfo.getDate());
            timeTextView.setText(transactionInfo.getTime());
            Long tempAmount=transactionInfo.getAmount();
            if(tempAmount<=0)
            {
                tempAmount=-tempAmount;
                got.setChecked(true);
                amount.setText(tempAmount.toString());
                amount.setTextColor(Color.parseColor("#4BAF50"));
            }
            else{
                gave.setChecked(true);
                amount.setText((tempAmount.toString()));
                amount.setTextColor(Color.parseColor("#CD0909"));
            }
            amount.setSelection(amount.getText().length());
        }


        getSupportActionBar().setTitle(completeInfo.getName());

        gave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                got.setChecked(false);
                amount.setTextColor(Color.parseColor("#CD0909"));
            }
        });
        got.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gave.setChecked(false);
                amount.setTextColor(Color.parseColor("#4BAF50"));

            }
        });
        _2_TransactionInfo temp=new _2_TransactionInfo();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amount.getText().toString().equals("") && !(Long.parseLong(amount.getText().toString()) == 0))
                {
                    if(gave.isChecked())
                    {
                        temp.setAmount(Long.parseLong(amount.getText().toString()));
                    }
                    else
                    {
                        temp.setAmount(-Long.parseLong(amount.getText().toString()));
                    }

                    temp.setDate(dateTextView.getText().toString());
                    temp.setTime(timeTextView.getText().toString());
                    temp.setInfo(note.getText().toString().trim());
                    if(delete_flag==0) {
                        MainActivity.list.get(index).transaction.add(temp);
                    }
                    else
                    {
                        MainActivity.list.get(index).transaction.get(trasaction_index).copy(temp);
                    }
                    _5_More_Info.a3CompleteInfo=MainActivity.list.get(index);
                    _5_More_Info.adapter.dataChange(MainActivity.list.get(index).transaction);
                    _5_More_Info.set_text_amount();
                    MainActivity.mainActivity_adapter.dataChange(MainActivity.list);
                    MainActivity.set_give_get(MainActivity.list);
                    dbHelper.updateData(MainActivity.list.get(index).getName(),MainActivity.list.get(index).getNumber(),MainActivity.list.get(index).getAmount(),MainActivity.list.get(index).transaction);
                    onBackPressed();
                }
                else
                {
                    Toast.makeText(_7_Add_New_Transaction.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_7_Add_New_Transaction.this);
                builder.setTitle("Delete Transaction");
                builder.setMessage("Are you sure you want to delete this Transaction?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.list.get(index).getTransaction().remove(trasaction_index);
                        _5_More_Info.a3CompleteInfo=MainActivity.list.get(index);
                        _5_More_Info.adapter.dataChange(MainActivity.list.get(index).transaction);
                        MainActivity.mainActivity_adapter.dataChange(MainActivity.list);
                        _5_More_Info.set_text_amount();
                        MainActivity.set_give_get(MainActivity.list);
                        dbHelper.updateData(MainActivity.list.get(index).getName(),MainActivity.list.get(index).getNumber(),MainActivity.list.get(index).getAmount(),MainActivity.list.get(index).transaction);
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year, month, dayOfMonth;
                if(delete_flag==0)
                {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
                else
                {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                    LocalDate date = LocalDate.parse(dateTextView.getText().toString(), formatter);
                    dayOfMonth = date.getDayOfMonth();
                    month = date.getMonthValue()-1;
                    year = date.getYear();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(_7_Add_New_Transaction.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the text of the TextView with the selected date
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                        String selectedDate = dateFormat.format(calendar.getTime());

                        dateTextView.setText(selectedDate);
                    }
                    }, year, month, dayOfMonth);
                        datePickerDialog.show();
                    }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                int hour, min;
                if (delete_flag == 0) {
                    hour = time.get(Calendar.HOUR_OF_DAY);
                    min = time.get(Calendar.MINUTE);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                    LocalTime date = LocalTime.parse(timeTextView.getText().toString().toLowerCase(), formatter);
                    hour = date.getHour();
                    min = date.getMinute();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(_7_Add_New_Transaction.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update the TextView with the selected time
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                                String s = timeFormat.format(selectedTime.getTime()).toUpperCase();
                                timeTextView.setText(s);
                            }
                        }, hour, min, false);

                timePickerDialog.show();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}