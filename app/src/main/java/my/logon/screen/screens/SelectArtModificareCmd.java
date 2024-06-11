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

import java.text.DecimalFormat;
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
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleModificareComanda;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;

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
    LinearLayout redBtnTable, layoutStocKA, layoutPretMaxKA, layoutPretMediuKA;
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

    private TextView textUM, procDisc, textPretTVA, textMultipluArt;
    private TextView labelCant, labelStoc, textPretMinKA, textPretMediuKA;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.selectartcmdheader);

        opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
        opArticol.setListener(this);

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

        textPretMediuKA = (TextView) findViewById(R.id.textPretMediuKA);
        layoutPretMediuKA = (LinearLayout) findViewById(R.id.layoutPretMediuKA);
        layoutPretMediuKA.setVisibility(View.INVISIBLE);

        textPretMinKA = (TextView) findViewById(R.id.textPretMaxKA);
        layoutPretMaxKA = (LinearLayout) findViewById(R.id.layoutPretMaxKA);
        layoutPretMaxKA.setVisibility(View.INVISIBLE);

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

        procDisc = (TextView) findViewById(R.id.procDisc);

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
        textPretTVA = (TextView) findViewById(R.id.textPretTVA);
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
        textPretTVA.setVisibility(View.INVISIBLE);
        textMultipluArt.setVisibility(View.INVISIBLE);

    }

    private void addSpinnerDepartamente() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
                DepartamentAgent.getDepartamenteAgent());

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

                        if (ModificareComanda.isComandaDistrib)
                            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
                        else
                            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

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

                        if (ModificareComanda.isComandaDistrib)
                            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
                        else
                            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

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

                                        if (ModificareComanda.isComandaDistrib)
                                            textPretTVA.setText(String.valueOf(nf2.format(finalPrice * Constants.TVA)));
                                        else
                                            textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));
                                    }
                                }

                            } else {
                                txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                                if (ModificareComanda.isComandaDistrib)
                                    textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
                                else
                                    textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));
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

                            if (ModificareComanda.isComandaDistrib)
                                textPretTVA.setText(String.valueOf(nf2.format(finalPrice * Constants.TVA)));
                            else
                                textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));

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

                    if (!textStoc.getText().equals("")) {
                        if (nf2.parse(textStoc.getText().toString()).doubleValue() > 0) {
                            if (Double.parseDouble(textCant.getText().toString().trim()) <= nf2.parse(textStoc.getText().toString()).doubleValue()) {
                                mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

                                performGetPret();
                            } else
                                Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Articolul nu exista in stoc!", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    @SuppressWarnings("unchecked")
    private void actionGetPret_old() {
        String depSel = "";
        String uLog = UserInfo.getInstance().getUnitLog();
        String tipUser = "";

        String localCanalDistrib = ModificareComanda.isComandaDistrib ? "10" : "20";

        if (ModificareComanda.filialaAlternativaM.toUpperCase().contains("BV9") && globalDepozSel.equals("MAV1"))
            localCanalDistrib = "10";

        if (codArticol.length() == 8)
            codArticol = "0000000000" + codArticol;

        depSel = globalCodDepartSelectetItem.substring(0, 2);

        if (!ModificareComanda.isComandaDistrib || globalDepozSel.equals("MAV1")) {
            depSel = "11";
            uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
        }
        if (UserInfo.getInstance().getTipAcces().equals("9")) {
            tipUser = "AV";
        }
        if (UserInfo.getInstance().getTipAcces().equals("10")) {
            tipUser = "SD";
        }
        if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")) {
            tipUser = "DV";
        }
        if (UserInfo.getInstance().getTipAcces().equals("27"))
            tipUser = "KA";

        String paramUnitMas = textUM.getText().toString();

        if (listUmVanz.size() > 1) {
            artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
            paramUnitMas = artMap.get("rowText");

        }

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("client", ModificareComanda.codClientVar);
        params.put("articol", codArticol);
        params.put("cantitate", textCant.getText().toString().trim());
        params.put("depart", depSel);
        params.put("um", paramUnitMas);
        params.put("ul", uLog);
        params.put("depoz", globalDepozSel);
        params.put("tipUser", tipUser);
        params.put("codUser", UserInfo.getInstance().getCod());
        params.put("canalDistrib", localCanalDistrib);
        params.put("filialaAlternativa", ModificareComanda.filialaAlternativaM);
        params.put("tipTransport", DateLivrare.getInstance().getTransport());

        opArticol.getPret(params);

    }


    private void actionGetPret() {
        String depSel = "";
        String uLog = UserInfo.getInstance().getUnitLog();


        String localCanalDistrib = ModificareComanda.isComandaDistrib ? "10" : "20";

        if (ModificareComanda.filialaAlternativaM.toUpperCase().contains("BV9") && globalDepozSel.equals("MAV1"))
            localCanalDistrib = "10";

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
        tglButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tglButton.isChecked()) {
                    txtNumeArticol.setHint("Introduceti cod articol");
                } else {
                    txtNumeArticol.setHint("Introduceti nume articol");
                }
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

    boolean isNotDepartRestricted(String codDepart) {
        if (UserInfo.getInstance().getTipAcces().equals("27"))
            return ModificareComanda.divizieComanda.equals(codDepart);
        else
            return UserInfo.getInstance().getCodDepart().equals(codDepart);
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
        saveArtBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                        valArticol = 0;
                        localUnitMas = "";
                        umStoc = "";
                        globalCantArt = 0;
                        globalCodDepartSelectetItem = "";
                        subCmp = "0";
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

    @SuppressWarnings("unchecked")
    private void listArtStoc(String pretResponse) {
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

            textUM.setText(tokenPret[1]);
            textStoc.setText(nf2.format(Double.valueOf(tokenPret[0])));

            // pentru KA se afiseaza stocul disponibil = stoc real / 2
            if (UserInfo.getInstance().getTipAcces().equals("27")) {
                layoutStocKA.setVisibility(View.VISIBLE);
                textUmKA.setText(tokenPret[1]);
                textStocKA.setText(nf2.format(Double.valueOf(tokenPret[0]) / 2));
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

    public void showModifCantInfo(String cantInfo, String cant50Info, String um50Info) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        StringBuilder infoText = new StringBuilder();

        infoText.append("\nAcest produs se vinde doar in ");
        infoText.append(um50Info);
        infoText.append(" si cantitatea a fost ajustata la ");
        infoText.append(cantInfo + " ");
        infoText.append(textUM.getText());
        infoText.append(" pentru a corespunde la ");
        infoText.append(cant50Info + " " + um50Info + ".\n");

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

    private boolean isConditiiModifCant50(String cantArticol, String cantitate50, String um50) {
        return !textUM.getText().toString().trim().equals(um50) && Double.parseDouble(cantitate50) > 0
                && selectedCant != Double.parseDouble(cantArticol);
    }

    private boolean isConditiiModifCant50(PretArticolGed pretArticol) {
        return !textUM.getText().toString().trim().equals(pretArticol.getUm50()) && Double.parseDouble(pretArticol.getCantitate50()) > 0
                && selectedCant != Double.parseDouble(pretArticol.getCantitate());
    }

    private void listPretArticol(PretArticolGed pretArticol) {

        selectedArticol = pretArticol;

        if (!pretArticol.getErrMsg().isEmpty()) {
            Toast.makeText(getApplicationContext(), pretArticol.getErrMsg(), Toast.LENGTH_LONG).show();
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

        String[] condPret =  pretArticol.getConditiiPret().split(";");
        int ii = 0;
        String[] tokPret;
        String stringCondPret = "";
        Double valCondPret = 0.0;

        for (ii = 0; ii < condPret.length; ii++) {
            tokPret = condPret[ii].split(":");
            valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
            if (valCondPret != 0) {
                stringCondPret += tokPret[0] + UtilsFormatting.addSpace(20 - tokPret[0].length()) + ":"
                        + UtilsFormatting.addSpace(10 - String.valueOf(nf2.format(valCondPret)).length()) + nf2.format(valCondPret)
                        + System.getProperty("line.separator");
            }
        }

        textCondPret.setVisibility(View.VISIBLE);
        textCondPret.setText(stringCondPret);

        txtImpachetare.setText(pretArticol.getImpachetare());

        afisIstoricPret(pretArticol.getIstoricPret());
        istoricPret = UtilsFormatting.getIstoricPret(pretArticol.getIstoricPret(), EnumTipComanda.DISTRIBUTIE);

        procDiscClient = 0;

        if (globalDepozSel.substring(2, 3).equals("V")) {

            if (initPrice / globalCantArt * valMultiplu < cmpArt) {

                Toast.makeText(getApplicationContext(), "Pret sub cmp.", Toast.LENGTH_LONG).show();

                if (layoutPretMaxKA.getVisibility() == View.VISIBLE)
                    layoutPretMaxKA.setVisibility(View.INVISIBLE);
            }
        }

        finalPrice = initPrice;

        textProcRed.setText("");

        redBtnTable.setVisibility(View.VISIBLE);
        textProcRed.setVisibility(View.VISIBLE);
        procDisc.setVisibility(View.VISIBLE);

        textMultipluArt.setVisibility(View.VISIBLE);
        textMultipluArt.setText("Unit.pret: " + valMultiplu + " " + umStoc);

        textPretTVA.setVisibility(View.VISIBLE);

        txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
        txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));

        dataExpPret = pretArticol.getDataExp();
        ((TextView) findViewById(R.id.textDataExp)).setText(UtilsDates.formatDataExp(pretArticol.getDataExp()));

        if (ModificareComanda.isComandaDistrib)
            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
        else
            textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

        discMaxAV = 0;
        discMaxSD = 0;

        infoArticol = pretArticol.getConditiiPret().replace(',', '.');

        pretVanzare = listPrice; // se calculeaza procentul de aprobare

        if (!UserInfo.getInstance().getTipAcces().equals("27")) {
            if (listPrice > 0)
                procDiscClient = 100 - (initPrice / listPrice) * 100;
        }


        procDisc.setText(nf2.format(procDiscClient));
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

    private void listArtPret(String pretResponse) {

        try {
            if (!pretResponse.equals("-1") && pretResponse.contains("#")) {

                String[] tokenPret = pretResponse.split("#");

                valMultiplu = 1;

                cantUmb = tokenPret[14];
                Umb = tokenPret[15];
                greutateArt = Double.parseDouble(tokenPret[24].trim());

                tipMarfa = tokenPret[26];
                greutateBruta = Double.parseDouble(tokenPret[27].trim());
                lungimeArt = tokenPret[28];

                cantitate50 = tokenPret[29];
                um50 = tokenPret[30];

                if (isConditiiModifCant50(tokenPret[0], cantitate50, um50)) {
                    textCant.setText(tokenPret[0]);
                    showModifCantInfo(tokenPret[0], cantitate50, um50);
                    selectedCant = Double.parseDouble(textCant.getText().toString().trim());
                }

                if (Double.parseDouble(cantitate50) == 0)
                    cantitate50 = tokenPret[0];

                cmpArt = Double.parseDouble(tokenPret[17]);

                if (Double.parseDouble(cantUmb) > nf2.parse(textStoc.getText().toString()).doubleValue()) {
                    Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
                    return;
                }

                valMultiplu = Double.parseDouble(tokenPret[13].toString().trim());

                globalCantArt = Double.parseDouble(tokenPret[14]);

                saveArtBtn.setVisibility(View.VISIBLE);
                textPromo.setText("");

                NumberFormat nf2 = NumberFormat.getInstance();
                nf2.setMinimumFractionDigits(3);
                nf2.setMaximumFractionDigits(3);

                if (tglProc.isChecked())
                    tglProc.performClick();

                codPromo = "-1";

                txtPretArt.setVisibility(View.VISIBLE);

                initPrice = Double.parseDouble(tokenPret[1]);
                listPrice = Double.parseDouble(tokenPret[8]);

                String[] condPret = tokenPret[9].split(";");
                int ii = 0;
                String[] tokPret;
                String stringCondPret = "";
                Double valCondPret = 0.0;

                for (ii = 0; ii < condPret.length; ii++) {
                    tokPret = condPret[ii].split(":");
                    valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
                    if (valCondPret != 0) {
                        stringCondPret += tokPret[0] + UtilsFormatting.addSpace(20 - tokPret[0].length()) + ":"
                                + UtilsFormatting.addSpace(10 - String.valueOf(nf2.format(valCondPret)).length()) + nf2.format(valCondPret)
                                + System.getProperty("line.separator");
                    }
                }

                textCondPret.setVisibility(View.VISIBLE);
                textCondPret.setText(stringCondPret);

                txtImpachetare.setText(tokenPret[19]);

                afisIstoricPret(tokenPret[20]);

                istoricPret = UtilsFormatting.getIstoricPret(tokenPret[20], EnumTipComanda.DISTRIBUTIE);

                procDiscClient = 0;
                minimKAPrice = 0;
                if (UserInfo.getInstance().getTipAcces().equals("27")) {

                    minimKAPrice = listPrice / globalCantArt * valMultiplu - (listPrice / globalCantArt * valMultiplu) * Double.valueOf(tokenPret[16]) / 100;

                    if (listPrice > 0)
                        procDiscClient = 100 - (initPrice / listPrice) * 100;

                    layoutPretMaxKA.setVisibility(View.VISIBLE);
                    textPretMinKA.setText(String.valueOf(nf2.format(minimKAPrice)));

                    layoutPretMediuKA.setVisibility(View.VISIBLE);
                    textPretMediuKA.setText(nf2.format(Double.valueOf(tokenPret[18])));

                }



                finalPrice = initPrice;

                textProcRed.setText("");

                redBtnTable.setVisibility(View.VISIBLE);
                textProcRed.setVisibility(View.VISIBLE);
                procDisc.setVisibility(View.VISIBLE);

                textMultipluArt.setVisibility(View.VISIBLE);
                textMultipluArt.setText("Unit.pret: " + tokenPret[13] + " " + umStoc);

                textPretTVA.setVisibility(View.VISIBLE);

                txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
                txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));

                dataExpPret = tokenPret[23];
                ((TextView) findViewById(R.id.textDataExp)).setText(UtilsDates.formatDataExp(tokenPret[23]));

                if (ModificareComanda.isComandaDistrib)
                    textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
                else
                    textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

                discMaxAV = Double.valueOf(tokenPret[10]);
                discMaxSD = Double.valueOf(tokenPret[11]);


                infoArticol = tokenPret[9].replace(',', '.');

                pretVanzare = listPrice; // se calculeaza procentul de aprobare

                if (!UserInfo.getInstance().getTipAcces().equals("27")) {
                    if (listPrice > 0)
                        procDiscClient = 100 - (initPrice / listPrice) * 100;
                }

                procDisc.setText(nf2.format(procDiscClient));
                textProcRed.setFocusableInTouchMode(true);
                tglProc.setEnabled(true);
                txtPretArt.setEnabled(true);

                // se afiseaza direct pretul si nu procentul
                tglProc.setChecked(false);
                tglProc.performClick();

                if (noDiscount(tokenPret[3])) {
                    txtPretArt.setEnabled(false);
                    textProcRed.setFocusable(false);
                    tglProc.setEnabled(false);
                    textPromo.setVisibility(View.VISIBLE);
                    textPromo.setText("Pret promotional");
                    codPromo = "1";
                } else {

                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

                    // verificare articole promotii
                    if (Double.parseDouble(tokenPret[5]) != 0) {
                        artPromoText = "";
                        textPromo.setVisibility(View.VISIBLE);

                        // articolul din promotie are alt pret
                        if (Double.parseDouble(tokenPret[6]) != 0) {

                        } else // articolul din promotie este gratuit
                        {
                            codPromo = "2";

                            // verificare cantitati articole gratuite
                            // cant. art promotie se adauga la cant. ceruta
                            if (Double.parseDouble(textCant.getText().toString().trim()) == Double.parseDouble(tokenPret[0])) {

                                // verificare cod articol promotie
                                // art. promo = art. din comanda
                                if (codArticol.equals(tokenPret[4])) {
                                    artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + numeArticol + " gratuit. ";
                                } else// art. promo diferit de art. din cmd.
                                {
                                    artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + tokenPret[4] + " gratuit. ";

                                }

                            } else // cant art. promotie se scade din cant.
                            // ceruta
                            {

                                artPromoText = "Din cantitatea comandata " + tokenPret[5] + " " + tokenPret[7] + " sunt gratis.";

                            }

                            textPromo.setText("Articol cu promotie");

                        }

                    }

                }

                // **la preturi zero se blocheaza modificarea
                if (Double.parseDouble(tokenPret[1].toString()) == 0) {
                    txtPretArt.setEnabled(false);
                }

                if (!codPromo.equals("1")) {
                    textProcRed.requestFocus();
                }

            } else {

                Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void afisIstoricPret(String infoIstoric) {
        LinearLayout layoutIstoric1 = (LinearLayout) findViewById(R.id.layoutIstoricPret1);
        LinearLayout layoutIstoric2 = (LinearLayout) findViewById(R.id.layoutIstoricPret2);
        LinearLayout layoutIstoric3 = (LinearLayout) findViewById(R.id.layoutIstoricPret3);

        layoutIstoric1.setVisibility(View.GONE);
        layoutIstoric2.setVisibility(View.GONE);
        layoutIstoric3.setVisibility(View.GONE);

        DecimalFormat df = new DecimalFormat("#0.00");

        if (infoIstoric.contains(":")) {
            String[] arrayIstoric = infoIstoric.split(":");

            if (arrayIstoric.length > 0 && arrayIstoric[0].contains("@")) {

                layoutIstoric1.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[0].split("@");

                TextView textIstoric1 = (TextView) findViewById(R.id.txtIstoricPret1);
                textIstoric1.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " / " + arrayPret[1] + " "
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

            if (arrayIstoric.length > 1 && arrayIstoric[1].contains("@")) {

                layoutIstoric2.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[1].split("@");

                TextView textIstoric2 = (TextView) findViewById(R.id.txtIstoricPret2);
                textIstoric2.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " / " + arrayPret[1] + " "
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

            if (arrayIstoric.length > 2 && arrayIstoric[2].contains("@")) {

                layoutIstoric3.setVisibility(View.VISIBLE);

                String[] arrayPret = arrayIstoric[2].split("@");

                TextView textIstoric3 = (TextView) findViewById(R.id.txtIstoricPret3);
                textIstoric3.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " / " + arrayPret[1] + " "
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

            }

        }

    }

    private boolean noDiscount(String artPromo) {

        if (artPromo.toUpperCase(Locale.getDefault()).equals("X"))
            return true;
        else if (!ModificareComanda.isComandaDistrib && !(ModificareComanda.filialaAlternativaM.toUpperCase().contains("BV9") && globalDepozSel.equals("MAV1")))
            return true;

        return false;

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

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("codArt", codArticol);
        params.put("filiala", localFiliala);
        params.put("depozit", globalDepozSel);

        opArticol.getStocDepozit(params);

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
            case GET_PRET:
                listArtPret((String) result);
                break;
            case GET_STOC_DEPOZIT:
                listArtStoc((String) result);
                break;
            case GET_CABLURI_05:
                afisCabluri05(opArticol.deserializeCabluri05((String) result));
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
