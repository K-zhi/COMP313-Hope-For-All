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
    private List<ChatData> mDataSet;
    private String uid;
    private String chatKey;
    private Boolean isExist = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRoomName;
        public TextView txtMsg;
        public View rootView;

        public MyViewHolder(View view) {
            super(view);

            rootView = view;
            txtRoomName = view.findViewById(R.id.txtChatRoomName);
            txtMsg = view.findViewById(R.id.txtChatRoomContent);

            view.setClickable(true);
            view.setEnabled(true);
        }
    }

    public MessageListAdapter(String id)
    {
        this.uid = id;
    }

    public MessageListAdapter(Context context, List<ChatData> dataSet) {
        if(mDataSet == null)
            mDataSet = new ArrayList<>();

        this.mDataSet = dataSet;
    }

    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_room_item, parent, false);

        MyViewHolder v = new MyViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder != null) {
            if(mDataSet != null && mDataSet.size() > 0) {
                // get element from dataSet at this position
                // replace the contents of the view with that element
                ChatData room = mDataSet.get(position);

                if(room != null) {
                    String key = room.getOpponentId();
                    if(key.equals(uid))
                        holder.txtRoomName.setText(room.getUserName());
                    else
                        holder.txtRoomName.setText(room.getOpponentName());
                    holder.txtMsg.setText(room.getMsg());
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Chat.class);
                    String key = mDataSet.get(position).getOpponentId();
                    // Check Chat Data key in Database
                    chatKey = mDataSet.get(position).getOpponentId() + mDataSet.get(position).getUid();
                    // Check Chat Data key in Database
                    FirebaseDatabase.getInstance().getReference("ChatRooms").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                String test = chatSnapshot.getKey().toString();

                                if(test.equals(chatKey)) {
                                    isExist = true;
                                    break;
                                } else
                                    isExist = false;
                            }
                            chatKey = isExist == true ? chatKey : mDataSet.get(position).getUid() + mDataSet.get(position).getOpponentId();

                            if(key.equals(uid)) {
                                intent.putExtra("UserName", mDataSet.get(position).getOpponentName());
                                intent.putExtra("Uid", mDataSet.get(position).getOpponentId());
                                intent.putExtra("OpponentId", mDataSet.get(position).getUid());
                                intent.putExtra("OpponentName", mDataSet.get(position).getUserName());
                            }
                            else {
                                intent.putExtra("UserName", mDataSet.get(position).getUserName());
                                intent.putExtra("Uid", mDataSet.get(position).getUid());
                                intent.putExtra("OpponentId", mDataSet.get(position).getOpponentId());
                                intent.putExtra("OpponentName", mDataSet.get(position).getOpponentName());
                            }
                            intent.putExtra("RoomKey", chatKey);

                            ActivityOptions activityOptions = null;
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.from_right, R.anim.from_left);
                                startActivity(v.getContext(), intent, activityOptions.toBundle());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    // Renewal chat data
    public void addRoom(ChatData room) {
        if(mDataSet == null)
            mDataSet = new ArrayList<>();
        mDataSet.add(room);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public Boolean checkIsExistKey(String key) {
        // Check Chat Data key in Database
        FirebaseDatabase.getInstance().getReference("ChatRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String test = chatSnapshot.getKey().toString();

                    if(test.equals(chatKey)) {
                        isExist = true;
                        break;
                    } else
                        isExist = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return isExist;
    }
}
