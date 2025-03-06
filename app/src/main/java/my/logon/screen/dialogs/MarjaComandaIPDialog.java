package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.listeners.MarjaComandaIPListener;

public class MarjaComandaIPDialog extends Dialog {

    private TextView textAlert;
    private Button btnOk, btnCancel;
    private boolean isBlocat;
    public MarjaComandaIPListener listener;
    private double valoareTaxe;


    public MarjaComandaIPDialog(Context context, boolean isBlocat, double valoareTaxe) {
        super(context);
        this.isBlocat = isBlocat;
        this.valoareTaxe = valoareTaxe;
        setContentView(R.layout.info_comanda_ip_dialog);
        setupLayout();
    }


    private void setupLayout() {
        textAlert = findViewById(R.id.textAlert);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Marja comenzii nu acopera costurile de transport/servicii!");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Cresteti marja comenzii cu " + String.format("%.02f", valoareTaxe) + " lei, altfel comanda va fi trimisa spre aprobare.");

        textAlert.setText(stringBuilder.toString());

        if (isBlocat) {
            setTitle("Atentie!");
            btnOk.setText("Revenire");
        } else {
            setTitle("Info");
            btnOk.setText("Continua");
        }

        setListenerBtnOk();
        setListenerBtnCancel();

    }

    @Override
    public void show() {
        super.show();
    }

    private void setListenerBtnOk() {
        btnOk.setOnClickListener(arg0 -> {
            if (listener != null)
                listener.comandaIPSelected(isBlocat);

            dismiss();

        });
    }

    private void setListenerBtnCancel() {
        btnCancel.setOnClickListener(v -> dismiss());
    }

    public void setMarjaComamdaIPListener(MarjaComandaIPListener listener) {
        this.listener = listener;
    }

}
