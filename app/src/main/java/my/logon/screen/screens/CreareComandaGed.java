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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import my.logon.screen.R;
import my.logon.screen.adapters.ArticoleGedAdapter;
import my.logon.screen.adapters.ArticolePretTransport;
import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolAmob;
import my.logon.screen.beans.ArticolDescarcare;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneCamion;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.beans.TaxaComanda;
import my.logon.screen.beans.TaxaMasina;
import my.logon.screen.beans.TaxaTransport;
import my.logon.screen.dialogs.MarjaComandaIPDialog;
import my.logon.screen.dialogs.RezumatComandaDialog;
import my.logon.screen.dialogs.TaxeMasiniDialog;
import my.logon.screen.dialogs.TipComandaGedDialog;
import my.logon.screen.dialogs.ValoareNegociataDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumComenziDAO;
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
import my.logon.screen.listeners.MarjaComandaIPListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.listeners.TaxeMasiniListener;
import my.logon.screen.listeners.TipCmdGedListener;
import my.logon.screen.listeners.ValoareNegociataDialogListener;
import my.logon.screen.model.AlgoritmComandaGed;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.Comanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
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
        ValoareNegociataDialogListener, ComenziDAOListener, TipCmdGedListener,
        CnpDialogListener, ComandaMathausListener, MarjaComandaIPListener, TaxeMasiniListener {

    Button saveCmdBtn, slideButtonCmd, valTranspBtn, debugBtn;


    private TextView textClient, textTotalCmd, textTipPlata, textAdrLivr, labelTotal, textAlertaMarja;
    private TextView textPersContact, textTelefon, textTransport, textTotalGed, textComisionGed, labelComisionClient, textComisionClient;

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


    private LinearLayout layoutHeader;

    private String selectedPretClient = "", selectedCodArticol = "", selectedCantClient = "", selectedDepozit = "", selectedUm = "";

    private double existingStoc = 0;

    private Dialog dialogModifCant, dialogModifValTransp;

    EditText textCantClient;
    TextView txtTranspSAP;

    ImageView imgAlertPret;

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
    private List<TaxaComanda> taxeComandaIP;

    private RezumatComandaDialog rezumatComanda;
    private boolean isAfisOptiuniMasini = false;
    private List<OptiuneCamion> stareOptiuniCamion;
    private TextView textGreutateCmd;
    public static String filialaCustodie = "";
    private boolean isPragNumerarZiValid = false;
    private List<TaxaTransport> listTaxeTransport;
    private CostDescarcare costDescarcareFiliala;


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

            imgAlertPret = findViewById(R.id.imageAlertaPret);

            layoutHeader = findViewById(R.id.layoutHeader);
            layoutHeader.setVisibility(View.INVISIBLE);

            listViewArticoleComanda.setClickable(true);
            addListenerListArtCmd();
            registerForContextMenu(listViewArticoleComanda);

            slidingDrawerCmd = findViewById(R.id.slidingDrawerCmd);
            this.slideButtonCmd = findViewById(R.id.slideButtonCmd);
            slidingDrawerCmd.setVisibility(View.GONE);
            addDrowerEvents();

            this.saveCmdBtn = findViewById(R.id.saveCmdBtn);
            addListenerSaveCmdBtn();

            debugBtn = findViewById(R.id.debugBtn);
            debugBtn.setVisibility(View.INVISIBLE);
            addListenerDebugBtn();

            this.valTranspBtn = findViewById(R.id.valTransp);
            valTranspBtn.setVisibility(View.INVISIBLE);
            addListenerValTranspBtn();

            ActionBar actionBar = getActionBar();
            actionBar.setTitle("Comanda GED");
            actionBar.setDisplayHomeAsUpEnabled(true);

            textClient = findViewById(R.id.textClient);
            textTotalCmd = findViewById(R.id.textTotalCmd);
            textComisionGed = findViewById(R.id.textComisionGed);
            labelComisionClient = findViewById(R.id.labelComisionClient);
            textComisionClient = findViewById(R.id.textComisionClient);

            textAlertaMarja = findViewById(R.id.textAlertaMarja);

            textTipPlata = findViewById(R.id.textTipPlata);
            textAdrLivr = findViewById(R.id.textAdrLivr);
            textPersContact = findViewById(R.id.textPersContact);
            textTelefon = findViewById(R.id.textTelefon);

            textTransport = findViewById(R.id.textTransport);
            labelTotal = findViewById(R.id.labelTotal);

            textTotalGed = findViewById(R.id.textTotalGed);

            textGreutateCmd = findViewById(R.id.textGreutateCmd);

            textClient.setVisibility(View.INVISIBLE);
            textTotalCmd.setVisibility(View.INVISIBLE);
            textTipPlata.setVisibility(View.INVISIBLE);
            textAdrLivr.setVisibility(View.INVISIBLE);
            textGreutateCmd.setVisibility(View.INVISIBLE);
            textPersContact.setVisibility(View.INVISIBLE);
            textTelefon.setVisibility(View.INVISIBLE);

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

            textTransport.setText("");
            textGreutateCmd.setText("");

            mProgress = findViewById(R.id.progress_bar_savecmd);
            mProgress.setVisibility(View.INVISIBLE);

            textFurnizor = findViewById(R.id.textFurnizor);

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
            } else if (tipComandaGed == TipCmdGed.LIVRARE_CUSTODIE) {
                actionBar.setTitle("Livrare din custodie");
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

            String varUnitLog;

            varUnitLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

            if (isLivrareCustodie() && !filialaCustodie.isEmpty())
                varUnitLog = filialaCustodie;

            // ---------------------------- Date livrare

            textAdrLivr.setText("");
            textPersContact.setText("");
            textTelefon.setText("");

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

        if (menuItemIndex == 1 && listViewSelPos >= 0) {

            Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmdGed.class);
            nextScreen.putExtra("totalNegociat", String.valueOf(isTotalNegociat));
            nextScreen.putExtra("codClientVar", codClientVar);
            nextScreen.putExtra("depozitUnic", depozitUnic);
            nextScreen.putExtra("tipComanda", tipComanda);
            nextScreen.putExtra("rezervStoc", rezervStoc);
            nextScreen.putExtra("filialaAlternativa", filialaAlternativa);
            nextScreen.putExtra("canalDistrib", canalDistrib);
            nextScreen.putExtra("indexArticolModificat", String.valueOf(listViewSelPos));
            startActivity(nextScreen);

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

                if (!isLivrareCustodie())
                    menu.add(Menu.NONE, 1, 1, "Modifica pret/cantitate");

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void calculProcente() {
        return;
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

    private void updateCantClient(int selectedPos, double newCantClient) {
        listArticole.get(selectedPos).setCantitate(newCantClient);

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

                            if (DateLivrare.getInstance().getTermenPlata().trim().equals("") && !isLivrareCustodie()) {
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

    private boolean isLivrareCustodie() {
        return DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.LIVRARE_CUSTODIE;
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
                        String cmdSAP = "-1";

                        String alerteKA = "!";

                        isPragNumerarZiValid = false;

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

                        valideazaFinal();

                    }
                });

                myTimer.cancel();
            } else {
                mProgress.setProgress(progressVal);
            }

        }
    }

    private boolean isGreutateMaximaComanda() {

        if (1 == 1)
            return false;

        double greutateComanda = ListaArticoleComandaGed.getInstance().getGreutateKgArticole();

        if (greutateComanda > Constants.MAX_GREUTATE_CMD_KG && DateLivrare.getInstance().getTransport().equals("TRAP")) {
            return true;
        }

        return false;
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

        double valPragNumerar = UserInfo.getInstance().getMaxNumerarPJuridica();

        if (DateLivrare.getInstance().getTipPersClient().equals("PF"))
            valPragNumerar = UserInfo.getInstance().getMaxNumerarPFizica();

        if (totalComanda + Double.valueOf(totalNumerar) > valPragNumerar) {

            isPragNumerarZiValid = false;

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

        } else {
            isPragNumerarZiValid = true;
            performSaveCmd();
        }
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


    private boolean isConditiiCostTransport() {
        return DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaDL_TRAP() ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_AMOB) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.LIVRARE_CUSTODIE);
    }

    private void getLivrariMathaus() {

        ListaArticoleComandaGed.getInstance().reseteazaArticoleLivrare();

        List<ArticolComanda> articoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
        ComandaMathaus comandaMathaus = new ComandaMathaus();

        String filialaLivrareMathaus = CreareComandaGed.filialaAlternativa;
        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE && DateLivrare.getInstance().getCodFilialaCLP().equals("BV90"))
            filialaLivrareMathaus = DateLivrare.getInstance().getCodFilialaCLP();
        else if ((DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_VANZARE) && DateLivrare.getInstance().getTransport().equals("TCLI") &&
                !DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog().trim().isEmpty())
            filialaLivrareMathaus = DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog();
        else if (DateLivrare.getInstance().getDatePoligonLivrare() != null &&
                !DateLivrare.getInstance().getDatePoligonLivrare().getFilialaPrincipala().trim().isEmpty() &&
                DateLivrare.getInstance().getTransport().equals("TRAP"))
            filialaLivrareMathaus = DateLivrare.getInstance().getDatePoligonLivrare().getFilialaPrincipala();

        comandaMathaus.setSellingPlant(filialaLivrareMathaus);
        List<DateArticolMathaus> listArticoleMat = new ArrayList<DateArticolMathaus>();

        String codDepartLivr = UserInfo.getInstance().getCodDepart();
        if (UserInfo.getInstance().getTipUserSap().contains("KA"))
            codDepartLivr = "10";

        String localCanal = "20";
        if (UtilsUser.isUserCVOB())
            localCanal = "60";

        for (ArticolComanda artCmd : articoleComanda) {

            List<BeanStocTCLI> listStocTCLI;

            if (HelperMathaus.isConditiiDepozitTCLI(artCmd, "20")) {
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
        antetComanda.setComandaDL(DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.DISPOZITIE_LIVRARE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_COMANDA));
        antetComanda.setNrCmdSap("");
        antetComanda.setStrada(DateLivrare.getInstance().getStrada());
        antetComanda.setCodFurnizor(UtilsComenzi.getCodFurnizorDL());

        copyLivrareMathaus(antetComanda, comandaMathaus);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("antetComanda", opArticol.serializeAntetCmdMathaus(antetComanda));
        params.put("comandaMathaus", opArticol.serializeComandaMathaus(comandaMathaus));
        params.put("canal", localCanal);
        params.put("datePoligon", opArticol.serializeDatePoligon(DateLivrare.getInstance().getDatePoligonLivrare()));

        comandaDAO.getLivrariMathaus(params);

    }

    private boolean isComandaDL_TRAP() {
        return tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE) && DateLivrare.getInstance().getTransport().equals("TRAP");
    }

    private void setLivrariMathaus(LivrareMathaus livrareMathaus) {

        if (isConditiiCostTransport())
            DateLivrare.getInstance().setCostTransportMathaus(livrareMathaus.getCostTransport());

        List<DateArticolMathaus> articoleMathaus = livrareMathaus.getComandaMathaus().getDeliveryEntryDataList();
        List<ArticolComanda> articoleComandaGed = ListaArticoleComandaGed.getInstance().getListArticoleComanda();

        String codArticolComanda;
        for (ArticolComanda articolComanda : articoleComandaGed) {

            articolComanda.setTipTransport(null);

            if (tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE))
                articolComanda.setTipTransport(DateLivrare.getInstance().getTransport());

            if ((tipComandaGed.equals(TipCmdGed.COMANDA_LIVRARE) || UtilsComenzi.isComandaClp()) && articolComanda.getArticolMathaus() == null)
                articolComanda.setFilialaSite(DateLivrare.getInstance().getCodFilialaCLP());


            if (articolComanda.getArticolMathaus() == null) {
                ArticolComandaGed articolLivrare = ListaArticoleComandaGed.getInstance().genereazaArticolLivrare((ArticolComandaGed) articolComanda);
                articolLivrare.setCmpCorectat(HelperMathaus.getCmpCorectat(articolComanda.getCodArticol(), livrareMathaus));
                HelperMathaus.setFilialaSite(articolLivrare, livrareMathaus);
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
                    articolLivrare.setCmpCorectat(articolMathaus.getCmpCorectat());

                    articolLivrare.setCantitate50(HelperMathaus.getCantitateCanal50(articolMathaus, articolComanda));
                    articolLivrare.setUm50(articolComanda.getUm50());

                    if (HelperMathaus.isConditiiDepozitTCLI(articolComanda, "20") && articolMathaus.getCantUmb() == 0) {
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

                    if (!tipComandaGed.equals(TipCmdGed.DISPOZITIE_LIVRARE))
                        articolLivrare.setDepozit(articolMathaus.getDepozit());

                    ListaArticoleComandaGed.getInstance().getListArticoleLivrare().add(articolLivrare);
                }

            }

        }

        if (isExceptieComandaIP()) {

        } else if ((DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaDL_TRAP() ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE) ||
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.LIVRARE_CUSTODIE)))
            HelperMathaus.adaugaArticolTransport(livrareMathaus.getCostTransport(), "20", null);


        recalculTotal();

        if (isExceptieComandaIP())
            verificaAprobareIP();
        else
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);


    }


    private void setTaxeMasini(String result) {

        livrareMathaus = opArticol.deserializeLivrareMathaus(result);
        livrareMathaus.getCostTransport().clear();

        boolean existaTRAP = existaTransportTRAP(livrareMathaus);

        if (DateLivrare.getInstance().getTransport().equals("TRAP") && existaTRAP) {

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);

            int nrFilialeLivrare = getNrFilialeTransport(livrareMathaus.getTaxeMasini());

            if (livrareMathaus.getListPaleti().isEmpty()) {

                if (nrFilialeLivrare == 1)
                    height = (int) (getResources().getDisplayMetrics().heightPixels * 0.3);
                else
                    height = (int) (getResources().getDisplayMetrics().heightPixels * 0.4);
            }

            TaxeMasiniDialog taxeMasiniDialog = new TaxeMasiniDialog(this, livrareMathaus);
            taxeMasiniDialog.setTipMasinaLivrareListener(this);
            taxeMasiniDialog.getWindow().setLayout(width, height);
            taxeMasiniDialog.show();

        } else {
            HelperMathaus.setTransportTERT(livrareMathaus);
            trateazaTransportIP();
            setLivrariMathaus(livrareMathaus);
        }

    }

    private void trateazaTransportIP() {
        costTransportIP = 0;
        taxeComandaIP = new ArrayList<>();

        if (isExceptieComandaIP())
            eliminaCostTransport();
    }

    @Override
    public void tipMasinaFilialaSelected(LivrareMathaus livrareMathaus, CostDescarcare costDescarcare, List<TaxaTransport> listTaxeTransport) {

        this.costDescarcare = costDescarcare;
        this.listTaxeTransport = listTaxeTransport;

        trateazaTransportIP();

        if (!DateLivrare.getInstance().isClientFurnizor())
            verificaPaletiComanda(costDescarcare.getArticolePaleti());

        this.costDescarcareFiliala = HelperMathaus.getCostDescarcareFiliala(listTaxeTransport);

        DateLivrare.getInstance().setMasinaMacara(!costDescarcareFiliala.getArticoleDescarcare().isEmpty());

        List<ArticolComanda> articoleDescarcare = HelperCostDescarcare.getArticoleDescarcare(costDescarcareFiliala, 0, ListaArticoleComandaGed.getInstance().getListArticoleComanda());
        ListaArticoleComandaGed.getInstance().getListArticoleLivrare().addAll(articoleDescarcare);

        for (ArticolPalet articolPalet : costDescarcare.getArticolePaleti()) {
            if (articolPalet.getCantitate() > 0)
                adaugaPaletComanda(articolPalet);
        }

        setLivrariMathaus(livrareMathaus);

    }

    private void adaugaPaletComanda(ArticolPalet articolPalet) {

        String depozitPalet = HelperCostDescarcare.getDepozitPalet(ListaArticoleComandaGed.getInstance().getListArticoleComanda(), articolPalet.getCodArticol());

        ArticolComanda articol = HelperCostDescarcare.getArticolPalet(articolPalet, depozitPalet, articolPalet.getFiliala());

        if (articol.getCantitate() > 0)
            ListaArticoleComandaGed.getInstance().addArticolLivrareComanda(articol);

    }

    private boolean existaTransportTRAP(LivrareMathaus livrareMathaus) {

        if (livrareMathaus.getTaxeMasini() == null)
            return false;

        for (TaxaMasina taxaMasina : livrareMathaus.getTaxeMasini()) {
            if (taxaMasina.getTraty().equals("TRAP"))
                return true;
        }

        return false;
    }

    private int getNrFilialeTransport(List<TaxaMasina> taxeMasini) {

        Set<String> filialeLivrare = new HashSet<>();

        for (TaxaMasina taxaMasina : taxeMasini) {
            if (!taxaMasina.getTraty().equals("TRAP"))
                continue;

            filialeLivrare.add(taxaMasina.getWerks());
        }

        return filialeLivrare.size();
    }


    private void eliminaCostTransport() {
        if (livrareMathaus.getCostTransport() != null && !livrareMathaus.getCostTransport().isEmpty())
            for (CostTransportMathaus costTrans : livrareMathaus.getCostTransport()) {

                TaxaComanda taxa = new TaxaComanda();
                taxa.setFiliala(costTrans.getFiliala());
                taxa.setValoare(Double.valueOf(costTrans.getValTransp()));
                taxeComandaIP.add(taxa);

                costTransportIP += Double.valueOf(costTrans.getValTransp());
                costTrans.setValTransp("0");
            }

        if (costDescarcare != null && !costDescarcare.getArticoleDescarcare().isEmpty()) {

            for (ArticolDescarcare articolDescarcare : costDescarcare.getArticoleDescarcare()) {

                TaxaComanda taxa = new TaxaComanda();
                taxa.setFiliala(articolDescarcare.getFiliala());
                taxa.setValoare(articolDescarcare.getValoare() * articolDescarcare.getCantitate());
                taxeComandaIP.add(taxa);

                costTransportIP += Double.valueOf(taxa.getValoare());
                articolDescarcare.setValoare(0);

            }

        }
    }

    public void verificaAprobareIP() {

        double totalAdaos = 0;
        double adaosArticol = 0;
        for (ArticolComanda articolComanda : ListaArticoleComandaGed.getInstance().getListArticoleLivrare()) {

            if (articolComanda.getPretMinim() == 0 || articolComanda.getCmpCorectat() == 0)
                continue;

            adaosArticol = (articolComanda.getPretUnitarClient() / articolComanda.getMultiplu() -
                    articolComanda.getPretMinim() / articolComanda.getMultiplu()) * articolComanda.getCantitate();
            totalAdaos += adaosArticol;

        }

        DateLivrare.getInstance().setTaxeComanda(taxeComandaIP);

        double totalTaxe = 0;
        for (TaxaComanda taxaComanda : taxeComandaIP) {
            totalTaxe += taxaComanda.getValoare();
        }

        if (totalAdaos >= totalTaxe)
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);
        else
            afisCostServiciiComandaIP(false, totalTaxe - totalAdaos);

    }

    private void afisCostServiciiComandaIP(boolean isBlocat, double valoareTaxe) {

        MarjaComandaIPDialog infoDialog = new MarjaComandaIPDialog(this, isBlocat, valoareTaxe);
        infoDialog.setMarjaComamdaIPListener(this);
        infoDialog.show();

    }

    @Override
    public void comandaIPSelected(boolean isBlocat) {
        if (!isBlocat)
            afisRezumatComandaDialog(livrareMathaus.getCostTransport(), true);

    }

    private void afisRezumatComandaDialog(List<CostTransportMathaus> costTransport, boolean selectTransp) {

        saveComandaMathaus = false;

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);

        rezumatComanda = new RezumatComandaDialog(this, ListaArticoleComandaGed.getInstance().getListArticoleLivrare(), "20", costTransport,
                DateLivrare.getInstance().getTransport(), CreareComandaGed.filialeArondateMathaus, selectTransp);
        rezumatComanda.setRezumatListener(this);
        rezumatComanda.getWindow().setLayout(width, height);
        rezumatComanda.show();

        comandaEliminata();

    }

    private boolean isExceptieComandaIP() {
        return UtilsUser.isUserIP() && tipClient.equals("IP") && tipClientIP == EnumTipClientIP.CONSTR;
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

    private boolean isPragNumerarComandaValid() {

        double totalComandaNumerar = 0;

        for (ArticolComanda articolCmd : ListaArticoleComandaGed.getInstance().getListArticoleLivrare()) {
            totalComandaNumerar += articolCmd.getPretUnitarClient() * articolCmd.getCantUmb();
        }

        DateLivrare dateLivrareInstance = DateLivrare.getInstance();

        if ((dateLivrareInstance.getTipPlata().equals("E") || dateLivrareInstance.getTipPlata().equals("E1") || dateLivrareInstance.getTipPlata().equals("N") || dateLivrareInstance.getTipPlata().equals("R"))
                && CreareComandaGed.tipClient.equals("PJ")) {
            if (totalComandaNumerar > UserInfo.getInstance().getMaxNumerarPJuridica()) {
                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de " + UserInfo.getInstance().getMaxNumerarPJuridica()
                        + " RON!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                getTotalComenziNumerar();
                return false;
            }

        } else if (isCondPF10_000()) {
            if (totalComandaNumerar > UserInfo.getInstance().getMaxNumerarPFizica()) {
                Toast.makeText(getApplicationContext(), "Pentru plata in numerar valoarea maxima este de " + UserInfo.getInstance().getMaxNumerarPFizica()
                        + " RON.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                getTotalComenziNumerar();
                return false;
            }
        } else if (isGreutateMaximaComanda()) {
            Toast.makeText(getApplicationContext(), Constants.MSG_MASA_MAXIMA_CMD, Toast.LENGTH_LONG).show();
            return false;
        } else {
            isPragNumerarZiValid = true;
            return true;
        }

    }

    private void performSaveCmd() {
        try {

            if (isLivrareCustodie()) {
                salveazaLivrareCustodie();
                return;
            }

            if (!isPragNumerarZiValid && !isPragNumerarComandaValid()) {
                saveComandaMathaus = false;
                return;
            }

            HashMap<String, String> params = new HashMap<>();

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

    private void salveazaLivrareCustodie() {

        comandaFinala.setCodClient(codClientVar);
        comandaFinala.setComandaBlocata(comandaBlocata);
        comandaFinala.setFilialaAlternativa(CreareComandaGed.filialaAlternativa);
        comandaFinala.setUserSite(UserInfo.getInstance().getUserSite());

        prepareArtForDelivery();

        performSaveLivrareCustodie();

    }

    private void performSaveLivrareCustodie() {
        try {

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("JSONArt", serializeArticole(prepareArtForDelivery()));
            params.put("JSONComanda", serializeComanda(comandaFinala));
            params.put("JSONDateLivrare", serializeDateLivrare());

            comandaDAO.salveazaLivrareCustodie(params);

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

                obj.put("cmpCorectat", listArticole.get(i).getCmpCorectat());
                obj.put("pretMinim", listArticole.get(i).getPretMinim());

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
            obj.put("codPostal", DateLivrare.getInstance().getCodPostal());
            obj.put("taxeComanda", opArticol.serializeTaxeComanda(DateLivrare.getInstance().getTaxeComanda()));
            obj.put("zona", DateLivrare.getInstance().getDatePoligonLivrare().getTipZona());
            obj.put("isComandaCustodie", DateLivrare.getInstance().isComandaCustodie());

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


    private void saveCmdStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {

            Toast.makeText(getApplicationContext(), ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();
            clearAllData();

            ActionBar actionBar = getActionBar();
            actionBar.setTitle("Comanda GED");

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

        if (!UserInfo.getInstance().getTipUserSap().equals("ASDL"))
            UserInfo.getInstance().setUnitLog(UserInfo.getInstance().getInitUnitLog());

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

    private void displayCmdGEDStatus(String saveResponse) {
        if (!saveResponse.equals("-1")) {

            clearAllData();
            Toast.makeText(getApplicationContext(), ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.parseInt(saveResponse)), Toast.LENGTH_SHORT).show();

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
            if (tipComandaGed == TipCmdGed.ARTICOLE_COMANDA || tipComandaGed == TipCmdGed.DISPOZITIE_LIVRARE) {
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

        valTranspBtn.setVisibility(View.INVISIBLE);
        valTransport = 0;

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
    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
        switch (methodName) {

            case GET_LIVRARI_MATHAUS:
                setTaxeMasini((String) result);
                break;
            case GET_TOTAL_COMENZI_NUMERAR:
                afisTotalComenziNumerar((String) result);
                break;
            case GET_ARTICOLE_AMOB:
                listArticoleAMOB = comandaDAO.deserializeArticoleAmob((String) result);
                break;

            case SALVEAZA_LIVRARE_CUSTODIE:
                saveLivrareStatus((String) result);
                break;
            default:
                break;

        }

    }


    @Override
    public void tipComandaSelected(TipCmdGed tipSelected, String idComanda, String codFilialaClp) {

        clearAllData();

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
        } else if (tipSelected == TipCmdGed.LIVRARE_CUSTODIE) {
            actionBar.setTitle("Livrare din custodie");
        } else if (tipSelected.equals(TipCmdGed.COMANDA_LIVRARE)) {
            actionBar.setTitle("Comanda livrare" + " " + codFilialaClp);
        } else if (tipSelected == TipCmdGed.ARTICOLE_COMANDA) {
            actionBar.setTitle("Articole la comanda");
        } else if (tipSelected == TipCmdGed.ARTICOLE_DETERIORATE) {
            actionBar.setTitle("Articole deteriorate 30%");
        } else if (tipSelected == TipCmdGed.COMANDA_FASONATE) {
            actionBar.setTitle("Comanda otel fasonat");
        } else {
            actionBar.setTitle("Comanda GED");
            selectedDepartIndexClp = -1;
            selectedDepozIndexClp = -1;
            selectedDepartCod = "-1";
        }

        invalidateOptionsMenu();

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
