package comp321.hope_for_all.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import comp321.hope_for_all.R;
import comp321.hope_for_all.activities.ChatActivity;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_SEND=1;
    private final static int TYPE_RECEIVE=2;
    private Context context;

    public ChatMessageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_send, parent, false);
            return new SendViewHolder(view);
        }else if(viewType == TYPE_RECEIVE){
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_receive, parent, false);
            return new ReceiveViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if(position %2 == 0){
           return TYPE_SEND;
        }else{
            return TYPE_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_text_send;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text_send = itemView.findViewById(R.id.tv_text_send);
        }

        public void bindData(){
            tv_text_send.setText("Send Message");
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_text_receive;

        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text_receive = itemView.findViewById(R.id.tv_text_receive);
        }

        public void bindData(){
            tv_text_receive.setText("Received Message");
        }
    }
}
