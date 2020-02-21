package live.yourdriver.driver.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Fragments.driver.transactions.TransactionDetailsFragment;


import live.yourdriver.driver.Model.DriverTransaction;
import live.yourdriver.driver.R;;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class DriverTransactionAdapter extends RecyclerView.Adapter<DriverTransactionAdapter.RideViewHolder> {

    private Context context;
    private List<DriverTransaction> transactions;
    public static AppPreference appPreference;


    public DriverTransactionAdapter(Context context, List<DriverTransaction> transactions) {
        this.context = context;
        this.transactions = transactions;
        appPreference = new AppPreference(context);

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
        if(transaction.getTransactionType().equals("CREDIT")){
            holder.transactionAmount.setText(String.valueOf(transaction.getAmountReceived()));
        }else{
            holder.transactionAmount.setText(String.valueOf(transaction.getTotalFare()));
        }

        holder.transactionDate.setText(String.valueOf(transaction.getCreatedAt()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("transactionId",Integer.valueOf(v.getTag().toString()));


                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

                if(appPreference.getUserObjectWithoutUserValidation().getId()==transaction.getPassengerId()){
                    live.yourdriver.driver.Fragments.passenger.transactions.TransactionDetailsFragment transactionDetailsFragment =
                            new live.yourdriver.driver.Fragments.passenger.transactions.TransactionDetailsFragment();
                    transactionDetailsFragment.setArguments(b);
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, transactionDetailsFragment).addToBackStack("@@").commit();
                }else{
                    TransactionDetailsFragment transactionDetailsFragment = new TransactionDetailsFragment();
                    transactionDetailsFragment.setArguments(b);
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, transactionDetailsFragment).addToBackStack("@@").commit();
                }

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
