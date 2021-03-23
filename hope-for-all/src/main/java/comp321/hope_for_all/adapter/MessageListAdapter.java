package comp321.hope_for_all.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.activities.Chat;
import comp321.hope_for_all.activities.Message;
import comp321.hope_for_all.models.ChatData;

import static androidx.core.content.ContextCompat.startActivity;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {
//    private ArrayList<ChatData> mItems = new ArrayList<>();

    private List<ChatData> mDataSet = new ArrayList<>();
    private String uid;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRoomName;
        public TextView txtMsg;
        public View rootView;

        public MyViewHolder(View view) {
            super(view);

            //txtRoomName = view.findViewById();
            rootView = view;
            view.setClickable(true);
            view.setEnabled(true);
        }
    }

    public MessageListAdapter() {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("ChatRooms").orderByChild("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren() != null) {
                    for(DataSnapshot item :snapshot.getChildren()) {
                        mDataSet.add(item.getValue(ChatData.class));
                    }

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MessageListAdapter(List<ChatData> dataset, Context context) {
        mDataSet = dataset;
    }

    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Chat.class);
                intent.putExtra("UserName", mDataSet.get(position).getUserName());
                intent.putExtra("Uid", mDataSet.get(position).getUid());
                intent.putExtra("OpponentId", mDataSet.get(position).getOpponentId());

                ActivityOptions activityOptions = null;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.from_right, R.anim.from_left);
                    startActivity(v.getContext(), intent, activityOptions.toBundle());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

//    public MessageListAdapter(ArrayList<ChatData> list) {
//        this.mItems = list;
//    }
//
//    @Override
//    public int getCount() {
//        return mItems.size();
//    }
//
//    @Override
//    public ChatData getItem(int position) {
//        return mItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Context context = parent.getContext();
//
//        if(convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.custom_chat_list, parent, false);
//        }
//
//        ImageView image = (ImageView) convertView.findViewById(R.id.ivChatImage);
//        TextView txtName = (TextView) convertView.findViewById(R.id.txtMessageName);
//        TextView txtContent = (TextView) convertView.findViewById(R.id.txtMessageContent);
//
//        ChatData item = getItem(position);
//
//        //image.setImageDrawable(R.drawable.ic_brain);
//        txtName.setText(item.getNickName());
//        txtContent.setText(item.getMsg());
//
//        return convertView;
//    }
//
//    public void addItem(Drawable img, String name, String content) {
//        ChatData mItem = new ChatData();
//
//        mItem.setImage(R.drawable.ic_brain);
//        mItem.setOpponentName(name);
//        mItem.setMsg(content);
//
//        mItems.add(mItem);
//    }
}
