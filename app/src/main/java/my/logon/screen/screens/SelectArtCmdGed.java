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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.beans.StocMathaus;
import my.logon.screen.dialogs.Cabluri05Dialog;
import my.logon.screen.dialogs.CategoriiMathausDialogNew;
import my.logon.screen.dialogs.RecomArtDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumFiliale;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.enums.EnumTipStoc;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.filters.DecimalDigitsInputFilter;
import my.logon.screen.helpers.HelperComenzi;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.helpers.HelperPreturi;
import my.logon.screen.listeners.ArticolMathausListener;
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.DownloadImageTask;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsArticole;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectArtCmdGed extends ListActivity implements OperatiiArticolListener, ArticolMathausListener, Cablu05SelectedListener {

    Button articoleBtn, saveArtBtn, pretBtn;
    String filiala = "", nume = "", cod = "", umStoc = "";
    String articolResponse = "";
    String pretResponse = "";
    String codArticol = "";
    String numeArticol = "", tipArticol = "";
    String depart = "";

    String numeClientVar = "";
    LinearLayout redBtnTable, layoutStocKA;

    public String globalDepozSel = "", artPromoText = "", globalCodDepartSelectetItem = "";

    ToggleButton tglButton, tglTipArtBtn;

    private EditText txtNumeArticol, textProcRed;
    private TextView textCodArticol, txtPretArt;
    private TextView textNumeArticol;

    ToggleButton tglProc;

    private TextView textStoc;
    private TextView textCant, textMultipluArt;

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
    private ArrayList<ArticolDB> listArticoleCustodie;
    private Spinner spinnerFilialeCustodie;
    private HashMap<String, String> paramsGetStocDepozit;
    private ArticolComanda articolComandaModif;
    private NumberFormat nfStoc;
    private double stocComandaModif;
    private int spinnerConversieCount = 0;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.selectartcmd_ged_header);

        intent = getIntent();

        nfStoc = NumberFormat.getInstance(new Locale("en", "US"));
        nfStoc.setMinimumFractionDigits(3);
        nfStoc.setMaximumFractionDigits(3);

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


        txtImpachetare = (TextView) findViewById(R.id.txtImpachetare);

        textPromo = (TextView) findViewById(R.id.textPromo);


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

        if (isLivrareCustodie()) {
            spinnerDepoz.setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.cautareArticoleLayout)).setVisibility(View.INVISIBLE);
            addSpinnerFilialeCustodie();
            getArticoleCustodie();

        } else {
            spinnerDepoz.setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.cautareArticoleLayout)).setVisibility(View.VISIBLE);
        }

        if (isWood() || UtilsUser.isCV())
            setDefaultDepoz(EnumDepoz.MAV1, arrayListDepozite);

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

        textCondPret.setVisibility(View.INVISIBLE);

        textMultipluArt.setVisibility(View.INVISIBLE);


        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB) {
            ((LinearLayout) findViewById(R.id.cautareArticoleLayout)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.articoleAmobHeaderLayout)).setVisibility(View.VISIBLE);

            Button importAllBtn = (Button) findViewById(R.id.importAmob);
            afiseazaArticoleAmob();
            setImportAmobButtonListener(importAllBtn);

        } else {
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

    private void addSpinnerFilialeCustodie() {

        ArrayAdapter<EnumFiliale> adapterFil = new ArrayAdapter<EnumFiliale>(getBaseContext(), android.R.layout.simple_list_item_1, EnumFiliale.values());
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
        spinnerFilialeCustodie = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

        spinnerFilialeCustodie.setAdapter(adapterFil);

        String tempUL10 = UtilsComenzi.getFilialaDistrib(DateLivrare.getInstance().getUnitLog());

        for (int i = 0; i < spinnerFilialeCustodie.getAdapter().getCount(); i++) {
            if (EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(i).toString()).equals(tempUL10)) {
                spinnerFilialeCustodie.setSelection(i);
                break;
            }

        }

        spinnerFilialeCustodie.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CreareComandaGed.filialaCustodie = EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(position).toString());
                getArticoleCustodie();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

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

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_AMOB)
            return;

        if (isLivrareCustodie())
            return;

        List<String> departamenteComanda = DepartamentAgent.getDepartamenteAgentGED();

        if (isComandaDL() || tipComanda.equals("S") || isOriceComandaModificata())
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

        if (DateLivrare.getInstance().getTransport().equals("TCLI") && !DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE)) {
            globalCodDepartSelectetItem = articolMathaus.getDepart();
            articolMathaus.setTip2("");
            getStocDisponibilTCLI();
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
        btnStocMathaus.setOnClickListener(v -> {

            globalCodDepartSelectetItem = articolMathaus.getDepart();

            articolDBSelected = articolMathaus;
            numeArticol = articolMathaus.getNume();

            textNumeArticol.setText(articolMathaus.getNume());
            textCodArticol.setText(articolMathaus.getCod());


            if (articolMathaus.getDepart().equals("11"))
                globalDepozSel = "MAV1";
            else
                globalDepozSel = articolMathaus.getDepart().substring(0,2) + "V1";


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
            articolMathaus.setTipStoc(EnumTipStoc.SAP);
            performGetStocSap();
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

            if (codArticol.length() > 0 && spinnerConversieCount > 0) {

                artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                selectedUnitMas = artMap.get("rowText");

                getFactoriConversie();

            }

            spinnerConversieCount++;

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void getFactoriConversie() {

        HashMap<String, String> params = new HashMap<String, String>();

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        params.put("codArt", codArticol);
        params.put("unitMas", selectedUnitMas);

        opArticol.getFactorConversie(params);

    }

    private void getFactoriConversieModifCmd(String codArticolModif, String umModif) {

        HashMap<String, String> params = new HashMap<String, String>();

        if (codArticolModif.length() == 8)
            codArticolModif = "0000000000" + codArticolModif;

        params.put("codArt", codArticolModif);
        params.put("unitMas", umModif);

        opArticol.getFactorConversieModifCmd(params);

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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void getArticoleCustodie() {

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codClient", CreareComandaGed.codClientVar);
        params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));
        params.put("departament", selectedDepartamentAgent);

        opArticol.getArticoleCustodie(params);
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

                                    }
                                }

                            } else {
                                txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));


                            }

                        }// modificare procent
                        else {

                            if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

                                double val1 = Double.parseDouble(textProcRed.getText().toString());

                                procR = (((initPrice / globalCantArt * valMultiplu) - val1) / ((initPrice / globalCantArt * valMultiplu))) * 100;

                                txtPretArt.setText(nf2.format(procR));
                                finalPrice = Double.parseDouble(textProcRed.getText().toString());


                            } else {
                                txtPretArt.setText("0");
                                finalPrice = 0;
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

        if (isWood()) {
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "4" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        }

        if (UtilsUser.isUserCVOB() && isOriceComandaModificata())
            uLog = ModificareComanda.filialaAlternativaM.substring(0, 2) + "2" + ModificareComanda.filialaAlternativaM.substring(3, 4);

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
        paramPret.setTipUser(UserInfo.getInstance().getTipUserSap());
        paramPret.setMetodaPlata(DateLivrare.getInstance().getTipPlata());
        paramPret.setTermenPlata(DateLivrare.getInstance().getTermenPlata());
        paramPret.setCodJudet(getCodJudetPret());
        paramPret.setLocalitate(getLocalitatePret());
        paramPret.setFilialaAlternativa(CreareComandaGed.filialaAlternativa);
        paramPret.setCodClientParavan(CreareComandaGed.codClientParavan);
        paramPret.setFilialaClp(DateLivrare.getInstance().getCodFilialaCLP());
        paramPret.setTipTransport(DateLivrare.getInstance().getTransport());

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));

        opArticol.getPretUnic(params);

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
        if (isComandaDL() && DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.DISPOZITIE_LIVRARE) {
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

        if (UtilsUser.isAV())
            departCautare = DepartamentAgent.getDepartArticole(departCautare);

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
        params.put("transpTert", String.valueOf(DateLivrare.getInstance().getTranspInit().equals("TERT")));

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

    @SuppressWarnings("unchecked")
    public void addListenerBtnSaveArt() {
        saveArtBtn.setOnClickListener(v -> {

            try {

                if (isLivrareCustodie()) {
                    saveArticolCustodie();
                    return;
                }

                if (UtilsComenzi.isPoligonRestrictionat()) {
                    Toast.makeText(this, Constants.ARTICOL_ZONA_RESTRICTIONATA, Toast.LENGTH_LONG).show();
                    return;
                }

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
                        articol.setFilialaSite(DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog());
                    else
                        articol.setFilialaSite(CreareComandaGed.filialaAlternativa);

                    if (isOriceComandaModificata() && UtilsUser.isUserCVOB())
                        articol.setFilialaSite(ModificareComanda.filialaAlternativaM);

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

                    articol.setSintetic(articolDBSelected.getSintetic());
                    articol.setPretMinim(selectedArticol.getPretMinim());

                    if (HelperMathaus.isComandaVanzareTCLI()) {

                        if (!UtilsUser.isConsWood() && !UtilsComenzi.isDepozitUnitLog(articol.getDepozit(), DateLivrare.getInstance().getFilialaLivrareTCLI().getDepozite())) {
                            Toast.makeText(getApplicationContext(), "Nu puteti folosi acest depozit pentru " + DateLivrare.getInstance().getFilialaLivrareTCLI().getNumeFiliala() + ".", Toast.LENGTH_LONG).show();
                            return;
                        }

                        listStocTCLI = HelperMathaus.getStocTCLIDepozit(textStoc.getText().toString(), articol.getDepozit(), articol.getUm());
                    }

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

    private void getPretArtCustodie() {

        HashMap<String, String> params = new HashMap<String, String>();

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String paramUnitMas = textUM.getText().toString();
        String uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(CreareComandaGed.codClientVar);
        paramPret.setArticol(codArticol);

        paramPret.setCantitate(textCant.getText().toString().trim());
        paramPret.setDepart(globalCodDepartSelectetItem.substring(0, 2));
        paramPret.setUm(paramUnitMas);
        paramPret.setUl(uLog);
        paramPret.setDepoz("CUSC");
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib("20");
        paramPret.setTipUser(UserInfo.getInstance().getTipUser());
        paramPret.setMetodaPlata(DateLivrare.getInstance().getTipPlata());
        paramPret.setTermenPlata(DateLivrare.getInstance().getTermenPlata());
        paramPret.setCodJudet(DateLivrare.getInstance().getCodJudet());
        paramPret.setLocalitate(DateLivrare.getInstance().getOras());
        paramPret.setFilialaAlternativa(CreareComandaGed.filialaAlternativa);
        paramPret.setCodClientParavan("");
        paramPret.setFilialaClp(DateLivrare.getInstance().getCodFilialaCLP());
        paramPret.setTipTransport(DateLivrare.getInstance().getTransport());

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));
        opArticol.getPretUnicCustodie(params);

    }

    private void listPretArticolCustodie(PretArticolGed pretArticol) {

        if (codArticol.length() == 18)
            codArticol = codArticol.substring(10, 18);

        String cantArticol = textCant.getText().toString().trim();

        double initPriceCust = Double.valueOf(pretArticol.getPret());
        double valMultipluCust = Double.valueOf(pretArticol.getMultiplu());
        double cantCust = Double.valueOf(cantArticol);

        double priceArtCust = (initPriceCust / cantCust) * valMultipluCust;

        ArticolComandaGed unArticol = new ArticolComandaGed();
        unArticol.setNumeArticol(numeArticol);
        unArticol.setCodArticol(codArticol);
        unArticol.setCantitate(Double.valueOf(cantArticol));
        unArticol.setDepozit(globalDepozSel);
        unArticol.setPretUnit(priceArtCust);
        unArticol.setProcent(0);
        unArticol.setUm(textUM.getText().toString());
        unArticol.setProcentFact(0);
        unArticol.setConditie(false);
        unArticol.setDiscClient(0);
        unArticol.setProcAprob(0);
        unArticol.setMultiplu(1);
        unArticol.setPret(priceArtCust * unArticol.getCantitate());
        unArticol.setMoneda("RON");
        unArticol.setInfoArticol("");
        unArticol.setCantUmb(cantCust);
        unArticol.setUmb(pretArticol.getUmBaza());
        unArticol.setAlteValori("");
        unArticol.setDepart(globalCodDepartSelectetItem);
        unArticol.setTipArt("");
        unArticol.setPromotie(0);
        unArticol.setObservatii("");
        unArticol.setDepartAprob(articolDBSelected.getDepartAprob());
        unArticol.setUmPalet(false);
        unArticol.setCategorie("");
        unArticol.setLungime(0);
        unArticol.setCmp(Double.valueOf(pretArticol.getCmp()));
        unArticol.setGreutate(pretArticol.getGreutate());
        unArticol.setCantitate50(unArticol.getCantitate50());
        unArticol.setUm50(unArticol.getUm50());
        unArticol.setFilialaSite(CreareComandaGed.filialaAlternativa);
        unArticol.setTipMarfa(pretArticol.getTipMarfa());
        unArticol.setGreutateBruta(pretArticol.getGreutateBruta());
        unArticol.setLungimeArt(pretArticol.getLungimeArt());

        ListaArticoleComandaGed listaComanda = ListaArticoleComandaGed.getInstance();
        listaComanda.addArticolComanda(unArticol);

        textNumeArticol.setText("");
        textCodArticol.setText("");
        textUM.setText("");
        textStoc.setText("");
        textCant.setText("");
        txtNumeArticol.setText("");
        resultLayout.setVisibility(View.INVISIBLE);

    }

    private void saveArticolCustodie() {

        if (textCant.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Cantitate invalida", Toast.LENGTH_LONG).show();
            return;
        }

        if (Double.parseDouble(textCant.getText().toString()) <= 0) {
            Toast.makeText(getApplicationContext(), "Cantitate invalida", Toast.LENGTH_LONG).show();
            return;
        }

        if (Double.parseDouble(textCant.getText().toString().trim()) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", ""))) {
            Toast.makeText(getApplicationContext(), "Stoc insuficient.", Toast.LENGTH_LONG).show();
            return;
        }

        getPretArtCustodie();

    }

    private boolean isLivrareCustodie() {
        return DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.LIVRARE_CUSTODIE;
    }

    private boolean isComandaDL() {
        return DateLivrare.getInstance().getFurnizorComanda() != null && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().isEmpty()
                && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().length() > 4;
    }

    private void listStocDisponibilTCLI(String stocResponse) {

        resultLayout.setVisibility(View.VISIBLE);
        listStocTCLI = new ArrayList<>();
        List<BeanStocTCLI> listStocDepozit = new ArrayList<>();

        if (!stocResponse.equals("-1")) {

            String[] tokStocArt = stocResponse.split("!");
            String[] tokenMain = tokStocArt[0].split("@@");
            double stocTotalTCLI = 0;
            String umStocTCLI = "BUC";

            for (int i = 0; i < tokenMain.length; i++) {
                String[] articol = tokenMain[i].toString().split("#");

                if (UtilsComenzi.isDepozitUnitLog(articol[2], DateLivrare.getInstance().getFilialaLivrareTCLI().getDepozite())) {

                    BeanStocTCLI beanStocTCLI = new BeanStocTCLI();
                    beanStocTCLI.setCantitate(Double.parseDouble(articol[0]));
                    beanStocTCLI.setUm(articol[1]);
                    beanStocTCLI.setDepozit(articol[2]);
                    listStocDepozit.add(beanStocTCLI);


                }
            }

            BeanStocTCLI stocMaxim = UtilsComenzi.getStocMaxim(listStocDepozit);
            if (stocMaxim != null) {
                listStocTCLI.add(stocMaxim);
                stocTotalTCLI = stocMaxim.getCantitate();
                umStocTCLI = stocMaxim.getUm();
                globalDepozSel = stocMaxim.getDepozit();
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

    private boolean isComandaModif() {
        return ModificareComanda.codClientVar != null && !ModificareComanda.codClientVar.trim().isEmpty() && !isComandaDL();
    }

    private boolean isOriceComandaModificata() {
        return ModificareComanda.codClientVar != null && !ModificareComanda.codClientVar.trim().isEmpty();
    }

    @SuppressWarnings("unchecked")
    private void listArtStoc(String stocResponse) {

        listStocTCLI = new ArrayList<>();
        articolComandaModif = null;
        stocComandaModif = 0;

        if (!stocResponse.equals("-1")) {

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

            double cantArtModificat = 0;
            stocComandaModif = Double.valueOf(tokenPret[0]);
            if (isComandaModif()) {

                articolComandaModif = HelperComenzi.getArticolModifCmd(paramsGetStocDepozit);
                if (articolComandaModif != null) {
                    if (tokenPret[1].equals(articolComandaModif.getUm()))
                        cantArtModificat = articolComandaModif.getCantitate();
                    else
                        getFactoriConversieModifCmd(articolComandaModif.getCodArticol(), articolComandaModif.getUm());
                }

            }

            textUM.setText(tokenPret[1]);
            textStoc.setText(nfStoc.format(Double.valueOf(tokenPret[0]) + cantArtModificat));

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
            } else if (isArticolModificatCantPret) {
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

    public void addFilialaMathaus(String filiala, Context context) {

        filiala = UtilsComenzi.getFilialaGed(filiala);

        if (!DateLivrare.getInstance().getFilialeExtraMathaus().contains(filiala)) {

            if (DateLivrare.getInstance().getFilialeExtraMathaus().isEmpty())
                DateLivrare.getInstance().setFilialeExtraMathaus(filiala);
            else
                DateLivrare.getInstance().setFilialeExtraMathaus(DateLivrare.getInstance().getFilialeExtraMathaus() + "," + filiala);
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

    private void listArtStocCustodie(String stocResponse) {
        if (!stocResponse.equals("-1")) {

            NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
            ;
            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);

            resultLayout.setVisibility(View.VISIBLE);
            String[] tokenStoc = stocResponse.split("#");

            textNumeArticol.setVisibility(View.VISIBLE);
            textCodArticol.setVisibility(View.VISIBLE);
            textUM.setVisibility(View.VISIBLE);
            textStoc.setVisibility(View.VISIBLE);
            textCant.setVisibility(View.VISIBLE);
            labelCant.setVisibility(View.VISIBLE);
            labelStoc.setVisibility(View.VISIBLE);

            textUM.setText(tokenStoc[1]);
            textStoc.setText(nf2.format(Double.valueOf(tokenStoc[0])));

            initPrice = Double.parseDouble(tokenStoc[3].trim());


            saveArtBtn.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();
        }
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
        return Double.parseDouble(selectedArticol.getCantitate50()) > 0
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
        textPromo.setVisibility(View.INVISIBLE);

        if (pretArticol.isPromo()) {
            textPromo.setVisibility(View.VISIBLE);
            textPromo.setText("Articol cu promotie.");
        }

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

        listArtRecom = pretArticol.getArticoleRecomandate();
        ((LinearLayout) findViewById(R.id.layoutRecommend)).setVisibility(View.GONE);
        if (!listArtRecom.isEmpty()) {
            ((LinearLayout) findViewById(R.id.layoutRecommend)).setVisibility(View.VISIBLE);
        }

        if (pretArticol.getIstoricPret().trim().isEmpty())
            findViewById(R.id.textIstoricPret).setVisibility(View.GONE);
        else {
            findViewById(R.id.textIstoricPret).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textIstoricPret)).setText(HelperPreturi.getIstoricPret(pretArticol.getIstoricPret()));
        }

        selectedArticol.setIstoricPret(UtilsFormatting.getIstoricPret(pretArticol.getIstoricPret(), EnumTipComanda.GED));

        txtImpachetare.setText(pretArticol.getImpachetare());

        if (!pretArticol.getErrMsg().isEmpty()) {
            Toast.makeText(getApplicationContext(), pretArticol.getErrMsg(), Toast.LENGTH_LONG).show();
            saveArtBtn.setVisibility(View.INVISIBLE);
            return;
        }

        if (globalDepozSel.substring(2, 3).equals("V")) {

            if (initPrice / globalCantArt * valMultiplu < cmpArt && !UtilsArticole.isArticolPermitSubCmp(codArticol)) {
                Toast.makeText(getApplicationContext(), "Pret sub cmp.", Toast.LENGTH_LONG).show();

                if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_AMOB && importAllAmob) {
                    setProcesatArticolAmob(codArticol, globalDepozSel);
                    proceseazaArticoleAmob();
                }
            }
        }


        textCondPret.setVisibility(View.VISIBLE);
        textCondPret.setText(HelperPreturi.getInfoPret(pretArticol, nf2));

        finalPrice = initPrice;
        textProcRed.setText("");

        pretVanzare = initPrice;

        redBtnTable.setVisibility(View.VISIBLE);
        textProcRed.setVisibility(View.VISIBLE);


        textMultipluArt.setVisibility(View.VISIBLE);

        labelFactConv.setVisibility(View.INVISIBLE);

        txtPretArt.setText(nf2.format((initPrice / globalCantArt) * valMultiplu));
        txtPretArt.setHint(nf2.format((initPrice / globalCantArt) * valMultiplu));

        infoArticol = pretArticol.getConditiiPret().replace(',', '.');

        // afisare pret in um alternativa
        afisPretUmAlternativa();


        discMaxAV = pretArticol.getDiscMaxAV();
        discMaxSD = pretArticol.getDiscMaxSD();

        procentTVA = getProcentTVA(pretArticol);
        double pretUnitar = (initPrice / globalCantArt) * valMultiplu;

        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);


        NumberFormat nForm2 = NumberFormat.getInstance(new Locale("en", "US"));
        nForm2.setMinimumFractionDigits(2);
        nForm2.setMaximumFractionDigits(2);

        procentTransport = pretArticol.getProcTransport();
        valoareTransport = pretArticol.getValTrap();


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


    private boolean userCannotModifyPrice() {
        return (UserInfo.getInstance().getTipUserSap().equals("CONS-GED") || UtilsUser.isCGED()) && !UtilsComenzi.isComandaInstPublica()
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

            if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.LIVRARE_CUSTODIE)
                performListArtStocCustodie();
            else
                performListArtStoc();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Eroare!", Toast.LENGTH_SHORT).show();

        }

    }

    private void performListArtStocCustodie() {

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArticol", codArticol);
        params.put("codClient", CreareComandaGed.codClientVar);
        params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));

        opArticol.getStocCustodie(params);
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

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArt", codArticol);
        params.put("filiala", DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog());
        params.put("showCmp", "0");
        params.put("depart", UserInfo.getInstance().getCodDepart());
        opArticol.getStocDisponibilTCLI(params);
    }


    private void performGetStocSap() {

        HashMap<String, String> params = new HashMap<String, String>();

        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String varLocalUnitLog;

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE || isComandaClp()) {
            varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
        } else
            varLocalUnitLog = CreareComandaGed.filialaAlternativa;

        if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            varLocalUnitLog = DateLivrare.getInstance().getCodFilialaFasonate();

        params.put("codArt", codArticol);
        params.put("filiala", varLocalUnitLog);
        params.put("um", articolMathaus.getUmVanz());
        params.put("tipUser", UserInfo.getInstance().getTipUserSap());

        opArticol.getStocSap(params);
    }

    private void performListArtStoc() {

        spinnerConversieCount = 0;

        paramsGetStocDepozit = new HashMap<>();

        String varLocalUnitLog;

        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;


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

                if (isOriceComandaModificata())
                    varLocalUnitLog = UtilsUser.getULUserSite(ModificareComanda.filialaAlternativaM, globalDepozSel);
                else
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

        paramsGetStocDepozit.put("codArt", codArticol);
        paramsGetStocDepozit.put("filiala", varLocalUnitLog);
        paramsGetStocDepozit.put("depozit", paramDepozitStoc);
        paramsGetStocDepozit.put("depart", UserInfo.getInstance().getCodDepart());
        paramsGetStocDepozit.put("isArtMathaus", String.valueOf(isDepartMathaus));

        opArticol.getStocDepozit(paramsGetStocDepozit);

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

    private void loadFactorConversieModifCmd(String result) {
        String[] convResult = result.split("#");

        double valoareUmrezLocal = Integer.parseInt(convResult[0]);
        double valoareUmrenLocal = Integer.parseInt(convResult[1]);

        if (isComandaModif() && articolComandaModif != null) {
            double cantArtModificat = articolComandaModif.getCantitate() * (valoareUmrezLocal / valoareUmrenLocal);
            textStoc.setText(nfStoc.format(stocComandaModif + cantArtModificat));
        }

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
            case GET_STOC_SAP:
            case GET_STOC_DEPOZIT:
                listArtStoc((String) result);
                break;
            case GET_PRET_UNIC:
            case GET_PRET_GED_JSON:
                listPretArticol(opArticol.deserializePretGed(result));
                break;
            case GET_FACTOR_CONVERSIE:
                loadFactorConversie((String) result);
                break;
            case GET_FACTOR_CONVERSIE_MODIF_CMD:
                loadFactorConversieModifCmd((String) result);
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
            case GET_ARTICOLE_CUSTODIE:
                listArticoleCustodie = opArticol.deserializeArticoleVanzare((String) result);
                populateListViewArticol(listArticoleCustodie);
                break;
            case GET_STOC_CUSTODIE:
                listArtStocCustodie((String) result);
                break;
            case GET_PRET_UNIC_CUSTODIE:
                listPretArticolCustodie(opArticol.deserializePretGed(result));
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
    public void cabluriSelected(List<BeanCablu05> listCabluri) {
        this.listCabluri = listCabluri;
        saveArtBtn.performClick();

    }

}
