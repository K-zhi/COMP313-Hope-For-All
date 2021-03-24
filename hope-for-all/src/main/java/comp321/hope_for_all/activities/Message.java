package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
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

    private FloatingActionButton fabMain, fabSub1, fabSub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> listChatRoom;

    //
    private static UserListAdapter userListAdapter;
    public static ListView listView;
    private List<User> listUserInfo;
    List<Map<String, String>> dialogUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connected to new custom title bar
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_message);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_message_title);

        Intent intent = getIntent();
        if(intent.getExtras().getString("UserName") != null)
            userName = intent.getExtras().getString("UserName");
        if(intent.getExtras().getString("Uid") != null)
            uid = intent.getExtras().getString("Uid");

        mRecyclerView = (RecyclerView) findViewById(R.id.chatRoomRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        listMessageSetting();

        if(listUserInfo != null)
            userListAdapter = new UserListAdapter(this, listUserInfo);
        else {
            listUserInfo = new ArrayList<>();
            userListAdapter = new UserListAdapter(this);
        }
        // Get the users data from Firebase
        getUsersInfo();

        fabMain = (FloatingActionButton) findViewById(R.id.FloatingBtnMain);
        fabMain.setOnClickListener(this);
    }

    private void listMessageSetting() {
        listChatRoom= new ArrayList<>();
        mAdapter = new MessageListAdapter();
        mRecyclerView.setAdapter(mAdapter);
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
                            if(userListAdapter.getUserList() != null && userListAdapter.getUserList().size() > 0)
                                listUserInfo = userListAdapter.getUserList();
                        }

                        if (position != -1) {
                            //Map.Entry<String, String> element = (Map.Entry<String, String>) dialogUserList.get(position).entrySet();

//                            String tempMessage = "Temp: " + listUserInfo.get(position).getName()
//                                    + " || " + listUserInfo.get(position).uid
//                                    + " || " + listUserInfo.get(position).userName;
//
//                            TextView temp = (TextView)view.findViewById(R.id.editTextSearchName);
//                            temp.setText(tempMessage);

                            Intent intent = new Intent(getApplicationContext(), Chat.class);
                            intent.putExtra("UserName", userName);
                            intent.putExtra("Uid", uid);
                            intent.putExtra("OpponentId", listUserInfo.get(position).uid);
                            intent.putExtra("OpponentName", listUserInfo.get(position).userName);

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
//                if(dialogUserList != null) {
//                    SimpleAdapter sAdapter = new SimpleAdapter(getApplicationContext(), dialogUserList,
//                    R.layout.custom_alert_dialog_user_item,
//                    new String[] {"userId", "userName"},
//                    new int[]{R.id.alertDialogIdItemTextView, R.id.alertDialogNameItemTextView});
//
//                    listView.setAdapter(sAdapter);
//
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    dialog.dismiss();
//
//                    if(position != -1) {
//                        Map.Entry<String, String> element = (Map.Entry<String, String>) dialogUserList.get(position).entrySet();
//
//                        Intent intent = new Intent(getApplicationContext(), Chat.class);
//                        intent.putExtra("UserName", userName);
//                        intent.putExtra("Uid", uid);
//                        intent.putExtra("OpponentId", element.getKey());
//                        intent.putExtra("OpponentName", element.getValue());
//
//                        ActivityOptions activityOptions = null;
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.from_right, R.anim.from_left);
//                            startActivity(intent, activityOptions.toBundle());
//                        }
//                    }
//                }
//            });
                // ## Original, but I want to change list type of dialog to show the list of users
//                FirebaseDatabase.getInstance().getReference("Counselors").orderByChild("c_name").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Counselor counselor = snapshot.getValue(Counselor.class);
//                        Log.d(TAG, "Read Counselors Info:success");
//                        if(counselor != null) {
//                            Intent intent = new Intent(getApplicationContext(), Chat.class);
//                            intent.putExtra("UserName", userName);
//                            intent.putExtra("Uid", uid);
//                            intent.putExtra("OpponentId", snapshot.getKey());
//                            intent.putExtra("OpponentName", counselor.getC_name());
//
//                            ActivityOptions activityOptions = null;
//                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.from_right, R.anim.from_left);
//                                startActivity(intent, activityOptions.toBundle());
////                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(Message.this, "Message: omething wrong happened!", Toast.LENGTH_LONG).show();
//                    }
//                });
        }
    }

    private void getUsersInfo() {
        // Get the users data from Firebase
        FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        user.uid = userSnapshot.getKey();
                        listUserInfo.add(user);
                        userListAdapter.setUser(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowAlertDialog() {
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.custom_alert_dialog_user_list, null);
//        listView = (ListView) view.findViewById(R.id.alertDialogUserList);
//        listView.setAdapter(userListAdapter);
//
//        // Create Dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        // Set Layout
//        builder.setView(view);
//        // Confirm & Cancel button
//        builder.setPositiveButton("Send", null);
//        builder.setNegativeButton("Cancel", null);
//        // Set Icon
//        builder.setIcon(R.drawable.ic_hope);
//        // Set Title
//        builder.setTitle("Choose an user to talk :)");
//        builder.show();
//        final AlertDialog dialog = builder.create();

//        if(dialogUserList != null) {
//            SimpleAdapter sAdapter = new SimpleAdapter(getApplicationContext(), dialogUserList,
//                    R.layout.custom_alert_dialog_user_item,
//                    new String[] {"userId", "userName"},
//                    new int[]{R.id.alertDialogIdItemTextView, R.id.alertDialogNameItemTextView});
//
//            listView.setAdapter(sAdapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    dialog.dismiss();
//
//                    if(position != -1) {
//                        Map.Entry<String, String> element = (Map.Entry<String, String>) dialogUserList.get(position).entrySet();
//
//                        Intent intent = new Intent(getApplicationContext(), Chat.class);
//                        intent.putExtra("UserName", userName);
//                        intent.putExtra("Uid", uid);
//                        intent.putExtra("OpponentId", element.getKey());
//                        intent.putExtra("OpponentName", element.getValue());
//
//                        ActivityOptions activityOptions = null;
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.from_right, R.anim.from_left);
//                            startActivity(intent, activityOptions.toBundle());
//                        }
//                    }
//                }
//            });
//       }
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