/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import my.logon.screen.R;
import my.logon.screen.adapters.ArticolModificareAdapter;
import my.logon.screen.adapters.ArticolePretTransport;
import my.logon.screen.adapters.ComandaModificareAdapter;
import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolCalculDesc;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanArticoleAfisare;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.BeanConditii;
import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.beans.BeanConditiiHeader;
import my.logon.screen.beans.ComandaCalculDescarcare;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneCamion;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.dialogs.AprobariDialog;
import my.logon.screen.dialogs.CostMacaraDialog;
import my.logon.screen.dialogs.CostPaletiDialog;
import my.logon.screen.dialogs.RezumatComandaDialog;
import my.logon.screen.dialogs.SelectTipMasinaDialog;
import my.logon.screen.dialogs.TranspModifCmdDialog;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumPaleti;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.helpers.HelperCostDescarcare;
import my.logon.screen.helpers.HelperCreareComanda;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.listeners.ArticolModificareListener;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.CnpDialogListener;
import my.logon.screen.listeners.ComandaMathausListener;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.CostMacaraListener;
import my.logon.screen.listeners.ModifCmdTranspListener;
import my.logon.screen.listeners.PaletiListener;
import my.logon.screen.listeners.TipMasinaLivrareListener;
import my.logon.screen.model.AlgoritmComandaGed;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Comanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.HelperTranspBuc;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.ListaArticoleModificareComanda;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsComenziGed;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class ModificareComanda extends Activity implements AsyncTaskListener, ComenziDAOListener, ArticolModificareListener, Observer, CostMacaraListener,
        PaletiListener, CnpDialogListener, ModifCmdTranspListener, TipMasinaLivrareListener, ComandaMathausListener {

    Button quitBtn, stocBtn, clientBtn, articoleBtn, livrareBtn, salveazaComandaBtn, stergeComandaBtn, btnCommentariiCond, aprobareBtn;
    String filiala = "", nume = "", cod = "", globalSubCmp = "0";
    public static String unitLogComanda = "";
    public static String numeDepart = "";
    public static String codDepart = "";

    private int listViewSelPos = -1;

    private Spinner spinnerComenzi;
    public SimpleAdapter adapterComenzi;

    public static String selectedCmd = "";
    private String selectedCmdSAP = "-1";
    private String selectedClientCode = "-1", selectedUnitLog = "-1";
    private BeanComandaCreata comandaSelectata;
    private TextView textTipPlata, textAdrLivr, textTotalCmd, textOras, textJudet;
    private TextView textPersContact, textTelefon, textCantar, textTransport, textFactRed, textPondereB, textTaxaVerde;

    private TextView textCondProcB, textCondNrFacturi, textCondComentarii;

    public static String codClientVar = "";
    public static String numeClientVar = "";
    public static String tipClientVar = "";
    public static String articoleComanda = "", numeArtSelContextMenu = "", codArtSelContextMenu = "";
    public static double totalComanda = 0, stocArtCond = 0;

    public static boolean isComandaDistrib = true;

    private static ArticolComanda[] objArticol = new ArticolComanda[70];

    public static String filialaAlternativaM = "NN10";

    private ProgressBar mProgress;
    private Timer myTimer;
    private int progressVal = 0;
    private Handler logonHandler = new Handler();

    private String comandaBlocata = "0";
    private String globalAlertSDKA = "", globalAlertDVKA = "";
    private String conditieID = "";
    private int idOperatieComanda = 3;

    private boolean alertSD = false;
    private boolean alertDV = false;

    public static String tipAcces;
    ListView listViewArticole, listViewArtCond;

    private LinearLayout layoutTaxaVerde, layoutConditiiHeader, layoutCondProcB, layoutCondNrFact, layoutCondObs;

    public static String depozitUnic = "";
    private String selectedCmdAdrLivr = "";
    private String mailAlertTipDest = "";

    public static String divizieComanda = "";

    private LinearLayout layoutDetaliiCmd;

    private Comanda comandaFinala;

    String serializedResult;
    private String comandaJson;
    private ComenziDAO operatiiComenzi;
    private List<BeanComandaCreata> listComenzi;
    private ArrayList<ArticolComanda> listArticoleComanda;
    private List<BeanConditiiArticole> conditiiComandaArticole;
    private ArticolModificareAdapter adapterArticole;
    private String codTipReducere = "-1";
    private LinearLayout layoutBV90;

    private CostDescarcare costDescarcare;
    private Button valTranspBtn;
    private TextView textAlertaMarja;

    private double valTransport = 0;
    private double valTransportSAP = 0;
    public static boolean permitArticoleDistribIP = true;
    private List<OptiuneCamion> stareOptiuniCamion;
    private LivrareMathaus livrareMathaus;
    private OperatiiArticol opArticol;
    private double costTransportIP = 0;
    private boolean isAfisOptiuniMasini = false;
    private static boolean saveComandaMathaus = false;
    private RezumatComandaDialog rezumatComanda;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.modificarecomandaheader);

        operatiiComenzi = ComenziDAO.getInstance(this);
        operatiiComenzi.setComenziDAOListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Modificare comanda");
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkStaticVars();

        opArticol = new OperatiiArticolImpl(this);

        spinnerComenzi = (Spinner) findViewById(R.id.spinnerCmd);
        spinnerComenzi.setVisibility(View.INVISIBLE);
        addListenerSpinnerComenzi();

        layoutDetaliiCmd = (LinearLayout) findViewById(R.id.layoutDetaliiCmd);
        listViewArticole = (ListView) findViewById(R.id.listArtModif);

        layoutBV90 = (LinearLayout) findViewById(R.id.layoutBV90);
        layoutBV90.setVisibility(View.GONE);

        if (isUserCV())
            ListaArticoleComandaGed.getInstance().addObserver(this);
        else
            ListaArticoleModificareComanda.getInstance().addObserver(this);

        addListenerListArtModif();
        registerForContextMenu(listViewArticole);

        layoutTaxaVerde = (LinearLayout) findViewById(R.id.layoutTaxaVerde);
        layoutTaxaVerde.setVisibility(View.INVISIBLE);

        textTaxaVerde = (TextView) findViewById(R.id.textTaxaVerde);

        textTipPlata = (TextView) findViewById(R.id.textTipPlata);
        textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
        textPersContact = (TextView) findViewById(R.id.textPersContact);
        textTelefon = (TextView) findViewById(R.id.textTelefon);
        textCantar = (TextView) findViewById(R.id.textCantar);
        textTransport = (TextView) findViewById(R.id.textTransport);
        textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);
        textFactRed = (TextView) findViewById(R.id.textFactRed);
        textPondereB = (TextView) findViewById(R.id.textPondereB);

        layoutConditiiHeader = (LinearLayout) findViewById(R.id.layoutConditiiHeader);
        layoutCondProcB = (LinearLayout) findViewById(R.id.layoutCondProcB);
        layoutCondNrFact = (LinearLayout) findViewById(R.id.layoutCondNrFact);
        layoutCondObs = (LinearLayout) findViewById(R.id.layoutCondObs);

        textCondProcB = (TextView) findViewById(R.id.textCondProcB);
        textCondNrFacturi = (TextView) findViewById(R.id.textCondNrFacturi);
        textCondComentarii = (TextView) findViewById(R.id.textCondComentarii);

        textOras = (TextView) findViewById(R.id.textOras);
        textJudet = (TextView) findViewById(R.id.textJudet);

        this.salveazaComandaBtn = (Button) findViewById(R.id.saveCmdBtn);
        this.salveazaComandaBtn.setVisibility(View.INVISIBLE);
        addListenerSaveCmdBtn();

        this.stergeComandaBtn = (Button) findViewById(R.id.delCmdBtn);

        aprobareBtn = (Button) findViewById(R.id.aprobareBtn);

        if (UtilsUser.isUserKA()) {
            aprobareBtn.setVisibility(View.VISIBLE);
            openAprobareDialog();
        }

        addListenerDelCmdBtn();

        mProgress = (ProgressBar) findViewById(R.id.progress_bar_savecmd);
        mProgress.setVisibility(View.INVISIBLE);

        valTranspBtn = (Button) findViewById(R.id.valTransp);
        addListenerValTranspBtn();
        textAlertaMarja = (TextView) findViewById(R.id.textAlertaMarja);

        loadListComenzi();

    }

    public void addListenerValTranspBtn() {
        valTranspBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showModifValTranspDialogBox();

            }
        });

    }

    private boolean isComandaClpGed() {
        return !isComandaDistrib && !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty()
                && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
    }

    public void showModifValTranspDialogBox() {
        final Dialog dialogModifValTransp = new Dialog(ModificareComanda.this);
        dialogModifValTransp.setContentView(R.layout.modifvaltranspdialogbox);
        dialogModifValTransp.setTitle("Modificare valoare transport");
        dialogModifValTransp.setCancelable(false);
        dialogModifValTransp.show();

        final NumberFormat nf3 = NumberFormat.getInstance(new Locale("en", "US"));
        nf3.setMinimumFractionDigits(2);
        nf3.setMaximumFractionDigits(2);
        nf3.setGroupingUsed(false);

        final EditText textValTransp = (EditText) dialogModifValTransp.findViewById(R.id.txtValTransp);

        TextView txtTranspSAP = (TextView) dialogModifValTransp.findViewById(R.id.txtTranspSAP);
        txtTranspSAP.setText("SAP: " + nf3.format(valTransportSAP));

        ListView listViewArticoleTransp = (ListView) dialogModifValTransp.findViewById(R.id.listArticoleTransp);

        ArticolePretTransport adapterArticoleTransport = new ArticolePretTransport(ModificareComanda.this, listArticoleComanda);
        listViewArticoleTransp.setAdapter(adapterArticoleTransport);

        textValTransp.setText(nf3.format(valTransport));
        textValTransp.setSelection(textValTransp.getText().length(), textValTransp.getText().length());

        Button btnOkTransp = (Button) dialogModifValTransp.findViewById(R.id.btnOkTransp);
        btnOkTransp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (textValTransp.getText().toString().trim().length() > 0) {
                    if (Double.parseDouble(textValTransp.getText().toString().trim()) >= 0) {

                        valTransport = Double.parseDouble(textValTransp.getText().toString().trim());

                        if (valTransport < valTransportSAP) {
                            Toast.makeText(getApplicationContext(), "Valoarea transportului nu poate fi mai mica decat cea din SAP!", Toast.LENGTH_SHORT)
                                    .show();
                            valTransport = valTransportSAP;
                            textValTransp.setText(nf3.format(valTransport));
                        } else {
                            valTranspBtn.setText("Transp: " + textValTransp.getText().toString().trim());
                            calculProcente(listArticoleComanda);
                            dialogModifValTransp.dismiss();
                        }

                        UtilsComenziGed.setValoareArticolTransport(listArticoleComanda, valTransport);
                        adapterArticole.notifyDataSetChanged();

                        DateLivrare.getInstance().setValTransport(valTransport);

                    }

                }

            }
        });

        Button btnCancelTransp = (Button) dialogModifValTransp.findViewById(R.id.btnCancelTransp);
        btnCancelTransp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialogModifValTransp.dismiss();

            }
        });

    }

    public void openAprobareDialog() {
        aprobareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    AprobariDialog aprove = new AprobariDialog(ModificareComanda.this);
                    aprove.getAproveData(selectedCmd);
                    aprove.show();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        });
    }

    private void CreateMenu(Menu menu) {

        if (!isUserCVExc()) {
            MenuItem mnu2 = menu.add(0, 0, 0, "Articole");
            mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        MenuItem mnu3 = menu.add(0, 1, 1, "Livrare");
        mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                if (codClientVar.length() > 0) {

                    Intent nextScreen = null;
                    if (isUserCV() || isComandaGed()) {
                        nextScreen = new Intent(getApplicationContext(), SelectArtCmdGed.class);
                        nextScreen.putExtra("totalNegociat", false);
                        nextScreen.putExtra("codClientVar", selectedClientCode);
                        nextScreen.putExtra("depozitUnic", depozitUnic);
                        nextScreen.putExtra("tipComanda", "X");
                        nextScreen.putExtra("rezervStoc", false);
                        nextScreen.putExtra("filialaAlternativa", selectedUnitLog);
                        nextScreen.putExtra("canalDistrib", ModificareComanda.isComandaDistrib ? "10" : "20");
                        nextScreen.putExtra("tipPersClient", comandaSelectata.isCmdInstPublica() ? "IP" : "");

                        new SelectArtCmdGed().addFilialaMathaus(selectedUnitLog, getBaseContext());

                    } else {
                        nextScreen = new Intent(getApplicationContext(), SelectArtModificareCmd.class);

                    }
                    startActivity(nextScreen);

                } else {
                    Toast.makeText(getApplicationContext(), "Selectati o comanda!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case 1:

                if (codClientVar.length() > 0) {

                    Intent nextScreenLivr = null;
                    if (isUserCV() || isComandaGed()) {
                        nextScreenLivr = new Intent(getApplicationContext(), SelectAdrLivrCmdGed.class);
                        nextScreenLivr.putExtra("codClient", selectedClientCode);
                        nextScreenLivr.putExtra("parrentClass", "ModificareComanda");
                        nextScreenLivr.putExtra("tipPlataContract", DateLivrare.getInstance().getTipPlata());
                        nextScreenLivr.putExtra("limitaCredit", String.valueOf(DateLivrare.getInstance().getLimitaCredit()));
                        nextScreenLivr.putExtra("termenPlata", DateLivrare.getInstance().getTermenPlata());

                    } else {
                        nextScreenLivr = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
                        nextScreenLivr.putExtra("parrentClass", "ModificareComanda");
                        nextScreenLivr.putExtra("tipPlataContract", DateLivrare.getInstance().getTipPlata());
                        nextScreenLivr.putExtra("limitaCredit", String.valueOf(DateLivrare.getInstance().getLimitaCredit()));
                        nextScreenLivr.putExtra("termenPlata", DateLivrare.getInstance().getTermenPlata());
                    }

                    selectedCmdAdrLivr = selectedCmd;
                    startActivity(nextScreenLivr);

                } else {
                    Toast.makeText(getApplicationContext(), "Selectati o comanda!", Toast.LENGTH_SHORT).show();
                }


                return true;

            case android.R.id.home:

                articoleComanda = "";
                numeClientVar = "";
                codClientVar = "";

                UserInfo.getInstance().setParentScreen("");
                clearAllData();

                Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

                startActivity(nextScreen);

                finish();
                return true;

        }
        return false;
    }

    boolean isUserCV() {
        return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("CVR")
                || UserInfo.getInstance().getTipUser().equals("SM") || UserInfo.getInstance().getTipUserSap().equals("KA3")
                || UserInfo.getInstance().getTipUser().equals("SMR") || UserInfo.getInstance().getTipUser().equals("WOOD")
                || UserInfo.getInstance().getTipUser().equals("SC");
    }

    private boolean isUserCVExc() {
        return UserInfo.getInstance().getTipUser().equals("CVR") || UserInfo.getInstance().getTipUserSap().equals("KA3")
                || UserInfo.getInstance().getTipUser().equals("SMR") || UserInfo.getInstance().getTipUser().equals("WOOD");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @Override
    public void onResume() {

        super.onResume();
        checkStaticVars();

        if (DateLivrare.getInstance().getDateLivrare().length() > 0) {

            DateLivrare dateLivrareInstance = DateLivrare.getInstance();

            textJudet.setText(dateLivrareInstance.getNumeJudet());
            textOras.setText(dateLivrareInstance.getOras());
            textAdrLivr.setText(dateLivrareInstance.getStrada());
            textPersContact.setText(dateLivrareInstance.getPersContact());
            textTelefon.setText(dateLivrareInstance.getNrTel());

            // ***************cantar
            if (dateLivrareInstance.getCantar().equals("NU"))
                textCantar.setText("Nu");
            else
                textCantar.setText("Da");
            // ***************sf. cantar

            textFactRed.setText(UtilsGeneral.getTipReducere(dateLivrareInstance.getRedSeparat()));
            textTipPlata.setText(UtilsGeneral.getDescTipPlata(dateLivrareInstance.getTipPlata(), dateLivrareInstance.getTermenPlata()));
            textTransport.setText(UtilsGeneral.getDescTipTransport(dateLivrareInstance.getTransport()));

            if (!isUserCV() && !isComandaGed()) {

                if (dateLivrareInstance.getZonaBucuresti() != null) {

                    HelperTranspBuc.eliminaCostTransportZoneBuc(listArticoleComanda);

                    if (HelperTranspBuc.isCondTranspZonaBuc(dateLivrareInstance, dateLivrareInstance.getZonaBucuresti())) {
                        HelperTranspBuc.adaugaTransportBucuresti(listArticoleComanda, dateLivrareInstance.getZonaBucuresti());

                    }

                    adapterArticole.setListArticole(listArticoleComanda);
                    adapterArticole.notifyDataSetChanged();
                    listViewArticole.setAdapter(adapterArticole);

                }

            }

        }

    }

    private double getTotalComanda() {

        double localTotalComanda = 0;

        if (listArticoleComanda != null) {
            for (ArticolComanda articol : listArticoleComanda) {

                localTotalComanda += articol.getPretUnit() * articol.getCantUmb();

            }
        }

        return localTotalComanda;

    }

    public void calculPondereB() {

        double totalArtB = 0, procentB = 0, localTotalComanda = 0;

        for (ArticolComanda articol : listArticoleComanda) {

            if (articol.getTipArt().equalsIgnoreCase("B"))
                totalArtB += articol.getPret();

            localTotalComanda += articol.getPretUnit() * articol.getCantUmb();

        }

        if (localTotalComanda == 0) {
            procentB = 0;
        } else {
            procentB = totalArtB / localTotalComanda * 100;
        }

        textPondereB.setText(String.format("%.02f", procentB) + "%");
        textTotalCmd.setText(String.format("%.02f", localTotalComanda));
        totalComanda = localTotalComanda;

    }

    public void addListenerSaveCmdBtn() {
        salveazaComandaBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                try {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            if (listArticoleComanda.size() == 0) {
                                Toast.makeText(getApplicationContext(), "Comanda nu contine articole!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (!selectedCmdAdrLivr.equals(selectedCmd)) {
                                Toast.makeText(getApplicationContext(), "Verificati datele de livrare!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (!isConditiiAcceptate()) {
                                Toast.makeText(getApplicationContext(), "Preluati toate conditiile!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (!isConditiiCmdAccept()) {
                                Toast.makeText(getApplicationContext(), "Comanda nu are toate aprobarile!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (isConditiiSolicitCnp() && DateLivrare.getInstance().getCnpClient().trim().length() == 0) {
                                showCnpDialog();
                                return false;
                            }

                            mProgress.setVisibility(View.VISIBLE);
                            mProgress.setProgress(0);
                            progressVal = 0;
                            myTimer = new Timer();
                            myTimer.schedule(new UpdateProgress(), 40, 15);

                            return true;

                        case MotionEvent.ACTION_UP:

                            if (listArticoleComanda.size() > 0 && myTimer != null) {
                                myTimer.cancel();
                                mProgress.setVisibility(View.INVISIBLE);
                            }
                            return true;

                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

        });

    }


    private boolean isConditiiSolicitCnp() {

        if (!ModificareComanda.tipClientVar.equals("PF"))
            return false;

        double valGreutateCmd = 0;
        double valFTvaCmd = 0;

        for (ArticolComanda articol : listArticoleComanda) {
            if (articol.getGreutate() > 0) {
                valGreutateCmd += articol.getGreutate();

                if (isComandaDistrib)
                    valFTvaCmd += articol.getPret();
                else
                    valFTvaCmd += HelperCreareComanda.getPretFaraTVA(articol);
            }
        }

        if (valGreutateCmd > Constants.MAX_GREUTATE_CNP || valFTvaCmd >= Constants.MAX_VALOARE_CNP)
            return true;

        return false;

    }

    private void showCnpDialog() {
        CnpDialog dialog = new CnpDialog(this);
        dialog.setCnpListener(ModificareComanda.this);
        dialog.show();
    }

    @Override
    public void cnpSaved(String cnp) {
        DateLivrare.getInstance().setCnpClient(cnp);
        mProgress.setProgress(50);
        myTimer = new Timer();
        myTimer.schedule(new UpdateProgress(), 40, 15);

    }


    class UpdateProgress extends TimerTask {
        public void run() {
            progressVal++;
            if (mProgress.getProgress() == 50) {
                logonHandler.post(new Runnable() {
                    public void run() {

                        DateLivrare dateLivrareInstance = DateLivrare.getInstance();

                        dateLivrareInstance.setTotalComanda(String.valueOf(totalComanda));
                        dateLivrareInstance.setCodAgent(UserInfo.getInstance().getCod());
                        dateLivrareInstance.setFactRed("-1");

                        String alerteKA = globalAlertSDKA + "!" + globalAlertDVKA;

                        // aprobare adr. livrare noua doar pentru agenti
                        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
                            if (dateLivrareInstance.isAdrLivrNoua())
                                comandaBlocata = "1";
                        }

                        String localRedSeparat = codTipReducere.equals("-1") ? dateLivrareInstance.getRedSeparat() : codTipReducere;

                        if (isComandaGed())
                            localRedSeparat = "NU";

                        if (isReducere())
                            localRedSeparat = "X";

                        NumberFormat nf3 = NumberFormat.getInstance();
                        nf3.setMinimumFractionDigits(2);
                        nf3.setMaximumFractionDigits(2);

                        if (dateLivrareInstance.getObsPlata().equals("SO") && dateLivrareInstance.getTipPlata().equals("E")) {
                            if (!dateLivrareInstance.isValIncModif()) {
                                dateLivrareInstance.setValoareIncasare(nf3.format(ModificareComanda.totalComanda * Constants.TVA));
                            }
                        }

                        String userSiteMail = " ", isValIncModif = " ", codJ = "", adrLivrareGED = "";

                        if (dateLivrareInstance.isValIncModif())
                            isValIncModif = "X";

                        comandaFinala = new Comanda();
                        comandaFinala.setCodClient(selectedClientCode);
                        comandaFinala.setComandaBlocata(comandaBlocata);
                        comandaFinala.setNrCmdSap(selectedCmdSAP);
                        comandaFinala.setConditieID(conditieID);

                        comandaFinala.setAlerteKA(alerteKA);
                        comandaFinala.setFactRedSeparat(localRedSeparat);
                        comandaFinala.setFilialaAlternativa(ModificareComanda.filialaAlternativaM);
                        comandaFinala.setUserSite(UserInfo.getInstance().getUserSite());
                        comandaFinala.setUserSiteMail(dateLivrareInstance.getMail());
                        comandaFinala.setIsValIncModif(isValIncModif);
                        comandaFinala.setCodJ(codJ);
                        comandaFinala.setAdresaLivrareGed(serializeDateLivrareGed());
                        comandaFinala.setNumeClient(dateLivrareInstance.getNumeClient());
                        comandaFinala.setCnpClient(dateLivrareInstance.getCnpClient());
                        comandaFinala.setNecesarAprobariCV(comandaSelectata.getAprobariNecesare());

                        if ((dateLivrareInstance.getTipPlata().equals("E") || dateLivrareInstance.getTipPlata().equals("N") || dateLivrareInstance.getTipPlata().equals("E1") || dateLivrareInstance.getTipPlata().equals("R")) && tipClientVar.equals("PJ")) {
                            if (totalComanda > 5000) {
                                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de 5000 RON!", Toast.LENGTH_SHORT).show();
                                return;
                            } else
                                getTotalComenziNumerar();
                        } else if ((dateLivrareInstance.getTipPlata().equals("E") || dateLivrareInstance.getTipPlata().equals("N") || dateLivrareInstance.getTipPlata().equals("E1") || dateLivrareInstance.getTipPlata().equals("R")) && tipClientVar.equals("PF")) {
                            if (totalComanda > 10000) {
                                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de 10000 RON!", Toast.LENGTH_SHORT).show();
                                return;
                            } else
                                getTotalComenziNumerar();
                        } else {
                                getLivrariMathaus();
                        }

                    }
                });

                myTimer.cancel();
            } else {
                mProgress.setProgress(progressVal);
            }

        }
    }

    //aici
    // de implementat isConditiiCostTransport()


    private void getOptiuniMasini() {

        stareOptiuniCamion = new ArrayList<>();
        isAfisOptiuniMasini = false;
        if (DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getDatePoligonLivrare() != null ) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("filiala", this.filialaAlternativaM);
            params.put("camionDescoperit", DateLivrare.getInstance().isCamionDescoperit() + "");
            params.put("macara", DateLivrare.getInstance().isMasinaMacara() + "");
            params.put("zona", DateLivrare.getInstance().getDatePoligonLivrare().getTipZona());
            params.put("greutateComanda", UtilsComenzi.getGreutateKgArticole(listArticoleComanda) + "");
            params.put("comandaEnergofaga", UtilsComenzi.isComandaEnergofaga(listArticoleComanda) + "");
            params.put("comandaExtralungi", UtilsComenzi.isComandaExtralungi(listArticoleComanda) + "");

            operatiiComenzi.getOptiuniMasini(params);
        } else
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
    }

    private void afisOptiuniMasini(String optiuniMasini) {

        if (!optiuniMasini.trim().isEmpty() && optiuniMasini.contains("#")) {
            isAfisOptiuniMasini = true;
            String[] listMasini = optiuniMasini.split("#");
            HelperCreareComanda.setStareOptiuniCamion(stareOptiuniCamion, listMasini);
            SelectTipMasinaDialog selectMasinaDialog = new SelectTipMasinaDialog(this, listMasini);
            selectMasinaDialog.setTipMasinaLivrareListener(this);
            selectMasinaDialog.show();
        } else
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);

    }

    @Override
    public void tipMasinaLivrareSelected(String tipMasinaLivrare) {
        HelperCreareComanda.setCamionSelectat(stareOptiuniCamion, tipMasinaLivrare);
        DateLivrare.getInstance().setTipMasina(tipMasinaLivrare);
        getLivrariMathaus();

    }

    private boolean isConditiiAfisOptiuniMasini() {
        return !isComandaDL() && DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getDatePoligonLivrare() != null;
    }

    private void getLivrariMathaus() {

        ComandaMathaus comandaMathaus = new ComandaMathaus();

        String filialaLivrareMathaus = ModificareComanda.filialaAlternativaM;
        if (isComandaCLP())
            filialaLivrareMathaus = DateLivrare.getInstance().getCodFilialaCLP();

        comandaMathaus.setSellingPlant(filialaLivrareMathaus);
        List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

        String codDepartLivr = UserInfo.getInstance().getCodDepart();
        if (UserInfo.getInstance().getTipUserSap().contains("KA"))
            codDepartLivr = "10";

        for (ArticolComanda artCmd : listArticoleComanda) {

            DateArticolMathaus dateArticol = new DateArticolMathaus();
            dateArticol.setProductCode(artCmd.getCodArticol());
            dateArticol.setQuantity(artCmd.getCantitate());
            dateArticol.setUnit(artCmd.getUm());
            dateArticol.setValPoz(artCmd.getPret());
            dateArticol.setGreutate(artCmd.getGreutateBruta());

            if (UtilsComenzi.isDespozitDeteriorate(artCmd.getDepozit()))
                dateArticol.setDepozit(artCmd.getDepozit());
            else
                dateArticol.setDepozit("");

            if (artCmd.getArticolMathaus() != null)
                dateArticol.setTip2(artCmd.getArticolMathaus().getTip2());
            else
                dateArticol.setTip2("");

            if (artCmd.getFilialaSite().equals("BV90"))
                dateArticol.setUlStoc("BV90");

            listArticoleMat.add(dateArticol);

        }

        comandaMathaus.setDeliveryEntryDataList(listArticoleMat);

        AntetCmdMathaus antetComanda = new AntetCmdMathaus();
        antetComanda.setLocalitate(DateLivrare.getInstance().getOras());
        antetComanda.setCodJudet(DateLivrare.getInstance().getCodJudet());
        antetComanda.setCodClient(comandaFinala.getCodClient());
        antetComanda.setTipPers(UserInfo.getInstance().getTipUserSap());
        antetComanda.setDepart(codDepartLivr);
        antetComanda.setCodPers(DateLivrare.getInstance().getCodAgent());
        antetComanda.setTipTransp(DateLivrare.getInstance().getTransport());
        antetComanda.setCamionDescoperit(DateLivrare.getInstance().isCamionDescoperit());
        antetComanda.setMacara(DateLivrare.getInstance().isMasinaMacara());
        antetComanda.setOptiuniCamion(stareOptiuniCamion);
        antetComanda.setGreutateComanda(UtilsComenzi.getGreutateKgArticole(listArticoleComanda));
        antetComanda.setTipComandaCamion(UtilsComenzi.isComandaEnergofaga(listArticoleComanda) ? "ENERGOFAGA" : "NORMALA");


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("antetComanda", opArticol.serializeAntetCmdMathaus(antetComanda));
        params.put("comandaMathaus", opArticol.serializeComandaMathaus(comandaMathaus));
        params.put("canal", ModificareComanda.isComandaDistrib ? "10" : "20");
        params.put("datePoligon", opArticol.serializeDatePoligon(DateLivrare.getInstance().getDatePoligonLivrare()));

        operatiiComenzi.getLivrariMathaus(params);

    }

    private void eliminaCostTransport() {
        if (livrareMathaus.getCostTransport() != null && !livrareMathaus.getCostTransport().isEmpty())
            for (CostTransportMathaus costTrans : livrareMathaus.getCostTransport()) {
                costTransportIP += Double.valueOf(costTrans.getValTransp());
                costTrans.setValTransp("0");
            }
    }

    private void setLivrariMathaus(String result){

        livrareMathaus = opArticol.deserializeLivrareMathaus(result);

        //de verificat IP
        costTransportIP = 0;
        if (isExceptieComandaIP())
            eliminaCostTransport();

        if (DateLivrare.getInstance().getTransport().equals("TRAP"))
            DateLivrare.getInstance().setCostTransportMathaus(livrareMathaus.getCostTransport());

        if (isAfisOptiuniMasini) {

            if (!isExceptieComandaIP()) {
                HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "0", listArticoleComanda);
                HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "0", ListaArticoleComanda.getInstance().getListArticoleLivrare());
            }

            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
            isAfisOptiuniMasini = false;
            return;
        }

        List<DateArticolMathaus> articoleMathaus = livrareMathaus.getComandaMathaus().getDeliveryEntryDataList();
        ListaArticoleComanda.getInstance().reseteazaArticoleLivrare();

        String codArticolComanda;
        for (ArticolComanda articolComanda : listArticoleComanda) {

            articolComanda.setTipTransport(null);

            if (isComandaDL())
                articolComanda.setTipTransport(DateLivrare.getInstance().getTransport());

            if (UtilsComenzi.isComandaClp() && articolComanda.getArticolMathaus() == null)
                articolComanda.setFilialaSite(DateLivrare.getInstance().getCodFilialaCLP());

            if (articolComanda.getArticolMathaus() == null) {
                ArticolComanda articolLivrare = ListaArticoleComanda.getInstance().genereazaArticolLivrare(articolComanda);
                ListaArticoleComanda.getInstance().getListArticoleLivrare().add(articolLivrare);
                continue;
            }

            codArticolComanda = articolComanda.getCodArticol();

            if (articolComanda.getCodArticol().length() == 8 || !Character.isDigit(codArticolComanda.charAt(0)))
                codArticolComanda = "0000000000" + articolComanda.getCodArticol();

            for (DateArticolMathaus articolMathaus : articoleMathaus) {

                if (codArticolComanda.equals(articolMathaus.getProductCode())) {

                    ArticolComanda articolLivrare = ListaArticoleComanda.getInstance().genereazaArticolLivrare((ArticolComandaGed) articolComanda);
                    articolLivrare.setCantitate(articolMathaus.getQuantity());

                    if (articolComanda.getFilialaSite().equals("BV90")) {
                    } else {
                        articolLivrare.setFilialaSite(articolMathaus.getDeliveryWarehouse());
                    }

                    if (isComandaDL())
                        articolLivrare.setDepozit(articolMathaus.getDepozit());

                    ListaArticoleComanda.getInstance().getListArticoleLivrare().add(articolLivrare);
                }

            }

        }

        if (isComandaDistrib || !isComandaDistrib || isComandaCLP() || isComandaDL()) {
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "0", listArticoleComanda);
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "0", ListaArticoleComanda.getInstance().getListArticoleLivrare());
        }


        prepareArtForDelivery();
        totalComanda = getTotalComanda();

        if (!saveComandaMathaus) {
            verificaPretMacaraRezumat();
        } else
            performSaveCmd();

    }

    private void afisRezumatComandaDialog(List<CostTransportMathaus> costTransport, boolean selectTransp) {

        saveComandaMathaus = false;

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);

        String localCanalDistrib = isComandaDistrib ? "10" : "20";

        rezumatComanda = new RezumatComandaDialog(this, ListaArticoleComanda.getInstance().getListArticoleLivrare(), localCanalDistrib, costTransport, DateLivrare.getInstance().getTransport(), ModificareComanda.filialaAlternativaM, selectTransp);
        rezumatComanda.setRezumatListener(this);
        rezumatComanda.getWindow().setLayout(width, height);
        rezumatComanda.show();



    }

    private void verificaPretTransport() {
        if (isConditiiComandaTransp()) {

            List<ArticolComanda> articoleComanda = listArticoleComanda;
            ComandaMathaus comandaMathaus = new ComandaMathaus();

            //aici
            String filialaLivrareMathaus = ModificareComanda.filialaAlternativaM;

            if (isComandaClpDistrib())
                filialaLivrareMathaus = DateLivrare.getInstance().getCodFilialaCLP();

            comandaMathaus.setSellingPlant(filialaLivrareMathaus);
            List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

            String codDepartLivr = UserInfo.getInstance().getCodDepart();
            if (UserInfo.getInstance().getTipUserSap().contains("KA"))
                codDepartLivr = "10";

            for (ArticolComanda artCmd : articoleComanda) {

                DateArticolMathaus dateArticol = new DateArticolMathaus();
                dateArticol.setProductCode(artCmd.getCodArticol());
                dateArticol.setQuantity(artCmd.getCantitate());
                dateArticol.setUnit(artCmd.getUm());
                dateArticol.setValPoz(artCmd.getPret());

                dateArticol.setTip2("");

                if (artCmd.getFilialaSite() != null && artCmd.getFilialaSite().equals("BV90"))
                    dateArticol.setUlStoc("BV90");

                listArticoleMat.add(dateArticol);

            }

            comandaMathaus.setDeliveryEntryDataList(listArticoleMat);

            AntetCmdMathaus antetComanda = new AntetCmdMathaus();
            antetComanda.setLocalitate(DateLivrare.getInstance().getOras());

            antetComanda.setCodJudet(DateLivrare.getInstance().getCodJudet());
            antetComanda.setCodClient(comandaFinala.getCodClient());
            antetComanda.setTipPers(UserInfo.getInstance().getTipUserSap());
            antetComanda.setDepart(codDepartLivr);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("antetComanda", new OperatiiArticolImpl(this).serializeAntetCmdMathaus(antetComanda));
            params.put("comandaMathaus", new OperatiiArticolImpl(this).serializeComandaMathaus(comandaMathaus));

            operatiiComenzi.getLivrariMathaus(params);

        } else {
            verificaPretMacaraRezumat();
        }
    }

    private boolean isComandaClpDistrib() {
        return !DateLivrare.getInstance().getTipPersAgent().equals("CV") && !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty()
                && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
    }

    private boolean isComandaCLP(){
        return !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty()
                && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
    }

    private boolean isComandaDL(){
        return DateLivrare.getInstance().getFurnizorComanda() != null
                && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().trim().isEmpty();
    }

    private boolean isConditiiComandaTransp() {
        return (DateLivrare.getInstance().getTransport().equals("TRAP") || DateLivrare.getInstance().getTransport().equals("TERT")) && !DateLivrare.getInstance().getTipPersAgent().equals("CV");

    }

    private void adaugaPretTransport(String pretTransport) {

        LivrareMathaus livrareMathaus = new OperatiiArticolImpl(this).deserializeLivrareMathaus(pretTransport);
        DateLivrare.getInstance().setCostTransportMathaus(livrareMathaus.getCostTransport());

        HelperMathaus.adaugaArticolTransportModificare(livrareMathaus.getCostTransport(), listArticoleComanda);
        adapterArticole.notifyDataSetChanged();

        TranspModifCmdDialog dialog = new TranspModifCmdDialog(livrareMathaus.getCostTransport(), this);
        dialog.setTranspModifCmdListener(this);
        dialog.show();
    }

    @Override
    public void pretTranspModificat(List<CostTransportMathaus> listCostTransport) {
        HelperMathaus.adaugaArticolTransportModificare(listCostTransport, listArticoleComanda);
        adapterArticole.notifyDataSetChanged();

    }

    @Override
    public void pretTranspSalvat() {
        performSaveCmd();
    }

    private void getTotalComenziNumerar() {

        String tipPers = "PJN";
        String codClientNumerar = comandaFinala.getCodClient();

        if (tipClientVar.equals("PF")) {
            tipPers = "PF";
            codClientNumerar = DateLivrare.getInstance().getNrTel();
        } else if (tipClientVar.equals("PJ") && !comandaFinala.getCnpClient().trim().isEmpty()) {
            tipPers = "PJG";
            codClientNumerar = CreareComandaGed.cnpClient.replaceAll("RO", "");
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("codClient", codClientNumerar);
        params.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
        params.put("tipClient", tipPers);
        operatiiComenzi.getTotalComenziNumerar(params);

    }

    private void afisTotalComenziNumerar(String totalNumerar) {

        double valPragNumerar = 5000;

        if (tipClientVar.equals("PF"))
            valPragNumerar = 10000;

        if (totalComanda + Double.valueOf(totalNumerar) > valPragNumerar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "\nLa acest client valoarea comenzilor cu plata in numerar livrate in data de " + DateLivrare.getInstance().getDataLivrare() + " depaseste " + (int) valPragNumerar + " de lei.\n\n" +
                            "Pentru a salva comanda trebuie sa schimbati metoda de plata sau data de livrare.\n").setCancelable(false)
                    .setPositiveButton("Inchide", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();

        } else
            getLivrariMathaus();
    }

    private void verificaPretMacaraRezumat() {


        HelperCostDescarcare.eliminaCostDescarcare(listArticoleComanda);
        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComanda.getInstance().getListArticoleLivrare());

        if (!UtilsUser.isAV_SD_01()) {

            String codFurnizor = " ";

            if (DateLivrare.getInstance().getFurnizorComanda() != null
                    && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().trim().isEmpty())
                codFurnizor = DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa();
            else if (!DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty())
                codFurnizor = DateLivrare.getInstance().getCodFilialaCLP();


            List<RezumatComanda> listComenziRezumat = HelperMathaus.getRezumatComanda(ListaArticoleComanda.getInstance().getListArticoleLivrare());
            List<ComandaCalculDescarcare> listComenziDescarcare = HelperCostDescarcare.getComenziCalculDescarcare(listComenziRezumat);

            String comenziSer = operatiiComenzi.serializeCalcComenziMacara(listComenziDescarcare);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("codAgent", DateLivrare.getInstance().getCodAgent());
            params.put("codClient", comandaFinala.getCodClient());
            params.put("codFurnizor", codFurnizor);
            params.put("listComenzi", comenziSer);
            params.put("canal", isComandaDistrib ? "10" : "20");

            operatiiComenzi.getCostMacaraComenzi(params);
        } else {

            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaFluxComanda();
        }

    }

    private void verificaPretMacara() {

        HelperCostDescarcare.eliminaCostDescarcare(listArticoleComanda);

        if ((DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP") || DateLivrare.getInstance().getTransport().equalsIgnoreCase("TCLI"))
                && !isExceptieComandaIP() && !UtilsUser.isAV_SD_01()) {

            String codFurnizor = " ";

            if (DateLivrare.getInstance().getFurnizorComanda() != null
                    && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().trim().isEmpty())
                codFurnizor = DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa();
            else if (!DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty())
                codFurnizor = DateLivrare.getInstance().getCodFilialaCLP();

            List<ArticolCalculDesc> artCalcul = HelperCostDescarcare.getDateCalculDescarcare(listArticoleComanda);

            String listArtSer = operatiiComenzi.serializeArtCalcMacara(artCalcul);

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("unitLog", DateLivrare.getInstance().getUnitLog());
            params.put("codAgent", DateLivrare.getInstance().getCodAgent());
            params.put("codClient", comandaFinala.getCodClient());
            params.put("codFurnizor", codFurnizor);
            params.put("listArt", listArtSer);

            operatiiComenzi.getCostMacara(params);
        } else {
            trateazaFluxComanda();
        }

    }

    private boolean isExceptieComandaIP() {
        return UtilsUser.isUserIP() && comandaSelectata.getTipClientInstPublica() == EnumTipClientIP.CONSTR;
    }

    private void afiseazaPretMacaraComenziDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostComenziMacara(result);

        if (isExceptieComandaIP()) {
            trateazaFluxComanda();
            return;
        }

        if (!DateLivrare.getInstance().isClientFurnizor()) {
            eliminaPaletiComanda(listArticoleComanda);
            eliminaPaletiComanda(ListaArticoleComanda.getInstance().getListArticoleLivrare());
        }

        if (!costDescarcare.getArticolePaleti().isEmpty()) {

            for (int ii = 0; ii < costDescarcare.getArticoleDescarcare().size(); ii++)
                costDescarcare.getArticoleDescarcare().get(ii).setCantitate(0);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);

            CostPaletiDialog costPaleti = new CostPaletiDialog(this, costDescarcare.getArticolePaleti(), DateLivrare.getInstance().getTransport());
            costPaleti.setPaletiDialogListener(this);
            costPaleti.getWindow().setLayout(width, height);
            costPaleti.show();

        } else if (costDescarcare.getValoareDescarcare() > 0
                && DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP")) {

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, false);
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);


            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaFluxComanda();
        }

    }

    private void afiseazaPretMacaraDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostMacara(result);

        if (!costDescarcare.getArticolePaleti().isEmpty()) {
            costDescarcare.getArticoleDescarcare().get(0).setCantitate(0);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);

            CostPaletiDialog costPaleti = new CostPaletiDialog(this, costDescarcare.getArticolePaleti(), DateLivrare.getInstance().getTransport());
            costPaleti.setPaletiDialogListener(this);
            costPaleti.getWindow().setLayout(width, height);
            costPaleti.show();

        } else if (costDescarcare.getSePermite() && costDescarcare.getValoareDescarcare() > 0
                && DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP")) {

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, isComandaGed());
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);

            trateazaFluxComanda();

        }

    }

    private void trateazaFluxComanda(){

        if (isConditiiAfisOptiuniMasini() && !isExceptieComandaIP())
            getOptiuniMasini();
        else
            if (isComandaDL())
                performSaveCmd();
            else
                afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);

    }


    private void eliminaPaletiComanda(List<ArticolComanda> listArticole){
        Iterator<ArticolComanda> articolIterator = listArticole.iterator();

        while (articolIterator.hasNext()) {
            ArticolComanda articol = articolIterator.next();

            if (articol.isUmPalet()) {
                articolIterator.remove();
                adapterArticole.notifyDataSetChanged();
            }

        }

    }

    private void verificaPaletiComanda(List<ArticolPalet> listPaleti) {

        Iterator<ArticolComanda> articolIterator = listArticoleComanda.iterator();

        boolean paletExista;

        while (articolIterator.hasNext()) {
            ArticolComanda articol = articolIterator.next();

            paletExista = true;

            if (articol.isUmPalet()) {
                paletExista = false;
            }

            for (ArticolPalet palet : listPaleti) {

                if (palet.getCodPalet().equals(articol.getCodArticol()) && (palet.getCantitate() == (int) articol.getCantitate())) {
                    paletExista = true;
                }

            }

            if (!paletExista) {
                articolIterator.remove();
                adapterArticole.notifyDataSetChanged();
            }

        }

        Iterator<ArticolPalet> paletIterator = listPaleti.iterator();

        for (ArticolComanda articol : listArticoleComanda) {

            while (paletIterator.hasNext()) {
                ArticolPalet palet = paletIterator.next();

                if (palet.getCodPalet().equals(articol.getCodArticol()) && (palet.getCantitate() == (int) articol.getCantitate())) {
                    paletIterator.remove();
                }

            }

            paletIterator = listPaleti.iterator();

        }

        adapterArticole.notifyDataSetChanged();

    }

    private void trateazaPretMacara(boolean acceptaPret, double valoarePret) {

        if (acceptaPret) {
            DateLivrare.getInstance().setMasinaMacara(true);

            List<ArticolComanda> articoleDescarcare = HelperCostDescarcare.getArticoleDescarcare(costDescarcare, valoarePret, ListaArticoleComanda.getInstance().getListArticoleLivrare());

            ListaArticoleComanda.getInstance().getListArticoleLivrare().addAll(articoleDescarcare);

        } else {
            DateLivrare.getInstance().setMasinaMacara(false);
        }


        if (isConditiiAfisOptiuniMasini())
            getOptiuniMasini();
        else
            trateazaFluxComanda();

    }

    private boolean isReducere() {
        return globalSubCmp.equals("1") && !UserInfo.getInstance().getCodDepart().equals("07") && !UserInfo.getInstance().getCodDepart().equals("04");
    }

    private void performSaveCmd() {
        try {

            // comanda cv cu conditii se salveaza direct
            if (isComandaGed() && UserInfo.getInstance().getTipUser().equals("CV") && !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
                alertSD = false;
                alertDV = false;
            }

            HashMap<String, String> params = new HashMap<String, String>();

            String tipUser = "";
            if (UserInfo.getInstance().getTipAcces().equals("27"))
                tipUser = "KA";
            else if (isComandaGed())
                tipUser = "CV";
            else if (UserInfo.getInstance().getTipAcces().equals("62"))
                tipUser = "AV";
            else
                tipUser = UserInfo.getInstance().getTipUser();

            if (alertSD || alertDV)
                comandaFinala.setComandaBlocata("1");

            params.put("comanda", " ");
            params.put("tipUser", tipUser);
            params.put("JSONArt", serializeArticole());
            params.put("JSONComanda", serializeComanda(comandaFinala));
            params.put("JSONDateLivrare", serializeDateLivrare());
            params.put("alertSD", String.valueOf(alertSD));
            params.put("alertDV", String.valueOf(alertDV));
            params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
            params.put("idCmdAmob", "-1");

            operatiiComenzi.salveazaComandaDistrib(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isComandaGed() {

        String tempDistribUL = InfoStrings.getDistribUnitLog(selectedUnitLog);

        if (InfoStrings.getClientGenericGed(tempDistribUL, "PF").equals(selectedClientCode)
                || InfoStrings.getClientGenericGed(tempDistribUL, "PJ").equals(selectedClientCode)
                || InfoStrings.getClientGenericGedWood(tempDistribUL, "PF").equals(selectedClientCode)
                || InfoStrings.getClientGenericGedWood(tempDistribUL, "PJ").equals(selectedClientCode)
                || InfoStrings.getClientGenericGedWood_faraFact(tempDistribUL, "PF").equals(selectedClientCode)
                || InfoStrings.getClientGenericGed_CONSGED_faraFactura(tempDistribUL, "PF").equals(selectedClientCode)
                || InfoStrings.getClientCVO_cuFact_faraCnp(tempDistribUL, "").equals(selectedClientCode)
                || InfoStrings.getClientGed_FaraFactura(tempDistribUL).equals(selectedClientCode) || !isComandaDistrib)

            return true;
        else
            return false;
    }

    public String prepareArtForDelivery() {
        String retVal = "";

        String[] tokPret;
        double valCondPret = 0;
        double taxaVerde = 0;
        boolean hasTaxaVerde = false;

        alertSD = false;
        alertDV = false;
        comandaBlocata = "0";
        globalAlertSDKA = "?";
        globalAlertDVKA = "?";
        globalSubCmp = "0";

        totalComanda = 0;

        Collections.sort(ListaArticoleComanda.getInstance().getListArticoleLivrare(), ArticolComanda.DepartComparator);
        Iterator<ArticolComanda> iterator = ListaArticoleComanda.getInstance().getListArticoleLivrare().iterator();
        ArticolComanda articolComanda = null;

        while (iterator.hasNext()) {

            articolComanda = iterator.next();

            if (articolComanda.getInfoArticol().contains(";")) {
                String[] condPret = articolComanda.getInfoArticol().split(";");

                for (int ii = 0; ii < condPret.length; ii++) {
                    tokPret = condPret[ii].split(":");
                    valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
                    if (valCondPret != 0) {
                        if (tokPret[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
                            taxaVerde += valCondPret;
                        }

                    }
                }
            }

            if (articolComanda.getAlteValori().toString().equals("1"))
                globalSubCmp = "1";

            if (isUserExceptie()) {
                if (articolComanda.getTipAlert().contains("SD")) {
                    comandaBlocata = "1";
                    alertSD = true;

                    if (!globalAlertSDKA.contains(articolComanda.getDepart()))
                        globalAlertSDKA += articolComanda.getDepart() + "?";
                }

                if (articolComanda.getTipAlert().contains("DV")) {
                    comandaBlocata = "1";
                    alertDV = true;

                    if (!globalAlertDVKA.contains(articolComanda.getDepart()))
                        globalAlertDVKA += articolComanda.getDepart() + "?";
                }

            }

            if (isUserCV()) {
                comandaBlocata = "0";
                alertSD = false;
                alertDV = false;

            }

            if (articolComanda.getCodArticol().equals("00000000")) {
                hasTaxaVerde = true;
            }

            totalComanda += articolComanda.getPretUnit() * articolComanda.getCantUmb();

        }

        if (ModificareComanda.isComandaDistrib) {
            if (hasTaxaVerde) {
                updateTaxaVerde(taxaVerde);
            } else {
                if (taxaVerde > 0)
                    addTaxaVerde(taxaVerde);
            }
        }

        return retVal;
    }

    // userul este agent, sd sau ka
    boolean isUserExceptie() {
        return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
                || UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("62");
    }

    private void updateTaxaVerde(double taxaVerde) {
        Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
        ArticolComanda articol = null;

        while (iterator.hasNext()) {
            articol = iterator.next();

            if (articol.getCodArticol().equals("00000000")) {
                articol.setTaxaVerde(taxaVerde);
                break;
            }
        }
    }

    private void addTaxaVerde(double taxaVerde) {

        ArticolComanda articol = new ArticolComanda();
        articol.setCodArticol("000000000000000000");
        articol.setCantitate(1.0);
        articol.setDepozit(listArticoleComanda.get(0).getDepozit());
        articol.setPretUnit(taxaVerde);
        articol.setProcent(0);
        articol.setUm("BUC");
        articol.setProcentFact(0);
        articol.setConditie(false);
        articol.setDiscClient(0);
        articol.setProcAprob(0);
        articol.setMultiplu(1);
        articol.setPret(taxaVerde);
        articol.setInfoArticol(" ");
        articol.setCantUmb(1);
        articol.setUmb("BUC");
        articol.setDepart(listArticoleComanda.get(0).getDepart());
        articol.setObservatii("");
        articol.setIstoricPret("");
        listArticoleComanda.add(articol);

    }

    private String serializeArticole() {
        JSONArray myArray = new JSONArray();
        TreeSet<String> aprobariCV = new TreeSet<String>();
        JSONObject obj = null;


        try {
            for (ArticolComanda artComanda : ListaArticoleComanda.getInstance().getListArticoleLivrare()) {

                if (artComanda.getStatus() != null && artComanda.getStatus().toLowerCase().contains("respins"))
                    continue;

                obj = new JSONObject();
                obj.put("codArticol", artComanda.getCodArticol());
                obj.put("cantitate", artComanda.getCantitate());
                obj.put("depozit", artComanda.getDepozit());
                obj.put("pretUnit", artComanda.getPretUnit());
                obj.put("procent", artComanda.getProcent());
                obj.put("um", artComanda.getUm());
                obj.put("procentFact", artComanda.getProcentFact());
                obj.put("conditie", artComanda.getConditie());
                obj.put("discClient", artComanda.getDiscClient());
                obj.put("procAprob", artComanda.getProcAprob());
                obj.put("multiplu", artComanda.getMultiplu());
                obj.put("pret", artComanda.getPret());
                obj.put("infoArticol", artComanda.getInfoArticol());
                obj.put("cantUmb", artComanda.getCantUmb());
                obj.put("Umb", artComanda.getUmb());
                obj.put("depart", artComanda.getDepart());
                obj.put("ponderare", artComanda.getPonderare());

                obj.put("observatii", artComanda.getTipAlert());
                obj.put("departAprob", artComanda.getDepartAprob());
                obj.put("istoricPret", artComanda.getIstoricPret());
                obj.put("valTransport", artComanda.getValTransport());
                obj.put("filialaSite", artComanda.getFilialaSite());
                obj.put("dataExp", artComanda.getDataExpPret());
                obj.put("listCabluri", new OperatiiArticolImpl(this).serializeCabluri05(artComanda.getListCabluri()));
                obj.put("greutate", artComanda.getGreutate());
                obj.put("greutateBruta", artComanda.getGreutateBruta());
                obj.put("cantitateInit", artComanda.getCantitateInit());

                if (!UtilsUser.isAgentOrSDorKA()) {
                    if ((artComanda.getNumeArticol() != null && artComanda.getPonderare() == 1)
                            || comandaSelectata.isCmdInstPublica()) {
                        alertDV = true;
                        if (!comandaFinala.getComandaBlocata().equals("21") && !comandaFinala.getComandaBlocata().equals("20"))
                            comandaFinala.setComandaBlocata("1");

                        aprobariCV.add(artComanda.getDepartSintetic());
                    }
                }

                myArray.put(obj);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        serializedResult = myArray.toString();

        if (!UtilsUser.isAgentOrSDorKA()) {
            String strAprobariCV = new String(aprobariCV.toString());
            comandaFinala.setNecesarAprobariCV(strAprobariCV.substring(1, strAprobariCV.length() - 1));
        }

        return serializedResult;

    }

    private String serializeComanda(Comanda comanda) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("codClient", comanda.getCodClient());
            obj.put("numeClient", comanda.getNumeClient());
            obj.put("persoanaContact", comanda.getPersoanaContact());
            obj.put("telefon", comanda.getTelefon());
            obj.put("cantarire", comanda.getCantarire());
            obj.put("metodaPlata", comanda.getMetodaPlata());
            obj.put("tipTransport", comanda.getTipTransport());
            obj.put("comandaBlocata", comanda.getComandaBlocata());
            obj.put("nrCmdSap", comanda.getNrCmdSap());
            obj.put("alerteKA", comanda.getAlerteKA());
            obj.put("factRedSeparat", comanda.getFactRedSeparat());
            obj.put("filialaAlternativa", comanda.getFilialaAlternativa());
            obj.put("userSite", comanda.getUserSite());
            obj.put("userSiteMail", comanda.getUserSiteMail());
            obj.put("isValIncModif", comanda.getIsValIncModif());
            obj.put("codJ", comanda.getCodJ());
            obj.put("cnpClient", comanda.getCnpClient());
            obj.put("adresaLivrareGed", comanda.getAdresaLivrareGed());
            obj.put("adresaLivrare", comanda.getAdresaLivrare());
            obj.put("valoareIncasare", comanda.getValoareIncasare());
            obj.put("conditieID", comanda.getConditieID());
            obj.put("canalDistrib", ModificareComanda.isComandaDistrib ? "10" : "20");
            obj.put("necesarAprobariCV", comanda.getNecesarAprobariCV());
            obj.put("valTransportSap", "0");


        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return obj.toString();
    }

    private String serializeDateLivrare() {

        JSONObject obj = new JSONObject();

        try {

            obj.put("codJudet", DateLivrare.getInstance().getCodJudet());
            obj.put("numeJudet", DateLivrare.getInstance().getNumeJudet());
            obj.put("Oras", DateLivrare.getInstance().getOras());
            obj.put("Strada", DateLivrare.getInstance().getStrada());
            obj.put("persContact", DateLivrare.getInstance().getPersContact());
            obj.put("nrTel", DateLivrare.getInstance().getNrTel());
            obj.put("redSeparat", DateLivrare.getInstance().getRedSeparat());
            obj.put("Cantar", DateLivrare.getInstance().getCantar());
            obj.put("tipPlata", UtilsComenzi.getTipPlataClient(DateLivrare.getInstance().getTipPlata(), CreareComandaGed.tipPlataContract.concat(CreareComanda.tipPlataContract).trim()));
            obj.put("Transport", DateLivrare.getInstance().getTransport());
            obj.put("dateLivrare", DateLivrare.getInstance().getDateLivrare());
            obj.put("termenPlata", DateLivrare.getInstance().getTermenPlata());
            obj.put("obsLivrare", DateLivrare.getInstance().getObsLivrare());
            obj.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
            obj.put("adrLivrNoua", DateLivrare.getInstance().isAdrLivrNoua());
            obj.put("tipDocInsotitor", DateLivrare.getInstance().getTipDocInsotitor());
            obj.put("obsPlata", DateLivrare.getInstance().getObsPlata());
            obj.put("addrNumber", DateLivrare.getInstance().getAddrNumber());
            obj.put("valoareIncasare", DateLivrare.getInstance().getValoareIncasare());
            obj.put("isValIncModif", DateLivrare.getInstance().isValIncModif());
            obj.put("mail", DateLivrare.getInstance().getMail());
            obj.put("totalComanda", getTotalComanda());
            obj.put("unitLog", DateLivrare.getInstance().getUnitLog());
            obj.put("codAgent", DateLivrare.getInstance().getCodAgent());
            obj.put("idObiectiv", DateLivrare.getInstance().getIdObiectiv());
            obj.put("isAdresaObiectiv", DateLivrare.getInstance().isAdresaObiectiv());
            obj.put("coordonateGps", getCoordAdresa());
            HelperMathaus.setTonajComanda();
            obj.put("tonaj", DateLivrare.getInstance().getTonaj());
            obj.put("prelucrare", DateLivrare.getInstance().getPrelucrare());
            obj.put("clientRaft", DateLivrare.getInstance().isClientRaft());
            obj.put("meserias", DateLivrare.getInstance().getCodMeserias());
            obj.put("isComandaACZC", comandaSelectata.isComandaACZC());

            if (isComandaGed())
                obj.put("factRed", "NU");
            else
                obj.put("factRed", codTipReducere.equals("-1") ? DateLivrare.getInstance().getFactRed() : codTipReducere);
            obj.put("macara", DateLivrare.getInstance().isMasinaMacara() ? "X" : " ");

            obj.put("factPaletiSeparat", DateLivrare.getInstance().isFactPaletSeparat());

            obj.put("furnizorMarfa", DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa());
            obj.put("furnizorProduse", DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorProduse());
            obj.put("isCamionDescoperit", DateLivrare.getInstance().isCamionDescoperit());
            obj.put("programLivrare", DateLivrare.getInstance().getProgramLivrare());
            obj.put("livrareSambata", DateLivrare.getInstance().getLivrareSambata());
            obj.put("codSuperAgent", UserInfo.getInstance().getCodSuperUser());
            obj.put("filialaCLP", DateLivrare.getInstance().getCodFilialaCLP());
            obj.put("numeDelegat", DateLivrare.getInstance().getDelegat().getNume());
            obj.put("ciDelegat", DateLivrare.getInstance().getDelegat().getSerieNumarCI());
            obj.put("autoDelegat", DateLivrare.getInstance().getDelegat().getNrAuto());
            obj.put("refClient", DateLivrare.getInstance().getRefClient());
            obj.put("prelucrareLemn", DateLivrare.getInstance().getPrelucrareLemn());
            obj.put("filialaPlata", DateLivrare.getInstance().getFilialaPlata());

        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return obj.toString();

    }

    private String serializeDateLivrareGed() {

        JSONObject jsonAdresa = new JSONObject();

        try {
            jsonAdresa.put("codJudet", DateLivrare.getInstance().getCodJudetD());
            jsonAdresa.put("oras", DateLivrare.getInstance().getOrasD());
            jsonAdresa.put("strada", DateLivrare.getInstance().getAdresaD());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonAdresa.toString();
    }

    private String getCoordAdresa() {
        if (DateLivrare.getInstance().getCoordonateAdresa() != null)
            return DateLivrare.getInstance().getCoordonateAdresa().latitude + "#" + DateLivrare.getInstance().getCoordonateAdresa().longitude;
        else
            return "0#0";
    }

    private boolean isConditiiAcceptate() {
        boolean isConditiiAcceptate = true;

        for (int i = 0; i < listArticoleComanda.size(); i++) {

            if (listArticoleComanda.get(i).hasConditii()) {
                isConditiiAcceptate = false;
                break;
            }
        }

        return isConditiiAcceptate;

    }

    private boolean isConditiiCmdAccept() {
        if ((UtilsUser.isAgentOrSD() && isComandaGed()) || comandaSelectata.isCmdInstPublica())
            return isCmdGEDOkToSave();
        else
            return isCommandaOkToSave();
    }

    private boolean isCmdGEDOkToSave() {

        for (ArticolComanda articol : listArticoleComanda) {
            if (articol.getConditie())
                return false;

        }

        return true;
    }

    private boolean isCommandaOkToSave() {
        boolean isOkToSave = true;
        String aprobariNecesare = comandaSelectata.getAprobariNecesare();
        String aprobariPrimite = comandaSelectata.getAprobariPrimite();
        String conditiiImpuse = comandaSelectata.getConditiiImpuse();

        if (conditiiImpuse.trim().isEmpty())
            return true;

        for (int i = 0; i < listArticoleComanda.size(); i++) {

            if (aprobariNecesare.contains(listArticoleComanda.get(i).getDepartSintetic())) {
                if (!aprobariPrimite.contains(listArticoleComanda.get(i).getDepartSintetic())) {

                    if (conditiiImpuse.contains(listArticoleComanda.get(i).getDepartSintetic()) && !listArticoleComanda.get(i).hasConditii()) {
                        continue;
                    } else {
                        isOkToSave = false;
                        break;
                    }

                }

            }

        }

        return isOkToSave;
    }

    private void saveCmdStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {
            try {

                if (!saveResponse.equals("9")) {
                    if (alertSD) {
                        sendMailAlert(0);
                    }
                    if (alertDV) {

                    }

                    if (UserInfo.getInstance().getTipAcces().equals("27"))// alerta
                    // director
                    // ka
                    {
                        // este nevoie de cel putin o aprobare
                        if (globalAlertSDKA.contains("0") || globalAlertDVKA.contains("0")) {
                            sendMailAlert(3);
                        }
                    }

                }

                Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_LONG).show();

                clearAllData();
                loadListComenzi();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
        }
    }

    public void sendMailAlert(int tip) {
        try {

            if (tip == 1) // alerta dv
            {
                mailAlertTipDest = "2";
            }

            if (tip == 3) // alerta director KA
            {
                mailAlertTipDest = "4";
            }

            if (tip == 1 || tip == 3) {
                HashMap<String, String> params = new HashMap<String, String>();

                params.put("ul", UserInfo.getInstance().getUnitLog());
                params.put("depart", UserInfo.getInstance().getCodDepart());
                params.put("dest", mailAlertTipDest); // tip alert
                params.put("agent", UserInfo.getInstance().getNume()); // agent
                params.put("clnt", numeClientVar); //
                params.put("suma", String.format("%.02f", totalComanda)); //

                AsyncTaskWSCall call = new AsyncTaskWSCall(this, "sendMailAlert", params);
                call.getCallResults();

            }

        } catch (Exception ex) {
            Log.e("Error", ex.toString());

        }
    }

    private void clearAllData() {

        DateLivrare.getInstance().resetAll();

        articoleComanda = "";
        numeClientVar = "";
        codClientVar = "";
        selectedCmd = "";
        totalComanda = 0;
        codTipReducere = "-1";
        permitArticoleDistribIP = true;
        CreareComanda.tipPlataContract = " ";
        CreareComandaGed.tipPlataContract = " ";
        saveComandaMathaus = false;

        ListaArticoleComandaGed.getInstance().clearArticoleComanda();
        ListaArticoleComandaGed.getInstance().deleteObserver(this);

        ListaArticoleModificareComanda.getInstance().clearArticoleComanda();
        ListaArticoleModificareComanda.getInstance().deleteObserver(this);

    }

    public void addListenerDelCmdBtn() {
        stergeComandaBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                showConfirmationAlert();

            }
        });

    }

    public void showConfirmationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stergeti comanda?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                idOperatieComanda = 3;
                opereazaComanda();

            }
        }).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        }).setTitle("Confirmare").setIcon(R.drawable.warning96);

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void opereazaComanda() {

        try {

            HashMap<String, String> params = new HashMap<String, String>();

            NumberFormat nf3 = new DecimalFormat("00000000");
            String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

            params.put("nrCmd", selectedCmd);
            params.put("nrCmdSAP", selectedCmdSAP);
            params.put("tipOp", String.valueOf(idOperatieComanda));
            params.put("codUser", fullCode);

            operatiiComenzi.opereazaComanda(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadListComenzi() {

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("filiala", UserInfo.getInstance().getUnitLog());
        params.put("codUser", UserInfo.getInstance().getCod());
        params.put("tipCmd", "1");
        params.put("depart", UserInfo.getInstance().getCodDepart());
        params.put("tipUser", UserInfo.getInstance().getTipUser());
        params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

        displayCmdDetails(false);
        operatiiComenzi.getListComenzi(params);

    }

    private void getArticoleComanda() {

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("nrCmd", selectedCmd);
        params.put("afisCond", "1");
        params.put("tipUser", UserInfo.getInstance().getTipUser());

        operatiiComenzi.getArticoleComandaJSON(params);

    }

    private void afiseazaArticoleComanda(BeanArticoleAfisare articoleComanda) {

        DateLivrareAfisare dateLivrare = articoleComanda.getDateLivrare();

        DateLivrare.getInstance().setDateLivrareAfisare(dateLivrare);
        DateLivrare.getInstance().setClientBlocat(articoleComanda.getDateLivrare().isClientBlocat());

        listArticoleComanda = articoleComanda.getListArticole();

        BeanConditii conditiiComanda = articoleComanda.getConditii();
        afisConditiiHeader(conditiiComanda.getHeader());

        conditieID = String.valueOf(conditiiComanda.getHeader().getId());

        conditiiComandaArticole = conditiiComanda.getArticole();

        if (isUserCV() || isComandaGed()) {
            ListaArticoleComandaGed.getInstance().setListaArticole(listArticoleComanda);
            ListaArticoleComandaGed.getInstance().addObserver(this);
            getPretTransport();
        } else {
            ListaArticoleModificareComanda.getInstance().setListaArticole(listArticoleComanda);
            ListaArticoleModificareComanda.getInstance().addObserver(this);
        }

        ListaArticoleComandaGed.getInstance().setConditiiArticole(conditiiComandaArticole);

        adapterArticole = new ArticolModificareAdapter(this, listArticoleComanda, conditiiComandaArticole, comandaSelectata);

        adapterArticole.setArticolModificareListener(this);
        listViewArticole.setAdapter(adapterArticole);

        textJudet.setText(dateLivrare.getNumeJudet());
        textOras.setText(dateLivrare.getOras());
        textAdrLivr.setText(dateLivrare.getDateLivrare());
        textPersContact.setText(dateLivrare.getPersContact());
        textTelefon.setText(dateLivrare.getNrTel());
        textCantar.setText(UtilsGeneral.getTipCantarire(dateLivrare.getCantar()));
        textTipPlata.setText(UtilsGeneral.getDescTipPlata(dateLivrare.getTipPlata(), dateLivrare.getTermenPlata()));
        textTransport.setText(UtilsGeneral.getDescTipTransport(dateLivrare.getTransport()));
        textFactRed.setText(UtilsGeneral.getTipReducere(dateLivrare.getRedSeparat()));

        tipClientVar = dateLivrare.getTipPersClient();

        calculPondereB();

        if (listArticoleComanda.get(0).getUnitLogAlt().equals("NN10")) {
            filialaAlternativaM = UserInfo.getInstance().getUnitLog();
        } else {
            filialaAlternativaM = listArticoleComanda.get(0).getUnitLogAlt();
        }

        if (listArticoleComanda.get(0).getUnitLogAlt().contains("BV9")) {
            layoutBV90.setVisibility(View.VISIBLE);
        } else {
            layoutBV90.setVisibility(View.GONE);
        }

        displayCmdDetails(true);

        adapterArticole.notifyDataSetChanged();

        calculValTransport(listArticoleComanda);
        calculProcente(listArticoleComanda);
    }

    private void getPretTransport() {

        for (int i = 0; i < listArticoleComanda.size(); i++) {
            if (listArticoleComanda.get(i).getNumeArticol().toLowerCase().contains("servicii")
                    && listArticoleComanda.get(i).getNumeArticol().toLowerCase().contains("transport")) {
                DateLivrare.getInstance().setValTransport(listArticoleComanda.get(i).getPretUnit());
                break;
            }

        }

    }

    private void afisConditiiHeader(BeanConditiiHeader conditiiHeader) {

        layoutConditiiHeader.setVisibility(View.GONE);
        boolean isHeaderVisible = false;
        NumberFormat nf2 = new DecimalFormat("#0.00");

        if (conditiiHeader.getConditiiCalit() > 0) {
            textCondProcB.setText(nf2.format(conditiiHeader.getConditiiCalit()));
            layoutCondProcB.setVisibility(View.VISIBLE);
            isHeaderVisible = true;
        } else {
            layoutCondProcB.setVisibility(View.GONE);
        }

        if (conditiiHeader.getNrFact() > 0) {
            textCondNrFacturi.setText(getDescNrFacturi(conditiiHeader.getNrFact()));
            layoutCondNrFact.setVisibility(View.VISIBLE);
            isHeaderVisible = true;
        } else {
            layoutCondNrFact.setVisibility(View.GONE);
        }

        if (!conditiiHeader.getObservatii().equals("null")) {
            textCondComentarii.setText(conditiiHeader.getObservatii());
            layoutCondObs.setVisibility(View.VISIBLE);
            isHeaderVisible = true;
        } else {
            layoutCondObs.setVisibility(View.GONE);
        }

        if (isHeaderVisible)
            layoutConditiiHeader.setVisibility(View.VISIBLE);

    }

    private String getDescNrFacturi(int codNrFacturi) {
        String descNrFacturi = "";
        switch (codNrFacturi) {
            case 1:
                descNrFacturi = "1 fact (red in pret)";
                codTipReducere = " ";
                break;
            case 2:
                descNrFacturi = "2 facturi";
                codTipReducere = "X";
                break;
            case 3:
                descNrFacturi = "1 fact (red separat)";
                codTipReducere = "R";
                break;

        }

        return descNrFacturi;

    }

    private void afiseazaListaComenzi(List<BeanComandaCreata> listComenzi) {

        if (listComenzi.size() > 0) {
            ComandaModificareAdapter adapter = new ComandaModificareAdapter(listComenzi, this);
            spinnerComenzi.setAdapter(adapter);
            displayComenziControl(true);

        } else {
            displayComenziControl(false);
        }

    }

    private void displayComenziControl(boolean isVisible) {
        if (isVisible) {
            spinnerComenzi.setVisibility(View.VISIBLE);
            salveazaComandaBtn.setVisibility(View.VISIBLE);
            stergeComandaBtn.setVisibility(View.VISIBLE);

            if (UtilsUser.isUserKA())
                aprobareBtn.setVisibility(View.VISIBLE);

        } else {
            spinnerComenzi.setVisibility(View.INVISIBLE);
            salveazaComandaBtn.setVisibility(View.INVISIBLE);
            stergeComandaBtn.setVisibility(View.INVISIBLE);
            aprobareBtn.setVisibility(View.INVISIBLE);

        }

    }

    public void addListenerListArtModif() {
        listViewArticole.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                listViewSelPos = position;
                return false;

            }
        });

        listViewArticole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                listViewSelPos = position;

                if ((listViewArticole.getFirstVisiblePosition() == listViewSelPos) || (listViewArticole.getFirstVisiblePosition() + 1 == listViewSelPos)) {
                    listViewArticole.smoothScrollToPositionFromTop(listViewSelPos - 1, 0);
                }

                if ((listViewArticole.getLastVisiblePosition() == listViewSelPos) || (listViewArticole.getLastVisiblePosition() - 1 == listViewSelPos)) {
                    listViewArticole.smoothScrollToPositionFromTop(listViewArticole.getFirstVisiblePosition() + 1, 0);
                }

            }
        });

    }

    private void checkStaticVars() {

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

    public double calculTaxaVerde() {

        double totalTaxaVerde = 0;

        try {

            String[] tokVal;
            Double valCondPret = 0.0;

            if (ModificareComanda.isComandaDistrib) {

                prepareArtForDelivery();

                for (int i = 0; i < objArticol.length; i++) {
                    if (objArticol[i] != null) {
                        if (!objArticol[i].getCodArticol().equals("-1")) {

                            if (objArticol[i].getInfoArticol().contains(";")) {
                                String[] tokInfoArt = objArticol[i].getInfoArticol().split(";");

                                for (int ii = 0; ii < tokInfoArt.length; ii++) {
                                    tokVal = tokInfoArt[ii].split(":");
                                    valCondPret = Double.valueOf(tokVal[1].replace(',', '.').trim());
                                    if (valCondPret != 0) {
                                        if (tokVal[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
                                            totalTaxaVerde += valCondPret;
                                        }

                                    }

                                }// for
                            }// if

                        }
                    }
                }// sf. for

                totalComanda = getTotalComanda();
                totalComanda += totalTaxaVerde;

                textTotalCmd.setText(String.format("%.02f", totalComanda));

                if (totalTaxaVerde > 0) {
                    layoutTaxaVerde.setVisibility(View.VISIBLE);
                    textTaxaVerde.setText(String.valueOf(totalTaxaVerde) + ")");
                }

            }// sf. if

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return totalTaxaVerde;

    }

    public void onBackPressed() {

        UserInfo.getInstance().setParentScreen("");
        clearAllData();

        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(nextScreen);

        finish();
        return;
    }

    public void onTaskComplete(String methodName, Object result) {

        if (methodName.equals("sendMailAlert")) {
            clearAllData();
        }

    }

    private void displayCmdDetails(boolean isVisible) {
        if (isVisible) {
            layoutDetaliiCmd.setVisibility(View.VISIBLE);
            listViewArticole.setVisibility(View.VISIBLE);
            stergeComandaBtn.setVisibility(View.VISIBLE);
            salveazaComandaBtn.setVisibility(View.VISIBLE);
        } else {
            layoutDetaliiCmd.setVisibility(View.INVISIBLE);
            listViewArticole.setVisibility(View.INVISIBLE);
            stergeComandaBtn.setVisibility(View.INVISIBLE);
            salveazaComandaBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void afiseazaArticoleComanda(BeanComandaCreata comanda) {

        textTipPlata.setText("");
        textAdrLivr.setText("");
        textPersContact.setText("");
        textTelefon.setText("");
        textCantar.setText("");
        textTransport.setText("");

        textTotalCmd.setText("0.00");

        comandaSelectata = comanda;

        unitLogComanda = comandaSelectata.getFiliala();

        selectedCmd = comanda.getId();

        selectedCmdSAP = comanda.getCmdSap();

        totalComanda = getTotalComanda();

        textTotalCmd.setText(String.format("%.02f", totalComanda));

        selectedClientCode = getCodClient(comanda);

        selectedUnitLog = comanda.getFiliala();

        if (selectedUnitLog.substring(2, 3).equals("1"))
            isComandaDistrib = true;
        else
            isComandaDistrib = false;

        if (!isComandaDistrib)
            DateLivrare.getInstance().setFilialeExtraMathaus("");

        codClientVar = comanda.getCodClient();
        numeClientVar = comanda.getNumeClient();

        if (!comanda.getDocInsotitor().equals("-1")) {
            DateLivrare.getInstance().setTipDocInsotitor(comanda.getDocInsotitor());
        } else {
            DateLivrare.getInstance().setTipDocInsotitor("1");
        }

        getArticoleComanda();

        textTipPlata.setVisibility(View.VISIBLE);
        textAdrLivr.setVisibility(View.VISIBLE);
        textPersContact.setVisibility(View.VISIBLE);
        textTelefon.setVisibility(View.VISIBLE);
        textCantar.setVisibility(View.VISIBLE);
        textTransport.setVisibility(View.VISIBLE);

    }

    private String getCodClient(BeanComandaCreata comanda) {

        return UtilsFormatting.isNumeric(comanda.getCodClient()) ? comanda.getCodClient() : comanda.getCodClientGenericGed();

    }

    private void addListenerSpinnerComenzi() {
        spinnerComenzi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayCmdDetails(false);
                afiseazaArticoleComanda(listComenzi.get(position));

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
        switch (methodName) {
            case GET_LIST_COMENZI:
                this.listComenzi = (List<BeanComandaCreata>) result;
                afiseazaListaComenzi(this.listComenzi);
                break;
            case GET_ARTICOLE_COMANDA_JSON:
                afiseazaArticoleComanda(operatiiComenzi.deserializeArticoleComanda((String) result));
                break;
            case OPERATIE_COMANDA:
                loadListComenzi();
                break;
            case SALVEAZA_COMANDA_DISTRIB:
                saveCmdStatus((String) result);
                break;
            case GET_COST_MACARA:
                afiseazaPretMacaraDialog((String) result);
                break;
            case GET_COST_MACARA_COMENZI:
                afiseazaPretMacaraComenziDialog((String) result);
                break;
            case GET_TOTAL_COMENZI_NUMERAR:
                afisTotalComenziNumerar((String) result);
                break;
            case GET_LIVRARI_MATHAUS:
                    setLivrariMathaus((String) result);
                break;
            case GET_OPTIUNI_MASINI:
                afisOptiuniMasini((String) result);
                break;
            default:
                break;
        }

    }

    public void articolModificat() {
        calculPondereB();
    }

    private void calculValTransport(ArrayList<ArticolComanda> listArticole) {

        valTranspBtn.setVisibility(View.GONE);
        valTransport = 0;

        if (UtilsUser.isAgentOrSDorKA() || UtilsUser.isConsWood() || comandaSelectata.isCmdInstPublica() || UtilsUser.isOIVPD()) {
            return;
        }


    }

    public boolean esteModificatPretulGed(ArrayList<ArticolComanda> listArticole) {
        boolean esteModificat = false;

        ArticolComanda articol = null;

        for (int i = 0; i < listArticole.size(); i++) {

            articol = listArticole.get(i);

            if (articol.getProcent() > 0) {
                esteModificat = true;
                break;
            }

        }

        if (!esteModificat) {
            AlgoritmComandaGed algoritm = new AlgoritmComandaGed();
            algoritm.inlaturaToateAlertelePret(listArticoleComanda);
        }

        return esteModificat;
    }

    public static double round(double value, int places) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    private void adaugaPalet(ArticolPalet articolPalet, EnumPaleti status) {

        String depozitPalet = HelperCostDescarcare.getDepozitPalet(ListaArticoleComanda.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());
        String unitlogPalet = HelperCostDescarcare.getUnitlogPalet(ListaArticoleComanda.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());

        ArticolComanda articol = HelperCostDescarcare.getArticolPalet(articolPalet, depozitPalet, unitlogPalet);
        ListaArticoleComanda.getInstance().addArticolLivrareComanda(articol);

        costDescarcare.getArticoleDescarcare().get(0).setCantitate(costDescarcare.getArticoleDescarcare().get(0).getCantitate() + articol.getCantitate());

    }

    private void respingePalet() {
        if (costDescarcare.getSePermite() && costDescarcare.getValoareDescarcare() > 0 && DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP")) {

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, isComandaGed());
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);

            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaFluxComanda();

        }
    }

    private void calculProcente(ArrayList<ArticolComanda> listArticole) {
        if (UtilsUser.isAgentOrSDorKA() || UtilsUser.isConsWood() || comandaSelectata.isCmdInstPublica()) {
            valTranspBtn.setVisibility(View.GONE);
            return;
        }

        textAlertaMarja.setVisibility(View.GONE);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        AlgoritmComandaGed algoritm = new AlgoritmComandaGed();
        algoritm.calculProcenteComanda(listArticole, esteModificatPretulGed(listArticole));

        double formulaTotalAdaosClientCorectat = algoritm.getTotalAdaosClientCorectat();
        double valTransportAlgoritm = valTransport - valTransportSAP;

        if (DateLivrare.getInstance().getTransport().equals("TRAP") || DateLivrare.getInstance().getTransport().equals("TERT")) {
            formulaTotalAdaosClientCorectat = algoritm.getTotalAdaosClientCorectat() + valTransportAlgoritm;
        }

        if (formulaTotalAdaosClientCorectat < algoritm.getTotalAdaosMinimReper()) {

            double deficitComanda = (algoritm.getTotalAdaosMinimReper() - formulaTotalAdaosClientCorectat);

            textAlertaMarja.setText("Cresteti val. cmd. cu minim " + nf.format(deficitComanda) + " RON");
            textAlertaMarja.setVisibility(View.VISIBLE);
            algoritm.redistribuireMarja(listArticole, valTransportAlgoritm);

        } else {
            algoritm.schimbaAlertaArticol(listArticole);
            textAlertaMarja.setText("");
        }
    }

    public void update(Observable observable, Object data) {
        if (observable instanceof ListaArticoleModificareComanda) {
            listArticoleComanda = ListaArticoleModificareComanda.getInstance().getListArticoleComanda();
            conditiiComandaArticole = ListaArticoleModificareComanda.getInstance().getConditiiArticole();
            adapterArticole.setListArticole(listArticoleComanda);
            adapterArticole.notifyDataSetChanged();

        }

        if (observable instanceof ListaArticoleComandaGed) {
            listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
            conditiiComandaArticole = ListaArticoleComandaGed.getInstance().getConditiiArticole();
            adapterArticole.setListArticole(listArticoleComanda);
            adapterArticole.notifyDataSetChanged();
            calculValTransport(listArticoleComanda);
            calculProcente(listArticoleComanda);
        }

    }

    @Override
    public void acceptaCostMacara(boolean acceptaCost, double valoareCost) {
        trateazaPretMacara(acceptaCost, valoareCost);

    }

    @Override
    public void articolSters() {

    }

    @Override
    public void articolSters(String codArticol, String filiala) {
        ListaArticoleComanda.getInstance().eliminaArticolLivrare(codArticol, filiala);
        calculProcente(listArticoleComanda);

    }

    @Override
    public void comandaEliminata() {

    }

    @Override
    public void comandaSalvata() {
        prepareArtForDelivery();
        saveComandaMathaus = true;
        performSaveCmd();
    }

    @Override
    public void redirectDateLivrare() {

    }

    @Override
    public void paletiStatus(EnumPaleti status, ArticolPalet palet) {
        switch (status) {
            case ACCEPTA:
                adaugaPalet(palet, status);
                break;
            case RESPINGE:
            case FINALIZEAZA:
                respingePalet();
                break;
            default:
                break;
        }

    }
}
