package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterServiciiAC;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.BeanAdresaGenerica;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.listeners.ServiciiACListener;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.utils.UtilsGeneral;

public class ArticoleServiciiACDialog extends Dialog implements OperatiiAdresaListener {

    private Context context;
    private Spinner spinnerServiciiAC;
    private AdapterServiciiAC adapterServiciiAC;
    private ServiciiACListener listener;

    private List<ArticolDB> listArticoleInit;
    private Button btnAdaugaArticol;
    private Button btnRenuntaPaleti;
    private ArticolDB servSelectat;
    private String selectedTipCant = "";
    private String selectedFurnizor = "";
    private Spinner spinnerJudet;
    private ArrayList<HashMap<String, String>> listJudete;
    private SimpleAdapter adapterJudete;
    private OperatiiAdresa operatiiAdresa;
    private AutoCompleteTextView textLocalitate, textStrada;
    private String codJudetInstalare = "";
    private boolean existaServicuComanda;


    public ArticoleServiciiACDialog(Context context, List<ArticolDB> listArticole, boolean existaServCmd) {
        super(context);
        this.context = context;
        this.listArticoleInit = listArticole;
        this.existaServicuComanda = existaServCmd;

        setContentView(R.layout.select_servicii_ac_dialog);
        setTitle("Servicii instalare AC");
        setCancelable(true);

        findViewById(R.id.layoutAdr1).setVisibility(View.GONE);
        findViewById(R.id.layoutAdr2).setVisibility(View.GONE);
        findViewById(R.id.layoutLabelAdresa).setVisibility(View.GONE);

        setUpLayout();

        if (DateLivrare.getInstance().getTransport().equals("TCLI"))
            setupAdresaInstalare();

        setSpinnerPaletiListener();

    }

    public void showDialog() {
        this.show();
    }


    private void setupAdresaInstalare() {

        spinnerJudet = findViewById(R.id.spinnerJudet);
        textLocalitate = findViewById(R.id.autoCompleteLocalitate);
        textStrada = findViewById(R.id.autoCompleteStrada);

        findViewById(R.id.layoutAdr1).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutAdr2).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutLabelAdresa).setVisibility(View.VISIBLE);

        operatiiAdresa = new OperatiiAdresaImpl(context);
        operatiiAdresa.setOperatiiAdresaListener(this);

        setSpinnerJudetListener();

        listJudete = new ArrayList<HashMap<String, String>>();
        adapterJudete = new SimpleAdapter(context, listJudete, R.layout.rowlayoutjudete, new String[]{"numeJudet", "codJudet"}, new int[]{R.id.textNumeJudet,
                R.id.textCodJudet});

        fillJudeteClient(EnumJudete.getRegionCodes());

        List<ArticolDB> listServiciiTemp = new ArrayList<>();
        ArticolDB articolDB = new ArticolDB();
        articolDB.setNume("Selectati un serviciu");
        articolDB.setStoc("");
        listServiciiTemp.add(0, articolDB);

        adapterServiciiAC = new AdapterServiciiAC(context, listServiciiTemp);
        spinnerServiciiAC.setAdapter(adapterServiciiAC);

    }

    private void setSpinnerJudetListener() {
        spinnerJudet.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                codJudetInstalare = "";

                if (spinnerJudet.getSelectedItemPosition() > 0) {

                    HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
                    codJudetInstalare = tempMap.get("codJudet");

                    HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
                    params.put("codJudet", tempMap.get("codJudet"));
                    operatiiAdresa.getAdreseJudet(params, null);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillJudeteClient(String arrayJudete) {


        if (listJudete != null)
            listJudete.clear();

        spinnerJudet.setEnabled(true);

        HashMap<String, String> temp;
        int i;

        int nrJud = 0, posJudetSel = 0;
        for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {

            if (arrayJudete.contains(UtilsGeneral.codJudete[i])) {
                temp = new HashMap<>();
                temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
                temp.put("codJudet", UtilsGeneral.codJudete[i]);
                listJudete.add(temp);

                nrJud++;

                if (DateLivrare.getInstance().getAdresaInstalareAC() != null && DateLivrare.getInstance().getAdresaInstalareAC().getCodJudet().equals(UtilsGeneral.codJudete[i])) {
                    posJudetSel = nrJud;
                }
            }

        }

        spinnerJudet.setAdapter(adapterJudete);

        if (posJudetSel > 0) {
            spinnerJudet.setSelection(posJudetSel - 1);
            findViewById(R.id.layoutLabelAdresa).setVisibility(View.INVISIBLE);

            if (existaServicuComanda)
                spinnerJudet.setEnabled(false);
        }

    }


    private void afiseazaArticoleServicii() {

        ArticolDB articolDB = new ArticolDB();
        articolDB.setNume("Selectati un serviciu");
        articolDB.setStoc("");
        listArticoleInit.add(0, articolDB);

        adapterServiciiAC = new AdapterServiciiAC(context, listArticoleInit);
        spinnerServiciiAC.setAdapter(adapterServiciiAC);
    }

    private void afiseazaArticoleServiciiJudet() {

        if (codJudetInstalare != null && !codJudetInstalare.isEmpty()) {

            ArrayList<ArticolDB> listServiciiJudet = new ArrayList<>();

            for (ArticolDB articolServiciu : listArticoleInit)
                if (articolServiciu.getCategorie().equals(codJudetInstalare))
                    listServiciiJudet.add(articolServiciu);

            ArticolDB articolDB = new ArticolDB();
            articolDB.setNume("Selectati un serviciu");
            articolDB.setStoc("");
            listServiciiJudet.add(0, articolDB);

            adapterServiciiAC = new AdapterServiciiAC(context, listServiciiJudet);
            spinnerServiciiAC.setAdapter(adapterServiciiAC);

            if (listServiciiJudet.size() == 1)
                Toast.makeText(context, "Nu exista servicii de instalare disponibile pentru acest judet.", Toast.LENGTH_LONG).show();
        }


    }

    private void setUpLayout() {

        spinnerServiciiAC = (Spinner) findViewById(R.id.spinnerServiciiAC);

        if (!DateLivrare.getInstance().getTransport().equals("TCLI")) {
            afiseazaArticoleServicii();
        }

        btnAdaugaArticol = (Button) findViewById(R.id.btnOkArticol);
        addBtnAcceptaListener();
        btnRenuntaPaleti = (Button) findViewById(R.id.btnCancelPalet);
        addBtnRespingeListener();

    }

    private void setSpinnerPaletiListener() {
        spinnerServiciiAC.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 > 0)
                    servSelectat = (ArticolDB) arg0.getAdapter().getItem(arg2);
                else
                    servSelectat = null;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void addBtnAcceptaListener() {
        btnAdaugaArticol.setOnClickListener(v -> {
            if (isConditiiSalvareServiciu()) {
                setAdresaInstalareAC();
                listener.serviciuACSelected(servSelectat);
                dismiss();
            }
        });
    }

    private void setAdresaInstalareAC() {
        if (DateLivrare.getInstance().getTransport().equals("TCLI")) {
            BeanAdresaGenerica adresaInstalare = new BeanAdresaGenerica();
            adresaInstalare.setCodJudet(codJudetInstalare);
            adresaInstalare.setOras(textLocalitate.getText().toString().trim());
            adresaInstalare.setStrada(textStrada.getText().toString().trim());
            DateLivrare.getInstance().setAdresaInstalareAC(adresaInstalare);
        }

    }

    private boolean isConditiiSalvareServiciu() {

        if (DateLivrare.getInstance().getTransport().equals("TCLI")) {

            if (textLocalitate.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Completati localitatea.", Toast.LENGTH_LONG).show();
                return false;
            } else if (textStrada.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Completati strada.", Toast.LENGTH_LONG).show();
                return false;
            }

        }

        return listener != null && servSelectat != null;

    }

    private void addBtnRespingeListener() {
        btnRenuntaPaleti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setServiciiACListener(ServiciiACListener listener) {
        this.listener = listener;
    }

    private void populateListLocalitati(BeanAdreseJudet listAdrese) {

        textLocalitate.setText("");
        textStrada.setText("");

        String[] arrayLocalitati = listAdrese.getListStringLocalitati().toArray(new String[listAdrese.getListStringLocalitati().size()]);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

        textLocalitate.setThreshold(0);
        textLocalitate.setAdapter(adapterLoc);

        String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
        ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

        textStrada.setThreshold(0);
        textStrada.setAdapter(adapterStrazi);

        if (DateLivrare.getInstance().getAdresaInstalareAC() != null && DateLivrare.getInstance().getAdresaInstalareAC().getCodJudet().equals(codJudetInstalare)){
            textLocalitate.setText(DateLivrare.getInstance().getAdresaInstalareAC().getOras());
            textStrada.setText(DateLivrare.getInstance().getAdresaInstalareAC().getStrada());
        }

        afiseazaArticoleServiciiJudet();

    }

    @Override
    public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {

        switch (numeComanda) {
            case GET_ADRESE_JUDET:
                populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));
                break;
            default:
                break;
        }

    }
}
