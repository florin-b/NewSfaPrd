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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import my.logon.screen.adapters.ArticoleGedAdapter;
import my.logon.screen.adapters.ArticolePretTransport;
import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolAmob;
import my.logon.screen.beans.ArticolCalculDesc;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.ComandaCalculDescarcare;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneCamion;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.beans.TranspComenzi;
import my.logon.screen.dialogs.ArtComplDialog;
import my.logon.screen.dialogs.CostMacaraDialog;
import my.logon.screen.dialogs.CostPaletiDialog;
import my.logon.screen.dialogs.MarjaComandaIPDialog;
import my.logon.screen.dialogs.RezumatComandaDialog;
import my.logon.screen.dialogs.SelectTipMasinaDialog;
import my.logon.screen.dialogs.TipComandaGedDialog;
import my.logon.screen.dialogs.ValoareNegociataDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumPaleti;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.enums.TipCmdGed;
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
import my.logon.screen.listeners.MarjaComandaIPListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.listeners.PaletAlertListener;
import my.logon.screen.listeners.PaletiListener;
import my.logon.screen.listeners.TipCmdGedListener;
import my.logon.screen.listeners.TipMasinaLivrareListener;
import my.logon.screen.listeners.ValoareNegociataDialogListener;
import my.logon.screen.model.AlgoritmComandaGed;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Comanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.OperatiiArticolImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.patterns.UlSiteComparator;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsComenziGed;
import my.logon.screen.utils.UtilsUser;

public class CreareComandaGed extends Activity implements AsyncTaskListener, ArtComplDialogListener, Observer, OperatiiArticolListener,
        ValoareNegociataDialogListener, PaletAlertListener, ComenziDAOListener, CostMacaraListener, TipCmdGedListener, PaletiListener,
        CnpDialogListener, ComandaMathausListener, MarjaComandaIPListener, TipMasinaLivrareListener {

    Button stocBtn, clientBtn, articoleBtn, livrareBtn, saveCmdBtn, slideButtonCmd, valTranspBtn, debugBtn;
    String filiala = "", nume = "", cod = "";
    String codClient = "", numeClient = "";

    private TextView textClient, textTotalCmd, textTipPlata, textAdrLivr, labelTotal, textAlertaMarja;
    private TextView textPersContact, textTelefon, textCantar, textTransport, textTotalGed, textComisionGed, labelComisionClient, textComisionClient;

    private int listViewSelPos = -1;
    private ArticoleGedAdapter adapter;

    public static String codClientVar = "";
    public static String numeClientVar = "";
    public static String codClientCUI = "";

    public static String codClientParavan = "";
    public static String numeClientParavan = "";

    public static String numeDepart = "";
    public static String codDepart = "";
    public static String unitLog = "";
    public static double totalComanda = 0;
    public static double totalComandaGed = 0;
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
    public static String tipComanda = "N"; // N = normala, S = simulata
    public static boolean rezervStoc = false;
    public static String cnpClient = "";
    public static String tipClient = "";
    public static double valTransport = 0;
    public static double valTransportSAP = 0;
    public static String codJ = "";
    public static String adresaLivrare = "";
    public static List<String> listTermenPlata = new ArrayList<String>();

    private boolean alertSD = false, alertDV = false;

    private String nrCmdGED = "";

    private String comandaFinalaStr = "";

    private String comandaBlocata = "0";
    private ProgressBar mProgress;
    private Timer myTimer;
    private int progressVal = 0;
    private Handler saveCmdHandler = new Handler();
    public static String tipAcces;
    private ListView listViewArticoleComanda;
    private SlidingDrawer slidingDrawerCmd;
    Dialog dialog, dlgTransp;

    private LinearLayout layoutHeader;

    private String selectedPretClient = "", selectedCodArticol = "", selectedCantClient = "", selectedDepozit = "", selectedUm = "";

    private double existingStoc = 0;

    Dialog dialogModifPret, dialogModifCant, dialogModifValTransp;

    EditText textCantClient;
    TextView txtTranspSAP;

    ImageView imgAlertPret;
    String articoleSite, umSite, cantSite, valSite;
    private ArrayList<ArticolComanda> listArticole;

    AlgoritmComandaGed algoritm;
    private Comanda comandaFinala;
    static int nrArticoleComanda = 0;

    private ListView listViewArticoleTransp;

    private OperatiiArticol opArticol;
    private ComenziDAO comandaDAO;
    private CostDescarcare costDescarcare;
    public static TipCmdGed tipComandaGed = TipCmdGed.COMANDA_VANZARE;

    public static List<ArticolAmob> listArticoleAMOB;
    private static String idCmdAmob = "-1";
    public static int selectedDepartIndexClp = -1, selectedDepozIndexClp = -1;
    public static String selectedDepartCod = "-1";
    public static EnumTipClientIP tipClientIP = EnumTipClientIP.CONSTR;
    public static boolean permitArticoleDistribIP = true;
    private TextView textFurnizor;
    public static String tipPlataContract = " ";
    public static String filialaLivrareMathaus = UserInfo.getInstance().getFiliala();
    public static String filialeArondateMathaus = UserInfo.getInstance().getFiliala();
    private static boolean saveComandaMathaus = false;
    private LivrareMathaus livrareMathaus;
    private ComandaMathaus comandaMathausTCLI;
    private AntetCmdMathaus antetMathausTCLI;
    private boolean redirectDateLivrareTCLI = false;
    private double costTransportIP = 0;

    private RezumatComandaDialog rezumatComanda;
    private boolean isAfisOptiuniMasini = false;
    private List<OptiuneCamion> stareOptiuniCamion;
    private TextView textGreutateCmd;


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
            setContentView(R.layout.crearecomanda_ged_header);
            checkStaticVars();

            ListaArticoleComandaGed.getInstance().addObserver(this);

            comandaDAO = ComenziDAO.getInstance(this);
            comandaDAO.setComenziDAOListener(this);

            algoritm = new AlgoritmComandaGed();

            opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
            opArticol.setListener(this);

            listViewArticoleComanda = (ListView) findViewById(R.id.listArtCmd);
            adapter = new ArticoleGedAdapter(new ArrayList<ArticolComanda>(), this);
            listViewArticoleComanda.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            imgAlertPret = (ImageView) findViewById(R.id.imageAlertaPret);

            layoutHeader = (LinearLayout) findViewById(R.id.layoutHeader);
            layoutHeader.setVisibility(View.INVISIBLE);

            listViewArticoleComanda.setClickable(true);
            addListenerListArtCmd();
            registerForContextMenu(listViewArticoleComanda);

            slidingDrawerCmd = (SlidingDrawer) findViewById(R.id.slidingDrawerCmd);
            this.slideButtonCmd = (Button) findViewById(R.id.slideButtonCmd);
            slidingDrawerCmd.setVisibility(View.GONE);
            addDrowerEvents();

            this.saveCmdBtn = (Button) findViewById(R.id.saveCmdBtn);
            addListenerSaveCmdBtn();

            debugBtn = (Button) findViewById(R.id.debugBtn);
            debugBtn.setVisibility(View.INVISIBLE);
            addListenerDebugBtn();

            this.valTranspBtn = (Button) findViewById(R.id.valTransp);
            valTranspBtn.setVisibility(View.INVISIBLE);
            addListenerValTranspBtn();

            ActionBar actionBar = getActionBar();
            actionBar.setTitle("Comanda GED");
            actionBar.setDisplayHomeAsUpEnabled(true);

            textClient = (TextView) findViewById(R.id.textClient);
            textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);
            textComisionGed = (TextView) findViewById(R.id.textComisionGed);
            labelComisionClient = (TextView) findViewById(R.id.labelComisionClient);
            textComisionClient = (TextView) findViewById(R.id.textComisionClient);

            textAlertaMarja = (TextView) findViewById(R.id.textAlertaMarja);

            textTipPlata = (TextView) findViewById(R.id.textTipPlata);
            textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
            textPersContact = (TextView) findViewById(R.id.textPersContact);
            textTelefon = (TextView) findViewById(R.id.textTelefon);
            textCantar = (TextView) findViewById(R.id.textCantar);
            textTransport = (TextView) findViewById(R.id.textTransport);
            labelTotal = (TextView) findViewById(R.id.labelTotal);

            textTotalGed = (TextView) findViewById(R.id.textTotalGed);

            textGreutateCmd = (TextView) findViewById(R.id.textGreutateCmd);

            textClient.setVisibility(View.INVISIBLE);
            textTotalCmd.setVisibility(View.INVISIBLE);
            textTipPlata.setVisibility(View.INVISIBLE);
            textAdrLivr.setVisibility(View.INVISIBLE);
            textGreutateCmd.setVisibility(View.INVISIBLE);
            textPersContact.setVisibility(View.INVISIBLE);
            textTelefon.setVisibility(View.INVISIBLE);
            textCantar.setVisibility(View.GONE);
            textTransport.setVisibility(View.INVISIBLE);

            labelTotal.setVisibility(View.INVISIBLE);

            textComisionGed.setVisibility(View.INVISIBLE);
            labelComisionClient.setVisibility(View.INVISIBLE);
            textComisionClient.setVisibility(View.INVISIBLE);

            textAlertaMarja.setVisibility(View.GONE);

            textTotalGed.setVisibility(View.INVISIBLE);

            textClient.setText("");
            textTotalCmd.setText("");
            textTotalGed.setText("");
            textTipPlata.setText("");
            textAdrLivr.setText("");
            textPersContact.setText("");
            textTelefon.setText("");
            textCantar.setText("");
            textTransport.setText("");
            textGreutateCmd.setText("");

            mProgress = (ProgressBar) findViewById(R.id.progress_bar_savecmd);
            mProgress.setVisibility(View.INVISIBLE);

            textFurnizor = (TextView) findViewById(R.id.textFurnizor);

            initLocale();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showTipComandaDialog() {
        TipComandaGedDialog tipCmdDialog = new TipComandaGedDialog(this);
        tipCmdDialog.setTipCmdGedListener(this);
        tipCmdDialog.showDialog();
    }

    private void CreateMenu(Menu menu) {

        if (tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE || tipComandaGed == TipCmdGed.ARTICOLE_COMANDA) {
            MenuItem mnu1 = menu.add(0, 1, 1, "Furnizor");
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }


        MenuItem mnu0 = menu.add(0, 0, 0, "Tip");
        mnu0.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu1 = menu.add(0, 2, 2, "Client");

        mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu2 = menu.add(0, 3, 3, "Articole");

        mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem mnu3 = menu.add(0, 4, 4, "Livrare");

        mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        if (UtilsUser.isAgentOrSD() || UtilsUser.isUserGed() || UtilsUser.isConsWood()) {
            MenuItem mnu4 = menu.add(0, 5, 5, "Valoare negociata");
            mnu4.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                if (listArticole == null || listArticole.size() == 0) {
                    showTipComandaDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case 1:
                if (listArticole == null || listArticole.size() == 0) {

                    Intent nextScreen = new Intent(getApplicationContext(), SelectFurnizorCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case 2:

                if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA) {

                    if (DateLivrare.getInstance().getFurnizorComanda() == null) {
                        Toast.makeText(getApplicationContext(), "Selectati furnizorul.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

                if (listArticole == null || listArticole.size() == 0) {

                    Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmdGed.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:

                if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA) {

                    if (DateLivrare.getInstance().getFurnizorComanda() == null) {
                        Toast.makeText(getApplicationContext(), "Selectati furnizorul.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

                if (numeClientVar.length() > 0) {


                    if (UserInfo.getInstance().getTipUserSap().equals("KA3") && DateLivrare.getInstance().getTipPersClient().equals("D")) {
                        if (listArticole == null || listArticole.size() == 0) {
                            if (DateLivrare.getInstance().getTipPlata().length() == 0) {
                                showKA3AlertPlataDialog();
                                return false;
                            }
                        }
                    }

                    if (listArticole == null || listArticole.size() == 0)
                        if (DateLivrare.getInstance().getCodJudet().length() == 0) {
                            showAlertAdresaLivrareDialog();
                            return false;
                        }

                    Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmdGed.class);
                    nextScreen.putExtra("totalNegociat", String.valueOf(isTotalNegociat));
                    nextScreen.putExtra("codClientVar", codClientVar);
                    nextScreen.putExtra("depozitUnic", depozitUnic);
                    nextScreen.putExtra("tipComanda", tipComanda);
                    nextScreen.putExtra("rezervStoc", rezervStoc);
                    nextScreen.putExtra("filialaAlternativa", filialaAlternativa);
                    nextScreen.putExtra("canalDistrib", canalDistrib);

                    startActivity(nextScreen);

                } else {
                    Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                }

                break;
            case 4:

                if (numeClientVar.length() > 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmdGed.class);
                    nextScreen.putExtra("codClient", codClientVar);
                    nextScreen.putExtra("parrentClass", "CreareComandaGed");

                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
                }

                break;

            case 5:
                showValNegociatDialogBox();
                break;

            case android.R.id.home:
                returnToHome();
                break;
            default:
                return false;
        }

        return true;

    }

    public void showValNegociatDialogBox() {
        ValoareNegociataDialog valDialog = new ValoareNegociataDialog(this);
        valDialog.setValoareNegociataListener(this);
        valDialog.showDialog(valNegociat, isTotalNegociat);

    }

    private void showKA3AlertPlataDialog() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Selectati metoda de plata");
        dlgAlert.setTitle("Atentie!");
        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
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

            backToMainMenu();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Datele se vor pierde. Continuati?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    clearAllData();
                    UserInfo.getInstance().setParentScreen("");

                    backToMainMenu();
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

    private void backToMainMenu() {

        ListaArticoleComandaGed.getInstance().deleteObserver(this);
        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(nextScreen);
        finish();

    }

    public void addDrowerEvents() {
        slidingDrawerCmd.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {
                slideButtonCmd.setBackgroundResource(R.drawable.slideright32);
                listViewArticoleComanda.setEnabled(false);
            }
        });

        slidingDrawerCmd.setOnDrawerCloseListener(new OnDrawerCloseListener() {
            public void onDrawerClosed() {
                slideButtonCmd.setBackgroundResource(R.drawable.slideleft32);
                listViewArticoleComanda.setEnabled(true);
            }
        });

    }

    @Override
    public void onResume() {

        try {

            ActionBar actionBar = getActionBar();

            if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
                actionBar.setTitle("Comanda otel fasonat");
            else if (tipComandaGed == TipCmdGed.COMANDA_LIVRARE)
                actionBar.setTitle("Comanda livrare " + DateLivrare.getInstance().getCodFilialaCLP());
            else if (tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE)
                actionBar.setTitle("Dispozitie livrare");
            else if (tipComandaGed == TipCmdGed.ARTICOLE_COMANDA)
                actionBar.setTitle("Articole la comanda");
            else if (tipComandaGed == TipCmdGed.ARTICOLE_DETERIORATE) {
                actionBar.setTitle("Articole deteriorate 30%");
            } else
                actionBar.setTitle("Comanda GED");

            // !! Se modifica din 2 locuri, User si selectArtCmd
            if (!filialaAlternativa.equals("BV90"))
                filialaAlternativa = UserInfo.getInstance().getUnitLog();

            super.onResume();
            checkStaticVars();

            if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA) {
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

            } else {
                if (textClient != null)
                    textClient.setText("Fara client");
            }

            String varUnitLog = UserInfo.getInstance().getUnitLog();

            varUnitLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

            // ---------------------------- Date livrare

            textAdrLivr.setText("");
            textPersContact.setText("");
            textTelefon.setText("");
            textCantar.setText("");
            textTipPlata.setText("");
            textTransport.setText("");

            if (DateLivrare.getInstance().getCodJudet().length() > 0) {

                if (DateLivrare.getInstance().getTransport().equals("TRAP") || DateLivrare.getInstance().getTransport().equals("TERT")) {
                    if (listArticole != null && listArticole.size() > 0) {
                        //valTranspBtn.setVisibility(View.VISIBLE);

                        getValoareTransportSap();

                    }
                } else {
                    valTranspBtn.setVisibility(View.INVISIBLE);
                    valTransport = 0;
                    valTransportSAP = 0;
                }

                if (listArticole != null)
                    calculProcente();

                textTotalCmd.setVisibility(View.VISIBLE);
                textGreutateCmd.setVisibility(View.VISIBLE);
                textTotalGed.setVisibility(View.VISIBLE);
                textTipPlata.setVisibility(View.VISIBLE);
                textAdrLivr.setVisibility(View.VISIBLE);
                textPersContact.setVisibility(View.VISIBLE);
                textTelefon.setVisibility(View.VISIBLE);

                textTransport.setVisibility(View.VISIBLE);
                slidingDrawerCmd.setVisibility(View.VISIBLE);

                if (DateLivrare.getInstance().getDateLivrare().contains("#")) {
                    String[] tokenLivrare = DateLivrare.getInstance().getDateLivrare().split("#");
                    String factRed = "NU";
                    textAdrLivr.setText(tokenLivrare[0]);
                    textPersContact.setText(tokenLivrare[1]);
                    textTelefon.setText(tokenLivrare[2]);

                    textCantar.setVisibility(View.GONE);
                    if (tokenLivrare[3].equalsIgnoreCase("da")) {
                        textCantar.setVisibility(View.VISIBLE);
                        textCantar.setText("Cu cantarire");
                    }

                    String localStrTipPlata = "";

                    if (tokenLivrare[4].equals("B")) {
                        localStrTipPlata = "Bilet la ordin";
                    }
                    if (tokenLivrare[4].equals("C")) {
                        localStrTipPlata = "Card bancar";
                    }
                    if (tokenLivrare[4].equals("E")) {
                        localStrTipPlata = "Plata in numerar";
                    }
                    if (tokenLivrare[4].equals("L")) {
                        localStrTipPlata = "Plata interna buget-trezorerie";
                    }
                    if (tokenLivrare[4].equals("O")) {
                        localStrTipPlata = "Ordin de plata";
                    }
                    if (tokenLivrare[4].equals("U")) {
                        localStrTipPlata = "Plata interna-alte institutii";
                    }
                    if (tokenLivrare[4].equals("W")) {
                        localStrTipPlata = "Plata in strainatate-banci";
                    }
                    if (tokenLivrare[4].equals("BRD")) {
                        localStrTipPlata = "Card BRD";
                    }
                    if (tokenLivrare[4].equals("ING")) {
                        localStrTipPlata = "Card ING";
                    }
                    if (tokenLivrare[4].equals("UNI")) {
                        localStrTipPlata = "Card Unicredit";
                    }

                    textTipPlata.setText(localStrTipPlata);

                    textTransport.setText(tokenLivrare[5].substring(0, 4));

                    DateLivrare dateLivrareInstance = DateLivrare.getInstance();

                    dateLivrareInstance.setPersContact(tokenLivrare[1]);
                    dateLivrareInstance.setNrTel(tokenLivrare[2]);
                    dateLivrareInstance.setCantar(tokenLivrare[3]);
                    dateLivrareInstance.setTipPlata(tokenLivrare[4]);
                    dateLivrareInstance.setTransport(tokenLivrare[5].substring(0, 4).trim());
                    dateLivrareInstance.setTotalComanda(String.valueOf(totalComanda));
                    dateLivrareInstance.setUnitLog(varUnitLog);
                    dateLivrareInstance.setCodAgent(UserInfo.getInstance().getCod());
                    dateLivrareInstance.setFactRed(factRed);
                }

            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private double getValoareTransportSap() {

        if (isExceptieComandaIP()) {
            valTransport = 0;
            return 0.0;
        }

        NumberFormat nf3 = NumberFormat.getInstance(new Locale("en", "US"));
        nf3.setMinimumFractionDigits(2);
        nf3.setMaximumFractionDigits(2);
        nf3.setGroupingUsed(false);

        double totalTransp = UtilsComenziGed.getValoareTransportSap(listArticole);

        if (valTransport <= valTransportSAP || valTransport < totalTransp) {
            valTransport = totalTransp;
        }

        valTransportSAP = totalTransp;
        valTranspBtn.setText("Transp: " + nf3.format(valTransport));

        return totalTransp;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int menuItemIndex = item.getItemId();

        if (menuItemIndex == 0) // stergere
        {

            if (listViewSelPos >= 0) {

                listArticole.remove(listViewSelPos);
                adapter.notifyDataSetChanged();
                listViewSelPos = -1;

                recalculTotal();
                calculProcente();

                if (listArticole.size() == 0) {
                    valTransport = 0;
                    valTranspBtn.setText("Transp: " + String.valueOf(DateLivrare.getInstance().getValTransport()));

                }

                if (isTotalNegociat) {
                    calculProcentReducere();
                }

            }

        }

        if (menuItemIndex == 1) // modificare pret
        {

            boolean canModifyPrice = listArticole.get(listViewSelPos).getPromotie() >= 1 ? false : true;

            showModifPretDialogBox(canModifyPrice);
        }

        if (menuItemIndex == 2) // modificare cantitate
        {
            showModifCantDialogBox();
        }

        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.listArtCmd) {

            try {

                ListView listView = (ListView) v;
                AdapterContextMenuInfo adapterMenu = (AdapterContextMenuInfo) menuInfo;
                ArticolComanda articol = (ArticolComanda) listView.getItemAtPosition(adapterMenu.position);

                selectedPretClient = String.valueOf(articol.getPretUnitarClient());
                selectedCantClient = String.valueOf(articol.getCantitate());
                selectedCodArticol = articol.getCodArticol();
                selectedDepozit = articol.getDepozit();
                selectedUm = articol.getUm();

                menu.setHeaderTitle(articol.getNumeArticol());

                menu.add(Menu.NONE, 0, 0, "Sterge");
                if (!UserInfo.getInstance().getTipAcces().equals("9") && !UserInfo.getInstance().getTipAcces().equals("10")
                        && !UserInfo.getInstance().getTipUserSap().equals("CONS-GED")) {
                    if (!isTotalNegociat)
                        menu.add(Menu.NONE, 1, 1, "Schimba pret");
                }

                // menu.add(Menu.NONE, 2, 2, "Schimba cantitate");

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void calculProcente() {

        if (UtilsUser.isAgentOrSD() || UtilsUser.isConsWood() || UtilsComenzi.isComandaInstPublica() || isTotalNegociat)
            return;

        adapter.notifyDataSetChanged();
        algoritm.calculProcenteComanda(listArticole, esteModificatPretulGed());

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        textComisionGed.setVisibility(View.INVISIBLE);
        labelComisionClient.setVisibility(View.INVISIBLE);
        textComisionClient.setVisibility(View.INVISIBLE);
        labelComisionClient.setText("Comision: ");

        textComisionGed.setText("Comision: " + nf.format(algoritm.getMarjaBrutaGed() * algoritm.getCoefComision()));
        textComisionClient.setText(nf.format(algoritm.getMarjaBrutaClient() * algoritm.getCoefComision()));

        textAlertaMarja.setVisibility(View.GONE);

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
        }

    }

    private void showAlertPretMareDialog(double diferentaComanda) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreareComandaGed.this);

        NumberFormat nf = new DecimalFormat("#0.00");

        StringBuilder strAlert = new StringBuilder();
        strAlert.append("Comanda este cu ");
        strAlert.append(nf.format(diferentaComanda));
        strAlert.append(" RON");
        strAlert.append(" peste preturile de raft.");

        alertDialogBuilder.setTitle("Atentie!");
        alertDialogBuilder.setMessage(strAlert.toString()).setCancelable(false).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showModifPretDialogBox(boolean canModifyPrice) {
        dialogModifPret = new Dialog(CreareComandaGed.this);
        dialogModifPret.setContentView(R.layout.modifpretclientdialogbox);
        dialogModifPret.setTitle("Modificare pret articol " + selectedCodArticol);
        dialogModifPret.setCancelable(false);
        dialogModifPret.show();

        NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf2.setGroupingUsed(false);

        final EditText textPretClient = (EditText) dialogModifPret.findViewById(R.id.txtPretClient);

        textPretClient.setText(nf2.format(Double.valueOf(selectedPretClient)));
        textPretClient.setSelection(textPretClient.getText().length(), textPretClient.getText().length());

        if (!canModifyPrice)
            textPretClient.setEnabled(false);
        else
            textPretClient.setEnabled(true);

        Button btnOkPret = (Button) dialogModifPret.findViewById(R.id.btnOkPret);
        btnOkPret.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (textPretClient.getText().toString().trim().length() > 0) {
                    if (Double.parseDouble(textPretClient.getText().toString().trim()) > 0) {

                        updatePretClient(listViewSelPos, Double.parseDouble(textPretClient.getText().toString().trim()));
                        dialogModifPret.dismiss();

                        recalculTotal();
                        calculProcente();

                        selectedPretClient = "";

                    }

                }

            }
        });

        Button btnCancelPret = (Button) dialogModifPret.findViewById(R.id.btnCancelPret);
        btnCancelPret.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialogModifPret.dismiss();

            }
        });

    }

    public void showModifCantDialogBox() {
        dialogModifCant = new Dialog(CreareComandaGed.this);
        dialogModifCant.setContentView(R.layout.modifcantclientdialogbox);
        dialogModifCant.setTitle("Modificare cantitate articol " + selectedCodArticol);
        dialogModifCant.setCancelable(false);
        dialogModifCant.show();

        textCantClient = (EditText) dialogModifCant.findViewById(R.id.txtCantClient);

        textCantClient.setText(selectedCantClient);
        textCantClient.setSelection(textCantClient.getText().length(), textCantClient.getText().length());

        Button btnOkCant = (Button) dialogModifCant.findViewById(R.id.btnOkCant);
        btnOkCant.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (textCantClient.getText().toString().trim().length() > 0) {
                    if (Double.parseDouble(textCantClient.getText().toString().trim()) > 0) {

                        performGetArtStoc();

                    }
                }

            }
        });

        Button btnCancelCant = (Button) dialogModifCant.findViewById(R.id.btnCancelCant);
        btnCancelCant.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialogModifCant.dismiss();

            }
        });

    }

    public void performGetArtStoc() {

        try {

            HashMap<String, String> params = new HashMap<String, String>();

            String varLocalUnitLog = "", localCodArticol = "";

            localCodArticol = selectedCodArticol;

            if (selectedCodArticol.length() == 8)
                localCodArticol = "0000000000" + selectedCodArticol;

            params.put("codArt", localCodArticol);

            if (selectedDepozit.equals("MAV1") || selectedDepozit.equals("MAV2")) {
                if (CreareComandaGed.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = "BV92";
                else
                    varLocalUnitLog = CreareComandaGed.filialaAlternativa.substring(0, 2) + "2" + CreareComandaGed.filialaAlternativa.substring(3, 4);
            } else {
                varLocalUnitLog = CreareComandaGed.filialaAlternativa;
            }

            if (UtilsUser.isUserSite()) {
                varLocalUnitLog = UtilsUser.getULUserSite(CreareComandaGed.filialaAlternativa, selectedDepozit);
            }

            params.put("filiala", varLocalUnitLog);
            params.put("depozit", selectedDepozit);
            params.put("depart", UserInfo.getInstance().getCodDepart());

            AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getStocDepozit", params);
            call.getCallResults();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void listArtStoc(String result) {
        if (!result.equals("-1")) {

            String[] tokenPret = result.split("#");

            existingStoc = Double.valueOf(tokenPret[0]);

            if (existingStoc > 0) {

                if (existingStoc >= Double.parseDouble(textCantClient.getText().toString().trim())) {
                    updateCantClient(listViewSelPos, Double.parseDouble(textCantClient.getText().toString().trim()));
                    getArtPretGed();

                } else {
                    Toast.makeText(getApplicationContext(), "Stoc existent: " + String.valueOf(existingStoc), Toast.LENGTH_LONG).show();
                }

                dialogModifCant.dismiss();

                selectedCantClient = "";

                selectedDepozit = "";

            } else {
                Toast.makeText(getApplicationContext(), "Nu exista stoc.", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void recalculTotal() {

        getValoareTransportSap();

        totalComanda = 0;
        totalComandaGed = 0;

        nrArticoleComanda = listArticole.size();

        for (int i = 0; i < listArticole.size(); i++) {
            totalComandaGed += listArticole.get(i).getPretUnitarGed() * listArticole.get(i).getCantUmb();
            totalComanda += listArticole.get(i).getPretUnitarClient() * listArticole.get(i).getCantUmb();

        }

        DateLivrare.getInstance().setTotalComanda(String.valueOf(totalComanda));
        textTotalCmd.setText(String.format("%.02f", totalComanda));
        textTotalGed.setText("Total GED: " + String.format("%.02f", totalComandaGed));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComandaGed.getInstance().getGreutateKgArticole()) + " kg");

    }

    private void updatePretClient(int selectedPos, double newPretClient) {

        listArticole.get(selectedPos).setPretUnitarClient(newPretClient);
        listArticole.get(selectedPos).setPretUnit(newPretClient);
        listArticole.get(selectedPos).setPret(listArticole.get(selectedPos).getPretUnit() * listArticole.get(selectedPos).getCantUmb());

        listArticole.get(selectedPos).setProcent(
                (1 - listArticole.get(selectedPos).getPretUnit()
                        / (listArticole.get(selectedPos).getPretUnitarGed() / listArticole.get(selectedPos).getCantitate() * listArticole.get(selectedPos)
                        .getMultiplu())) * 100);

        listArticole.get(selectedPos).setValTransport(
                ((listArticole.get(selectedPos).getPretUnitarClient() * listArticole.get(selectedPos).getCantUmb()) / listArticole.get(selectedPos)
                        .getMultiplu()) * (listArticole.get(selectedPos).getProcTransport() / 100));

        adapter.notifyDataSetChanged();

    }

    private void updateCantClient(int selectedPos, double newCantClient) {
        listArticole.get(selectedPos).setCantitate(newCantClient);

    }

    @SuppressWarnings("deprecation")
    public void showMessage(String msgText) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false);
        ad.setMessage(msgText);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    public void addListenerSaveCmdBtn() {

        saveCmdBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            if (!existaArticole()) {
                                Toast.makeText(getApplicationContext(), "Comanda nu contine articole!", Toast.LENGTH_SHORT).show();

                                slidingDrawerCmd.animateClose();
                                return true;
                            }
                            if (textAdrLivr.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Comanda nu contine datele de livrare!", Toast.LENGTH_SHORT).show();

                                slidingDrawerCmd.animateClose();
                                return true;
                            }

                            if (DateLivrare.getInstance().getTermenPlata().trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Verificati datele de livrare!", Toast.LENGTH_SHORT).show();
                                return true;
                            }


                            if (isComandaDifValorica()) {
                            } else {
                                if (!isTotalComandaCorect()) {
                                    return true;
                                }
                            }

                            //solicitare cnp
                            if (isConditiiSolicitCnp() && CreareComandaGed.cnpClient.trim().length() == 0) {
                                showCnpDialog();
                                return true;
                            }

                            if (existaArticole() && !textAdrLivr.getText().toString().equals("")) {

                                mProgress.setVisibility(View.VISIBLE);
                                mProgress.setProgress(0);
                                progressVal = 0;
                                myTimer = new Timer();
                                myTimer.schedule(new UpdateProgress(), 40, 15);

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

        if (!DateLivrare.getInstance().getTipPersClient().equals("PF"))
            return false;

        double valGreutateCmd = 0;
        double valFTvaCmd = 0;

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {
            if (articol.getGreutate() > 0) {
                valGreutateCmd += articol.getGreutate();
                valFTvaCmd += (articol.getPretFaraTva() * articol.getCantitate()) / articol.getMultiplu();
            }
        }

        if (valGreutateCmd >= Constants.MAX_GREUTATE_CNP || valFTvaCmd >= Constants.MAX_VALOARE_CNP)
            return true;

        return false;

    }


    private void showCnpDialog() {
        CnpDialog dialog = new CnpDialog(this);
        dialog.setCnpListener(CreareComandaGed.this);
        dialog.show();
    }

    private boolean isComandaACZC() {
        return DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA;
    }

    private boolean isCondPF10_000() {
        return DateLivrare.getInstance().getTipPersClient().equals("PF")
                && (DateLivrare.getInstance().getTipPlata().equals("E") || DateLivrare.getInstance().getTipPlata().equals("E1") || DateLivrare.getInstance().getTipPlata().equals("N") || DateLivrare.getInstance().getTipPlata().equals("R"))
                ;
    }

    @Override
    public void cnpSaved(String cnp) {
        CreareComandaGed.cnpClient = cnp;
        mProgress.setProgress(50);
        myTimer = new Timer();
        myTimer.schedule(new UpdateProgress(), 40, 15);

    }

    class UpdateProgress extends TimerTask {
        public void run() {
            progressVal++;
            if (mProgress.getProgress() == 50) {
                saveCmdHandler.post(new Runnable() {
                    public void run() {

                        DateLivrare dateLivrareInstance = DateLivrare.getInstance();

                        String redSeparat = "";
                        String cmdSAP = "-1"; // se foloseste doar la modificare
                        // comanda

                        String alerteKA = "!";

                        // comanda consilier (av) simulata
                        if (CreareComandaGed.tipComanda.equals("S")) {

                            if (CreareComandaGed.rezervStoc) {
                                comandaBlocata = "20"; // simulare cu rezervare
                                // de stoc
                            } else
                                comandaBlocata = "21"; // simulare fara
                            // rezervare de stoc
                        }

                        String valIncasare = String.valueOf(valTransport), isValIncModif = " ";

                        comandaFinala = new Comanda();
                        comandaFinala.setNumeClient(CreareComandaGed.numeClientVar);
                        comandaFinala.setCodClient(CreareComandaGed.codClientVar);
                        comandaFinala.setCnpClient(CreareComandaGed.cnpClient.length() == 0 ? " " : CreareComandaGed.cnpClient);
                        comandaFinala.setComandaBlocata(comandaBlocata);
                        comandaFinala.setNrCmdSap(cmdSAP);
                        comandaFinala.setAlerteKA(alerteKA);
                        comandaFinala.setFactRedSeparat(redSeparat);
                        comandaFinala.setFilialaAlternativa(CreareComandaGed.filialaAlternativa);
                        comandaFinala.setUserSite(UserInfo.getInstance().getUserSite());
                        comandaFinala.setUserSiteMail(dateLivrareInstance.getMail());
                        comandaFinala.setIsValIncModif(isValIncModif);
                        comandaFinala.setCodJ(codJ);

                        comandaFinala.setAdresaLivrareGed(serializeDateLivrareGed());

                        comandaFinala.setValoareIncasare(valIncasare);

                        if ((dateLivrareInstance.getTipPlata().equals("E") || dateLivrareInstance.getTipPlata().equals("E1") || dateLivrareInstance.getTipPlata().equals("N") || dateLivrareInstance.getTipPlata().equals("R"))
                                && CreareComandaGed.tipClient.equals("PJ")) {
                            if (totalComanda > 5000) {
                                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de 5000 RON!", Toast.LENGTH_SHORT).show();
                                return;
                            } else
                                getTotalComenziNumerar();

                        } else if (isCondPF10_000()) {
                            if (totalComanda > 10000) {
                                Toast.makeText(getApplicationContext(), "Valoarea comenzii este mai mare de 10000 RON.", Toast.LENGTH_SHORT).show();
                                return;
                            } else
                                getTotalComenziNumerar();
                        } else
                            valideazaFinal();

                    }
                });

                myTimer.cancel();
            } else {
                mProgress.setProgress(progressVal);
            }

        }
    }

    private void getTotalComenziNumerar() {

        String tipPers = "PJN";
        String codClientNumerar = comandaFinala.getCodClient();

        if (CreareComandaGed.tipClient.equals("PF")) {
            tipPers = "PF";
            codClientNumerar = DateLivrare.getInstance().getNrTel();
        } else if (CreareComandaGed.tipClient.equals("PJ") && !CreareComandaGed.cnpClient.trim().isEmpty()) {
            tipPers = "PJG";
            codClientNumerar = CreareComandaGed.cnpClient.replaceAll("RO", "");
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("codClient", codClientNumerar);
        params.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
        params.put("tipClient", tipPers);
        comandaDAO.getTotalComenziNumerar(params);

    }

    private void afisTotalComenziNumerar(String totalNumerar) {

        double valPragNumerar = 5000;

        if (DateLivrare.getInstance().getTipPersClient().equals("PF"))
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
            valideazaFinal();
    }


    private boolean isComandaBV() {

        boolean isBV90 = false;

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {
            if (articol.getFilialaSite().equals("BV90")) {
                isBV90 = true;
                break;
            }
        }

        return isBV90;
    }

    private boolean isConditiiTranspCmdIP_BV_02_05() {

        boolean isBV90 = false;

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {
            if (articol.getFilialaSite().equals("BV90") && (articol.getDepartSintetic().equals("02") || articol.getDepartSintetic().equals("05"))) {
                isBV90 = true;
                break;
            }
        }

        if (isBV90 && !DateLivrare.getInstance().getTransport().equals("TCLI") && !DateLivrare.getInstance().getTransport().equals("TERT"))
            return false;
        else
            return true;

    }

    private void valideazaFinal() {
        if (isComandaBV()
                && (DateLivrare.getInstance().getTipDocInsotitor().equals("2") || DateLivrare.getInstance().getTipDocInsotitor().equals("3")
                && DateLivrare.getInstance().getTipPlata().substring(0, 1).equals("E"))) {
            Toast.makeText(getApplicationContext(), "Pentru comenzi din BV90 nu puteti selecta optiunile 'Plata in numerar' si 'Aviz de expeditie'.",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (HelperCreareComanda.isConditiiAlertaIndoire(ListaArticoleComandaGed.getInstance().getListArticoleComanda())) {
            HelperDialog.showInfoDialog(CreareComandaGed.this, "Atentie!", "Selectati tipul de prelucrare (indoire sau debitare).");
        } else {

            if (tipComandaGed.equals(TipCmdGed.COMANDA_AMOB))
                verificaPretMacara();
            else
                getLivrariMathaus();

        }

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
        antetMathausTCLI.setGreutateComanda(ListaArticoleComandaGed.getInstance().getGreutateKgArticole());
        antetMathausTCLI.setTipComandaCamion(ListaArticoleComandaGed.getInstance().isComandaEnergofaga() ? "ENERGOFAGA" : "NORMALA");

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

    private void getLivrariMathausCmdTCLI() {


        List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

        for (TranspComenzi transpComenzi : DateLivrare.getInstance().getTranspComenzi()) {

            if (transpComenzi.getTransport().equals("TRAP")) {

                for (DateArticolMathaus articolMathaus : livrareMathaus.getComandaMathaus().getDeliveryEntryDataList()) {

                    if (transpComenzi.getFiliala().equals(articolMathaus.getDeliveryWarehouse())) {


                        for (DateArticolMathaus dateArticolMathaus : comandaMathausTCLI.getDeliveryEntryDataList()) {

                            if (dateArticolMathaus.getProductCode().equals(articolMathaus.getProductCode())) {

                                DateArticolMathaus dateArticol = new DateArticolMathaus();
                                dateArticol.setProductCode(articolMathaus.getProductCode().replaceAll("^0+(?!$)", ""));
                                dateArticol.setQuantity(articolMathaus.getQuantity());
                                dateArticol.setUnit(articolMathaus.getUnit());
                                dateArticol.setValPoz(dateArticolMathaus.getValPoz());
                                dateArticol.setTip2("");
                                dateArticol.setUlStoc(articolMathaus.getUlStoc());
                                dateArticol.setGreutate(articolMathaus.getGreutate());
                                listArticoleMat.add(dateArticol);
                            }

                        }
                    }

                }

            }

        }

        comandaMathausTCLI.setDeliveryEntryDataList(listArticoleMat);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("antetComanda", opArticol.serializeAntetCmdMathaus(antetMathausTCLI));
        params.put("comandaMathaus", opArticol.serializeComandaMathaus(comandaMathausTCLI));
        params.put("canal", "20");

        comandaDAO.getLivrariMathausTCLI(params);

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

        if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) || DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaDL_TRAP())
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "20", null);

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
            params.put("greutateComanda", ListaArticoleComandaGed.getInstance().getGreutateKgArticole() + "");
            params.put("comandaEnergofaga", ListaArticoleComandaGed.getInstance().isComandaEnergofaga() + "");
            params.put("comandaExtralungi", ListaArticoleComandaGed.getInstance().isComandaExtralungi() + "");

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

    private boolean isConditiiAfisOptiuniMasini() {
        return !DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.DISPOZITIE_LIVRARE) &&
                DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getDatePoligonLivrare() != null && isConditiiCostTransport();
    }

    private boolean isConditiiCostTransport() {
        return DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaDL_TRAP() ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE);
    }

    private void getLivrariMathaus() {

        List<ArticolComanda> articoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
        ComandaMathaus comandaMathaus = new ComandaMathaus();

        String filialaLivrareMathaus = CreareComandaGed.filialaAlternativa;
        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE)
            filialaLivrareMathaus = DateLivrare.getInstance().getCodFilialaCLP();
        else if ((DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_VANZARE) && DateLivrare.getInstance().getTransport().equals("TCLI") &&
                !DateLivrare.getInstance().getFilialaLivrareTCLI().trim().isEmpty())
            filialaLivrareMathaus = DateLivrare.getInstance().getFilialaLivrareTCLI();


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

            if (HelperMathaus.isConditiiDepozitTCLI(artCmd)) {
                listStocTCLI = HelperMathaus.genereazaStocArticolTCLI(artCmd);

                for (BeanStocTCLI stocTCLI : listStocTCLI){
                    DateArticolMathaus dateArticol = HelperMathaus.genereazaStocArticolTCLI(artCmd, stocTCLI);
                    listArticoleMat.add(dateArticol);
                }

            }
            else {
                DateArticolMathaus dateArticol = HelperMathaus.genereazaStocArticolTCLI(artCmd, null);
                listArticoleMat.add(dateArticol);
            }

        }

        comandaMathaus.setDeliveryEntryDataList(listArticoleMat);

        String locLivrare = DateLivrare.getInstance().getOras();
        String judLivrare = DateLivrare.getInstance().getCodJudet();

        AntetCmdMathaus antetComanda = new AntetCmdMathaus();

        antetComanda.setLocalitate(locLivrare);
        antetComanda.setCodJudet(judLivrare);
        antetComanda.setCodClient(comandaFinala.getCodClient());
        antetComanda.setTipPers(UserInfo.getInstance().getTipUserSap());
        antetComanda.setDepart(codDepartLivr);
        antetComanda.setCodPers(DateLivrare.getInstance().getCodAgent());
        antetComanda.setTipTransp(DateLivrare.getInstance().getTransport());

        antetComanda.setCamionDescoperit(DateLivrare.getInstance().isCamionDescoperit());
        antetComanda.setMacara(DateLivrare.getInstance().isMasinaMacara());
        antetComanda.setOptiuniCamion(stareOptiuniCamion);
        antetComanda.setGreutateComanda(ListaArticoleComandaGed.getInstance().getGreutateKgArticole());
        antetComanda.setTipComandaCamion(ListaArticoleComandaGed.getInstance().isComandaEnergofaga() ? "ENERGOFAGA" : "NORMALA");

        copyLivrareMathaus(antetComanda, comandaMathaus);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("antetComanda", opArticol.serializeAntetCmdMathaus(antetComanda));
        params.put("comandaMathaus", opArticol.serializeComandaMathaus(comandaMathaus));
        params.put("canal", "20");
        params.put("datePoligon", opArticol.serializeDatePoligon(DateLivrare.getInstance().getDatePoligonLivrare()));

        comandaDAO.getLivrariMathaus(params);

    }

    private boolean isComandaDL_TRAP() {
        return tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE) && DateLivrare.getInstance().getTransport().equals("TRAP");
    }

    private void setLivrariMathaus(String result) {

        livrareMathaus = opArticol.deserializeLivrareMathaus(result);

        costTransportIP = 0;
        if (isExceptieComandaIP())
            eliminaCostTransport();

        if (isConditiiCostTransport())
            DateLivrare.getInstance().setCostTransportMathaus(livrareMathaus.getCostTransport());

        if (isAfisOptiuniMasini) {

            if (!isExceptieComandaIP())
                HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "20", null);

            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
            isAfisOptiuniMasini = false;
            return;
        }

        List<DateArticolMathaus> articoleMathaus = livrareMathaus.getComandaMathaus().getDeliveryEntryDataList();
        List<ArticolComanda> articoleComandaGed = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
        ListaArticoleComandaGed.getInstance().reseteazaArticoleLivrare();

        String codArticolComanda;
        for (ArticolComanda articolComanda : articoleComandaGed) {

            articolComanda.setTipTransport(null);

            if (tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE))
                articolComanda.setTipTransport(DateLivrare.getInstance().getTransport());

            if ((tipComandaGed.equals(TipCmdGed.COMANDA_LIVRARE) || UtilsComenzi.isComandaClp()) && articolComanda.getArticolMathaus() == null)
                articolComanda.setFilialaSite(DateLivrare.getInstance().getCodFilialaCLP());


            if (articolComanda.getArticolMathaus() == null) {
                ArticolComandaGed articolLivrare = ListaArticoleComandaGed.getInstance().genereazaArticolLivrare((ArticolComandaGed) articolComanda);
                ListaArticoleComandaGed.getInstance().getListArticoleLivrare().add(articolLivrare);
                continue;
            }


            codArticolComanda = articolComanda.getCodArticol();

            if (articolComanda.getCodArticol().length() == 8 || !Character.isDigit(codArticolComanda.charAt(0)))
                codArticolComanda = "0000000000" + articolComanda.getCodArticol();

            for (DateArticolMathaus articolMathaus : articoleMathaus) {

                if (codArticolComanda.equals(articolMathaus.getProductCode())) {

                    ArticolComandaGed articolLivrare = ListaArticoleComandaGed.getInstance().genereazaArticolLivrare((ArticolComandaGed) articolComanda);
                    articolLivrare.setCantitate(articolMathaus.getQuantity());

                    articolLivrare.setCantitate50(HelperMathaus.getCantitateCanal50(articolMathaus, articolComanda));
                    articolLivrare.setUm50(articolComanda.getUm50());

                    if (HelperMathaus.isConditiiDepozitTCLI(articolComanda) && articolMathaus.getCantUmb() == 0){
                        double cantUmbTCLI = (articolMathaus.getQuantity() * articolComanda.getCantUmb()) / articolComanda.getCantitate();
                        articolLivrare.setCantUmb(cantUmbTCLI);
                    }

                    if (articolMathaus.getCantUmb() > 0)
                        articolLivrare.setCantUmb(articolMathaus.getCantUmb());

                    if (articolComanda.getFilialaSite().equals("BV90")) {
                    }
                    else if (articolComanda.getArticolMathaus()!= null && !articolComanda.getArticolMathaus().getTip2().equals("S")
                            && !DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty()){
                        articolLivrare.setFilialaSite(DateLivrare.getInstance().getCodFilialaFasonate());
                    }
                    else {
                        articolLivrare.setFilialaSite(articolMathaus.getDeliveryWarehouse());
                    }

                    if (!tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE))
                        articolLivrare.setDepozit(articolMathaus.getDepozit());

                    ListaArticoleComandaGed.getInstance().getListArticoleLivrare().add(articolLivrare);
                }

            }

        }

        if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaDL_TRAP() ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE))
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "20", null);

        recalculTotal();

        if (!saveComandaMathaus) {
            verificaPretMacaraRezumat();
        } else
            performSaveCmd();

    }

    private void verificaPretMacaraRezumat() {

        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComandaGed.getInstance().getListArticoleComanda());
        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComandaGed.getInstance().getListArticoleLivrare());

        if (!UtilsUser.isAV_SD_01() && !isComandaPaletiCVO()) {

            String codFurnizor = " ";

            if (tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE || tipComandaGed == TipCmdGed.ARTICOLE_COMANDA)
                codFurnizor = DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa();
            else if (tipComandaGed == TipCmdGed.COMANDA_LIVRARE)
                codFurnizor = DateLivrare.getInstance().getCodFilialaCLP();

            //aici
            List<RezumatComanda> listComenziRezumat = HelperMathaus.getRezumatComanda(ListaArticoleComandaGed.getInstance().getListArticoleLivrare());
            List<ComandaCalculDescarcare> listComenziDescarcare = HelperCostDescarcare.getComenziCalculDescarcare(listComenziRezumat);

            String comenziSer = comandaDAO.serializeCalcComenziMacara(listComenziDescarcare);

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("codAgent", DateLivrare.getInstance().getCodAgent());
            params.put("codClient", comandaFinala.getCodClient());
            params.put("codFurnizor", codFurnizor);
            params.put("listComenzi", comenziSer);
            params.put("canal", "20");

            comandaDAO.getCostMacaraComenzi(params);
        } else {
            if (isConditiiAfisOptiuniMasini())
                getOptiuniMasini();
            else
                trateazaConditiiSuplimentare();
        }

    }

    private void afiseazaPretMacaraComenziDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostComenziMacara(result);

        if (isExceptieComandaIP()) {
            getMarjaComenziIP();
            return;
        }

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

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, costDescarcare.getSePermite());
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

    private void eliminaCostTransport() {
        if (livrareMathaus.getCostTransport() != null && !livrareMathaus.getCostTransport().isEmpty())
            for (CostTransportMathaus costTrans : livrareMathaus.getCostTransport()) {
                costTransportIP += Double.valueOf(costTrans.getValTransp());
                costTrans.setValTransp("0");
            }
    }

    private void getMarjaComenziIP() {

        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("codClient", CreareComandaGed.codClientVar);
        comandaDAO.getProcMarjaIP(params);
    }

    private void setProcentMarjaIP(String marjaIP) {
        double procMinMarja = Double.valueOf(marjaIP.split("#")[0]);
        boolean isBlocat = Boolean.valueOf(marjaIP.split("#")[1]);

        double valoareServicii = 0;

        for (ArticolPalet palet : costDescarcare.getArticolePaleti()) {
            valoareServicii += palet.getCantitate() * palet.getPretUnit();
        }

        valoareServicii += costDescarcare.getValoareDescarcare();
        valoareServicii += costTransportIP;

        double marjaBrutaComanda = algoritm.getMarjaBrutaClient(listArticole);
        double procMarja = (marjaBrutaComanda / (totalComanda + valoareServicii)) * 100;

        if (procMarja < procMinMarja && valoareServicii > 0)
            afisCostPaletiMacaraComandaIP(isBlocat);
        else
            trateazaConditiiSuplimentare();

    }

    private void afisCostPaletiMacaraComandaIP(boolean isBlocat) {

        MarjaComandaIPDialog infoDialog = new MarjaComandaIPDialog(this, isBlocat);
        infoDialog.setMarjaComamdaIPListener(this);
        infoDialog.show();

    }

    @Override
    public void comandaIPSelected(boolean isBlocat) {
        if (!isBlocat)
            trateazaConditiiSuplimentare();

    }

    private void afisRezumatComandaDialog(List<CostTransportMathaus> costTransport, boolean selectTransp) {

        saveComandaMathaus = false;


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);

        rezumatComanda = new RezumatComandaDialog(this, ListaArticoleComandaGed.getInstance().getListArticoleLivrare(), "20", costTransport, DateLivrare.getInstance().getTransport(), CreareComandaGed.filialeArondateMathaus, selectTransp);
        rezumatComanda.setRezumatListener(this);
        rezumatComanda.getWindow().setLayout(width, height);
        rezumatComanda.show();

        comandaEliminata();

    }

    private void verificaPretMacara() {

        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComandaGed.getInstance().getListArticoleComanda());

        HelperCostDescarcare.eliminaCostDescarcare(ListaArticoleComandaGed.getInstance().getListArticoleLivrare());

        if (!isExceptieComandaIP() && !UtilsUser.isAV_SD_01() && !isComandaPaletiCVO()) {

            String codFurnizor = " ";

            if (tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE)
                codFurnizor = DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa();
            else if (tipComandaGed == TipCmdGed.COMANDA_LIVRARE)
                codFurnizor = DateLivrare.getInstance().getCodFilialaCLP();

            List<ArticolCalculDesc> artCalcul = HelperCostDescarcare.getDateCalculDescarcare(ListaArticoleComandaGed.getInstance().getListArticoleComanda());

            String listArtSer = comandaDAO.serializeArtCalcMacara(artCalcul);

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("unitLog", DateLivrare.getInstance().getUnitLog());
            params.put("codAgent", DateLivrare.getInstance().getCodAgent());
            params.put("codClient", comandaFinala.getCodClient());
            params.put("codFurnizor", codFurnizor);
            params.put("listArt", listArtSer);

            comandaDAO.getCostMacara(params);
        } else
            trateazaConditiiSuplimentare();

    }

    private boolean isExceptieComandaIP() {
        return UtilsUser.isUserIP() && tipClient.equals("IP") && tipClientIP == EnumTipClientIP.CONSTR;
    }

    private void trateazaConditiiSuplimentare() {

        displayArtComplDialog();

    }

    private void trateazaPretMacara(boolean acceptaPret, double valoarePret) {

        if (acceptaPret) {

            DateLivrare.getInstance().setMasinaMacara(true);

            List<ArticolComanda> articoleDescarcare = HelperCostDescarcare.getArticoleDescarcare(costDescarcare, valoarePret, ListaArticoleComandaGed.getInstance().getListArticoleLivrare());

            ListaArticoleComandaGed.getInstance().getListArticoleLivrare().addAll(articoleDescarcare);

            recalculTotal();

            serializeArticole(prepareArtForDelivery());

        } else {
            DateLivrare.getInstance().setMasinaMacara(false);
        }

        if (isConditiiAfisOptiuniMasini())
            getOptiuniMasini();
        else
            trateazaConditiiSuplimentare();

    }

    private void afiseazaPretMacaraDialog(String result) {

        costDescarcare = HelperCostDescarcare.deserializeCostMacara(result);

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

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, costDescarcare.getSePermite());
            macaraDialog.setCostMacaraListener(this);
            macaraDialog.show();

        } else {
            if (!costDescarcare.getSePermite())
                DateLivrare.getInstance().setMasinaMacara(false);

            trateazaConditiiSuplimentare();

        }

    }

    private boolean isComandaPaletiCVO() {

        boolean isComandaPaleti = true;

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {
            if (!articol.isUmPalet()) {
                isComandaPaleti = false;
                break;
            }
        }

        return isComandaPaleti && (UtilsUser.isCVO() || UtilsUser.isSVO());
    }

    private void verificaPaletiComanda(List<ArticolPalet> listPaleti) {

        Iterator<ArticolComanda> articolIterator = ListaArticoleComandaGed.getInstance().getListArticoleComanda().iterator();

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

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {

            while (paletIterator.hasNext()) {
                ArticolPalet palet = paletIterator.next();

                if (palet.getCodPalet().equals(articol.getCodArticol()) && (palet.getCantitate() == (int) articol.getCantitate())) {
                    paletIterator.remove();
                }

            }

            paletIterator = listPaleti.iterator();
        }

        adapter.notifyDataSetChanged();

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

    private boolean isTotalComandaCorect() {
        double totalGedComanda = algoritm.getTotalPretGedComanda(listArticole);
        double totalClientComanda = algoritm.getTotalPretClientComanda(listArticole);

        boolean comandaOk = true;
        if (totalClientComanda > totalGedComanda && !UtilsUser.isUserIP()) {
            comandaOk = false;
            double diferentaTotal = totalClientComanda - totalGedComanda;
            showAlertPretMareDialog(diferentaTotal);
        }

        return comandaOk;
    }

    private boolean isComandaDifValorica() {

        boolean isCmd = false;

        if (listArticole.size() > 1)
            return isCmd = false;

        for (ArticolComanda articol : listArticole) {
            if (articol.getNumeArticol().startsWith("Diferenta valorica"))
                isCmd = true;
        }

        return isCmd;
    }

    private void displayArtComplDialog() {
        slidingDrawerCmd.animateClose();

        ArtComplDialog artCompl = new ArtComplDialog(this);
        artCompl.setArtComplListener(this);
        artCompl.showDialogGed(listArticole);

    }

    private void performSaveCmd() {
        try {

            HashMap<String, String> params = new HashMap<String, String>();

            String tipUser = "CV";

            if (UtilsUser.isUserSite())
                tipUser = "SITE";

            params.put("comanda", comandaFinalaStr);
            params.put("tipUser", tipUser);
            params.put("JSONArt", serializeArticole(prepareArtForDelivery()));
            params.put("JSONComanda", serializeComanda(comandaFinala));
            params.put("JSONDateLivrare", serializeDateLivrare());
            params.put("alertSD", String.valueOf(alertSD));
            params.put("alertDV", String.valueOf(alertDV));
            params.put("cmdAngajament", String.valueOf(cmdAngajament));
            params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
            params.put("idCmdAmob", idCmdAmob);

            AsyncTaskWSCall call = new AsyncTaskWSCall(this, "saveNewCmdAndroid", params);
            call.getCallResults();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    boolean isCV() {
        return UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
                || UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10");
    }

    public List<ArticolComanda> prepareArtForDelivery() {

        List<ArticolComanda> tempListArticole = new ArrayList<ArticolComanda>(ListaArticoleComandaGed.getInstance().getListArticoleLivrare());

        return tempListArticole;
    }

    private String serializeArticole(List<ArticolComanda> listArticole) {
        JSONArray myArray = new JSONArray();
        JSONObject obj = null;
        TreeSet<String> aprobariCV = new TreeSet<String>();

        Collections.sort(listArticole, new UlSiteComparator());

        alertSD = false;
        alertDV = false;

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
                obj.put("ponderare", listArticole.get(i).getPonderare());
                obj.put("filialaSite", listArticole.get(i).getFilialaSite());
                obj.put("istoricPret", listArticole.get(i).getIstoricPret());
                obj.put("valTransport", listArticole.get(i).getValTransport());
                obj.put("procTransport", listArticole.get(i).getProcTransport());
                obj.put("dataExp", listArticole.get(i).getDataExpPret());
                obj.put("depart", listArticole.get(i).getDepart());
                obj.put("listCabluri", new OperatiiArticolImpl(this).serializeCabluri05(listArticole.get(i).getListCabluri()));
                obj.put("greutate", listArticole.get(i).getGreutate());
                obj.put("tipTransport", listArticole.get(i).getTipTransport());
                obj.put("greutateBruta", listArticole.get(i).getGreutateBruta());
                obj.put("cantitateInit", listArticole.get(i).getCantitateInit());

                obj.put("cantitate50", listArticole.get(i).getCantitate50());
                obj.put("um50", listArticole.get(i).getUm50());

                myArray.put(obj);

                if ((listArticole.get(i).getNumeArticol() != null && listArticole.get(i).getPonderare() == 1) || UtilsComenzi.isComandaInstPublica()
                        || (isTotalNegociat && !UtilsUser.isAgentOrSD() && !UtilsUser.isConsWood())) {

                    if (listArticole.get(i).getProcent() > 0) {
                        alertDV = true;

                        if (!comandaFinala.getComandaBlocata().equals("21") && !comandaFinala.getComandaBlocata().equals("20"))
                            comandaFinala.setComandaBlocata("1");

                        if (UtilsUser.isAgentOrSD() && !UserInfo.getInstance().isFilHome())
                            aprobariCV.add(UserInfo.getInstance().getCodDepart().substring(0, 2));
                        else
                            aprobariCV.add(listArticole.get(i).getDepart().substring(0, 2));
                    }
                }

                if (UtilsUser.isAgentOrSD() || UtilsUser.isConsWood()) {
                    if (listArticole.get(i).getTipAlert().contains("SD"))
                        alertSD = true;

                    if (listArticole.get(i).getTipAlert().contains("DV"))
                        alertDV = true;

                    if ((alertSD || alertDV) && !comandaFinala.getComandaBlocata().equals("21") && !comandaFinala.getComandaBlocata().equals("20"))
                        comandaFinala.setComandaBlocata("1");
                }

            }

            if (DateLivrare.getInstance().isAdrLivrNoua() && UtilsUser.isAgentOrSD()) {
                alertSD = true;

                if (!comandaFinala.getComandaBlocata().equals("21") && !comandaFinala.getComandaBlocata().equals("20"))
                    comandaFinala.setComandaBlocata("1");
            }

        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        if (UtilsUser.isConsWood()) {
            alertSD = false;
            comandaFinala.setNecesarAprobariCV("");

        } else {
            String strAprobariCV = new String(aprobariCV.toString());
            comandaFinala.setNecesarAprobariCV(strAprobariCV.substring(1, strAprobariCV.length() - 1));
        }

        return myArray.toString();

    }

    private String getFilialaGed(String filiala) {
        return filiala.substring(0, 2) + "2" + filiala.substring(3, 4);
    }

    private String getFilialaDistrib(String filiala) {
        if (!filiala.equals("BV90"))
            return filiala.substring(0, 2) + "1" + filiala.substring(3, 4);
        else
            return filiala;
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
            obj.put("canalDistrib", "20");
            obj.put("necesarAprobariCV", comanda.getNecesarAprobariCV());
            obj.put("valTransportSap", String.valueOf(valTransportSAP));

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
            obj.put("tipPlata", UtilsComenzi.getTipPlataClient(DateLivrare.getInstance().getTipPlata(), CreareComandaGed.tipPlataContract));
            obj.put("Transport", DateLivrare.getInstance().getTransport());
            obj.put("DateLivrare", DateLivrare.getInstance().getDateLivrare());
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
            obj.put("totalComanda", DateLivrare.getInstance().getTotalComanda());
            obj.put("unitLog", getUnitLog());
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
            obj.put("meserias", DateLivrare.getInstance().getCodMeserias());
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
            obj.put("filialaCLP", DateLivrare.getInstance().getCodFilialaCLP());
            obj.put("refClient", DateLivrare.getInstance().getRefClient());
            obj.put("isComandaACZC", isComandaACZC());
            obj.put("prelucrareLemn", DateLivrare.getInstance().getPrelucrareLemn());
            obj.put("filialaPlata", DateLivrare.getInstance().getFilialaPlata());

        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return obj.toString();

    }

    private String getUnitLog() {

        String uLog = DateLivrare.getInstance().getUnitLog();
        if (UserInfo.getInstance().getTipUserSap().equals("WOOD"))
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "4" + UserInfo.getInstance().getUnitLog().substring(3, 4);

        return uLog;
    }

    private String getCoordAdresa() {
        if (DateLivrare.getInstance().getCoordonateAdresa() != null)
            return DateLivrare.getInstance().getCoordonateAdresa().latitude + "#" + DateLivrare.getInstance().getCoordonateAdresa().longitude;
        else
            return "0#0";
    }

    public void addListenerClientBtn() {
        clientBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (listArticole.size() == 0) {
                    Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addListenerArticoleBtn() {
        articoleBtn.setOnClickListener(new OnClickListener() {
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
        livrareBtn.setOnClickListener(new OnClickListener() {
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


    private void saveCmdStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {

            if (tipComandaGed == TipCmdGed.COMANDA_AMOB) {
                String[] varResp = saveResponse.split("#");
                nrCmdGED = varResp[2];
                displayDlgPretTransp(saveResponse);
            } else {
                Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();
                clearAllData();

                ActionBar actionBar = getActionBar();
                actionBar.setTitle("Comanda GED");
            }

        } else {
            saveComandaMathaus = false;
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
        }
    }


    private void clearAllData() {

        textClient.setText("");
        textFurnizor.setText("");
        textTotalCmd.setText("");
        textGreutateCmd.setText("");
        textTotalGed.setText("");
        textTipPlata.setText("");
        textAdrLivr.setText("");
        textPersContact.setText("");
        textTelefon.setText("");
        textCantar.setText("");
        textTransport.setText("");

        textClient.setVisibility(View.GONE);
        textFurnizor.setVisibility(View.GONE);
        textTotalCmd.setVisibility(View.GONE);
        textTotalGed.setVisibility(View.GONE);
        textGreutateCmd.setVisibility(View.GONE);

        textTipPlata.setVisibility(View.GONE);
        textAdrLivr.setVisibility(View.GONE);
        textPersContact.setVisibility(View.GONE);
        textTelefon.setVisibility(View.GONE);
        textCantar.setVisibility(View.GONE);
        textTransport.setVisibility(View.GONE);
        slidingDrawerCmd.setVisibility(View.GONE);
        labelTotal.setVisibility(View.GONE);

        listViewArticoleComanda.setVisibility(View.GONE);

        layoutHeader.setVisibility(View.INVISIBLE);
        textTotalGed.setVisibility(View.INVISIBLE);

        textComisionGed.setVisibility(View.INVISIBLE);
        labelComisionClient.setVisibility(View.INVISIBLE);
        textComisionClient.setVisibility(View.INVISIBLE);
        textAlertaMarja.setVisibility(View.GONE);
        valTranspBtn.setVisibility(View.GONE);

        listViewArticoleComanda.setEnabled(true);

        if (tipComandaGed == TipCmdGed.COMANDA_LIVRARE && !DateLivrare.getInstance().getCodFilialaCLP().isEmpty() && UtilsUser.isUserIP()) {
            UserInfo.getInstance().setUnitLog(DateLivrare.getInstance().getCodFilialaCLP());
        }

        tipComandaGed = TipCmdGed.COMANDA_VANZARE;
        listArticoleAMOB = null;
        saveComandaMathaus = false;

        // reset variabile
        resetAllVars();

    }

    private void resetAllVars() {

        adapter.notifyDataSetChanged();

        numeClientVar = "";

        dateLivrare = "";
        cnpClient = "";

        comandaBlocata = "0";
        numeClientVar = "";
        codClientVar = "";
        codClientCUI = "";
        totalComanda = 0;
        totalComandaGed = 0;
        canalDistrib = "";
        cursValutar = 0;
        limitaCredit = 0;
        restCredit = 0;
        depozitUnic = "";
        tipClient = "";
        valTransport = 0;
        valTransportSAP = 0;
        tipComanda = "N";
        selectedCodArticol = "";
        termenPlata = "";
        codJ = "";
        adresaLivrare = "";
        valNegociat = 0;

        isTotalNegociat = false;
        idCmdAmob = "-1";
        listTermenPlata = new ArrayList<String>();
        tipComandaGed = TipCmdGed.COMANDA_VANZARE;

        DateLivrare.getInstance().resetAll();

        filialaAlternativa = UserInfo.getInstance().getUnitLog();
        ListaArticoleComandaGed.getInstance().clearArticoleComanda();

        selectedDepartIndexClp = -1;
        selectedDepozIndexClp = -1;
        selectedDepartCod = "-1";
        permitArticoleDistribIP = true;
        tipPlataContract = " ";
        redirectDateLivrareTCLI = false;

        initLocale();

    }

    private void displayDlgPretTransp(String response) {
        try {
            dlgTransp = new Dialog(CreareComandaGed.this);
            dlgTransp.setContentView(R.layout.valtranspdlgbox);
            dlgTransp.setTitle("Confirmare comanda GED");

            NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
            nf2.setMinimumFractionDigits(2);
            nf2.setMaximumFractionDigits(2);

            double valTranspExtra = 0;

            if (response.contains("#")) {
                String[] tokResponse = response.split("#");
                valTranspExtra = Double.parseDouble(tokResponse[1]);
            }

            final double valTranspExtraFinal = valTranspExtra;

            TextView textValCmd = (TextView) dlgTransp.findViewById(R.id.textValCmd);
            textValCmd.setText(nf2.format(CreareComandaGed.totalComanda));

            TextView textValTransp = (TextView) dlgTransp.findViewById(R.id.textValTransp);
            textValTransp.setText(nf2.format(valTransport + valTranspExtra));

            double totCmd = valTransport + CreareComandaGed.totalComanda + valTranspExtra;

            TextView textTotCmd = (TextView) dlgTransp.findViewById(R.id.textTotCmd);
            textTotCmd.setText(nf2.format(totCmd));

            Button btnOkCmd = (Button) dlgTransp.findViewById(R.id.btnOkCmd);
            btnOkCmd.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dlgTransp.dismiss();
                    valTransport += valTranspExtraFinal;

                    if (CreareComandaGed.tipComanda.equals("N")) // comanda
                        // ferma
                        performSaveCmdGED();
                    else {

                        if (CreareComandaGed.tipComanda.equals("S")) {
                            performSaveCmdGED();
                        } else {
                            // este simulare, a fost salvata deja
                            Toast.makeText(getApplicationContext(), "Comanda salvata.", Toast.LENGTH_SHORT).show();
                            clearAllData();
                        }
                    }
                }
            });

            Button btnCancelCmd = (Button) dlgTransp.findViewById(R.id.btnCancelCmd);
            btnCancelCmd.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dlgTransp.dismiss();
                    slidingDrawerCmd.animateClose();
                }
            });

            dlgTransp.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void performSaveCmdGED() {
        try {

            String canalDistrib = "20";
            if (UtilsUser.isConsWood())
                canalDistrib = "40";

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("comanda", nrCmdGED);
            params.put("canal", canalDistrib);
            AsyncTaskWSCall call = new AsyncTaskWSCall(this, "saveCmdGED", params);
            call.getCallResults();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addListenerValTranspBtn() {
        valTranspBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (UtilsComenzi.isComandaInstPublica())
                    return;

                showModifValTranspDialogBox();

            }
        });

    }

    private void calculProcentReducere() {
        ListaArticoleComandaGed.getInstance().setValoareNegociata(valNegociat);
        ListaArticoleComandaGed.getInstance().calculProcentReducere();
        displayArticoleComanda();
        getValoareTransportSap();
    }

    public void showModifValTranspDialogBox() {
        dialogModifValTransp = new Dialog(CreareComandaGed.this);
        dialogModifValTransp.setContentView(R.layout.modifvaltranspdialogbox);
        dialogModifValTransp.setTitle("Modificare valoare transport");
        dialogModifValTransp.setCancelable(false);
        dialogModifValTransp.show();

        final NumberFormat nf3 = NumberFormat.getInstance(new Locale("en", "US"));
        nf3.setMinimumFractionDigits(2);
        nf3.setMaximumFractionDigits(2);
        nf3.setGroupingUsed(false);

        final EditText textValTransp = (EditText) dialogModifValTransp.findViewById(R.id.txtValTransp);

        txtTranspSAP = (TextView) dialogModifValTransp.findViewById(R.id.txtTranspSAP);
        txtTranspSAP.setText("SAP: " + nf3.format(valTransportSAP));

        listViewArticoleTransp = (ListView) dialogModifValTransp.findViewById(R.id.listArticoleTransp);

        ArticolePretTransport adapterArticoleTransport = new ArticolePretTransport(CreareComandaGed.this, listArticole);
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
                            calculProcente();
                            dialogModifValTransp.dismiss();
                        }

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

    private void displayCmdGEDStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {

            clearAllData();
            Toast.makeText(getApplicationContext(), InfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Comanda NU a fost salvata!", Toast.LENGTH_LONG).show();
            slidingDrawerCmd.animateClose();
        }
    }

    @Override
    public void onBackPressed() {

        returnToHome();
        return;
    }

    public void addListenerListArtCmd() {
        listViewArticoleComanda.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                listViewSelPos = position;
                return false;

            }
        });
    }

    public boolean esteModificatPretulGed() {
        boolean esteModificat = false;

        String[] varAlteValoriArray = null;
        double varPretArtClient = 0, varPretArtGed = 0;

        ArticolComanda articol = null;

        for (int i = 0; i < listArticole.size(); i++) {

            articol = listArticole.get(i);

            if (!articol.getAlteValori().contains("!"))
                continue;

            varAlteValoriArray = articol.getAlteValori().split("!");
            double factorConvUM = Double.parseDouble(varAlteValoriArray[7]);

            varPretArtClient = round(articol.getPretUnitarClient() * factorConvUM, 3);
            varPretArtGed = round(articol.getPretUnitarGed() * factorConvUM, 3);

            if (varPretArtClient != varPretArtGed) {
                esteModificat = true;
                break;

            }

        }

        if (!esteModificat) {
            AlgoritmComandaGed algoritm = new AlgoritmComandaGed();
            algoritm.inlaturaToateAlertelePret(listArticole);
        }

        return esteModificat;
    }

    public static double round(double value, int places) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    private void getArtPretGed() {

        HashMap<String, String> params = new HashMap<String, String>();

        String uLog = UserInfo.getInstance().getUnitLog();

        String localCodArticol = "";

        localCodArticol = selectedCodArticol;

        if (localCodArticol.length() == 8)
            localCodArticol = "0000000000" + localCodArticol;

        uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

        String tipUser;
        if (UserInfo.getInstance().getTipUserSap().equals("KA3"))
            tipUser = "KA3";
        else
            tipUser = "CV";

        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(CreareComandaGed.codClientVar);
        paramPret.setArticol(localCodArticol);
        paramPret.setCantitate(textCantClient.getText().toString().trim());
        paramPret.setDepart("11");
        paramPret.setUm(selectedUm);
        paramPret.setUl(uLog);
        paramPret.setDepoz(" ");
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib("20");
        paramPret.setTipUser(tipUser);
        paramPret.setMetodaPlata(DateLivrare.getInstance().getTipPlata());
        paramPret.setTermenPlata(DateLivrare.getInstance().getTermenPlata());
        paramPret.setFilialaAlternativa(uLog);

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));
        opArticol.getPretGedJson(params);

    }

    private void updateArtInfo(PretArticolGed pretArticol) {

        String infoArticol = pretArticol.getConditiiPret().replace(',', '.');
        String localCantUmb = pretArticol.getCantitateUmBaza().toString();

        for (int i = 0; i < listArticole.size(); i++) {
            if (listArticole.get(i).getCodArticol().equals(selectedCodArticol)) {
                listArticole.get(i).setInfoArticol(infoArticol);
                listArticole.get(i).setCantUmb(Double.parseDouble(localCantUmb));

                listArticole.get(i).setValTransport(
                        ((listArticole.get(i).getPretUnitarClient() * listArticole.get(i).getCantitate()) / listArticole.get(i).getMultiplu())
                                * (listArticole.get(i).getProcTransport() / 100));

                break;
            }
        }

        selectedCodArticol = "";

        if (isTotalNegociat) {
            calculProcentReducere();
        }

        recalculTotal();
        calculProcente();

        adapter.notifyDataSetChanged();

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

    public void onTaskComplete(String methodName, Object result) {
        if (methodName.equals("saveNewCmdAndroid")) {
            saveCmdStatus((String) result);
        }

        if (methodName.equals("getStocDepozit")) {
            listArtStoc((String) result);
        }

        if (methodName.equals("saveCmdGED")) {
            displayCmdGEDStatus((String) result);
        }

        if (methodName.equals("getValTransportComandaSite")) {
            String[] pretResponse = {};

            if (!result.equals("-1")) {

                pretResponse = ((String) result).split("#");

                if (!DateLivrare.getInstance().getTransport().equals(pretResponse[1].toUpperCase(Locale.getDefault()))) {
                    Toast.makeText(getApplicationContext(), "Tipul de transport recomandat este " + pretResponse[1], Toast.LENGTH_LONG).show();
                }
            } else {
                pretResponse[0] = "0.0";
            }

            valTransportSAP = Double.valueOf(pretResponse[0]);
            showModifValTranspDialogBox();
        }

    }

    public void operationComplete(EnumArticoleDAO methodName, Object result) {
        switch (methodName) {
            case GET_PRET_GED_JSON:
                updateArtInfo(opArticol.deserializePretGed(result));
                break;
            default:
                break;
        }

    }

    public void operationArtComplComplete(boolean btnSaveCmd) {
        if (btnSaveCmd) {

            //comanda simulata
            if (tipComandaGed == TipCmdGed.ARTICOLE_COMANDA || tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE || tipComandaGed == TipCmdGed.COMANDA_AMOB) {
                performSaveCmd();
            } else {
                afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
            }

        }

    }

    private void setComandaHeaderVisibility(boolean isVisible) {
        if (isVisible) {
            listViewArticoleComanda.setVisibility(View.VISIBLE);
            labelTotal.setVisibility(View.VISIBLE);
            textTotalCmd.setVisibility(View.VISIBLE);
            textGreutateCmd.setVisibility(View.VISIBLE);
            textTotalGed.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
        } else {
            listViewArticoleComanda.setVisibility(View.INVISIBLE);
            labelTotal.setVisibility(View.INVISIBLE);
            textTotalCmd.setVisibility(View.INVISIBLE);
            textGreutateCmd.setVisibility(View.INVISIBLE);
            textTotalGed.setVisibility(View.INVISIBLE);
            layoutHeader.setVisibility(View.INVISIBLE);
        }
    }

    private void adaugaPalet(ArticolPalet articolPalet, EnumPaleti status) {

        String depozitPalet = HelperCostDescarcare
                .getDepozitPalet(ListaArticoleComandaGed.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());

        String unitlogPalet = HelperCostDescarcare.getUnitlogPalet(ListaArticoleComandaGed.getInstance().getListArticoleLivrare(), articolPalet.getCodArticol());
        ArticolComanda articol = HelperCostDescarcare.getArticolPalet(articolPalet, depozitPalet, unitlogPalet);
        ListaArticoleComandaGed.getInstance().addArticolLivrareComanda(articol);

        costDescarcare.getArticoleDescarcare().get(0).setCantitate(costDescarcare.getArticoleDescarcare().get(0).getCantitate() + articol.getCantitate());

    }

    private void respingePalet() {
        if (costDescarcare.getValoareDescarcare() > 0 && DateLivrare.getInstance().getTransport().equalsIgnoreCase("TRAP")) {

            CostMacaraDialog macaraDialog = new CostMacaraDialog(this, costDescarcare, costDescarcare.getSePermite());
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

    private void displayArticoleComanda() {

        setComandaHeaderVisibility(true);
        listArticole = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
        adapter.setListArticole(listArticole);
        adapter.notifyDataSetChanged();

        recalculTotal();

        textTotalCmd.setText(String.format("%.02f", totalComanda));

        textTotalGed.setText("Total GED: " + String.format("%.02f", totalComandaGed));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComandaGed.getInstance().getGreutateKgArticole()) + " kg");

        calculProcente();

        // transport
        if (DateLivrare.getInstance().getTransport().equals("TRAP") || DateLivrare.getInstance().getTransport().equals("TERT")) {
            // valTranspBtn.setVisibility(View.VISIBLE);
            //valTranspBtn.setText("Transp: " + String.valueOf(DateLivrare.getInstance().getValTransport()));
        } else {
            valTranspBtn.setVisibility(View.INVISIBLE);
            valTransport = 0;
        }

    }

    private void getArticoleAmob(String idComanda) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("idComanda", idComanda);
        comandaDAO.getArticoleAmob(params);
    }

    private void addListenerDebugBtn() {
        debugBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreareComandaGed.this);

                String debugString = "";

                if (listArticole != null) {
                    debugString = listArticole.toString();

                    debugString += "\n\n";

                    debugString += "Marja disponibila = " + algoritm.getMarjaDisponibila();

                    debugString += "\n\n";

                    debugString += "Deficit divizii = " + algoritm.getDeficitDivizii();
                }

                alertDialogBuilder.setMessage(debugString).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });
    }

    private boolean existaArticole() {
        return ListaArticoleComandaGed.getInstance().getListArticoleComanda() != null
                && ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0;
    }

    public void update(Observable observable, Object data) {
        if (observable instanceof ListaArticoleComandaGed) {
            displayArticoleComanda();

            if (isTotalNegociat) {
                calculProcentReducere();
            }
        }

    }

    public void operationComplete(double valNegociat, boolean isTotalNegociat) {
        CreareComandaGed.valNegociat = valNegociat;
        CreareComandaGed.isTotalNegociat = isTotalNegociat;

        if (isTotalNegociat) {
            calculProcentReducere();
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
    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
        switch (methodName) {
            case GET_COST_MACARA:
                afiseazaPretMacaraDialog((String) result);
                break;
            case GET_LIVRARI_MATHAUS:
                setLivrariMathaus((String) result);
                break;
            case GET_LIVRARI_MATHAUS_TCLI:
                setLivrariMathausTCLI((String) result);
                break;
            case GET_COST_MACARA_COMENZI:
                afiseazaPretMacaraComenziDialog((String) result);
                break;
            case GET_TOTAL_COMENZI_NUMERAR:
                afisTotalComenziNumerar((String) result);
                break;
            case GET_ARTICOLE_AMOB:
                listArticoleAMOB = comandaDAO.deserializeArticoleAmob((String) result);
                break;
            case GET_PROC_MARJA_IP:
                setProcentMarjaIP((String) result);
                break;
            case GET_OPTIUNI_MASINI:
                afisOptiuniMasini((String) result);
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
    public void tipComandaSelected(TipCmdGed tipSelected, String idComanda, String codFilialaClp) {

        DateLivrare.getInstance().setCodFilialaFasonate("");

        if (tipSelected.equals(TipCmdGed.COMANDA_FASONATE)) {
            DateLivrare.getInstance().setCodFilialaFasonate(codFilialaClp);
            tipComandaGed = TipCmdGed.COMANDA_VANZARE;
        } else {
            tipComandaGed = tipSelected;
            DateLivrare.getInstance().setTipComandaGed(tipSelected);
            DateLivrare.getInstance().setCodFilialaCLP(codFilialaClp);
        }

        ActionBar actionBar = getActionBar();

        if (tipSelected.equals(TipCmdGed.COMANDA_AMOB)) {
            actionBar.setTitle("Comanda GED - preluare comanda Amob");
            idCmdAmob = idComanda;
            getArticoleAmob(idComanda);

        } else if (tipSelected.equals(TipCmdGed.DISPOZITIE_LIVRARE)) {
            actionBar.setTitle("Dispozitie livrare");
        } else if (tipSelected.equals(TipCmdGed.COMANDA_LIVRARE)) {
            actionBar.setTitle("Comanda livrare" + " " + codFilialaClp);
        } else if (tipSelected == TipCmdGed.ARTICOLE_COMANDA) {
            actionBar.setTitle("Articole la comanda");
        } else if (tipSelected == TipCmdGed.ARTICOLE_DETERIORATE) {
            actionBar.setTitle("Articole deteriorate 30%");
        } else if (tipSelected == TipCmdGed.COMANDA_FASONATE) {
            actionBar.setTitle("Comanda otel fasonat");
        }
        else {
            actionBar.setTitle("Comanda GED");
            selectedDepartIndexClp = -1;
            selectedDepozIndexClp = -1;
            selectedDepartCod = "-1";
        }

        invalidateOptionsMenu();

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

        adapter.setListArticole(ListaArticoleComandaGed.getInstance().getListArticoleComanda());
        listViewArticoleComanda.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        totalComanda = ListaArticoleComandaGed.getInstance().getTotalComanda();
        textTotalCmd.setText(String.format("%.02f", totalComanda));
        textGreutateCmd.setText("Masa bruta: " + String.format("%.02f", ListaArticoleComandaGed.getInstance().getGreutateKgArticole()) + " kg");
        recalculTotal();

        rezumatComanda.setListArticole(ListaArticoleComandaGed.getInstance().getListArticoleLivrare());


    }

    @Override
    public void comandaSalvata() {
        saveComandaMathaus = true;
        performSaveCmd();
    }

    @Override
    public void redirectDateLivrare() {
        redirectDateLivrareTCLI = true;
        Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmdGed.class);
        nextScreen.putExtra("parrentClass", "CreareComandaGed");
        nextScreen.putExtra("adrLivrareTCLI", "true");
        startActivity(nextScreen);
    }

}
