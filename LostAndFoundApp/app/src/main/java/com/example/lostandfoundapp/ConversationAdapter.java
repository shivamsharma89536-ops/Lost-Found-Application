package com.example.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.lostandfoundapp.models.Conversation;
import com.example.lostandfoundapp.models.User;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private Context context;
    private List<Conversation> conversationList;
    private String currentUserId;

    public ConversationAdapter(Context context, List<Conversation> conversationList, String currentUserId) {
        this.context = context;
        this.conversationList = conversationList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_row_layout, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation convo = conversationList.get(position);

        // --- Find the other user (your existing code) ---
        User otherUser = null;
        if (convo.getParticipants() != null) {
            for (User user : convo.getParticipants()) {
                if (user != null && !user.getId().equals(currentUserId)) {
                    otherUser = user;
                    break; // Found them
                }
            }
        }

        // --- Set text/image (your existing code) ---
        if (otherUser != null) {
            holder.tvUserName.setText(otherUser.getName());
            Glide.with(context)
                    .load(otherUser.getProfileImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .circleCrop()
                    .into(holder.ivProfile);
        } else {
            holder.tvUserName.setText("Unknown User");
        }

        if (convo.getLastMessage() != null) {
            holder.tvLastMessage.setText(convo.getLastMessage().getContent());
            holder.tvTimestamp.setText(convo.getLastMessage().getCreatedAt());
        } else {
            holder.tvLastMessage.setText("No messages yet...");
            holder.tvTimestamp.setText("");
        }

        // --- THIS IS THE FIX ---
        // Create a final copy of otherUser for the lambda
        final User finalOtherUser = otherUser;

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("CONVERSATION_ID", convo.getId());

            // Use the final variable here
            if (finalOtherUser != null) {
                intent.putExtra("OTHER_USER_NAME", finalOtherUser.getName());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversationList = conversations;
        notifyDataSetChanged();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvUserName, tvLastMessage, tvTimestamp;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}