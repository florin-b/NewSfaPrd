package my.logon.screen.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {


    public void showInfoDialog(Context context, String message){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(
                    message).setCancelable(false)
                    .setPositiveButton("Inchide", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();

    }

}
