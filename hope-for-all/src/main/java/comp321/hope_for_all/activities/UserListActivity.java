package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PastChatAdapter;

public class UserListActivity extends AppCompatActivity {

    private PastChatAdapter pastChatAdapter;
    private RecyclerView rv_chat_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("User List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initPastHistory();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPastHistory() {
        if(pastChatAdapter == null){
            pastChatAdapter = new PastChatAdapter(this, true);
        }

        rv_chat_list = findViewById(R.id.rv_chat_list);
        rv_chat_list.setLayoutManager(new LinearLayoutManager(this));
        rv_chat_list.setHasFixedSize(false);
        rv_chat_list.setAdapter(pastChatAdapter);

    }

}