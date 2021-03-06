package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();

        etUserName.setText(editUserName);
        etName.setText(editName);
        etEmail.setText(editEmail);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String srtUserName = etUserName.getText().toString().trim();
                String strName = etName.getText().toString().trim();
                String strEmail = etEmail.getText().toString().trim();

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");

                String key = ref.getKey();

                userID = user.getUid();
                
                ref.orderByChild(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                String key = datas.getKey();

                                String name = datas.child("name").getValue().toString();
                                String userName = datas.child("userName").getValue().toString();
                                String email = datas.child("email").getValue().toString();

                                ref.child(userID).child("userName").setValue(srtUserName);
                                ref.child(userID).child("name").setValue(strName);
                                ref.child(userID).child("email").setValue(strEmail);

                                startActivity(new Intent(getApplicationContext(), UserProfile.class));

                                Toast.makeText(UpdateUserProfile.this, "Your profile is updated", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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