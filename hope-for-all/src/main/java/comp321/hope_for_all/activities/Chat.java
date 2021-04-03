package comp321.hope_for_all.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.ChatAdapter;
import comp321.hope_for_all.models.ChatData;

public class Chat extends AppCompatActivity {
    DatabaseReference mDatabaseRef;

    private Toolbar mToolbar;
    private static final String TAG = "Chat ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;

    private String uid;
    private String uName;
    private String oppId;
    private String oppName;
    private String chatKey;
    private Boolean isExist = false;
    private String currDateStr;

    private EditText editTxtChat;
    private Button btnSendChat;
    private LinearLayout layout;

    private FirebaseUser user;
    private String userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.chatLayout);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ChatRooms");

        Intent intent = getIntent();
        if(intent.hasExtra("UserName"))
            uName = intent.getExtras().getString("UserName");
        if(intent.hasExtra("Uid"))
            uid = intent.getExtras().getString("Uid");
        if(intent.hasExtra("OpponentId"))
            oppId = intent.getExtras().getString("OpponentId");
        if(intent.hasExtra("OpponentName"))
            oppName = intent.getExtras().getString("OpponentName");
        if(intent.hasExtra("RoomKey"))
            chatKey = intent.getExtras().getString("RoomKey");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chatting with " + intent.getExtras().getString("OpponentName"));

        btnSendChat = findViewById(R.id.btnSendChat);
        editTxtChat = findViewById(R.id.editTxtChat);
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTxtChat.getText() != null) {
                    String message = editTxtChat.getText().toString();

                    if(message != null) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
                        String strNow = sdfNow.format(date);

                        ChatData chat = new ChatData();
                        chat.setUid(uid);
                        chat.setUserName(uName);
                        chat.setOpponentId(oppId);
                        chat.setOpponentName(oppName);
                        chat.setMsg(message);
                        chat.setDate(strNow);
                        FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(chatKey).push().setValue(chat);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    }
                }
            }
        });


        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList();
        mAdapter = new ChatAdapter(Chat.this, chatList, uName);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);


        mDatabaseRef.child(chatKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Chat: ", snapshot.getValue().toString());
                if(snapshot.getValue() != null) {
                    ChatData chat = snapshot.getValue(ChatData.class);
                    ((ChatAdapter) mAdapter).addChat(chat);
                    editTxtChat.setText("");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "## onChildChanged: " + snapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "## onChildRemoved: " + snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "## onChildMoved: " + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "## onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), Message.class);
        intent.putExtra("UserName", userName);
        intent.putExtra("Uid", userID);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();


    }
}