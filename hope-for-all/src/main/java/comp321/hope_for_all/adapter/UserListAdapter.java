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
import java.util.List;

import comp321.hope_for_all.R;
import comp321.hope_for_all.models.User;

public class UserListAdapter extends BaseAdapter {
    private final List<User> userList;
    private final Activity context;

    public class UserListHolder {
        TextView userName;
        TextView userId;
        ImageView image;
    }

    public UserListAdapter(Activity context, List<User> userList) {
        //super(context, R.layout.custom_alert_dialog_user_item, userList);
        this.context = context;
        if(userList == null)
            userList = new ArrayList<>();

        this.userList = userList;
    }

    public void setUser(User user) {
        userList.add(user);
    }

    public void setUserList(List<User> list) {
        list = list;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

//        UserListHolder userListHolder = null;
//        final int pos = position;
//        TextView userName, userId;
//        ImageView icon;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_alert_dialog_user_item, parent, false);

            final UserListHolder userHolder = new UserListHolder();
            userHolder.userName = (TextView) convertView.findViewById(R.id.alertDialogNameItemTextView);
            userHolder.userId = (TextView) convertView.findViewById(R.id.alertDialogIdItemTextView);
            userHolder.image = (ImageView) convertView.findViewById(R.id.alertDialogItemImageView);

            view.setTag(userHolder);

            //            userListHolder = new UserListHolder();
//            userListHolder.userId = userId;
//            userListHolder.userName = userName;
//            userListHolder.image = icon;
//
//            convertView.setTag(userListHolder);
            //userId.setVisibility(View.INVISIBLE);
        } else {
            view = convertView;
            //userListHolder = (UserListHolder) convertView.getTag();
            //userName = userListHolder.userName;
        }

        //userName.setText(list.get(pos).userName);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
