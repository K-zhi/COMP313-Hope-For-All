package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.ChatMessageAdapter;

public class ChatActivity extends AppCompatActivity {

    private ChatMessageAdapter chatMessageAdapter;
    private RecyclerView rv_chat_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rv_chat_data = findViewById(R.id.rv_chat_data);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        if(getIntent()!=null){
            if(!getIntent().getStringExtra("UserName").isEmpty()){
                setToolbar(getIntent().getStringExtra("UserName"));
                prepareChat();
            }
        }
    }

    private void setToolbar(String username){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(username);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void prepareChat(){
        if(chatMessageAdapter == null){
            chatMessageAdapter = new ChatMessageAdapter(this);
        }

        rv_chat_data.setLayoutManager(new LinearLayoutManager(this));
        rv_chat_data.setAdapter(chatMessageAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}