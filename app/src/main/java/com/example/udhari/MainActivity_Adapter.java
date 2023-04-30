package com.example.udhari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity_Adapter extends RecyclerView.Adapter<MainActivity_Adapter.ViewHolder> {
    Context context;
    ArrayList<_1_CompleteInfo>list=new ArrayList<>();
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_EXTRA = 1;

    MainActivity_Adapter(Context context, ArrayList<_1_CompleteInfo>list)
    {
        this.list=list;
        this.context=context;
        notifyDataSetChanged();
    }
    public void dataChange(ArrayList<_1_CompleteInfo>list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    public void set_filtered_list(ArrayList<_1_CompleteInfo>list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ITEM_TYPE_EXTRA;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == ITEM_TYPE_EXTRA) {
            v = LayoutInflater.from(context).inflate(R.layout._3_extra_part, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout._1_main_screen_contact_view, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivity_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position == list.size()) {}
        else {
            _1_CompleteInfo a3CompleteInfo =list.get(position);
            holder.avatar.setText(a3CompleteInfo.getName().substring(0,1));
            holder.name.setText(a3CompleteInfo.getName());
            holder.number.setText(a3CompleteInfo.getNumber().substring(0,7)+"*****");
            holder.line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  intent = new Intent(context, _5_More_Info.class);
                    intent.putExtra("number", a3CompleteInfo.getNumber());
                    context.startActivity(intent);
                }
            });
            holder.line.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Contact");
                    builder.setMessage("Are you sure you want to delete this Contact?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _3_DataBase dbHelper = new _3_DataBase(context);
                            dbHelper.deleteData(a3CompleteInfo.getName());
                            MainActivity.list.remove(position);
                            MainActivity.set_give_get(MainActivity.list);
                            MainActivity.mainActivity_adapter.dataChange(MainActivity.list);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                    return true;
                }
            });
            if(a3CompleteInfo.getAmount()<0)
            {
                holder.amount.setText(MainActivity.convert_amount(-a3CompleteInfo.getAmount()));
                holder.amount.setTextColor(Color.parseColor("#CD0909"));
                holder.rupees.setTextColor(Color.parseColor("#CD0909"));


            }else
            {
                holder.amount.setText(MainActivity.convert_amount(a3CompleteInfo.getAmount()));
                holder.amount.setTextColor(Color.parseColor("#4BAF50"));
                holder.rupees.setTextColor(Color.parseColor("#4BAF50"));
                holder.reminder.setVisibility(View.VISIBLE);
                holder.reminder.setText("Reminder >");
                holder.reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + Uri.encode(a3CompleteInfo.getNumber())));
                        intent.putExtra("sms_body", "Dear Sir/Madam, your payment of \u20B9" + a3CompleteInfo.getAmount() + " is still pending. Please make the payment as soon as possible.");
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView avatar, name, number, amount, reminder, rupees;
        CardView line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.main_screen_contact_avatar);
            name = itemView.findViewById(R.id.main_screen_contact_name);
            number = itemView.findViewById(R.id.main_screen_contact_number);
            amount = itemView.findViewById(R.id.main_screen_contact_amount);
            reminder = itemView.findViewById(R.id.main_screen_contact_reminder);
            line = itemView.findViewById(R.id.main_screen_contact_line);
            rupees= itemView.findViewById(R.id.main_screen_contact_amount_rupees);
        }
    }
}
