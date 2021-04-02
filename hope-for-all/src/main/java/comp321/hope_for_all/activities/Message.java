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
import com.google.protobuf.StringValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private FirebaseDatabase database;

    private static final String TAG = "Message";
    private String userName;
    private String uid;
    private String chatKey;
    public Boolean isExistKey = false;

    private FloatingActionButton fabMain, fabCounselor;
    private boolean isFabOpen = false;
    private AlertDialog.Builder builder;

    // #K: For show the list of chats
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> listChatRoom;
    private List<ChatData> listChatRoomKeys;

    // # Custom Alert Dialog
    public ListView listView;
    // #K: To be related with the list of Users
    private UserListAdapter userListAdapter;
    private List<User> listUserInfo;

    // #K: Related with the list of Counselors
    private CounselorListAdapter counselorAdapter;
    private List<Counselor> listCounselors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connected to new custom title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setTitle(R.string.chats);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_message_title);

        // Initialize database valuable
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("UserName"))
            userName = intent.getExtras().getString("UserName");
        if (intent.hasExtra("Uid"))
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

        if (listUserInfo != null)
            userListAdapter = new UserListAdapter(this, listUserInfo);
        else {
            listUserInfo = new ArrayList<>();
            userListAdapter = new UserListAdapter(this);
        }

        if (listCounselors != null)
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

        fabCounselor = (FloatingActionButton)findViewById(R.id.FloatingBtnCou);
        fabCounselor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCounselorsList();
            }
        });

        bottomNavigation();
    }

    private void getChatRoomList() {
        List<ChatData> tempList = new ArrayList<>();

        // #K: Step0. Read the data from our database
        database.getReference("ChatRooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // getValue : Read data from Firebase
                Log.d(TAG, "## onChildAdded: " + snapshot.getKey());

                List<ChatData> compList = new ArrayList<>();

                // Read all data about messages from database
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ChatData room = userSnapshot.getValue(ChatData.class);

                    if(room != null) {
                        compList.add(room);
                    }
                }

                // ##### Filtering to be connected with current user
                String listId, saveId;

                saveId = "";
                listChatRoom = new ArrayList<>();

                for(int i = 0; i < compList.size(); i++) {
                    if(compList.get(i).getUid().equals(uid) || compList.get(i).getOpponentId().equals(uid)){
                        if(compList.get(i).getUid().equals(uid)) {
                            listId = compList.get(i).getOpponentId();
                        } else {
                            listId = compList.get(i).getUid();
                        }

                        if(listId.equals(saveId)) {
                            listChatRoom.remove(listChatRoom.size() - 1);
                        }

                        listChatRoom.add(compList.get(i));
                        saveId = listId;
                    }
                }

                for(int i = 0; i < listChatRoom.size(); i++) {
                    ((MessageListAdapter) mAdapter).addRoom(listChatRoom.get(i));
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

    //
    private void getChatRoomKeys() {
        database.getReference("ChatRoomKeys").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listChatRoomKeys = new ArrayList<>();
                // getValue : Read data from Firebase
                Log.d(TAG, "## onChildAdded about ChatRoom Keys: " + snapshot.getKey());

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ChatData room = userSnapshot.getValue(ChatData.class);

                    if(room != null) {
                        listChatRoomKeys.add(room);
                    }
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

    // For converting string value to Date
    private Date convertStrDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = sdf.parse(strDate);

        }catch (ParseException ex) {
            Log.v("Parse Exception: ", ex.getLocalizedMessage());
        }

        return date;
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
                        finish();
                        return true;

                    case R.id.messageNav:
                        return true;

                    case R.id.profileNav:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FloatingBtnMain:
                //dialogUserList = new ArrayList<>();
                listUserInfo = new ArrayList<>();

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
                listView = (ListView) view.findViewById(R.id.alertDialogUserList);
                listView.setAdapter(userListAdapter);

                // Create Dialog
                createDialogSetting(view, "user");
                final AlertDialog dialog = builder.create();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (listUserInfo == null || listUserInfo.size() == 0) {
                            if (userListAdapter.getUserList() != null && userListAdapter.getUserList().size() > 0) {
                                listUserInfo = userListAdapter.getUserList();
                            }
                        }

                        if (position != -1) {
                            Intent intent = new Intent(getApplicationContext(), Chat.class);

                            // Check Chat Data key in Database
                            chatKey = listUserInfo.get(position).uid + uid;

                            // Check Chat Data key in Database
                            database.getReference("ChatRooms").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    isExistKey = false;
                                    for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                        if (chatSnapshot.getKey().equals(chatKey)) {
                                            isExistKey = true;
                                            break;
                                        } else
                                            isExistKey = false;
                                    }

                                    // Todo: delete ' == true'
                                    chatKey = isExistKey ? chatKey : uid + listUserInfo.get(position).uid;

                                    // Make ChatRoomKey Table
                                    if(isExistKey == false) {
                                        ChatData chatKeyId = new ChatData();
                                        chatKeyId.setUid(uid);
                                        chatKeyId.setOpponentId(listUserInfo.get(position).uid);
                                        database.getReference().child("ChatRoomKeys").child(chatKey).push().setValue(chatKeyId);
                                    }

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

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        dialog.dismiss();
                    }
                });

                break;
        }
    }

    // #D: To make the list of counselors in Custom dialog
    private void makeCounselorsList() {
        listCounselors = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
        listView = (ListView) view.findViewById(R.id.alertDialogUserList);
        listView.setAdapter(counselorAdapter);

        // Create Dialog
        createDialogSetting(view, "counselor");
        final AlertDialog dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listCounselors == null || listCounselors.size() == 0) {
                    if (counselorAdapter.getCounselorList() != null && counselorAdapter.getCounselorList().size() > 0) {
                        listCounselors = counselorAdapter.getCounselorList();
                    }
                }

                if (position != -1) {
                    Intent intent = new Intent(getApplicationContext(), Chat.class);

                    // Check Chat Data key in Database
                    chatKey = listCounselors.get(position).getCid() + uid;

                    // Check Chat Data key in Database
                    FirebaseDatabase.getInstance().getReference("ChatRooms").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isExistKey = false;
                            for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                if (chatSnapshot.getKey().equals(chatKey)) {
                                    isExistKey = true;
                                    break;
                                } else
                                    isExistKey = false;
                            }

                            // Todo: delete ' == true'
                            chatKey = isExistKey ? chatKey : uid + listCounselors.get(position).getCid();

                            // Make ChatRoomKey Table
                            if(isExistKey == false) {
                                ChatData chatKeyId = new ChatData();
                                chatKeyId.setUid(uid);
                                chatKeyId.setOpponentId(listCounselors.get(position).getCid());
                                database.getReference().child("ChatRoomKeys").child(chatKey).push().setValue(chatKeyId);
                            }

                            intent.putExtra("UserName", userName);
                            intent.putExtra("Uid", uid);
                            intent.putExtra("OpponentId", listCounselors.get(position).getCid());
                            intent.putExtra("OpponentName", listCounselors.get(position).getC_name());
                            intent.putExtra("RoomKey", chatKey);

                            ActivityOptions activityOptions = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                //activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                dialog.dismiss();
            }
        });
    }

    private void createDialogSetting(View view, String role) {
        // Create Dialog
        builder = new AlertDialog.Builder(this);

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
        String title = String.format("Choose a %s to talk :)", role);
        builder.setTitle(title);
        builder.show();
    }

    // #D: Make the list of Users
    private void getUsersInfo() {
        // Get the users data from Firebase
        database.getReference("Users").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        user.uid = userSnapshot.getKey();
                        // #D: if there is an current user, delete the item in the list of users
                        if (uid != null && !uid.equals(user.uid)) {
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
        database.getReference("Counselors").orderByChild("c_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot counselorSnapshot : snapshot.getChildren()) {
                    Counselor counselor = counselorSnapshot.getValue(Counselor.class);

                    if (counselor != null) {
                        counselor.setCid(counselorSnapshot.getKey());
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