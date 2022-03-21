package my.logon.screen.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class HelperDialog {

	public static void showInfoDialog(Context context, String strHeader, String strMessage) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle(strHeader);

		alertDialogBuilder.setMessage(strMessage).setCancelable(false).setPositiveButton("Inchide", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

}
