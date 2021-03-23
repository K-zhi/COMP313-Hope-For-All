package comp321.hope_for_all.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UserListAdapter extends BaseAdapter {
    private ArrayList<User> list;
    private Activity activity;

    public UserListAdapter(Activity activity) {
        this.activity = activity;
        list = new ArrayList<>();
    }

    public void setUser(User user) {
        list.add(user);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserListHolder userListHolder = null;
        final int pos = position;
        TextView userName, userId;
        ImageView icon;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_alert_dialog_user_list, parent, false);
            userName = (TextView) convertView.findViewById(R.id.alertDialogNameItemTextView);
            userId = (TextView) convertView.findViewById(R.id.alertDialogIdItemTextView);
            icon = (ImageView) convertView.findViewById(R.id.alertDialogItemImageView);

            userListHolder = new UserListHolder();
            userListHolder.userId = userId;
            userListHolder.userName = userName;
            userListHolder.image = icon;

            convertView.setTag(userListHolder);
            userId.setVisibility(View.INVISIBLE);
        } else {
            userListHolder = (UserListHolder) convertView.getTag();
            userName = userListHolder.userName;
        }

        userName.setText(list.get(pos).userName);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return null;
    }

    private class UserListHolder {
        TextView userName;
        TextView userId;
        ImageView image;
    }
}
