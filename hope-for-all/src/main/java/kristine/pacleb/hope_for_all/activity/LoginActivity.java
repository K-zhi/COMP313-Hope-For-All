package kristine.pacleb.hope_for_all.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kristine.pacleb.hope_for_all.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button logIn;
    private LinearLayout layout;
    private TextView signUp, guest;

    String inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        logIn = findViewById(R.id.btnLogIn);
        layout = findViewById(R.id.userLoginLayout);

        guest = findViewById(R.id.tvGuestLogIn);

        signUp = findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
            }
        });


    }



}