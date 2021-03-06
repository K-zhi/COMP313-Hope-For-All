package comp321.hope_for_all.adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        this.list = sortList(list);

        notifyDataSetChanged();
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

        String id = post.getId();
        if(id == null || id.isEmpty()) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
            params.setMarginStart(200);
            holder.itemView.findViewById(R.id.id_postview).setLayoutParams(params);
            holder.editTextComment.setVisibility(View.VISIBLE);
            holder.tvPostContent.setVisibility(View.INVISIBLE);
            holder.comment.setVisibility(View.INVISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.confirmcomment.setVisibility(View.VISIBLE);
            holder.cancelcomment.setVisibility(View.VISIBLE);
            holder.editTextComment.post(new Runnable() {
                @Override
                public void run() {
                    holder.editTextComment.requestFocus();
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            });
        }
        else {
            holder.editTextComment.setVisibility(View.INVISIBLE);
            holder.tvPostContent.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.confirmcomment.setVisibility(View.INVISIBLE);
            holder.cancelcomment.setVisibility(View.INVISIBLE);

            if(!post.getParentId().isEmpty()) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
                params.setMarginStart(200);
                holder.itemView.findViewById(R.id.id_postview).setLayoutParams(params);
                holder.comment.setVisibility(View.INVISIBLE);
            }
            else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
                params.setMarginStart(0);
                holder.itemView.findViewById(R.id.id_postview).setLayoutParams(params);
                holder.comment.setVisibility(View.VISIBLE);
            }

            if(context.getClass() == MainGuest.class) {
                holder.comment.setVisibility(View.GONE);
            }
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

        holder.confirmcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setContent(holder.editTextComment.getText().toString());
                onCallBack.onButtonConfirmComment(post);
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.editTextComment.getWindowToken(), 0);
            }
        });

        holder.cancelcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.editTextComment.getWindowToken(), 0);
                onCallBack.onButtonCancelComment();
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
        private ImageButton comment, delete, edit, confirmcomment, cancelcomment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPostContent = itemView.findViewById(R.id.tv_postContent);
            editTextComment = itemView.findViewById(R.id.edittext_comment);
            comment = itemView.findViewById(R.id.comment_post);
            delete = itemView.findViewById(R.id.delete_post);
            edit = itemView.findViewById(R.id.edit_post);
            confirmcomment = itemView.findViewById(R.id.confirm_comment);
            cancelcomment = itemView.findViewById(R.id.cancel_comment);

            editTextComment.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d("tag", "enter pressed");
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public interface OnCallBack{
        void onButtonCommentClick(Post post);
        void onButtonDeleteClick(Post posts);
        void onButtonEditClick(Post posts);
        void onButtonConfirmComment(Post post);
        void onButtonCancelComment();
    }

    public void addItem(String parentId) {
        for(int i=0; i<list.size(); ++i) {
            Post p = list.get(i);
            if(p.getId() == parentId) {
                list.add(i + 1, new Post("", "", p.getId()));
                break;
            }
        }

        notifyDataSetChanged();
    }

    private List<Post> sortList(List<Post> list) {
        List<Post> posts = new ArrayList<>();
        List<Post> comments = new ArrayList<>();
        List<Post> sorted = new ArrayList<>();
        for(int i=0; i<list.size(); ++i) {
            Post post = list.get(i);
            if(post.getParentId().equals("")) {
                posts.add(post);
                sorted.add(post);
            }
            else {
                comments.add(post);
            }
        }

        for(int i=0; i<posts.size(); ++i) {
            Post post = posts.get(i);
            int currentCommentCount = 0;
            for (int j=0; j<comments.size(); ++j) {
                Post comment = comments.get(j);
                int postIndex = sorted.indexOf(post);
                if(comment.getParentId().equals(post.getId())) {
                    ++currentCommentCount;
                    sorted.add(postIndex + currentCommentCount, comment);
                }
            }
        }

        return sorted;
    }
}
