package my.logon.screen.dialogs;

import my.logon.screen.listeners.OfertaMailListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class OfertaMailDialog extends Dialog {

	private Context context;
	private ImageButton sendButton, cancelButton;
	private OfertaMailListener listener;
	private EditText textAdresaMail;
	private String adresaMail;

	public OfertaMailDialog(Context context, String adresaMail) {
		super(context);
		this.context = context;
		this.adresaMail = adresaMail;

		setContentView(R.layout.oferta_mail_dialog);
		setTitle("Expediere oferta");
		setCancelable(true);

		setupLayout();
	}

	private void setupLayout() {

		textAdresaMail = (EditText) findViewById(R.id.textAdresaMail);
		textAdresaMail.setText(adresaMail);
		sendButton = (ImageButton) findViewById(R.id.btnSave);
		cancelButton = (ImageButton) findViewById(R.id.btnCancel);

		setSendButtonListener();
		setCancelButtonListener();
	}

	private void setSendButtonListener() {
		sendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String mailAddr = textAdresaMail.getText().toString().trim();

				if (isAdresaValid(mailAddr)) {
					if (listener != null) {
						listener.sendMail(mailAddr);
						dismiss();
					}
				} else {
					Toast.makeText(context, "Adresa invalida", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	public void setOfertaMailListener(OfertaMailListener listener) {
		this.listener = listener;
	}

	private boolean isAdresaValid(String mailAddr) {

		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return mailAddr.matches(EMAIL_REGEX);

	}

	private void setCancelButtonListener() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
