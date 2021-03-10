package comp321.hope_for_all.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.Post;

public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PostHolder> {


    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull Post model) {

        //TODO:

    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    class PostHolder extends RecyclerView.ViewHolder {

        TextView textView_userName, textView_postContent, textView_dateTime;

        public PostHolder(@NonNull View itemView) {

            super(itemView);

            textView_userName = itemView.findViewById(R.id.tv_userName);
            textView_postContent = itemView.findViewById(R.id.tv_postContent);
            textView_dateTime = itemView.findViewById(R.id.tv_dateTime);

        }
    }
}
