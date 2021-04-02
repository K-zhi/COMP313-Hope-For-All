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
    private DatabaseReference databaseReference;
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

    // # Custom Alert Dialog
    public static ListView listView;
    // #K: To be related with the list of Users
    private static UserListAdapter userListAdapter;
    private List<User> listUserInfo;

    // #K: Related with the list of Counselors
    private static CounselorListAdapter counselorAdapter;
    private List<Counselor> listCounselors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connected to new custom title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
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

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ChatData room = userSnapshot.getValue(ChatData.class);

                    if(room != null) {
                        listChatRoom.add(room);
                        ((MessageListAdapter) mAdapter).addRoom(room);
                    }
                }

                // #K: Step1. Check the list to show one thing each chat room
                String strDateCurr, strDateNext;
                String id, oppId, chatKey;
                Integer index = 0;

                if(listChatRoom != null && listChatRoom.size() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

                    List<ChatData> compList = new ArrayList<>(listChatRoom);

                    // Todo : Comment out temporarily
                    for(int i = 0; i < listChatRoom.size(); i++) {
                        // #K: Step3.0. Check RoomKey is the same
                        id = listChatRoom.get(index).getUid();
                        oppId = listChatRoom.get(index).getOpponentId();
                        strDateCurr = listChatRoom.get(index).getDate();

                        for(int j = 0; j < compList.size(); j++) {
                            if(compList.get(j).getUid().equals(id) && compList.get(j).getOpponentId().equals(oppId)) {
                                // #K: Step4. If RoomKey is the same, Compare the data
                                strDateNext = compList.get(j).getDate();

                                Date dateCur = convertStrDate(strDateCurr);
                                Date dateNext = convertStrDate(strDateNext);

                                // Step5: If current one is after the previous item, delete it and add the new one
                                if(dateCur.before(dateNext)) {
                                    listChatRoom.remove(compList.get(index));
                                    ((MessageListAdapter)mAdapter).deleteRoom(compList.get(index));

                                    id = compList.get(i).getUid();
                                    oppId = compList.get(i).getOpponentId();
                                    strDateCurr = compList.get(i).getDate();
                                    index = compList.size() - 1;
                                }
                            } else if(compList.get(i).getUid().equals(oppId) // #K: Step3.1. RoomKey's opponent id is the same as current UserId
                                    && compList.get(i).getOpponentId().equals(id)) {
                                // #K: Step4. If RoomKey is the same, Compare the data
                                strDateNext = compList.get(i).getDate();

                                Date dateCur = convertStrDate(strDateCurr);
                                Date dateNext = convertStrDate(strDateNext);

                                // Step5: If current one is after the previous item, delete it and add the new one
                                if(dateCur.before(dateNext)) {
                                    listChatRoom.remove(compList.get(index));
                                    ((MessageListAdapter)mAdapter).deleteRoom(compList.get(index));

                                    id = compList.get(i).getOpponentId();
                                    oppId = compList.get(i).getUid();
                                    strDateCurr = compList.get(i).getDate();
                                    index = compList.size() - 1;
                                }

                            }
                        }
                    }

                    //                    // Compare the list
//                    for(int i = 0; i < tempList.size(); i++) {
//                        id = tempList.get(i).getUid();
//                        oppId = tempList.get(i).getOpponentId();
//
//                        // #K: Step2. Check the chat list size
//                        if(filterList.size() == 0) {
//                            filterList.add(tempList.get(i));
//                            index = filterList.size() - 1;
//                        } else {
//                            // #K: Step3. Check RoomKey is the same or not
//                            for(int j = 0; j < filterList.size(); j++) {
//                                if((filterList.get(j).getUid().equals(id) && filterList.get(j).getOpponentId().equals(oppId))
//                                        || (filterList.get(j).getUid().equals(oppId) && filterList.get(j).getOpponentId().equals(id))) {
//                                    // #K: Step3. If RoomKey is the same or not
//                                    strDatePrev = filterList.get(j).getDate();
//                                    strDateCurr = tempList.get(i).getDate();
//
//                                    try {
//                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                                        Date datePrev = sdf.parse(strDatePrev);
//                                        Date dateCur = sdf.parse(strDateCurr);
//
//                                        // Step3: If current one is after the previous item, delete it and add the new one
//                                        if(datePrev.before(dateCur)) {
//                                            filterList.add(tempList.get(i));
//                                            filterList.remove(tempList.get(index));
//                                            index = filterList.size() - 1;
//                                        }
//
//                                    }catch (ParseException ex) {
//                                        Log.v("Parse Exception: ", ex.getLocalizedMessage());
//                                    }
//                                } else {
//                                    filterList.add(tempList.get(i));
//                                    index = filterList.size() - 1;
//                                }
//                            }
//                        }
//                    }
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
                                    chatKey = isExistKey == true ? chatKey : uid + listUserInfo.get(position).uid;

                                    intent.putExtra("UserName", userName);
                                    intent.putExtra("Uid", uid);
                                    intent.putExtra("OpponentId", listUserInfo.get(position).uid);
                                    intent.putExtra("OpponentName", listUserInfo.get(position).userName);
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
                            chatKey = isExistKey == true ? chatKey : uid + listCounselors.get(position).getCid();

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