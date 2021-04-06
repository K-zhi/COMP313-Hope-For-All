package comp321.hope_for_all.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.CounselorListAdapter;
import comp321.hope_for_all.adapter.MessageListAdapter;
import comp321.hope_for_all.adapter.UserListAdapter;
import comp321.hope_for_all.models.ChatData;
import comp321.hope_for_all.models.Counselor;
import comp321.hope_for_all.models.User;

public class Message extends AppCompatActivity {
    private FirebaseDatabase database;

    private static final String TAG = "Message";
    private String userName;
    private String uid;
    private String chatKey;
    public Boolean isExistKey = false;

    private AlertDialog.Builder builder;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> listChatRoom;
    private List<ChatData> listChatRoomKeys;

    public ListView listView;
    private UserListAdapter userListAdapter;
    private List<User> listUserInfo;

    private CounselorListAdapter counselorAdapter;
    private List<Counselor> listCounselors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().hide();

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

        getUsersInfo();

        getCounselorsInfo();

        bottomNavigation();

        upperNavigation();
    }


    private void getChatRoomList() {
        List<ChatData> tempList = new ArrayList<>();

        database.getReference("ChatRooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Log.d(TAG, "## onChildAdded: " + snapshot.getKey());

                List<ChatData> compList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ChatData room = userSnapshot.getValue(ChatData.class);

                    if(room != null) {
                        compList.add(room);
                    }
                }

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

    private void getChatRoomKeys() {
        database.getReference("ChatRoomKeys").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listChatRoomKeys = new ArrayList<>();

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

    private void upperNavigation() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.upper_navigation);
        bottomNavigationView.setSelectedItemId(R.id.usersList);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.usersList:
                        makeUsersList();
                        return true;

                    case R.id.counselorList:
                        makeCounselorsList();
                        return true;
                }
                return false;
            }
        });
    }


    private void bottomNavigation() {

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


    private void makeUsersList() {

        listUserInfo = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
        listView = (ListView) view.findViewById(R.id.alertDialogUserList);
        listView.setAdapter(userListAdapter);

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

                    chatKey = listUserInfo.get(position).uid + uid;

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

                            chatKey = isExistKey ? chatKey : uid + listUserInfo.get(position).uid;

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


    }

    public void makeCounselorsList() {
        listCounselors = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
        listView = (ListView) view.findViewById(R.id.alertDialogUserList);
        listView.setAdapter(counselorAdapter);

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

                    chatKey = listCounselors.get(position).getCid() + uid;

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

                            chatKey = isExistKey ? chatKey : uid + listCounselors.get(position).getCid();

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

        builder = new AlertDialog.Builder(this);

        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setIcon(R.drawable.ic_hope);

        String title = String.format("Choose a %s to talk :)", role);
        builder.setTitle(title);
        builder.show();
    }


    private void getUsersInfo() {

        database.getReference("Users").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        user.uid = userSnapshot.getKey();

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

    private void getCounselorsInfo() {

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