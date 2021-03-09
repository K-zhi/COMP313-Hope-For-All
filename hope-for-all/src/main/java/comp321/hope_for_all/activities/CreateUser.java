package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class CreateUser extends AppCompatActivity implements View.OnClickListener{

    private EditText userName, name, email, password, confirmPassword;
    private TextView logIn;
    private Button createAccount;
    private LinearLayout layout;
    private ProgressDialog progressDialog;

    private Context mContext;
    private Activity mActivity;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userName = findViewById(R.id.etUserName);
        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);

        layout = findViewById(R.id.createUserLayout);

        logIn = findViewById(R.id.tvLogIn);
        logIn.setOnClickListener(this);

        createAccount = findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mContext = getApplicationContext();
        mActivity = CreateUser.this;

        progressDialog = new ProgressDialog(mActivity);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLogIn:
                startActivity(new Intent(getApplicationContext(),LoginUser.class));
                break;

            case R.id.btnCreateAccount:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String inputUserName = userName.getText().toString().trim();
        String inputName = name.getText().toString().trim();
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();
        String inputConfPassword = confirmPassword.getText().toString().trim();

        if (inputUserName.isEmpty()) {
            userName.setError("username is required");
            userName.requestFocus();
            return;
        }

        if (inputName.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }

        if (inputPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            password.setError("Minimum password is 6 characters");
            password.requestFocus();
            return;
        }

        if (!inputConfPassword.equals(inputPassword)) {
            confirmPassword.setError("Password does not match");
            confirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(CreateUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            User user = new User(inputUserName, inputName, inputEmail);

                            mDatabase.getInstance().getReference("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                                        Toast.makeText(CreateUser.this, "Account created", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(getApplicationContext(), LoginUser.class));

                                    }else {
                                        Toast.makeText(CreateUser.this, "User failed to create!", Toast.LENGTH_LONG).show();
                                    }

                                }

                            });

                        }else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                            Toast.makeText(CreateUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}