package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UpdateUserProfile extends AppCompatActivity {

    private EditText etUserName, etName, etEmail;
    private Button save, cancel;

    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        Intent data = getIntent();
        String editUserName = data.getStringExtra("userName");
        String editName = data.getStringExtra("name");
        String editEmail = data.getStringExtra("email");

        etUserName = findViewById(R.id.update_userName);
        etName = findViewById(R.id.update_name);
        etEmail = findViewById(R.id.update_email);

        save = findViewById(R.id.btnSave);
        cancel = findViewById(R.id.btnCancel);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Users");
        user = firebaseAuth.getCurrentUser();

        etUserName.setText(editUserName);
        etName.setText(editName);
        etEmail.setText(editEmail);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String srtUserName = etUserName.getText().toString().trim();
                String strName = etName.getText().toString().trim();
                String strEmail = etEmail.getText().toString().trim();

                user = firebaseAuth.getCurrentUser();
                userID = user.getUid();

                String key = database.child("Users").child(userID).getKey();

                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userID).build();

                user.updateProfile(userProfileChangeRequest)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                User users = new User(editUserName, editName, editEmail);

                                Map<String, Object> postValues = users.toMap();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(key, postValues);
                                database.updateChildren(childUpdates);

                                Toast.makeText(UpdateUserProfile.this,"Account is updated!", Toast.LENGTH_LONG).show();


                                //startActivity(new Intent(getApplicationContext(), UpdateUserProfile.class));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateUserProfile.this, "Email already exist!", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateUserProfile.this, UserProfile.class));
            }
        });

    }


}