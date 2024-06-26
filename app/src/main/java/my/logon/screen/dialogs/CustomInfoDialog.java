package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import my.logon.screen.R;

public class CustomInfoDialog extends Dialog {

	private TextView textAlert;
	private Button btnOk;
	private String alertText;
	

	public CustomInfoDialog(Context context, String title) {
		super(context);

		setContentView(R.layout.info_dialog);
		setTitle(title);

		setupLayout();

	}

	public void setInfoText(String alertText) {
		this.alertText = alertText;
	}
	
	

	

	private void setupLayout() {
		textAlert = (TextView) findViewById(R.id.textAlert);

		btnOk = (Button) findViewById(R.id.btnOk);
		setListenerBtnOk();

	}

	@Override
	public void show() {
		textAlert.setText(alertText);
		textAlert.setMovementMethod(new ScrollingMovementMethod());
		super.show();
	}

	private void setListenerBtnOk() {
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dismiss();

			}
		});
	}

}
