package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context context;
    private List<MessageModel> messageModelList;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messageModelList = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void add(MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    public void clear() {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View view = inflater.inflate(R.layout.message_row_sent, parent, false);
            return new MyViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.message_row_received, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.textViewSentMessage.setText(messageModel.getMessage());
        } else {
            holder.textViewReceivedMessage.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public List<MessageModel> getMessageModelList() {
        return messageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView textViewSentMessage, textViewReceivedMessage;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.textViewSentMessage);
            textViewReceivedMessage = itemView.findViewById(R.id.textViewReceivedMessage);
        }
    }
}