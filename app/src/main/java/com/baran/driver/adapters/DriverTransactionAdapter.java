package com.baran.driver.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baran.driver.Fragments.driver.transactions.TransactionDetailsFragment;
import com.baran.driver.Model.DriverTransaction;
import com.baran.driver.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class DriverTransactionAdapter extends RecyclerView.Adapter<DriverTransactionAdapter.RideViewHolder> {

    private Context context;
    private List<DriverTransaction> transactions;

    public DriverTransactionAdapter(Context context, List<DriverTransaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_item, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        final DriverTransaction transaction = transactions.get(position);
        holder.transactionId.setText(String.valueOf(transaction.getId()));
        holder.itemView.setTag(transaction.getId());
        holder.transactionAmount.setText(String.valueOf(transaction.getTotalFare()));
        holder.transactionDate.setText(String.valueOf(transaction.getCreatedAt()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("transactionId",Integer.valueOf(v.getTag().toString()));
                TransactionDetailsFragment transactionDetailsFragment = new TransactionDetailsFragment();
                transactionDetailsFragment.setArguments(b);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.d_nav_host_fragment, transactionDetailsFragment).addToBackStack("@@").commit();
            }
        });
    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {

        TextView transactionId,transactionDate,transactionAmount;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionId = itemView.findViewById(R.id.trans_id);
            transactionDate = itemView.findViewById(R.id.trans_date);
            transactionAmount = itemView.findViewById(R.id.trans_amount);
        }
    }
}
