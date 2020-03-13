package live.yourdriver.driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Model.RideAlert;
import live.yourdriver.driver.R;

;

public class RideAlertAdapter extends RecyclerView.Adapter<RideAlertAdapter.RideViewHolder> {

    private Context context;
    private List<RideAlert> rideAlerts;
    public static AppPreference appPreference;


    public RideAlertAdapter(Context context, List<RideAlert> rideAlerts) {
        this.context = context;
        this.rideAlerts = rideAlerts;
        appPreference = new AppPreference(context);

    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ride_alert_item, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        final RideAlert alert = rideAlerts.get(position);


         holder.alertId.setText(String.valueOf(alert.getId()));
         holder.alertDate.setText(String.valueOf(alert.getCreatedAt()));

         if(alert.getIsRejected()==1){
             holder.alertImage.setImageResource(R.drawable.reject);
         }else if(alert.getIsAccepted()==1){
             holder.alertImage.setImageResource(R.drawable.accept);
         }else if(alert.getFirebaseRequestReceived()==0){
             holder.alertImage.setImageResource(R.drawable.noconnection);
         }else{
             holder.alertImage.setImageResource(R.drawable.missed);
         }


//        holder.transactionId.setText(String.valueOf(transaction.getId()));
//        holder.itemView.setTag(transaction.getId());
//        if(transaction.getTransactionType().equals("CREDIT")){
//            holder.transactionAmount.setText(String.valueOf(transaction.getAmountReceived()));
//        }else{
//            holder.transactionAmount.setText(String.valueOf(transaction.getTotalFare()));
//        }
//
//        holder.transactionDate.setText(String.valueOf(transaction.getCreatedAt()));

    }



    @Override
    public int getItemCount() {
        return rideAlerts.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {

        TextView alertId,alertDate;
        ImageView alertImage;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            alertId = itemView.findViewById(R.id.alert_id);
            alertDate = itemView.findViewById(R.id.alert_date);
            alertImage = itemView.findViewById(R.id.alert_image);
        }
    }
}
