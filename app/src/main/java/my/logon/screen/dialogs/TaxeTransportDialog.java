package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterTaxeTransport;
import my.logon.screen.beans.Details;

public class TaxeTransportDialog extends Dialog {


    private Context context;
    private ListView listViewTaxeTransport;
    private Button btnInchide;
    private Button btnRenunta;
    private List<Details> listTaxeTransport;

    public TaxeTransportDialog(Context context, List<Details> listTaxeTransport) {
        super(context);
        this.context = context;
        this.listTaxeTransport = listTaxeTransport;

        setContentView(R.layout.taxe_transport_dialog);
        setTitle("Taxe transport");
        setCancelable(false);
        setupLayout();

    }

    private void setupLayout() {

        listViewTaxeTransport = findViewById(R.id.listTaxeTransport);
        btnInchide = findViewById(R.id.btnInchide);
        btnRenunta = findViewById(R.id.btnRenunta);

        setListenerBtnSalveaza();
        afisTaxeTransport();
    }

    private void setListenerBtnSalveaza() {
        btnInchide.setOnClickListener(v -> {
            dismiss();
        });
    }


    private void afisTaxeTransport() {
        AdapterTaxeTransport adapterTaxeTransport = new AdapterTaxeTransport(context, listTaxeTransport);
        listViewTaxeTransport.setAdapter(adapterTaxeTransport);
    }


}
