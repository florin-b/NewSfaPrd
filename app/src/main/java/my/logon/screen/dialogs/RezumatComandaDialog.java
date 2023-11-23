package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterRezumatComanda;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.listeners.ComandaMathausListener;
import my.logon.screen.listeners.RezumatListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;

public class RezumatComandaDialog extends Dialog implements RezumatListener {

    private Context context;
    private List<ArticolComanda> listArticole;
    private ListView listViewComenzi;
    private String canalDistrib;
    private Button btnCancelComanda;
    private Button btnOkComanda;
    private ComandaMathausListener listener;
    private List<CostTransportMathaus> costTransport;
    private String tipTransport;
    private String filialeArondate;
    private LinearLayout layoutInfo;
    private Button btnAdresaLivrare;
    private boolean selectTransp;
    private TextView textTotalComanda;
    private List<RezumatComanda> listRezumat;
    private NumberFormat nf2 = new DecimalFormat("#,##0.00");
    private AdapterRezumatComanda adapterRezumat;

    public RezumatComandaDialog(Context context, List<ArticolComanda> listArticole, String canal, List<CostTransportMathaus> costTransport, String tipTransport, String filialeArondate, boolean selectTransp) {
        super(context);
        this.context = context;
        this.listArticole = listArticole;
        this.canalDistrib = canal;
        this.costTransport = costTransport;
        this.tipTransport = tipTransport;
        this.filialeArondate = filialeArondate;
        this.selectTransp = selectTransp;

        setContentView(R.layout.rezumat_comanda_dialog);
        setTitle("Rezumat comanda");
        setCancelable(false);

        setupLayout();

    }

    private void setupLayout() {

        listViewComenzi = (ListView) findViewById(R.id.listComenzi);

        adapterRezumat = new AdapterRezumatComanda(context, getRezumatComanda(), costTransport, tipTransport, filialeArondate, selectTransp, canalDistrib);
        adapterRezumat.setRezumatListener(this);
        listViewComenzi.setAdapter(adapterRezumat);

        this.listRezumat = getRezumatComanda();

        btnCancelComanda = (Button) findViewById(R.id.btnCancelComanda);
        setListenerBtnCancel();
        btnOkComanda = (Button) findViewById(R.id.btnOkComanda);
        setListenerComandaSalvata();

        layoutInfo = (LinearLayout) findViewById(R.id.layoutInfo);
        btnAdresaLivrare = (Button) findViewById(R.id.btnAdresaLivrare);
        setListenerAdresaLivrare();

        textTotalComanda = (TextView) findViewById(R.id.textTotalComanda);
        getTotalComenzi();

    }

    private void setListenerBtnCancel() {
        btnCancelComanda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    private void setListenerComandaSalvata() {
        btnOkComanda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {

                    if (listViewComenzi.getAdapter().getCount() > 0)
                        listener.comandaSalvata();

                    dismiss();
                }

            }
        });
    }

    private void setListenerAdresaLivrare() {
        btnAdresaLivrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.redirectDateLivrare();
                dismiss();
            }
        });
    }

    private List<RezumatComanda> getRezumatComanda() {

        Set<String> filiale = getFilialeComanda();

        List<RezumatComanda> listComenzi = new ArrayList<RezumatComanda>();

        for (String filiala : filiale) {

            RezumatComanda rezumat = new RezumatComanda();
            rezumat.setFilialaLivrare(filiala);
            List<ArticolComanda> listArtComanda = new ArrayList<ArticolComanda>();

            for (ArticolComanda articol : listArticole) {
                if (articol.getFilialaSite().equals(filiala)) {
                    listArtComanda.add(articol);
                }
            }

            rezumat.setListArticole(listArtComanda);
            listComenzi.add(rezumat);
        }

        return listComenzi;
    }

    private Set<String> getFilialeComanda() {

        Set<String> filiale = new HashSet<String>();
        for (final ArticolComanda articol : listArticole) {
            filiale.add(articol.getFilialaSite());
        }
        return filiale;

    }

    public void setRezumatListener(ComandaMathausListener listener) {
        this.listener = listener;
    }

    public void showDialog() {
        this.show();
    }

    @Override
    public void comandaEliminata(List<String> listArticoleEliminate, String filialaLivrare) {

        List<ArticolComanda> listArticoleComanda;
        if (canalDistrib.equals("10"))
            listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();
        else
            listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();

        Iterator<ArticolComanda> listIterator = listArticoleComanda.iterator();

        while (listIterator.hasNext()) {
            ArticolComanda articol = listIterator.next();

            for (String articolEliminat : listArticoleEliminate) {

                if (articol.getCodArticol().equals(articolEliminat) && articol.getFilialaSite().equals(filialaLivrare)) {
                    listIterator.remove();
                    break;
                }

            }

        }

        if (listener != null)
            listener.comandaEliminata();

    }

    @Override
    public void adaugaArticol(ArticolComanda articolComanda) {

        List<ArticolComanda> listArticoleComanda;
        if (canalDistrib.equals("10"))
            listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleLivrare();
        else
            listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleLivrare();

        listArticoleComanda.add(articolComanda);

        if (listener != null)
            listener.comandaEliminata();

        getTotalComenzi();


    }

    @Override
    public void eliminaArticol(ArticolComanda articolComanda) {

        List<ArticolComanda> listArticoleComanda;
        if (canalDistrib.equals("10"))
            listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleLivrare();
        else
            listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleLivrare();

        Iterator<ArticolComanda> listIterator = listArticoleComanda.iterator();

        while (listIterator.hasNext()) {
            ArticolComanda articol = listIterator.next();

            if (articol.getCodArticol().equals(articolComanda.getCodArticol()) && articol.getFilialaSite().equals(articolComanda.getFilialaSite())) {
                listIterator.remove();
                break;
            }
        }

        if (listener != null)
            listener.comandaEliminata();

        getTotalComenzi();



    }

    private boolean isCom1() {

        for (ArticolComanda articol : listArticole) {
            if (articol.getTipTransport() != null && articol.getTipTransport().equals("TRAP"))
                return true;
        }

        return false;
    }

    private void getTotalComenzi(){

        double valoareTotal = 0;

        for (RezumatComanda rezumatComanda : getRezumatComanda()){
            valoareTotal += getTotalComanda(rezumatComanda);
        }

        textTotalComanda.setText(nf2.format(valoareTotal));

    }

    private double getTotalComanda(RezumatComanda rezumat) {

        double valoareTotal = 0;

        for (ArticolComanda art : rezumat.getListArticole()) {

            if (art instanceof ArticolComandaGed)
                valoareTotal += canalDistrib.equals("10") ? art.getPret() : art.getPretUnitarClient() * art.getCantUmb();
            else
                valoareTotal += art.getPret();
        }

        return valoareTotal;
    }

    @Override
    public void setStareRezumat(String codStare, String filialaLivrare) {

        if (DateLivrare.getInstance().getFilialaLivrareTCLI() == null || DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog().isEmpty())
            return;

        if ((codStare.equals("0") && !isCom1()) || !selectTransp) {
            btnOkComanda.setVisibility(View.VISIBLE);
            layoutInfo.setVisibility(View.INVISIBLE);
        } else {
            btnOkComanda.setVisibility(View.INVISIBLE);
            layoutInfo.setVisibility(View.VISIBLE);
        }

    }

    public void setListArticole(List<ArticolComanda> listArticole){
        this.listArticole = listArticole;
        adapterRezumat.setListRezumat(getRezumatComanda());
    }


}
