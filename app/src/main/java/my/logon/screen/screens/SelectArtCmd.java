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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.ArticolCant;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.ArticolMathaus;
import my.logon.screen.beans.BeanArticolCautare;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.dialogs.ArticoleCantDialog;
import my.logon.screen.dialogs.Cabluri05Dialog;
import my.logon.screen.dialogs.CategoriiMathausDialogNew;
import my.logon.screen.dialogs.RecomArtDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumFiliale;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.enums.EnumTipStoc;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.helpers.HelperComenzi;
import my.logon.screen.helpers.HelperMathaus;
import my.logon.screen.helpers.HelperPreturi;
import my.logon.screen.listeners.ArticolCantListener;
import my.logon.screen.listeners.ArticolMathausListener;
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.DownloadImageTask;
import my.logon.screen.model.ListaArticoleComanda;
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

public class SelectArtCmd extends ListActivity implements OperatiiArticolListener, ArticolCantListener, ArticolMathausListener, Cablu05SelectedListener {

    Button articoleBtn, saveArtBtn, pretBtn;
    String filiala = "", nume = "", cod = "", umStoc = "";
    String articolResponse = "";
    String pretResponse = "";
    String codArticol = "";
    String numeArticol = "", tipArticol = "";
    String depart = "";
    String codClientVar = "";
    String numeClientVar = "";
    LinearLayout redBtnTable, layoutStocKA;
    EditText valRedIntText, valRedDecText;

    public String globalDepozSel = "", artPromoText = "", globalCodDepartSelectetItem = "";

    ToggleButton tglButton, tglTipArtBtn;

    private EditText txtNumeArticol, textProcRed;
    private TextView textCodArticol, txtPretArt;
    private TextView textNumeArticol, textStocKA, textUmKA;

    ToggleButton tglProc;

    private TextView textStoc;
    private TextView textCant,  textMultipluArt;

    private TextView textUM;
    private TextView labelCant, labelStoc;
    private Spinner spinnerDepoz, spinnerUnitMas;

    private TextView textPromo, textCondPret;

    private boolean pretMod = false;

    private double initPrice = 0, cmpArt = 0;
    private double finalPrice = 0, minimKAPrice = 0;
    private double listPrice = 0, procDiscClient = 0;
    private double discMaxAV = 0, discMaxSD = 0;
    private double pretVanzare = 0, procentAprob = 0, selectedCant = 0;
    private String istoricPret;
    private double procReducereCmp = 0;
    private double valoareUmrez = 1, valoareUmren = 1;

    private String cantitate50 = "", um50 = "";

    NumberFormat nf2;
    private PretArticolGed selectedArticol;

    private static ArrayList<HashMap<String, String>> listUmVanz = null;

    SimpleAdapter adapterUmVanz;
    private double varProc = 0, valMultiplu = 0, greutateArt = 0;
    private String tipMarfa = "";
    private double greutateBruta = 0;
    private String lungimeArt = "";


    String tipAlert = "", codPromo = "", infoArticol = "", Umb = "", cantUmb = "", selectedUnitMas = "", selectedUnitMasPret = "";

    private HashMap<String, String> artMap = null;
    double procR = 0, globalCantArt = 0;
    private String depozUnic = "", unitLogUnic = "";
    Dialog dialogSelFilArt;

    LinearLayout resultLayout;
    OperatiiArticol opArticol;

    private String selectedDepartamentAgent;
    private ArrayAdapter<String> adapterSpinnerDepozite;
    private ArticolDB articolDBSelected;
    private TextView txtImpachetare;

    private enum EnumTipCautare {
        NOMINAL, STATISTIC;
    }

    private EnumTipCautare tipCautareArticol = EnumTipCautare.NOMINAL;

    private ArrayList<ArticolDB> listArticoleStatistic;
    private ArrayList<ArticolDB> listArticoleCustodie;
    private double discountASDL;

    private Spinner spinnerFilialeCustodie;
    private String dataExpPret;
    private List<ArticolCant> listArticoleCant;

    private boolean isDepartMathaus;
    private ArticolMathaus articolMathaus;
    private CategoriiMathausDialogNew categoriiDialog;
    private Button btnStocMathaus;
    private Button btnCatMathaus;
    private List<BeanCablu05> listCabluri;
    private boolean cautaStocBV90 = false;
    private boolean verificatStocBV90 = false;
    private String filialaStocBV90 = "";
    private Button btnArtRecom;
    private List<BeanArticolCautare> listArtRecom;

    private List<BeanStocTCLI> listStocTCLI;
    private boolean isArticolModificatCantPret;
    private ArticolComanda articolModificat;
    private Spinner spinnerDepartament;
    private LayoutInflater mInflater;
    private View mCustomView;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {

            setTheme(R.style.LRTheme);
            setContentView(R.layout.selectartcmdheader);

            mInflater = LayoutInflater.from(this);
            mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
            spinnerDepartament = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

            initSelectionDepartament();

            if (!isCV())
                addSpinnerDepartamente();

            opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
            opArticol.setListener(this);

            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            resultLayout = (LinearLayout) findViewById(R.id.resLayout);
            resultLayout.setVisibility(View.INVISIBLE);

            nf2 = NumberFormat.getInstance(new Locale("en", "US"));

            textStocKA = (TextView) findViewById(R.id.textStocKA);
            textUmKA = (TextView) findViewById(R.id.textUmKA);

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

            CreareComanda.articoleComanda = "";

            spinnerDepoz = (Spinner) findViewById(R.id.spinnerDepoz);

            ArrayList<String> arrayListDepozite = new ArrayList<String>();
            arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteDistributie()));
            adapterSpinnerDepozite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListDepozite);

            adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDepoz.setAdapter(adapterSpinnerDepozite);
            spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

            if (isLivrareCustodie()) {
                spinnerDepoz.setVisibility(View.INVISIBLE);
                ((LinearLayout) findViewById(R.id.layoutHeaderArt)).setVisibility(View.INVISIBLE);
                addSpinnerFilialeCustodie();
                getArticoleCustodie();

            } else {
                spinnerDepoz.setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.layoutHeaderArt)).setVisibility(View.VISIBLE);
            }

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

            txtPretArt.setVisibility(View.INVISIBLE);
            labelStoc.setVisibility(View.INVISIBLE);
            saveArtBtn.setVisibility(View.INVISIBLE);

            redBtnTable.setVisibility(View.INVISIBLE);
            textProcRed.setVisibility(View.INVISIBLE);
            pretBtn.setVisibility(View.INVISIBLE);
            textPromo.setVisibility(View.INVISIBLE);

            textCondPret.setVisibility(View.INVISIBLE);

            textMultipluArt.setVisibility(View.INVISIBLE);

            btnCatMathaus = (Button) findViewById(R.id.btnBackToList);
            setCatMathausListener();

            btnStocMathaus = (Button) findViewById(R.id.btnStocMathaus);
            setStocMathausListener();

            btnArtRecom = (Button) findViewById(R.id.afisArtRecomBtn);
            setArtRecomListener();

            isArticolModificatCantPret = false;
            articolModificat = null;

            bundle = getIntent().getExtras();

            if (bundle != null && bundle.getString("indexArticolModificat") != null) {
                isArticolModificatCantPret = true;
                int indexArtModif = Integer.parseInt(getIntent().getExtras().getString("indexArticolModificat"));
                articolModificat = ListaArticoleComanda.getInstance().getListArticoleComanda().get(indexArtModif);
                trateazaModificarePretCantitate(articolModificat);

            }

        }
        catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
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
            spinnerDepartament.setSelection(0);
        } else
            spinnerDepartament.setSelection(spinnerDepartament.getAdapter().getCount() - 1);

        spinnerDepartament.setVisibility(View.INVISIBLE);

    }

    private void trateazaModificarePretCantitate(ArticolComanda articolModificat) {

        setupSpinnerDepartModifArticol();

        if (articolModificat.getArticolMathaus() == null) {
            isDepartMathaus = false;
            CreareComanda.filialaAlternativa = articolModificat.getFilialaSite();
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

        for (int i = 0; i < spinnerFilialeCustodie.getAdapter().getCount(); i++) {
            if (EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(i).toString()).equals(DateLivrare.getInstance().getUnitLog())) {
                spinnerFilialeCustodie.setSelection(i);
                break;
            }

        }

        spinnerFilialeCustodie.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CreareComanda.filialaCustodie = EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(position).toString());
                getArticoleCustodie();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

    }

    private void addSpinnerDepartamente() {

        if (isLivrareCustodie())
            return;

        List<String> departamenteComanda = DepartamentAgent.getDepartamenteAgent();

        if (isComandaDL())
            departamenteComanda.remove("Catalog site");
        else if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_VANZARE) {

            String localFilialaMathaus = CreareComanda.filialaLivrareMathaus;

            if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
                localFilialaMathaus = DateLivrare.getInstance().getCodFilialaFasonate();

            if (!localFilialaMathaus.contains(UserInfo.getInstance().getUnitLog())) {
                departamenteComanda.clear();
                departamenteComanda.add("Catalog site");
                DateLivrare.getInstance().setTipComandaDistrib(TipCmdDistrib.COMANDA_LIVRARE);
                DateLivrare.getInstance().setCodFilialaCLP(localFilialaMathaus);
            } else {
                DateLivrare.getInstance().setTipComandaDistrib(TipCmdDistrib.COMANDA_VANZARE);
                DateLivrare.getInstance().setCodFilialaCLP("");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
                departamenteComanda);


        spinnerDepartament.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (spinnerDepartament.getSelectedItem().toString().equalsIgnoreCase("catalog site")) {
                    selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();
                    isDepartMathaus = true;
                    spinnerDepoz.setVisibility(View.INVISIBLE);
                } else {
                    selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerDepartament.getSelectedItem().toString());
                    isDepartMathaus = false;
                    spinnerDepoz.setVisibility(View.VISIBLE);
                }

                if (!isArticolModificatCantPret)
                    populateListViewArticol(new ArrayList<ArticolDB>());

                if (selectedDepartamentAgent.equals("11") || selectedDepartamentAgent.equals("05")) {
                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());

                    UtilsGeneral.trateazaExceptieMAV_BU(adapterSpinnerDepozite);

                    if (selectedDepartamentAgent.equals("11"))
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
                    else
                        spinnerDepoz.setSelection(0);
                } else {

                    if (UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED()) {
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
                        spinnerDepoz.setEnabled(false);
                    } else {
                        adapterSpinnerDepozite.clear();
                        adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteDistributie());
                        spinnerDepoz.setSelection(0);
                    }
                }

                if (isDepartMathaus) {
                    setArtMathausVisibility(true);

                    if (articolModificat == null)
                        showCategoriiMathaus();
                } else
                    setArtMathausVisibility(false);

                if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE)) {

                    spinnerDepoz.setEnabled(false);
                    if (spinnerDepartament.getSelectedItem().toString().equalsIgnoreCase("MAGAZIN") && selectedDepartamentAgent.equals("11"))
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAD1"));
                    else
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("D1 - deteriorate"));

                }

                setupSpinnerDepozModifArticol();

                saveArtBtn.setVisibility(View.INVISIBLE);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerDepartament.setAdapter(adapter);
        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

    }

    private void showCategoriiMathaus() {

        if (categoriiDialog == null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.97);
            categoriiDialog = new CategoriiMathausDialogNew(SelectArtCmd.this);
            categoriiDialog.setFilialaLivrareMathaus(CreareComanda.filialaLivrareMathaus);
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

        if (articol.getDepart().equals("11"))
            globalDepozSel = "MAV1";
        else
            globalDepozSel = articol.getDepart().substring(0, 2) + "V1";

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

        if (DateLivrare.getInstance().getTransport().equals("TCLI") && !DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE)) {
            globalCodDepartSelectetItem = articolMathaus.getDepart();
            articolMathaus.setTip2("");
            getStocDisponibilTCLI();
        } else if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE)) {
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

                HashMap<String, String> params = new HashMap<String, String>();

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

                String filialaStocMathaus = CreareComanda.filialaAlternativa;
                if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE)
                    filialaStocMathaus = DateLivrare.getInstance().getCodFilialaCLP();

                String stocFilialaSecundara = HelperMathaus.getFilialaSecundara();

                if (!stocFilialaSecundara.isEmpty())
                    filialaStocMathaus += "," + stocFilialaSecundara;

                params.put("filiala", filialaStocMathaus);
                params.put("codArticol", codArticol);
                params.put("um", umVanz);
                params.put("tipCmd", "D");
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
            articolMathaus.setTipStoc(EnumTipStoc.SAP);
            performGetStocSap();
        }


    }

    private void initSelectionDepartament() {

        selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

        if (isCV())
            selectedDepartamentAgent = "";

        if (isKA() || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD())
            selectedDepartamentAgent = "00";
    }

    private void CreateMenu(Menu menu) {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
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

    boolean isKA() {
        return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
    }

    boolean isCV() {
        return (UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")) && !UtilsUser.isSMR()
                && !UtilsUser.isCVR() && !UtilsUser.isSSCM() && !UtilsUser.isCGED() && !UtilsUser.isOIVPD();
    }

    // eveniment selectie depozit
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


            case 1:
                showCautareStatisticDialog();
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showCautareStatisticDialog() {

        final Dialog dialogStatistic = new Dialog(SelectArtCmd.this);
        dialogStatistic.setContentView(R.layout.cautare_statistic_dialog);
        dialogStatistic.setTitle("Cautare articole");
        dialogStatistic.setCancelable(true);
        dialogStatistic.show();

        final RadioButton radioNominal = (RadioButton) dialogStatistic.findViewById(R.id.radio1);
        final RadioButton radioStatistic = (RadioButton) dialogStatistic.findViewById(R.id.radio2);

        if (tipCautareArticol == EnumTipCautare.NOMINAL)
            radioNominal.setChecked(true);
        else if (tipCautareArticol == EnumTipCautare.STATISTIC)
            radioStatistic.setChecked(true);

        Button btnStatistic = (Button) dialogStatistic.findViewById(R.id.btnStatistic);

        btnStatistic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (radioNominal.isChecked())
                    tipCautareArticol = EnumTipCautare.NOMINAL;
                else if (radioStatistic.isChecked())
                    tipCautareArticol = EnumTipCautare.STATISTIC;

                dialogStatistic.dismiss();

                trateazaTipCautareArticole(tipCautareArticol);

            }
        });

    }

    private void trateazaTipCautareArticole(EnumTipCautare tipCautare) {
        if (tipCautare == EnumTipCautare.NOMINAL) {
            setCautaLayoutVisibility(true);
            populateListViewArticol(new ArrayList<ArticolDB>());

        } else if (tipCautare == EnumTipCautare.STATISTIC) {
            setCautaLayoutVisibility(false);
            populateListViewArticol(new ArrayList<ArticolDB>());
            getArticoleStatistic();
        }
    }

    private void setCautaLayoutVisibility(boolean isVisible) {
        if (isVisible) {
            articoleBtn.setVisibility(View.VISIBLE);
            tglButton.setVisibility(View.VISIBLE);
            tglTipArtBtn.setVisibility(View.VISIBLE);
            txtNumeArticol.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.GONE);

        } else {
            articoleBtn.setVisibility(View.INVISIBLE);
            tglButton.setVisibility(View.INVISIBLE);
            tglTipArtBtn.setVisibility(View.INVISIBLE);
            txtNumeArticol.setVisibility(View.INVISIBLE);

        }

    }

    private void getArticoleStatistic() {

        if (listArticoleStatistic != null && !listArticoleStatistic.isEmpty()) {
            ((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);
            populateListViewArticol(listArticoleStatistic);
            return;
        }

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codClient", CreareComanda.codClientVar);
        params.put("filiala", UserInfo.getInstance().getUnitLog());
        params.put("departament", selectedDepartamentAgent);

        opArticol.getArticoleStatistic(params);
    }

    private void getArticoleCustodie() {

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codClient", CreareComanda.codClientVar);
        params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));
        params.put("departament", selectedDepartamentAgent);

        opArticol.getArticoleCustodie(params);
    }




    public void addListenerTglProc() {
        tglProc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (globalCantArt > 0) {

                    if (tglProc.isChecked()) {

                        nf2.setMinimumFractionDigits(3);
                        nf2.setMaximumFractionDigits(3);

                        varProc = -1;

                        textProcRed.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                        textProcRed.requestFocus();
                        textProcRed.selectAll();

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

                        txtPretArt.setText("0");

                        pretMod = true;
                        finalPrice = initPrice;



                    } else {

                        nf2.setMinimumFractionDigits(3);
                        nf2.setMaximumFractionDigits(3);

                        varProc = 0;
                        textProcRed.setText("");

                        textProcRed.setSelection(textProcRed.getText().length());
                        textProcRed.requestFocus();
                        textProcRed.selectAll();
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

                    nf2.setMinimumFractionDigits(3);
                    nf2.setMaximumFractionDigits(3);

                    // verif. cantitate

                    if (globalCantArt > 0) {

                        if (!pretMod) // modificare valoare
                        {
                            if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

                                if (varProc != -1) {
                                    varProc = Double.parseDouble(textProcRed.getText().toString());

                                    if (varProc >= 0) {

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
            Double.parseDouble(input);
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

                actionGetPret();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private void actionGetPret() {

        HashMap<String, String> params = new HashMap<String, String>();

        String depSel = "";
        String uLog = UserInfo.getInstance().getUnitLog();



        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
            globalDepozSel = articolModificat.getDepozit();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        depSel = globalCodDepartSelectetItem.substring(0, 2);

        if (CreareComanda.canalDistrib.equals("20") || globalDepozSel.equals("MAV1") || globalDepozSel.equals("MAV2") || globalDepozSel.equals("DSCM")) {
            depSel = "11";
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        }

        String paramUnitMas = textUM.getText().toString();

        if (listUmVanz.size() > 1) {
            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
            paramUnitMas = artMap.get("rowText");

        }

        String filialaPret = CreareComanda.filialaAlternativa;

        if (!filialaStocBV90.isEmpty())
            filialaPret = filialaStocBV90;

        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(CreareComanda.codClientVar);
        paramPret.setArticol(codArticol);
        paramPret.setCantitate(textCant.getText().toString().trim());
        paramPret.setDepart(depSel);
        paramPret.setUm(paramUnitMas);
        paramPret.setUl(uLog);
        paramPret.setDepoz(globalDepozSel);
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib(CreareComanda.canalDistrib);
        paramPret.setTipUser(UserInfo.getInstance().getTipUserSap());
        paramPret.setFilialaAlternativa(filialaPret);
        paramPret.setFilialaClp(DateLivrare.getInstance().getCodFilialaCLP());
        paramPret.setTipTransport(DateLivrare.getInstance().getTransport());

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));

        opArticol.getPretUnic(params);

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
        txtNumeArticol.setText("");
        resultLayout.setVisibility(View.INVISIBLE);
        CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
        setListAdapter(adapterArticole);

    }

    private void showArticoleCantDialog(String listArticoleSer) {
        listArticoleCant = opArticol.deserializeArticoleCant(listArticoleSer);

        if (!listArticoleCant.isEmpty()) {

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

            ArticoleCantDialog articoleCant = new ArticoleCantDialog(this, listArticoleCant);
            articoleCant.setArticoleCantListener(this);
            articoleCant.getWindow().setLayout(width, height);
            articoleCant.show();
        }

    }

    private void showArticoleCantDialog() {
        if (listArticoleCant != null && !listArticoleCant.isEmpty()) {

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

            ArticoleCantDialog articoleCant = new ArticoleCantDialog(this, listArticoleCant);
            articoleCant.setArticoleCantListener(this);
            articoleCant.getWindow().setLayout(width, height);
            articoleCant.show();
        }
    }

    boolean isNotDepartRestricted(String codDepart) {
        if (UserInfo.getInstance().getTipAcces().equals("27"))
            return true;
        else
            return UserInfo.getInstance().getCodDepart().equals(codDepart);
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

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

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
        if (isComandaDL() && DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.DISPOZITIE_LIVRARE) {
            performGetArticoleFurnizor();
        } else if (isComandaDL() && DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA)
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
        String departCautare = DepartamentAgent.getDepartArticole(selectedDepartamentAgent);

        if (tglButton.isChecked())
            tipCautare = "C";
        else
            tipCautare = "N";

        if (tglTipArtBtn.isChecked())
            tipArticol = "S";
        else
            tipArticol = "A";

        if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            tipComanda = "fasonate";

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("searchString", numeArticol);
        params.put("tipArticol", tipArticol);
        params.put("tipCautare", tipCautare);
        params.put("departament", departCautare);
        params.put("filiala", UserInfo.getInstance().getUnitLog());
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

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_COMANDA)
            params.put("aczc", "true");
        else
            params.put("aczc", "false");

        opArticol.getArticoleFurnizor(params);
    }

    @SuppressWarnings("unchecked")
    public void addListenerBtnSaveArt() {
        saveArtBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                try {

                    if (isLivrareCustodie()) {
                        saveArticolCustodie();
                        return;
                    }

                    if (isConditieCabluri05BV90() && listCabluri == null) {
                        getCabluri05(codArticol);
                        return;
                    }

                    String localUnitMas = "";
                    String alteValori = "", subCmp = "0";
                    boolean altDepozit = false;

                    if (textCant.getVisibility() != View.VISIBLE) {
                        return;
                    }

                    if (textProcRed.getText().toString().trim().length() == 0) {
                        if (tglProc.getText().equals(("%")))
                            textProcRed.setText("0");

                    }

                    if (CreareComanda.articoleComanda.equals("")) {
                        depozUnic = globalDepozSel.substring(2, 3);
                        unitLogUnic = CreareComanda.filialaAlternativa;
                    }

                    if (!depozUnic.equals(globalDepozSel.substring(2, 3))) {
                        altDepozit = true;
                    }

                    if (!CreareComanda.depozitUnic.equals("") && !CreareComanda.depozitUnic.equals(globalDepozSel.substring(2, 3))) {
                        altDepozit = true;
                    }

                    if (!unitLogUnic.equals(CreareComanda.filialaAlternativa) && !isUserExceptieFiliale()) {
                        Toast.makeText(getApplicationContext(), "Selectati articole dintr-o singura filiala!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (listUmVanz.size() > 1) {

                        artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                        String currentUnitMas = artMap.get("rowText");

                        if (!currentUnitMas.equals(selectedUnitMasPret)) {
                            Toast.makeText(getApplicationContext(), "Unitatea de masura nu corespunde celei pentru care a fost solicitat pretul.",
                                    Toast.LENGTH_LONG).show();
                            return;

                        }

                    }

                    String cantArticol = textCant.getText().toString().trim();

                    if (selectedCant != Double.parseDouble(cantArticol)) {

                        Toast.makeText(getApplicationContext(), "Pretul nu corespunde cantitatii solicitate!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!isComandaDL()
                            && !CreareComanda.tipComanda.equals("S")
                            && Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) > Double.parseDouble(textStoc.getText()
                            .toString().replaceAll(",", ""))) {
                        Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // verificare umvanz.

                    localUnitMas = textUM.getText().toString().trim();

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

                    // exceptie material transport
                    if (ClientiGenericiGedInfoStrings.isMatTransport(codArticol)) {
                        initPrice = pretVanzare = finalPrice;
                    }

                    if (finalPrice == initPrice) // pretul din sap e pe
                        // cantitate, daca se
                        // modifica devine pe
                        // unitate
                        finalPrice = (finalPrice / globalCantArt) * valMultiplu;

                    valArticol = (finalPrice / valMultiplu) * globalCantArt;

                    if (initPrice != 0) {
                        if (!tglProc.isChecked()) {
                            if (textProcRed.getText().length() > 0)
                                procRedFin = Double.parseDouble(textProcRed.getText().toString());
                            else
                                procRedFin = 0;
                        } else
                            procRedFin = (1 - finalPrice / (initPrice / globalCantArt * valMultiplu)) * 100;
                    }

                    if (procRedFin > 0)// procent_aprob se calculeaza doar daca
                    // exista proc. de reducere dat de ag.
                    {
                        procentAprob = (1 - finalPrice / (pretVanzare / globalCantArt * valMultiplu)) * 100;
                    }

                    // sf. verificare

                    if (finalPrice != 0) {

                        tipAlert = " ";

                        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
                            if (procentAprob > discMaxAV) {
                                tipAlert = "SD";
                            }
                        }

                        // pentru KA este nevoie de aprobarea SD-ului in cazul
                        // in care
                        // cantitatea comandata este mai mare decat jumatate din
                        // stocul disponibil
                        if (UserInfo.getInstance().getTipAcces().equals("27")) {
                            if (Double.parseDouble(cantArticol) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", "")) / 2) {

                                if (!globalCodDepartSelectetItem.equals("11"))
                                    tipAlert = "SD";
                            }
                        }

                        if (UserInfo.getInstance().getTipAcces().equals("27")) {// KA
                            if (procentAprob > 0 && finalPrice < minimKAPrice) {

                                if (!tipAlert.equals(""))
                                    tipAlert += ";" + "DV";
                                else
                                    tipAlert = "DV";
                            }

                        } else {// agenti
                            if (procentAprob > discMaxSD) {
                                tipAlert = "DV";
                            }
                        }

                        if ((UtilsUser.isASDL() || UtilsUser.isOIVPD())
                                && Math.round(procentAprob * 1000.0) / 1000.0 <= Math.round(discountASDL * 1000.0) / 1000.0) {
                            tipAlert = " ";
                        }

                        double procRedFact = 0; // factura de reducere
                        if (listPrice != 0)
                            procRedFact = (initPrice / globalCantArt * valMultiplu - finalPrice) / (listPrice / globalCantArt * valMultiplu) * 100;

                        alteValori = String.valueOf(valArticol) + "!" + String.valueOf(listPrice) + "!" + String.valueOf(initPrice) + "!"
                                + String.valueOf(discMaxAV) + "!" + String.valueOf(discMaxSD) + "!" + codPromo + "!" + subCmp;

                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMinimumFractionDigits(2);
                        nf.setMaximumFractionDigits(2);

                        if (articolModificat != null)
                            codArticol = articolModificat.getCodArticol();

                        if (codArticol.length() == 18)
                            codArticol = codArticol.substring(10, 18);

                        ArticolComanda unArticol = new ArticolComanda();
                        unArticol.setNumeArticol(numeArticol);
                        unArticol.setCodArticol(codArticol);
                        unArticol.setCantitate(Double.valueOf(cantArticol));
                        unArticol.setDepozit(globalDepozSel);
                        unArticol.setPretUnit(finalPrice);

                        unArticol.setProcent(Double.valueOf(procRedFin));
                        unArticol.setUm(localUnitMas);
                        unArticol.setProcentFact(Double.valueOf(procRedFact));
                        unArticol.setConditie(false);
                        unArticol.setDiscClient(procDiscClient);
                        unArticol.setProcAprob(procentAprob);
                        unArticol.setMultiplu(valMultiplu);
                        unArticol.setPret(valArticol);
                        unArticol.setMoneda("RON");
                        unArticol.setInfoArticol(infoArticol);
                        unArticol.setCantUmb(Double.valueOf(cantUmb));
                        unArticol.setUmb(Umb);
                        unArticol.setAlteValori(alteValori);
                        unArticol.setDepart(globalCodDepartSelectetItem);
                        unArticol.setTipArt(tipArticol);
                        unArticol.setPromotie(Integer.parseInt(codPromo));
                        unArticol.setObservatii(tipAlert);
                        unArticol.setDepartAprob(articolDBSelected.getDepartAprob());
                        unArticol.setUmPalet(articolDBSelected.isUmPalet());
                        unArticol.setCategorie(articolDBSelected.getCategorie());
                        unArticol.setLungime(articolDBSelected.getLungime());
                        unArticol.setCmp(cmpArt);

                        if (!filialaStocBV90.isEmpty())
                            unArticol.setFilialaSite(filialaStocBV90);
                        else if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
                            unArticol.setFilialaSite(DateLivrare.getInstance().getCodFilialaFasonate());
                        else if (HelperMathaus.isComandaVanzareTCLI())
                            unArticol.setFilialaSite(DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog());
                        else
                            unArticol.setFilialaSite(CreareComanda.filialaAlternativa);

                        unArticol.setDataExpPret(dataExpPret);
                        unArticol.setArticolMathaus(articolMathaus);
                        unArticol.setListCabluri(listCabluri);
                        unArticol.setGreutate(greutateArt);
                        unArticol.setTipMarfa(tipMarfa);
                        unArticol.setGreutateBruta(greutateBruta);
                        unArticol.setLungimeArt(lungimeArt);
                        unArticol.setCantitateInit(Double.valueOf(cantArticol));

                        unArticol.setCantitate50(Double.valueOf(cantitate50));
                        unArticol.setUm50(um50);

                        unArticol.setSintetic(articolDBSelected.getSintetic());
                        unArticol.setPretMinim(selectedArticol.getPretMinim());

                        if (HelperMathaus.isComandaVanzareTCLI()) {

                            if (!UtilsComenzi.isDepozitUnitLog(unArticol.getDepozit(), DateLivrare.getInstance().getFilialaLivrareTCLI().getDepozite())) {
                                Toast.makeText(getApplicationContext(), "Nu puteti folosi acest depozit pentru " + DateLivrare.getInstance().getFilialaLivrareTCLI().getNumeFiliala() + ".", Toast.LENGTH_LONG).show();
                                return;
                            }

                            listStocTCLI = HelperMathaus.getStocTCLIDepozit(textStoc.getText().toString(), unArticol.getDepozit(), unArticol.getUm());
                        }

                        unArticol.setListStocTCLI(listStocTCLI);

                        if (procRedFin > 0)
                            unArticol.setIstoricPret(istoricPret);

                        ListaArticoleComanda listaComanda = ListaArticoleComanda.getInstance();
                        listaComanda.addArticolComanda(unArticol);

                        showArticoleCantDialog();

                        if (UtilsArticole.isArticolPal(articolDBSelected.getSintetic()) && !ScreenUtils.isBundleArticolModificat(bundle))
                            afiseazaArticoleCant(codArticol, CreareComanda.filialaAlternativa);

                        textNumeArticol.setText("");
                        textCodArticol.setText("");
                        textUM.setText("");
                        textStoc.setText("");
                        textCant.setText("");
                        textPromo.setText("");
                        txtNumeArticol.setText("");

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
                        minimKAPrice = 0;
                        cmpArt = 0;
                        subCmp = "0";

                        valoareUmrez = 1;
                        valoareUmren = 1;

                        greutateArt = 0;
                        tipMarfa = "";
                        greutateBruta = 0;
                        lungimeArt = "";


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

                    } else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Articolul nu are pret definit!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    textProcRed.setText("");

                } catch (Exception e) {

                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (ScreenUtils.isBundleArticolModificat(bundle))
                    finish();

            }


        });

    }

    private boolean isConditieCabluri05BV90() {
        return articolDBSelected.getDepart().equals("05") && CreareComanda.filialaAlternativa.equals("BV90");
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

        Cabluri05Dialog cabluriDialog = new Cabluri05Dialog(SelectArtCmd.this, listCabluri, textCant.getText().toString().trim());
        cabluriDialog.getWindow().setLayout(width, height);
        cabluriDialog.setCabluSelectedListener(this);
        cabluriDialog.show();

    }

    private void afiseazaArticoleCant(String codArticol, String filiala) {

        if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE))
            return;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unitLog", filiala);
        params.put("codArtPal", codArticol);

        opArticol.getArticoleCant(params);

    }

    private boolean isUserExceptieFiliale() {
        return globalCodDepartSelectetItem.equals("01") || globalCodDepartSelectetItem.equals("02");
    }

    private void getPretArtCustodie() {

        HashMap<String, String> params = new HashMap<String, String>();

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String paramUnitMas = textUM.getText().toString();
        String uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);

        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(CreareComanda.codClientVar);
        paramPret.setArticol(codArticol);

        paramPret.setCantitate(textCant.getText().toString().trim());
        paramPret.setDepart(globalCodDepartSelectetItem.substring(0, 2));
        paramPret.setUm(paramUnitMas);
        paramPret.setUl(uLog);
        paramPret.setDepoz("");
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib("20");
        paramPret.setTipUser(UserInfo.getInstance().getTipUser());
        paramPret.setMetodaPlata(DateLivrare.getInstance().getTipPlata());
        paramPret.setTermenPlata(DateLivrare.getInstance().getTermenPlata());
        paramPret.setCodJudet(DateLivrare.getInstance().getCodJudet());
        paramPret.setLocalitate(DateLivrare.getInstance().getOras());
        paramPret.setFilialaAlternativa(CreareComanda.filialaAlternativa);
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

        ArticolComanda unArticol = new ArticolComanda();
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
        unArticol.setFilialaSite(CreareComanda.filialaAlternativa);
        unArticol.setTipMarfa(pretArticol.getTipMarfa());
        unArticol.setGreutateBruta(pretArticol.getGreutateBruta());
        unArticol.setLungimeArt(pretArticol.getLungimeArt());

        ListaArticoleComanda listaComanda = ListaArticoleComanda.getInstance();
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
        return DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE;
    }

    private boolean isComandaDL() {
        return DateLivrare.getInstance().getFurnizorComanda() != null && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa() != null;
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

    @SuppressWarnings("unchecked")
    private void listArtStoc(String pretResponse) {

        resultLayout.setVisibility(View.VISIBLE);
        listStocTCLI = new ArrayList<>();

        if (!pretResponse.equals("-1")) {

            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);

            String[] tokenStoc = pretResponse.split("#");

            textNumeArticol.setVisibility(View.VISIBLE);
            textCodArticol.setVisibility(View.VISIBLE);
            textUM.setVisibility(View.VISIBLE);
            textStoc.setVisibility(View.VISIBLE);
            textCant.setVisibility(View.VISIBLE);
            labelCant.setVisibility(View.VISIBLE);
            labelStoc.setVisibility(View.VISIBLE);
            pretBtn.setVisibility(View.VISIBLE);

            textUM.setText(tokenStoc[1]);

            // verificare daca se afiseaza valoarea stocului (exceptia de la
            // BV90 pentr ag/sd)
            if (tokenStoc[2].equals("0")) {
                textStoc.setVisibility(View.INVISIBLE);
            } else {
                textStoc.setVisibility(View.VISIBLE);
            }

            textStoc.setText(nf2.format(Double.valueOf(tokenStoc[0])));

            // pentru KA se afiseaza stocul disponibil = stoc real / 2
            if (UserInfo.getInstance().getTipAcces().equals("27")) {
                layoutStocKA.setVisibility(View.VISIBLE);
                textUmKA.setText(tokenStoc[1]);
                textStocKA.setText(nf2.format(Double.valueOf(tokenStoc[0]) / 2));
            }

            if (articolModificat != null && articolModificat.getArticolMathaus() != null)
                articolMathaus = articolModificat.getArticolMathaus();

            if (isDepartMathaus) {
                textNumeArticol.setText(articolMathaus.getNume());
                textCodArticol.setText(articolMathaus.getCod());
            }

            umStoc = tokenStoc[1];

            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();

            if (artMap != null) {


                String stocUM = artMap.get("rowText");

                if (!stocUM.equals(tokenStoc[1]) && !tokenStoc[1].trim().equals("")) // um
                // vanz
                // si
                // um
                // vanz
                // difera
                {
                    // listUmVanz.clear();
                    HashMap<String, String> tempUmVanz;
                    tempUmVanz = new HashMap<String, String>();
                    tempUmVanz.put("rowText", tokenStoc[1]);

                    listUmVanz.add(tempUmVanz);
                    spinnerUnitMas.setAdapter(adapterUmVanz);
                    spinnerUnitMas.setVisibility(View.VISIBLE);
                }
            }

            if (isCondArtStocBV90() && tokenStoc[0].equals("0") && !verificatStocBV90) {
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

    private void listArtStocCustodie(String stocResponse) {
        if (!stocResponse.equals("-1")) {

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
            greutateBruta = Double.parseDouble(tokenStoc[4].trim());
            lungimeArt = tokenStoc[5];
            tipMarfa = tokenStoc[6];

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
        return Double.parseDouble(pretArticol.getCantitate50()) > 0
                && selectedCant != Double.parseDouble(pretArticol.getCantitate());
    }

    private void listPretArticol(PretArticolGed pretArticol) {

        selectedArticol = pretArticol;

        valMultiplu = Double.parseDouble(pretArticol.getMultiplu());
        greutateArt = pretArticol.getGreutate();
        tipMarfa = pretArticol.getTipMarfa();
        greutateBruta = pretArticol.getGreutateBruta();
        lungimeArt = pretArticol.getLungimeArt();

        cantitate50 = pretArticol.getCantitate50();
        um50 = pretArticol.getUm50();

        if (isConditiiModifCant50(pretArticol)) {
            textCant.setText(pretArticol.getCantitate());
            showModifCantInfo(pretArticol);
            selectedCant = Double.parseDouble(textCant.getText().toString().trim());
        }

        if (Double.parseDouble(pretArticol.getCantitate50()) == 0)
            cantitate50 = pretArticol.getCantitate();

        globalCantArt = Double.valueOf(pretArticol.getCantitateUmBaza());

        cantUmb = pretArticol.getCantitateUmBaza();
        Umb = pretArticol.getUmBaza();

        cmpArt = Double.valueOf(pretArticol.getCmp());

        saveArtBtn.setVisibility(View.VISIBLE);

        textPromo.setText("");
        textPromo.setVisibility(View.INVISIBLE);

        if (pretArticol.isPromo()) {
            textPromo.setVisibility(View.VISIBLE);
            textPromo.setText("Articol cu promotie.");
        }


        nf2.setMinimumFractionDigits(3);
        nf2.setMaximumFractionDigits(3);

        codPromo = "-1";

        txtPretArt.setVisibility(View.VISIBLE);

        initPrice = Double.valueOf(pretArticol.getPret());
        listPrice = Double.valueOf(pretArticol.getPretLista());

        txtImpachetare.setText(pretArticol.getImpachetare());


        if (pretArticol.getIstoricPret().trim().isEmpty())
            findViewById(R.id.textIstoricPret).setVisibility(View.GONE);
        else {
            findViewById(R.id.textIstoricPret).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textIstoricPret)).setText(HelperPreturi.getIstoricPret(pretArticol.getIstoricPret()));
        }

        istoricPret = UtilsFormatting.getIstoricPret(pretArticol.getIstoricPret(), EnumTipComanda.DISTRIBUTIE);

        procReducereCmp = pretArticol.getProcReducereCmp();


        dataExpPret = pretArticol.getDataExp();

        procDiscClient = 0;

        if (!pretArticol.getErrMsg().isEmpty()) {
            Toast.makeText(getApplicationContext(), pretArticol.getErrMsg(), Toast.LENGTH_LONG).show();
            saveArtBtn.setVisibility(View.INVISIBLE);
            return;
        }

        if (globalDepozSel.substring(2, 3).equals("V")) {

            if (initPrice / globalCantArt * valMultiplu < cmpArt) {
                Toast.makeText(getApplicationContext(), "Pret sub cmp.", Toast.LENGTH_LONG).show();
            }
        }

        finalPrice = initPrice;

        textProcRed.setText("");

        redBtnTable.setVisibility(View.VISIBLE);
        textProcRed.setVisibility(View.VISIBLE);
        textMultipluArt.setVisibility(View.VISIBLE);

        if (ClientiGenericiGedInfoStrings.isMatTransport(codArticol)) {
            txtPretArt.setVisibility(View.INVISIBLE);
        } else {
            txtPretArt.setVisibility(View.VISIBLE);
        }

        textMultipluArt.setText("Unit.pret: " + String.valueOf(valMultiplu) + " " + umStoc);

        txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
        txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));


        discMaxAV = 0;
        discMaxSD = 0;


        infoArticol = pretArticol.getConditiiPret().replace(',', '.');
        pretVanzare = listPrice; // calcul procent aprobare
        textCondPret.setVisibility(View.VISIBLE);
        textCondPret.setText(HelperPreturi.getInfoPret(pretArticol, nf2));



        // calcul doar pentru AG
        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
            if (listPrice > 0)
                procDiscClient = 100 - (initPrice / listPrice) * 100;
        }

        // pret in um alternativa
        afisPretUmAlternativa();

        txtPretArt.setEnabled(true);
        ScreenUtils.enableEditText(textProcRed);
        tglProc.setEnabled(true);


        // pentru totaluri negociate nu se modifica preturi
        if (CreareComanda.isTotalNegociat) {
            textProcRed.setFocusable(false);
            tglProc.setEnabled(false);
        }

        // se afiseaza direct pretul si nu procentul
        tglProc.setChecked(false);
        tglProc.performClick();

        if (articolModificat != null) {
            textProcRed.setText(nf2.format(articolModificat.getPretUnit()));
            codArticol = articolModificat.getCodArticol();
            isArticolModificatCantPret = false;
            articolModificat = null;
        }

        if (UtilsUser.isASDL() || UtilsUser.isOIVPD()) {

            String rawIstoricPret = getPretIstoric(pretArticol.getIstoricPret());

            double istoricPretAsdl = Double.parseDouble(rawIstoricPret.split("#")[0]);
            double valPret = listPrice / globalCantArt * valMultiplu;
            String umIstoric = rawIstoricPret.split("#")[1];
            String umVanzASDL = umIstoric;

            if (spinnerUnitMas.getVisibility() == View.VISIBLE) {
                HashMap<String, String> unitMasVanz = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                umVanzASDL = unitMasVanz.get("rowText");
            }

            if (istoricPretAsdl > 0 && umIstoric.equals(umVanzASDL)) {
                valPret = istoricPretAsdl;
            } else {
                valPret = valPret - valPret * (discMaxSD / 100);
            }

            textProcRed.setText(nf2.format(valPret));

            discountASDL = Double.parseDouble(txtPretArt.getText().toString());
        }


        if (hasArticolDiscount(pretArticol) || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED()) {
            txtPretArt.setEnabled(false);
            tglProc.setEnabled(false);
            ScreenUtils.disableEditText(textProcRed);
            codPromo = "1";

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


        if (!codPromo.equals("1")) {
            textProcRed.requestFocus();
        }


    }

    private boolean hasArticolDiscount(PretArticolGed pretArticol) {
        return pretArticol.getFaraDiscount().toUpperCase().equals("X");

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

    private String getPretIstoric(String infoIstoric) {

        String pretIstoric = "0";
        String umIstoric = "-1";

        DecimalFormat df = new DecimalFormat("#0.00");

        if (infoIstoric.contains(":")) {
            String[] arrayIstoric = infoIstoric.split(":");

            String[] arrayPret = arrayIstoric[0].split("@");

            pretIstoric = df.format(Double.valueOf(arrayPret[0]));
            umIstoric = arrayPret[2].split(" ")[1].trim();
        }

        return pretIstoric + "#" + umIstoric;

    }


    protected void onListItemClick(ListView l, View v, int position, long id) {

        ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

        articolDBSelected = articol;

        numeArticol = articol.getNume();
        codArticol = articol.getCod();
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

        String umVanz = articol.getUmVanz();
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
            String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

            if (tokenDep[0].trim().length() == 2)
                globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
            else
                globalDepozSel = tokenDep[0].trim();

            if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE) {
                performListArtStocCustodie();
            } else {

                if (isDepartMathaus)
                    btnStocMathaus.performClick();
                else
                    performListArtStoc();

            }

        } catch (Exception ex) {
            Log.e("Error", ex.toString());

        }

    }

    private void performListArtStocCustodie() {

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArticol", codArticol);
        params.put("codClient", CreareComanda.codClientVar);
        params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));

        opArticol.getStocCustodie(params);
    }


    private void getStocDeteriorate() {

        if (this.getListView().getAdapter() != null && this.getListView().getAdapter().getCount() == 0 && !isDepartMathaus)
            return;

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String varLocalUnitLog;

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE) {
            if (globalDepozSel.equals("MAV1"))
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP().substring(0, 2) + "2"
                        + DateLivrare.getInstance().getCodFilialaCLP().substring(3, 4);
            else
                varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
        } else {
            if (globalDepozSel.equals("MAV1") || globalDepozSel.equals("DSCM")) {
                if (CreareComanda.filialaAlternativa.equals("BV90"))
                    varLocalUnitLog = "BV92";
                else
                    varLocalUnitLog = CreareComanda.filialaAlternativa.substring(0, 2) + "2" + CreareComanda.filialaAlternativa.substring(3, 4);
            } else {
                varLocalUnitLog = CreareComanda.filialaAlternativa;
            }
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codArt", codArticol);
        params.put("filiala", varLocalUnitLog);
        params.put("showCmp", "0");
        params.put("depart", UserInfo.getInstance().getCodDepart());
        opArticol.getStocDisponibil(params);


    }

    private void performGetStocSap() {

        HashMap<String, String> params = new HashMap<String, String>();

        if (articolModificat != null) {
            codArticol = articolModificat.getCodArticol();
        }

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String varLocalUnitLog;

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE) {
            varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
        } else
            varLocalUnitLog = CreareComanda.filialaAlternativa;

        if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            varLocalUnitLog = DateLivrare.getInstance().getCodFilialaFasonate();

        params.put("codArt", codArticol);
        params.put("filiala", varLocalUnitLog);
        params.put("um", articolMathaus.getUmVanz());
        params.put("tipUser", UserInfo.getInstance().getTipUserSap());

        opArticol.getStocSap(params);
    }

    private void performListArtStoc() {
        try {

            if (this.getListView().getAdapter() != null && this.getListView().getAdapter().getCount() == 0 && !isDepartMathaus)
                return;

            HashMap<String, String> params = new HashMap<String, String>();

            if (articolModificat != null) {
                codArticol = articolModificat.getCodArticol();
            }

            if (codArticol.length() == 8)
                codArticol = "0000000000" + codArticol;


            String varLocalUnitLog;

            if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE) {
                if (globalDepozSel.equals("MAV1"))
                    varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP().substring(0, 2) + "2"
                            + DateLivrare.getInstance().getCodFilialaCLP().substring(3, 4);
                else
                    varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
            } else {
                if (globalDepozSel.equals("MAV1") || globalDepozSel.equals("DSCM")) {
                    if (CreareComanda.filialaAlternativa.equals("BV90"))
                        varLocalUnitLog = "BV92";
                    else
                        varLocalUnitLog = CreareComanda.filialaAlternativa.substring(0, 2) + "2" + CreareComanda.filialaAlternativa.substring(3, 4);
                } else {
                    varLocalUnitLog = CreareComanda.filialaAlternativa;
                }
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

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isCondArtStocBV90() {

        return articolMathaus != null && !DateLivrare.getInstance().getTransport().equals("TCLI") && DateLivrare.getInstance().getTipComandaDistrib() != TipCmdDistrib.ARTICOLE_DETERIORATE &&
                (articolMathaus.getDepart().equals(("01")) || articolMathaus.getDepart().equals(("02")) || articolMathaus.getDepart().equals("05)"));

    }

    private String addSpace(int nrCars) {
        String retVal = "";

        for (int i = 0; i < nrCars; i++)
            retVal += " ";

        return retVal;
    }

    private void loadFactorConversie(String result) {
        String[] convResult = result.split("#");

        valoareUmrez = Integer.parseInt(convResult[0]);
        valoareUmren = Integer.parseInt(convResult[1]);

    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    public void operationComplete(EnumArticoleDAO methodName, Object result) {

        switch (methodName) {
            case GET_STOC_SAP:
            case GET_STOC_DEPOZIT:
                listArtStoc((String) result);
                break;
            case GET_PRET_UNIC:
                listPretArticol(opArticol.deserializePretGed(result));
                break;
            case GET_ARTICOLE_STATISTIC:
                ((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);
                listArticoleStatistic = opArticol.deserializeArticoleVanzare((String) result);
                populateListViewArticol(listArticoleStatistic);
                break;

            case GET_ARTICOLE_CUSTODIE:
                ((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);

                TextView labelCustodie = ((TextView) findViewById(R.id.textAfisStatistic));
                labelCustodie.setText("Articole custodie");
                labelCustodie.setVisibility(View.VISIBLE);

                listArticoleCustodie = opArticol.deserializeArticoleVanzare((String) result);
                populateListViewArticol(listArticoleCustodie);
                break;

            case GET_ARTICOLE_DISTRIBUTIE:
            case GET_ARTICOLE_FURNIZOR:
            case GET_ARTICOLE_ACZC:
                populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
                break;
            case GET_STOC_CUSTODIE:
                listArtStocCustodie((String) result);
                break;
            case GET_FACTOR_CONVERSIE:
                loadFactorConversie((String) result);
                break;
            case GET_ARTICOLE_CANT:
                showArticoleCantDialog((String) result);
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
            case GET_PRET_UNIC_CUSTODIE:
                listPretArticolCustodie(opArticol.deserializePretGed(result));
                break;
            default:
                break;

        }

    }

    @Override
    public void articolCantSelected(ArticolCant articolCant) {

        List<ArticolDB> listArticole = new ArrayList<ArticolDB>();
        articolCant.setCod(articolCant.getCod().replaceFirst("^0*", ""));
        listArticole.add(articolCant);

        if (isDepartMathaus) {
            articolMathaus = new ArticolMathaus();
            articolMathaus.setCod(articolCant.getCod());
            articolMathaus.setNume(articolCant.getNume());
            articolMathaus.setDepart(articolCant.getDepart());
            articolMathaus.setUmVanz(articolCant.getUmVanz());
            articolMathaus.setUmVanz10(articolCant.getUmVanz10());
            articolMathaus.setUmPalet(articolCant.isUmPalet());
            articolMathaus.setSintetic(articolCant.getSintetic());
            articolMathaus.setNivel1((articolCant.getNivel1()));
        } else {
            CreareComanda.filialaAlternativa = articolCant.getUlStoc();

            if (articolCant.getDepozit().equals("92V1"))
                spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("92V1"));
            else
                spinnerDepoz.setSelection(0);
        }

        txtNumeArticol.setText("");
        resultLayout.setVisibility(View.INVISIBLE);
        CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, listArticole);
        setListAdapter(adapterArticole);

        this.getListView().performItemClick(this.getListView().getAdapter().getView(0, null, null), 0, this.getListView().getItemIdAtPosition(0));


    }

    @Override
    public void articolCantClosed() {
        listArticoleCant.clear();

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
