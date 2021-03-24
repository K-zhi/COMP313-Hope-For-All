package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.Counselor;
import comp321.hope_for_all.models.User;

public class CounselorProfile extends AppCompatActivity {

    private static final String TAG = "CounselorProfile";
    private TextView logOut;
    private Button editProfile;

    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private String userID;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_profile);

        bottomNavigation();

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: EDIT USER PROFILE ACTIVITY
            }
        });

        logOut = findViewById(R.id.tvSignOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CounselorProfile.this, LoginUser.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Counselors");
        userID = user.getUid();

        final TextView userNameTextView = findViewById(R.id.tvUserName);
        final TextView nameTextView = findViewById(R.id.tvName);
        final TextView bioTextView = findViewById(R.id.tvBio);
        final TextView emailTextView = findViewById(R.id.tvEmail);
        final TextView websiteTextView = findViewById(R.id.tvWebsite);
        final TextView locationTextView = findViewById(R.id.tvLocation);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Counselor counselorProfile = snapshot.getValue(Counselor.class);

                Log.d(TAG, "signInWithEmail:success");

                if (counselorProfile != null) {
                    String strUserName = counselorProfile.c_userName;
                    String strName = counselorProfile.c_name;
                    String strBio = counselorProfile.c_bio;
                    String strEmail = counselorProfile.c_email;
                    String strWebsite = counselorProfile.c_website;
                    String strLocation = counselorProfile.c_location;

                    userNameTextView.setText(strUserName);
                    nameTextView.setText(strName);
                    bioTextView.setText(strBio);
                    emailTextView.setText(strEmail);
                    websiteTextView.setText(strWebsite);
                    locationTextView.setText(strLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CounselorProfile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void bottomNavigation() {

        setTitle("Account Profile");
        
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profileNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeNav:
                        startActivity(new Intent(getApplicationContext(), MainCounselor.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profileNav:
                        return true;

                    case R.id.messageNav:
                        Intent intent = new Intent(getApplicationContext(), Message.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}