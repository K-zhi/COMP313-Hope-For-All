package comp321.hope_for_all.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.ChatData;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickNmae;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNickName;
        public TextView txtMsg;
        public View rootView;

        public MyViewHolder(View view) {
            super(view);
            txtNickName = view.findViewById(R.id.txtNickName);
            txtMsg = view.findViewById(R.id.txtMessage);
            rootView = view;

            view.setClickable(true);
            view.setEnabled(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset
    public ChatAdapter(List<ChatData> myDataset, Context context, String myNickName) {
        mDataset = myDataset;
        this.myNickNmae = myNickName;
    }

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
        // Get element from your dataset at this position
        // Replace the contents of the view with that element
        if(mDataset != null && mDataset.size() > 0) {
            ChatData chat = mDataset.get(position);

            if(chat != null) {
                holder.txtNickName.setText(chat.getUserName());
                holder.txtMsg.setText(chat.getMsg());

                if(chat.getUserName().equals(this.myNickNmae)) {
                    holder.txtMsg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    holder.txtNickName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                }
                else {
                    holder.txtMsg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    holder.txtNickName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    // Renewal chat data
    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() - 1);
    }
}
