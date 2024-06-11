/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.ArticolAprobareAdapter;
import my.logon.screen.adapters.ComandaAprobAdapter;
import my.logon.screen.beans.BeanArticoleAfisare;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.BeanConditii;
import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.beans.BeanConditiiHeader;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.ValoriComanda;
import my.logon.screen.dialogs.DenialListDialog;
import my.logon.screen.dialogs.GenericAlertDialog;
import my.logon.screen.dialogs.InfoPretArticolDialog;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumDialogConstraints;
import my.logon.screen.enums.EnumTipReducere;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.DenialListDialogListener;
import my.logon.screen.listeners.GenericDialogListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.SerializeConditiiComanda;
import my.logon.screen.model.UserInfo;
import my.logon.screen.patterns.ComandaAprobareComparator;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class AprobareComanda extends Activity implements ComenziDAOListener, DenialListDialogListener, GenericDialogListener {

    Button quitBtn, aprobaCmd, respingeCmd, trimiteCond, saveCondArt, slideButtonAprob, btnCommentAprob, btnCommAprob;
    ToggleButton btnConditii;

    String filiala = "", nume = "", cod = "";
    public static String unitLog = "";
    public static String numeDepart = "";
    public static String codDepart = "";
    public String commentsCondAprob = "", readComments = "";
    public static boolean cmdAngajament = false;
    private Dialog commentsDialog;

    private Spinner spinnerComenzi, spinnerTipReducere;
    public SimpleAdapter adapterComenzi, adapterCndAprob;
    private static ArrayList<HashMap<String, String>> arrayCndAprob = null;

    ArrayList<HashMap<String, String>> listMotive = null;

    private String selectedCmd = "-1";
    private String selectedCmdSAP = "-1", pretArtSel = "";
    private int tipOpCmd = -1;
    private int listViewSelPos = -1;
    private String cmdNr = null, disClient = "", procAgent = "";

    private TextView textNumeArt, textCodArt, textUmArt, textProcCalitAprob, textNrFacturiAprob, textMarjaCmd, textAdrLivrNoua, labelAdresa, textAdresaLivrare,
            textPondereArtB, textCastigBrut, textPondereB_30, textTipTransport;
    private TextView textCastigBrutTotal, textMarjaCmdTotal;
    private LinearLayout condTable;
    private LinearLayout conditiiArticolLayout;

    WrappingSlidingDrawer slidingDrawerAprob;

    private CheckBox checkEliminaTransp;
    private TextView textComandaBV90, textMonedaPretArt;
    ListView listViewArticoleComanda, listViewArticoleConditii;
    private EditText textCantArt, textPretArt, textCondCal;
    private int selectedPos = -1;
    private int selectedPosComanda = -1;

    public String stringMotiveRespingere = "", unitatePret = "", unitatePretBaza = "", tipAgentComanda = "";
    String tipAprob = "";
    Spinner denialOptions;
    String codRespingere = "";
    String numeArtSelContextMenu = "", codArtSelContextMenu = "", infoPretArticol = "";

    private Double globalPondere30Cmd = 0.0;
    private ComenziDAO operatiiComenzi;
    private List<BeanComandaCreata> listComenzi;
    private List<ArticolComanda> listArticoleComanda;
    private List<BeanConditiiArticole> conditiiComandaArticole;
    private BeanConditiiHeader conditiiComandaHeader;
    private String divizieAgent;
    private EnumTipReducere tipReducere;
    private BeanComandaCreata comandaCurenta;
    private TextView textTipReducere;
    private TextView textValMarjaT1, textProcMarjaT1,textValMarjaT1Total, textProcMarjaT1Total, textMetodaPlata;
    private TextView textFilialaClp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.aprobarecomandaheader);

        checkStaticVars();
        operatiiComenzi = ComenziDAO.getInstance(this);
        operatiiComenzi.setComenziDAOListener(this);

        this.aprobaCmd = (Button) findViewById(R.id.aprobaCmd);
        aprobaCmd.setVisibility(View.GONE);
        addListenerAprobaCmd();

        this.respingeCmd = (Button) findViewById(R.id.respingeCmd);
        addListenerRespingereCmd();
        respingeCmd.setVisibility(View.GONE);

        spinnerTipReducere = (Spinner) findViewById(R.id.spinnerTipReducere);
        populateSpinnerTipReducere();
        setListenerSpinnerReducere();

        btnConditii = (ToggleButton) findViewById(R.id.btnConditii);
        btnConditii.setVisibility(View.GONE);
        addListenerBtnConditii();

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Aprobare comanda");
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.condTable = (LinearLayout) findViewById(R.id.tabelaConditii);
        condTable.setVisibility(View.INVISIBLE);

        this.textMarjaCmd = (TextView) findViewById(R.id.textMarjaCmd);
        textMarjaCmd.setVisibility(View.INVISIBLE);

        this.textMarjaCmdTotal = (TextView) findViewById(R.id.textMarjaCmdTotal);
        textMarjaCmdTotal.setVisibility(View.INVISIBLE);

        this.btnCommentAprob = (Button) findViewById(R.id.btnComentariiAprob);
        addListenerBtnCommentAprob();
        this.conditiiArticolLayout = (LinearLayout) findViewById(R.id.tabelaDetaliiCmd);
        conditiiArticolLayout.setVisibility(View.GONE);

        this.textAdrLivrNoua = (TextView) findViewById(R.id.textAdresaNoua);
        textAdrLivrNoua.setText("");

        this.labelAdresa = (TextView) findViewById(R.id.labelAdresa);
        labelAdresa.setVisibility(View.INVISIBLE);

        this.saveCondArt = (Button) findViewById(R.id.saveCondArt);
        addListenerSaveCondArt();

        textAdresaLivrare = (TextView) findViewById(R.id.textAdresaLivrare);
        textAdresaLivrare.setVisibility(View.INVISIBLE);

        textPondereArtB = (TextView) findViewById(R.id.textPondereArtB);
        textPondereArtB.setVisibility(View.INVISIBLE);

        textCastigBrut = (TextView) findViewById(R.id.textCastigBrut);
        textCastigBrut.setVisibility(View.INVISIBLE);

        textCastigBrutTotal = (TextView) findViewById(R.id.textCastigBrutTotal);
        textCastigBrutTotal.setVisibility(View.INVISIBLE);

        textPondereB_30 = (TextView) findViewById(R.id.textPondereB_30);
        textPondereB_30.setVisibility(View.INVISIBLE);

        this.trimiteCond = (Button) findViewById(R.id.trimiteCond);
        addListenerTrimiteCond();
        trimiteCond.setVisibility(View.GONE);

        spinnerComenzi = (Spinner) findViewById(R.id.spinnerCmd);
        addListenerSpinnerCmd();

        spinnerComenzi.setVisibility(View.INVISIBLE);

        listViewArticoleComanda = (ListView) findViewById(R.id.listArtAprob);
        registerForContextMenu(listViewArticoleComanda);
        addListenerArtAprob();
        listViewArticoleComanda.setVisibility(View.INVISIBLE);

        listViewArticoleConditii = (ListView) findViewById(R.id.listArtCondAprob);
        arrayCndAprob = new ArrayList<HashMap<String, String>>();
        adapterCndAprob = new SimpleAdapter(this, arrayCndAprob, R.layout.rowlayoutartcond, new String[]{"nrCrt", "numeArtCond", "codArtCond", "cantArtCond",
                "umArtCond", "pretArtCond", "monedaArtCond"}, new int[]{R.id.textNrCrt, R.id.textNumeArtCond, R.id.textCodArtCond, R.id.textCantArtCond,
                R.id.textUmArtCond, R.id.textPretArtCond, R.id.textMonArtCond});

        listViewArticoleConditii.setAdapter(adapterCndAprob);

        textNumeArt = (TextView) findViewById(R.id.textNumeArt);
        textCodArt = (TextView) findViewById(R.id.textCodArt);

        slidingDrawerAprob = (WrappingSlidingDrawer) findViewById(R.id.slidingDrawerAprob);
        slidingDrawerAprob.setVisibility(View.GONE);
        addDrowerAprobEvents();
        this.slideButtonAprob = (Button) findViewById(R.id.slideButtonAprob);

        btnCommAprob = (Button) findViewById(R.id.btnCommAprob);
        addListenerBtnCommAprob();

        this.textCantArt = (EditText) findViewById(R.id.txtCantArt);
        this.textUmArt = (TextView) findViewById(R.id.txtUmArt);
        this.textPretArt = (EditText) findViewById(R.id.txtPretArt);
        this.textMonedaPretArt = (TextView) findViewById(R.id.txtMonedaPretArt);
        this.textCondCal = (EditText) findViewById(R.id.txtCondCal);
        this.textProcCalitAprob = (TextView) findViewById(R.id.textProcCalitAprob);
        this.textNrFacturiAprob = (TextView) findViewById(R.id.textNrFactAprob);
        textTipTransport = (TextView) findViewById(R.id.textTipTransport);

        textTipReducere = (TextView) findViewById(R.id.textTipReducere);

        textValMarjaT1 = (TextView) findViewById(R.id.textValMarjaT1);
        textProcMarjaT1 = (TextView) findViewById(R.id.textProcMarjaT1);

        textValMarjaT1Total = (TextView) findViewById(R.id.textValMarjaT1Total);
        textProcMarjaT1Total = (TextView) findViewById(R.id.textProcMarjaT1Total);

        textMetodaPlata = (TextView) findViewById(R.id.textMetodaPlata);

        textFilialaClp = (TextView) findViewById(R.id.textFilialaClp);


        textComandaBV90 = (TextView) findViewById(R.id.textComandaBV90);
        textComandaBV90.setVisibility(View.INVISIBLE);

        checkEliminaTransp = (CheckBox) findViewById(R.id.checkEliminaTransp);
        checkEliminaTransp.setVisibility(View.INVISIBLE);

        getListComenzi();

    }

    private void setCheckEliminaTranspVisibility() {
        if (UtilsUser.isANYDV()) {
            checkEliminaTransp.setVisibility(View.VISIBLE);
            checkEliminaTransp.setChecked(false);
        } else {
            checkEliminaTransp.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        checkStaticVars();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                UserInfo.getInstance().setParentScreen("");
                Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

                startActivity(nextScreen);

                finish();
                return true;

        }
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == 0) {
            new InfoPretArticolDialog(this, infoPretArticol, unitatePretBaza).showDialog();
        }

        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listArtAprob) {

            ArticolComanda articolSelectat = listArticoleComanda.get(listViewSelPos);
            numeArtSelContextMenu = articolSelectat.getNumeArticol();
            codArtSelContextMenu = articolSelectat.getCodArticol();
            infoPretArticol = articolSelectat.getInfoArticol();

            menu.setHeaderTitle(numeArtSelContextMenu);
            menu.add(Menu.NONE, 0, 0, "Detalii pret");

        }

    }

    private void populateSpinnerTipReducere() {

        List<String> tipuriReducere = new ArrayList<String>();

        tipuriReducere.add("Impuneti un tip de reducere");

        for (EnumTipReducere tipReducere : EnumTipReducere.values()) {
            tipuriReducere.add(tipReducere.getNumeReducere());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipuriReducere);
        spinnerTipReducere.setAdapter(spinnerArrayAdapter);

    }

    private void setListenerSpinnerReducere() {
        spinnerTipReducere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    tipReducere = EnumTipReducere.getEnumReducere((String) parent.getAdapter().getItem(position));
                } else
                    tipReducere = null;

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                return;
            }
        });
    }

    public void addListenerBtnConditii() {
        btnConditii.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (btnConditii.isChecked()) {

                    if (listComenzi.size() > 0) {
                        conditiiArticolLayout.setVisibility(View.INVISIBLE);
                        textPondereArtB.setVisibility(View.INVISIBLE);
                        textCastigBrut.setVisibility(View.INVISIBLE);
                        textCastigBrutTotal.setVisibility(View.INVISIBLE);
                        textPondereB_30.setVisibility(View.INVISIBLE);
                        textTipTransport.setVisibility(View.INVISIBLE);
                        textMetodaPlata.setVisibility(View.INVISIBLE);

                        textFilialaClp.setVisibility(View.INVISIBLE);


                        textComandaBV90.setVisibility(View.INVISIBLE);
                        checkEliminaTransp.setVisibility(View.INVISIBLE);
                        textTipReducere.setVisibility(View.INVISIBLE);
                        spinnerComenzi.setEnabled(false);
                        trimiteCond.setVisibility(View.VISIBLE);
                        condTable.setVisibility(View.VISIBLE);
                        textPretArt.setText("");
                        textMonedaPretArt.setText("");
                        textUmArt.setText("");
                        textCantArt.setText("");
                        textNumeArt.setText("");
                        textCodArt.setText("");

                        textCondCal.setText(String.format("%.02f", globalPondere30Cmd));

                        commentsCondAprob = "";
                        aprobaCmd.setVisibility(View.GONE);
                        respingeCmd.setVisibility(View.GONE);
                        slidingDrawerAprob.setVisibility(View.INVISIBLE);

                        textValMarjaT1.setVisibility(View.INVISIBLE);
                        textProcMarjaT1.setVisibility(View.INVISIBLE);

                        textMarjaCmdTotal.setVisibility(View.INVISIBLE);

                        textValMarjaT1Total.setVisibility(View.INVISIBLE);
                        textProcMarjaT1Total.setVisibility(View.INVISIBLE);

                        if (!textAdrLivrNoua.getText().equals("")) {
                            textAdrLivrNoua.setVisibility(View.INVISIBLE);
                            labelAdresa.setVisibility(View.INVISIBLE);
                        }

                        setDep01InfoVisibility(false);

                    }

                } else {

                    selectedPos = -1;
                    spinnerComenzi.setEnabled(true);
                    trimiteCond.setVisibility(View.GONE);
                    condTable.setVisibility(View.GONE);
                    getListComenzi();

                }
            }
        });

    }

    public void addListenerTrimiteCond() {
        trimiteCond.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendCondToDB();

            }
        });

    }

    public void sendCondToDB() {

        String condCal = "";
        int nrFact = 0;

        if (textCondCal.getText().toString().trim().equals("")) {
            condCal = "0";
        } else {
            condCal = textCondCal.getText().toString().trim();
        }

        if (tipReducere == null)
            nrFact = -1;
        else {
            if (tipReducere == EnumTipReducere.FACT_RED_PRET)
                nrFact = 1;
            if (tipReducere == EnumTipReducere.FACT_2)
                nrFact = 2;
            if (tipReducere == EnumTipReducere.FACT_RED_SEP)
                nrFact = 3;
        }

        conditiiComandaHeader.setId(Integer.valueOf(selectedCmd));
        conditiiComandaHeader.setConditiiCalit(Double.valueOf(condCal.replace(',', '.')));
        conditiiComandaHeader.setNrFact(nrFact);
        conditiiComandaHeader.setObservatii(commentsCondAprob);

        if (noConditions()) {
            Toast.makeText(getApplicationContext(), "Adaugati cel putin o conditie!", Toast.LENGTH_SHORT).show();
        } else {
            performSaveCond();
        }

    }

    boolean noConditions() {
        return conditiiComandaArticole != null && conditiiComandaArticole.size() == 0 && textCondCal.getText().toString().trim().equals("")
                && commentsCondAprob.trim().equals("");
    }

    private void performSaveCond() {
        try {

            SerializeConditiiComanda serializer = new SerializeConditiiComanda();

            String headerConditii = serializer.serializeHeaderConditii(conditiiComandaHeader);
            String articoleConditii = serializer.serializeArticoleConditii(conditiiComandaArticole);
            String conditii = serializer.serializeConditiiObject(headerConditii, articoleConditii);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("conditiiComanda", conditii);
            operatiiComenzi.salveazaConditiiComandaSer(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCondStatus(String saveResponse) {
        if (saveResponse.equals("0")) {
            try {

                spinnerComenzi.setEnabled(true);
                trimiteCond.setVisibility(View.GONE);
                condTable.setVisibility(View.GONE);
                conditiiArticolLayout.setVisibility(View.GONE);
                aprobaCmd.setVisibility(View.VISIBLE);
                respingeCmd.setVisibility(View.VISIBLE);
                slidingDrawerAprob.setVisibility(View.VISIBLE);
                btnConditii.setChecked(false);

                if (comandaCurenta.getMoneda().equals("RON")) {
                    tipOpCmd = 2;
                    opereazaComanda();
                } else if (comandaCurenta.getMoneda().equals("BGN")) {
                    Toast.makeText(getApplicationContext(), "Operatie reusita.", Toast.LENGTH_SHORT).show();
                    getListComenzi();
                }

                tipOpCmd = 2;
                opereazaComanda();

                commentsCondAprob = "";

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Conditiile NU au fost salvate!", Toast.LENGTH_LONG).show();
        }
    }

    public void addListenerSaveCondArt() {
        saveCondArt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (selectedPos != -1) {
                    if (textPretArt.getText().toString().trim().length() != 0 && textCantArt.getText().toString().trim().length() != 0) {

                        addCondArticol();
                        selectedPos = -1;
                    } else {
                        Toast.makeText(getApplicationContext(), "Completati ambele campuri!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Selectati o pozitie!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addCondArticol() {

        double newProcRed = 0, newProcRedAprob = 0, pretVanzare = 0, pretVanzareInit = 0;

        pretVanzareInit = (Double.parseDouble(pretArtSel)) / (1 - Double.parseDouble(procAgent) / 100);
        pretVanzare = pretVanzareInit / (1 - Double.parseDouble(disClient.substring(0, disClient.length() - 1)) / 100);
        newProcRedAprob = (1 - Double.parseDouble(textPretArt.getText().toString()) / pretVanzare) * 100;
        newProcRed = (1 - Double.parseDouble(textPretArt.getText().toString()) / pretVanzareInit) * 100;

        listArticoleComanda.get(selectedPos).setCantitate(Double.parseDouble(textCantArt.getText().toString()));
        listArticoleComanda.get(selectedPos).setPretUnit(Double.parseDouble(textPretArt.getText().toString()));
        listArticoleComanda.get(selectedPos).setProcent(newProcRed);
        listArticoleComanda.get(selectedPos).setProcAprob(newProcRedAprob);
        listArticoleComanda.get(selectedPos).setAddCond("*");

        ArticolAprobareAdapter adapterArt = new ArticolAprobareAdapter(this, listArticoleComanda);
        listViewArticoleComanda.setAdapter(adapterArt);

        BeanConditiiArticole articolConditie = new BeanConditiiArticole();
        articolConditie.setCod(listArticoleComanda.get(selectedPos).getCodArticol());
        articolConditie.setNume(listArticoleComanda.get(selectedPos).getNumeArticol());
        articolConditie.setCantitate(listArticoleComanda.get(selectedPos).getCantitate());
        articolConditie.setUm(listArticoleComanda.get(selectedPos).getUm());
        articolConditie.setValoare(listArticoleComanda.get(selectedPos).getPretUnit());
        articolConditie.setMultiplu(listArticoleComanda.get(selectedPos).getMultiplu());

        addArticolConditie(articolConditie);

        textNumeArt.setText(" ");
        textCodArt.setText(" ");
        textCantArt.setText(" ");
        textUmArt.setText(" ");
        textPretArt.setText(" ");
        textMonedaPretArt.setText(" ");

        pretArtSel = " ";

    }

    private void addArticolConditie(BeanConditiiArticole articolConditie) {
        if (conditiiComandaArticole == null)
            conditiiComandaArticole = new ArrayList<BeanConditiiArticole>();

        int existingArt = -1;

        for (int i = 0; i < conditiiComandaArticole.size(); i++) {
            if (conditiiComandaArticole.get(i).equals(articolConditie)) {
                existingArt = i;
                break;
            }
        }

        if (existingArt != -1)
            conditiiComandaArticole.remove(existingArt);

        conditiiComandaArticole.add(articolConditie);

    }

    public void addListenerAprobaCmd() {
        aprobaCmd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!isComandaSubCmp()) {
                    showConfirmationAlert();
                } else
                    showComandaCmpAlertDialog();

            }
        });

    }

    private void showComandaCmpAlertDialog() {

        String title = "Atentie!";
        String alertMessage = "Comanda contine preturi sub cmp. Continuati?";

        GenericAlertDialog alertDialog = new GenericAlertDialog(AprobareComanda.this, title, alertMessage, EnumDialogConstraints.COMANDA_SUB_CMP);
        alertDialog.setGenericDialogListener(this);
        alertDialog.showAlertDialog();

    }

    private void showConfirmationAlert() {
        String title = "Confirmare";
        String alertMessage = "Aprobati comanda?";

        if (comandaCurenta.isComandaACZC()) {
            alertMessage += "\n\n";
            alertMessage += "Aceasta este o solicitare de produse la comanda AC/ZC.\n";
            alertMessage += "Trebuie sa avem proces verbal de angajament semnat de client.";
        }

        GenericAlertDialog alertDialog = new GenericAlertDialog(AprobareComanda.this, title, alertMessage, EnumDialogConstraints.APROBARE_COMANDA);
        alertDialog.setGenericDialogListener(this);
        alertDialog.showAlertDialog();
    }

    public void addListenerRespingereCmd() {
        respingeCmd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showDenialAlert();

            }
        });

    }

    public void showDenialAlert() {

        DenialListDialog denialDialog = new DenialListDialog(this);
        denialDialog.setDenialListDialogListener(this);
        denialDialog.showDialog();

    }

    public void opereazaComanda() {

        try {

            NumberFormat nf3 = new DecimalFormat("00000000");
            String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

            String localDivizieAgent = divizieAgent;
            if (isConditie11())
                localDivizieAgent = "11";

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("nrCmd", selectedCmd);
            params.put("nrCmdSAP", selectedCmdSAP);
            params.put("tipOp", String.valueOf(tipOpCmd));
            params.put("codUser", fullCode);
            params.put("codRespingere", codRespingere);
            params.put("divizieAgent", localDivizieAgent);
            params.put("elimTransp", getStareElimTransport());
            params.put("filiala", listComenzi.get(selectedPosComanda).getFiliala());
            params.put("codStare", comandaCurenta.getCodStare());
            params.put("isComandaACZC", comandaCurenta.isComandaACZC() + "");

            operatiiComenzi.opereazaComanda(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConditie11() {
        return comandaCurenta.getCanalDistrib().equals("20") && comandaCurenta.getAprobariNecesare().trim().length() > 0 && !UtilsUser.isSD();
    }

    private String getStareElimTransport() {
        String stareTransp = " ";
        if (UtilsUser.isANYDV()) {
            if (checkEliminaTransp.isChecked())
                stareTransp = "X";
            else
                stareTransp = " ";
        }

        return stareTransp;
    }

    private void getListComenzi() {

        try {

            HashMap<String, String> params = new HashMap<String, String>();

            String tipUsr = "";
            if (UserInfo.getInstance().getTipAcces().equals("10"))
                tipUsr = "SD";
            if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12"))
                tipUsr = "DV";

            if (UserInfo.getInstance().getTipAcces().equals("18"))
                tipUsr = "SM";

            String depart = UserInfo.getInstance().getCodDepart();

            if (UtilsUser.isDV_WOOD() || UserInfo.getInstance().getCod().equals("00010281") || UserInfo.getInstance().getCod().equals("00018768")
                    || UserInfo.getInstance().getCod().equals("00086469"))
                depart = "11";

            params.put("filiala", UserInfo.getInstance().getUnitLog());
            params.put("codUser", UserInfo.getInstance().getCod());
            params.put("tipCmd", "2"); // pentru aprobare
            params.put("tipUser", tipUsr); // selectie comenzi
            params.put("depart", depart);

            operatiiComenzi.getListComenzi(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void performArtCmd() {

        HashMap<String, String> params = new HashMap<String, String>();

        String tipCmd = "-1";
        String codDepart;

        // CMATEI
        if (UserInfo.getInstance().getCod().equals("00010281"))
            codDepart = "11";
        else
            codDepart = UserInfo.getInstance().getCodDepart();

        if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")) // admin
            // sau
            // DV sau DD
            tipCmd = "3";
        if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
                || UserInfo.getInstance().getTipAcces().equals("18")) // agent,
            // sd,
            // sm
            tipCmd = "2";

        params.put("nrCmd", selectedCmd);
        params.put("afisCond", tipCmd);
        params.put("tipUser", UserInfo.getInstance().getTipUser());
        params.put("departament", codDepart);

        operatiiComenzi.getArticoleComandaJSON(params);

    }

    private boolean isComandaSubCmp() {

        for (int i = 0; i < listArticoleComanda.size(); i++) {

            if (listArticoleComanda.get(i).getDepozit().substring(2, 3).equals("V") && !listArticoleComanda.get(i).getDepozit().equals("MAV1")
                    && listArticoleComanda.get(i).getPermitSubCmp().equals("0")) {
                if (listArticoleComanda.get(i).getPretUnit() < listArticoleComanda.get(i).getCmp())
                    return true;
            }

        }

        return false;
    }

    private void populateArtCmdList(BeanArticoleAfisare articoleComanda) {

        DateLivrareAfisare dateLivrare = articoleComanda.getDateLivrare();
        listArticoleComanda = articoleComanda.getListArticole();
        BeanConditii conditiiComanda = articoleComanda.getConditii();

        conditiiComandaHeader = conditiiComanda.getHeader();
        conditiiComandaArticole = conditiiComanda.getArticole();

        textProcCalitAprob.setText(String.valueOf(conditiiComandaHeader.getConditiiCalit()));
        textNrFacturiAprob.setText(String.valueOf(conditiiComandaHeader.getNrFact()));
        readComments = conditiiComandaHeader.getObservatii();

        showArticoleConditiiSlider();

        ArticolAprobareAdapter articolAdapter = new ArticolAprobareAdapter(this, listArticoleComanda);
        listViewArticoleComanda.setVisibility(View.VISIBLE);
        listViewArticoleComanda.setAdapter(articolAdapter);

        ValoriComanda valoriComanda = articolAdapter.getValoriComanda(listArticoleComanda);

        textAdresaLivrare.setVisibility(View.VISIBLE);
        textTipTransport.setVisibility(View.VISIBLE);
        textMetodaPlata.setVisibility(View.VISIBLE);
        textTipReducere.setVisibility(View.VISIBLE);
        textMarjaCmd.setVisibility(View.VISIBLE);
        textMarjaCmdTotal.setVisibility(View.VISIBLE);
        setCheckEliminaTranspVisibility();

        if (!UserInfo.getInstance().getTipAcces().equals("18")) {
            textPondereArtB.setVisibility(View.VISIBLE);
            textPondereB_30.setVisibility(View.VISIBLE);
        }

        textCastigBrut.setVisibility(View.VISIBLE);
        textCastigBrutTotal.setVisibility(View.VISIBLE);
        if (isSDorSM()) {
            if (tipAgentComanda.equals("KA")) {
                textPondereArtB.setVisibility(View.INVISIBLE);
                textPondereB_30.setVisibility(View.INVISIBLE);
            }

            textCastigBrut.setVisibility(View.INVISIBLE);
            textCastigBrutTotal.setVisibility(View.INVISIBLE);
        }

        StringBuilder strAdresaLivrare = new StringBuilder();
        strAdresaLivrare.append(dateLivrare.getNumeJudet());
        strAdresaLivrare.append(", ");
        strAdresaLivrare.append(dateLivrare.getOras());

        if (dateLivrare.getStrada().trim().length() > 0) {
            strAdresaLivrare.append(", ");
            strAdresaLivrare.append(dateLivrare.getStrada());
        } else if (dateLivrare.getDateLivrare().trim().length() > 0) {
            strAdresaLivrare.append(", ");
            strAdresaLivrare.append(dateLivrare.getDateLivrare());
        }

        textAdresaLivrare.setText("Adresa livrare: " + strAdresaLivrare.toString());
        textTipTransport.setText("Transport: " + UtilsGeneral.getDescTipTransport(dateLivrare.getTransport()));
        textMetodaPlata.setText("Plata: " + UtilsGeneral.getDescTipPlata(dateLivrare.getTipPlata(), dateLivrare.getTermenPlata()));

        //corectie pt. adresa de livrare in city_d
        if (labelAdresa.getVisibility() == View.VISIBLE && labelAdresa.getText().equals("Adresa de livrare noua") && !dateLivrare.getOras().trim().isEmpty()){
            textAdrLivrNoua.setText(strAdresaLivrare.toString());
        }


        textFilialaClp.setVisibility(View.INVISIBLE);
        textFilialaClp.setText("");

        if (dateLivrare.getCodFilialaCLP() != null && dateLivrare.getCodFilialaCLP().length() == 4) {
            textFilialaClp.setText("Filiala clp: " + dateLivrare.getCodFilialaCLP());
            textFilialaClp.setVisibility(View.VISIBLE);
        }

        String strTipReducere = UtilsGeneral.getTipReducere(dateLivrare.getFactRed()).isEmpty() ? " " : "Tip reducere: "
                + UtilsGeneral.getTipReducere(dateLivrare.getFactRed());

        textTipReducere.setText(strTipReducere);

        textMarjaCmd.setText("Marja comanda: " + String.format("%.02f", dateLivrare.getProcMarjaBruta()).toString() + "%");
        textMarjaCmdTotal.setText("Marja comanda total: " + String.format("%.02f", dateLivrare.getProcMarjaBrutaTot()).toString() + "%");

        if (textPondereArtB.getVisibility() == View.VISIBLE) {
            textPondereArtB.setText("Pondere art. B comanda: " + String.format("%.02f", (valoriComanda.getPondereB() / valoriComanda.getTotal()) * 100) + "%");
            globalPondere30Cmd = valoriComanda.getPondereB() / valoriComanda.getTotal() * 100;
        }

        if (textCastigBrut.getVisibility() == View.VISIBLE) {
            textCastigBrut.setText("Marja bruta comanda" + ": " + String.format("%.02f", dateLivrare.getMarjaBruta()) + " " + valoriComanda.getMoneda());
            textCastigBrutTotal.setText("Marja bruta comanda total" + ": " + String.format("%.02f", dateLivrare.getMarjaBrutaTot()) + " " + valoriComanda.getMoneda());
        }

        String unitLogAlt = listArticoleComanda.get(0).getUnitLogAlt();

        if (unitLogAlt.equals("BV90") || unitLogAlt.equals("BV92"))
            textComandaBV90.setVisibility(View.VISIBLE);
        else
            textComandaBV90.setVisibility(View.INVISIBLE);

        if (dateLivrare.getMarjaT1() > 0) {

            textValMarjaT1.setVisibility(View.VISIBLE);
            textProcMarjaT1.setVisibility(View.VISIBLE);

            textValMarjaT1Total.setVisibility(View.VISIBLE);
            textProcMarjaT1Total.setVisibility(View.VISIBLE);

            textValMarjaT1.setText("Marja T1 comanda: " + String.format("%.02f", dateLivrare.getMarjaT1()) + " RON");
            textProcMarjaT1.setText("Marja T1 : " + String.format("%.02f", (dateLivrare.getProcentT1() * 100)) + "%");

            textValMarjaT1Total.setText("Marja T1 comanda total: " + String.format("%.02f", dateLivrare.getMarjaT1Tot()) + " RON");
            textProcMarjaT1Total.setText("Marja T1 total: " + String.format("%.02f", (dateLivrare.getProcentT1Tot() * 100)) + "%");


        } else {
            textValMarjaT1.setVisibility(View.INVISIBLE);
            textProcMarjaT1.setVisibility(View.INVISIBLE);

            textValMarjaT1Total.setVisibility(View.INVISIBLE);
            textProcMarjaT1Total.setVisibility(View.INVISIBLE);

        }

        if (UserInfo.getInstance().getCodDepart().equals("01") && UtilsUser.isDV()) {
            textPondereArtB.setVisibility(View.GONE);
            textPondereB_30.setVisibility(View.GONE);

            ((TableLayout) findViewById(R.id.tablePalCant)).setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.textMarjaPalMoneda)).setText("Marja bruta (" + listArticoleComanda.get(0).getMoneda() + ")");

            ((TextView) findViewById(R.id.textMCantCmd)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textMCantCmd)).setText("Metri cant / foaie comanda: " + String.valueOf(dateLivrare.getmCantCmd()) + " m");

            ((TextView) findViewById(R.id.textMCant30)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textMCant30)).setText("Metri cant / foaie 30 zile: " + String.valueOf(dateLivrare.getmCant30()) + " m");

            ((TextView) findViewById(R.id.textMarjaPalVal)).setText(String.valueOf(dateLivrare.getMarjaBrutaPalVal()));
            ((TextView) findViewById(R.id.textMarjaCantVal)).setText(String.valueOf(dateLivrare.getMarjaBrutaCantVal()));

            ((TextView) findViewById(R.id.textMarjaPalProc)).setText(String.valueOf(dateLivrare.getMarjaBrutaPalProc()));
            ((TextView) findViewById(R.id.textMarjaCantProc)).setText(String.valueOf(dateLivrare.getMarjaBrutaCantProc()));

            ((TextView) findViewById(R.id.textMarjaTotalVal)).setText(String.format("%.02f",
                    dateLivrare.getMarjaBrutaPalVal() + dateLivrare.getMarjaBrutaCantVal()));
            ((TextView) findViewById(R.id.textMarjaTotalProc)).setText(String.format("%.02f",
                    dateLivrare.getMarjaBrutaPalProc() + dateLivrare.getMarjaBrutaCantProc()));

        }

        setupContextLayout(comandaCurenta);

        if (aprobaCmd.getVisibility() == View.VISIBLE) {
            if (UtilsComenzi.isComandaExpirata(listArticoleComanda)) {
                aprobaCmd.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Aceasta comanda contine articole cu pretul de vanzare expirat.", Toast.LENGTH_LONG).show();
            } else {
                aprobaCmd.setVisibility(View.VISIBLE);
            }
        }

    }

    private void setDep01InfoVisibility(boolean isVisible) {
        if (UserInfo.getInstance().getCodDepart().equals("01") && UtilsUser.isDV()) {

            if (isVisible) {
                ((TableLayout) findViewById(R.id.tablePalCant)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMCantCmd)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMCant30)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.textMarjaPalVal)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMarjaCantVal)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.textMarjaPalProc)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMarjaCantProc)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.textMarjaTotalVal)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMarjaTotalProc)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.textMarjaPalMoneda)).setVisibility(View.VISIBLE);

            } else {
                ((TableLayout) findViewById(R.id.tablePalCant)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMCantCmd)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMCant30)).setVisibility(View.INVISIBLE);

                ((TextView) findViewById(R.id.textMarjaPalVal)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMarjaCantVal)).setVisibility(View.INVISIBLE);

                ((TextView) findViewById(R.id.textMarjaPalProc)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMarjaCantProc)).setVisibility(View.INVISIBLE);

                ((TextView) findViewById(R.id.textMarjaTotalVal)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMarjaTotalProc)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.textMarjaPalMoneda)).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showArticoleConditiiSlider() {

        NumberFormat nf2 = NumberFormat.getInstance();
        nf2.setMaximumFractionDigits(2);
        nf2.setMinimumFractionDigits(2);
        nf2.setGroupingUsed(false);

        textProcCalitAprob.setText(nf2.format(conditiiComandaHeader.getConditiiCalit()));

        String varTipRed = "";

        if (conditiiComandaHeader.getNrFact() == 1) {
            varTipRed = "1 factura (red. in pret)";
        }
        if (conditiiComandaHeader.getNrFact() == 2) {
            varTipRed = "2 facturi";
        }
        if (conditiiComandaHeader.getNrFact() == 3) {
            varTipRed = "1 factura (red. separat)";
        }

        textNrFacturiAprob.setText(varTipRed);
        readComments = conditiiComandaHeader.getObservatii();
        slidingDrawerAprob.setVisibility(View.VISIBLE);

        if (UserInfo.getInstance().getTipAcces().equals("10") && !tipAprob.contains("SD"))
            slidingDrawerAprob.setVisibility(View.INVISIBLE);

        arrayCndAprob.clear();
        listViewArticoleConditii.setAdapter(adapterCndAprob);

        if (conditiiComandaArticole != null) {
            HashMap<String, String> temp;

            for (int i = 0; i < conditiiComandaArticole.size(); i++) {

                temp = new HashMap<String, String>();
                temp.put("nrCrt", String.valueOf(i + 1) + ".");
                temp.put("numeArtCond", conditiiComandaArticole.get(i).getNume());
                temp.put("codArtCond", conditiiComandaArticole.get(i).getCod());
                temp.put("cantArtCond", String.valueOf(conditiiComandaArticole.get(i).getCantitate()));
                temp.put("umArtCond", conditiiComandaArticole.get(i).getUm());
                temp.put("pretArtCond", String.valueOf(conditiiComandaArticole.get(i).getValoare()));
                temp.put("monedaArtCond", "RON");

                arrayCndAprob.add(temp);

            }

            listViewArticoleConditii.setAdapter(adapterCndAprob);
        }

    }

    private boolean isSDorSM() {
        return UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("18");
    }

    private void populateCmdList(List<BeanComandaCreata> cmdList) {

        this.listComenzi = cmdList;

        Collections.sort(cmdList, new ComandaAprobareComparator());

        ComandaAprobAdapter adapter = new ComandaAprobAdapter(cmdList, this);
        spinnerComenzi.setAdapter(adapter);

        if (cmdList.size() > 0) {

            if (!UserInfo.getInstance().getTipAcces().equals("18")) {
                btnConditii.setVisibility(View.VISIBLE);
            }

            textAdrLivrNoua.setVisibility(View.VISIBLE);
            labelAdresa.setVisibility(View.VISIBLE);
            spinnerComenzi.setVisibility(View.VISIBLE);
            setDep01InfoVisibility(true);
        } else {

            Toast.makeText(getApplicationContext(), "Nu exista comenzi.", Toast.LENGTH_SHORT).show();

            textAdrLivrNoua.setVisibility(View.INVISIBLE);
            labelAdresa.setVisibility(View.INVISIBLE);

            slidingDrawerAprob.setVisibility(View.GONE);
            textAdresaLivrare.setText("");
            textTipTransport.setText("");
            textMetodaPlata.setText("");
            textTipReducere.setText("");
            textMarjaCmd.setText("");
            textMarjaCmdTotal.setText("");
            textCastigBrut.setText("");
            textCastigBrutTotal.setText("");
            textPondereArtB.setText("");
            textPondereB_30.setText("");
            textComandaBV90.setVisibility(View.INVISIBLE);
            checkEliminaTransp.setVisibility(View.INVISIBLE);

            aprobaCmd.setVisibility(View.GONE);
            respingeCmd.setVisibility(View.GONE);
            btnConditii.setVisibility(View.GONE);
            spinnerComenzi.setVisibility(View.INVISIBLE);
            listViewArticoleComanda.setVisibility(View.INVISIBLE);
            textValMarjaT1.setVisibility(View.INVISIBLE);
            textProcMarjaT1.setVisibility(View.INVISIBLE);

            textValMarjaT1Total.setVisibility(View.INVISIBLE);
            textProcMarjaT1Total.setVisibility(View.INVISIBLE);
        }

    }

    public void addListenerArtAprob() {

        listViewArticoleComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                if (btnConditii.isChecked()) {

                    ArticolComanda articolSelectat = listArticoleComanda.get(position);

                    if ((articolSelectat.getPonderare() == 0 || articolSelectat.getPonderare() == 2) && (!tipAgentComanda.equals("AV"))) {
                        Toast.makeText(AprobareComanda.this, "Pe acest articol nu se pot pune conditii.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    selectedPos = position;

                    pretArtSel = String.valueOf(articolSelectat.getPretUnit());
                    procAgent = String.valueOf(articolSelectat.getProcent());

                    textNumeArt.setText(articolSelectat.getNumeArticol());
                    textCodArt.setText(articolSelectat.getCodArticol());
                    textCantArt.setText(String.valueOf(articolSelectat.getCantitate()));
                    textUmArt.setText(articolSelectat.getUm());
                    textPretArt.setText(String.valueOf(articolSelectat.getPretUnit()));
                    textMonedaPretArt.setText(articolSelectat.getMoneda());

                    disClient = String.valueOf(articolSelectat.getDiscClient());

                    infoPretArticol = articolSelectat.getInfoArticol();

                    textCantArt.requestFocus();
                    textCantArt.setSelection(textCantArt.getText().length());

                }

            }
        });

        listViewArticoleComanda.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ArticolComanda articolSelectat = listArticoleComanda.get(position);
                unitatePret = articolSelectat.getMultiplu() + " " + articolSelectat.getUm();

                unitatePretBaza = articolSelectat.getMultiplu() + " " + articolSelectat.getUmb();

                listViewSelPos = position;
                return false;

            }
        });

    }

    public void addDrowerAprobEvents() {
        slidingDrawerAprob.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {

                aprobaCmd.setVisibility(View.INVISIBLE);
                respingeCmd.setVisibility(View.INVISIBLE);
                btnConditii.setVisibility(View.INVISIBLE);

                if (!textAdrLivrNoua.getText().equals("")) {
                    textAdrLivrNoua.setVisibility(View.INVISIBLE);
                    labelAdresa.setVisibility(View.INVISIBLE);
                }

                spinnerComenzi.setEnabled(false);

                if (conditiiArticolLayout.getVisibility() == View.VISIBLE) {
                    conditiiArticolLayout.setVisibility(View.GONE);
                }

                textPondereArtB.setVisibility(View.INVISIBLE);
                textPondereB_30.setVisibility(View.INVISIBLE);
                textCastigBrut.setVisibility(View.INVISIBLE);
                textCastigBrutTotal.setVisibility(View.INVISIBLE);
                textTipTransport.setVisibility(View.INVISIBLE);
                textMetodaPlata.setVisibility(View.INVISIBLE);
                checkEliminaTransp.setVisibility(View.INVISIBLE);
                textTipReducere.setVisibility(View.INVISIBLE);

                textFilialaClp.setVisibility(View.INVISIBLE);

                slideButtonAprob.setBackgroundResource(R.drawable.slideright32);

            }
        });

        slidingDrawerAprob.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {

                if (!UserInfo.getInstance().getTipAcces().equals("10")) // userul
                // nu
                // este
                // SD
                {
                    conditiiArticolLayout.setVisibility(View.VISIBLE);
                }

                if (!textAdrLivrNoua.getText().equals("")) {
                    textAdrLivrNoua.setVisibility(View.VISIBLE);
                    labelAdresa.setVisibility(View.VISIBLE);
                }

                spinnerComenzi.setEnabled(true);

                aprobaCmd.setVisibility(View.VISIBLE);
                respingeCmd.setVisibility(View.VISIBLE);
                btnConditii.setVisibility(View.VISIBLE);

                textPondereArtB.setVisibility(View.VISIBLE);
                textPondereB_30.setVisibility(View.VISIBLE);
                textCastigBrut.setVisibility(View.VISIBLE);
                textCastigBrutTotal.setVisibility(View.VISIBLE);
                textTipTransport.setVisibility(View.VISIBLE);
                textMetodaPlata.setVisibility(View.VISIBLE);
                textTipReducere.setVisibility(View.VISIBLE);

                textFilialaClp.setVisibility(View.VISIBLE);
                slideButtonAprob.setBackgroundResource(R.drawable.slideleft32);
                setCheckEliminaTranspVisibility();

            }
        });

    }

    public void addListenerBtnCommentAprob() {
        btnCommentAprob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                displayCommentsDialogBox();

            }
        });

    }

    public void displayCommentsDialogBox() {

        commentsDialog = new Dialog(AprobareComanda.this);
        commentsDialog.setContentView(R.layout.aprobcommentsdialogbox);
        commentsDialog.setTitle("Comentarii");
        commentsDialog.setCancelable(false);

        final EditText textCommentsAprob = (EditText) commentsDialog.findViewById(R.id.editTextComments);
        textCommentsAprob.setText(commentsCondAprob);

        Button btnOkComment = (Button) commentsDialog.findViewById(R.id.btnOkComment);
        btnOkComment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                commentsCondAprob = textCommentsAprob.getText().toString();
                commentsDialog.dismiss();

            }
        });

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.5);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.3);

        commentsDialog.show();
        commentsDialog.getWindow().setLayout(width, height);

    }

    private void addListenerBtnCommAprob() {
        btnCommAprob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                displayCommentsAprob();

            }
        });

    }

    private void displayCommentsAprob() {

        commentsDialog = new Dialog(AprobareComanda.this);
        commentsDialog.setContentView(R.layout.aprobcommentsdialogbox);
        commentsDialog.setTitle("Comentarii");
        commentsDialog.setCancelable(false);

        final EditText textCommentsAprob = (EditText) commentsDialog.findViewById(R.id.editTextComments);
        textCommentsAprob.setText(readComments);
        textCommentsAprob.setEnabled(false);

        Button btnOkComment = (Button) commentsDialog.findViewById(R.id.btnOkComment);
        btnOkComment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                commentsDialog.dismiss();

            }
        });

        commentsDialog.show();
        commentsDialog.getWindow().setLayout(500, 290);

    }

    private void checkStaticVars() {
        // pentru in idle mare variabilele statice se sterg si setarile locale
        // se reseteaza

        // resetare locale la idle
        String locLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

        if (!locLang.toLowerCase(Locale.getDefault()).equals("en")) {

            String languageToLoad = "en";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        // restart app la idle
        if (UserInfo.getInstance().getCod().equals("")) {

            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

    @Override
    public void onBackPressed() {

        UserInfo.getInstance().setParentScreen("");
        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(nextScreen);
        finish();
        return;
    }

    private void fillComandaDetails(int comandaPos) {

        textNumeArt.setText("");
        textCodArt.setText("");
        textAdrLivrNoua.setText("");
        textAdresaLivrare.setText("");
        textTipTransport.setText("");
        textMetodaPlata.setText("");
        textTipReducere.setText("");
        textMarjaCmd.setText("");
        textMarjaCmdTotal.setText("");
        textPondereArtB.setText("");
        textPondereB_30.setText("");
        setCheckEliminaTranspVisibility();

        labelAdresa.setVisibility(View.INVISIBLE);
        textPondereArtB.setVisibility(View.INVISIBLE);
        textPondereB_30.setVisibility(View.INVISIBLE);
        textCastigBrut.setVisibility(View.INVISIBLE);
        textCastigBrutTotal.setVisibility(View.INVISIBLE);
        textComandaBV90.setVisibility(View.INVISIBLE);

        tipAgentComanda = "AV";

        divizieAgent = listComenzi.get(comandaPos).getDivizieAgent();

        if (listComenzi.get(comandaPos).getNumeAgent().contains("(KA)"))
            tipAgentComanda = "KA";

        tipAprob = "";
        tipAprob += listComenzi.get(comandaPos).getAccept1().equals("X") ? "SD" : " " + ";";
        tipAprob += listComenzi.get(comandaPos).getAccept2().equals("X") ? "DV" : " ";

        String adrLivrN = listComenzi.get(comandaPos).getAdresaNoua();
        cmdNr = listComenzi.get(comandaPos).getId();
        selectedCmd = cmdNr;
        selectedCmdSAP = listComenzi.get(comandaPos).getCmdSap();
        String cmdAngaj = listComenzi.get(comandaPos).getStare();
        String strPondereB_30 = listComenzi.get(comandaPos).getPondere30();
        textPondereB_30.setText("Pondere art. B ultimele 30 zile: " + strPondereB_30 + "%");

        cmdAngajament = false;

        if (cmdAngaj.contains("angaj")) {
            cmdAngajament = true;
        }

        if (UserInfo.getInstance().getTipAcces().equals("10")) // userul
        // este SD
        {

            conditiiArticolLayout.setVisibility(View.GONE);

            // este comanda de agent
            if (tipAgentComanda.equals("AV")) {
                if (tipAprob.contains("DV")) {

                    btnConditii.setVisibility(View.VISIBLE);
                    aprobaCmd.setVisibility(View.VISIBLE);
                    respingeCmd.setVisibility(View.VISIBLE);

                }

                if (tipAprob.contains("SD") || !adrLivrN.equals("-1")) {

                    labelAdresa.setVisibility(View.VISIBLE);
                    textAdrLivrNoua.setVisibility(View.VISIBLE);

                    if (!adrLivrN.equals("-1")) {
                        btnConditii.setVisibility(View.VISIBLE);
                        labelAdresa.setText("Adresa de livrare noua");
                    } else {
                        btnConditii.setVisibility(View.VISIBLE);
                        labelAdresa.setText("Adresa de livrare");
                        textAdrLivrNoua.setText(listComenzi.get(comandaPos).getAdresaLivrare());

                    }

                    aprobaCmd.setVisibility(View.VISIBLE);
                    respingeCmd.setVisibility(View.VISIBLE);

                }

                // aprobare SD pentru adresa de livrare noua
                if (tipAprob.contains("SD") && !adrLivrN.equals("-1")) {
                    btnConditii.setVisibility(View.VISIBLE);
                    aprobaCmd.setVisibility(View.VISIBLE);
                    respingeCmd.setVisibility(View.VISIBLE);
                }
            }

            // este comanda KA
            if (tipAgentComanda.equals("KA")) {

                // aprobare SD pentru stoc KA
                if (tipAprob.contains("SD")) {
                    aprobaCmd.setVisibility(View.VISIBLE);
                    respingeCmd.setVisibility(View.VISIBLE);
                }

                // afisare adresa de livrare noua
                if (!adrLivrN.equals("-1")) {
                    labelAdresa.setVisibility(View.VISIBLE);
                    textAdrLivrNoua.setVisibility(View.VISIBLE);
                }

                // nu necesita aprobare sd, doar se afiseaza
                if (!tipAprob.contains("SD")) {
                    btnConditii.setVisibility(View.INVISIBLE);
                    aprobaCmd.setVisibility(View.INVISIBLE);
                    respingeCmd.setVisibility(View.INVISIBLE);
                    slidingDrawerAprob.setVisibility(View.INVISIBLE);
                }

            }

        }

        if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")) {// DV
            // sau
            // DD

            conditiiArticolLayout.setVisibility(View.VISIBLE);

            if (cmdAngaj.contains("angaj")) {
                btnConditii.setVisibility(View.INVISIBLE);

            } else {
                btnConditii.setVisibility(View.VISIBLE);
                aprobaCmd.setVisibility(View.VISIBLE);
                respingeCmd.setVisibility(View.VISIBLE);
            }

        }

        if (UserInfo.getInstance().getTipAcces().equals("18")) // sef de
        // magazin
        {
            btnConditii.setVisibility(View.INVISIBLE);
            textPondereArtB.setVisibility(View.INVISIBLE);
            textPondereB_30.setVisibility(View.INVISIBLE);

            aprobaCmd.setVisibility(View.VISIBLE);
            respingeCmd.setVisibility(View.INVISIBLE);

        }

        performArtCmd();

    }

    @SuppressWarnings("unchecked")
    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {

        switch (methodName) {
            case GET_LIST_COMENZI:
                populateCmdList((List<BeanComandaCreata>) result);
                break;

            case GET_ARTICOLE_COMANDA_JSON:
                populateArtCmdList(operatiiComenzi.deserializeArticoleComanda((String) result));
                break;

            case OPERATIE_COMANDA:
                selectedCmd = "-1";
                selectedCmdSAP = "-1";
                cmdNr = "";
                codRespingere = "";
                getListComenzi();
                break;

            case SALVEAZA_CONDITII_COMANDA_SER:
                saveCondStatus((String) result);
                break;
            default:
                break;
        }

    }

    public void denialOperationOccured(int tipOpCmd, String codRespingere) {
        this.tipOpCmd = tipOpCmd;
        this.codRespingere = codRespingere;
        opereazaComanda();

    }

    private void setupContextLayout(BeanComandaCreata comanda) {
        if (!tipAgentComanda.equals("AV") && UtilsUser.isSD()) {
            slidingDrawerAprob.setVisibility(View.GONE);
            btnConditii.setVisibility(View.INVISIBLE);
        } else {
            slidingDrawerAprob.setVisibility(View.VISIBLE);
            btnConditii.setVisibility(View.VISIBLE);
        }
    }

    private void addListenerSpinnerCmd() {
        spinnerComenzi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosComanda = position;
                fillComandaDetails(position);

                comandaCurenta = (BeanComandaCreata) parent.getAdapter().getItem(position);
                setupContextLayout(comandaCurenta);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dialogResponse(EnumDialogConstraints constraint, EnumDaNuOpt response) {
        switch (constraint) {
            case COMANDA_SUB_CMP:
                if (response == EnumDaNuOpt.DA)
                    showConfirmationAlert();
                break;
            case APROBARE_COMANDA:
                if (response == EnumDaNuOpt.DA) {
                    if (!cmdAngajament) {
                        tipOpCmd = 0;
                    } else
                        tipOpCmd = 7; // aprobare comanda amgajament

                    opereazaComanda();
                }
                break;
            default:
                break;
        }

    }

}
