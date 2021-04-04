package comp321.hope_for_all.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PostAdapter;
import comp321.hope_for_all.models.Post;

public class MainGuest extends AppCompatActivity {

    private FloatingActionButton addPost;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Posts");

    private List<Post> list = new ArrayList<>();
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guest);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        readData();
    }


    private void readData() {
        list.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post values = dataSnapshot.getValue(Post.class);
                    list.add(values);
                }
                adapter = new PostAdapter(MainGuest.this, list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read from database", error.toException());

            }
        });

    }

    private void deletePost(Post post){

        startActivity(new Intent(MainGuest.this, LoginUser.class));

    }

    private void showDialogUpdatePost(Post posts){

        startActivity(new Intent(MainGuest.this, LoginUser.class));

    }

    private void updatePost(Post posts, String newPost) {
        startActivity(new Intent(MainGuest.this, LoginUser.class));
    }
}