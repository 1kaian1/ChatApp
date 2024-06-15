package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    String receiverId, receiverName;
    String senderId, senderName, senderRoom, receiverRoom;
    Long numMessages;
    DatabaseReference dbReferenceSender, dbRefereceReceiver, userReference;
    ImageView sendBtn;
    EditText messageText;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userReference = FirebaseDatabase.getInstance().getReference("user");
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        Objects.requireNonNull(getSupportActionBar()).setTitle(receiverName);
        if (receiverId != null) {
            senderRoom = FirebaseAuth.getInstance().getUid()+receiverId;
            receiverRoom = receiverId+FirebaseAuth.getInstance().getUid();
        }
        sendBtn = findViewById(R.id.sendMessageIcon);
        messageAdapter = new MessageAdapter(this);
        recyclerView = findViewById(R.id.chatrecycler);
        messageText = findViewById(R.id.messageEdit);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        dbRefereceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        dbReferenceSender.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }
                numMessages = snapshot.getChildrenCount();
                messageAdapter.clear();
                for (MessageModel message : messages) {
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageText.getText().toString();
                if (!message.trim().isEmpty()) {
                    SendMessage(message);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendMessage(String message) {
        Long messageId = numMessages + 1;
        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), message);
        messageAdapter.add(messageModel);
        dbReferenceSender.child(String.valueOf(messageId)).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {}
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });
        dbRefereceReceiver.child(String.valueOf(messageId)).setValue(messageModel);
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
        messageText.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        return false;
    }
}