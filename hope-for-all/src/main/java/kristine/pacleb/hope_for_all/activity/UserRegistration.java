package kristine.pacleb.hope_for_all.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import kristine.pacleb.hope_for_all.R;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener{

    private EditText userName, name, email, password, confirmPassword;
    private Button createAccount;
    private TextView logIn;
    private LinearLayout layout;

    String inputUserName, inputName, inputEmail, inputPassword, inputConfirmPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        userName = findViewById(R.id.etUserName);
        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        layout = findViewById(R.id.userRegistrationLayout);

        logIn = findViewById(R.id.tvLogIn);
        logIn.setOnClickListener(this);

        createAccount = findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreateAccount:
                createAccount();
                break;

            case R.id.tvLogIn:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
        }
    }

    private void createAccount() {
        inputUserName = userName.getText().toString().trim();
        inputName = name.getText().toString().trim();
        inputEmail = email.getText().toString().trim();
        inputPassword = password.getText().toString().trim();
        inputConfirmPw = confirmPassword.getText().toString().trim();

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


        if (!inputConfirmPw.equals(inputPassword)) {
            confirmPassword.setError("Password does not match");
            confirmPassword.requestFocus();
            return;
        }

    }

}
