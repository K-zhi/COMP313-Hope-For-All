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
import comp321.hope_for_all.models.Counselor;

public class CounselorListAdapter extends BaseAdapter {
    private final List<Counselor> counselorList;
    private final Activity context;

    public CounselorListAdapter(Activity context) {
        this.context = context;
        counselorList = new ArrayList<>();
    }

    public CounselorListAdapter(Activity context, List<Counselor> counselorList) {
        this.context = context;

        if(counselorList == null)
            counselorList = new ArrayList<>();
        this.counselorList = counselorList;
    }

    public class CounselorHolder {
        TextView counselorName;
        TextView counselorId;
        ImageView image;
    }

    public void setCounselor(Counselor counselor) {
        counselorList.add(counselor);
    }

    public List<Counselor> getCounselorList() {
        return this.counselorList;
    }

    @Override
    public int getCount() {
        return counselorList.size();
    }

    @Override
    public Object getItem(int position) {
        return counselorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_alert_dialog_user_item, parent, false);

            final CounselorListAdapter.CounselorHolder counselorHolder = new CounselorHolder();
            counselorHolder.counselorName = (TextView) view.findViewById(R.id.alertDialogNameItemTextView);
            counselorHolder.counselorId = (TextView) view.findViewById(R.id.alertDialogIdItemTextView);
            counselorHolder.image = (ImageView) view.findViewById(R.id.alertDialogItemImageView);

            view.setTag(counselorHolder);
        } else {
            view = convertView;
        }

        if(counselorList != null) {
            CounselorHolder holder = (CounselorHolder) view.getTag();
            holder.counselorName.setText(counselorList.get(position).c_userName);
            holder.counselorId.setText(counselorList.get(position).c_email);
            holder.image.setImageResource(R.drawable.ic_brain);
        }

        return view;
    }
}
