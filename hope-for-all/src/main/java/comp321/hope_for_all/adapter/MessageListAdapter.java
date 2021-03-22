package comp321.hope_for_all.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.ChatData;

public class MessageListAdapter extends BaseAdapter {
    private ArrayList<ChatData> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ChatData getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_chat_list, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.ivChatImage);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtMessageName);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtMessageContent);

        ChatData item = getItem(position);

        //image.setImageDrawable(R.drawable.ic_brain);
        txtName.setText(item.getNickname());
        txtContent.setText(item.getMsg());

        return convertView;
    }

    public void addItem(Drawable img, String name, String content) {
        ChatData mItem = new ChatData();

        mItem.setNickname(name);
        mItem.setMsg(content);

        mItems.add(mItem);
    }
}
