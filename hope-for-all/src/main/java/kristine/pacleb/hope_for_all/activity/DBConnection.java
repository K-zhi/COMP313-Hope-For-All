package kristine.pacleb.hope_for_all.activity;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DBConnection {
    // DatabaseReference
    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    User userPost;

    public void ConnectionFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void AdminConnectionFireBase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void CheckUserSignedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void AddNewUser(int userId, String userName, String userEmail, String userPassword, String userType) {
        HashMap result = new HashMap<>();
        result.put("userId", userId);
        result.put("userName", userName);
        result.put("userPassword", userEmail);
        result.put("userEmail", userPassword);
        result.put("userType", userType);

        createNewtUser(userId, userName, userEmail, userPassword, userType);
    }

    public void createNewtUser(int userId, String userName, String userEmail, String userPassword, String userType) {
        User user = new User(userId, userName, userPassword, userEmail, userType);
        String strUserId = String.valueOf(userId);

        mDatabase.child("Users").child(userType).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Log.w("Write was successful", "getData" + userPost.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write was Failed!
                        Log.w("Write was Failed!", "getData" + userPost.toString());
                    }
                });
    }

    public void readUser() {
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get Post Object and use the values to update the UI
                if(snapshot.getValue(User.class) != null) {
                    userPost = snapshot.getValue(User.class);

                    if(userPost != null) {
                        Log.w("FireBaseData", "getData" + userPost.toString());
                    }
                } else {
                    Log.w("No data in FireBaseData", "getData" + userPost.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, Log a message
                Log.w("FireBaseData", "LoadPost:onCancelled", error.toException());
            }
        });
    }

    // ## I"ll delete this one later
    private void MakeTestDat() {
        int getUserId = 1;
        String getUserName = "Counselor01";
        String getUserPw = "1234";
        String getUserEmail = "test@test.ca";
        String getUserType = "User";
        AddNewUser(getUserId, getUserName, getUserPw, getUserEmail, getUserType);

        getUserId = 2;
        getUserName = "Counselor01";
        getUserPw = "1234";
        getUserEmail = "test@test.ca";
        getUserType = "Counselor";
        AddNewUser(getUserId, getUserName, getUserPw, getUserEmail, getUserType);
    }
}
