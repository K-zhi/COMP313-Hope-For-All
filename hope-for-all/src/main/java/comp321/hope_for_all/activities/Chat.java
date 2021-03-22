package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.ChatAdapter;
import comp321.hope_for_all.models.ChatData;

public class Chat extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    // After change it
    private String nick = "Nick2";

    private EditText editTxtChat;
    private Button btnSendChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        if(intent.getExtras().getString("UserName") != null)
            nick = intent.getExtras().getString("UserName");

        btnSendChat = findViewById(R.id.btnSendChat);
        editTxtChat = findViewById(R.id.editTxtChat);
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTxtChat.getText() != null) {
                    String message = editTxtChat.getText().toString();

                    // Caution, if the data's format is nor correct, have to delete it from Firebase
                    if(message != null) {
                        ChatData chat = new ChatData();
                        chat.setNickname(nick);
                        chat.setMsg(message);
                        myRef.push().setValue(chat);
                    }
                }
            }
        });

        // Recycle the view continuously
        mRecyclerView = findViewById(R.id.my_recycler_view);
        // To improve performance if know that changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList();
        mAdapter = new ChatAdapter(chatList, Chat.this, nick);
        mRecyclerView.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Message");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Chat", snapshot.getValue().toString());

                if(snapshot.getValue() != null) {
                    ChatData chat = snapshot.getValue(ChatData.class);
                    ((ChatAdapter) mAdapter).addChat(chat);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}