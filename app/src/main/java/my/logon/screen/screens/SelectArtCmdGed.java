/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.ArticolAmob;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.ArticolMathaus;
import my.logon.screen.beans.BeanArticolCautare;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.ComandaExtraMathaus;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.DepoziteUl;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.beans.StocMathaus;
import my.logon.screen.dialogs.Cabluri05Dialog;
import my.logon.screen.dialogs.CategoriiMathausDialogNew;
import my.logon.screen.dialogs.RecomArtDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.filters.DecimalDigitsInputFilter;
import my.logon.screen.helpers.HelperComenzi;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.listeners.ArticolMathausListener;
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.MagazinMathausListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.DownloadImageTask;
import my.logon.screen.model.HelperUserSite;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.patterns.StocMathausComparator;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsArticole;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectArtCmdGed extends ListActivity implements OperatiiArticolListener, ArticolMathausListener, MagazinMathausListener, Cablu05SelectedListener {

    Button articoleBtn, saveArtBtn, pretBtn;
    String filiala = "", nume = "", cod = "", umStoc = "";
    String articolResponse = "";
    String pretResponse = "";
    String codArticol = "";
    String numeArticol = "", tipArticol = "";
    String depart = "";

    String numeClientVar = "";
    LinearLayout redBtnTable, layoutStocKA;
    EditText valRedIntText, valRedDecText;

    public String globalDepozSel = "", artPromoText = "", globalCodDepartSelectetItem = "";

    ToggleButton tglButton, tglTipArtBtn;

    private EditText txtNumeArticol, textProcRed;
    private TextView textCodArticol, txtPretArt;
    private TextView textNumeArticol;

    ToggleButton tglProc;

    private TextView textStoc;
    private TextView textCant, procDisc, textPretGED, textMultipluArt;

    private TextView textUM;
    private TextView labelCant, labelStoc;
    private Spinner spinnerDepoz, spinnerUnitMas;

    private TextView textPromo, textCondPret, labelFactConv;

    private boolean pretMod = false;

    private double initPrice = 0, cmpArt = 0;
    private double finalPrice = 0;
    private double listPrice = 0, procDiscClient = 0;

    private double procentAprob = 0, selectedCant = 0;
    private double pretMediuDistrib = 0, adaosMediuDistrib = 0;
    private double valoareUmrez = 1, valoareUmren = 1;

    private static ArrayList<HashMap<String, String>> listUmVanz = null;
    public SimpleAdapter adapterUmVanz;
    private double varProc = 0, valMultiplu = 0, pretVanzare = 0;

    String tipAlert = "", codPromo = "0", infoArticol = "", Umb = "", cantUmb = "", selectedUnitMas = "", selectedUnitMasPret = "";

    private HashMap<String, String> artMap = null;
    double procR = 0, globalCantArt = 0;

    Dialog dialogSelFilArt;

    private double discMaxAV = 0, discMaxSD = 0;

    OperatiiArticol opArticol;

    String selectedDepartamentAgent;
    LinearLayout resultLayout;

    private boolean totalNegociat;
    public static String codClientVar;

    private String tipComanda;
    private boolean rezervStoc;
    private String filialaAlternativa;

    private double coefCorectie;
    private ArrayAdapter<String> adapterSpinnerDepozite;
    private LinearLayout layoutPretGEDFTva;
    private TextView textPretGEDFTva, textTransport, textDataExp;
    private double procentTVA, procentTransport, valoareTransport;
    private PretArticolGed selectedArticol;

    private ArticolDB articolDBSelected;
    private TextView txtImpachetare;

    private static String unitLogUnic = "";
    private boolean isFilialaMavSite = false;

    private enum EnumDepoz {
        MAV1;
    }

    private String tipPersClient;
    private ArticolAmob articolAmob;
    private boolean importAllAmob = false;
    private int currentAmob = 0;
    private Spinner spinnerDepart;

    private CategoriiMathausDialogNew categoriiDialog;
    private Button btnCatMathaus;
    private Button btnStocMathaus;
    private ArticolMathaus articolMathaus;
    private boolean isDepartMathaus;
    private double stocExtraMathaus;
    private List<StocMathaus> artStocMathaus;
    private ComandaExtraMathaus cmdExtraMathaus;
    private List<BeanCablu05> listCabluri;

    private boolean cautaStocBV90 = false;
    private boolean verificatStocBV90 = false;
    private String filialaStocBV90 = "";
    private Button btnArtRecom;
    private List<BeanArticolCautare> listArtRecom;

    private List<BeanStocTCLI> listStocTCLI;

    private boolean isArticolModificatCantPret;
    private ArticolComanda articolModificat;

    private LayoutInflater mInflater;
    private View mCustomView;
    private Intent intent;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.selectartcmd_ged_header);

        intent = getIntent();

        totalNegociat = Boolean.valueOf(intent.getStringExtra("totalNegociat"));
        codClientVar = intent.getStringExtra("codClientVar");

        tipComanda = intent.getStringExtra("tipComanda");
        rezervStoc = Boolean.valueOf(intent.getStringExtra("rezervStoc"));
        filialaAlternativa = intent.getStringExtra("filialaAlternativa");

        tipPersClient = intent.getStringExtra("tipPersClient");

        if (isCV() && tipPersClient != null && !tipPersClient.isEmpty())
            DateLivrare.getInstance().setTipPersClient(tipPersClient);

        opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
        opArticol.setListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
        spinnerDepart = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

        initSelectionDepartament();

        addSpinnerDepartamente();

        resultLayout = (LinearLayout) findViewById(R.id.resLayout);
        resultLayout.setVisibility(View.INVISIBLE);

        labelFactConv = (TextView) findViewById(R.id.labelFactConv);
        labelFactConv.setVisibility(View.INVISIBLE);

        layoutStocKA = (LinearLayout) findViewById(R.id.layoutStocKA);
        layoutStocKA.setVisibility(View.INVISIBLE);

        this.articoleBtn = (Button) findViewById(R.id.articoleBtn);
        addListenerBtnArticole();

        this.saveArtBtn = (Button) findViewById(R.id.saveArtBtn);
        addListenerBtnSaveArt();

        this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
        addListenerToggle();
        this.tglButton.setChecked(true);

        this.tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
        addListenerTglTipArtBtn();

        this.redBtnTable = (LinearLayout) findViewById(R.id.RedBtnTable);

        txtPretArt = (TextView) findViewById(R.id.txtPretArt);

        this.tglProc = (ToggleButton) findViewById(R.id.tglProc);
        addListenerTglProc();

        this.pretBtn = (Button) findViewById(R.id.pretBtn);
        addListenerPretBtn();

        textProcRed = (EditText) findViewById(R.id.textProcRed);
        textProcRed.setFocusableInTouchMode(true);
        addListenerProcArt();

        if (CreareComandaGed.tipClient.equals("IP"))
            textProcRed.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        else
            textProcRed.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3)});

        procDisc = (TextView) findViewById(R.id.procDisc);

        textMultipluArt = (TextView) findViewById(R.id.txtMultipluArt);

        txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
        textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
        textCodArticol = (TextView) findViewById(R.id.textCodArticol);
        textUM = (TextView) findViewById(R.id.textUm);
        textStoc = (TextView) findViewById(R.id.textStoc);
        textCant = (EditText) findViewById(R.id.txtCantArt);
        labelCant = (TextView) findViewById(R.id.labelCant);
        labelStoc = (TextView) findViewById(R.id.labelStoc);
        textCondPret = (TextView) findViewById(R.id.textCondPret);
        textPretGED = (TextView) findViewById(R.id.textPretGED);

        txtImpachetare = (TextView) findViewById(R.id.txtImpachetare);

        textPromo = (TextView) findViewById(R.id.textPromo);
        addListenerTextPromo();

        txtNumeArticol.setHint("Introduceti cod articol");

        spinnerDepoz = (Spinner) findViewById(R.id.spinnerDepoz);

        ArrayList<String> arrayListDepozite = new ArrayList<String>();

        if (isWood())
            arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteWood()));
        else if (UtilsUser.isUserSite())
            arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteSite()));
        else
            arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteGed()));

        adapterSpinnerDepozite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListDepozite);

        UtilsGeneral.trateazaExceptieMAV_BU(adapterSpinnerDepozite);

        adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepoz.setAdapter(adapterSpinnerDepozite);
        spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

        if (isWood() || UtilsUser.isCV())
            setDefaultDepoz(EnumDepoz.MAV1, arrayListDepozite);

        if (DateLivrare.getInstance().getTransport().equals("TCLI") && UtilsGeneral.isFilMareLivrTCLIGed() && DateLivrare.getInstance().getFilialaLivrareTCLI() != null && !DateLivrare.getInstance().getFilialaLivrareTCLI().isEmpty()) {
            spinnerDepoz.setEnabled(false);
            if (UtilsGeneral.isUlDistrib(DateLivrare.getInstance().getFilialaLivrareTCLI())) {
                spinnerDepoz.setSelection(0);
            } else {
                UtilsGeneral.setSpinnerSelectedItem(spinnerDepoz, "MAV1");
            }
        } else
            spinnerDepoz.setEnabled(true);

        spinnerUnitMas = (Spinner) findViewById(R.id.spinnerUnitMas);

        listUmVanz = new ArrayList<HashMap<String, String>>();
        adapterUmVanz = new SimpleAdapter(this, listUmVanz, R.layout.simplerowlayout, new String[]{"rowText"}, new int[]{R.id.textRowName});
        spinnerUnitMas.setVisibility(View.GONE);
        spinnerUnitMas.setOnItemSelectedListener(new OnSelectUnitMas());

        textNumeArticol.setVisibility(View.INVISIBLE);
        textCodArticol.setVisibility(View.INVISIBLE);
        textUM.setVisibility(View.INVISIBLE);

        textStoc.setVisibility(View.INVISIBLE);
        textCant.setVisibility(View.INVISIBLE);

        labelCant.setVisibility(View.INVISIBLE);

        labelStoc.setVisibility(View.INVISIBLE);
        saveArtBtn.setVisibility(View.INVISIBLE);

        redBtnTable.setVisibility(View.INVISIBLE);
        textProcRed.setVisibility(View.INVISIBLE);
        pretBtn.setVisibility(View.INVISIBLE);
        textPromo.setVisibility(View.INVISIBLE);
        procDisc.setVisibility(View.INVISIBLE);
        textCondPret.setVisibility(View.INVISIBLE);
        textPretGED.setVisibility(View.INVISIBLE);
        textMultipluArt.setVisibility(View.INVISIBLE);

        layoutPretGEDFTva = (LinearLayout) findViewById(R.id.layoutPretGEDFTva);
        layoutPretGEDFTva.setVisibility(View.GONE);

        textPretGEDFTva = (TextView) findViewById(R.id.textPretGEDFTVA);
        textTransport = (TextView) findViewById(R.id.textTransport);
        textDataExp = (TextView) findViewById(R.id.textDataExp);

        if (UserInfo.getInstance().getTipUserSap().equals("KA3") && DateLivrare.getInstance().getTipPersClient().equals("D")) {
            layoutPretGEDFTva.setVisibility(View.VISIBLE);
        }

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
            ((LinearLayout) findViewById(R.id.cautareArticoleLayout)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.articoleAmobHeaderLayout)).setVisibility(View.VISIBLE);

            Button importAllBtn = (Button) findViewById(R.id.importAmob);
            afiseazaArticoleAmob();
            setImportAmobButtonListener(importAllBtn);

        } else {
            ((LinearLayout) findViewById(R.id.cautareArticoleLayout)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.articoleAmobHeaderLayout)).setVisibility(View.GONE);
        }

        if (spinnerDepart != null)
            spinnerDepart.setEnabled(true);

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_LIVRARE) {

            if (ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() == 0) {
                CreareComandaGed.selectedDepozIndexClp = -1;
                CreareComandaGed.selectedDepartIndexClp = -1;
                CreareComandaGed.selectedDepartCod = "-1";
            }

            if (CreareComandaGed.selectedDepozIndexClp != -1) {

                if (spinnerDepart != null) {
                    spinnerDepart.setSelection(CreareComandaGed.selectedDepartIndexClp);
                    spinnerDepart.setEnabled(false);
                }

                spinnerDepoz.setSelection(CreareComandaGed.selectedDepozIndexClp);
                spinnerDepoz.setEnabled(false);
            }
        } else if (isModifComandaCLP()) {

            blockDepartCLPGed();

        }

        btnCatMathaus = (Button) findViewById(R.id.btnBackToList);
        setCatMathausListener();

        btnStocMathaus = (Button) findViewById(R.id.btnStocMathaus);
        setStocMathausListener();

        btnArtRecom = (Button) findViewById(R.id.afisArtRecomBtn);
        setArtRecomListener();

        isArticolModificatCantPret = false;
        articolModificat = null;


        if (intent.getStringExtra("indexArticolModificat") != null) {
            isArticolModificatCantPret = true;
            int indexArtModif = Integer.parseInt(intent.getStringExtra("indexArticolModificat"));
            articolModificat = ListaArticoleComandaGed.getInstance().getListArticoleComanda().get(indexArtModif);
            trateazaModificarePretCantitate(articolModificat);

        }

    }

    private void setupSpinnerDepozModifArticol() {

        if (articolModificat == null || articolModificat.getDepozit() == null)
            return;

        if (articolModificat.getArticolMathaus() != null)
            return;

        if (articolModificat.getDepozit().startsWith("0")) {
            String depozitArticol = articolModificat.getDepozit().substring(2, 4);

            for (int ii = 0; ii < spinnerDepoz.getAdapter().getCount(); ii++) {
                if (spinnerDepoz.getItemAtPosition(ii).toString().startsWith(depozitArticol)) {
                    spinnerDepoz.setSelection(ii);
                    break;
                }
            }
        } else
            spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition(articolModificat.getDepozit()));
    }

    private void setupSpinnerDepartModifArticol() {

        if (articolModificat == null)
            return;

        if (articolModificat.getArticolMathaus() == null) {
            spinnerDepart.setSelection(0);
        } else
            spinnerDepart.setSelection(spinnerDepart.getAdapter().getCount() - 1);

        spinnerDepart.setVisibility(View.INVISIBLE);

    }

    private void trateazaModificarePretCantitate(ArticolComanda articolModificat) {

        setupSpinnerDepartModifArticol();

        if (articolModificat.getArticolMathaus() == null) {
            isDepartMathaus = false;
            CreareComandaGed.filialaAlternativa = articolModificat.getFilialaSite();
            populateListViewArticol(Arrays.asList(HelperComenzi.getArticolDB(articolModificat)));
            setupSpinnerDepozModifArticol();
            getListView().performItemClick(getListView().getAdapter().getView(0, null, null), 0, getListView().getItemIdAtPosition(0));

        } else {
            isDepartMathaus = true;
            articolMathausSelected(articolModificat.getArticolMathaus());
        }

    }

    private boolean isModifComandaCLP() {
        return !ModificareComanda.codClientVar.trim().isEmpty() && !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty()
                && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
    }

    private String blockDepartCLPGed() {

        String codDepart = "-1";
        String codDepoz = "-1";

        for (ArticolComanda articol : ListaArticoleComandaGed.getInstance().getListArticoleComanda()) {
            if (!articol.getCodArticol().startsWith("3")) {
                codDepart = articol.getDepartSintetic();
                codDepoz = articol.getDepozit();
                break;
            }
        }

        String numeDepart = EnumDepartExtra.getNumeDepart(codDepart);

        for (int i = 0; i < spinnerDepart.getAdapter().getCount(); i++) {
            if (numeDepart.equals(spinnerDepart.getItemAtPosition(i))) {
                spinnerDepart.setSelection(i);
                spinnerDepart.setEnabled(false);
                break;
            }
        }

        if (codDepoz.startsWith("0"))
            codDepoz = codDepoz.substring(2, 4);

        for (int i = 0; i < spinnerDepoz.getAdapter().getCount(); i++) {

            if (spinnerDepoz.getItemAtPosition(i).toString().startsWith(codDepoz)) {
                spinnerDepoz.setSelection(i);
                spinnerDepoz.setEnabled(false);
                break;
            }

        }

        return codDepart;
    }

    private void setDefaultDepoz(EnumDepoz depoz, ArrayList<String> listDepozite) {

        if (depoz == EnumDepoz.MAV1) {
            int position = 0;
            for (String dep : listDepozite) {
                if (dep.equals(depoz.toString())) {
                    spinnerDepoz.setSelection(position);
                    break;
                }
                position++;
            }
        }

    }

    private void setImportAmobButtonListener(Button importAmobButton) {
        importAmobButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                proceseazaArticoleAmob();

            }
        });
    }

    private void proceseazaArticoleAmob() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {

        }

        int pos = 0;
        for (ArticolAmob articol : CreareComandaGed.listArticoleAMOB) {
            if (!articol.isProcesat()) {
                importAllAmob = true;
                getListView().performItemClick(getListView(), pos, getListView().getItemIdAtPosition(pos));
                break;
            }
            importAllAmob = false;
            pos++;
        }

    }

    private void setProcesatArticolAmob(String codArticol, String globalDepozSel) {

        for (ArticolAmob articol : CreareComandaGed.listArticoleAMOB) {
            if (codArticol.contains(articol.getCodArticol())) {
                articol.setProcesat(true);
                break;
            }
        }

    }

    private void addSpinnerDepartamente() {

        List<String> departamenteComanda = DepartamentAgent.getDepartamenteAgentGED();

        if (isComandaDL() || tipComanda.equals("S"))
            departamenteComanda.remove("Catalog site");

        else if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_VANZARE) {

            String localFilialaMathaus = CreareComandaGed.filialaLivrareMathaus;

            if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
                localFilialaMathaus = DateLivrare.getInstance().getCodFilialaFasonate();

            if (!UtilsGeneral.isInFilialaArondate(localFilialaMathaus, UserInfo.getInstance().getUnitLog())) {
                departamenteComanda.clear();
                departamenteComanda.add("Catalog site");
                DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_LIVRARE);
                DateLivrare.getInstance().setCodFilialaCLP(localFilialaMathaus);
            } else {
                DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_VANZARE);
                DateLivrare.getInstance().setCodFilialaCLP("");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
                departamenteComanda);

        spinnerDepart.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (spinnerDepart.getSelectedItem().toString().equalsIgnoreCase("catalog site")) {
                    selectedDepartamentAgent = "11";
                    isDepartMathaus = true;
                    spinnerDepoz.setVisibility(View.INVISIBLE);
                } else {
                    selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerDepart.getSelectedItem().toString());
                    isDepartMathaus = false;
                    spinnerDepoz.setVisibility(View.VISIBLE);
                }

                if (!isArticolModificatCantPret)
                    populateListViewArticol(new ArrayList<ArticolDB>());

                if (selectedDepartamentAgent.equals("11") || selectedDepartamentAgent.equals("05")) {

                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());

                    UtilsGeneral.trateazaExceptieMAV_BU(adapterSpinnerDepozite);

                    if (selectedDepartamentAgent.equals("11")) {
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));

                        if (isDepartMathaus) {
                            setArtMathausVisibility(true);

                            if (articolModificat == null)
                                showCategoriiMathaus();
                        } else
                            setArtMathausVisibility(false);

                    } else
                        spinnerDepoz.setSelection(0);
                } else {
                    setArtMathausVisibility(false);
                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());
                    UtilsGeneral.trateazaExceptieMAV_BU(adapterSpinnerDepozite);
                    spinnerDepoz.setSelection(0);
                }

                if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE)) {
                    spinnerDepoz.setEnabled(false);
                    if (spinnerDepart.getSelectedItem().toString().equalsIgnoreCase("MAGAZIN") && selectedDepartamentAgent.equals("11"))
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAD1"));
                    else
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("D1 - deteriorate"));
                }

                setupSpinnerDepozModifArticol();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerDepart.setAdapter(adapter);
        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

    }

    private void showCategoriiMathaus() {

        if (categoriiDialog == null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels);
            int height = (int) (getResources().getDisplayMetrics().heightPixels);
            categoriiDialog = new CategoriiMathausDialogNew(SelectArtCmdGed.this);
            categoriiDialog.setFilialaLivrareMathaus(CreareComandaGed.filialaLivrareMathaus);
            categoriiDialog.setArticolMathausListener(this);
            categoriiDialog.getWindow().setLayout(width, height);

        }

        categoriiDialog.show();
        setArtMathausVisibility(true);
    }

    private void setArtMathausVisibility(boolean isVisible) {

        codArticol = "";
        articolMathaus = null;

        if (isVisible) {
            ((LinearLayout) findViewById(R.id.layout_list_view)).setVisibility(View.GONE);
            tglButton.setVisibility(View.INVISIBLE);
            articoleBtn.setVisibility(View.INVISIBLE);
            txtNumeArticol.setVisibility(View.INVISIBLE);
            tglTipArtBtn.setVisibility(View.INVISIBLE);
        } else {

            ((LinearLayout) findViewById(R.id.layout_list_view)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layout_articol_mathaus)).setVisibility(View.GONE);
            tglButton.setVisibility(View.VISIBLE);
            articoleBtn.setVisibility(View.VISIBLE);
            txtNumeArticol.setVisibility(View.VISIBLE);
            tglTipArtBtn.setVisibility(View.VISIBLE);
        }
    }

    private void afisArticolMathaus(ArticolMathaus articol) {

        resultLayout.setVisibility(View.INVISIBLE);
        textNumeArticol.setVisibility(View.INVISIBLE);
        textCodArticol.setVisibility(View.INVISIBLE);
        saveArtBtn.setVisibility(View.GONE);
        redBtnTable.setVisibility(View.GONE);
        textCant.setText("");

        articolDBSelected = articol;
        articolMathaus = articol;
        codArticol = articol.getCod();
        numeArticol = articol.getNume();
        ((LinearLayout) findViewById(R.id.layout_articol_mathaus)).setVisibility(View.VISIBLE);

        ImageView imageArt = (ImageView) findViewById(R.id.imageview_art);

        ((TextView) findViewById(R.id.text_nume_art)).setText(articol.getNume());
        ((TextView) findViewById(R.id.text_desc_art)).setText(articol.getDescriere());
        ((TextView) findViewById(R.id.text_desc_art)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView) findViewById(R.id.text_desc_art)).scrollTo(0, 0);

        if (articolModificat != null)
            ((Button) findViewById(R.id.btnBackToList)).setVisibility(View.INVISIBLE);

        int width = (int) (getApplicationContext().getResources().getDisplayMetrics().widthPixels * 0.45);
        int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.45);
        imageArt.getLayoutParams().width = width;
        imageArt.getLayoutParams().height = height;

        new DownloadImageTask(imageArt).execute(articol.getAdresaImgMare());

        if (DateLivrare.getInstance().getTransport().equals("TCLI") && DateLivrare.getInstance().getFilialaLivrareTCLI() != null && !DateLivrare.getInstance().getFilialaLivrareTCLI().isEmpty()) {
            globalCodDepartSelectetItem = articolMathaus.getDepart();
            articolMathaus.setTip2("");
            performListArtStoc();
        } else if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE)) {
            globalCodDepartSelectetItem = articolMathaus.getDepart();
            articolMathaus.setTip2("");
            getStocDeteriorate();
        } else
            btnStocMathaus.performClick();

    }


    private void setCatMathausListener() {
        btnCatMathaus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                categoriiDialog.show();

            }
        });
    }

    private void setArtRecomListener() {
        btnArtRecom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder strArticole = new StringBuilder();

                for (BeanArticolCautare artRec : listArtRecom) {
                    strArticole.append(artRec.getNume());
                    strArticole.append("\n");
                }

                showStareComanda(strArticole.toString());

            }
        });
    }

    private void showStareComanda(String stareComanda) {
        RecomArtDialog infoDialog = new RecomArtDialog(this, listArtRecom, numeArticol);
        infoDialog.show();
    }

    private void setStocMathausListener() {
        btnStocMathaus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                globalCodDepartSelectetItem = articolMathaus.getDepart();

                articolDBSelected = articolMathaus;
                numeArticol = articolMathaus.getNume();

                textNumeArticol.setText(articolMathaus.getNume());
                textCodArticol.setText(articolMathaus.getCod());


                if (articolMathaus.getDepart().equals("11"))
                    globalDepozSel = "MAV1";
                else
                    globalDepozSel = articolMathaus.getDepart() + "V1";


                String umVanz = articolMathaus.getUmVanz();
                listUmVanz.clear();
                spinnerUnitMas.setVisibility(View.GONE);
                HashMap<String, String> tempUmVanz;
                tempUmVanz = new HashMap<String, String>();
                tempUmVanz.put("rowText", umVanz);

                listUmVanz.add(tempUmVanz);
                spinnerUnitMas.setAdapter(adapterUmVanz);


                //adaugat
                String filialaStocMathaus = CreareComandaGed.filialaAlternativa;
                if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE)
                    filialaStocMathaus = DateLivrare.getInstance().getCodFilialaCLP();

                String stocFilialaSecundara = HelperMathaus.getFilialaSecundara();

                if (!stocFilialaSecundara.isEmpty())
                    filialaStocMathaus += "," + stocFilialaSecundara;

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("filiala", filialaStocMathaus);
                params.put("codArticol", codArticol);
                params.put("um", umVanz);
                params.put("tipCmd", "G");
                params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
                params.put("codUser", UserInfo.getInstance().getCod());
                params.put("tipZona", DateLivrare.getInstance().getDatePoligonLivrare().getTipZona());

                opArticol.getStocMathaus(params);


            }
        });
    }

    private void listStocMathaus(Object resultStoc) {

        ComandaMathaus comandaMathaus = opArticol.deserializeStocMathaus((String) resultStoc);
        DateArticolMathaus dateArticol = comandaMathaus.getDeliveryEntryDataList().get(0);
        String strStoc = dateArticol.getQuantity() + "#" + dateArticol.getUnit() + "#1#";

        if (articolModificat != null)
            articolMathaus = articolModificat.getArticolMathaus();

        if (dateArticol.getQuantity() > 0) {
            listArtStoc(strStoc);
            articolMathaus.setTip2("S");
        } else {
            globalCodDepartSelectetItem = articolMathaus.getDepart();
            articolMathaus.setTip2("");
            performListArtStoc();
        }

    }

    private void initSelectionDepartament() {

        selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

        if (isCV())
            selectedDepartamentAgent = "";

        if (isKA())
            selectedDepartamentAgent = "00";

        if (isWood())
            selectedDepartamentAgent = "12";

    }

    boolean isKA() {
        return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
    }

    boolean isCV() {
        return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")
                || UserInfo.getInstance().getTipUser().equals("CVR") || UserInfo.getInstance().getTipUser().equals("SMR")
                || UserInfo.getInstance().getTipUser().equals("WOOD") || UserInfo.getInstance().getTipUser().equals("SC");
    }

    boolean isWood() {
        return UserInfo.getInstance().getTipUser().equals("WOOD");
    }

    private boolean isUserCAG() {
        return UserInfo.getInstance().getTipUserSap().toUpperCase().startsWith("CAG");
    }

    private boolean isUserCVA() {
        return UserInfo.getInstance().getTipUserSap().toUpperCase().startsWith("CVA");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    // eveniment selectie unitate masura alternativa
    public class OnSelectUnitMas implements OnItemSelectedListener {
        @SuppressWarnings("unchecked")
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            if (codArticol.length() > 0) {

                artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                selectedUnitMas = artMap.get("rowText");

                getFactoriConversie();

            }

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void getFactoriConversie() {
        try {

            HashMap<String, String> params = new HashMap<String, String>();

            if (codArticol.length() == 8)
                codArticol = "0000000000" + codArticol;

            params.put("codArt", codArticol);
            params.put("unitMas", selectedUnitMas);

            opArticol.getFactorConversie(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public class OnSelectDepozit implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            if (codArticol.length() > 0) {
                String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

                if (tokenDep[0].trim().length() == 2)
                    globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
                else
                    globalDepozSel = tokenDep[0].trim();

                if (articolModificat == null)
                    performListArtStoc();
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                showFilialaDialogBox();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showFilialaDialogBox() {
        if (UtilsUser.isUserExceptieBV90Ged() || isWood() || UtilsUser.isUserIP() || isUserCAG() || isUserCVA()) {
            showFilialaDialogBV90();
        } else if (UtilsUser.isUserSite()) {
            showFilialaDialogUserSite();
        }
    }

    private void showFilialaDialogUserSite() {

        List<String> listDepozite = DepoziteUl.getInstance().getListDepozite();

        if (listDepozite == null)
            return;

        dialogSelFilArt = new Dialog(SelectArtCmdGed.this);
        dialogSelFilArt.setContentView(R.layout.select_fil_site);
        dialogSelFilArt.setTitle("Selectati filiala");
        dialogSelFilArt.setCancelable(false);
        dialogSelFilArt.show();

        final RadioButton radioFilAg = (RadioButton) dialogSelFilArt.findViewById(R.id.radio1);
        radioFilAg.setText(UserInfo.getInstance().getUnitLog());
        radioFilAg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteSite());
                    spinnerDepoz.setSelection(0);
                    isFilialaMavSite = false;
                }

            }
        });

        final RadioButton radioFilMav = (RadioButton) dialogSelFilArt.findViewById(R.id.radio2);

        if (HelperUserSite.hasDepozitMagazin(listDepozite)) {
            radioFilMav.setText("Magazin");
            radioFilMav.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        adapterSpinnerDepozite.clear();
                        adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteMav());
                        adapterSpinnerDepozite.notifyDataSetChanged();
                        spinnerDepoz.setSelection(0);
                        isFilialaMavSite = true;
                    }

                }
            });
        } else
            radioFilMav.setVisibility(View.GONE);

        final RadioButton radioFilBV90 = (RadioButton) dialogSelFilArt.findViewById(R.id.radio3);

        if (listDepozite.contains("BV90")) {
            radioFilBV90.setText("BV90");
            radioFilBV90.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        adapterSpinnerDepozite.clear();
                        adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteSite());
                        spinnerDepoz.setSelection(0);
                        isFilialaMavSite = false;
                    }
                }
            });
        } else {
            radioFilBV90.setVisibility(View.GONE);
        }

        if (CreareComandaGed.filialaAlternativa.equals(UserInfo.getInstance().getUnitLog()))
            radioFilAg.setChecked(true);
        else if (CreareComandaGed.filialaAlternativa.equals("BV90"))
            radioFilBV90.setChecked(true);
        else
            radioFilMav.setChecked(true);

        Button btnOkFilArt = (Button) dialogSelFilArt.findViewById(R.id.btnOkSelFilArt);
        btnOkFilArt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (radioFilAg.isChecked()) {
                    CreareComandaGed.filialaAlternativa = UserInfo.getInstance().getUnitLog();

                } else if (radioFilMav.isChecked()) {
                    CreareComandaGed.filialaAlternativa = HelperUserSite.getDepozitMavSite();
                } else {
                    CreareComandaGed.filialaAlternativa = "BV90";
                }

                if (!numeArticol.equals("")) {
                    performListArtStoc();
                }
                dialogSelFilArt.dismiss();

            }
        });

    }

    private void showFilialaDialogBV90() {
        dialogSelFilArt = new Dialog(SelectArtCmdGed.this);
        dialogSelFilArt.setContentView(R.layout.selectfilartdialogbox);
        dialogSelFilArt.setTitle("Selectati filiala");
        dialogSelFilArt.setCancelable(false);
        dialogSelFilArt.show();

        final RadioButton radioFilAg = (RadioButton) dialogSelFilArt.findViewById(R.id.radio1);
        radioFilAg.setText(UserInfo.getInstance().getUnitLog());
        radioFilAg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            }
        });

        final RadioButton radioFilBV90 = (RadioButton) dialogSelFilArt.findViewById(R.id.radio2);

        radioFilBV90.setText("BV90");
        radioFilBV90.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            }
        });

        if (CreareComandaGed.filialaAlternativa.equals(UserInfo.getInstance().getUnitLog()))
            radioFilAg.setChecked(true);
        else
            radioFilBV90.setChecked(true);

        Button btnOkFilArt = (Button) dialogSelFilArt.findViewById(R.id.btnOkSelFilArt);
        btnOkFilArt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                if (radioFilAg.isChecked()) {
                    CreareComandaGed.filialaAlternativa = UserInfo.getInstance().getUnitLog();

                } else {
                    CreareComandaGed.filialaAlternativa = "BV90";
                }

                if (!numeArticol.equals("")) {
                    performListArtStoc();
                }
                dialogSelFilArt.dismiss();

            }
        });

    }

    public void addListenerTextPromo() {
        textPromo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!artPromoText.equals(""))
                    showPromoWindow(artPromoText);

            }
        });
    }

    public void addListenerTglProc() {
        tglProc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));

                if (CreareComandaGed.tipClient.equals("IP")) {
                    nf2.setMinimumFractionDigits(2);
                    nf2.setMaximumFractionDigits(2);
                } else {
                    nf2.setMinimumFractionDigits(3);
                    nf2.setMaximumFractionDigits(3);
                }
                nf2.setGroupingUsed(false);

                NumberFormat nForm2 = NumberFormat.getInstance(new Locale("en", "US"));
                nForm2.setMinimumFractionDigits(2);
                nForm2.setMaximumFractionDigits(2);

                if (globalCantArt > 0) {

                    if (tglProc.isChecked()) {

                        varProc = -1;

                        textProcRed.setText(nf2.format(initPrice / globalCantArt * valMultiplu));

                        textProcRed.requestFocus();
                        textProcRed.setSelection(textProcRed.getText().length());

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

                        txtPretArt.setText("0");

                        pretMod = true;
                        finalPrice = initPrice;

                        textPretGED.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                        textPretGEDFTva.setText(nf2.format((initPrice / globalCantArt * valMultiplu) / procentTVA));
                        textTransport.setText(nForm2.format(initPrice * (procentTransport / 100) + valoareTransport));

                    } else {

                        varProc = 0;
                        textProcRed.setText("");

                        textProcRed.requestFocus();
                        textProcRed.setSelection(textProcRed.getText().length());

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

                        txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                        pretMod = false;
                        finalPrice = initPrice;

                        textPretGED.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                        textPretGEDFTva.setText(nf2.format((initPrice / globalCantArt * valMultiplu) / procentTVA));
                        textTransport.setText(nForm2.format(initPrice * (procentTransport / 100) + valoareTransport));

                    }
                }
            }
        });

    }

    public void addListenerProcArt() {

        final NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
        if (CreareComandaGed.tipClient.equals("IP")) {
            nf2.setMinimumFractionDigits(2);
            nf2.setMaximumFractionDigits(2);
        } else {
            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);
        }

        final NumberFormat nForm2 = NumberFormat.getInstance(new Locale("en", "US"));
        nForm2.setMinimumFractionDigits(2);
        nForm2.setMaximumFractionDigits(2);

        textProcRed.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (textProcRed.hasFocus()) {
                    InputMethodManager inputStatus = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputStatus.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }

            }
        });

        textProcRed.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                try {

                    // verif. cantitate

                    if (globalCantArt > 0) {

                        if (!pretMod) // modificare valoare
                        {
                            if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

                                if (varProc != -1) {
                                    varProc = Double.parseDouble(textProcRed.getText().toString());

                                    if (varProc > 0) {

                                        double newPr = (((initPrice / globalCantArt) * valMultiplu - (initPrice / globalCantArt) * valMultiplu
                                                * (varProc / 100)));

                                        txtPretArt.setText(nf2.format(newPr));
                                        finalPrice = newPr;
                                        textPretGEDFTva.setText(nf2.format(finalPrice / procentTVA));
                                        textPretGED.setText(txtPretArt.getText().toString());

                                        textTransport.setText(nForm2.format(((finalPrice / valMultiplu) * globalCantArt) * (procentTransport / 100)
                                                + valoareTransport));

                                    }
                                }

                            } else {
                                txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                                textPretGED.setText(nf2.format((initPrice / globalCantArt) * valMultiplu));
                                textPretGEDFTva.setText(nf2.format((initPrice / globalCantArt * valMultiplu) / procentTVA));
                                textTransport.setText(nForm2.format((initPrice) * (procentTransport / 100) + valoareTransport));

                            }

                        }// modificare procent
                        else {

                            if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

                                double val1 = Double.parseDouble(textProcRed.getText().toString());

                                procR = (((initPrice / globalCantArt * valMultiplu) - val1) / ((initPrice / globalCantArt * valMultiplu))) * 100;

                                txtPretArt.setText(nf2.format(procR));
                                finalPrice = Double.parseDouble(textProcRed.getText().toString());
                                textPretGEDFTva.setText(nf2.format(finalPrice / procentTVA));
                                textTransport.setText(nForm2.format(((finalPrice / valMultiplu) * globalCantArt) * (procentTransport / 100) + valoareTransport));
                                textPretGED.setText(textProcRed.getText().toString());

                            } else {
                                txtPretArt.setText("0");
                                finalPrice = 0;
                                textPretGEDFTva.setText("0");
                                textTransport.setText("0");
                                textPretGED.setText("0");
                                textProcRed.setFocusableInTouchMode(true);
                            }

                        }

                        afisPretUmAlternativa();

                    }// sf. verif. cantitate

                } catch (Exception ex) {
                    finalPrice = 0;
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    public boolean isNumeric(String input) {
        try {
            Double.parseDouble(input.replace(",", ""));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void addListenerPretBtn() {
        pretBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                InputMethodManager mgr;

                try {

                    if (textCant.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Cantitate invalida!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

                    performGetPret();

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressWarnings("unchecked")
    protected void performGetPret() {

        try {

            selectedCant = Double.parseDouble(textCant.getText().toString().trim());

            selectedUnitMas = "";
            selectedUnitMasPret = "";
            listCabluri = null;

            if (listUmVanz.size() > 1) {
                artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                selectedUnitMas = artMap.get("rowText");
                selectedUnitMasPret = artMap.get("rowText");
            }

            if (selectedCant > 0) {
                callGetPret();

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("unchecked")
    private void callGetPret() {
        HashMap<String, String> params = new HashMap<String, String>();

        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
            globalDepozSel = articolModificat.getDepozit();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        String tipUser = UserInfo.getInstance().getTipUser();

        if (isWood()) {
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "4" + UserInfo.getInstance().getUnitLog().substring(3, 4);
            tipUser = "CV";
        }

        String paramUnitMas = textUM.getText().toString();

        if (listUmVanz.size() > 1) {
            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
            paramUnitMas = artMap.get("rowText");

        }

        String paramDepozit = " ";
        if (UtilsComenzi.isDespozitDeteriorate(globalDepozSel))
            paramDepozit = globalDepozSel;


        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(codClientVar);
        paramPret.setArticol(codArticol);

        paramPret.setCantitate(textCant.getText().toString().trim());
        paramPret.setDepart(globalCodDepartSelectetItem);
        paramPret.setUm(paramUnitMas);
        paramPret.setUl(uLog);
        paramPret.setDepoz(paramDepozit);
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib("20");
        paramPret.setTipUser(tipUser);
        paramPret.setMetodaPlata(DateLivrare.getInstance().getTipPlata());
        paramPret.setTermenPlata(DateLivrare.getInstance().getTermenPlata());
        paramPret.setCodJudet(getCodJudetPret());
        paramPret.setLocalitate(getLocalitatePret());
        paramPret.setFilialaAlternativa(CreareComandaGed.filialaAlternativa);
        paramPret.setCodClientParavan(CreareComandaGed.codClientParavan);
        paramPret.setFilialaClp(DateLivrare.getInstance().getCodFilialaCLP());
        paramPret.setTipTransport(DateLivrare.getInstance().getTransport());

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));

        opArticol.getPretGedJson(params);


    }

    private String getCodJudetPret() {

        return DateLivrare.getInstance().getCodJudet();

    }

    private String getLocalitatePret() {

        return DateLivrare.getInstance().getOras();


    }

    public void addListenerToggle() {
        tglButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tglButton.isChecked()) {
                    if (tglTipArtBtn.isChecked())
                        txtNumeArticol.setHint("Introduceti cod sintetic");
                    else
                        txtNumeArticol.setHint("Introduceti cod articol");
                } else {
                    if (tglTipArtBtn.isChecked())
                        txtNumeArticol.setHint("Introduceti nume sintetic");
                    else
                        txtNumeArticol.setHint("Introduceti nume articol");
                }
            }
        });

    }

    public void addListenerTglTipArtBtn() {
        tglTipArtBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tglTipArtBtn.isChecked()) {
                    if (!tglButton.isChecked())
                        txtNumeArticol.setHint("Introduceti nume sintetic");
                    else
                        txtNumeArticol.setHint("Introduceti cod sintetic");
                } else {
                    if (!tglButton.isChecked())
                        txtNumeArticol.setHint("Introduceti nume articol");
                    else
                        txtNumeArticol.setHint("Introduceti cod articol");

                }
            }
        });

    }

    public void populateListViewArticol(List<ArticolDB> resultsList) {

        clearTextField(txtNumeArticol);
        resultLayout.setVisibility(View.INVISIBLE);

        if (UtilsUser.isUserSite() && isFilialaMavSite)
            UtilsArticole.getArt111Only(resultsList);

        CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
        setListAdapter(adapterArticole);

    }

    private void clearTextField(EditText textField) {
        textField.setText("");
    }

    public void addListenerBtnArticole() {
        articoleBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    if (txtNumeArticol.getText().toString().length() > 0) {

                        isArticolModificatCantPret = false;
                        articolModificat = null;

                        textNumeArticol.setVisibility(View.GONE);
                        textCodArticol.setVisibility(View.GONE);
                        textUM.setVisibility(View.GONE);
                        textStoc.setVisibility(View.GONE);
                        textCant.setVisibility(View.GONE);
                        labelCant.setVisibility(View.GONE);
                        labelStoc.setVisibility(View.GONE);
                        saveArtBtn.setVisibility(View.GONE);

                        pretBtn.setVisibility(View.GONE);

                        redBtnTable.setVisibility(View.GONE);
                        spinnerUnitMas.setVisibility(View.GONE);
                        layoutStocKA.setVisibility(View.GONE);

                        performGetArticole();

                    } else {
                        Toast.makeText(getApplicationContext(), "Introduceti nume articol!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    protected void performGetArticole() {
        if (isComandaDL() && DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE) {
            performGetArticoleFurnizor();
        } else if (isComandaDL() && DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA)
            getArticoleACZC();
        else {
            performGetArticoleDistributie();
        }

    }

    private void getArticoleACZC() {
        performGetArticoleFurnizor();
    }

    protected void performGetArticoleDistributie() {

        String numeArticol = txtNumeArticol.getText().toString().trim();
        String tipCautare = "", tipArticol = "", tipComanda = "";

        if (tglButton.isChecked())
            tipCautare = "C";
        else
            tipCautare = "N";

        if (tglTipArtBtn.isChecked())
            tipArticol = "S";
        else
            tipArticol = "A";

        String departCautare = selectedDepartamentAgent;

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_LIVRARE && !CreareComandaGed.selectedDepartCod.equals("-1"))
            departCautare = CreareComandaGed.selectedDepartCod;

        if (isCV())
            departCautare = "";

        if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            tipComanda = "fasonate";

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("searchString", numeArticol);
        params.put("tipArticol", tipArticol);
        params.put("tipCautare", tipCautare);
        params.put("filiala", UserInfo.getInstance().getUnitLog());
        params.put("departament", departCautare);
        params.put("codUser", UserInfo.getInstance().getCod());
        params.put("tipComanda", tipComanda);

        opArticol.getArticoleDistributie(params);

    }

    private void performGetArticoleFurnizor() {
        HashMap<String, String> params = new HashMap<String, String>();

        String tipArticol1 = "", tipArticol2 = "";

        if (tglButton.isChecked()) {
            tipArticol1 = "1";
            if (tglTipArtBtn.isChecked()) {
                tipArticol2 = "2";
            } else {
                tipArticol2 = "1";
            }

        } else {
            tipArticol1 = "2";
            if (tglTipArtBtn.isChecked()) {
                tipArticol2 = "2";
            } else {
                tipArticol2 = "1";
            }

        }

        String numeArticol = txtNumeArticol.getText().toString().trim();

        params.put("codArticol", numeArticol);
        params.put("tip1", tipArticol1);
        params.put("tip2", tipArticol2);
        params.put("furnizor", DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa());
        params.put("codDepart", selectedDepartamentAgent);
        params.put("codUser", UserInfo.getInstance().getCod());
        params.put("filiala", UserInfo.getInstance().getUnitLog());

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA)
            params.put("aczc", "true");
        else
            params.put("aczc", "false");


        opArticol.getArticoleFurnizor(params);
    }

    boolean isAgentorSD() {
        return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD");
    }

    @SuppressWarnings("unchecked")
    public void addListenerBtnSaveArt() {
        saveArtBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                try {

                    String localUnitMas = "";
                    String alteValori = "", subCmp = "0";

                    NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                    nf.setMinimumFractionDigits(2);
                    nf.setMaximumFractionDigits(2);

                    NumberFormat nfPret = NumberFormat.getInstance(new Locale("en", "US"));
                    nfPret.setMinimumFractionDigits(3);
                    nfPret.setMaximumFractionDigits(3);

                    if (textCant.getVisibility() != View.VISIBLE) {
                        return;
                    }

                    if (isConditieCabluri05BV90() && listCabluri == null) {
                        getCabluri05(codArticol);
                        return;
                    }

                    if (ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() == 0) {
                        unitLogUnic = CreareComandaGed.filialaAlternativa;
                    } else if (ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0) {
                        unitLogUnic = ListaArticoleComandaGed.getInstance().getListArticoleComanda().get(0).getFilialaSite();
                    }

                    if (totalNegociat && UtilsUser.isUserGed() && !globalCodDepartSelectetItem.substring(0, 2).equals("09")) {
                        Toast.makeText(getApplicationContext(), "Pentru comenzile cu valoare negociata adaugati doar articole din divizia 09.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (UtilsUser.isUserIP() && !conditiiCmdIP()) {
                        return;
                    }

                    String cantArticol = textCant.getText().toString().trim();

                    if (listUmVanz.size() > 1) {

                        artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                        String currentUnitMas = artMap.get("rowText");

                        if (!currentUnitMas.equals(selectedUnitMasPret)) {
                            Toast.makeText(getApplicationContext(), "Unitatea de masura nu corespunde celei pentru care a fost solicitat pretul.",
                                    Toast.LENGTH_LONG).show();
                            return;

                        }

                    }

                    if (selectedCant != Double.parseDouble(cantArticol)) {

                        // pentru comenzile simulate fara rezervare de
                        // stoc se poate afisa pretul
                        if (tipComanda.equalsIgnoreCase("S") && !rezervStoc) {

                        } else {
                            Toast.makeText(getApplicationContext(), "Pretul nu corespunde cantitatii solicitate!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    if (!isComandaDL()
                            && Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) > Double.parseDouble(textStoc.getText()
                            .toString().replaceAll(",", ""))) {
                        if (tipComanda.equalsIgnoreCase("S") && !rezervStoc) {

                        } else {

                            Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();

                            if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB && importAllAmob) {
                                setProcesatArticolAmob(codArticol, globalDepozSel);
                                proceseazaArticoleAmob();
                            }

                            return;
                        }
                    }

                    // verificare umvanz.

                    localUnitMas = (textUM.getText().toString().trim().length() > 0) ? textUM.getText().toString().trim() : " ";

                    if (listUmVanz.size() > 1) {

                        artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                        localUnitMas = artMap.get("rowText");

                        if (!selectedUnitMas.equals(localUnitMas)) {
                            Toast.makeText(getApplicationContext(), "U.m. vanzare eronata!", Toast.LENGTH_LONG).show();

                            return;
                        }

                    }

                    // verificare procent discount
                    double procRedFin = 0, valArticol = 0;
                    procentAprob = 0;

                    if (finalPrice == initPrice) // pretul din sap e pe
                        // cantitate, daca se
                        // modifica devine pe
                        // unitate
                        finalPrice = (finalPrice / globalCantArt) * valMultiplu;

                    valArticol = ((finalPrice / valMultiplu) * globalCantArt);

                    if (initPrice != 0) {
                        if (!tglProc.isChecked()) {
                            if (textProcRed.getText().length() > 0)
                                procRedFin = Double.parseDouble(textProcRed.getText().toString());
                            else
                                procRedFin = 0;
                        } else
                            procRedFin = (1 - finalPrice / (initPrice / globalCantArt * valMultiplu)) * 100;
                    }




                    // pt. unele articole care nu au pret (servicii)
                    if (pretVanzare == 0)
                        pretVanzare = finalPrice;

                    procentAprob = (1 - finalPrice / (pretVanzare / globalCantArt * valMultiplu)) * 100;

                    if (finalPrice != 0) {

                        tipAlert = " ";

                        if (UtilsUser.isAgentOrSD() || UtilsUser.isConsWood()) {
                            if (procentAprob > discMaxAV) {
                                tipAlert = "SD";
                            }
                            if (procentAprob > discMaxSD) {
                                tipAlert += ";DV";
                            }
                        }

                        if (finalPrice < cmpArt) {
                            subCmp = "1";
                        }

                        double factorConvUM = 1;
                        if (!textUM.getText().toString().trim().equals(localUnitMas)) {
                            factorConvUM = (Double.parseDouble(cantArticol) / valMultiplu) / globalCantArt;
                        }

                        alteValori = String.valueOf(valArticol) + "!" + String.valueOf(listPrice) + "!" + String.valueOf(finalPrice) + "!"
                                + String.valueOf(pretMediuDistrib * (valoareUmrez / valoareUmren)) + "!"
                                + String.valueOf(adaosMediuDistrib * (valoareUmrez / valoareUmren)) + "!" + codPromo + "!" + subCmp + "!"
                                + String.valueOf(factorConvUM) + "!";

                        if (articolModificat != null)
                            codArticol = articolModificat.getCodArticol();

                        if (codArticol.length() == 18)
                            codArticol = codArticol.substring(10, 18);

                        double pretUnitClient = valArticol / Double.parseDouble(cantUmb);
                        double pretUnitGed = initPrice / Double.parseDouble(cantUmb);

                        ArticolComandaGed articol = new ArticolComandaGed();

                        articol.setNumeArticol(numeArticol);
                        articol.setCodArticol(codArticol);
                        articol.setCantitate(Double.valueOf(cantArticol));
                        articol.setPretUnitGed(pretUnitGed);
                        articol.setUm(localUnitMas);
                        articol.setDepozit(globalDepozSel);
                        articol.setPretUnitarClient(pretUnitClient);
                        articol.setPretUnit(pretUnitClient);
                        articol.setTipAlert(tipAlert);
                        articol.setPromotie(Integer.valueOf(codPromo));

                        if (UtilsUser.isAgentOrSD() || UtilsUser.isConsWood()) {
                            if (articol.getPromotie() >= 1)
                                articol.setPonderare(0);
                            else
                                articol.setPonderare(1);
                        } else {
                            if (articol.getPromotie() >= 1)
                                articol.setPonderare(0);
                            else
                                articol.setPonderare(2);
                        }

                        articol.setProcent(procRedFin);
                        articol.setDiscClient(procDiscClient);
                        articol.setProcAprob(procentAprob);
                        articol.setMultiplu(valMultiplu);
                        articol.setPret(articol.getPretUnitarClient() * articol.getCantUmb());
                        articol.setInfoArticol(infoArticol);
                        articol.setUmb(Umb);
                        articol.setCantUmb(Double.valueOf(cantUmb));
                        articol.setAlteValori(alteValori);
                        articol.setDepart(globalCodDepartSelectetItem);
                        articol.setDepartSintetic(articol.getDepart());
                        articol.setCmp(cmpArt);
                        articol.setCoefCorectie(coefCorectie);
                        articol.setPretMediu(pretMediuDistrib);
                        articol.setAdaosMediu(adaosMediuDistrib);
                        articol.setTipArt(tipArticol);
                        articol.setValTransport((articol.getPretUnitarClient() * articol.getCantUmb()) * (selectedArticol.getProcTransport() / 100)
                                + selectedArticol.getValTrap());
                        articol.setProcTransport(selectedArticol.getProcTransport());
                        articol.setDiscountAg(discMaxAV);
                        articol.setDiscountSd(discMaxSD);
                        articol.setUmPalet(articolDBSelected.isUmPalet());


                        if (!filialaStocBV90.isEmpty())
                            articol.setFilialaSite(filialaStocBV90);
                        else if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
                            articol.setFilialaSite(DateLivrare.getInstance().getCodFilialaFasonate());
                        else if (HelperMathaus.isComandaVanzareTCLI())
                            articol.setFilialaSite(DateLivrare.getInstance().getFilialaLivrareTCLI());
                        else
                            articol.setFilialaSite(CreareComandaGed.filialaAlternativa);


                        articol.setArticolMathaus(articolMathaus);

                        articol.setLungime(articolDBSelected.getLungime());
                        articol.setDataExpPret(selectedArticol.getDataExp());
                        articol.setListCabluri(listCabluri);
                        articol.setGreutate(selectedArticol.getGreutate());
                        articol.setPretFaraTva(selectedArticol.getPretFaraTva());

                        articol.setTipMarfa(selectedArticol.getTipMarfa());
                        articol.setGreutateBruta(selectedArticol.getGreutateBruta());
                        articol.setLungimeArt(selectedArticol.getLungimeArt());
                        articol.setCantitateInit(Double.valueOf(cantArticol));

                        articol.setCantitate50(Double.valueOf(selectedArticol.getCantitate50()));
                        articol.setUm50(selectedArticol.getUm50());

                        if (DateLivrare.getInstance().getTransport().equals("TCLI"))
                            listStocTCLI = HelperMathaus.getStocTCLIDepozit(textStoc.getText().toString(), articol.getDepozit(), articol.getUm());

                        articol.setListStocTCLI(listStocTCLI);

                        if (procRedFin > 0)
                            articol.setIstoricPret(selectedArticol.getIstoricPret());

                        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
                            articol.setFilialaSite(articolAmob.getDepozit());
                            eliminaArticolAmobSelectat();

                            ((TextView) findViewById(R.id.articolAmobDetalii)).setText("");

                        }

                        ListaArticoleComandaGed listaArticole = ListaArticoleComandaGed.getInstance();
                        listaArticole.addArticolComanda(articol);


                        textNumeArticol.setText("");
                        textCodArticol.setText("");
                        textUM.setText("");
                        textStoc.setText("");
                        textCant.setText("");
                        textPromo.setText("");

                        numeArticol = "";
                        codArticol = "";
                        tipArticol = "";
                        umStoc = "";
                        globalCodDepartSelectetItem = "";

                        localUnitMas = "";
                        procDiscClient = 0;
                        initPrice = 0;
                        finalPrice = 0;
                        valArticol = 0;
                        globalCantArt = 0;

                        cmpArt = 0;
                        subCmp = "0";

                        valoareUmrez = 1;
                        valoareUmren = 1;

                        listCabluri = null;

                        cautaStocBV90 = false;
                        verificatStocBV90 = false;
                        filialaStocBV90 = "";

                        listStocTCLI = null;

                        redBtnTable.setVisibility(View.GONE);
                        labelStoc.setVisibility(View.GONE);
                        labelCant.setVisibility(View.GONE);
                        textCant.setVisibility(View.GONE);
                        pretBtn.setVisibility(View.GONE);
                        spinnerUnitMas.setVisibility(View.GONE);
                        layoutStocKA.setVisibility(View.GONE);

                        resultLayout.setVisibility(View.INVISIBLE);

                        if (!tglProc.isChecked())
                            tglProc.performClick();

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

                        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {

                            if (importAllAmob)
                                proceseazaArticoleAmob();

                        }

                    } else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Articolul nu are pret definit!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    textProcRed.setText("");

                } catch (Exception e) {

                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (ScreenUtils.isIntentArticolModificat(intent))
                    finish();

            }
        });

    }

    private boolean isConditieCabluri05BV90() {
        return articolDBSelected.getDepart().equals("05") && CreareComandaGed.filialaAlternativa.equals("BV90");
    }

    private void getCabluri05(String codArticol) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArticol", codArticol);
        params.put("sinteticArticol", articolDBSelected.getSintetic());
        opArticol.getCabluri05(params);

    }

    private void afisCabluri05(List<BeanCablu05> listCabluri) {

        if (listCabluri.isEmpty()) {
            this.listCabluri = listCabluri;
            saveArtBtn.performClick();
            return;
        }

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.5);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);

        Cabluri05Dialog cabluriDialog = new Cabluri05Dialog(SelectArtCmdGed.this, listCabluri, textCant.getText().toString().trim());
        cabluriDialog.getWindow().setLayout(width, height);
        cabluriDialog.setCabluSelectedListener(this);
        cabluriDialog.show();

    }

    private boolean conditiiCmdIP() {

        if (CreareComandaGed.filialaAlternativa.equals("BV90")) {

            if (!globalCodDepartSelectetItem.equals("01") && !globalCodDepartSelectetItem.equals("02") && !globalCodDepartSelectetItem.equals("05")) {
                Toast.makeText(getApplicationContext(), "Din BV90 adaugati doar articole din departamentele 01, 02 sau 05.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (!DateLivrare.getInstance().getTransport().equals("TCLI") && !DateLivrare.getInstance().getTransport().equals("TERT")) {
                Toast.makeText(getApplicationContext(), "Din BV90 selectati doar transport TCLI sau TERT.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (!globalDepozSel.equals("02V1") && !globalDepozSel.equals("05V1") && !globalDepozSel.equals("92V1") && !globalDepozSel.equals("95V1")) {
                Toast.makeText(getApplicationContext(), "Din BV90 adaugati doar articole din depozitele  02V1, 05V1, 92V1, 95V1.", Toast.LENGTH_LONG).show();
                return false;
            }

        }

        return true;

    }

    private boolean isComandaDL() {
        return DateLivrare.getInstance().getFurnizorComanda() != null && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa() != null;
    }

    private void listStocDisponibilTCLI(String stocResponse) {
        resultLayout.setVisibility(View.VISIBLE);

        listStocTCLI = new ArrayList<>();

        if (!stocResponse.equals("-1")) {

            String[] tokStocArt = stocResponse.split("!");
            String[] tokenMain = tokStocArt[0].split("@@");
            double stocTotalTCLI = 0;
            String umStocTCLI = "BUC";

            for (int i = 0; i < tokenMain.length; i++) {
                String[] articol = tokenMain[i].toString().split("#");

                if (Double.valueOf(articol[0]) > 0) {

                    if (UtilsComenzi.isDepozitTCLI(articol[2], articolDBSelected.getDepart())) {
                        stocTotalTCLI += Double.parseDouble(articol[0]);
                        globalDepozSel = articol[2];
                        umStocTCLI = articol[1];

                        BeanStocTCLI beanStocTCLI = new BeanStocTCLI();
                        beanStocTCLI.setCantitate(Double.parseDouble(articol[0]));
                        beanStocTCLI.setUm(umStocTCLI);
                        beanStocTCLI.setDepozit(articol[2]);
                        listStocTCLI.add(beanStocTCLI);

                    }
                }
            }

            listArtStoc(stocTotalTCLI + "#" + umStocTCLI + "#1");

        } else {
            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();
            textUM.setText("");
            textStoc.setText("");
        }

    }

    private void listStocDeteriorat(String stocResponse) {
        resultLayout.setVisibility(View.VISIBLE);

        if (!stocResponse.equals("-1")) {

            String[] tokStocArt = stocResponse.split("!");
            String[] tokenMain = tokStocArt[0].split("@@");
            boolean stocDeteriorat = false;

            for (int i = 0; i < tokenMain.length; i++) {
                String[] articol = tokenMain[i].toString().split("#");

                if (Double.valueOf(articol[0]) > 0) {

                    if (UtilsComenzi.isDespozitDeteriorate(articol[2])) {
                        globalDepozSel = articol[2];
                        listArtStoc(articol[0] + "#" + articol[1] + "#1");
                        stocDeteriorat = true;
                        break;
                    }
                }
            }

            if (!stocDeteriorat)
                listArtStoc("0#BUC#1");

        } else {
            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();
            textUM.setText("");
            textStoc.setText("");
        }

    }

    @SuppressWarnings("unchecked")
    private void listArtStoc(String stocResponse) {

        listStocTCLI = new ArrayList<>();

        if (!stocResponse.equals("-1")) {

            NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);

            resultLayout.setVisibility(View.VISIBLE);

            String[] tokenPret = stocResponse.split("#");

            textNumeArticol.setVisibility(View.VISIBLE);
            textCodArticol.setVisibility(View.VISIBLE);
            textUM.setVisibility(View.VISIBLE);
            textStoc.setVisibility(View.VISIBLE);
            textCant.setVisibility(View.VISIBLE);
            labelCant.setVisibility(View.VISIBLE);
            labelStoc.setVisibility(View.VISIBLE);
            pretBtn.setVisibility(View.VISIBLE);

            textUM.setText(tokenPret[1]);
            textStoc.setText(nf2.format(Double.valueOf(tokenPret[0])));

            if (articolModificat != null && articolModificat.getArticolMathaus() != null)
                articolMathaus = articolModificat.getArticolMathaus();

            if (isDepartMathaus) {
                textNumeArticol.setText(articolMathaus.getNume());
                textCodArticol.setText(articolMathaus.getCod());
            }

            umStoc = tokenPret[1];

            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();

            if (artMap != null) {
                String stocUM = artMap.get("rowText");

                if (!stocUM.equals(tokenPret[1]) && !tokenPret[1].trim().equals("")) // um
                // vanz
                // si
                // um
                // vanz
                // difera
                {
                    HashMap<String, String> tempUmVanz;
                    tempUmVanz = new HashMap<String, String>();
                    tempUmVanz.put("rowText", tokenPret[1]);

                    listUmVanz.add(0, tempUmVanz);

                    spinnerUnitMas.setAdapter(adapterUmVanz);
                    spinnerUnitMas.setVisibility(View.VISIBLE);
                }
            }


            if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
                textCant.setText(String.valueOf(articolAmob.getCantitate()));
                textCant.setFocusable(false);
                performGetPret();
            }

            if (isCondArtStocBV90() && tokenPret[0].equals("0") && !verificatStocBV90) {
                cautaStocBV90 = true;
                verificatStocBV90 = true;
                performListArtStoc();
            }else if (isArticolModificatCantPret) {
                textCant.setText(String.valueOf(articolModificat.getCantitate()));
                performGetPret();
            }

            cautaStocBV90 = false;

        } else {

            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

            textUM.setText("");
            textStoc.setText("");
        }

    }

    private boolean isConditieExtraMathaus() {
        return articolMathaus != null
                && Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) > Double.parseDouble(textStoc.getText().toString()
                .replaceAll(",", ""))
                && Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) <= Double.parseDouble(textStoc.getText().toString()
                .replaceAll(",", ""))
                + stocExtraMathaus;
    }

    private boolean isConditieMathaus() {
        return articolMathaus != null
                && Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) <= Double.parseDouble(textStoc.getText().toString()
                .replaceAll(",", ""));

    }

    private ComandaExtraMathaus getArticoleExtraMathaus() {

        ComandaExtraMathaus comanda = new ComandaExtraMathaus();
        String filialaGed = filialaAlternativa.substring(0, 2) + "2" + filialaAlternativa.substring(3, 4);

        List<StocMathaus> artExtraMathaus = new ArrayList<StocMathaus>();
        double stocFilialaAg = Double.parseDouble(textStoc.getText().toString().replaceAll(",", ""));
        double cantArticol = Double.parseDouble(textCant.getText().toString().trim());
        int nrMaxFiliale = Constants.NR_FILIALE_MATHAUS;

        double restCant = cantArticol;

        if (stocFilialaAg > 0) {
            StocMathaus stoc = new StocMathaus();
            stoc.setCantitate(stocFilialaAg);
            stoc.setUl(filialaGed);
            artExtraMathaus.add(stoc);

            restCant = cantArticol - stocFilialaAg;
            nrMaxFiliale--;

            addFilialaMathaus(filialaGed, getBaseContext());

        }

        int ii = 0;

        for (StocMathaus artStoc : artStocMathaus) {

            if (artStoc.getCantitate() > 0 && restCant > 0 && ii < nrMaxFiliale) {
                StocMathaus stoc = new StocMathaus();

                if (artStoc.getCantitate() >= restCant) {
                    stoc.setCantitate(restCant);
                    restCant = 0;
                } else {
                    stoc.setCantitate(artStoc.getCantitate());
                    restCant = restCant - artStoc.getCantitate();
                }

                stoc.setUl(artStoc.getUl());
                artExtraMathaus.add(stoc);

                addFilialaMathaus(artStoc.getUl(), getBaseContext());

                ii++;
            }

        }

        comanda.setListArticole(artExtraMathaus);
        comanda.setSucces(Double.compare(restCant, 0) == 0 ? true : false);

        return comanda;
    }

    public void addFilialaMathaus(String filiala, Context context) {

        filiala = UtilsComenzi.getFilialaGed(filiala);

        if (!DateLivrare.getInstance().getFilialeExtraMathaus().contains(filiala)) {

            if (DateLivrare.getInstance().getFilialeExtraMathaus().isEmpty())
                DateLivrare.getInstance().setFilialeExtraMathaus(filiala);
            else
                DateLivrare.getInstance().setFilialeExtraMathaus(DateLivrare.getInstance().getFilialeExtraMathaus() + "," + filiala);
        }

    }

    private void listArtStocMathaus(String stocResponse) {

        stocExtraMathaus = 0;
        artStocMathaus = new ArrayList<StocMathaus>();
        int nrFilMathaus = Constants.NR_FILIALE_MATHAUS;

        String strFilStocM = "";

        String filialaGed = filialaAlternativa.substring(0, 2) + "2" + filialaAlternativa.substring(3, 4);

        if (!articolMathaus.getCatMathaus().equals("S"))
            listArtStoc(stocResponse);
        else {

            ((LinearLayout) findViewById(R.id.layoutStocMathaus)).setVisibility(View.VISIBLE);

            String[] stocuri = stocResponse.split("@");

            String umStocMathaus = "";

            for (String stcM : stocuri) {

                if (!stcM.split("#")[3].equals(filialaGed)) {

                    StocMathaus stocMathaus = new StocMathaus();
                    stocMathaus.setCantitate(Double.parseDouble(stcM.split("#")[0]));
                    stocMathaus.setUl(stcM.split("#")[3]);
                    artStocMathaus.add(stocMathaus);
                    umStocMathaus = stcM.split("#")[1];

                } else {

                    listArtStoc(stcM);
                    if (Double.parseDouble(stcM.split("#")[0]) > 0)
                        nrFilMathaus--;

                }

            }

            Collections.sort(artStocMathaus, new StocMathausComparator());

            for (StocMathaus math : artStocMathaus) {
                if (nrFilMathaus > 0) {
                    stocExtraMathaus += math.getCantitate();
                    nrFilMathaus--;
                    strFilStocM += ", " + math.getUl();
                }

            }

            ((TextView) findViewById(R.id.textStocMathaus)).setText(Double.toString(stocExtraMathaus));
            ((TextView) findViewById(R.id.textUmMathaus)).setText(umStocMathaus);

        }

    }

    private void eliminaArticolAmobSelectat() {

        Iterator<ArticolAmob> iterator = CreareComandaGed.listArticoleAMOB.iterator();

        while (iterator.hasNext()) {
            ArticolAmob articol = iterator.next();

            if (articol.getCodArticol().equals(articolAmob.getCodArticol()) && articol.getDepozit().equals(articolAmob.getDepozit())) {
                iterator.remove();
                break;
            }

        }

        afiseazaArticoleAmob();

    }

    public void showModifCantInfo(PretArticolGed pretArticol) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        StringBuilder infoText = new StringBuilder();

        infoText.append("\nAcest produs se vinde doar in ");
        infoText.append(pretArticol.getUm50());
        infoText.append(" si cantitatea a fost ajustata la ");
        infoText.append(pretArticol.getCantitate() + " ");
        infoText.append(pretArticol.getUm());
        infoText.append(" pentru a corespunde la ");
        infoText.append(pretArticol.getCantitate50() + " " + pretArticol.getUm50() + ".\n");

        alertDialogBuilder.setTitle("Info");
        alertDialogBuilder.setMessage(infoText.toString()).setCancelable(false)
                .setNegativeButton("Inchide", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isConditiiModifCant50(PretArticolGed pretArticol) {
        return !textUM.getText().toString().trim().equals(selectedArticol.getUm50()) && Double.parseDouble(selectedArticol.getCantitate50()) > 0
                && selectedCant != Double.parseDouble(pretArticol.getCantitate());
    }

    private void listPretArticol(PretArticolGed pretArticol) {

        selectedArticol = pretArticol;

        if (isConditiiModifCant50(selectedArticol)) {
            textCant.setText(selectedArticol.getCantitate());
            showModifCantInfo(pretArticol);
            selectedCant = Double.parseDouble(textCant.getText().toString().trim());
        }

        if (Double.parseDouble(selectedArticol.getCantitate50()) == 0)
            selectedArticol.setCantitate50(selectedArticol.getCantitate());


        NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));

        if (CreareComandaGed.tipClient.equals("IP")) {
            nf2.setMinimumFractionDigits(2);
            nf2.setMaximumFractionDigits(2);
        } else {
            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);
        }
        nf2.setGroupingUsed(false);

        codPromo = "-1";
        procDiscClient = 0;
        saveArtBtn.setVisibility(View.VISIBLE);
        textPromo.setText("");

        valMultiplu = Double.valueOf(pretArticol.getMultiplu());
        coefCorectie = pretArticol.getCoefCorectie();

        textMultipluArt.setText("Unit.pret: " + pretArticol.getMultiplu() + " " + umStoc);

        globalCantArt = Double.valueOf(pretArticol.getCantitateUmBaza());
        cantUmb = pretArticol.getCantitateUmBaza();
        Umb = pretArticol.getUmBaza();
        cmpArt = Double.valueOf(pretArticol.getCmp());
        pretMediuDistrib = Double.valueOf(pretArticol.getPretMediu());
        adaosMediuDistrib = Double.valueOf(pretArticol.getAdaosMediu());

        initPrice = Double.valueOf(pretArticol.getPret());
        listPrice = Double.valueOf(pretArticol.getPretLista());

        afisIstoricPret(pretArticol.getIstoricPret());

        listArtRecom = pretArticol.getArticoleRecomandate();
        ((LinearLayout) findViewById(R.id.layoutRecommend)).setVisibility(View.GONE);
        if (!listArtRecom.isEmpty()) {
            ((LinearLayout) findViewById(R.id.layoutRecommend)).setVisibility(View.VISIBLE);
        }

        selectedArticol.setIstoricPret(UtilsFormatting.getIstoricPret(pretArticol.getIstoricPret(), EnumTipComanda.GED));

        txtImpachetare.setText(pretArticol.getImpachetare());

        if (!pretArticol.getErrMsg().isEmpty()) {
            Toast.makeText(getApplicationContext(), pretArticol.getErrMsg(), Toast.LENGTH_LONG).show();
            return;
        }

        if (globalDepozSel.substring(2, 3).equals("V")) {

            if (initPrice / globalCantArt * valMultiplu < cmpArt && !UtilsArticole.isArticolPermitSubCmp(codArticol)) {
                Toast.makeText(getApplicationContext(), "Pret sub cmp!", Toast.LENGTH_LONG).show();

                if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB && importAllAmob) {
                    setProcesatArticolAmob(codArticol, globalDepozSel);
                    proceseazaArticoleAmob();
                }

                return;
            }

        }

        String[] condPret = pretArticol.getConditiiPret().split(";");

        int ii = 0;
        String[] tokPret;
        String stringCondPret = "";
        Double valCondPret = 0.0;

        // ZSTA taxa verde
        for (ii = 0; ii < condPret.length; ii++) {
            tokPret = condPret[ii].split(":");
            valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
            if (valCondPret != 0 && tokPret[0].equalsIgnoreCase("ZSTA")) {
                stringCondPret += "Taxa verde" + addSpace(15 - "Taxa verde".length()) + ":" + addSpace(10 - String.valueOf(nf2.format(valCondPret)).length())
                        + nf2.format(valCondPret) + System.getProperty("line.separator");

            }

        }

        textCondPret.setVisibility(View.VISIBLE);
        textCondPret.setText(stringCondPret);

        finalPrice = initPrice;
        textProcRed.setText("");

        pretVanzare = initPrice;

        redBtnTable.setVisibility(View.VISIBLE);
        textProcRed.setVisibility(View.VISIBLE);

        textPretGED.setVisibility(View.VISIBLE);
        textMultipluArt.setVisibility(View.VISIBLE);

        labelFactConv.setVisibility(View.INVISIBLE);

        txtPretArt.setText(nf2.format((initPrice / globalCantArt) * valMultiplu));
        txtPretArt.setHint(nf2.format((initPrice / globalCantArt) * valMultiplu));

        if (CreareComandaGed.tipClient.equals("IP"))
            ((TextView) findViewById(R.id.labelPretGED)).setText("Pret GED fara tva: ");

        textPretGED.setText(nf2.format((initPrice / globalCantArt) * valMultiplu));
        infoArticol = pretArticol.getConditiiPret().replace(',', '.');

        // afisare pret in um alternativa
        afisPretUmAlternativa();

        procDisc.setText(nf2.format(procDiscClient));

        discMaxAV = pretArticol.getDiscMaxAV();
        discMaxSD = pretArticol.getDiscMaxSD();

        procentTVA = getProcentTVA(pretArticol);
        double pretUnitar = (initPrice / globalCantArt) * valMultiplu;
        double valoareFaraTva = pretUnitar / procentTVA;

        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        textPretGEDFTva.setText(nf.format(valoareFaraTva));

        NumberFormat nForm2 = NumberFormat.getInstance(new Locale("en", "US"));
        nForm2.setMinimumFractionDigits(2);
        nForm2.setMaximumFractionDigits(2);

        procentTransport = pretArticol.getProcTransport();
        valoareTransport = pretArticol.getValTrap();
        double pretTransport = (initPrice) * (pretArticol.getProcTransport() / 100) + pretArticol.getValTrap();
        textTransport.setText(nForm2.format(pretTransport));
        textDataExp.setText(UtilsDates.formatDataExp(pretArticol.getDataExp()));

        // agentii nu pot modifica pretul
        if (userCannotModifyPrice()) {
            txtPretArt.setEnabled(false);
            textProcRed.setFocusable(false);
            tglProc.setEnabled(false);
        } else {
            txtPretArt.setEnabled(true);
            textProcRed.setFocusableInTouchMode(true);
            tglProc.setEnabled(true);
        }

        // pentru totaluri negociate nu se modifica preturi
        if (totalNegociat) {
            textProcRed.setFocusable(false);
            tglProc.setEnabled(false);
        }

        // se afiseaza direct pretul si nu procentul
        tglProc.setChecked(false);
        tglProc.performClick();

        if (articolModificat != null) {
            textProcRed.setText(nf2.format(articolModificat.getPretUnitarClient()));
            codArticol = articolModificat.getCodArticol();
            isArticolModificatCantPret = false;
            articolModificat = null;

        }

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
            textProcRed.setText(String.valueOf(articolAmob.getPretUnitar() * valMultiplu));
        }

        if (pretArticol.getFaraDiscount().toUpperCase().equals("X"))
            codPromo = "1";

        // nu se acorda discounturi
        if (hasArticolDiscount(pretArticol)) {
            txtPretArt.setEnabled(false);
            textProcRed.setFocusable(false);
            tglProc.setEnabled(false);

            if (Double.parseDouble(pretArticol.getCantitateArticolPromo()) != 0) {

                artPromoText = "";
                textPromo.setVisibility(View.VISIBLE);
                textPromo.setText("Articol cu promotie!");

                double pret1 = (Double.parseDouble(pretArticol.getPret()) / Double.parseDouble(pretArticol.getCantitate())) * valMultiplu;
                double pret2 = (Double.parseDouble(pretArticol.getPretArticolPromo()) / Double.parseDouble(pretArticol.getCantitateArticolPromo()))
                        * valMultiplu;

                artPromoText = "Din cantitatea comandata " + pretArticol.getCantitate() + " " + pretArticol.getUm() + " au pretul de " + nf2.format(pret1)
                        + " RON/" + pretArticol.getUm() + " si " + pretArticol.getCantitateArticolPromo() + " " + pretArticol.getUmArticolPromo()
                        + " au pretul de " + nf2.format(pret2) + " RON/" + pretArticol.getUmArticolPromo() + ".";
            }

        } else {

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

            // verificare articole promotii
            if (Double.parseDouble(pretArticol.getCantitateArticolPromo()) != 0) {
                artPromoText = "";
                textPromo.setVisibility(View.VISIBLE);

                // articolul din promotie are alt pret
                if (Double.parseDouble(pretArticol.getPretArticolPromo()) != 0) {

                } else // articolul din promotie este gratuit
                {
                    codPromo = "2";

                    // verificare cantitati articole gratuite
                    // cant. art promotie se adauga la cant. ceruta
                    if (Double.parseDouble(textCant.getText().toString().trim()) == Double.parseDouble(pretArticol.getCantitate())) {

                        // verificare cod articol promotie
                        // art. promo = art. din comanda
                        if (codArticol.equals(pretArticol.getCodArticolPromo())) {
                            artPromoText = pretArticol.getCantitateArticolPromo() + " " + pretArticol.getUmArticolPromo() + " x " + numeArticol + " gratuit. ";
                        } else// art. promo diferit de art. din cmd.
                        {
                            artPromoText = pretArticol.getCantitateArticolPromo() + " " + pretArticol.getUmArticolPromo() + " x "
                                    + pretArticol.getCodArticolPromo() + " gratuit. ";

                        }

                    } else // cant art. promotie se scade din cant.
                    // ceruta
                    {

                        artPromoText = "Din cantitatea comandata " + pretArticol.getCantitateArticolPromo() + " " + pretArticol.getUmArticolPromo()
                                + " sunt gratis.";

                    }

                    textPromo.setText("Articol cu promotie");

                }

            }

        }

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB && importAllAmob) {
            saveArtBtn.performClick();
        }

    }

    private void afisPretUmAlternativa() {

        if (valoareUmrez / valoareUmren != 1) {

            double pretUmAlt;

            if (pretMod) {

                if (textProcRed.getText().toString().trim().isEmpty())
                    return;

                pretUmAlt = Double.parseDouble(textProcRed.getText().toString()) * valoareUmrez / valoareUmren;
            } else {

                if (txtPretArt.getText().toString().trim().isEmpty())
                    return;

                pretUmAlt = Double.parseDouble(txtPretArt.getText().toString()) * valoareUmrez / valoareUmren;
            }

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            ((TextView) findViewById(R.id.txtPretUmAlt)).setText("1 " + selectedUnitMas + " = " + nf.format(pretUmAlt) + " RON");
        } else
            ((TextView) findViewById(R.id.txtPretUmAlt)).setText("");

    }

    private String addSpace(int nrCars) {
        String retVal = "";

        for (int i = 0; i < nrCars; i++)
            retVal += " ";

        return retVal;
    }

    private void afisIstoricPret(String infoIstoric) {
        LinearLayout layoutIstoric1 = (LinearLayout) findViewById(R.id.layoutIstoricPret1);
        LinearLayout layoutIstoric2 = (LinearLayout) findViewById(R.id.layoutIstoricPret2);
        LinearLayout layoutIstoric3 = (LinearLayout) findViewById(R.id.layoutIstoricPret3);

        layoutIstoric1.setVisibility(View.GONE);
        layoutIstoric2.setVisibility(View.GONE);
        layoutIstoric3.setVisibility(View.GONE);

        DecimalFormat df = new DecimalFormat("#0.00");

        double valoarePret = 0;

        if (infoIstoric.contains(":")) {
            String[] arrayIstoric = infoIstoric.split(":");

            if (arrayIstoric.length > 0 && arrayIstoric[0].contains("@")) {

                layoutIstoric1.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[0].split("@");

                if (isComandaIP())
                    valoarePret = Double.parseDouble(arrayPret[0]);
                else
                    valoarePret = Double.parseDouble(arrayPret[0]) * Constants.TVA;

                TextView textIstoric1 = (TextView) findViewById(R.id.txtIstoricPret1);
                textIstoric1.setText(" " + df.format(valoarePret) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1] + " " + arrayPret[2]
                        + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

            if (arrayIstoric.length > 1 && arrayIstoric[1].contains("@")) {

                layoutIstoric2.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[1].split("@");

                if (isComandaIP())
                    valoarePret = Double.parseDouble(arrayPret[0]);
                else
                    valoarePret = Double.parseDouble(arrayPret[0]) * Constants.TVA;

                TextView textIstoric2 = (TextView) findViewById(R.id.txtIstoricPret2);
                textIstoric2.setText(df.format(valoarePret) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1] + " " + arrayPret[2]
                        + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

            if (arrayIstoric.length > 2 && arrayIstoric[2].contains("@")) {

                layoutIstoric3.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[2].split("@");

                if (isComandaIP())
                    valoarePret = Double.parseDouble(arrayPret[0]);
                else
                    valoarePret = Double.parseDouble(arrayPret[0]) * Constants.TVA;

                TextView textIstoric3 = (TextView) findViewById(R.id.txtIstoricPret3);
                textIstoric3.setText(df.format(valoarePret) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1] + " " + arrayPret[2]
                        + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

        }

    }

    private static boolean isComandaIP() {
        return UtilsUser.isUserIP() && (CreareComandaGed.tipClientIP == EnumTipClientIP.CONSTR || CreareComandaGed.tipClientIP == EnumTipClientIP.NONCONSTR);
    }

    private boolean userCannotModifyPrice() {
        return (UserInfo.getInstance().getTipUserSap().equals("CONS-GED") || UtilsUser.isCGED() || UtilsUser.isSSCM()) && !UtilsComenzi.isComandaInstPublica()
                || CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB;
    }

    private void afiseazaArticoleAmob() {

        List<ArticolDB> listArticole = new ArrayList<ArticolDB>();

        if (CreareComandaGed.listArticoleAMOB != null && !CreareComandaGed.listArticoleAMOB.isEmpty()) {

            for (ArticolAmob articolAMOB : CreareComandaGed.listArticoleAMOB) {
                ArticolDB articol = new ArticolDB();
                articol.setCod(articolAMOB.getCodArticol());
                articol.setNume(articolAMOB.getNumeArticol());
                articol.setStoc("0");
                articol.setDepart(articolAMOB.getDepart());
                articol.setTipAB(articolAMOB.getTipAB());
                articol.setUmVanz(articolAMOB.getUmVanz());
                listArticole.add(articol);
            }

        }

        populateListViewArticol(listArticole);

    }

    private double getProcentTVA(PretArticolGed pretArticol) {
        double procent = 0;
        double valMWSI = 0;
        if (pretArticol.getConditiiPret().contains(";")) {

            String[] arrayInfo = pretArticol.getConditiiPret().split(";");
            String[] tokValue;

            for (int i = 0; i < arrayInfo.length; i++) {
                if (arrayInfo[i].toUpperCase().contains("MWSI")) {
                    tokValue = arrayInfo[i].split(":");
                    valMWSI = Double.valueOf(tokValue[1].replace(',', '.'));
                    break;
                }

            }

        }

        if (valMWSI == 0)
            procent = 0;
        else
            procent = Constants.TVA;

        return procent;
    }

    private boolean hasArticolDiscount(PretArticolGed pretArticol) {
        return pretArticol.getFaraDiscount().toUpperCase().equals("X");

    }

    public void showPromoWindow(String promoString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(promoString).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).setTitle("Promotie!").setIcon(R.drawable.promotie96);

        AlertDialog alert = builder.create();
        alert.show();

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

        articolDBSelected = articol;

        numeArticol = articol.getNume();
        codArticol = articol.getCod();

        String umVanz = articol.getUmVanz();

        tipArticol = articol.getTipAB();

        if (!tipArticol.trim().equals(""))
            numeArticol += " (" + tipArticol + ")";
        else
            tipArticol = " ";

        globalCodDepartSelectetItem = articol.getDepart();

        textNumeArticol.setText(numeArticol);
        textCodArticol.setText(codArticol);

        textUM.setText("");
        textStoc.setText("");
        textCant.setText("");

        valoareUmrez = 1;
        valoareUmren = 1;

        listUmVanz.clear();
        spinnerUnitMas.setVisibility(View.GONE);
        HashMap<String, String> tempUmVanz;
        tempUmVanz = new HashMap<String, String>();
        tempUmVanz.put("rowText", umVanz);

        listUmVanz.add(tempUmVanz);
        spinnerUnitMas.setAdapter(adapterUmVanz);

        textNumeArticol.setVisibility(View.INVISIBLE);
        textCodArticol.setVisibility(View.INVISIBLE);
        saveArtBtn.setVisibility(View.GONE);

        redBtnTable.setVisibility(View.GONE);

        try {

            if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
                articolAmob = CreareComandaGed.listArticoleAMOB.get(position);

                if (articolAmob.getDepozit().equals("BV90") || articolAmob.getDepozit().substring(2, 3).equals("1"))
                    globalDepozSel = articol.getDepart() + "V1";
                else if (articolAmob.getDepozit().substring(2, 3).equals("4"))
                    globalDepozSel = "WOOD";

                filialaAlternativa = articolAmob.getDepozit();

            } else {
                String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

                if (tokenDep[0].trim().length() == 2)
                    globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
                else
                    globalDepozSel = tokenDep[0].trim();
            }

            performListArtStoc();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Eroare!", Toast.LENGTH_SHORT).show();

        }

    }

    private String getFilialaLivrareCVIP() {

        String filialaLivrare;

        if (!isComandaClp())
            filialaLivrare = UserInfo.getInstance().getUnitLog();
        else
            filialaLivrare = DateLivrare.getInstance().getCodFilialaCLP();

        return filialaLivrare;

    }

    private boolean isComandaClp() {
        return !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty() && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
    }

    private void getStocDeteriorate() {


        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String varLocalUnitLog;

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE) {
            if (globalDepozSel.equals("MAV1"))
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP().substring(0, 2) + "2"
                        + DateLivrare.getInstance().getCodFilialaCLP().substring(3, 4);
            else
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
        } else {

            if (globalDepozSel.equals("MAV1") || globalDepozSel.equals("MAV2")) {
                if (CreareComandaGed.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = "BV92";
                else
                    varLocalUnitLog = filialaAlternativa.substring(0, 2) + "2" + filialaAlternativa.substring(3, 4);
            } else if (globalDepozSel.equals("WOOD")) {
                varLocalUnitLog = filialaAlternativa.substring(0, 2) + "4" + filialaAlternativa.substring(3, 4);
            } else {

                if (CreareComandaGed.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = CreareComandaGed.filialaAlternativa;
                else
                    varLocalUnitLog = filialaAlternativa.substring(0, 2) + "1" + filialaAlternativa.substring(3, 4);
            }

            if (UtilsUser.isUserSite()) {
                varLocalUnitLog = UtilsUser.getULUserSite(CreareComandaGed.filialaAlternativa, globalDepozSel);
            }

        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArt", codArticol);
        params.put("filiala", varLocalUnitLog);
        params.put("showCmp", "0");
        params.put("depart", UserInfo.getInstance().getCodDepart());
        opArticol.getStocDisponibil(params);


    }

    private void getStocDisponibilTCLI() {
        if (this.getListView().getAdapter().getCount() == 0 && !isDepartMathaus)
            return;

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArt", codArticol);
        params.put("filiala", DateLivrare.getInstance().getFilialaLivrareTCLI());
        params.put("showCmp", "0");
        params.put("depart", UserInfo.getInstance().getCodDepart());
        opArticol.getStocDisponibilTCLI(params);
    }


    private void performListArtStoc() {

        HashMap<String, String> params = new HashMap<String, String>();

        String varLocalUnitLog;

        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        if (isDepartMathaus && DateLivrare.getInstance().getTransport().equals("TCLI") && DateLivrare.getInstance().getFilialaLivrareTCLI() != null && !DateLivrare.getInstance().getFilialaLivrareTCLI().isEmpty()) {
            if (UtilsGeneral.isUlDistrib(DateLivrare.getInstance().getFilialaLivrareTCLI()))
                globalDepozSel = articolDBSelected.getDepart().substring(0, 2) + "V1";
            else
                globalDepozSel = "MAV1";
        }

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE || isComandaClp()) {
            if (globalDepozSel.equals("MAV1"))
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP().substring(0, 2) + "2"
                        + DateLivrare.getInstance().getCodFilialaCLP().substring(3, 4);
            else
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
        } else {

            if (globalDepozSel.equals("MAV1") || globalDepozSel.equals("MAV2")) {
                if (CreareComandaGed.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = "BV92";
                else
                    varLocalUnitLog = filialaAlternativa.substring(0, 2) + "2" + filialaAlternativa.substring(3, 4);
            } else if (globalDepozSel.equals("WOOD")) {
                varLocalUnitLog = filialaAlternativa.substring(0, 2) + "4" + filialaAlternativa.substring(3, 4);
            } else {

                if (CreareComandaGed.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = CreareComandaGed.filialaAlternativa;
                else
                    varLocalUnitLog = filialaAlternativa.substring(0, 2) + "1" + filialaAlternativa.substring(3, 4);
            }

            if (UtilsUser.isUserSite()) {
                varLocalUnitLog = UtilsUser.getULUserSite(CreareComandaGed.filialaAlternativa, globalDepozSel);
            }

        }

        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
            ((TextView) findViewById(R.id.articolAmobDetalii)).setText("Ul: " + varLocalUnitLog + " , depozit: " + globalDepozSel);
        }

        if (articolDBSelected.getDepart().equals("11") && globalDepozSel.startsWith("11")) {
            Toast.makeText(getApplicationContext(), "Pentru articole din divizia 11 selectati un depozit aferent.", Toast.LENGTH_LONG).show();
            return;
        }

        String paramDepozitStoc = globalDepozSel;

        if (isCondArtStocBV90() && cautaStocBV90) {
            varLocalUnitLog = "BV90";
            filialaStocBV90 = "BV90";

            if (articolMathaus.getDepart().equals("01"))
                paramDepozitStoc = "92V1";
        }

        if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            varLocalUnitLog = DateLivrare.getInstance().getCodFilialaFasonate();

        params.put("codArt", codArticol);
        params.put("filiala", varLocalUnitLog);
        params.put("depozit", paramDepozitStoc);
        params.put("depart", UserInfo.getInstance().getCodDepart());
        params.put("isArtMathaus", String.valueOf(isDepartMathaus));

        opArticol.getStocDepozit(params);

    }

    private boolean isCondArtStocBV90() {

        return articolMathaus != null && !DateLivrare.getInstance().getTransport().equals("TCLI") && DateLivrare.getInstance().getTipComandaGed() != TipCmdGed.ARTICOLE_DETERIORATE &&
                (articolMathaus.getDepart().equals(("01")) || articolMathaus.getDepart().equals(("02")) || articolMathaus.getDepart().equals("05"));

    }

    private void loadFactorConversie(String result) {
        String[] convResult = result.split("#");

        valoareUmrez = Integer.parseInt(convResult[0]);
        valoareUmren = Integer.parseInt(convResult[1]);

    }

    public void onBackPressed() {
        finish();
        return;
    }

    public void operationComplete(EnumArticoleDAO methodName, Object result) {
        switch (methodName) {
            case GET_ARTICOLE_DISTRIBUTIE:
            case GET_ARTICOLE_FURNIZOR:
            case GET_ARTICOLE_ACZC:
                populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
                break;
            case GET_STOC_DEPOZIT:
                listArtStoc((String) result);
                break;
            case GET_PRET_GED_JSON:
                listPretArticol(opArticol.deserializePretGed(result));
                break;
            case GET_FACTOR_CONVERSIE:
                loadFactorConversie((String) result);
                break;
            case GET_STOC_MATHAUS:
                listStocMathaus(result);
                break;
            case GET_STOC_DISPONIBIL:
                listStocDeteriorat((String) result);
                break;
            case GET_STOC_TCLI:
                listStocDisponibilTCLI((String) result);
                break;
            case GET_CABLURI_05:
                afisCabluri05(opArticol.deserializeCabluri05((String) result));
                break;
            default:
                break;

        }

    }

    @Override
    public void articolMathausSelected(ArticolMathaus articol) {
        cautaStocBV90 = false;
        verificatStocBV90 = false;
        filialaStocBV90 = "";
        afisArticolMathaus(articol);

    }

    @Override
    public void magazinMathausSelected(String filialaMathaus) {

        String filialaLivrare = getFilialaLivrareCVIP();

        if (!filialaLivrare.equals(filialaMathaus)) {
            CreareComandaGed.tipComandaGed = TipCmdGed.COMANDA_LIVRARE;
            DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_LIVRARE);
            DateLivrare.getInstance().setCodFilialaCLP(filialaMathaus);
        } else {
            CreareComandaGed.tipComandaGed = TipCmdGed.COMANDA_VANZARE;
            DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_VANZARE);
            DateLivrare.getInstance().setCodFilialaCLP("");
        }

        CreareComandaGed.permitArticoleDistribIP = false;
        ModificareComanda.permitArticoleDistribIP = false;

    }

    @Override
    public void cabluriSelected(List<BeanCablu05> listCabluri) {
        this.listCabluri = listCabluri;
        saveArtBtn.performClick();

    }

}
