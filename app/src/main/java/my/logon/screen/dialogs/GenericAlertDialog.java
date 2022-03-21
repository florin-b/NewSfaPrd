package my.logon.screen.dialogs;

import my.logon.screen.listeners.GenericDialogListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumDialogConstraints;

public class GenericAlertDialog extends Dialog {

	private String alertMessage;
	private String title;
	private Context context;
	private GenericDialogListener listener;
	private EnumDialogConstraints dialogConstraint;

	public GenericAlertDialog(Context context, String title, String alertMessage, EnumDialogConstraints dialogConstraint) {
		super(context);
		this.context = context;
		this.title = title;
		this.alertMessage = alertMessage;
		this.dialogConstraint = dialogConstraint;
	}

	public void showAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(alertMessage).setCancelable(false).setPositiveButton("Da", new OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (listener != null)
					listener.dialogResponse(dialogConstraint, EnumDaNuOpt.DA);

				dialog.cancel();
			}
		}).setNegativeButton("Nu", new OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (listener != null)
					listener.dialogResponse(dialogConstraint, EnumDaNuOpt.NU);
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void setGenericDialogListener(GenericDialogListener listener) {
		this.listener = listener;
	}

}
