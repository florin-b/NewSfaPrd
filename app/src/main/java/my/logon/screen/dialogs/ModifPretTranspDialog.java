package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterRezumatComanda;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.listeners.ModifPretTransportListener;

public class ModifPretTranspDialog extends Dialog {

    private Button btnSave;
    private EditText editTextValue;
    private TextView textStatus, textEtichetaLinie;
    private ModifPretTransportListener listener;
    private RezumatComanda rezumat;
    private String pretMinTransport;
    private AdapterRezumatComanda.ViewHolder viewHolder;

    public ModifPretTranspDialog(Context context, RezumatComanda rezumat, String pretMinTransp, AdapterRezumatComanda.ViewHolder viewHolder) {
        super(context);
        setContentView(R.layout.modif_pret_transp_dialog);
        setTitle("Modifica valoare transport");
        setCancelable(true);
        this.rezumat = rezumat;
        this.pretMinTransport = pretMinTransp;
        this.viewHolder = viewHolder;
        setupLayout();

    }

    private void setupLayout() {

        btnSave = (Button) findViewById(R.id.btnSave);

        setListenerOkButton();

        editTextValue = (EditText) findViewById(R.id.textValoareLinie);
        editTextValue.setSelection(editTextValue.getText().length());
        editTextValue.setText(pretMinTransport);

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
                    listener.pretTransportModificat(rezumat, editTextValue.getText().toString().trim(), viewHolder);
                    dismiss();
                }

            }
        });
    }

    private boolean isPretTransportValid(String pretTransport) {

        if (pretTransport.trim().isEmpty())
            return false;

        if (Double.valueOf(pretTransport) > 5000)
            return false;

        return Double.valueOf(pretTransport) >= Double.valueOf(pretMinTransport);
    }

    public void setPretTransportListener(ModifPretTransportListener listener) {
        this.listener = listener;
    }

}
