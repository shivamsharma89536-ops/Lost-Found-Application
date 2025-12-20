package com.example.lostandfoundapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfoundapp.models.Claim;
import java.util.List;

public class MyClaimsAdapter extends RecyclerView.Adapter<MyClaimsAdapter.ClaimViewHolder> {

    private Context context;
    private List<Claim> claimList;

    public MyClaimsAdapter(Context context, List<Claim> claimList) {
        this.context = context;
        this.claimList = claimList;
    }

    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.claim_row_layout, parent, false);
        return new ClaimViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimViewHolder holder, int position) {
        Claim claim = claimList.get(position);

        // TODO: The API should ideally return the item title.
        // For now, we only have the ID.
        holder.tvClaimItemTitle.setText("Claim for Item ID: " + claim.getItemId());
        holder.tvClaimDate.setText("Claimed on: " + claim.getCreatedAt());

        String status = claim.getStatus().toUpperCase();
        holder.tvClaimStatus.setText("Status: " + status);

        // Set status color
        switch (status) {
            case "APPROVED":
                holder.tvClaimStatus.setTextColor(Color.parseColor("#008000")); // Green
                break;
            case "REJECTED":
                holder.tvClaimStatus.setTextColor(Color.parseColor("#FF0000")); // Red
                break;
            case "PENDING":
            default:
                holder.tvClaimStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
                break;
        }

        // TODO: Add OnClickListener to open the chat or item details
    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    public void setClaims(List<Claim> claims) {
        this.claimList = claims;
        notifyDataSetChanged();
    }

    static class ClaimViewHolder extends RecyclerView.ViewHolder {
        TextView tvClaimItemTitle, tvClaimStatus, tvClaimDate;

        public ClaimViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClaimItemTitle = itemView.findViewById(R.id.tvClaimItemTitle);
            tvClaimStatus = itemView.findViewById(R.id.tvClaimStatus);
            tvClaimDate = itemView.findViewById(R.id.tvClaimDate);
        }
    }
}