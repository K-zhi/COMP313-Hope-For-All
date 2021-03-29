package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.CounselorListAdapter;
import comp321.hope_for_all.adapter.MessageListAdapter;
import comp321.hope_for_all.adapter.UserListAdapter;
import comp321.hope_for_all.models.ChatData;
import comp321.hope_for_all.models.Counselor;
import comp321.hope_for_all.models.User;

import static androidx.core.content.ContextCompat.startActivity;

public class Message extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private static final String TAG = "Message";
    private String userName;
    private String uid;
    private String chatKey;

    private FloatingActionButton fabMain, fabSub1, fabSub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    // #K: For show the list of chats
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> listChatRoom;

    // # Custom Alert Dialog
    public static ListView listView;
    // #K: To be related with the list of Users
    private static UserListAdapter userListAdapter;
    private List<User> listUserInfo;

    // #K: Related with the list of Counselors
    private static CounselorListAdapter counselorAdapter;
    private static List<Counselor> listCounselors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connected to new custom title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_message_title);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatRooms");

        Intent intent = getIntent();
        if(intent.getExtras().getString("UserName") != null)
            userName = intent.getExtras().getString("UserName");
        if(intent.getExtras().getString("Uid") != null)
            uid = intent.getExtras().getString("Uid");

        mRecyclerView = (RecyclerView) findViewById(R.id.chatRoomRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        listChatRoom = new ArrayList<>();
        mAdapter = new MessageListAdapter(uid);
        mRecyclerView.setAdapter(mAdapter);

        // Get the list of chats from Firebase;
        getChatRoomList();

        if(listUserInfo != null)
            userListAdapter = new UserListAdapter(this, listUserInfo);
        else {
            listUserInfo = new ArrayList<>();
            userListAdapter = new UserListAdapter(this);
        }

        if(listCounselors != null)
            counselorAdapter = new CounselorListAdapter(this, listCounselors);
        else {
            listCounselors = new ArrayList<>();
            counselorAdapter = new CounselorListAdapter(this);
        }

        // Get the users data from Firebase
        getUsersInfo();
        // # Get the counselors data from Firebase
        getCounselorsInfo();

        fabMain = (FloatingActionButton) findViewById(R.id.FloatingBtnMain);
        fabMain.setOnClickListener(this);

        bottomNavigation();
    }

    private void getChatRoomList() {
        //String chatKey = "Group" + userName.substring(0, 1);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // getValue : Read data from Firebase
                Log.d(TAG, "## onChildAdded: " + snapshot.getKey());

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ChatData room = userSnapshot.getValue(ChatData.class);

                    if (room != null) {
                        //user.uid = userSnapshot.getKey();
                        listChatRoom.add(room);
                        ((MessageListAdapter)mAdapter).addRoom(room);
                    }
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
                Log.w(TAG, "## onCancelled: " + error);
                Toast.makeText(getApplicationContext(), "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        });
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
                        return true;

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
                //dialogUserList = new ArrayList<>();
                listUserInfo = new ArrayList<>();

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
                listView = (ListView) view.findViewById(R.id.alertDialogUserList);
                listView.setAdapter(userListAdapter);

                // Create Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Set Layout
                builder.setView(view);
                // Confirm & Cancel button
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // Set Icon
                builder.setIcon(R.drawable.ic_hope);
                // Set Title
                builder.setTitle("Choose an user to talk :)");
                builder.show();

                final AlertDialog dialog = builder.create();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(listUserInfo == null  || listUserInfo.size() == 0) {
                            if(userListAdapter.getUserList() != null && userListAdapter.getUserList().size() > 0){
                                listUserInfo = userListAdapter.getUserList();
                            }
                        }

                        if (position != -1) {
                            Intent intent = new Intent(getApplicationContext(), Chat.class);

                            String key = listUserInfo.get(position).uid;
                            // Check Chat Data key in Database
                            chatKey = listUserInfo.get(position).uid + uid;
                            Boolean isExist = ((MessageListAdapter)mAdapter).checkIsExistKey(chatKey);
                            chatKey = isExist == false ? chatKey = uid + listUserInfo.get(position).uid : chatKey;

                            intent.putExtra("UserName", userName);
                            intent.putExtra("Uid", uid);
                            intent.putExtra("OpponentId", listUserInfo.get(position).uid);
                            intent.putExtra("OpponentName", listUserInfo.get(position).userName);
                            intent.putExtra("RoomKey", chatKey);

                            ActivityOptions activityOptions = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.from_right, R.anim.from_left);
                                startActivity(intent, activityOptions.toBundle());
                            }
                        }

                        dialog.dismiss();
                    }
                });

                break;
        }
    }

    // #D: Make the list of Users
    private void getUsersInfo() {
        // Get the users data from Firebase
        FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        user.uid = userSnapshot.getKey();
                        // #D: if there is an current user, delete the item in the list of users
                        if(!uid.equals(user.uid)) {
                            listUserInfo.add(user);
                            userListAdapter.setUser(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // #K: Make the list of Counselors
    private void getCounselorsInfo() {
        // Get the Counselors data from Firebase
        FirebaseDatabase.getInstance().getReference("Counselors").orderByChild("c_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot counselorSnapshot : snapshot.getChildren()) {
                    Counselor counselor = counselorSnapshot.getValue(Counselor.class);

                    if(counselor != null) {
                        listCounselors.add(counselor);
                        counselorAdapter.setCounselor(counselor);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}