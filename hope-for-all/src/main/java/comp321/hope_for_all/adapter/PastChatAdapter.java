package comp321.hope_for_all.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import comp321.hope_for_all.R;
import comp321.hope_for_all.activities.ChatActivity;
import comp321.hope_for_all.activities.UserListActivity;

public class PastChatAdapter extends RecyclerView.Adapter<PastChatAdapter.ViewHolder> {

    private Context context;
    private boolean isFromNewChat;

    public PastChatAdapter(Context context, boolean isFromNewChat) {
        this.context = context;
        this.isFromNewChat = isFromNewChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_list_single, parent, false);
        return new PastChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bindData();

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("UserID","123456");
            intent.putExtra("UserName","Maheshvari Rana");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView iv_user;
        private AppCompatTextView tv_user_name,tv_last_chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_user = itemView.findViewById(R.id.iv_user);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_last_chat = itemView.findViewById(R.id.tv_last_chat);
        }

        public void bindData(){
            tv_last_chat.setVisibility(isFromNewChat?View.GONE:View.VISIBLE);
        }
    }
}
