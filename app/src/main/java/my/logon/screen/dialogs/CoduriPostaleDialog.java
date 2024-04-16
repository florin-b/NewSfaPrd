package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterCoduriPostale;
import my.logon.screen.beans.BeanCodPostal;
import my.logon.screen.listeners.CodPostalListener;

public class CoduriPostaleDialog extends Dialog {


    private Context context;
    private ListView listViewCoduriPostale;
    private Button btnSalveaza;
    private Button btnRenunta;
    private CodPostalListener codPostalListener;
    private List<BeanCodPostal> listCoduriPostale;
    private String codPostalSelected;

    public CoduriPostaleDialog(Context context, List<BeanCodPostal> listCoduriPostale) {
        super(context);
        this.context = context;
        this.listCoduriPostale = listCoduriPostale;

        setContentView(R.layout.coduri_postale_dialog);
        setTitle("Selectati codul postal:");
        setCancelable(false);
        setupLayout();

    }

    private void setupLayout() {

        listViewCoduriPostale = findViewById(R.id.listCoduriPostale);
        btnSalveaza = findViewById(R.id.btnSalveaza);
        btnRenunta = findViewById(R.id.btnRenunta);

        setListenerBtnSalveaza();
        setListenerBtnRenunta();
        setListenerZileLivrare();
        afisCoduriPostale();
    }

    private void setListenerBtnSalveaza() {
        btnSalveaza.setOnClickListener(v -> {
            if (codPostalListener != null)
                codPostalListener.codPostalSelected(codPostalSelected);

            dismiss();
        });
    }

    private void setListenerBtnRenunta() {
        btnRenunta.setOnClickListener(v -> dismiss());
    }

    private void setListenerZileLivrare() {
        listViewCoduriPostale.setOnItemClickListener((parent, view, position, id) -> {

            BeanCodPostal codPostal = (BeanCodPostal) listViewCoduriPostale.getAdapter().getItem(position);
            codPostalSelected = codPostal.getCodPostal();

            for (int j = 0; j < parent.getChildCount(); j++)
                parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

            view.setBackgroundColor(Color.LTGRAY);
        });

    }


    private void afisCoduriPostale() {
        AdapterCoduriPostale adapterCoduriPostale = new AdapterCoduriPostale(context, listCoduriPostale);
        listViewCoduriPostale.setAdapter(adapterCoduriPostale);
    }

    public void setCodPostalListener(CodPostalListener listener) {
        this.codPostalListener = listener;
    }


}
