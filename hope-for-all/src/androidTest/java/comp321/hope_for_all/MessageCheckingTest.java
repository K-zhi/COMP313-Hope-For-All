package comp321.hope_for_all;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import comp321.hope_for_all.activities.Message;
import comp321.hope_for_all.activities.UpdateUserProfile;
import comp321.hope_for_all.models.ChatData;
import comp321.hope_for_all.models.User;

import static org.junit.Assert.*;

public class MessageCheckingTest extends TestCase {
    private static final String TAG = "TEST MESSAGE CLASS:";
    private Boolean isExistKey = false;
    private User testUser1, testUser2;
    private List<ChatData> roomKeys;
    private FirebaseDatabase database;

    @Before
    public void setUp() throws Exception {
        testUser1 = new User();
        testUser1.uid = "Fs4zS9BsVNMgcWvvAP2WfbhbhVy1";

        testUser2 = new User();
        testUser2.uid = "2P66aMDXEKcLVKwy8r9A5VeYqXs2";

        roomKeys = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        database.getReference("ChatRoomKeys").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                    ChatData data = keySnapshot.getValue(ChatData.class);
                    roomKeys.add(data);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, snapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "MessageTest:onCancelled", error.toException());
            }
        });
    }

    @Test
    public void checkingExistKey() throws Exception {
        String roomKey = String.format(testUser1.uid, testUser2.uid);

        for(int i = 0; i < roomKeys.size(); i++) {
            if(roomKey.equals(roomKeys.get(i)))
                isExistKey = true;
        }

        if(isExistKey)
            Log.d("FIRST: TEST TO EQUAL KEY", roomKey);
        else
            roomKey = String.format(testUser2.uid, testUser1.uid);

        for(int i = 0; i < roomKeys.size(); i++) {
            if(roomKey.equals(roomKeys.get(i)))
                isExistKey = true;
        }

        if(isExistKey)
            Log.d("SECOND: TEST TO EQUAL KEY", roomKey);
    }

    @After
    public void checkedExistKey() {
        if(isExistKey)
            Log.d(TAG, "RESULT TEST: THERE IS THE KEY");
        else
            Log.d(TAG, "RESULT TEST: THERE IS NOTHING");
    }
}