package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import comp321.hope_for_all.R;

public class Message extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String nickName;

    private FloatingActionButton fabMain, fabSub1, fabSub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connected to new custom title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_message_title);

        Intent intent = getIntent();
        if(intent.getExtras().getString("UserName") != null)
            nickName = intent.getExtras().getString("UserName");

        mListView = (ListView) findViewById(R.id.lvMessage);
        listMessageSetting();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabMain = (FloatingActionButton) findViewById(R.id.FloatingBtnMain);
        fabMain.setOnClickListener(this);

        bottomNavigation();
    }

    private void listMessageSetting() {

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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.messageNav:
                        return false;
                    case R.id.profileNav:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FloatingBtnMain :
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("UserName", nickName);
                startActivity(intent);

                break;
        }
    }

    private void toggleFab() {
        if(isFabOpen) {
            fabMain.setImageResource(R.drawable.ic_chat);
            fabSub1.startAnimation(fab_close);
            fabSub2.startAnimation(fab_close);
            fabSub1.setClickable(false);
            fabSub2.setClickable(false);
            isFabOpen = false;
        } else {
            fabMain.setImageResource(R.drawable.ic_close);
            fabSub1.startAnimation(fab_open);
            fabSub2.startAnimation(fab_open);
            fabSub1.setClickable(true);
            fabSub2.setClickable(true);
            isFabOpen = true;
        }
    }
}