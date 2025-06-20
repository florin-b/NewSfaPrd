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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.dialogs.Cabluri05Dialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.helpers.HelperComenzi;
import my.logon.screen.helpers.HelperPreturi;
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleModificareComanda;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectArtModificareCmd extends ListActivity implements OperatiiArticolListener, Cablu05SelectedListener {

    Button articoleBtn, saveArtBtn, pretBtn;
    String filiala = "", nume = "", cod = "", umStoc = "";
    String articolResponse = "";
    String pretResponse = "";
    String codArticol = "";
    String numeArticol = "";
    String depart = "";
    String codClientVar = "";
    String numeClientVar = "";
    LinearLayout redBtnTable, layoutStocKA;
    EditText valRedIntText, valRedDecText;
    public String globalDepozSel = "", artPromoText = "", cantUmb = "", Umb = "", selectedUnitMas = "", globalCodDepartSelectetItem = "";
    private String cantitate50 = "", um50 = "";

    ToggleButton tglButton, tglProc, tglTipArtBtn;

    private EditText txtNumeArticol, textProcRed;
    private TextView textCodArticol, txtPretArt;
    private TextView textNumeArticol, textStocKA, textUmKA;

    private PretArticolGed selectedArticol;

    private TextView textStoc;
    private TextView textCant;

    private TextView textUM, textMultipluArt;
    private TextView labelCant, labelStoc;
    private Spinner spinnerDepoz, spinnerUnitMas;

    private TextView textPromo, textCondPret;
    private boolean pretMod = false;
    private double initPrice = 0, cmpArt = 0;
    private double finalPrice = 0, minimKAPrice = 0, greutateArt = 0;
    private double listPrice = 0, procDiscClient = 0;
    private double discMaxAV = 0, discMaxSD = 0; // , discMaxDV = 0;

    private static ArrayList<HashMap<String, String>> listArticole = null, listUmVanz = null;
    public SimpleAdapter adapterUmVanz;

    String tipAlert = "", codPromo = "", infoArticol = "", tipArticol = "";
    double procR = 0;
    private double pretVanzare = 0, procentAprob = 0, valMultiplu = 0, varProc = 0, selectedCant = 0, globalCantArt = 0;
    private NumberFormat nf2;
    private HashMap<String, String> artMap = null;
    private String depozUnic = "";
    private Dialog dialogSelFilArt;

    LinearLayout resultLayout;
    OperatiiArticol opArticol;
    String selectedDepartamentAgent;
    private ArrayAdapter<String> adapterSpinnerDepozite;

    private ArticolDB articolDBSelected;
    private TextView txtImpachetare;
    private String istoricPret;
    private String dataExpPret;
    private List<BeanCablu05> listCabluri;

    private String tipMarfa = "";
    private double greutateBruta = 0;
    private String lungimeArt = "";
    private HashMap<String, String> paramsGetStocDepozit;
    private ArticolComanda articolComandaModif;
    private NumberFormat nfStoc;
    private double stocComandaModif;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.selectartcmdheader);

        opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
        opArticol.setListener(this);

        nfStoc = NumberFormat.getInstance(new Locale("en", "US"));
        nfStoc.setMinimumFractionDigits(3);
        nfStoc.setMaximumFractionDigits(3);

        initSelectionDepartament();

        if (!isCV())
            addSpinnerDepartamente();

        resultLayout = (LinearLayout) findViewById(R.id.resLayout);
        resultLayout.setVisibility(View.INVISIBLE);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Articole comanda");
        actionBar.setDisplayHomeAsUpEnabled(true);

        listArticole = new ArrayList<HashMap<String, String>>();

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

        nf2 = NumberFormat.getInstance();

        this.tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
        addListenerTglTipArtBtn();

        this.redBtnTable = (LinearLayout) findViewById(R.id.RedBtnTable);

        txtPretArt = (TextView) findViewById(R.id.txtPretArt);

        this.pretBtn = (Button) findViewById(R.id.pretBtn);
        addListenerPretBtn();

        this.tglProc = (ToggleButton) findViewById(R.id.tglProc);
        addListenerTglProc();


        textProcRed = (EditText) findViewById(R.id.textProcRed);
        textProcRed.setFocusableInTouchMode(true);
        addListenerProcArt();

        txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
        textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
        textCodArticol = (TextView) findViewById(R.id.textCodArticol);
        textUM = (TextView) findViewById(R.id.textUm);
        textStoc = (TextView) findViewById(R.id.textStoc);
        textCant = (EditText) findViewById(R.id.txtCantArt);
        labelCant = (TextView) findViewById(R.id.labelCant);
        labelStoc = (TextView) findViewById(R.id.labelStoc);
        txtImpachetare = (TextView) findViewById(R.id.txtImpachetare);
        textCondPret = (TextView) findViewById(R.id.textCondPret);

        textPromo = (TextView) findViewById(R.id.textPromo);

        txtNumeArticol.setHint("Introduceti cod articol");
        textMultipluArt = (TextView) findViewById(R.id.txtMultipluArt);

        ModificareComanda.articoleComanda = "";

        spinnerDepoz = (Spinner) findViewById(R.id.spinnerDepoz);

        ArrayList<String> arrayListDepozite = new ArrayList<String>();
        arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteDistributie()));
        adapterSpinnerDepozite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListDepozite);
        adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepoz.setAdapter(adapterSpinnerDepozite);
        spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

        spinnerUnitMas = (Spinner) findViewById(R.id.spinnerUnitMas);

        listUmVanz = new ArrayList<HashMap<String, String>>();
        adapterUmVanz = new SimpleAdapter(this, listUmVanz, R.layout.simplerowlayout, new String[]{"rowText"}, new int[]{R.id.textRowName});
        spinnerUnitMas.setVisibility(View.GONE);

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

    }

    private void addSpinnerDepartamente() {

        List<String> departamenteComanda = DepartamentAgent.getDepartamenteAgent();

        if (isOriceComandaModificata())
            departamenteComanda.remove("Catalog site");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
                departamenteComanda);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
        final Spinner spinnerView = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

        spinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerView.getSelectedItem().toString());
                populateListViewArt(new ArrayList<ArticolDB>());

                if (selectedDepartamentAgent.equals("11") || selectedDepartamentAgent.equals("05")) {
                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());

                    if (selectedDepartamentAgent.equals("11"))
                        spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
                    else
                        spinnerDepoz.setSelection(0);
                } else {
                    adapterSpinnerDepozite.clear();
                    adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteDistributie());
                    spinnerDepoz.setSelection(0);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerView.setAdapter(adapter);
        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

    }

    private void initSelectionDepartament() {

        selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

        if (isCV())
            selectedDepartamentAgent = "";

        if (isKA())
            selectedDepartamentAgent = "00";
    }

    private void CreateMenu(Menu menu) {

        // pentru ag si sd de la 02 si 05 se ofera accesul la BV90
        if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")) {
            if (UserInfo.getInstance().getCodDepart().equals("02") || UserInfo.getInstance().getCodDepart().equals("05")) {

                if (DateLivrare.getInstance().getCodFilialaCLP().length() == 4)
                    return;
                else {
                    MenuItem mnu1 = menu.add(0, 0, 0, "Filiala");
                    {

                        mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

                    }
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    private void getFactoriConversieModifCmd(String codArticolModif, String umModif) {

        HashMap<String, String> params = new HashMap<String, String>();

        if (codArticolModif.length() == 8)
            codArticolModif = "0000000000" + codArticolModif;

        params.put("codArt", codArticolModif);
        params.put("unitMas", umModif);

        opArticol.getFactorConversieModifCmd(params);

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

    // eveniment selectie depozit
    public class OnSelectDepozit implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            if (codArticol.length() > 0) {
                String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

                if (tokenDep[0].trim().length() == 2)
                    globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
                else
                    globalDepozSel = tokenDep[0].trim();

                performListArtStoc();
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // TODO
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0:
                showSelectFilArtDialogBox();
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    boolean isKA() {
        return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
    }

    boolean isCV() {
        return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
    }

    public void showSelectFilArtDialogBox() {
        dialogSelFilArt = new Dialog(SelectArtModificareCmd.this);
        dialogSelFilArt.setContentView(R.layout.selectfilartdialogbox);
        dialogSelFilArt.setTitle("Filiala selectata");
        dialogSelFilArt.setCancelable(true);
        dialogSelFilArt.show();

        final RadioButton radioFilAg = (RadioButton) dialogSelFilArt.findViewById(R.id.radio1);
        radioFilAg.setText(UserInfo.getInstance().getUnitLog());

        radioFilAg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1) {

                }

            }
        });

        final RadioButton radioFilBV90 = (RadioButton) dialogSelFilArt.findViewById(R.id.radio2);

        radioFilBV90.setText("BV90");

        radioFilBV90.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1) {

                }

            }
        });

        if (ModificareComanda.filialaAlternativaM.equals(UserInfo.getInstance().getUnitLog()))
            radioFilAg.setChecked(true);
        else
            radioFilBV90.setChecked(true);

        radioFilAg.setEnabled(false);
        radioFilBV90.setEnabled(false);

        Button btnOkFilArt = (Button) dialogSelFilArt.findViewById(R.id.btnOkSelFilArt);

        btnOkFilArt.setVisibility(View.INVISIBLE);

    }


    public void addListenerTglProc() {
        tglProc.setOnClickListener(new View.OnClickListener() {
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
                // TODO

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
            Log.e("Error", ex.toString());
            return false;
        }
    }

    public void addListenerPretBtn() {
        pretBtn.setOnClickListener(new View.OnClickListener() {
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

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @SuppressWarnings("unchecked")
    protected void performGetPret() {

        try {

            selectedUnitMas = "";
            listCabluri = null;
            if (listUmVanz.size() > 1) {
                artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                selectedUnitMas = artMap.get("rowText");
            }

            selectedCant = Double.parseDouble(textCant.getText().toString().trim());

            if (selectedCant > 0) {

                actionGetPret();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private void actionGetPret() {
        String depSel = "";
        String uLog = UserInfo.getInstance().getUnitLog();


        String localCanalDistrib = ModificareComanda.canalDistributie;

        if (ModificareComanda.filialaAlternativaM.toUpperCase().contains("BV9") && globalDepozSel.equals("MAV1"))
            localCanalDistrib = "10";

        if (UtilsUser.isUserCVOB())
            localCanalDistrib = "60";

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        depSel = globalCodDepartSelectetItem.substring(0, 2);

        if (!ModificareComanda.isComandaDistrib || globalDepozSel.equals("MAV1")) {
            depSel = "11";
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        }


        String paramUnitMas = textUM.getText().toString();

        if (listUmVanz.size() > 1) {
            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
            paramUnitMas = artMap.get("rowText");

        }

        HashMap<String, String> params = new HashMap<String, String>();

        BeanParametruPretGed paramPret = new BeanParametruPretGed();
        paramPret.setClient(ModificareComanda.codClientVar);
        paramPret.setArticol(codArticol);
        paramPret.setCantitate(textCant.getText().toString().trim());
        paramPret.setDepart(depSel);
        paramPret.setUm(paramUnitMas);
        paramPret.setUl(uLog);
        paramPret.setDepoz(globalDepozSel);
        paramPret.setCodUser(UserInfo.getInstance().getCod());
        paramPret.setCanalDistrib(localCanalDistrib);
        paramPret.setTipUser(UserInfo.getInstance().getTipUserSap());
        paramPret.setFilialaAlternativa(ModificareComanda.filialaAlternativaM);
        paramPret.setTipTransport(DateLivrare.getInstance().getTransport());

        params.put("parametruPret", opArticol.serializeParamPretGed(paramPret));

        opArticol.getPretUnic(params);


    }

    public void addListenerToggle() {
        tglButton.setOnClickListener(v -> {
            if (tglButton.isChecked()) {
                txtNumeArticol.setHint("Introduceti cod articol");
            } else {
                txtNumeArticol.setHint("Introduceti nume articol");
            }
        });

    }

    public void addListenerTglTipArtBtn() {
        tglTipArtBtn.setOnClickListener(new View.OnClickListener() {
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

    public void addListenerBtnArticole() {
        articoleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (txtNumeArticol.getText().toString().length() > 0) {

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

        try {

            String numeArticol = txtNumeArticol.getText().toString().trim();
            String tipCautare = "", tipArticol = "";

            if (tglButton.isChecked())
                tipCautare = "C";
            else
                tipCautare = "N";

            if (tglTipArtBtn.isChecked())
                tipArticol = "S";
            else
                tipArticol = "A";

            String departCautare = DepartamentAgent.getDepartArticole(selectedDepartamentAgent);

            HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
            params.put("searchString", numeArticol);
            params.put("tipArticol", tipArticol);
            params.put("tipCautare", tipCautare);
            params.put("departament", departCautare);
            params.put("filiala", UserInfo.getInstance().getUnitLog());
            params.put("codUser", UserInfo.getInstance().getCod());
            params.put("transpTert", String.valueOf(DateLivrare.getInstance().getTranspInit().equals("TERT")));

            opArticol.getArticoleDistributie(params);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("unchecked")
    public void addListenerBtnSaveArt() {
        saveArtBtn.setOnClickListener(v -> {
            try {

                String localUnitMas = "", alteValori = "", subCmp = "0";
                boolean altDepozit = false;

                if (textCant.getVisibility() != View.VISIBLE) {
                    return;
                }

                if (isConditieCabluri05BV90() && listCabluri == null) {
                    getCabluri05(codArticol);
                    return;
                }

                if (UtilsComenzi.isPoligonRestrictionat()) {
                    Toast.makeText(getApplicationContext(), Constants.ARTICOL_ZONA_RESTRICTIONATA, Toast.LENGTH_LONG).show();
                    return;
                }

                if (textProcRed.getText().toString().trim().length() == 0) {
                    if (tglProc.getText().equals(("%")))
                        textProcRed.setText("0");

                }

                if (ModificareComanda.articoleComanda.equals("")) {
                    depozUnic = globalDepozSel.substring(2, 3);
                }

                if (!depozUnic.equals(globalDepozSel.substring(2, 3))) {
                    altDepozit = true;
                }

                if (!ModificareComanda.depozitUnic.equals("") && !ModificareComanda.depozitUnic.equals(globalDepozSel.substring(2, 3))) {
                    altDepozit = true;
                }

                String cantArticol = textCant.getText().toString().trim();

                if (selectedCant != Double.parseDouble(cantArticol)) {

                    Toast.makeText(getApplicationContext(), "Pretul nu corespunde cantitatii solicitate!", Toast.LENGTH_LONG).show();

                    return;
                }

                if (!UtilsComenzi.isComandaDl()
                        && Double.parseDouble(textCant.getText().toString().trim()) > Double.parseDouble(textStoc.getText()
                        .toString().replaceAll(",", ""))) {
                    Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
                    return;
                }

                localUnitMas = textUM.getText().toString().trim();

                // verificare umvanz.
                if (listUmVanz.size() > 1) {

                    artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
                    localUnitMas = artMap.get("rowText");

                    if (!selectedUnitMas.equals(localUnitMas)) {
                        Toast.makeText(getApplicationContext(), "U.m. vanzare eronata!", Toast.LENGTH_LONG).show();

                        return;
                    }

                }

                if (finalPrice != 0) {

                    tipAlert = " ";

                    double procRedFin = 0, valArticol = 0;

                    procentAprob = 0;

                    if (finalPrice == initPrice) // pretul din sap e pe
                        // cantitate, daca se
                        // modifica devine pe
                        // unitate
                        finalPrice = finalPrice / globalCantArt * valMultiplu;

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

                    if (procRedFin != 0)// procent_aprob se calculeaza doar
                    // daca exista proc. de reducere dat
                    // de ag.
                    {
                        procentAprob = (1 - finalPrice / (pretVanzare / globalCantArt * valMultiplu)) * 100;
                    }

                    if (procentAprob > discMaxAV) {
                        tipAlert = "SD";
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
                                tipAlert = ";" + "DV";
                            else
                                tipAlert = "DV";
                        }

                    } else {// agenti
                        if (procentAprob > discMaxSD) {
                            tipAlert = "DV";
                        }
                    }

                    double procRedFact = 0; // factura de reducere
                    if (listPrice != 0)
                        procRedFact = (initPrice / globalCantArt * valMultiplu - finalPrice) / (listPrice / globalCantArt * valMultiplu) * 100;

                    alteValori = String.valueOf(subCmp);

                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMinimumFractionDigits(2);
                    nf.setMaximumFractionDigits(2);

                    if (codArticol.length() == 18)
                        codArticol = codArticol.substring(10, 18);

                    ArticolComandaGed unArticol = new ArticolComandaGed();
                    unArticol.setNumeArticol(numeArticol);
                    unArticol.setCodArticol(codArticol);
                    unArticol.setCantitate(Double.valueOf(cantArticol));
                    unArticol.setDepozit(globalDepozSel);
                    unArticol.setPretUnit(finalPrice / valMultiplu);
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
                    unArticol.setDepartSintetic(unArticol.getDepart());
                    unArticol.setTipArt(tipArticol);
                    unArticol.setPromotie(Integer.parseInt(codPromo));
                    unArticol.setObservatii(tipAlert);
                    unArticol.setTipAlert(tipAlert);
                    unArticol.setStatus(" ");
                    unArticol.setDepartAprob(articolDBSelected.getDepartAprob());
                    unArticol.setDataExpPret(dataExpPret);
                    unArticol.setListCabluri(listCabluri);
                    unArticol.setGreutate(greutateArt);
                    unArticol.setFilialaSite(ModificareComanda.filialaAlternativaM);

                    unArticol.setTipMarfa(tipMarfa);
                    unArticol.setGreutateBruta(greutateBruta);
                    unArticol.setLungimeArt(lungimeArt);

                    unArticol.setCantitate50(Double.valueOf(cantitate50));
                    unArticol.setUm50(um50);

                    unArticol.setSintetic(articolDBSelected.getSintetic());

                    unArticol.setCantitateInit(Double.valueOf(cantArticol));
                    unArticol.setPretMinim(selectedArticol.getPretMinim());

                    if (procRedFin > 0)
                        unArticol.setIstoricPret(istoricPret);

                    ListaArticoleModificareComanda listaComanda = ListaArticoleModificareComanda.getInstance();
                    listaComanda.addArticolComanda(unArticol);

                    if (!altDepozit) {

                        if (ModificareComanda.articoleComanda.indexOf(codArticol) == -1) // articolul
                            // nu
                            // e
                            // adaugat
                            // deja
                            ModificareComanda.articoleComanda += numeArticol + "#" + codArticol + "#" + cantArticol + "#" + String.valueOf(finalPrice)
                                    + "#" + localUnitMas + "#" + globalDepozSel + "#" + nf.format(procRedFin) + "#" + tipAlert + "#" + codPromo + "#"
                                    + nf.format(procRedFact) + "#" + nf.format(procDiscClient) + "#" + nf.format(procentAprob) + "#" + valMultiplu + "#"
                                    + String.valueOf(valArticol) + "#" + infoArticol + "#" + Umb + "#" + cantUmb + "#" + alteValori + "#"
                                    + globalCodDepartSelectetItem + "#" + tipArticol + "@@";
                    } else {
                        Toast.makeText(getApplicationContext(), "Comanda contine depozite diferite, articolul nu a fost adaugat! ", Toast.LENGTH_LONG)
                                .show();
                    }

                    textNumeArticol.setText("");
                    textCodArticol.setText("");
                    textUM.setText("");
                    textStoc.setText("");
                    textCant.setText("");

                    numeArticol = "";
                    codArticol = "";
                    tipArticol = "";

                    procDiscClient = 0;
                    initPrice = 0;
                    finalPrice = 0;
                    umStoc = "";
                    globalCantArt = 0;
                    globalCodDepartSelectetItem = "";
                    greutateArt = 0;
                    listCabluri = null;

                    tipMarfa = "";
                    greutateBruta = 0;
                    lungimeArt = "";

                    redBtnTable.setVisibility(View.GONE);
                    labelStoc.setVisibility(View.GONE);
                    spinnerUnitMas.setVisibility(View.GONE);
                    labelCant.setVisibility(View.GONE);
                    textCant.setVisibility(View.GONE);
                    pretBtn.setVisibility(View.GONE);
                    layoutStocKA.setVisibility(View.GONE);
                    resultLayout.setVisibility(View.INVISIBLE);

                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

                } else {

                    textNumeArticol.setVisibility(View.GONE);
                    textCodArticol.setVisibility(View.GONE);
                    textUM.setVisibility(View.GONE);
                    textStoc.setVisibility(View.GONE);
                    textCant.setVisibility(View.GONE);
                    labelCant.setVisibility(View.GONE);
                    labelStoc.setVisibility(View.GONE);
                    saveArtBtn.setVisibility(View.GONE);

                    redBtnTable.setVisibility(View.GONE);
                    pretBtn.setVisibility(View.GONE);
                    spinnerUnitMas.setVisibility(View.GONE);

                    Toast toast = Toast.makeText(getApplicationContext(), "Articolul nu are pret definit!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                textProcRed.setText("0");

            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

        });

    }


    private boolean isConditieCabluri05BV90() {
        return articolDBSelected.getDepart().equals("05") && ModificareComanda.filialaAlternativaM.equals("BV90");
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

        Cabluri05Dialog cabluriDialog = new Cabluri05Dialog(SelectArtModificareCmd.this, listCabluri, textCant.getText().toString().trim());
        cabluriDialog.getWindow().setLayout(width, height);
        cabluriDialog.setCabluSelectedListener(this);
        cabluriDialog.show();

    }


    private void populateListViewArt(List<ArticolDB> resultsList) {
        listArticole.clear();
        txtNumeArticol.setText("");
        resultLayout.setVisibility(View.INVISIBLE);

        CautareArticoleAdapter adapterArticole1 = new CautareArticoleAdapter(this, resultsList);
        setListAdapter(adapterArticole1);
    }

    private boolean isComandaModif() {
        return ModificareComanda.codClientVar != null && !ModificareComanda.codClientVar.trim().isEmpty() && !UtilsComenzi.isComandaDl();
    }

    private boolean isOriceComandaModificata() {
        return ModificareComanda.codClientVar != null && !ModificareComanda.codClientVar.trim().isEmpty();
    }

    @SuppressWarnings("unchecked")
    private void listArtStoc(String pretResponse) {

        articolComandaModif = null;
        stocComandaModif = 0;

        if (!pretResponse.equals("-1")) {

            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);

            String[] tokenPret = pretResponse.split("#");

            resultLayout.setVisibility(View.VISIBLE);

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
            textStoc.setText(nf2.format(Double.valueOf(tokenPret[0]) + cantArtModificat));

            // pentru KA se afiseaza stocul disponibil = stoc real / 2
            if (UserInfo.getInstance().getTipAcces().equals("27")) {
                layoutStocKA.setVisibility(View.VISIBLE);
                textUmKA.setText(tokenPret[1]);
                textStocKA.setText(nf2.format((Double.valueOf(tokenPret[0]) + cantArtModificat) / 2));
            }

            umStoc = tokenPret[1];

            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();

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

                listUmVanz.add(tempUmVanz);
                spinnerUnitMas.setAdapter(adapterUmVanz);
                spinnerUnitMas.setVisibility(View.VISIBLE);
            }

        } else {

            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

            textUM.setText("");
            textStoc.setText("");
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

        if (!pretArticol.getErrMsg().isEmpty()) {
            Toast.makeText(getApplicationContext(), pretArticol.getErrMsg(), Toast.LENGTH_LONG).show();
            saveArtBtn.setVisibility(View.INVISIBLE);
            return;
        }

        cantUmb = pretArticol.getCantitateUmBaza();
        Umb = pretArticol.getUmBaza();
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

        cmpArt = Double.valueOf(pretArticol.getCmp());
        valMultiplu = Double.parseDouble(pretArticol.getMultiplu());
        globalCantArt = Double.valueOf(pretArticol.getCantitateUmBaza());

        saveArtBtn.setVisibility(View.VISIBLE);
        textPromo.setText("");

        textPromo.setVisibility(View.INVISIBLE);

        if (pretArticol.isPromo()) {
            textPromo.setVisibility(View.VISIBLE);
            textPromo.setText("Articol cu promotie.");
        }

        NumberFormat nf2 = NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(3);
        nf2.setMaximumFractionDigits(3);

        if (tglProc.isChecked())
            tglProc.performClick();

        codPromo = "-1";

        txtPretArt.setVisibility(View.VISIBLE);

        initPrice = Double.valueOf(pretArticol.getPret());
        listPrice = Double.valueOf(pretArticol.getPretLista());

        textCondPret.setVisibility(View.VISIBLE);
        textCondPret.setText(HelperPreturi.getInfoPret(pretArticol, nf2));

        txtImpachetare.setText(pretArticol.getImpachetare());

        if (pretArticol.getIstoricPret().trim().isEmpty())
            findViewById(R.id.textIstoricPret).setVisibility(View.GONE);
        else {
            findViewById(R.id.textIstoricPret).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textIstoricPret)).setText(HelperPreturi.getIstoricPret(pretArticol.getIstoricPret()));
        }


        istoricPret = UtilsFormatting.getIstoricPret(pretArticol.getIstoricPret(), EnumTipComanda.DISTRIBUTIE);

        procDiscClient = 0;

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
        textMultipluArt.setText("Unit.pret: " + valMultiplu + " " + umStoc);


        txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
        txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));

        dataExpPret = pretArticol.getDataExp();


        discMaxAV = 0;
        discMaxSD = 0;

        infoArticol = pretArticol.getConditiiPret().replace(',', '.');

        pretVanzare = listPrice; // se calculeaza procentul de aprobare

        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
            if (listPrice > 0)
                procDiscClient = 100 - (initPrice / listPrice) * 100;
        }


        textProcRed.setFocusableInTouchMode(true);
        tglProc.setEnabled(true);
        txtPretArt.setEnabled(true);

        // se afiseaza direct pretul si nu procentul
        tglProc.setChecked(false);
        tglProc.performClick();

        if (hasArticolDiscount(pretArticol)) {
            txtPretArt.setEnabled(false);
            textProcRed.setFocusable(false);
            tglProc.setEnabled(false);
            textPromo.setVisibility(View.VISIBLE);
            textPromo.setText("Pret promotional");
            codPromo = "1";
        } else {

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);
        }

        if (!codPromo.equals("1")) {
            textProcRed.requestFocus();
        }

    }

    private boolean hasArticolDiscount(PretArticolGed pretArticol) {
        return pretArticol.getFaraDiscount().toUpperCase().equals("X");

    }


    protected void onListItemClick(ListView l, View v, int position, long id) {

        ArticolDB articol = ((ArticolDB) l.getAdapter().getItem(position));

        articolDBSelected = articol;

        super.onListItemClick(l, v, position, id);

        numeArticol = articol.getNume();
        codArticol = articol.getCod();

        String umVanz;

        if (ModificareComanda.isComandaDistrib)
            umVanz = articol.getUmVanz10();
        else
            umVanz = articol.getUmVanz();

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

        listUmVanz.clear();
        spinnerUnitMas.setVisibility(View.GONE);
        HashMap<String, String> tempUmVanz;
        tempUmVanz = new HashMap<String, String>();
        tempUmVanz.put("rowText", umVanz);

        listUmVanz.add(tempUmVanz);
        spinnerUnitMas.setAdapter(adapterUmVanz);

        textNumeArticol.setVisibility(View.GONE);
        textCodArticol.setVisibility(View.GONE);
        saveArtBtn.setVisibility(View.GONE);

        redBtnTable.setVisibility(View.GONE);

        try {
            String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

            if (tokenDep[0].trim().length() == 2)
                globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
            else
                globalDepozSel = tokenDep[0].trim();

            performListArtStoc();

        } catch (Exception ex) {
            Log.e("Error", ex.toString());
            Toast.makeText(getApplicationContext(), "Eroare!", Toast.LENGTH_SHORT).show();

        }

    }

    private void performListArtStoc() {

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        String localFiliala;

        if (DateLivrare.getInstance().getCodFilialaCLP().length() == 4)
            localFiliala = DateLivrare.getInstance().getCodFilialaCLP();
        else {
            if (ModificareComanda.filialaAlternativaM.equals("BV90") && globalDepozSel.equals("MAV1"))
                localFiliala = "BV92";
            else
                localFiliala = ModificareComanda.filialaAlternativaM;
        }

        paramsGetStocDepozit = new HashMap<String, String>();

        paramsGetStocDepozit.put("codArt", codArticol);
        paramsGetStocDepozit.put("filiala", localFiliala);
        paramsGetStocDepozit.put("depozit", globalDepozSel);

        opArticol.getStocDepozit(paramsGetStocDepozit);

    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    public void operationComplete(EnumArticoleDAO methodName, Object result) {

        switch (methodName) {
            case GET_ARTICOLE_DISTRIBUTIE:
                populateListViewArt(opArticol.deserializeArticoleVanzare((String) result));
                break;
            case GET_PRET_UNIC:
                listPretArticol(opArticol.deserializePretGed(result));
                break;
            case GET_STOC_DEPOZIT:
                listArtStoc((String) result);
                break;
            case GET_CABLURI_05:
                afisCabluri05(opArticol.deserializeCabluri05((String) result));
                break;
            case GET_FACTOR_CONVERSIE_MODIF_CMD:
                loadFactorConversieModifCmd((String) result);
                break;
            default:
                break;

        }

    }

    @Override
    public void cabluriSelected(List<BeanCablu05> listCabluri) {
        this.listCabluri = listCabluri;
        saveArtBtn.performClick();

    }

}
