package com.example.udhari;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class _5_More_Info extends AppCompatActivity {
    public static TextView name, you_will_get_or_give,you_will_get_or_give_amount;
    RecyclerView recyclerView;
    ImageView delete, call;
    Toolbar toolbar;
    FloatingActionButton actionButton;
    public static _6_More_Info_Adapter adapter;
    public static _1_CompleteInfo a3CompleteInfo =new _1_CompleteInfo();
    public static int temp=0;
    public static _3_DataBase dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new _3_DataBase(_5_More_Info.this);

        setContentView(R.layout.activity_more_info);

        you_will_get_or_give=findViewById(R.id.info_screen_get_or_give);
        you_will_get_or_give_amount=findViewById(R.id.info_screen_get_or_give_amount);
        recyclerView=findViewById(R.id.info_screen_recycle_view);

        toolbar=findViewById(R.id.toolbar);
        delete=toolbar.findViewById(R.id.delete_button);
        call=toolbar.findViewById(R.id.call_button);
        actionButton=findViewById(R.id.more_screen_add_button);


        String number=getIntent().getStringExtra("number");
        for (int i = 0; i < MainActivity.list.size(); i++) {
            if(number.equals(MainActivity.list.get(i).getNumber()))
            {
                a3CompleteInfo =MainActivity.list.get(i);
                temp=i;
            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(a3CompleteInfo.getName());
        getSupportActionBar().setSubtitle(a3CompleteInfo.getNumber().substring(0,7)+"*****");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new _6_More_Info_Adapter(this, a3CompleteInfo.getTransaction(),temp);
        recyclerView.setAdapter(adapter);
        set_text_amount();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(_5_More_Info.this, _7_Add_New_Transaction.class);
                intent.putExtra("index",temp);
                intent.putExtra("delete",0);
                startActivity(intent);
            }
        });




        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_5_More_Info.this);
                builder.setTitle("Delete Contact");
                builder.setMessage("Are you sure you want to delete this Contact?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _3_DataBase dbHelper = new _3_DataBase(_5_More_Info.this);
                        dbHelper.deleteData(a3CompleteInfo.getName());
                        MainActivity.list.remove(temp);
                        MainActivity.set_give_get(MainActivity.list);
                        MainActivity.mainActivity_adapter.dataChange(MainActivity.list);
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = a3CompleteInfo.getNumber();
                if (!phoneNumber.startsWith("+91")) {
                    phoneNumber = "+91" + phoneNumber;
                }
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });
    }

    public static void set_text_amount()
    {
        long amount=0;
        for (int i = 0; i < a3CompleteInfo.getTransaction().size(); i++) {
            amount=amount+a3CompleteInfo.getTransaction().get(i).getAmount();
        }
        if(amount<0)
        {
            you_will_get_or_give.setText("You will give");
            you_will_get_or_give_amount.setText(MainActivity.convert_amount(-amount));
        }else if(amount>0)
        {
            you_will_get_or_give.setText("You will get");
            you_will_get_or_give_amount.setText(MainActivity.convert_amount(amount));

        } else if(amount==0)
        {
            you_will_get_or_give.setText("You will get");
            you_will_get_or_give_amount.setText(MainActivity.convert_amount(amount));

        }
        MainActivity.list.get(temp).setAmount(amount);
        MainActivity.mainActivity_adapter.notifyDataSetChanged();
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