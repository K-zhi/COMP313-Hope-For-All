package comp321.hope_for_all.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PlaceAutoSuggestAdapter;

public class UpdateCounselorProfile extends AppCompatActivity {

    private EditText name, bio, email, website;
    private AutoCompleteTextView autoCompleteTextView;
    private Button save, cancel;

    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_counselor_profile);

        Intent data = getIntent();
        String editName = data.getStringExtra("name");
        String editBio = data.getStringExtra("bio");
        String editEmail = data.getStringExtra("email");
        String editWebsite = data.getStringExtra("website");
        String editLocation = data.getStringExtra("location");

        name = findViewById(R.id.etName);
        bio = findViewById(R.id.etBio);
        email = findViewById(R.id.etEmail);
        website = findViewById(R.id.etWebsite);

        autoCompleteTextView = findViewById(R.id.etLocation);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(UpdateCounselorProfile.this,android.R.layout.simple_list_item_1));

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

        save = findViewById(R.id.btnSave);
        cancel = findViewById(R.id.btnCancel);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();

        name.setText(editName);
        bio.setText(editBio);
        email.setText(editEmail);
        website.setText(editWebsite);
        autoCompleteTextView.setText(editLocation);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateCounselorProfile.this, CounselorProfile.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strName = name.getText().toString().trim();
                String strBio = bio.getText().toString().trim();
                String strEmail = email.getText().toString().trim();
                String strWebsite = website.getText().toString().trim();
                String strLocation = autoCompleteTextView.getText().toString().trim();

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Counselors");

                String key = ref.getKey();

                userID = user.getUid();

                ref.orderByChild(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {

                                String key = datas.getKey();

                                String name = datas.child("c_name").getValue().toString();
                                String bio = datas.child("c_bio").getValue().toString();
                                String email = datas.child("c_email").getValue().toString();
                                String website = datas.child("c_website").getValue().toString();
                                String location = datas.child("c_location").getValue().toString();

                                ref.child(userID).child("c_name").setValue(strName);
                                ref.child(userID).child("c_bio").setValue(strBio);
                                ref.child(userID).child("c_email").setValue(strEmail);
                                ref.child(userID).child("c_website").setValue(strWebsite);
                                ref.child(userID).child("c_location").setValue(strLocation);

                                startActivity(new Intent(getApplicationContext(), CounselorProfile.class));

                                Toast.makeText(UpdateCounselorProfile.this, "Your profile is updated", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

            }
        });
    }

    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(UpdateCounselorProfile.this);
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
        Geocoder geocoder=new Geocoder(UpdateCounselorProfile.this);
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
}