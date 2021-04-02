package comp321.hope_for_all.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.ChatData;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataSet;
    private String myNickName;
    private final Activity context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNickName;
        public TextView txtMsg;
        public TextView txtTime;
        public ImageView image;
        public View rootView;

        public MyViewHolder(View view) {
            super(view);
            txtNickName = view.findViewById(R.id.txtNickName);
            txtMsg = view.findViewById(R.id.txtMessage);
            //txtTime = view.findViewById(R.id.txtTime);
            rootView = view;

            view.setClickable(true);
            view.setEnabled(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataSet
    public ChatAdapter(Activity context, List<ChatData> myDataSet, String myNickName) {
        if(mDataSet == null)
            myDataSet = new ArrayList<>();

        mDataSet = myDataSet;
        this.myNickName = myNickName;
        this.context = context;
    }

    public List<ChatData> getChatList() { return this.mDataSet; }

    // Create New view (Invoked by the layout manager)
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return  viewHolder;
    }

    // Replace the contents of a view invoked by the layout manager
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Get element from your dataSet at this position
        // Replace the contents of the view with that element
        ChatData chat = mDataSet.get(position);

        if(chat != null) {

            holder.txtNickName.setText(chat.getUserName());
            holder.txtMsg.setText(chat.getMsg());
            //holder.txtTime.setText("HH:mm");

            if(chat.getUserName().equals(this.myNickName)) {
                holder.txtMsg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                holder.txtNickName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            else {
                holder.txtMsg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                holder.txtNickName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public ChatData getChat(int position) {
        return mDataSet != null ? mDataSet.get(position) : null;
    }

    // Renewal chat data
    public void addChat(ChatData chat) {
        mDataSet.add(chat);
        notifyItemInserted(mDataSet.size() - 1);
    }
}
