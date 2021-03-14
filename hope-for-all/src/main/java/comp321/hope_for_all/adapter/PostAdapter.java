package comp321.hope_for_all.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp321.hope_for_all.R;
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
        holder.tvPostContent.setText(list.get(position).getContent());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonDeleteClick(list.get(position));
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonEditClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPostContent;
        private ImageButton delete, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPostContent = itemView.findViewById(R.id.tv_postContent);
            delete = itemView.findViewById(R.id.delete_post);
            edit = itemView.findViewById(R.id.edit_post);

        }
    }

    public interface OnCallBack{
        void onButtonDeleteClick(Post posts);
        void onButtonEditClick(Post posts);
    }
}
