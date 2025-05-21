//Creat de Robert
package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterPaleti;
import my.logon.screen.adapters.AdapterTransportFiliala;
import my.logon.screen.beans.ArticolDescarcare;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanTaxaCamion;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.TaxaMasina;
import my.logon.screen.beans.TaxaTransport;
import my.logon.screen.beans.TaxeLivrare;
import my.logon.screen.enums.EnumTipCamion;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.listeners.TaxeMasiniListener;
import my.logon.screen.model.DateLivrare;

public class TaxeMasiniDialog extends Dialog {

    private Context context;
    private Button btnContinua, btnRenunta;
    private TaxeMasiniListener listener;
    private ListView listTransportFiliala;
    private Spinner spinnerPaleti;
    private LivrareMathaus dateLivrare;
    private List<TaxaTransport> taxeTransport;

    private AdapterPaleti adapterPaleti;

    private Button btnAdaugaPaleti;
    private TextView textValPalet;
    private Button btnRenuntaPaleti;
    private TextView textPaletSel;
    private EditText textCantPaletSel;
    private LinearLayout layoutPaletSel;
    private ArticolPalet articol;
    private NumberFormat nf2;
    private LinearLayout layoutPaleti;
    private AdapterTransportFiliala adapterTransportFiliala;


    public TaxeMasiniDialog(Context context, LivrareMathaus dateLivrare) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.taxe_masini_dialog);
        this.context = context;
        setCancelable(false);

        this.dateLivrare = dateLivrare;
        nf2 = new DecimalFormat("#0.00");

        btnContinua = findViewById(R.id.btnContinua);
        addBtnContinuaListener();

        btnRenunta = findViewById(R.id.btnCancel);
        addBtnRenuntaListener();

        layoutPaleti = findViewById(R.id.layoutPaleti);

        if (!dateLivrare.getListPaleti().isEmpty()) {
            setupPaletiLayout();
        } else {
            layoutPaleti.setVisibility(View.GONE);
        }

        setupMasiniLayout();

    }

    private void setupMasiniLayout() {

        listTransportFiliala = findViewById(R.id.listTransportFiliala);
        taxeTransport = getTaxeTransport(dateLivrare);

        adapterTransportFiliala = new AdapterTransportFiliala(context, taxeTransport, dateLivrare);
        listTransportFiliala.setAdapter(adapterTransportFiliala);


    }

    private void setupPaletiLayout() {

        spinnerPaleti = findViewById(R.id.spinnerPaleti);

        adapterPaleti = new AdapterPaleti(context, getPaletiAdapter());
        spinnerPaleti.setAdapter(adapterPaleti);
        setListViewPaletiListener();

        textPaletSel = findViewById(R.id.textPaletSel);
        textCantPaletSel = findViewById(R.id.textCantPaletSel);
        setListenerTextCantPaleti();

        layoutPaletSel = findViewById(R.id.layoutPaletSel);

        textValPalet = findViewById(R.id.textValPalet);
        setTotalPaleti();

        btnAdaugaPaleti = findViewById(R.id.btnOkPalet);
        btnAdaugaPaleti.setVisibility(View.GONE);
        btnAdaugaPaleti.setText("Modifica");
        setBtnModificaPaletiListener();
        btnRenuntaPaleti = findViewById(R.id.btnCancelPalet);
        btnRenuntaPaleti.setVisibility(View.GONE);

        layoutPaletSel.setVisibility(View.GONE);

    }

    private List<ArticolPalet> getPaletiAdapter() {
        List<ArticolPalet> copyListPaleti = HelperMathaus.getListPaletiCopy(dateLivrare.getListPaleti());

        ArticolPalet articolPalet = new ArticolPalet();
        articolPalet.setNumePalet("Selectati articolul pentru modificare");
        articolPalet.setCantitate(0);
        articolPalet.setPretUnit(0);
        copyListPaleti.add(0, articolPalet);

        return copyListPaleti;

    }

    private void setTotalPaleti() {
        double cantTotal = 0;
        double valTotal = 0;
        NumberFormat nf2 = new DecimalFormat("#0.00");

        for (ArticolPalet palet : dateLivrare.getListPaleti()) {
            cantTotal += palet.getCantitate();
            valTotal += palet.getCantitate() * palet.getPretUnit();

        }

        textValPalet.setText((int) cantTotal + " BUC   " + nf2.format(valTotal) + " RON");

    }

    private void setListViewPaletiListener() {

        spinnerPaleti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    articol = dateLivrare.getListPaleti().get(position - 1);
                    layoutPaletSel.setVisibility(View.VISIBLE);
                    textPaletSel.setText(articol.getNumePalet());
                    textCantPaletSel.setText(String.valueOf(articol.getCantitate()));
                    btnAdaugaPaleti.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setListenerTextCantPaleti() {

        textCantPaletSel.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    articol.setCantitate(Integer.parseInt(s.toString()));
                    adapterPaleti.setListPaleti(getPaletiAdapter());
                    setTotalPaleti();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setBtnModificaPaletiListener() {
        btnAdaugaPaleti.setOnClickListener(v -> {

            if (!verificaNrPaleti())
                return;
            else {
                btnAdaugaPaleti.setVisibility(View.GONE);
                layoutPaletSel.setVisibility(View.GONE);
                spinnerPaleti.setSelection(0);
                adapterTransportFiliala.notifyDataSetChanged();
            }

        });
    }

    private boolean verificaNrPaleti() {

        int nrPaleti = 0;

        for (ArticolPalet palet : dateLivrare.getListPaleti())
            nrPaleti += palet.getCantitate();

        if (nrPaleti > 0)
            return true;
        else {
            Toast.makeText(context, "Adaugati cel putin un palet.", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private List<TaxaTransport> getTaxeTransport(LivrareMathaus dateLivrare) {

        List<TaxaTransport> listTaxeTransport = new ArrayList<>();
        Set<String> setFiliale = new HashSet<>();

        for (TaxaMasina taxaMasina : dateLivrare.getTaxeMasini()) {
            setFiliale.add(taxaMasina.getWerks());
        }

        for (String filialaTaxa : setFiliale) {

            TaxaTransport taxaTransport = new TaxaTransport();
            List<BeanTaxaCamion> listTaxeCamion = new ArrayList<>();
            taxaTransport.setFiliala(filialaTaxa);

            for (TaxaMasina taxaMasina : dateLivrare.getTaxeMasini()) {

                if (!taxaMasina.getTraty().equals("TRAP"))
                    continue;

                if (filialaTaxa.equals(taxaMasina.getWerks())) {

                    BeanTaxaCamion taxaCamion = new BeanTaxaCamion();

                    TaxeLivrare taxeLivrare = new TaxeLivrare();

                    if (taxaMasina.isCamionIveco()) {
                        taxaCamion.setTipCamion(EnumTipCamion.CAMION_IVECO);
                    } else if (taxaMasina.isCamionScurt()) {
                        taxaCamion.setTipCamion(EnumTipCamion.CAMION_SCURT);
                    } else if (taxaMasina.isCamionOricare()) {
                        taxaCamion.setTipCamion(EnumTipCamion.CAMION_ORICARE);
                    }

                    taxeLivrare.setCodTaxaTransport(taxaMasina.getMatnrTransport());
                    taxeLivrare.setNumeTaxaTransport(taxaMasina.getMaktxTransport());
                    taxeLivrare.setValoareTaxaTransport(taxaMasina.getTaxaTransport());
                    taxeLivrare.setCodTaxaZona(taxaMasina.getMatnrZona());
                    taxeLivrare.setNumeTaxaZona(taxaMasina.getMaktxZona());
                    taxeLivrare.setValoareTaxaZona(taxaMasina.getTaxaZona());
                    taxeLivrare.setCodTaxaAcces(taxaMasina.getMatnrAcces());
                    taxeLivrare.setNumeTaxaAcces(taxaMasina.getMaktxAcces());
                    taxeLivrare.setValoareTaxaAcces(taxaMasina.getTaxaAcces());
                    taxeLivrare.setTaxaMacara(taxaMasina.getTaxaMacara());
                    taxeLivrare.setMacara(taxaMasina.isMacara());
                    taxeLivrare.setNrPaleti(taxaMasina.getNrPaleti());
                    taxeLivrare.setLift(taxaMasina.isLift());
                    taxeLivrare.setCodTaxaMacara(taxaMasina.getMatnrMacara());
                    taxeLivrare.setNumeTaxaMacara(taxaMasina.getMaktxMacara());
                    taxeLivrare.setDepart(taxaMasina.getSpart());
                    taxeLivrare.setTaxeDivizii(taxaMasina.getTaxeDivizii());
                    taxaCamion.setTaxeLivrare(taxeLivrare);
                    listTaxeCamion.add(taxaCamion);

                    taxaTransport.setListTaxe(listTaxeCamion);

                }
            }

            if (taxaTransport.getListTaxe() != null)
                listTaxeTransport.add(taxaTransport);
        }


        return listTaxeTransport;
    }


    private void trateazaTipMasina(BeanTaxaCamion taxaCamion) {

        DateLivrare.getInstance().setTipMasina("");

        if (taxaCamion.getTipCamion().equals(EnumTipCamion.CAMION_IVECO)) {
            DateLivrare.getInstance().setTipMasina("iveco");
        } else if (taxaCamion.getTipCamion().equals(EnumTipCamion.CAMION_SCURT))
            DateLivrare.getInstance().setTipMasina("scurt");


    }

    private void setTaxeTransportAgent() {


        for (TaxaTransport taxaTransport : taxeTransport) {

            for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {
                if (taxaTransport.getSelectedCamion().equals(taxaCamion.getTipCamion()) && isCamionLiftMacara(taxaCamion.getTaxeLivrare()) == taxaTransport.isAcceptaMacara()) {

                    trateazaTipMasina(taxaCamion);

                    CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                    costTransportMathaus.setCodArtTransp(taxaCamion.getTaxeLivrare().getCodTaxaTransport());
                    costTransportMathaus.setNumeCost(taxaCamion.getTaxeLivrare().getNumeTaxaTransport());
                    costTransportMathaus.setValTransp(String.valueOf(taxaCamion.getTaxeLivrare().getValoareTaxaTransport()));
                    costTransportMathaus.setFiliala(taxaTransport.getFiliala());
                    costTransportMathaus.setTipTransp("TRAP");
                    costTransportMathaus.setDepart(taxaCamion.getTaxeLivrare().getDepart());
                    dateLivrare.getCostTransport().add(costTransportMathaus);

                    if (taxaCamion.getTaxeLivrare().getValoareTaxaZona() > 0) {
                        costTransportMathaus = new CostTransportMathaus();
                        costTransportMathaus.setCodArtTransp(taxaCamion.getTaxeLivrare().getCodTaxaZona());
                        costTransportMathaus.setNumeCost(taxaCamion.getTaxeLivrare().getNumeTaxaZona());
                        costTransportMathaus.setValTransp(String.valueOf(taxaCamion.getTaxeLivrare().getValoareTaxaZona()));
                        costTransportMathaus.setFiliala(taxaTransport.getFiliala());
                        costTransportMathaus.setTipTransp("TRAP");
                        costTransportMathaus.setDepart(taxaCamion.getTaxeLivrare().getDepart());
                        dateLivrare.getCostTransport().add(costTransportMathaus);
                    }

                    if (taxaCamion.getTaxeLivrare().getValoareTaxaAcces() > 0) {
                        costTransportMathaus = new CostTransportMathaus();
                        costTransportMathaus.setCodArtTransp(taxaCamion.getTaxeLivrare().getCodTaxaAcces());
                        costTransportMathaus.setNumeCost(taxaCamion.getTaxeLivrare().getNumeTaxaAcces());
                        costTransportMathaus.setValTransp(String.valueOf(taxaCamion.getTaxeLivrare().getValoareTaxaAcces()));
                        costTransportMathaus.setFiliala(taxaTransport.getFiliala());
                        costTransportMathaus.setTipTransp("TRAP");
                        costTransportMathaus.setDepart(taxaCamion.getTaxeLivrare().getDepart());
                        dateLivrare.getCostTransport().add(costTransportMathaus);
                    }

                }
            }

        }

        HelperMathaus.setTransportTERT(dateLivrare);


    }


    private void addBtnContinuaListener() {
        btnContinua.setOnClickListener(v -> {

            if (!isTransportSelected()) {
                Toast.makeText(context, "Selectati tipul de transport.", Toast.LENGTH_LONG).show();
                return;
            }


            if (listener != null) {
                setTaxeTransportAgent();
                listener.tipMasinaFilialaSelected(dateLivrare, getCostDescarcare(), taxeTransport);
                dismiss();
            }

        });
    }

    private void addBtnRenuntaListener() {
        btnRenunta.setOnClickListener(v -> dismiss());
    }

    private boolean isTransportSelected() {
        for (TaxaTransport taxaTransport : taxeTransport) {
            if (taxaTransport.getSelectedCamion() == null) {
                return false;
            }
        }

        return true;
    }

    private CostDescarcare getCostDescarcare() {
        CostDescarcare costDescarcare = new CostDescarcare();
        List<ArticolDescarcare> listArticole = new ArrayList<ArticolDescarcare>();
        List<ArticolPalet> listPaleti = new ArrayList<ArticolPalet>();


        for (TaxaTransport taxaTransport : taxeTransport) {
            for (ArticolPalet articolPalet : dateLivrare.getListPaleti()) {

                for (DateArticolMathaus articolMathaus : dateLivrare.getComandaMathaus().getDeliveryEntryDataList()) {

                    if (articolPalet.getCodArticol().replaceFirst("^0*", "").equals(articolMathaus.getProductCode().replaceFirst("^0*", ""))
                            && articolMathaus.getDeliveryWarehouse().equals(taxaTransport.getFiliala())) {

                        ArticolPalet articol = new ArticolPalet();
                        articol.setCodPalet(articolPalet.getCodPalet());
                        articol.setNumePalet(articolPalet.getNumePalet());
                        articol.setDepart(articolPalet.getDepart());
                        articol.setCantitate(articolPalet.getCantitate());
                        articol.setPretUnit(articolPalet.getPretUnit());
                        articol.setFurnizor(articolPalet.getFurnizor());
                        articol.setCodArticol(articolPalet.getCodArticol());
                        articol.setNumeArticol(articolPalet.getNumeArticol());
                        articol.setCantArticol(articolPalet.getCantArticol());
                        articol.setUmArticol(articolPalet.getUmArticol());
                        articol.setFiliala(taxaTransport.getFiliala());
                        listPaleti.add(articol);

                    }
                }
            }
        }


        for (TaxaTransport taxaTransport : taxeTransport) {
            for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {
                if (taxaCamion.getTipCamion().equals(taxaTransport.getSelectedCamion()) && isCamionLiftMacara(taxaCamion.getTaxeLivrare()) == taxaTransport.isAcceptaMacara()) {

                    if (taxaTransport.getTaxaMacaraAgent() > 0) {

                        ArticolDescarcare articol = new ArticolDescarcare();
                        int nrPaleti = HelperMathaus.getNrPaletiFiliala(dateLivrare, taxaTransport.getFiliala());
                        articol.setCod(taxaCamion.getTaxeLivrare().getCodTaxaMacara());
                        articol.setDepart(taxaCamion.getTaxeLivrare().getDepart());
                        articol.setValoare(taxaTransport.getTaxaMacaraAgent() / nrPaleti);
                        articol.setCantitate(nrPaleti);
                        articol.setValoareMin(taxaCamion.getTaxeLivrare().getTaxaMacara());
                        articol.setFiliala(taxaTransport.getFiliala());
                        listArticole.add(articol);

                    }
                }
            }
        }

        costDescarcare.setArticoleDescarcare(listArticole);
        costDescarcare.setArticolePaleti(listPaleti);
        return costDescarcare;
    }

    private boolean isCamionLiftMacara(TaxeLivrare taxeLivrare) {
        return taxeLivrare.isLift() || taxeLivrare.isMacara();
    }


    private boolean isTaxaMacaraValid() {

        for (TaxaTransport taxaTransport : taxeTransport) {

            if (!taxaTransport.isAcceptaMacara())
                continue;

            for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {
                if (taxaCamion.getTipCamion().equals(taxaTransport.getSelectedCamion())) {

                    int nrPaleti = HelperMathaus.getNrPaletiFiliala(dateLivrare, taxaTransport.getFiliala());

                    double taxaMacaraFiliala = taxaCamion.getTaxeLivrare().getTaxaMacara() * nrPaleti;

                    if (taxaTransport.getTaxaMacaraAgent() < taxaMacaraFiliala) {
                        Toast.makeText(context, "Pentru livrarea din " + taxaTransport.getFiliala() + " taxa de macara trebuie sa fie minimum " + nf2.format(taxaMacaraFiliala) +
                                " lei.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }


        }

        return true;
    }

    public void setTipMasinaLivrareListener(TaxeMasiniListener listener) {
        this.listener = listener;
    }

}

