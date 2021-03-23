package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";
    private TextView userNameTextView, nameTextView, emailTextView, logOut;
    private Button editProfile;

    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private String userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        bottomNavigation();

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
        final TextView greetingTextView = findViewById(R.id.tvGreeting);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                Log.d(TAG, "signInWithEmail:success");

                if (userProfile != null) {
                    userName = userProfile.userName;
                    String strName = userProfile.name;
                    String strEmail = userProfile.email;

                    greetingTextView.setText("Hello, " + strName + "!");
                    userNameTextView.setText(userName);
                    nameTextView.setText(strName);
                    emailTextView.setText(strEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), UpdateUserProfile.class);

                i.putExtra("userName", userNameTextView.getText().toString().trim());
                i.putExtra("name", nameTextView.getText().toString().trim());
                i.putExtra("email", emailTextView.getText().toString().trim());

                startActivity(i);
            }
        });

    }


    private void bottomNavigation() {

        setTitle("Account Profile");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profileNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeNav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profileNav:
                        return true;
                    case R.id.messageNav:
                        Intent intent = new Intent(getApplicationContext(), Message.class);
                        intent.putExtra("UserName", userName);
                        intent.putExtra("Uid", userID);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}