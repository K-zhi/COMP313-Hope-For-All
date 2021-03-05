package kristine.pacleb.hope_for_all.activity;

import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kristine.pacleb.hope_for_all.R;

public class UserLogin extends AppCompatActivity {
    // DatabaseReference
    private DatabaseReference mDatabase;
    DBConnection dbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbConnection = new DBConnection();
        dbConnection.AdminConnectionFireBase();
    }

    public void CreateUSerWithEmailAndPassword(String userEmail, String userPassword) {
        dbConnection.mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "signInWithEmail:success");
                            FirebaseUser user = dbConnection.mAuth.getCurrentUser();
                            // Will add update Method
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            // Will add update Method and parameter's value is null
                        }
                    }
                });
    }
}