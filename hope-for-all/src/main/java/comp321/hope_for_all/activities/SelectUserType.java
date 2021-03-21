package comp321.hope_for_all.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import comp321.hope_for_all.R;

public class SelectUserType extends AppCompatActivity implements View.OnClickListener {

    private Button s_counselor, s_user;
    private TextView s_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        s_counselor = findViewById(R.id.btnCounselor);
        s_user = findViewById(R.id.btnUser);
        s_guest = findViewById(R.id.tvGuestLogIn);

        s_counselor.setOnClickListener(this);
        s_user.setOnClickListener(this);
        s_guest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnCounselor:
                startActivity(new Intent(getApplicationContext(), CreateCounselor.class));
                break;

            case R.id.btnUser:
                startActivity(new Intent(getApplicationContext(), CreateUser.class));
                break;

            case R.id.tvGuestLogIn:
                startActivity(new Intent(getApplicationContext(), MainGuest.class));
                break;
        }

    }
}