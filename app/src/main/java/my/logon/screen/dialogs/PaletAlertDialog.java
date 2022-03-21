package my.logon.screen.dialogs;

import my.logon.screen.listeners.PaletAlertListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import my.logon.screen.enums.EnumDaNuOpt;

public class PaletAlertDialog extends Dialog {

	private Context context;
	private PaletAlertListener listener;

	public PaletAlertDialog(Context context) {
		super(context);
		this.context = context;
	}

	public void showAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		alertDialogBuilder.setTitle("Atentie");
		alertDialogBuilder.setMessage("Comanda trebuie sa contina paleti!").setCancelable(false)
				.setPositiveButton("Continua salvarea", new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (listener != null)
							listener.paletDialogResponse(EnumDaNuOpt.DA);
						dialog.cancel();
					}
				}).setNegativeButton("Inchide", new OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (listener != null)
							listener.paletDialogResponse(EnumDaNuOpt.NU);
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void setPaletAlertListener(PaletAlertListener listener) {
		this.listener = listener;
	}

}
