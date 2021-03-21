package comp321.hope_for_all.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import comp321.hope_for_all.R;
public class CustomAlertDialog {

    private Activity activity;
    private AlertDialog dialog;

    public CustomAlertDialog(Activity activity){
        this.activity = activity;
    }

    public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_alert_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();

    }

    public void dismissDialog(){
        dialog.dismiss();

    }


}
