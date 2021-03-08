package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";
    private TextView logOut;

    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logOut = findViewById(R.id.tvSignOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, LoginUser.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView userNameTextView = findViewById(R.id.tvUserName);
        final TextView nameTextView = findViewById(R.id.tvName);
        final TextView emailTextView = findViewById(R.id.tvEmail);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                Log.d(TAG, "signInWithCustomToken:success");

                if (userProfile != null) {
                    String strUserName = userProfile.userName;
                    String strName = userProfile.name;
                    String strEmail = userProfile.email;

                    userNameTextView.setText(strUserName);
                    nameTextView.setText(strName);
                    emailTextView.setText(strEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


    }
}