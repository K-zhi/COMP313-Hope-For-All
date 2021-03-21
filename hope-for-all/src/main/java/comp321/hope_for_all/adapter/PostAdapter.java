package comp321.hope_for_all.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.activities.MainGuest;
import comp321.hope_for_all.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> list;

    private OnCallBack onCallBack;

    public PostAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnCallBack(OnCallBack onCallBack){
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = list.get(position);
        holder.tvPostContent.setText(post.getContent());

        if(context.getClass() == MainGuest.class) {
//            holder.comment.setVisibility(View.INVISIBLE);
        }

        String id = post.getId();
        if(id == null || id == "") {
            Log.d("tag", "checked");
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
            params.setMarginStart(200);
            holder.itemView.findViewById(R.id.id_postview).setLayoutParams(params);
            holder.editTextComment.setVisibility(View.VISIBLE);
            holder.tvPostContent.setVisibility(View.INVISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
        }
        else {
            holder.editTextComment.setVisibility(View.INVISIBLE);
        }

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonCommentClick(post);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonDeleteClick(post);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonEditClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPostContent;
        private EditText editTextComment;
        private ImageButton comment, delete, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPostContent = itemView.findViewById(R.id.tv_postContent);
            editTextComment = itemView.findViewById(R.id.edittext_comment);
            comment = itemView.findViewById(R.id.comment_post);
            delete = itemView.findViewById(R.id.delete_post);
            edit = itemView.findViewById(R.id.edit_post);

        }
    }

    public interface OnCallBack{
        void onButtonCommentClick(Post post);
        void onButtonDeleteClick(Post posts);
        void onButtonEditClick(Post posts);
    }

    public void addItem(String parentId) {
        for(int i=0; i<list.size(); ++i) {
            Post p = list.get(i);
            if(p.getId() == parentId) {
                list.add(i + 1, new Post());
                break;
            }
        }

        notifyDataSetChanged();
    }
}
