package com.example.udhari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class _6_More_Info_Adapter extends RecyclerView.Adapter<_6_More_Info_Adapter.ViewHolder> {
    Context context;
    ArrayList<_2_TransactionInfo>transaction_of_people_list=new ArrayList<>();
    public static int index=0;
    _6_More_Info_Adapter(Context context, ArrayList<_2_TransactionInfo>list,int index)
    {
        this.context=context;
        this.transaction_of_people_list=list;
        this.index=index;
        notifyDataSetChanged();
    }
    void dataChange(ArrayList<_2_TransactionInfo>list)
    {
        this.transaction_of_people_list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public _6_More_Info_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout._2_more_info_screen_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull _6_More_Info_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        _2_TransactionInfo temp=transaction_of_people_list.get(position);

        if(temp.getInfo().equals("")) {}
        else
        {
            holder.transaction_note.setVisibility(View.VISIBLE);
            holder.transaction_note.setText(temp.getInfo());
        }
        if(temp.getAmount()>0)
        {
            holder.transaction_amount.setText(MainActivity.convert_amount(temp.getAmount()));
            holder.transaction_amount.setTextColor(Color.parseColor("#CD0909"));
            holder.rupees.setTextColor(Color.parseColor("#CD0909"));
            holder.date_time.setText("Paid on "+temp.getDate()+" "+temp.getTime());

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.line.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            holder.line.setLayoutParams(params);

        }else
        {
            holder.transaction_amount.setText(MainActivity.convert_amount(-temp.getAmount()));
            holder.transaction_amount.setTextColor(Color.parseColor("#4BAF50"));
            holder.rupees.setTextColor(Color.parseColor("#4BAF50"));
            holder.date_time.setText("Received on "+temp.getDate()+" "+temp.getTime());

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.line.getLayoutParams();
            params.gravity = Gravity.LEFT;
            holder.line.setLayoutParams(params);
        }
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, _7_Add_New_Transaction.class);
                intent.putExtra("main_index",index);
                intent.putExtra("delete",1);
                intent.putExtra("transaction_index",position);
                context.startActivity(intent);

            }
        });
        holder.line.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Transaction");
                builder.setMessage("Are you sure you want to delete this Transaction?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _3_DataBase dbHelper = new _3_DataBase(context);
                        notifyDataSetChanged();
                        transaction_of_people_list.remove(position);
                        MainActivity.list.get(_5_More_Info.temp).setTransaction(transaction_of_people_list);
                        _5_More_Info.set_text_amount();
                        MainActivity.mainActivity_adapter.dataChange(MainActivity.list);
                        MainActivity.set_give_get(MainActivity.list);
                        dbHelper.updateData(MainActivity.list.get(_5_More_Info.temp).getName(),MainActivity.list.get(_5_More_Info.temp).getNumber(),MainActivity.list.get(_5_More_Info.temp).getAmount(),transaction_of_people_list);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaction_of_people_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_time, transaction_amount, transaction_note,rupees;
        CardView line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            line=itemView.findViewById(R.id.transaction_line);
            date_time=itemView.findViewById(R.id.more_info_transaction_date);
            transaction_amount=itemView.findViewById(R.id.transaction_amount);
            transaction_note=itemView.findViewById(R.id.more_info_transaction_reason);
            rupees=itemView.findViewById(R.id.transaction_amount_rupees);
        }
    }
}
