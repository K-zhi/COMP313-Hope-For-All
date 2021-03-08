package comp321.hope_for_all.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import comp321.hope_for_all.tasks.CustomAlertDialog;

public class LoginUser extends AppCompatActivity implements View.OnClickListener{

    private EditText email, password;
    private Button logIn;
    private TextView signUp;
    private LinearLayout layout;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        layout = findViewById(R.id.userLoginLayout);

        logIn = findViewById(R.id.btnLogIn);
        logIn.setOnClickListener(this);

        signUp = findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSignUp:
                startActivity(new Intent(getApplicationContext(),CreateUser.class));
                break;

            case R.id.btnLogIn:
                userLogIn();
                break;
        }

    }

    private void userLogIn() {
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        if(inputEmail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
            email.setError("Email is invalid");
            email.requestFocus();
            return;
        }

        if(inputPassword.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if(password.length() < 6 ){
            password.setError("Minimum password is 6 characters");
            password.requestFocus();
            return;
        }

    }
}