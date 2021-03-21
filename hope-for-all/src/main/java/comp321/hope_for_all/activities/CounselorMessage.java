package comp321.hope_for_all.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PastChatAdapter;

public class CounselorMessage extends AppCompatActivity {

    private PastChatAdapter pastChatAdapter;
    private RecyclerView rv_chat_list;
    private FloatingActionButton fab_new_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        fab_new_chat = findViewById(R.id.fab_new_chat);

        fab_new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounselorMessage.this, UserListActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigation();
        initPastHistory();
    }

    private void initPastHistory() {
        if(pastChatAdapter == null){
            pastChatAdapter = new PastChatAdapter(this, false);
        }

        rv_chat_list = findViewById(R.id.rv_chat_list);
        rv_chat_list.setLayoutManager(new LinearLayoutManager(this));
        rv_chat_list.setHasFixedSize(false);
        rv_chat_list.setAdapter(pastChatAdapter);

    }

    private void bottomNavigation() {

        setTitle("Chat Message");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.messageNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeNav:
                        startActivity(new Intent(getApplicationContext(), MainCounselor.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.messageNav:
                        return false;
                    case R.id.profileNav:
                        startActivity(new Intent(getApplicationContext(), CounselorProfile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
