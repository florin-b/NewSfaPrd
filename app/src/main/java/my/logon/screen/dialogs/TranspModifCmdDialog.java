package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.listeners.ModifCmdTranspListener;
import my.logon.screen.listeners.TranspModifCmdListener;

public class TranspModifCmdDialog extends Dialog implements TranspModifCmdListener {

    private Button btnSave, btnCancel;
    private TextView textValTransport;
    private List<CostTransportMathaus> listCostTransport;
    private ImageButton btnPretTransport;
    private double valTransp;
    private Context context;
    private ModifCmdTranspListener listener;

    public TranspModifCmdDialog(List<CostTransportMathaus> listCostTransport, Context context) {
        super(context);

        setContentView(R.layout.transp_modif_cmd_dialog);
        setTitle("Info");
        setCancelable(true);
        this.context = context;
        this.listCostTransport = listCostTransport;

        setupLayout();

    }

    private void setupLayout() {

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        textValTransport = (TextView) findViewById(R.id.textValTransp);
        btnPretTransport = (ImageButton) findViewById(R.id.btnPretTransport);

        for (CostTransportMathaus costTransportMathaus : listCostTransport) {
            valTransp += Double.valueOf(costTransportMathaus.getValTransp());
        }

        textValTransport.setText(valTransp + " lei.");

        setListenerOkButton();
        setListenerCancelButton();
        setListenerBtnTransp();

    }

    private void setListenerBtnTransp() {
        btnPretTransport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PretTranspModifCmdDialog transpDialog = new PretTranspModifCmdDialog(context, valTransp);
                transpDialog.setPretTransportListener(TranspModifCmdDialog.this);
                transpDialog.show();


            }
        });
    }

    private void setListenerOkButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.pretTranspSalvat();
                    dismiss();
                }

            }
        });
    }

    private void setListenerCancelButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dismiss();

            }
        });
    }


    @Override
    public void pretTransportModif(double pretTransport) {
        textValTransport.setText(pretTransport + " lei.");

        for (CostTransportMathaus costTransportMathaus : listCostTransport) {

            if (Double.valueOf(costTransportMathaus.getValTransp()) > 0) {
                costTransportMathaus.setValTransp(String.valueOf(pretTransport));
                break;
            }

        }

        if (listener != null)
            listener.pretTranspModificat(listCostTransport);
    }

    public void setTranspModifCmdListener(ModifCmdTranspListener listener) {
        this.listener = listener;
    }
}
