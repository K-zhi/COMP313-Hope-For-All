package comp321.hope_for_all.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp321.hope_for_all.R;
import comp321.hope_for_all.adapter.PostAdapter;
import comp321.hope_for_all.models.Post;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addPost;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Posts");

    private List<Post> list = new ArrayList<>();
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addPost.getVisibility() == View.VISIBLE) {
                    addPost.hide();
                } else if (dy < 0 && addPost.getVisibility() != View.VISIBLE) {
                    addPost.show();
                }
            }
        });


        addPost = findViewById(R.id.button_add_post);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddPost();
            }
        });

        readData();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts");

    }


    private void showDialogAddPost() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_post);

        dialog.setCancelable(true);
        WindowManager.LayoutParams lp =new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow().getAttributes()));
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton close = dialog.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final EditText text = dialog.findViewById(R.id.et_post);

        Button post = dialog.findViewById(R.id.btn_post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(text.getText())){
                    text.setError("Cannot be empty");
                }else {
                    addDataToFirebase(text.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void addDataToFirebase(String toString, String parentId) {
        String id = myRef.push().getKey();
        Post my_post = new Post(id, toString, parentId);

        myRef.child(id).setValue(my_post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Posted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to post.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDataToFirebase(String content) {
        addDataToFirebase(content, "");
    }

    private void readData() {
        list.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post values = dataSnapshot.getValue(Post.class);
                    list.add(values);
                }
                adapter = new PostAdapter(MainActivity.this, list);
                recyclerView.setAdapter(adapter);
                setClick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read from database", error.toException());

            }
        });

    }

    private void setClick() {
        adapter.setOnCallBack(new PostAdapter.OnCallBack() {
            @Override
            public void onButtonCommentClick(Post post) {
                commentPost(post);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                addPost.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onButtonDeleteClick(Post post) {
                deletePost(post);
            }

            @Override
            public void onButtonEditClick(Post post) {
                showDialogUpdatePost(post);
            }

            @Override
            public void onButtonConfirmComment(Post post) {
                addDataToFirebase(post.getContent(), post.getParentId());
                bottomNavigationView.setVisibility(View.VISIBLE);
                addPost.setVisibility(View.VISIBLE);
            }

            @Override
            public void onButtonCancelComment() {
                bottomNavigationView.setVisibility(View.VISIBLE);
                addPost.setVisibility(View.VISIBLE);
            }
        });
    }

    private void deletePost(Post post){
        myRef.child(post.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(), "Removed: " + post.getContent(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialogUpdatePost(Post posts){

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_post);

        dialog.setCancelable(true);
        WindowManager.LayoutParams lp =new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow().getAttributes()));
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton close = dialog.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final EditText text = dialog.findViewById(R.id.et_post);
        text.setText(posts.getContent());
        Button btn_post = dialog.findViewById(R.id.btn_post);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(text.getText())){
                    text.setError("Cannot be empty");
                }else {
                    updatePost(posts, text.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    private void updatePost(Post posts, String newPost) {
        myRef.child(posts.getId()).child("content").setValue(newPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.toast_post_updated, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void commentPost(Post post) {
        adapter.addItem(post.getId());
    }

    private void bottomNav() {

        setTitle("User Home Page");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homeNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profileNav:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.homeNav:
                        return true;

                    case R.id.messageNav:
                        startActivity(new Intent(getApplicationContext(), Message.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}