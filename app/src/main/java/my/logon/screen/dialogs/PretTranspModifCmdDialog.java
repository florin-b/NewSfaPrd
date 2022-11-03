package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.listeners.TranspModifCmdListener;

public class PretTranspModifCmdDialog extends Dialog {

    private Button btnSave;
    private EditText editTextValue;
    private TextView textStatus, textEtichetaLinie;
    private TranspModifCmdListener listener;
    private double pretMinTransport;


    public PretTranspModifCmdDialog(Context context, double pretMinTransp) {
        super(context);
        setContentView(R.layout.pret_transp_modif_cmd_dialog);
        setTitle("Modifica valoare transport");
        setCancelable(true);

        this.pretMinTransport = pretMinTransp;

        setupLayout();

    }

    private void setupLayout() {

        btnSave = (Button) findViewById(R.id.btnSave);

        setListenerOkButton();

        editTextValue = (EditText) findViewById(R.id.textValoareLinie);
        editTextValue.setSelection(editTextValue.getText().length());
        editTextValue.setText(String.valueOf(pretMinTransport));

        textEtichetaLinie = (TextView) findViewById(R.id.textEtichetaLinie);
        textEtichetaLinie.setText("Pret transport (minim " + pretMinTransport + " lei):");

        textStatus = (TextView) findViewById(R.id.textStatus);
        textStatus.setVisibility(View.INVISIBLE);

    }

    private void setListenerOkButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!isPretTransportValid(editTextValue.getText().toString().trim())) {
                    textStatus.setText("Pretul trebuie sa fie minim " + pretMinTransport + " lei");
                    textStatus.setVisibility(View.VISIBLE);
                    return;
                }

                if (listener != null) {
                    listener.pretTransportModif(Double.valueOf(editTextValue.getText().toString().trim()));
                    dismiss();
                }

            }
        });
    }

    private boolean isPretTransportValid(String pretTransport) {

        if (pretTransport.trim().isEmpty())
            return false;

        if (Double.valueOf(pretTransport) > 9999)
            return false;

        return Double.valueOf(pretTransport) >= Double.valueOf(pretMinTransport);
    }

    public void setPretTransportListener(TranspModifCmdListener listener) {
        this.listener = listener;
    }

}
