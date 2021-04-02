package comp321.hope_for_all.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    private TextView logOut;
    private Button editProfile, deleteProfile;

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
        logOut.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        final TextView userNameTextView = findViewById(R.id.tvUserName);
        final TextView nameTextView = findViewById(R.id.tvName);
        final TextView emailTextView = findViewById(R.id.tvEmail);
        final TextView greetingTextView = findViewById(R.id.tvGreeting);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

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

        deleteProfile = findViewById(R.id.btnDeleteProfile);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setIcon(R.drawable.ic_hope);
                builder.setTitle(R.string.dialog_title).setMessage(R.string.dialog_message);
                builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.delete();
                        startActivity(new Intent(UserProfile.this, LoginUser.class));

                    }
                });

                builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancel to delete your information", Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });

                builder.show();;
            }
        });
    }

    private void deleteProfile(User user){

        databaseReference.child(user.getUserName()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(), "Removed: " + user.getUserName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.ic_hope);
        alert.setTitle("Sign out");
        alert.setMessage("Are you sure you want to sign out?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, LoginUser.class));
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserProfile.this, UserProfile.class));
            }
        });
        alert.show();
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
                        finish();
                        return true;

                    case R.id.profileNav:
                        return true;

                    case R.id.messageNav:
                        Intent intent = new Intent(getApplicationContext(), Message.class);
                        intent.putExtra("UserName", userName);
                        intent.putExtra("Uid", userID);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

}