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
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import my.logon.screen.R;
import my.logon.screen.adapters.ArticoleCreareAdapter;
import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.ArticolTaxaVerde;
import my.logon.screen.beans.BeanArticolStoc;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.ComandaCalculDescarcare;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneCamion;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.beans.TranspComenzi;
import my.logon.screen.dialogs.ArtComplDialog;
import my.logon.screen.dialogs.CostMacaraDialog;
import my.logon.screen.dialogs.CostPaletiDialog;
import my.logon.screen.dialogs.PretTransportDialog;
import my.logon.screen.dialogs.RezumatComandaDialog;
import my.logon.screen.dialogs.SelectTipMasinaDialog;
import my.logon.screen.dialogs.TipComandaDistributieDialog;
import my.logon.screen.dialogs.ValoareNegociataDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumPaleti;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.helpers.HelperCostDescarcare;
import my.logon.screen.helpers.HelperCreareComanda;
import my.logon.screen.helpers.HelperDialog;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.listeners.ArtComplDialogListener;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.CnpDialogListener;
import my.logon.screen.listeners.ComandaMathausListener;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.CostMacaraListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.listeners.PaletAlertListener;
import my.logon.screen.listeners.PaletiListener;
import my.logon.screen.listeners.PretTransportDialogListener;
import my.logon.screen.listeners.TipCmdDistribListener;
import my.logon.screen.listeners.TipMasinaLivrareListener;
import my.logon.screen.listeners.ValoareNegociataDialogListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.Comanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsUser;

public class CreareComanda extends Activity implements AsyncTaskListener, ValoareNegociataDialogListener, ComenziDAOListener, PretTransportDialogListener,
        ArtComplDialogListener, Observer, PaletAlertListener, CostMacaraListener, TipCmdDistribListener, OperatiiArticolListener, PaletiListener,
        ComandaMathausListener, CnpDialogListener, TipMasinaLivrareListener {

    Button stocBtn, clientBtn, articoleBtn, livrareBtn, saveCmdBtn, slideButtonCmd;
    String filiala = "", nume = "", cod = "";
    String codClient = "", numeClient = "";

    private TextView textClient, textTotalCmd, textTipPlata, textAdrLivr, labelTotal, textCursValut;
    private TextView textPersContact, textTelefon, textCantar, textTransport, textNrFact, textLimCrd, textRestCrd, textPondereB, textTaxaVerde;
    private TextView textGreutateCmd;

    private int listViewSelPos = -1;

    private static ArrayList<HashMap<String, String>> arrayListArticole = new ArrayList<HashMap<String, String>>();

    public static String codClientVar = "";
    public static String numeClientVar = "";
    public static String tipClientVar = "";
    public static String articoleComanda = "";
    public static String numeDepart = "";
    public static String codDepart = "";
    public static String unitLog = "";
    public static double totalComanda = 0;
    public static double limitaCredit = 0;
    public static double restCredit = 0;
    public static double cursValutar = 0;
    public static String dateLivrare = "";
    public static String canalDistrib = "";
    public static String codJudet = "";
    public static String judet = "";
    public static String oras = "";
    public static String strada = "";
    public static String persContact = "";
    public static String telefon = "";
    public static String termenPlata = " ";
    public static boolean cmdAngajament = false;
    public static boolean isTotalNegociat = false;
    public static double valNegociat = 0;
    public static String depozitUnic = "";
    public static String filialaAlternativa = UserInfo.getInstance().getUnitLog();
    public static String cnpClient = "";

    public static String tipPlataContract = " ";

    String serializedResult;

    private boolean alertSD = false, alertDV = false, alertCredite = false;

    private String nrCmdGED = "";

    private String comandaFinalaStr = "", globalAlertSDKA = "", globalAlertDVKA = "", globalSubCmp = "0";
    private String articoleFinaleStr = "";
    private String clientFinalStr = "";
    private String comandaBlocata = "0";
    private ProgressBar mProgress;
    private Timer myTimer;
    private int progressVal = 0;
    private Handler logonHandler = new Handler();
    public static String tipAcces;
    private ListView listArtCmd;
    private SlidingDrawer slidingDrawerCmd;
    Dialog dialog, dlgTransp, dialogValNegociat;
    private String mailAlertTipDest = "";
    private LinearLayout layoutHeader, layoutTaxaVerde;
    private NumberFormat nf3;
    private List<ArticolComanda> listArticole = null;
    private String comandaJson;
    private Comanda comandaFinala = new Comanda();

    private ComenziDAO comandaDAO;
    private CostDescarcare costDescarcare;
    private TipCmdDistrib tipComandaDistributie = TipCmdDistrib.COMANDA_VANZARE;
    private TextView textFurnizor;
    private OperatiiArticol opArticol;
    public static String filialaCustodie = "";
    public static String filialaLivrareMathaus = UserInfo.getInstance().getFiliala();
    public static String filialeArondateMathaus = UserInfo.getInstance().getFiliala();
    private static boolean saveComandaMathaus = false;
    private LivrareMathaus livrareMathaus;
    private ComandaMathaus comandaMathausTCLI;
    private AntetCmdMathaus antetMathausTCLI;
    private boolean redirectDateLivrareTCLI = false;

    public static String tipComanda = "N"; // N = normala, S = simulata

    private RezumatComandaDialog rezumatComanda;
    private boolean isAfisOptiuniMasini = false;
    private List<OptiuneCamion> stareOptiuniCamion;

    public void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

            setTheme(R.style.LRTheme);
            setContentView(R.layout.crearecomandaheader);
            checkStaticVars();

            ListaArticoleComanda.getInstance().addObserver(this);

            comandaDAO = ComenziDAO.getInstance(this);
            comandaDAO.setComenziDAOListener(this);

            listArtCmd = (ListView) findViewById(R.id.listArtCmd);

            layoutHeader = (LinearLayout) findViewById(R.id.layoutHeader);
            layoutHeader.setVisibility(View.INVISIBLE);

            layoutTaxaVerde = (LinearLayout) findViewById(R.id.layoutTaxaVerde);
            layoutTaxaVerde.setVisibility(View.INVISIBLE);

            textTaxaVerde = (TextView) findViewById(R.id.textTaxaVerde);
            textTaxaVerde.setText("");

            listArtCmd.setClickable(true);
            addListenerListArtCmd();
            registerForContextMenu(listArtCmd);

            if (arrayListArticole.size() == 0) {
                listArtCmd.setVisibility(View.GONE);
            } else
                listArtCmd.setVisibility(View.VISIBLE);

            slidingDrawerCmd = (SlidingDrawer) findViewById(R.id.slidingDrawerCmd);
            this.slideButtonCmd = (Button) findViewById(R.id.slideButtonCmd);
            slidingDrawerCmd.setVisibility(View.GONE);
            addDrowerEvents();

            this.saveCmdBtn = (Button) findViewById(R.id.saveCmdBtn);
            addListenerSaveCmdBtn();

            ActionBar actionBar = getActionBar();
            actionBar.setTitle("Comanda distributie");
            actionBar.setDisplayHomeAsUpEnabled(true);

            textClient = (TextView) findViewById(R.id.textClient);
            textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);

            textGreutateCmd = (TextView) findViewById(R.id.textGreutateCmd);

            textTipPlata = (TextView) findViewById(R.id.textTipPlata);
            textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
            textPersContact = (TextView) findViewById(R.id.textPersContact);
            textTelefon = (TextView) findViewById(R.id.textTelefon);
            textCantar = (TextView) findViewById(R.id.textCantar);
            textTransport = (TextView) findViewById(R.id.textTransport);
            labelTotal = (TextView) findViewById(R.id.labelTotal);

            textNrFact = (TextView) findViewById(R.id.textNrFact);

            textPondereB = (TextView) findViewById(R.id.textPondereB);

            textLimCrd = (TextView) findViewById(R.id.textLimCrd);
            textRestCrd = (TextView) findViewById(R.id.textRestCrd);
            textCursValut = (TextView) findViewById(R.id.textCursValut);

            textClient.setVisibility(View.INVISIBLE);
            textTotalCmd.setVisibility(View.INVISIBLE);
            textGreutateCmd.setVisibility(View.INVISIBLE);
            textTipPlata.setVisibility(View.INVISIBLE);
            textAdrLivr.setVisibility(View.INVISIBLE);
            textPersContact.setVisibility(View.INVISIBLE);
            textTelefon.setVisibility(View.INVISIBLE);
            textCantar.setVisibility(View.INVISIBLE);
            textTransport.setVisibility(View.INVISIBLE);
            textNrFact.setVisibility(View.INVISIBLE);

            labelTotal.setVisibility(View.INVISIBLE);

            textLimCrd.setVisibility(View.INVISIBLE);
            textRestCrd.setVisibility(View.INVISIBLE);
            textCursValut.setVisibility(View.INVISIBLE);
            textPondereB.setVisibility(View.INVISIBLE);

            textClient.setText("");
            textTotalCmd.setText("");
            textGreutateCmd.setText("");
            textTipPlata.setText("");
            textAdrLivr.setText("");
            textPersContact.setText("");
            textTelefon.setText("");
            textCantar.setText("");
            textTransport.setText("");
            textNrFact.setText("");

            mProgress = (ProgressBar) findViewById(R.id.progress_bar_savecmd);
            mProgress.setVisibility(View.INVISIBLE);

            initLocale();
            nf3 = NumberFormat.getInstance();
            nf3.setMinimumFractionDigits(2);
            nf3.setMaximumFractionDigits(2);

            textFurnizor = (TextView) findViewById(R.id.textFurnizor);

            opArticol = new OperatiiArticolImpl(this);
            opArticol.setListener(this);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showTipComandaDialog() {
        TipComandaDistributieDialog tipCmdDialog = new TipComandaDistributieDialog(this);
        tipCmdDialog.setTipCmdDistribListener(this);
        tipCmdDialog.showDialog();
    }

    private void CreateMenu(Menu menu) {

        if (!UserInfo.getInstance().getTipUserSap().equals("INFO") && !UtilsUser.isSMR() && !UtilsUser.isCVR() && !UtilsUser.isSSCM() && !UtilsUser.isCGED()
                && !UtilsUser.isOIVPD()) {
            MenuItem mnu0 = menu.add(0, 0, 0, "Tip");

            mnu0.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            if (tipComandaDistributie == TipCmdDistrib.DISPOZITIE_LIVRARE || tipComandaDistributie == TipCmdDistrib.ARTICOLE_COMANDA) {
                MenuItem mnu1 = menu.add(0, 1, 1, "Furnizor");

                mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            }

        }

        MenuItem mnu2 = menu.add(0, 2, 2, "Client");

        mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu3 = menu.add(0, 3, 3, "Articole");

        mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu4 = menu.add(0, 4, 4, "Livrare");

        mnu4.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu5 = menu.add(0, 5, 5, "Valoare negociata");

        mnu5.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int nrArticole = ListaArticoleComanda.getInstance().getNrArticoleComanda();

        switch (item.getItemId()) {

            case 0:
                if (nrArticole == 0) {

                    showTipComandaDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case 1:
                if (nrArticole == 0) {

                    Intent nextScreen = new Intent(getApplicationContext(), SelectFurnizorCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case 2:
                if (nrArticole == 0) {

                    Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case 3:
                if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_VANZARE
                        || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE
                        || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE
                        || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA
                        || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_DETERIORATE) {

                    if (codClientVar.length() > 0) {

                        if (DateLivrare.getInstance().getTranspComenzi() != null && !DateLivrare.getInstance().getTranspComenzi().isEmpty())
                            return false;

                        if (nrArticole == 0 && DateLivrare.getInstance().getDataLivrare().length() == 0) {
                            showAlertAdresaLivrareDialog();
                        } else {
                            Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
                            startActivity(nextScreen);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                    }
                } else if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA) {
                    if (codClientVar.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Selectati clientul.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (DateLivrare.getInstance().getFurnizorComanda() == null) {
                        Toast.makeText(getApplicationContext(), "Selectati furnizorul.", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
                    startActivity(nextScreen);
                }

                return true;
            case 4:

                if (codClientVar.length() > 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case 5:

                if (CreareComanda.canalDistrib.equals("10")) {
                    if (nrArticole == 0) {
                        showValNegociatDialogBox();
                    } else {
                        if (!CreareComanda.isTotalNegociat) {
                            Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();

                        } else {
                            showValNegociatDialogBox();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Doar pe vanzarile din distributie!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case android.R.id.home:

                returnToHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertAdresaLivrareDialog() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Completati adresa de livrare");
        dlgAlert.setTitle("Atentie!");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void returnToHome() {
        if (numeClientVar.equals("")) {

            resetAllVars();
            UserInfo.getInstance().setParentScreen("");
            Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(nextScreen);
            finish();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Datele se vor pierde. Continuati?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    resetAllVars();
                    UserInfo.getInstance().setParentScreen("");

                    Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(nextScreen);
                    finish();
                }
            }).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).setTitle("Atentie!").setIcon(R.drawable.warning96);

            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    public void showValNegociatDialogBox() {
        ValoareNegociataDialog valDialog = new ValoareNegociataDialog(this);
        valDialog.setValoareNegociataListener(this);
        valDialog.showDialog(valNegociat, isTotalNegociat);

    }

    public void addDrowerEvents() {
        slidingDrawerCmd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {
                slideButtonCmd.setBackgroundResource(R.drawable.slideright32);
                listArtCmd.setEnabled(false);
            }
        });

        slidingDrawerCmd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                slideButtonCmd.setBackgroundResource(R.drawable.slideleft32);
                listArtCmd.setEnabled(true);
            }
        });

    }

    @Override
    public void onResume() {

        // !! Se modifica din 2 locuri, User si selectArtCmd
        if (!filialaAlternativa.equals("BV90"))
            filialaAlternativa = UserInfo.getInstance().getUnitLog();

        super.onResume();
        checkStaticVars();

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA) {
            if (DateLivrare.getInstance().getFurnizorComanda() != null) {

                String strFurnizor = "Furnizor: " + DateLivrare.getInstance().getFurnizorComanda().getNumeFurnizorMarfa();
                strFurnizor += " / " + DateLivrare.getInstance().getFurnizorComanda().getNumeFurnizorProduse();
                textFurnizor.setText(strFurnizor);
                textFurnizor.setVisibility(View.VISIBLE);
            }
        }

        if (numeClientVar.length() > 0) {
            int maxLen = numeClientVar.length();
            if (numeClientVar.length() > 70)
                maxLen = 70;

            textClient.setVisibility(View.VISIBLE);
            textClient.setText(numeClientVar.trim().substring(0, maxLen));
            clientFinalStr = codClientVar;

        } else {
            if (textClient != null)
                textClient.setText("Fara client");
        }

        String varUnitLog = UserInfo.getInstance().getUnitLog();

        if (CreareComanda.canalDistrib.equals("20"))
            varUnitLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        if (isLivrareCustodie() && !filialaCustodie.isEmpty())
            varUnitLog = filialaCustodie;

        if (DateLivrare.getInstance().getDateLivrare().length() > 0) {

            textTotalCmd.setVisibility(View.VISIBLE);
            textGreutateCmd.setVisibility(View.VISIBLE);
            textTipPlata.setVisibility(View.VISIBLE);
            textAdrLivr.setVisibility(View.VISIBLE);
            textPersContact.setVisibility(View.VISIBLE);
            textTelefon.setVisibility(View.VISIBLE);
            textCantar.setVisibility(View.VISIBLE);
            textTransport.setVisibility(View.VISIBLE);
            slidingDrawerCmd.setVisibility(View.VISIBLE);

            textNrFact.setVisibility(View.VISIBLE);
            textLimCrd.setVisibility(View.VISIBLE);
            textRestCrd.setVisibility(View.VISIBLE);
            textCursValut.setVisibility(View.VISIBLE);

            textLimCrd.setText("LC: " + nf3.format(limitaCredit));
            textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

            String[] tokenLivrare = DateLivrare.getInstance().getDateLivrare().split("#");
            String factRed = "NU";
            textAdrLivr.setText(tokenLivrare[0]);
            textPersContact.setText(tokenLivrare[1]);
            textTelefon.setText(tokenLivrare[2]);

            // ********* cantar
            if (tokenLivrare[3].equals("DA")) {
                textCantar.setText("Se cantareste");
                textCantar.setVisibility(View.VISIBLE);

            }
            if (tokenLivrare[3].equals("NU")) {
                textCantar.setVisibility(View.GONE);
            }
            // ********* sf. cantar

            // ********* tip plata
            String varTipPlata = "";

            if (tokenLivrare[4].equals("B")) {
                varTipPlata = "Bilet la ordin";
            }

            if (tokenLivrare[4].equals("C")) {
                varTipPlata = "Card";
            }

            if (tokenLivrare[4].equals("E")) {
                varTipPlata = "Plata in numerar";
            }

            if (tokenLivrare[4].equals("O")) {
                varTipPlata = "Ordin de plata";
            }

            textTipPlata.setText(varTipPlata);
            // ********* sf. tip plata

            // ********* tip transport
            String varTipTransp = "";
            if (tokenLivrare[5].equals("TRAP")) {
                varTipTransp = "Transport Arabesque";
            }

            if (tokenLivrare[5].equals("TCLI")) {
                varTipTransp = "Transport client";
            }

            textTransport.setText(varTipTransp);
            // ********* sf. tip transport

            textCursValut.setText("\u20ac: " + String.valueOf(CreareComanda.cursValutar));

            DateLivrare dateLivrareInstance = DateLivrare.getInstance();

            if (dateLivrareInstance.getRedSeparat().equals("X")) {
                textNrFact.setText("2 facturi");
            }
            if (dateLivrareInstance.getRedSeparat().equals("R")) {
                textNrFact.setText("1 factura (red. separat)");
            }
            if (dateLivrareInstance.getRedSeparat().equals(" ")) {
                textNrFact.setText("1 factura (red. in pret)");
            }

            clientFinalStr += "#" + dateLivrareInstance.getStrada() + "^" + dateLivrareInstance.getOras() + "^" + dateLivrareInstance.getCodJudet() + "#"
                    + tokenLivrare[1] + "#" + tokenLivrare[2] + "#" + tokenLivrare[3] + "#" + tokenLivrare[4].substring(0, 1).trim() + "#"
                    + tokenLivrare[5].substring(0, 4).trim() + "#" + String.valueOf(totalComanda) + "#" + varUnitLog + "#" + UserInfo.getInstance().getCod()
                    + "#" + factRed + "#";

            dateLivrareInstance.setPersContact(tokenLivrare[1]);
            dateLivrareInstance.setNrTel(tokenLivrare[2]);
            dateLivrareInstance.setCantar(tokenLivrare[3]);
            dateLivrareInstance.setTipPlata(tokenLivrare[4]);
            dateLivrareInstance.setTransport(tokenLivrare[5]);
            dateLivrareInstance.setTotalComanda(String.valueOf(totalComanda));
            dateLivrareInstance.setUnitLog(varUnitLog);
            dateLivrareInstance.setCodAgent(UserInfo.getInstance().getCod());
            dateLivrareInstance.setFactRed(factRed);


        }

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int menuItemIndex = item.getItemId();

        if (menuItemIndex == 0) // stergere
        {

            if (listViewSelPos >= 0) {
                // recalculare total

                ListaArticoleComanda.getInstance().removeArticolComanda(listViewSelPos);
                displayArticoleComanda();

                calculTotalComanda();
                listViewSelPos = -1;

                // pentru comenzile cu total negociat se calculeaza reducerile
                if (CreareComanda.isTotalNegociat) {
                    calculProcReducere();
                }

                // recalculare pondere articole B
                calculPondereB();

            }

        } else if (menuItemIndex == 1 && listViewSelPos >= 0) {
            Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
            nextScreen.putExtra("indexArticolModificat", String.valueOf(listViewSelPos));
            startActivity(nextScreen);

        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listArtCmd) {

            ListView listView = (ListView) v;
            AdapterContextMenuInfo adapterMenu = (AdapterContextMenuInfo) menuInfo;
            ArticolComanda articol = (ArticolComanda) listView.getItemAtPosition(adapterMenu.position);
            menu.setHeaderTitle(articol.getNumeArticol());
            menu.add(Menu.NONE, 0, 0, "Sterge");

            if (!isLivrareCustodie())
                menu.add(Menu.NONE, 1, 1, "Modifica pret/cantitate");

        }

    }

    // calcul pondere articole de tip B
    public void calculPondereB() {

        double procentB = ListaArticoleComanda.getInstance().calculPondereB();
        textPondereB.setText("Pondere art. B: " + String.format("%.02f", procentB) + "%");

    }

    public double calculTaxaVerde() {

        double totalTaxaVerde = 0;

        try {

            if (CreareComanda.canalDistrib.equals("10")) {

                totalTaxaVerde = ListaArticoleComanda.getInstance().calculTaxaVerde();

                totalComanda += totalTaxaVerde;
                textTotalCmd.setText(String.format("%.02f", totalComanda));

                textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComanda.getInstance().getGreutateKgArticole()) + " kg");

                if (totalTaxaVerde > 0) {
                    layoutTaxaVerde.setVisibility(View.VISIBLE);
                    textTaxaVerde.setText(String.format("%.02f", totalTaxaVerde) + ")");
                } else layoutTaxaVerde.setVisibility(View.INVISIBLE);

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return totalTaxaVerde;

    }

    // calcul procent reducere pentru comenzi cu valoare totala negociata
    public void calculProcReducere() {
        NumberFormat nf2 = new DecimalFormat("#0.00");

        ListaArticoleComanda.getInstance().calculProcReducere();
        displayArticoleComanda();

        totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();
        textTotalCmd.setText(nf2.format(totalComanda));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComanda.getInstance().getGreutateKgArticole()) + " kg");
        textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

    }

    public void addListenerSaveCmdBtn() {

        saveCmdBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                boolean goSaveCmd = false;

                try {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            int count = ListaArticoleComanda.getInstance().getNrArticoleComanda();

                            if (count == 0) {
                                Toast.makeText(getApplicationContext(), "Comanda nu contine articole!", Toast.LENGTH_SHORT).show();

                                slidingDrawerCmd.animateClose();
                            }
                            if (textAdrLivr.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Comanda nu contine datele de livrare!", Toast.LENGTH_SHORT).show();

                                slidingDrawerCmd.animateClose();
                            }



                            if (DateLivrare.getInstance().getTermenPlata().trim().equals("") && !isLivrareCustodie()) {
                                Toast.makeText(getApplicationContext(), "Verificati datele de livrare!", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (isCondPF10_000()) {
                                Toast.makeText(getApplicationContext(), "Valoarea comenzii este mai mare de 10000 RON.", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (isRestrictieCLP()) {
                                Toast.makeText(getApplicationContext(), "Nu se poate salva o comanda CLP cu articole din MAV, transport TCLI si plata in numerar.",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (count > 0 && !textAdrLivr.getText().toString().equals("")) {

                                if (CreareComanda.canalDistrib.equals("10")) {
                                    if (!cmdAngajament) {
                                        if (CreareComanda.restCredit >= CreareComanda.totalComanda) {
                                            goSaveCmd = true;
                                        } else {

                                            Toast.makeText(getApplicationContext(), "Limita de credit a fost depasita!", Toast.LENGTH_SHORT).show();

                                            // test
                                            if (UserInfo.getInstance().getTipUserSap().equals(Constants.tipInfoAv) || UtilsUser.isSMR() || UtilsUser.isCVR()
                                                    || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD())
                                                goSaveCmd = true;
                                            else
                                                goSaveCmd = true;

                                            alertCredite = true;

                                        }
                                    } else {
                                        goSaveCmd = true;
                                    }
                                } else {
                                    goSaveCmd = true;
                                }

                                if (isConditiiSolicitCnp() && CreareComanda.cnpClient.trim().length() == 0) {
                                    showCnpDialog();
                                    return true;
                                }

                                if (goSaveCmd) {
                                    mProgress.setVisibility(View.VISIBLE);
                                    mProgress.setProgress(0);
                                    progressVal = 0;
                                    myTimer = new Timer();
                                    myTimer.schedule(new UpdateProgress(), 40, 15);
                                }

                            }

                            return true;

                        case MotionEvent.ACTION_UP:
                            if (mProgress.getVisibility() == View.VISIBLE) {
                                myTimer.cancel();
                                mProgress.setVisibility(View.INVISIBLE);
                                return true;
                            }

                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

        });

    }

    private boolean isConditiiSolicitCnp() {

        if (!CreareComanda.tipClientVar.equals("PF"))
            return false;

        double valGreutateCmd = 0;
        double valFTvaCmd = 0;

        for (ArticolComanda articol : ListaArticoleComanda.getInstance().getListArticoleComanda()) {
            if (articol.getGreutate() > 0) {
                valGreutateCmd += articol.getGreutate();
                valFTvaCmd += articol.getPret();
            }
        }

        if (valGreutateCmd > Constants.MAX_GREUTATE_CNP || valFTvaCmd >= Constants.MAX_VALOARE_CNP)
            return true;

        return false;
    }

    private void showCnpDialog() {
        CnpDialog dialog = new CnpDialog(this);
        dialog.setCnpListener(CreareComanda.this);
        dialog.show();
    }

    @Override
    public void cnpSaved(String cnp) {
        CreareComanda.cnpClient = cnp;
        mProgress.setProgress(50);
        myTimer = new Timer();
        myTimer.schedule(new UpdateProgress(), 40, 15);

    }

    private boolean isCondPF10_000() {
        return CreareComanda.tipClientVar.equals("PF") && DateLivrare.getInstance().getTipPlata().equals("E") && totalComanda > 10000;
    }

    private boolean isLivrareCustodie() {
        return DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE;
    }

    private boolean isComandaACZC() {
        return DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA;
    }

    private boolean isRestrictieCLP() {

        boolean isCLP = DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE;

        boolean hasArtMAV = false;

        List<ArticolComanda> listaArticole = ListaArticoleComanda.getInstance().getListArticoleComanda();

        for (ArticolComanda articol : listaArticole) {
            if (articol.getDepozit().contains("MAV")) {
                hasArtMAV = true;
                break;
            }
        }

        boolean isTranspTCLI = DateLivrare.getInstance().getTransport().equals("TCLI");
        boolean isPlataNumerar = DateLivrare.getInstance().getTipPlata().equals("E");

        return isCLP && hasArtMAV && isTranspTCLI && isPlataNumerar;

    }

    private void trateazaLivrareCustodie() {
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress(0);
        progressVal = 0;
        myTimer = new Timer();
        myTimer.schedule(new UpdateProgress(), 40, 15);
    }

    private void salveazaLivrareCustodie() {

        comandaFinala.setCodClient(codClientVar);
        comandaFinala.setComandaBlocata(comandaBlocata);
        comandaFinala.setFilialaAlternativa(CreareComanda.filialaAlternativa);
        comandaFinala.setUserSite(UserInfo.getInstance().getUserSite());

        comandaJson = serializeComanda(comandaFinala);
        prepareArtForDelivery();
        articoleFinaleStr = serializedResult;

        performSaveLivrareCustodie();

    }

    private void performSaveLivrareCustodie() {
        try {

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("JSONArt", articoleFinaleStr);
            params.put("JSONComanda", comandaJson);
            params.put("JSONDateLivrare", serializeDateLivrare());

            comandaDAO.salveazaLivrareCustodie(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    class UpdateProgress extends TimerTask {
        public void run() {
            progressVal++;
            if (mProgress.getProgress() == 50) {
                logonHandler.post(new Runnable() {
                    public void run() {

                        DateLivrare dateLivrareInstance = DateLivrare.getInstance();

                        String cmdSAP = "-1"; // se foloseste doar la modificare

                        String alerteKA = globalAlertSDKA + "!" + globalAlertDVKA;

                        String localRedSeparat = dateLivrareInstance.getRedSeparat();

                        if (isReducere())
                            localRedSeparat = "X";

                        if (dateLivrareInstance.getObsPlata().equals("SO") && dateLivrareInstance.getTipPlata().equals("R")) {
                            if (!dateLivrareInstance.isValIncModif()) {
                                dateLivrareInstance.setValoareIncasare(nf3.format(CreareComanda.totalComanda * Constants.TVA));
                            }
                        }

                        String userSiteMail = " ", isValIncModif = " ", codJ = "", adrLivrareGED = "";

                        if (dateLivrareInstance.isValIncModif())
                            isValIncModif = "X";

                        comandaFinalaStr = clientFinalStr + comandaBlocata + "#" + cmdSAP + "#-1" + "#" + dateLivrareInstance.getTermenPlata() + "#"
                                + dateLivrareInstance.getObsLivrare() + "#" + dateLivrareInstance.getDataLivrare().toString() + "#"
                                + dateLivrareInstance.isAdrLivrNoua() + "#" + dateLivrareInstance.getTipDocInsotitor() + "#" + alerteKA + "#"
                                + dateLivrareInstance.getObsPlata() + "#" + dateLivrareInstance.getAddrNumber() + "#" + localRedSeparat + "#"
                                + CreareComanda.filialaAlternativa + "#" + dateLivrareInstance.getValoareIncasare() + "#"
                                + UserInfo.getInstance().getUserSite() + "#" + userSiteMail + "#" + isValIncModif + "#" + codJ + "#" + adrLivrareGED + "@"
                                + articoleFinaleStr;

                        comandaFinala.setCodClient(codClientVar);
                        comandaFinala.setComandaBlocata(comandaBlocata);
                        comandaFinala.setNrCmdSap(cmdSAP);
                        comandaFinala.setAlerteKA(alerteKA);
                        comandaFinala.setFactRedSeparat(localRedSeparat);
                        comandaFinala.setFilialaAlternativa(CreareComanda.filialaAlternativa);
                        comandaFinala.setUserSite(UserInfo.getInstance().getUserSite());
                        comandaFinala.setUserSiteMail(dateLivrareInstance.getMail());
                        comandaFinala.setIsValIncModif(isValIncModif);
                        comandaFinala.setCodJ(codJ);
                        comandaFinala.setAdresaLivrareGed(adrLivrareGED);
                        comandaFinala.setCnpClient(CreareComanda.cnpClient.length() == 0 ? " " : CreareComanda.cnpClient);

                        comandaJson = serializeComanda(comandaFinala);


                        if ((dateLivrareInstance.getTipPlata().equals("E") || dateLivrareInstance.getTipPlata().equals("N") || dateLivrareInstance.getTipPlata().equals("R")) && CreareComanda.tipClientVar.equals("PJ")) {
                            if (totalComanda > UserInfo.getInstance().getMaxNumerarPJuridica()) {
                                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de " + UserInfo.getInstance().getMaxNumerarPJuridica() + " RON!", Toast.LENGTH_SHORT).show();
                                return;
                            } else
                                getTotalComenziNumerar();
                        } else if (isGreutateMaximaComanda()) {
                            Toast.makeText(getApplicationContext(), Constants.MSG_MASA_MAXIMA_CMD, Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                            valideazaFinal();

                    }
                });

                myTimer.cancel();
            } else {
                mProgress.setProgress(progressVal);
            }

        }
    }

    private boolean isGreutateMaximaComanda(){

        if (1==1)
            return false;

        double greutateComanda = ListaArticoleComanda.getInstance().getGreutateKgArticole();

        if (greutateComanda > Constants.MAX_GREUTATE_CMD_KG && DateLivrare.getInstance().getTransport().equals("TRAP")){
            return true;
        }

        return false;
    }


    private void valideazaFinal() {

        if (HelperCreareComanda.isComandaAmbalaje(ListaArticoleComanda.getInstance().getListArticoleComanda())) {
            HelperDialog.showInfoDialog(CreareComanda.this, "Atentie!", "Comanda nu poate sa contina doar ambalaje.");

        } else if (HelperCreareComanda.isConditiiAlertaIndoire(ListaArticoleComanda.getInstance().getListArticoleComanda())) {
            HelperDialog.showInfoDialog(CreareComanda.this, "Atentie!", "Selectati tipul de prelucrare (indoire sau debitare).");
        } else {
            getLivrariMathaus();
        }

    }

    private void getTotalComenziNumerar() {

        HashMap<String, String> params = new HashMap<>();
        params.put("codClient", comandaFinala.getCodClient());
        params.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
        params.put("tipClient", "PJ");
        comandaDAO.getTotalComenziNumerar(params);

    }

    private void afisTotalComenziNumerar(String totalNumerar) {

        if (totalComanda + Double.valueOf(totalNumerar) > UserInfo.getInstance().getMaxNumerarPJuridica()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "\nLa acest client valoarea comenzilor cu plata in numerar livrate in data de " + DateLivrare.getInstance().getDataLivrare() + " depaseste " +
                            UserInfo.getInstance().getMaxNumerarPJuridica() + " de lei.\n\n" +
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
            valideazaFinal();


    }

    private void trateazaConditiiSuplimentare() {

        displayArtComplDialog();
    }

    private void afiseazaPretMacaraDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostMacara(result);

        if (!DateLivrare.getInstance().isClientFurnizor())
            verificaPaletiComanda(costDescarcare.getArticolePaleti());

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

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, false, false);
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);

            trateazaConditiiSuplimentare();
        }

    }


    private void afiseazaPretMacaraComenziDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostComenziMacara(result);

        if (!DateLivrare.getInstance().isClientFurnizor())
            verificaPaletiComanda(costDescarcare.getArticolePaleti());

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

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, false, false);
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);


            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaConditiiSuplimentare();
        }

    }

    private boolean isConditiiAfisOptiuniMasini() {
        return DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getDatePoligonLivrare() != null && isConditiiCostTransport();
    }

    private void verificaPaletiComanda(List<ArticolPalet> listPaleti) {

        Iterator<ArticolComanda> articolIterator = ListaArticoleComanda.getInstance().getListArticoleComanda().iterator();

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
            }

        }

        Iterator<ArticolPalet> paletIterator = listPaleti.iterator();

        for (ArticolComanda articol : ListaArticoleComanda.getInstance().getListArticoleComanda()) {

            while (paletIterator.hasNext()) {
                ArticolPalet palet = paletIterator.next();

                if (palet.getCodPalet().equals(articol.getCodArticol()) && (palet.getCantitate() == (int) articol.getCantitate())) {
                    paletIterator.remove();
                }

            }

            paletIterator = listPaleti.iterator();

        }

        ArticoleCreareAdapter adapterArticole = new ArticoleCreareAdapter(new ArrayList<ArticolComanda>(), this);
        adapterArticole.setListArticole(ListaArticoleComanda.getInstance().getListArticoleComanda());
        listArtCmd.setAdapter(adapterArticole);
        adapterArticole.notifyDataSetChanged();

    }

    private void trateazaPretMacara(boolean acceptaPret, double valoarePret) {

        if (acceptaPret) {

            DateLivrare.getInstance().setMasinaMacara(true);

            List<ArticolComanda> articoleDescarcare = HelperCostDescarcare.getArticoleDescarcareDistrib(costDescarcare, valoarePret, ListaArticoleComanda.getInstance().getListArticoleLivrare());

            ListaArticoleComanda.getInstance().getListArticoleLivrare().addAll(articoleDescarcare);

            prepareArtForDelivery();

            articoleFinaleStr = serializedResult;
        } else {
            DateLivrare.getInstance().setMasinaMacara(false);
        }


        if (isConditiiAfisOptiuniMasini())
            getOptiuniMasini();
        else
            trateazaConditiiSuplimentare();

    }


    private void verificaPretMacaraRezumat() {


        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComanda.getInstance().getListArticoleComanda());
        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComanda.getInstance().getListArticoleLivrare());

        if (!UtilsUser.isAV_SD_01()) {

            String codFurnizor = " ";

            if (tipComandaDistributie == TipCmdDistrib.DISPOZITIE_LIVRARE || tipComandaDistributie == TipCmdDistrib.ARTICOLE_COMANDA)
                codFurnizor = DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa();
            else if (tipComandaDistributie == TipCmdDistrib.COMANDA_LIVRARE)
                codFurnizor = DateLivrare.getInstance().getCodFilialaCLP();


            List<RezumatComanda> listComenziRezumat = HelperMathaus.getRezumatComanda(ListaArticoleComanda.getInstance().getListArticoleLivrare());
            List<ComandaCalculDescarcare> listComenziDescarcare = HelperCostDescarcare.getComenziCalculDescarcare(listComenziRezumat);

            String comenziSer = comandaDAO.serializeCalcComenziMacara(listComenziDescarcare);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("codAgent", DateLivrare.getInstance().getCodAgent());
            params.put("codClient", comandaFinala.getCodClient());
            params.put("codFurnizor", codFurnizor);
            params.put("listComenzi", comenziSer);
            params.put("canal", "10");
            params.put("isCustodie",String.valueOf(tipComandaDistributie == TipCmdDistrib.LIVRARE_CUSTODIE));

            comandaDAO.getCostMacaraComenzi(params);
        } else {

            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaConditiiSuplimentare();
        }

    }

    private boolean isReducere() {
        return globalSubCmp.equals("1") && !UserInfo.getInstance().getCodDepart().equals("07") && !UserInfo.getInstance().getCodDepart().equals("04");
    }

    private void verificaStareComanda() {

        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
            if (DateLivrare.getInstance().isAdrLivrNoua())
                comandaBlocata = "1";
        }

        comandaFinala.setComandaBlocata(comandaBlocata);
        comandaJson = serializeComanda(comandaFinala);
    }

    private void performSaveCmd() {
        try {

            if (isLivrareCustodie()) {
                salveazaLivrareCustodie();
                return;
            }

            verificaStareComanda();

            String tipUser = "AV";
            HashMap<String, String> params = new HashMap<String, String>();

            if (UserInfo.getInstance().getTipAcces().equals("27") || UtilsUser.isUserSDKA())
                tipUser = "KA";

            params.put("comanda", comandaFinalaStr);
            params.put("alertSD", String.valueOf(alertSD));
            params.put("alertDV", String.valueOf(alertDV));
            params.put("cmdAngajament", String.valueOf(cmdAngajament));
            params.put("tipUser", tipUser);
            params.put("JSONArt", articoleFinaleStr);
            params.put("JSONComanda", comandaJson);
            params.put("JSONDateLivrare", serializeDateLivrare());
            params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
            params.put("idCmdAmob", "-1");

            ComenziDAO comanda = ComenziDAO.getInstance(this);
            comanda.setComenziDAOListener(this);
            comanda.salveazaComandaDistrib(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private String prepareArtForDelivery() {
        String retVal = "";

        listArticole = new ArrayList<ArticolComanda>();
        ArticolComanda articol = null;

        try {

            comandaBlocata = "0";
            alertSD = false;
            alertDV = false;
            alertCredite = false;
            globalAlertSDKA = "";
            globalAlertDVKA = "";
            globalSubCmp = "0";


            String tokPretArticol, tokCantArticol, tokCodArticol, tokDepozArticol, tokProcent, tokUM, tokProcentFact, tokDiscClient, tokProcAprob, tokMultiplu, tokValArticol, tokInfoArticol, tokCantUmb, tokUmb;
            String[] tokAlteValori;
            String tokCond = " ", varAlteValori = " ";
            String[] tokPret;
            double valCondPret = 0;
            String localCodDepart = "";
            String filialaTaxaVerde = "";
            String depozitTaxaVerde = "";
            String departTaxaVerde = "";
            String tipTranspTaxaVerde = "";


            List<ArticolComanda> listaArticole = ListaArticoleComanda.getInstance().getListArticoleLivrare();
            List<ArticolTaxaVerde> listArticoleTVerde = new ArrayList<>();

            // sortare articole dupa departament
            Collections.sort(listaArticole, ArticolComanda.DepartComparator);

            Iterator<ArticolComanda> iterator = listaArticole.iterator();

            while (iterator.hasNext()) {
                articol = iterator.next();
                tokPretArticol = String.valueOf(articol.getPretUnit());
                tokCantArticol = String.valueOf(articol.getCantitate());
                tokCodArticol = articol.getCodArticol();
                tokDepozArticol = articol.getDepozit();
                tokProcent = String.valueOf(articol.getProcent());
                tokUM = articol.getUm();
                tokProcentFact = String.valueOf(articol.getProcentFact());
                tokDiscClient = String.valueOf(articol.getDiscClient());
                tokProcAprob = String.valueOf(articol.getProcAprob());
                tokMultiplu = String.valueOf(articol.getMultiplu());
                tokValArticol = String.valueOf(articol.getPret());
                tokInfoArticol = articol.getInfoArticol();

                // calcul taxa verde
                if (tokInfoArticol.contains(";")) {
                    String[] condPret = tokInfoArticol.split(";");

                    for (int ii = 0; ii < condPret.length; ii++) {
                        tokPret = condPret[ii].split(":");
                        valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
                        if (valCondPret != 0) {
                            if (tokPret[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
                                ArticolTaxaVerde artVerde = new ArticolTaxaVerde();
                                artVerde.setValoare(valCondPret);
                                artVerde.setFiliala(articol.getFilialaSite());
                                artVerde.setDepozit(articol.getDepozit());
                                artVerde.setDepart(articol.getDepart());
                                artVerde.setTipTransp(articol.getTipTransport());
                                listArticoleTVerde.add(artVerde);
                            }

                        }
                    }
                }
                // sf. taxa verde

                tokCantUmb = String.valueOf(articol.getCantUmb());
                tokUmb = articol.getUmb();

                if (articol.getAlteValori() == null) {
                    retVal = "";
                    break;
                }

                varAlteValori = articol.getAlteValori();

                if (varAlteValori.contains("!")) {
                    tokAlteValori = varAlteValori.split("!");
                    if (tokAlteValori[6].equals("1"))
                        globalSubCmp = "1";
                }

                tokCond = " ";
                if (articol.getConditie())
                    tokCond = "X";

                if (isUserExceptie()) {
                    if (articol.getObservatii() != null) {

                        localCodDepart = articol.getDepart();

                        if (isArtGedExceptie(articol)) {
                            localCodDepart = articol.getDepartAprob();

                        }

                        if (articol.getObservatii().contains("SD")) {
                            comandaBlocata = "1";
                            alertSD = true;

                            if (!globalAlertSDKA.contains(localCodDepart))
                                globalAlertSDKA += localCodDepart + "?";

                        }

                        if (articol.getObservatii().contains("DV")) {
                            comandaBlocata = "1";
                            alertDV = true;

                            if (!globalAlertDVKA.contains(localCodDepart))

                                globalAlertDVKA += localCodDepart + "?";
                        }
                    }

                }

                ArticolComanda articolCmd = new ArticolComanda();
                articolCmd.setCodArticol(tokCodArticol);
                articolCmd.setCantitate(Double.valueOf(tokCantArticol));
                articolCmd.setDepozit(tokDepozArticol);
                articolCmd.setPretUnit(Double.valueOf(tokPretArticol));
                articolCmd.setProcent(Double.valueOf(tokProcent));
                articolCmd.setUm(tokUM);
                articolCmd.setProcentFact(Double.valueOf(tokProcentFact));
                articolCmd.setConditie(Boolean.valueOf(tokCond));
                articolCmd.setDiscClient(Double.valueOf(tokDiscClient));
                articolCmd.setProcAprob(Double.valueOf(tokProcAprob));
                articolCmd.setMultiplu(Double.valueOf(tokMultiplu));
                articolCmd.setPret(Double.valueOf(tokValArticol));
                articolCmd.setInfoArticol(tokInfoArticol);
                articolCmd.setCantUmb(Double.valueOf(tokCantUmb));
                articolCmd.setUmb(tokUmb);
                articolCmd.setDepart(articol.getDepart());
                articolCmd.setDepartAprob(articol.getDepartAprob());
                articolCmd.setIstoricPret(articol.getIstoricPret());
                articolCmd.setFilialaSite(articol.getFilialaSite());
                articolCmd.setDataExpPret(articol.getDataExpPret());
                articolCmd.setListCabluri(articol.getListCabluri());
                articolCmd.setTipTransport(articol.getTipTransport());
                articolCmd.setGreutate(articol.getGreutate());
                articolCmd.setGreutateBruta(articol.getGreutateBruta());
                articolCmd.setCantitateInit(articol.getCantitateInit());

                articolCmd.setCantitate50(articol.getCantitate50());
                articolCmd.setUm50(articol.getUm50());

                articolCmd.setCmpCorectat(articol.getCmpCorectat());
                articolCmd.setPretMinim(articol.getPretMinim());

                if (isArtGedExceptie(articol))
                    articolCmd.setObservatii(articol.getObservatii());

                listArticole.add(articolCmd);

                retVal += tokCodArticol + "#" + tokCantArticol + "#" + tokDepozArticol + "#" + tokPretArticol + "#" + tokProcent + "#" + tokUM + "#"
                        + tokProcentFact + "#" + tokCond + "#" + tokDiscClient + "#" + tokProcAprob + "#" + tokMultiplu + "#" + tokValArticol + "#"
                        + tokInfoArticol + "#" + tokCantUmb + "#" + tokUmb + "@";

            }

            // adaugare material taxa verde
            if (CreareComanda.canalDistrib.equals("10")) {

                if (!listArticoleTVerde.isEmpty()) {

                    List<ArticolTaxaVerde> articoleVerde = HelperCreareComanda.getArticoleTVerde(listArticoleTVerde);

                    for (ArticolTaxaVerde artVerde : articoleVerde) {
                        ArticolComanda articolCmd = new ArticolComanda();
                        articolCmd.setCodArticol("000000000000000000");
                        articolCmd.setCantitate(1.0);
                        articolCmd.setDepozit(artVerde.getDepozit());
                        articolCmd.setPretUnit(artVerde.getValoare());
                        articolCmd.setProcent(0);
                        articolCmd.setUm("BUC");
                        articolCmd.setProcentFact(0);
                        articolCmd.setConditie(false);
                        articolCmd.setDiscClient(0);
                        articolCmd.setProcAprob(0);
                        articolCmd.setMultiplu(1);
                        articolCmd.setPret(artVerde.getValoare());
                        articolCmd.setInfoArticol(" ");
                        articolCmd.setCantUmb(1);
                        articolCmd.setUmb("BUC");
                        articolCmd.setDepart(artVerde.getDepart());
                        articolCmd.setObservatii("");
                        articolCmd.setIstoricPret("");
                        articolCmd.setTipTransport(artVerde.getTipTransp());
                        articolCmd.setFilialaSite(artVerde.getFiliala());
                        listArticole.add(articolCmd);
                    }
                }
            }

            if (CreareComanda.canalDistrib.equals("20")) {
                alertSD = false;
                alertDV = false;
                alertCredite = false;
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        serializeArticole();

        return retVal;
    }

    // userul este agent, sd sau ka
    boolean isUserExceptie() {
        return UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
                || UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("62")
                || UserInfo.getInstance().getTipAcces().equals("32");
    }

    private boolean isArtGedExceptie(ArticolComanda articolComanda) {
        return articolComanda.getDepozit().equals("MAV1") && articolComanda.getDepart().equals("11") && !articolComanda.getDepartAprob().equals("00");
    }

    private String serializeArticole() {
        JSONArray myArray = new JSONArray();

        JSONObject obj = null;

        try {
            for (int i = 0; i < listArticole.size(); i++) {
                obj = new JSONObject();
                obj.put("codArticol", listArticole.get(i).getCodArticol());
                obj.put("cantitate", listArticole.get(i).getCantitate());
                obj.put("depozit", listArticole.get(i).getDepozit());
                obj.put("pretUnit", listArticole.get(i).getPretUnit());
                obj.put("procent", listArticole.get(i).getProcent());
                obj.put("um", listArticole.get(i).getUm());
                obj.put("procentFact", listArticole.get(i).getProcentFact());
                obj.put("conditie", listArticole.get(i).getConditie());
                obj.put("discClient", listArticole.get(i).getDiscClient());
                obj.put("procAprob", listArticole.get(i).getProcAprob());
                obj.put("multiplu", listArticole.get(i).getMultiplu());
                obj.put("pret", listArticole.get(i).getPret());
                obj.put("infoArticol", listArticole.get(i).getInfoArticol());
                obj.put("cantUmb", listArticole.get(i).getCantUmb());
                obj.put("Umb", listArticole.get(i).getUmb());
                obj.put("depart", listArticole.get(i).getDepart());
                obj.put("observatii", listArticole.get(i).getObservatii());
                obj.put("departAprob", listArticole.get(i).getDepartAprob());
                obj.put("istoricPret", listArticole.get(i).getIstoricPret());
                obj.put("filialaSite", listArticole.get(i).getFilialaSite());
                obj.put("valTransport", "0");
                obj.put("procTransport", "0");
                obj.put("dataExp", listArticole.get(i).getDataExpPret());
                obj.put("listCabluri", opArticol.serializeCabluri05(listArticole.get(i).getListCabluri()));
                obj.put("tipTransport", listArticole.get(i).getTipTransport());
                obj.put("greutate", listArticole.get(i).getGreutate());
                obj.put("greutateBruta", listArticole.get(i).getGreutateBruta());
                obj.put("cantitateInit", listArticole.get(i).getCantitateInit());

                obj.put("cantitate50", listArticole.get(i).getCantitate50());
                obj.put("um50", listArticole.get(i).getUm50());

                obj.put("cmpCorectat", listArticole.get(i).getCmpCorectat());
                obj.put("pretMinim", listArticole.get(i).getPretMinim());

                myArray.put(obj);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        serializedResult = myArray.toString();

        return serializedResult;

    }

    private String serializeComanda(Comanda comanda) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("codClient", comanda.getCodClient());
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
            obj.put("adresaLivrareGed", comanda.getAdresaLivrareGed());
            obj.put("adresaLivrare", comanda.getAdresaLivrare());
            obj.put("valoareIncasare", comanda.getValoareIncasare());
            obj.put("conditieID", comanda.getConditieID());
            obj.put("canalDistrib", CreareComanda.canalDistrib);
            obj.put("valTransportSap", "0");
            obj.put("nrDocumentClp", comanda.getNrDocumentClp());
            obj.put("cnpClient", comanda.getCnpClient());

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
            obj.put("Strada", DateLivrare.getInstance().getStrada() + " ");
            obj.put("persContact", DateLivrare.getInstance().getPersContact());
            obj.put("nrTel", DateLivrare.getInstance().getNrTel());
            obj.put("redSeparat", DateLivrare.getInstance().getRedSeparat());
            obj.put("Cantar", DateLivrare.getInstance().getCantar());
            obj.put("tipPlata", UtilsComenzi.getTipPlataClient(DateLivrare.getInstance().getTipPlata(), CreareComanda.tipPlataContract));
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
            obj.put("totalComanda", ListaArticoleComanda.getInstance().getTotalComanda());
            obj.put("unitLog", DateLivrare.getInstance().getUnitLog());
            obj.put("codAgent", DateLivrare.getInstance().getCodAgent());
            obj.put("factRed", DateLivrare.getInstance().getFactRed());
            obj.put("macara", DateLivrare.getInstance().isMasinaMacara() ? "X" : " ");
            obj.put("idObiectiv", DateLivrare.getInstance().getIdObiectiv());
            obj.put("isAdresaObiectiv", DateLivrare.getInstance().isAdresaObiectiv());
            obj.put("coordonateGps", getCoordAdresa());
            HelperMathaus.setTonajComanda();
            obj.put("tonaj", DateLivrare.getInstance().getTonaj());
            obj.put("prelucrare", DateLivrare.getInstance().getPrelucrare());
            obj.put("clientRaft", DateLivrare.getInstance().isClientRaft());
            obj.put("factPaletiSeparat", DateLivrare.getInstance().isFactPaletSeparat());

            String codFurnizorMarfa = DateLivrare.getInstance().getFurnizorComanda() == null ? " " : DateLivrare.getInstance().getFurnizorComanda()
                    .getCodFurnizorMarfa();
            obj.put("furnizorMarfa", codFurnizorMarfa);

            String codFurnizorProduse = DateLivrare.getInstance().getFurnizorComanda() == null ? " " : DateLivrare.getInstance().getFurnizorComanda()
                    .getCodFurnizorProduse();
            obj.put("furnizorProduse", codFurnizorProduse);

            obj.put("isCamionDescoperit", DateLivrare.getInstance().isCamionDescoperit());
            obj.put("codSuperAgent", UserInfo.getInstance().getCodSuperUser());
            obj.put("programLivrare", DateLivrare.getInstance().getProgramLivrare());
            obj.put("livrareSambata", DateLivrare.getInstance().getLivrareSambata());
            obj.put("blocScara", DateLivrare.getInstance().getBlocScara());
            obj.put("filialaCLP", DateLivrare.getInstance().getCodFilialaCLP());
            obj.put("numeDelegat", DateLivrare.getInstance().getDelegat().getNume());
            obj.put("ciDelegat", DateLivrare.getInstance().getDelegat().getSerieNumarCI());
            obj.put("autoDelegat", DateLivrare.getInstance().getDelegat().getNrAuto());
            obj.put("refClient", DateLivrare.getInstance().getRefClient());
            obj.put("costTransportMathaus", opArticol.serializeCostTransportMathaus(DateLivrare.getInstance().getCostTransportMathaus()));
            obj.put("isComandaACZC", isComandaACZC());
            obj.put("prelucrareLemn", DateLivrare.getInstance().getPrelucrareLemn());
            obj.put("filialaPlata", DateLivrare.getInstance().getFilialaPlata());
            obj.put("codPostal", DateLivrare.getInstance().getCodPostal());
            obj.put("isComandaCustodie", DateLivrare.getInstance().isComandaCustodie());
            obj.put("taxeComanda", opArticol.serializeTaxeComanda(DateLivrare.getInstance().getTaxeComanda()));

        } catch (JSONException ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return obj.toString();

    }

    private String getCoordAdresa() {
        if (DateLivrare.getInstance().getCoordonateAdresa() != null)
            return DateLivrare.getInstance().getCoordonateAdresa().latitude + "#" + DateLivrare.getInstance().getCoordonateAdresa().longitude;
        else
            return "0#0";
    }

    public void addListenerClientBtn() {
        clientBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ListaArticoleComanda.getInstance().getNrArticoleComanda() == 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addListenerArticoleBtn() {
        articoleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (codClientVar.length() > 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addListenerLivrareBtn() {
        livrareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (codClientVar.length() > 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void calculTotalComanda() {

        totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();
        textTotalCmd.setText(String.format("%.02f", totalComanda));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComanda.getInstance().getGreutateKgArticole()) + " kg");
        textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

    }

    private boolean isComandaDL_TRAP() {
        return tipComandaDistributie.equals(TipCmdDistrib.DISPOZITIE_LIVRARE) && DateLivrare.getInstance().getTransport().equals("TRAP");
    }

    private boolean isConditiiCostTransport() {
        return DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_LIVRARE) || isComandaDL_TRAP() ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE) ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.LIVRARE_CUSTODIE);
    }

    private void setLivrariMathaus(String result) {

        livrareMathaus = opArticol.deserializeLivrareMathaus(result);

        if (isConditiiCostTransport())
            DateLivrare.getInstance().setCostTransportMathaus(livrareMathaus.getCostTransport());

        if (isAfisOptiuniMasini) {
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "10", null);
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
            isAfisOptiuniMasini = false;
            return;
        }


        List<DateArticolMathaus> articoleMathaus = livrareMathaus.getComandaMathaus().getDeliveryEntryDataList();
        List<ArticolComanda> articoleComandaDistrib = ListaArticoleComanda.getInstance().getListArticoleComanda();
        ListaArticoleComanda.getInstance().reseteazaArticoleLivrare();


        String codArticolComanda;
        for (ArticolComanda articolComanda : articoleComandaDistrib) {

            articolComanda.setTipTransport(null);

            if (tipComandaDistributie.equals(TipCmdDistrib.DISPOZITIE_LIVRARE))
                articolComanda.setTipTransport(DateLivrare.getInstance().getTransport());

            if ((tipComandaDistributie.equals(TipCmdDistrib.COMANDA_LIVRARE) || UtilsComenzi.isComandaClp()) && articolComanda.getArticolMathaus() == null)
                articolComanda.setFilialaSite(DateLivrare.getInstance().getCodFilialaCLP());


            codArticolComanda = articolComanda.getCodArticol();

            if (articolComanda.getCodArticol().length() == 8 || !Character.isDigit(codArticolComanda.charAt(0)))
                codArticolComanda = "0000000000" + articolComanda.getCodArticol();

            for (DateArticolMathaus articolMathaus : articoleMathaus) {

                if (codArticolComanda.equals(articolMathaus.getProductCode())) {

                    ArticolComanda articolLivrare = ListaArticoleComanda.getInstance().genereazaArticolLivrare(articolComanda);
                    articolLivrare.setCantitate(articolMathaus.getQuantity());
                    articolLivrare.setCmpCorectat(articolMathaus.getCmpCorectat());
                    articolLivrare.setPret((articolComanda.getPret() / articolComanda.getCantitate()) * articolMathaus.getQuantity());

                    articolLivrare.setCantitate50(HelperMathaus.getCantitateCanal50(articolMathaus, articolComanda));
                    articolLivrare.setUm50(articolComanda.getUm50());

                    if (HelperMathaus.isConditiiDepozitTCLI(articolComanda, "10") && articolMathaus.getCantUmb() == 0) {
                        double cantUmbTCLI = (articolMathaus.getQuantity() * articolComanda.getCantUmb()) / articolComanda.getCantitate();
                        articolLivrare.setCantUmb(cantUmbTCLI);
                    }

                    if (articolMathaus.getCantUmb() > 0)
                        articolLivrare.setCantUmb(articolMathaus.getCantUmb());

                    if (articolComanda.getFilialaSite().equals("BV90")) {
                    } else if (articolComanda.getArticolMathaus() != null && !articolComanda.getArticolMathaus().getTip2().equals("S")
                            && !DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty()) {
                        articolLivrare.setFilialaSite(DateLivrare.getInstance().getCodFilialaFasonate());
                    } else {
                        articolLivrare.setFilialaSite(articolMathaus.getDeliveryWarehouse());
                    }

                    if (!tipComandaDistributie.equals(TipCmdDistrib.DISPOZITIE_LIVRARE))
                        articolLivrare.setDepozit(articolMathaus.getDepozit());

                    ListaArticoleComanda.getInstance().getListArticoleLivrare().add(articolLivrare);
                }

            }

        }

        if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_LIVRARE) ||
                isComandaDL_TRAP() || DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE) ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.LIVRARE_CUSTODIE)
        )
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "10", null);


        prepareArtForDelivery();
        articoleFinaleStr = serializedResult;

        if (!saveComandaMathaus) {
            verificaPretMacaraRezumat();
        } else
            performSaveCmd();

    }

    private void copyLivrareMathaus(AntetCmdMathaus antetCmdMathaus, ComandaMathaus comandaMathaus) {
        comandaMathausTCLI = new ComandaMathaus();
        antetMathausTCLI = new AntetCmdMathaus();

        antetMathausTCLI.setLocalitate(antetCmdMathaus.getLocalitate());
        antetMathausTCLI.setCodJudet(antetCmdMathaus.getCodJudet());
        antetMathausTCLI.setCodClient(antetCmdMathaus.getCodClient());
        antetMathausTCLI.setTipPers(antetCmdMathaus.getTipPers());
        antetMathausTCLI.setDepart(antetCmdMathaus.getDepart());
        antetMathausTCLI.setCodPers(antetCmdMathaus.getCodPers());
        antetMathausTCLI.setTipTransp("TRAP");

        antetMathausTCLI.setCamionDescoperit(DateLivrare.getInstance().isCamionDescoperit());
        antetMathausTCLI.setMacara(DateLivrare.getInstance().isMasinaMacara());
        antetMathausTCLI.setOptiuniCamion(stareOptiuniCamion);
        antetMathausTCLI.setGreutateComanda(ListaArticoleComanda.getInstance().getGreutateKgArticole());
        antetMathausTCLI.setTipComandaCamion(ListaArticoleComanda.getInstance().isComandaEnergofaga() ? "ENERGOFAGA" : "NORMALA");

        List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

        for (DateArticolMathaus dateArticolMathaus : comandaMathaus.getDeliveryEntryDataList()) {
            DateArticolMathaus dateArticol = new DateArticolMathaus();
            dateArticol.setProductCode("0000000000" + dateArticolMathaus.getProductCode());
            dateArticol.setQuantity(dateArticolMathaus.getQuantity());
            dateArticol.setUnit(dateArticolMathaus.getUnit());
            dateArticol.setValPoz(dateArticolMathaus.getValPoz());
            dateArticol.setTip2("");
            dateArticol.setUlStoc(dateArticolMathaus.getUlStoc());
            listArticoleMat.add(dateArticol);
        }
        comandaMathausTCLI.setSellingPlant(comandaMathaus.getSellingPlant());
        comandaMathausTCLI.setDeliveryEntryDataList(listArticoleMat);

    }


    private void setLivrariMathausTCLI(String result) {

        LivrareMathaus livrareMathausTCLI = opArticol.deserializeLivrareMathaus(result);


        for (TranspComenzi transpComenzi : DateLivrare.getInstance().getTranspComenzi()) {

            for (CostTransportMathaus costTransportMathaus : livrareMathaus.getCostTransport()) {

                if (transpComenzi.getFiliala().equals(costTransportMathaus.getFiliala())) {

                    if (transpComenzi.getTransport().equals("TCLI")) {
                        costTransportMathaus.setTipTransp(transpComenzi.getTransport());
                        costTransportMathaus.setValTransp("0");
                    } else {
                        for (CostTransportMathaus costTransportMathausTCLI : livrareMathausTCLI.getCostTransport()) {
                            if (costTransportMathausTCLI.getFiliala().equals(costTransportMathaus.getFiliala())) {
                                costTransportMathaus.setValTransp(costTransportMathausTCLI.getValTransp());
                                costTransportMathaus.setCodArtTransp(costTransportMathausTCLI.getCodArtTransp());
                                costTransportMathaus.setDepart(costTransportMathausTCLI.getDepart());
                                costTransportMathaus.setTipTransp(costTransportMathausTCLI.getTipTransp());
                            }
                        }
                    }
                }
            }
        }

        if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_VANZARE) || DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_LIVRARE) || isComandaDL_TRAP())
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "10", null);

        afisRezumatComandaDialog(livrareMathaus.getCostTransport(), false);

    }


    private void getOptiuniMasini() {

        stareOptiuniCamion = new ArrayList<>();
        isAfisOptiuniMasini = false;
        if (DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getDatePoligonLivrare() != null && isConditiiCostTransport()) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("filiala", this.filialaLivrareMathaus);
            params.put("camionDescoperit", DateLivrare.getInstance().isCamionDescoperit() + "");
            params.put("macara", DateLivrare.getInstance().isMasinaMacara() + "");
            params.put("zona", DateLivrare.getInstance().getDatePoligonLivrare().getTipZona());
            params.put("greutateComanda", ListaArticoleComanda.getInstance().getGreutateKgArticole() + "");
            params.put("comandaEnergofaga", ListaArticoleComanda.getInstance().isComandaEnergofaga() + "");
            params.put("comandaExtralungi", ListaArticoleComanda.getInstance().isComandaExtralungi() + "");

            comandaDAO.getOptiuniMasini(params);
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

    private void getLivrariMathaus() {

        List<ArticolComanda> articoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();
        ComandaMathaus comandaMathaus = new ComandaMathaus();

        String filialaLivrareMathaus = CreareComanda.filialaAlternativa;
        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE)
            filialaLivrareMathaus = DateLivrare.getInstance().getCodFilialaCLP();

        String livrareFilialaSecundara = HelperMathaus.getFilialaSecundara();

        if (!livrareFilialaSecundara.isEmpty())
            filialaLivrareMathaus += "," + livrareFilialaSecundara;

        comandaMathaus.setSellingPlant(filialaLivrareMathaus);
        List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

        String codDepartLivr = UserInfo.getInstance().getCodDepart();
        if (UserInfo.getInstance().getTipUserSap().contains("KA"))
            codDepartLivr = "10";

        for (ArticolComanda artCmd : articoleComanda) {

            List<BeanStocTCLI> listStocTCLI;

            if (HelperMathaus.isConditiiDepozitTCLI(artCmd, "10")) {
                listStocTCLI = HelperMathaus.genereazaStocUnitLog(artCmd);

                for (BeanStocTCLI stocTCLI : listStocTCLI) {
                    DateArticolMathaus dateArticol = HelperMathaus.genereazaStocArticolTCLI(artCmd, stocTCLI);
                    listArticoleMat.add(dateArticol);
                }

            } else {
                DateArticolMathaus dateArticol = HelperMathaus.genereazaStocArticolTCLI(artCmd, null);
                listArticoleMat.add(dateArticol);
            }

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
        antetComanda.setGreutateComanda(ListaArticoleComanda.getInstance().getGreutateKgArticole());
        antetComanda.setTipComandaCamion(ListaArticoleComanda.getInstance().isComandaEnergofaga() ? "ENERGOFAGA" : "NORMALA");
        antetComanda.setComandaDL(DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.DISPOZITIE_LIVRARE) ||
                DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_COMANDA));

        copyLivrareMathaus(antetComanda, comandaMathaus);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("antetComanda", opArticol.serializeAntetCmdMathaus(antetComanda));
        params.put("comandaMathaus", opArticol.serializeComandaMathaus(comandaMathaus));
        params.put("canal", "10");
        params.put("datePoligon", opArticol.serializeDatePoligon(DateLivrare.getInstance().getDatePoligonLivrare()));

        comandaDAO.getLivrariMathaus(params);

    }


    private void afisRezumatComandaDialog(List<CostTransportMathaus> costTransport, boolean selectTransp) {

        saveComandaMathaus = false;

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);

        rezumatComanda = new RezumatComandaDialog(this, ListaArticoleComanda.getInstance().getListArticoleLivrare(), "10", costTransport, DateLivrare.getInstance().getTransport(),
                CreareComanda.filialeArondateMathaus, selectTransp);
        rezumatComanda.setRezumatListener(this);
        rezumatComanda.getWindow().setLayout(width, height);
        rezumatComanda.show();

        comandaEliminata();

    }

    public void displayArticoleComanda() {
        ArticoleCreareAdapter adapterArticole = new ArticoleCreareAdapter(new ArrayList<ArticolComanda>(), this);
        listArtCmd.setAdapter(adapterArticole);

        adapterArticole.setListArticole(ListaArticoleComanda.getInstance().getListArticoleComanda());

    }

    private void saveCmdStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {
            try {

                if (CreareComanda.canalDistrib.equals("10")) {
                    // comanda a fost salvata

                    if (!saveResponse.equals("9")) {
                        // pentru stoc insuficient nu se mai fac alertari

                        if (alertSD) {
                            clearAllData();
                        }

                        if (alertDV) {

                            clearAllData();

                        }

                        if (alertCredite) {
                            sendMailAlert(2);
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

                        if (!alertSD && !alertDV && !alertCredite) {
                            clearAllData();
                        }
                    } else {
                        clearAllData();
                    }

                    Toast.makeText(getApplicationContext(), ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

                } else {
                    // instiintare pret transport client (daca e cazul) si apoi
                    // confirmare salvare
                    if (saveResponse.contains("#")) {
                        // comanda ged cu transport client, se afiseaza pretul
                        String[] varResp = saveResponse.split("#");
                        nrCmdGED = varResp[2];
                        displayDlgPretTransp(varResp[1]);
                    } else
                    // comanda ged fara transport client
                    {
                        Toast.makeText(getApplicationContext(), ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

                        clearAllData();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
            slidingDrawerCmd.animateClose();
        }
    }

    private void saveLivrareStatus(String saveResponse) {
        if (saveResponse.equals("-1")) {
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
        } else {
            if (saveResponse.contains("#")) {
                String[] tokResponse = saveResponse.split("#");
                Toast.makeText(getApplicationContext(), tokResponse[1], Toast.LENGTH_LONG).show();

                if (tokResponse[0].equals("0"))
                    clearAllData();
            }

        }
    }

    public void sendMailAlert(int tip) {
        try {

            if (tip == 1) // alerta dv
            {
                mailAlertTipDest = "2";
            }

            if (tip == 2) // alerta ofiter credite
            {
                mailAlertTipDest = "3";
            }

            if (tip == 3) // alerta director KA
            {
                mailAlertTipDest = "4";
            }

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("ul", UserInfo.getInstance().getUnitLog());
            params.put("depart", UserInfo.getInstance().getCodDepart());
            params.put("dest", mailAlertTipDest);
            params.put("agent", UserInfo.getInstance().getNume()); // nume agent
            params.put("clnt", numeClientVar); //
            params.put("suma", String.format("%.02f", totalComanda)); //

            AsyncTaskWSCall call = new AsyncTaskWSCall(this, "sendMailAlert", params);
            call.getCallResults();

        } catch (Exception ex) {
            Log.e("Error", ex.toString());

        }
    }

    private void clearAllData() {

        arrayListArticole.clear();

        textClient.setText("");
        textFurnizor.setText("");
        textTotalCmd.setText("");
        textGreutateCmd.setText("");
        textTipPlata.setText("");
        textAdrLivr.setText("");
        textPersContact.setText("");
        textTelefon.setText("");
        textCantar.setText("");
        textTransport.setText("");
        textLimCrd.setText("");
        textRestCrd.setText("");
        textTaxaVerde.setText("");
        layoutTaxaVerde.setVisibility(View.INVISIBLE);
        textClient.setVisibility(View.GONE);
        textFurnizor.setVisibility(View.GONE);
        textTotalCmd.setVisibility(View.GONE);
        textGreutateCmd.setVisibility(View.GONE);
        textTipPlata.setVisibility(View.GONE);
        textAdrLivr.setVisibility(View.GONE);
        textPersContact.setVisibility(View.GONE);
        textTelefon.setVisibility(View.GONE);
        textCantar.setVisibility(View.GONE);
        textTransport.setVisibility(View.GONE);
        slidingDrawerCmd.setVisibility(View.GONE);
        labelTotal.setVisibility(View.GONE);
        textLimCrd.setVisibility(View.GONE);
        textRestCrd.setVisibility(View.GONE);

        textNrFact.setVisibility(View.GONE);
        listArtCmd.setVisibility(View.GONE);
        textCursValut.setVisibility(View.GONE);
        layoutHeader.setVisibility(View.INVISIBLE);
        textPondereB.setVisibility(View.INVISIBLE);

        // reset variabile
        resetAllVars();

    }

    private void resetAllVars() {
        arrayListArticole.clear();

        numeClientVar = "";
        tipClientVar = "";
        articoleComanda = "";
        dateLivrare = "";
        cnpClient = "";

        articoleComanda = "";
        articoleFinaleStr = "";
        clientFinalStr = "";
        comandaBlocata = "0";
        numeClientVar = "";
        codClientVar = "";
        totalComanda = 0;
        canalDistrib = "";
        cursValutar = 0;
        limitaCredit = 0;
        restCredit = 0;
        depozitUnic = "";
        globalAlertSDKA = "";
        globalAlertDVKA = "";
        termenPlata = "";
        globalSubCmp = "0";
        valNegociat = 0;

        DateLivrare.getInstance().resetAll();
        filialaAlternativa = UserInfo.getInstance().getUnitLog();
        filialaCustodie = "";
        saveComandaMathaus = false;
        tipPlataContract = " ";
        redirectDateLivrareTCLI = false;

        ListaArticoleComanda.getInstance().clearArticoleComanda();

        if (!UserInfo.getInstance().getCodSuperUser().isEmpty())
            UserInfo.getInstance().setCod(UserInfo.getInstance().getCodSuperUser());

        initLocale();

    }

    private void displayDlgPretTransp(String valTransp) {
        PretTransportDialog transportDialog = new PretTransportDialog(this, CreareComanda.totalComanda, valTransp);
        transportDialog.setTransportDialogListener(this);
        transportDialog.showDialog();

    }

    private void displayArtComplDialog() {
        slidingDrawerCmd.animateClose();

        ArtComplDialog artCompl = new ArtComplDialog(this);
        artCompl.setArtComplListener(this);
        artCompl.showDialog(arrayListArticole, ListaArticoleComanda.getInstance().getListArticoleComanda());

    }

    private void performSaveCmdGED() {
        try {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("comanda", nrCmdGED);

            ComenziDAO comanda = ComenziDAO.getInstance(this);
            comanda.setComenziDAOListener(this);
            comanda.salveazaComandaGed(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayCmdGEDStatus(String saveResponse) {

        if (!saveResponse.equals("-1")) {

            clearAllData();
            Toast.makeText(getApplicationContext(), ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
            slidingDrawerCmd.animateClose();
        }
    }

    public void addListenerListArtCmd() {
        listArtCmd.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                listViewSelPos = position;
                return false;

            }
        });
    }

    private void initLocale() {
        Locale locale = new Locale("en", "US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void checkStaticVars() {

        // restart app la idle
        if (UserInfo.getInstance().getCod().equals("")) {

            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

    @Override
    public void onBackPressed() {
        returnToHome();
        return;
    }

    public void onTaskComplete(String methodName, Object result) {
        if (methodName.equals("sendMailAlert")) {
            clearAllData();
        }

    }

    public void operationComplete(double valNegociat, boolean isTotalNegociat) {
        CreareComanda.valNegociat = valNegociat;
        CreareComanda.isTotalNegociat = isTotalNegociat;
        if (isTotalNegociat) {
            calculProcReducere();
        }

    }

    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
        switch (methodName) {
            case SALVEAZA_COMANDA_DISTRIB:
                saveCmdStatus((String) result);
                break;
            case SALVEAZA_LIVRARE_CUSTODIE:
                saveLivrareStatus((String) result);
                break;
            case SALVEAZA_COMANDA_GED:
                displayCmdGEDStatus((String) result);
                break;
            case GET_COST_MACARA:
                afiseazaPretMacaraDialog((String) result);
                break;
            case GET_COST_MACARA_COMENZI:
                afiseazaPretMacaraComenziDialog((String) result);
                break;
            case GET_LIVRARI_MATHAUS:
                setLivrariMathaus((String) result);
                break;
            case GET_LIVRARI_MATHAUS_TCLI:
                setLivrariMathausTCLI((String) result);
                break;
            case GET_TOTAL_COMENZI_NUMERAR:
                afisTotalComenziNumerar((String) result);
                break;
            case GET_OPTIUNI_MASINI:
                afisOptiuniMasini((String) result);
                break;
            default:
                break;

        }

    }

    public void opTransportComplete(boolean operationOk) {
        if (operationOk) {
            performSaveCmdGED();
        }

    }

    private void verificaStocArticoleDistributie() {

        List<BeanArticolStoc> listArticoleTemp = getArticoleComanda();

        for (BeanArticolStoc articol : listArticoleTemp) {

            if (articol.getDepozit() != null && articol.getDepozit().contains("MAV")) {
                articol.setDepozit(articol.getDepart() + "V1");
                articol.setUnitLog(articol.getUnitLog().substring(0, 2) + "1" + articol.getUnitLog().substring(3, 4));
            }

        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("listArticole", opArticol.serializeListArtStoc(listArticoleTemp));

        opArticol.getStocArticole(params);

    }

    private List<BeanArticolStoc> getArticoleComanda() {

        Iterator<ArticolComanda> iterator = ListaArticoleComanda.getInstance().getListArticoleComanda().iterator();

        List<BeanArticolStoc> listArtStoc = new ArrayList<BeanArticolStoc>();
        String codArticol;

        while (iterator.hasNext()) {
            ArticolComanda articolComanda = iterator.next();

            if (!ClientiGenericiGedInfoStrings.isMatTransport(articolComanda.getCodArticol()) && !ClientiGenericiGedInfoStrings.isMatTransportNume(articolComanda.getNumeArticol())) {
                BeanArticolStoc articolStoc = new BeanArticolStoc();

                if (articolComanda.getCodArticol().length() == 8)
                    codArticol = "0000000000" + articolComanda.getCodArticol();
                else
                    codArticol = articolComanda.getCodArticol();

                articolStoc.setCod(codArticol);
                articolStoc.setDepozit(articolComanda.getDepozit());

                articolStoc.setUnitLog(getUnitLog(articolComanda.getDepozit(), DateLivrare.getInstance().getUnitLog()));
                articolStoc.setDepart(articolComanda.getDepart());
                listArtStoc.add(articolStoc);
            }
        }

        return listArtStoc;

    }

    private String getUnitLog(String depozit, String unitLog) {

        if (depozit.equals("MAV1")) {
            unitLog = unitLog.substring(0, 2) + "2" + unitLog.substring(3, 4);
        } else if (depozit.equals("WOOD")) {
            unitLog = unitLog.substring(0, 2) + "4" + unitLog.substring(3, 4);
        } else {
            unitLog = unitLog.substring(0, 2) + "1" + unitLog.substring(3, 4);
        }

        return unitLog;
    }

    public void operationArtComplComplete(boolean btnSaveCmd) {
        if (btnSaveCmd) {
            if (CreareComanda.canalDistrib.equals("10") && existaArticoleMav() && DateLivrare.getInstance().getTipPlata().equals("E")
                    && DateLivrare.getInstance().getTransport().equals("TCLI"))
                verificaStocArticoleDistributie();
            else if (tipComandaDistributie == TipCmdDistrib.ARTICOLE_COMANDA || tipComandaDistributie == TipCmdDistrib.DISPOZITIE_LIVRARE) {
                prepareArtForDelivery();
                articoleFinaleStr = serializedResult;
                performSaveCmd();
            } else {
                afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
            }

        }

    }

    private void valideazaStoc(List<BeanArticolStoc> listArticoleStoc) {

        String alertMsg = "";
        boolean stocValidDistrib = false;

        List<ArticolComanda> articoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();

        for (BeanArticolStoc artStoc : listArticoleStoc) {
            for (ArticolComanda artCmd : articoleComanda) {

                if (artStoc.getCod().equals(artCmd.getCodArticol()) && artCmd.getDepozit().contains("MAV") && artStoc.getStoc() > artCmd.getCantitate()) {
                    alertMsg = "Pentru articolul " + artCmd.getNumeArticol() + " exista stoc disponibil in depozitul din distributie.";
                    stocValidDistrib = true;
                    break;
                }

            }

        }

        if (stocValidDistrib)
            Toast.makeText(this, alertMsg, Toast.LENGTH_LONG).show();
        else
            performSaveCmd();

    }

    private boolean existaArticoleMav() {

        boolean hasMAV = false;

        List<ArticolComanda> articoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();
        for (ArticolComanda artCmd : articoleComanda) {
            if (artCmd.getDepozit().contains("MAV")) {
                hasMAV = true;
                break;
            }

        }

        return hasMAV;
    }

    private void adaugaPalet(ArticolPalet articolPalet, EnumPaleti status) {


        String depozitPalet = HelperCostDescarcare.getDepozitPalet(ListaArticoleComanda.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());
        String unitlogPalet = HelperCostDescarcare.getUnitlogPalet(ListaArticoleComanda.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());

        ArticolComanda articol = HelperCostDescarcare.getArticolPalet(articolPalet, depozitPalet, unitlogPalet);
        ListaArticoleComanda.getInstance().addArticolLivrareComanda(articol);

        for (int ii = 0; ii < costDescarcare.getArticoleDescarcare().size(); ii++) {
            if (costDescarcare.getArticoleDescarcare().get(ii).getFiliala().equals(articolPalet.getFiliala()) &&
                    costDescarcare.getArticoleDescarcare().get(ii).getDepart().equals(articolPalet.getDepart())) {
                costDescarcare.getArticoleDescarcare().get(ii).setCantitate(costDescarcare.getArticoleDescarcare().get(ii).getCantitate() + articol.getCantitate());
            }
        }


        prepareArtForDelivery();

        articoleFinaleStr = serializedResult;

    }

    private void respingePalet() {
        if (costDescarcare.getValoareDescarcare() > 0 && DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP")) {

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, false, false);
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);

            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaConditiiSuplimentare();
        }
    }

    private void setHeaderVisibility(boolean isVisible) {
        if (isVisible) {
            listArtCmd.setVisibility(View.VISIBLE);
            labelTotal.setVisibility(View.VISIBLE);
            textTotalCmd.setVisibility(View.VISIBLE);
            textGreutateCmd.setVisibility(View.VISIBLE);
            textPondereB.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
        } else {
            listArtCmd.setVisibility(View.INVISIBLE);
            labelTotal.setVisibility(View.INVISIBLE);
            textTotalCmd.setVisibility(View.INVISIBLE);
            textGreutateCmd.setVisibility(View.INVISIBLE);
            textPondereB.setVisibility(View.INVISIBLE);
            layoutHeader.setVisibility(View.INVISIBLE);
        }
    }

    public void update(Observable observable, Object data) {

        if (observable instanceof ListaArticoleComanda) {
            setHeaderVisibility(true);
            displayArticoleComanda();

            totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();

            textTotalCmd.setText(String.format("%.02f", totalComanda));
            textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComanda.getInstance().getGreutateKgArticole()) + " kg");
            textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

            if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE) {
                return;
            }

            // pentru comenzile cu total negociat se recalculeaza reducerile
            if (CreareComanda.isTotalNegociat) {
                calculProcReducere();
            }

            calculPondereB();
            calculTaxaVerde();
        }

    }

    @Override
    public void paletDialogResponse(EnumDaNuOpt response) {
        switch (response) {
            case DA:
                displayArtComplDialog();
                break;
            default:
                break;
        }

    }

    @Override
    public void acceptaCostMacara(boolean acceptaCost, double valoareCost) {
        trateazaPretMacara(acceptaCost, valoareCost);

    }

    @Override
    public void tipComandaSelected(TipCmdDistrib tipSelected, String codFilialaDest) {

        DateLivrare.getInstance().setCodFilialaFasonate("");

        if (tipSelected.equals(TipCmdDistrib.COMANDA_FASONATE)) {
            DateLivrare.getInstance().setCodFilialaFasonate(codFilialaDest);
            tipComandaDistributie = TipCmdDistrib.COMANDA_VANZARE;
        } else {
            tipComandaDistributie = tipSelected;
            DateLivrare.getInstance().setTipComandaDistrib(tipSelected);
            DateLivrare.getInstance().setCodFilialaCLP(codFilialaDest);
        }

        ActionBar actionBar = getActionBar();
        if (tipSelected == TipCmdDistrib.DISPOZITIE_LIVRARE) {
            actionBar.setTitle("Dispozitie livrare");
        } else if (tipSelected == TipCmdDistrib.COMANDA_VANZARE) {
            actionBar.setTitle("Comanda distributie");
        } else if (tipSelected == TipCmdDistrib.LIVRARE_CUSTODIE) {
            actionBar.setTitle("Livrare din custodie");
        } else if (tipSelected == TipCmdDistrib.COMANDA_LIVRARE) {
            actionBar.setTitle("Comanda livrare" + " " + codFilialaDest);
        } else if (tipSelected == TipCmdDistrib.ARTICOLE_COMANDA) {
            actionBar.setTitle("Articole la comanda");
        } else if (tipSelected == TipCmdDistrib.ARTICOLE_DETERIORATE) {
            actionBar.setTitle("Articole deteriorate 30%");
        } else if (tipSelected == TipCmdDistrib.COMANDA_FASONATE) {
            actionBar.setTitle("Comanda otel fasonat");
        }

        invalidateOptionsMenu();

    }

    @Override
    public void operationComplete(EnumArticoleDAO methodName, Object result) {
        switch (methodName) {
            case GET_STOC_ARTICOLE:
                valideazaStoc(opArticol.derializeListArtStoc((String) result));
                break;
            default:
                break;

        }

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

    @Override
    public void comandaEliminata() {

        ArticoleCreareAdapter adapterArticole = new ArticoleCreareAdapter(new ArrayList<ArticolComanda>(), this);
        adapterArticole.setListArticole(ListaArticoleComanda.getInstance().getListArticoleComanda());
        listArtCmd.setAdapter(adapterArticole);
        adapterArticole.notifyDataSetChanged();

        rezumatComanda.setListArticole(ListaArticoleComanda.getInstance().getListArticoleLivrare());

        totalComanda = ListaArticoleComanda.getInstance().getTotalComanda();


        textTotalCmd.setText(String.format("%.02f", totalComanda));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComanda.getInstance().getGreutateKgArticole()) + " kg");
        textRestCrd.setText("RC: " + nf3.format(restCredit - totalComanda));

        // pentru comenzile cu total negociat se recalculeaza reducerile
        if (CreareComanda.isTotalNegociat) {
            calculProcReducere();
        }

        calculPondereB();
        calculTaxaVerde();

        prepareArtForDelivery();
        articoleFinaleStr = serializedResult;

    }

    @Override
    public void comandaSalvata() {
        prepareArtForDelivery();
        articoleFinaleStr = serializedResult;

        saveComandaMathaus = true;

        performSaveCmd();

    }

    @Override
    public void redirectDateLivrare() {
        redirectDateLivrareTCLI = true;
        Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
        nextScreen.putExtra("parrentClass", "CreareComanda");
        nextScreen.putExtra("adrLivrareTCLI", "true");
        startActivity(nextScreen);
    }

}
