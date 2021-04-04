package comp321.hope_for_all.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PlaceAutoSuggestAdapter;
import comp321.hope_for_all.models.Counselor;

public class CreateCounselor extends AppCompatActivity implements View.OnClickListener{

    private EditText userName, name, bio, email, website, password, confirmPassword;

    private AutoCompleteTextView autoCompleteTextView;

    private Button createAccount;
    private TextView logIn;
    private LinearLayout layout;
    private ProgressDialog progressDialog;

    private Context mContext;
    private Activity mActivity;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_counselor);

        userName = findViewById(R.id.etUserName);
        name = findViewById(R.id.etName);
        bio = findViewById(R.id.etBio);
        website = findViewById(R.id.etWebsite);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);

        layout = findViewById(R.id.createCounselorLayout);

        logIn = findViewById(R.id.tvLogIn);
        logIn.setOnClickListener(this);

        createAccount = findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mContext = getApplicationContext();
        mActivity = CreateCounselor.this;

        progressDialog = new ProgressDialog(mActivity);

        autoCompleteTextView=findViewById(R.id.etLocation);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(CreateCounselor.this,android.R.layout.simple_list_item_1));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Address : ",autoCompleteTextView.getText().toString());
                LatLng latLng=getLatLngFromAddress(autoCompleteTextView.getText().toString());
                if(latLng!=null) {
                    Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
                    Address address=getAddressFromLatLng(latLng);
                    if(address!=null) {
                        Log.d("Address : ", "" + address.toString());
                        Log.d("Address Line : ",""+address.getAddressLine(0));
                        Log.d("Phone : ",""+address.getPhone());
                        Log.d("Pin Code : ",""+address.getPostalCode());
                        Log.d("Feature : ",""+address.getFeatureName());
                        Log.d("More : ",""+address.getLocality());
                    }
                    else {
                        Log.d("Adddress","Address Not Found");
                    }
                }
                else {
                    Log.d("Lat Lng","Lat Lng Not Found");
                }

            }
        });

    }


    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(CreateCounselor.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleaddress=addressList.get(0);
                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(CreateCounselor.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address=addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvLogIn:
                startActivity(new Intent(getApplicationContext(), LoginUser.class));
                break;

            case R.id.btnCreateAccount:
                registerCounselor();
                break;
        }
    }

    private void registerCounselor() {

        String inputUserName = userName.getText().toString().trim();
        String inputName = name.getText().toString().trim();
        String inputBio = bio.getText().toString().trim();
        String inputEmail = email.getText().toString().trim();
        String inputWebsite = website.getText().toString().trim();
        String inputLocation = autoCompleteTextView.getText().toString().trim();
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

        if (inputBio.isEmpty()) {
            name.setError("Biography is required");
            name.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }

        if (inputWebsite.isEmpty()) {
            name.setError("Website is required");
            name.requestFocus();
            return;
        }

        if (inputLocation.isEmpty()) {
            name.setError("Location is required");
            name.requestFocus();
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
                .addOnCompleteListener(CreateCounselor.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Counselor counselor = new Counselor(inputUserName, inputName, inputBio, inputEmail, inputWebsite, inputLocation);

                            mDatabase.getInstance().getReference("Counselors")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(counselor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                                        Toast.makeText(CreateCounselor.this, "Account created", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(getApplicationContext(), LoginUser.class));

                                    }else {
                                        Toast.makeText(CreateCounselor.this, "User failed to create!", Toast.LENGTH_LONG).show();
                                    }

                                }

                            });

                        }else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                            Toast.makeText(CreateCounselor.this, "Failed to register", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}