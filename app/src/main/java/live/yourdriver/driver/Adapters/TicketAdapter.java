package live.yourdriver.driver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import live.yourdriver.driver.Activity.SupportActivity;
import live.yourdriver.driver.Model.SupportTicket;
import live.yourdriver.driver.R;;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<SupportTicket> tickets;

    public TicketAdapter(Context context, List<SupportTicket> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.support_ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketViewHolder holder, int position) {
        final SupportTicket ticket = tickets.get(position);


        holder.tvTicketStatus.setText((ticket.getIsClosed()==1?"Closed":"Open"));
        holder.itemView.setTag(R.id.support_ticket_id,ticket.getId());
        holder.itemView.setTag(R.id.support_ticket_subject,ticket.getTitle());

        holder.tvTicketTitle.setText(String.valueOf(ticket.getTitle()));
        if(ticket.getIsUnread()==1){
            holder.tvTicketTitle.setTypeface(null, Typeface.BOLD);
        }else{
            holder.tvTicketTitle.setTypeface(null);
        }
        holder.tvTicketId.setText(String.valueOf(ticket.getId()));

        holder.tvTicketCreatedAt.setText(String.valueOf(ticket.getCreatedAt()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvTicketTitle.setTypeface(null);
                Intent intent = new Intent(context, SupportActivity.class);
                intent.putExtra("ticket_id", v.getTag(R.id.support_ticket_id).toString());
                intent.putExtra("subject", v.getTag(R.id.support_ticket_subject).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView tvTicketStatus,tvTicketTitle,tvTicketCreatedAt,tvTicketId;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketStatus = itemView.findViewById(R.id.tv_ticket_status);
            tvTicketTitle = itemView.findViewById(R.id.tv_ticket_title);
            tvTicketCreatedAt = itemView.findViewById(R.id.tv_ticket_created_at);
            tvTicketId = itemView.findViewById(R.id.tv_ticket_id);
        }
    }
}
