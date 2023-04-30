package com.example.udhari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 1;
    private static final int REQUEST_PICK_CONTACT = 2;
    FloatingActionButton actionButton;
    SearchView searchView;
    public static RecyclerView recyclerView;
    public static TextView give_text,get_text;
    public static MainActivity_Adapter mainActivity_adapter;
    public static ArrayList<_1_CompleteInfo>list=new ArrayList<>();
    public static _3_DataBase dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("UDHARI");
        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.v5)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.v3)));

        dbHelper = new _3_DataBase(MainActivity.this);
        list=dbHelper.getAllData();


        give_text=findViewById(R.id.main_screen_give);
        get_text=findViewById(R.id.main_screen_get);
        actionButton=findViewById(R.id.main_screen_add_button);
        recyclerView=findViewById(R.id.main_screen_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainActivity_adapter=new MainActivity_Adapter(MainActivity.this,list);
        recyclerView.setAdapter(mainActivity_adapter);

        searchView=findViewById(R.id.search_contact);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter_list(newText);
                return true;
            }
        });
        set_give_get(list);

    }
    public static void set_give_get(ArrayList<_1_CompleteInfo>list)
    {
        long give=0;
        long get=0;
        for (int i = 0; i < list.size(); i++) {
            long num=list.get(i).getAmount();
            if(num>=0)
            {
                get=get+num;
            }
            else
            {
                give=give+num;
            }
        }
        give_text.setText(convert_amount(-give));
        get_text.setText(convert_amount(get));
    }
    public static String convert_amount(long amount) {

        return String.format("%,d", amount);
    }


    public static void maintain_Main_List(Context context, String name, String number) {
        Intent intent;
        for (int i = 0; i < list.size(); i++) {
            _1_CompleteInfo a3CompleteInfo = list.get(i);
            if (a3CompleteInfo.getNumber().equals(number)) {
                intent = new Intent(context, _5_More_Info.class);
                intent.putExtra("number", number);
                context.startActivity(intent);
                return;
            }
        }
        _1_CompleteInfo a3CompleteInfo = new _1_CompleteInfo(name, number, 0L, new ArrayList<_2_TransactionInfo>());
        list.add(a3CompleteInfo);
        mainActivity_adapter.dataChange(list);
        intent = new Intent(context, _5_More_Info.class);
        intent.putExtra("number", number);
        context.startActivity(intent);
        try {
            dbHelper.addData(a3CompleteInfo.getName(), a3CompleteInfo.getNumber(), a3CompleteInfo.getAmount(), a3CompleteInfo.getTransaction());
        }catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filter_list(String newText)
    {
        ArrayList<_1_CompleteInfo>new_list=new ArrayList<>();

        for (_1_CompleteInfo i:list)
        {
            if(i.getName().toLowerCase().contains(newText.toLowerCase()) ||
                    i.getNumber().toLowerCase().contains(newText.toLowerCase()))
            {
                new_list.add(i);
            }
        }
        if(new_list.isEmpty())
        {
        }
        else
        {
            mainActivity_adapter.set_filtered_list(new_list);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private boolean permissionGranted = false; // Add a boolean flag to check if the permission has been granted before

    public void onPickContactClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true; // Set the flag to true if permission is granted
                pickContact();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    // Show a rationale to explain the need for the permission
                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed")
                            .setMessage("This permission is needed to access your contacts.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS);
                }
            }
        } else {
            permissionGranted = true; // Set the flag to true for API levels lower than M
            pickContact();
        }
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_CONTACT) {
                contactPicked(data);
            }
        } else {
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String name = null;
            String phoneNo = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                name = cursor.getString(nameIndex);
                phoneNo = cursor.getString(phoneIndex);
            }
            if(!phoneNo.startsWith("+91"))
            {
                phoneNo="+91"+phoneNo;
            }
            maintain_Main_List(this,name,phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true; // Set the flag to true if permission is granted
                pickContact();
            } else {
                // Show a message or dialog to inform the user that the permission is needed to use this feature
            }
        }
    }
}