/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterFilialeLivrare;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanCodPostal;
import my.logon.screen.beans.BeanDateLivrareClient;
import my.logon.screen.beans.BeanFilialaLivrare;
import my.logon.screen.beans.BeanLocalitate;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.beans.GeocodeAddress;
import my.logon.screen.beans.ObiectivConsilier;
import my.logon.screen.beans.StatusIntervalLivrare;
import my.logon.screen.dialogs.CautaObiectivDialog;
import my.logon.screen.dialogs.CoduriPostaleDialog;
import my.logon.screen.dialogs.MapAddressDialog;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumFiliale;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.helpers.HelperAdreseLivrare;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.CautaObiectivListener;
import my.logon.screen.listeners.CodPostalListener;
import my.logon.screen.listeners.MapListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.listeners.ZileLivrareListener;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsAddress;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectAdrLivrCmdGed extends AppCompatActivity implements AsyncTaskListener, OperatiiClientListener, OperatiiAdresaListener, MapListener,
        CautaObiectivListener, ZileLivrareListener, CodPostalListener {

    private Button saveAdrLivrBtn;
    private EditText txtPers, txtTel, txtObservatii;

    private TextView txtObsPlata, textMail;

    private static final String METHOD_NAME = "getClientJud";

    private String[] tipTransport = {"CU LIVRARE", "CU RIDICARE"};

    String[] docInsot = {"Factura", "Aviz de expeditie"};

    private String[] tipPlataContract = {"LC - Limita credit", "N - Numerar in filiala", "OPA - OP avans", "R - ramburs"};
    private String[] tipPlataClBlocatIP = {"N - Numerar in filiala", "OPA - OP avans", "R - ramburs"};
    private String[] tipPlataClBlocatNonIP = {"C - Card bancar", "N - Numerar in filiala", "OPA - OP avans", "R - Ramburs"};
    private String[] tipPlataRestIP = {"C - Card bancar", "N - Numerar in filiala", "OPA - OP avans", "R - Ramburs"};
    private String[] tipPlataRestNonIP = {"C - Card bancar", "N - Numerar in filiala", "OPA - OP avans", "R - Ramburs"};
    private String[] tipPlataDL = {"C - Card bancar", "N - Numerar in filiala", "OPA - OP avans"};

    public SimpleAdapter adapterJudete, adapterJudeteLivrare, adapterAdreseLivrare;

    private Spinner spinnerPlata, spinnerTransp, spinnerJudet, spinnerTermenPlata, spinnerJudetLivrare;
    private static ArrayList<HashMap<String, String>> listJudete = null, listJudeteLivrare = null;
    private ArrayAdapter<String> adapterTermenPlata;
    private LinearLayout layoutAdr1, layoutAdr2, layoutAdr21, layoutAdr3, layoutMail;

    int posJudetSel = 0;
    private NumberFormat nf;
    String articoleSite, umSite, cantSite, valSite;
    private String codJudetLivrare = "";
    private Spinner spinnerAdreseLivrare;

    public static RadioButton radioAdresaSediu, radioAltaAdresa, radioAdresaObiectiv;
    LinearLayout layoutAdrLivrare1, layoutAdrLivrare2;

    private ArrayList<HashMap<String, String>> listAdreseLivrare = null;
    private OperatiiClient operatiiClient;
    private List<BeanAdresaLivrare> listAdrese = null;
    private RadioButton radioListAdrese, radioTextAdrese;
    private LinearLayout layoutHeaderAdrese, layoutListAdrese, layoutAdrOras, layoutAdrStrada;
    private CheckBox chkbClientLaRaft;
    private OperatiiAdresa operatiiAdresa;
    private AutoCompleteTextView textLocalitate, textStrada, textLocalitateLivrare, textStradaLivrare;
    private Button btnPozitieAdresa;
    private EditText textNrStr;
    private Spinner spinnerIndoire;
    private TextView textDataLivrare;
    private Button btnDataLivrare;
    private Spinner spinnerMeseriasi;
    private CheckBox checkFactPaleti;
    private CheckBox chkCamionDescoperit;
    private Spinner spinnerProgramLivrare;
    private CheckBox checkObsSofer;
    private List<String> listLocalitatiSediu;
    private List<String> listLocalitatiLivrare;

    private Button btnSelectObiectiv;
    private TextView textObiectivSelectat;
    private ObiectivConsilier obiectivSelectat;
    private ImageButton btnStergeObiectiv;
    private BeanDateLivrareClient dateLivrareClient;

    private CheckBox checkFactura, checkAviz;
    private BeanAdreseJudet listAdreseJudet, listAlteAdrese;

    private Spinner spinnerDebitare;
    private Spinner spinnerFilialeTCLI;
    private boolean isAdresaLivrareTCLI;

    private ActivityResultLauncher<Intent> startActivityForResult;
    private String ulLivrareModifCmd;
    private Button btnCodPostal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        try {

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

            setTheme(R.style.LRTheme);
            setContentView(R.layout.selectadrlivrcmd_ged_header);

            ActionBar actionBar = getActionBar();

            actionBar.setTitle("Date livrare");
            actionBar.setDisplayHomeAsUpEnabled(true);

            nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);

            this.saveAdrLivrBtn = (Button) findViewById(R.id.saveAdrLivrBtn);
            addListenerSaveAdr();

            this.layoutAdr1 = (LinearLayout) findViewById(R.id.layoutAdr1);
            this.layoutAdr2 = (LinearLayout) findViewById(R.id.layoutAdr2);
            this.layoutAdr21 = (LinearLayout) findViewById(R.id.layoutAdr21);
            this.layoutAdr3 = (LinearLayout) findViewById(R.id.layoutAdr3);

            btnCodPostal = findViewById(R.id.btnCodPostal);
            setListenerBtnCodPostal();

            textLocalitate = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocalitate);
            textNrStr = (EditText) findViewById(R.id.textNrStr);

            btnPozitieAdresa = (Button) findViewById(R.id.btnPozitieAdresa);
            setListnerBtnPozitieAdresa();

            chkbClientLaRaft = (CheckBox) findViewById(R.id.clientRaft);
            addListenerClientLaRaft();

            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("parrentClass") != null && bundle.getString("parrentClass").equals("ModificareComanda")
                    && !DateLivrare.getInstance().getTipPersClient().equals("PF")) {
                LinearLayout layoutRaft = (LinearLayout) findViewById(R.id.layoutClientRaft);
                layoutRaft.setVisibility(View.GONE);

                if ((Double.parseDouble(bundle.getString("limitaCredit")) > 1) && !bundle.getString("termenPlata").equals("C000")) {
                    CreareComandaGed.tipPlataContract = bundle.getString("tipPlataContract");
                    DateLivrare.getInstance().setTipPlata("LC");
                }

            }

            ulLivrareModifCmd = "";

            if (bundle != null && bundle.getString("parrentClass") != null && bundle.getString("parrentClass").equals("ModificareComanda")) {
                ulLivrareModifCmd = bundle.getString("ulLivrare");
            }

            textLocalitateLivrare = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocLivrare);

            textStrada = (AutoCompleteTextView) findViewById(R.id.autoCompleteStrada);
            textStrada.setText(UtilsAddress.getStreetFromAddress(DateLivrare.getInstance().getStrada()));

            textNrStr.setText(UtilsAddress.getStreetNumber(DateLivrare.getInstance().getStrada()));

            ((EditText) findViewById(R.id.textCodPostal)).setText(DateLivrare.getInstance().getCodPostal());

            textStradaLivrare = (AutoCompleteTextView) findViewById(R.id.autoCompleteStradaLivrare);

            layoutMail = (LinearLayout) findViewById(R.id.layoutMail);
            textMail = (EditText) findViewById(R.id.txtMail);

            DateLivrare dateLivrareInstance = DateLivrare.getInstance();

            txtPers = (EditText) findViewById(R.id.txtPersCont);
            txtTel = (EditText) findViewById(R.id.txtTelefon);
            txtPers.setText(dateLivrareInstance.getPersContact());
            txtTel.setText(dateLivrareInstance.getNrTel());

            checkFactura = (CheckBox) findViewById(R.id.checkFactura);
            setListenerCheckFactura();
            checkAviz = (CheckBox) findViewById(R.id.checkAviz);
            setListenerCheckAviz();

            checkObsSofer = (CheckBox) findViewById(R.id.chkObsSofer);
            setListenerCheckObsSofer();

            txtObservatii = (EditText) findViewById(R.id.txtObservatii);

            if (DateLivrare.getInstance().getObsLivrare() != null && !DateLivrare.getInstance().getObsLivrare().trim().isEmpty()) {
                txtObservatii.setText(dateLivrareInstance.getObsLivrare());
                checkObsSofer.setChecked(true);
            }

            txtObsPlata = (EditText) findViewById(R.id.txtObsPlata);
            txtObsPlata.setText(dateLivrareInstance.getObsPlata());

            chkbClientLaRaft.setChecked(dateLivrareInstance.isClientRaft());

            spinnerPlata = (Spinner) findViewById(R.id.spinnerPlata);

            ArrayAdapter<String> adapterSpinnerPlata;
            ArrayAdapter<String> adapterSpinnerTransp;

            layoutMail.setVisibility(View.VISIBLE);
            textMail.setText(dateLivrareInstance.getMail().trim().replace("~", "@"));

            adapterSpinnerTransp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipTransport);

            if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_COMANDA)) {
                List<String> itemsTransp = new ArrayList<>();
                for (int ii = 0; ii < adapterSpinnerTransp.getCount(); ii++) {
                    if (!adapterSpinnerTransp.getItem(ii).startsWith("TFRN")) {
                        itemsTransp.add((String) adapterSpinnerTransp.getItem(ii));
                    }
                }
                adapterSpinnerTransp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsTransp);
            }

            if (DateLivrare.getInstance().isClientBlocat()) {
                if (CreareComandaGed.tipClient.equals("IP"))
                    adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataClBlocatIP);
                else
                    adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataClBlocatNonIP);

            } else {

                if (!CreareComandaGed.tipPlataContract.trim().isEmpty() || DateLivrare.getInstance().getTipPlata().equals("LC")) {

                    if (!CreareComandaGed.tipPlataContract.trim().isEmpty()) {
                        ((TextView) findViewById(R.id.tipPlataContract)).setText("Contract: " + CreareComandaGed.tipPlataContract);
                    }

                    adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataContract);
                } else if (CreareComandaGed.tipClient.equals("IP"))
                    adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataRestIP);
                else
                    adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipPlataRestNonIP);

            }

            if (isComandaDl()) {
                List<String> metsPlata = new ArrayList<>();

                for (int ii = 0; ii < adapterSpinnerPlata.getCount(); ii++)
                    if (!adapterSpinnerPlata.getItem(ii).toString().startsWith("R"))
                        metsPlata.add(adapterSpinnerPlata.getItem(ii));

                adapterSpinnerPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, metsPlata.toArray(new String[0]));

            }

            adapterSpinnerPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlata.setAdapter(adapterSpinnerPlata);

            UtilsComenzi.setDefaultPlataMethod(spinnerPlata);

            addListenerTipPlata();


            checkFactPaleti = (CheckBox) findViewById(R.id.chkFactPaleti);
            checkFactPaleti.setChecked(DateLivrare.getInstance().isFactPaletSeparat());

            chkCamionDescoperit = (CheckBox) findViewById(R.id.chkCamionDescoperit);
            chkCamionDescoperit.setChecked(DateLivrare.getInstance().isCamionDescoperit());

            spinnerIndoire = (Spinner) findViewById(R.id.spinnerIndoire);
            setupSpinnerIndoire();

            spinnerDebitare = (Spinner) findViewById(R.id.spinnerDebitare);
            setupSpinnerDebitare();

            spinnerTransp = (Spinner) findViewById(R.id.spinnerTransp);


            adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTransp.setAdapter(adapterSpinnerTransp);
            spinnerFilialeTCLI = (Spinner) findViewById(R.id.spinnerFiliale);
            setSpinnerFilialeTCLIListener();
            addListenerTipTransport();


            spinnerJudet = (Spinner) findViewById(R.id.spinnerJudet);
            spinnerJudet.setOnItemSelectedListener(new regionSelectedListener());


            operatiiAdresa = new OperatiiAdresaImpl(this);
            operatiiAdresa.setOperatiiAdresaListener(this);

            listJudete = new ArrayList<HashMap<String, String>>();
            adapterJudete = new SimpleAdapter(this, listJudete, R.layout.rowlayoutjudete, new String[]{"numeJudet", "codJudet"}, new int[]{
                    R.id.textNumeJudet, R.id.textCodJudet});

            spinnerTermenPlata = (Spinner) findViewById(R.id.spinnerTermenPlata);
            adapterTermenPlata = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            adapterTermenPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTermenPlata.setAdapter(adapterTermenPlata);

            if (CreareComandaGed.listTermenPlata != null && CreareComandaGed.listTermenPlata.size() > 0) {
                adapterTermenPlata.addAll(CreareComandaGed.listTermenPlata);

                if (UserInfo.getInstance().getTipUserSap().equals("CGED") || UtilsUser.isSSCM() || UtilsUser.isUserIP()) {
                    spinnerTermenPlata.setSelection(CreareComandaGed.listTermenPlata.size() - 1);
                }

            } else {
                adapterTermenPlata.add("C000");
                if (DateLivrare.getInstance().getTermenPlata().trim().length() > 0) {
                    String[] tokTermen = DateLivrare.getInstance().getTermenPlata().split(";");
                    int nrLivr = 0;
                    for (nrLivr = 0; nrLivr < tokTermen.length; nrLivr++) {
                        if (!tokTermen[nrLivr].equals("C000"))
                            adapterTermenPlata.add(tokTermen[nrLivr]);
                    }

                }
            }


            addListenerTermenPlata();

            addAdresaLivrare();

            int i = 0;

            // document insotitor
            checkFactura.setChecked(false);
            checkAviz.setChecked(false);
            if (DateLivrare.getInstance().getTipDocInsotitor().equals("1"))
                checkFactura.setChecked(true);
            else if (DateLivrare.getInstance().getTipDocInsotitor().equals("2"))
                checkAviz.setChecked(true);
            else if (DateLivrare.getInstance().getTipDocInsotitor().equals("3")) {
                checkFactura.setChecked(true);
                checkAviz.setChecked(true);
            }
            // sf. doc insot

            // tip plata
            String strTipPlata = "";

            for (i = 0; i < adapterSpinnerPlata.getCount(); i++) {

                strTipPlata = adapterSpinnerPlata.getItem(i).toString().substring(0, adapterSpinnerPlata.getItem(i).toString().indexOf("-") - 1).trim();

                if (strTipPlata.equals(UtilsComenzi.setTipPlataClient(dateLivrareInstance.getTipPlata()))) {
                    spinnerPlata.setSelection(i);
                    break;
                }
            }


            // tip transport
            for (i = 0; i < adapterSpinnerTransp.getCount(); i++) {
                if (UtilsComenzi.getSpinnerTipTransp(adapterSpinnerTransp.getItem(i)).equals(dateLivrareInstance.getTransport())) {
                    spinnerTransp.setSelection(i);
                    break;
                }
            }

            layoutAdr1.setVisibility(View.VISIBLE);
            layoutAdr2.setVisibility(View.VISIBLE);
            layoutAdr21.setVisibility(View.VISIBLE);
            layoutAdr3.setVisibility(View.VISIBLE);


            radioListAdrese = (RadioButton) findViewById(R.id.radioListAdrese);
            radioTextAdrese = (RadioButton) findViewById(R.id.radioTextAdrese);

            layoutHeaderAdrese = (LinearLayout) findViewById(R.id.layoutHeaderAdrese);
            layoutListAdrese = (LinearLayout) findViewById(R.id.layoutListAdrese);

            layoutAdrOras = (LinearLayout) findViewById(R.id.layoutAdr1);
            layoutAdrStrada = (LinearLayout) findViewById(R.id.layoutAdr2);

            layoutHeaderAdrese.setVisibility(View.GONE);
            layoutListAdrese.setVisibility(View.GONE);

            operatiiClient = new OperatiiClient(this);
            operatiiClient.setOperatiiClientListener(SelectAdrLivrCmdGed.this);

            if (UserInfo.getInstance().getTipUserSap().equals("KA3") && DateLivrare.getInstance().getTipPersClient().equals("D")) {

                setupListAdreseLayout();
            }

            textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

            if (!dateLivrareInstance.getDataLivrare().isEmpty())
                textDataLivrare.setText(dateLivrareInstance.getDataLivrare());

            btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
            addListenerDataLivrare();

            String tipUser;

            if (UtilsUser.isAgentOrSD())
                tipUser = "AV";
            else
                tipUser = "CV";

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("codFiliala", UserInfo.getInstance().getUnitLog());
            params.put("tipUser", tipUser);
            params.put("codUser", UserInfo.getInstance().getCod());
            params.put("codDepart", UserInfo.getInstance().getCodDepart());

            spinnerMeseriasi = (Spinner) findViewById(R.id.spinnerMeseriasi);
            operatiiClient.getMeseriasi(params);

            spinnerProgramLivrare = (Spinner) findViewById(R.id.spinnerProgramLivrare);
            spinnerProgramLivrare.setSelection(3);
            if (!dateLivrareInstance.getProgramLivrare().equals("0"))
                spinnerProgramLivrare.setSelection(Integer.parseInt(dateLivrareInstance.getProgramLivrare()));

            textObiectivSelectat = (TextView) findViewById(R.id.textObiectivSelectat);

            if (!UtilsUser.isAgentOrSDorKA()) {

                ((LinearLayout) findViewById(R.id.layoutObiectiveConsilieri)).setVisibility(View.VISIBLE);

                btnSelectObiectiv = (Button) findViewById(R.id.btnSelectObiectiv);
                setListenerSelectObiectiv();

                btnStergeObiectiv = (ImageButton) findViewById(R.id.btnStergeObiectiv);
                setListenerStergeObiectiv();

                if (DateLivrare.getInstance().isAdresaObiectiv()) {
                    radioAdresaObiectiv.setVisibility(View.VISIBLE);
                    radioAdresaObiectiv.setChecked(true);
                }

                if (DateLivrare.getInstance().getObiectivConsilier() != null) {
                    textObiectivSelectat.setVisibility(View.VISIBLE);
                    textObiectivSelectat.setText(DateLivrare.getInstance().getObiectivConsilier().getBeneficiar() + " / "
                            + DateLivrare.getInstance().getObiectivConsilier().getAdresa());
                    btnStergeObiectiv.setVisibility(View.VISIBLE);
                }
            }


            startActivityForResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 1) {
                            DateLivrare.getInstance().setCoordonateAdresa(result.getData().getParcelableExtra("coord"));
                            setAdresaLivrare(MapUtils.getAddress(result.getData().getParcelableExtra("address")));
                        }
                    }
            );

            if (UtilsUser.isCGED() || UtilsUser.isSSCM() || CreareComandaGed.tipClient.equals("IP")) {
                getDateLivrareClient();
            }

            isAdresaLivrareTCLI = false;
            if (bundle != null && bundle.getString("parrentClass") != null && bundle.getString("parrentClass").equals("CreareComandaGed")) {
                if (bundle.getString("adrLivrareTCLI") != null && bundle.getString("adrLivrareTCLI").equals(("true"))) {
                    isAdresaLivrareTCLI = true;
                    ((LinearLayout) findViewById(R.id.layoutFilLivrare)).setVisibility(View.GONE);
                    spinnerTransp.setSelection(0);
                    spinnerTransp.setSelection(0);
                    clearAdresaLivrare();
                    performGetJudete();
                }
            }

            if (DateLivrare.getInstance().getTransport().trim().isEmpty() || DateLivrare.getInstance().getTransport().equals("TRAP") || DateLivrare.getInstance().getTransport().equals("TERT") || DateLivrare.getInstance().getTransport().equals("TFRN"))
                performGetJudete();

        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void clearAdresaLivrare() {
        DateLivrare.getInstance().setOras("");
        DateLivrare.getInstance().setStrada("");
        DateLivrare.getInstance().setCodJudet("");
        DateLivrare.getInstance().setCoordonateAdresa(null);

        spinnerJudet.setSelection(0);
        textLocalitate.setText("");
        textNrStr.setText("");
        textStrada.setText("");

    }


    private void setListenerCheckFactura() {
        checkFactura.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkAviz.setChecked(false);
            }
        });
    }

    private void setListenerCheckAviz() {
        checkAviz.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checkFactura.setChecked(false);
            }
        });
    }

    private void getDateLivrareClient() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codClient", CreareComandaGed.codClientVar);
        operatiiAdresa.getDateLivrareClient(params);

    }

    private void setListenerSelectObiectiv() {

        btnSelectObiectiv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CautaObiectivDialog obiectivDialog = new CautaObiectivDialog(SelectAdrLivrCmdGed.this);
                obiectivDialog.setObiectivListener(SelectAdrLivrCmdGed.this);
                obiectivDialog.show();

            }
        });

    }

    private void setAdresaLivrareObiectiv() {

        String[] tokenAdresa = obiectivSelectat.getAdresa().split(",");
        DateLivrare.getInstance().setCodJudetD(obiectivSelectat.getCodJudet());
        DateLivrare.getInstance().setOrasD(tokenAdresa[0]);

        if (obiectivSelectat.getCoordGps() != null && obiectivSelectat.getCoordGps().contains(",")) {

            String[] tokCoords = obiectivSelectat.getCoordGps().split(",");
            LatLng coordonateAdresa = new LatLng(Double.valueOf(tokCoords[0]), Double.valueOf(tokCoords[1]));
            DateLivrare.getInstance().setCoordonateAdresa(coordonateAdresa);

        }

        if (tokenAdresa.length == 2)
            DateLivrare.getInstance().setAdresaD(tokenAdresa[1].trim());
        else if (tokenAdresa.length == 3)
            DateLivrare.getInstance().setAdresaD(tokenAdresa[1].trim() + " , " + tokenAdresa[2].trim());
        else
            DateLivrare.getInstance().setAdresaD("-");

    }

    private void setListenerStergeObiectiv() {

        btnStergeObiectiv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                obiectivSelectat = null;
                textObiectivSelectat.setText("");
                radioAdresaObiectiv.setVisibility(View.GONE);
                radioAdresaSediu.setChecked(true);
                btnStergeObiectiv.setVisibility(View.GONE);
                textObiectivSelectat.setVisibility(View.GONE);
                DateLivrare.getInstance().setIdObiectiv("");
                DateLivrare.getInstance().setAdresaObiectiv(false);

            }
        });

    }

    private void setListenerCheckObsSofer() {
        checkObsSofer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    txtObservatii.setVisibility(View.VISIBLE);
                    checkObsSofer.setText("Da");
                    txtObservatii.setFocusableInTouchMode(true);
                    txtObservatii.requestFocus();
                } else {
                    checkObsSofer.setText("Nu");
                    txtObservatii.setText("");
                    txtObservatii.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void addListenerDataLivrare() {
        btnDataLivrare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateLivrareFiliala();
            }
        });

    }

    private void getDateLivrareFiliala() {

        String tipTransp = UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString());

        if (tipTransp.equals("TRAP")) {

            if (!isAdresaCorecta() || DateLivrare.getInstance().getCoordonateAdresa() == null) {
                Toast.makeText(getApplicationContext(), "Completati adresa corect sau pozitionati adresa pe harta.", Toast.LENGTH_LONG).show();
                return;
            }

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("coords", DateLivrare.getInstance().getCoordonateAdresa().latitude + "," + DateLivrare.getInstance().getCoordonateAdresa().longitude);
            operatiiAdresa.getZileLivrareFiliala(params);
        } else {
            afisDateLivrareDialog(UtilsDates.getZileLivrare().toArray(new String[0]));
        }

    }

    private void afisZileLivrare(String result) {

        String[] arrayDate;
        if (result != null && !result.trim().isEmpty() && !result.trim().equals("[]"))
            arrayDate = result.replace("[", "").replace("]", "").replace("\"", "").split((","));
        else
            arrayDate = UtilsDates.getZileLivrare().toArray(new String[0]);

        afisDateLivrareDialog(arrayDate);

    }


    private void afisDateLivrareDialog(String[] dateLivrare) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", new Locale("ro"));

        try {
            Date minDate = formatter.parse(dateLivrare[0]);
            Date maxDate = formatter.parse(dateLivrare[dateLivrare.length - 1]);

            Locale.setDefault(new Locale("ro"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(minDate);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            SelectDateDialog datePickerDialog = new SelectDateDialog(SelectAdrLivrCmdGed.this, datePickerListener, year, month, day);
            datePickerDialog.setTitle("Data livrare");
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
            datePickerDialog.show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Eroare afisare date livrare.", Toast.LENGTH_LONG).show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            if (view.isShown()) {

                validateDataLivrare(selectedYear, selectedMonth, selectedDay);

            }

        }
    };

    private void validateDataLivrare(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

        Calendar calendarNow = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar
                .getInstance().get(Calendar.DAY_OF_MONTH));

        int dayLivrare = calendar.get(Calendar.DAY_OF_WEEK);

        int dayNow = calendarNow.get(Calendar.DAY_OF_WEEK);

        String tipTransp = UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString());

        if (tipTransp.toLowerCase().contains("trap")) {
            if ((dayNow == 5 || dayNow == 6) && dayLivrare == 6) {
                showDialogLivrareSambata(calendar);
                setDataLivrare(calendar);
            } else {
                if (calendar.getTime().getTime() == calendarNow.getTime().getTime())
                    showDialogLivrareAstazi(calendar);
                else
                    setDataLivrare(calendar);
            }
        } else
            setDataLivrare(calendar);
    }

    private void showDialogLivrareAstazi(final Calendar dataLivrare) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Atentie!");
        builder.setMessage("Ai solicitat livrare in cursul zilei de azi! Sigur este corect?");
        builder.setCancelable(false);
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                setDataLivrare(dataLivrare);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                btnDataLivrare.performClick();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setDataLivrare(Calendar dataLivrare) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");

        StatusIntervalLivrare statusInterval = UtilsDates.getStatusIntervalLivrare(dataLivrare.getTime());

        if (CreareComandaGed.tipComanda.equals("S") && CreareComandaGed.rezervStoc) {

            int dateDiff = UtilsDates.dateDiffinDays(dataLivrare.getTime());

            if (dateDiff > Constants.NR_ZILE_CMD_SIM_REZ_STOC) {
                Toast.makeText(getApplicationContext(),
                        "Livrarea trebuie sa se faca in cel mult " + Constants.NR_ZILE_CMD_SIM_REZ_STOC + " zile de la data curenta.", Toast.LENGTH_LONG)
                        .show();
            } else {
                textDataLivrare.setText(displayFormat.format(dataLivrare.getTime()));
                DateLivrare.getInstance().setDataLivrare(displayFormat.format(dataLivrare.getTime()));
            }

        } else {
            if (statusInterval.isValid()) {
                textDataLivrare.setText(displayFormat.format(dataLivrare.getTime()));
                DateLivrare.getInstance().setDataLivrare(displayFormat.format(dataLivrare.getTime()));
            } else
                Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void showDialogLivrareSambata(final Calendar dataLivrare) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Atentie!");
        builder.setMessage("Clientul are program de lucru si sambata?");
        builder.setCancelable(false);
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DateLivrare.getInstance().setLivrareSambata("X");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateLivrare.getInstance().setLivrareSambata("-");
                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addListenerClientLaRaft() {

        chkbClientLaRaft.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    chkbClientLaRaft.setText("Da");
                } else {
                    chkbClientLaRaft.setText("Nu");
                }

                DateLivrare.getInstance().setClientRaft(isChecked);
            }

        });

    }

    private void setupListAdreseLayout() {

        layoutAdrOras.setVisibility(View.GONE);
        layoutAdrStrada.setVisibility(View.GONE);

        layoutHeaderAdrese.setVisibility(View.VISIBLE);
        layoutListAdrese.setVisibility(View.VISIBLE);

        setListenerRadioLista();

        setListenerRadioText();

        spinnerAdreseLivrare = (Spinner) findViewById(R.id.spinnerAdreseLivrare);
        getAdreseLivrareClient();
        setSpinnerAdreseItemSelectedListener();


    }


    private void setSpinnerAdreseItemSelectedListener() {
        spinnerAdreseLivrare.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DateLivrare.getInstance().setCodJudet(listAdrese.get(arg2).getCodJudet());
                DateLivrare.getInstance().setOras(listAdrese.get(arg2).getOras());
                DateLivrare.getInstance().setStrada(listAdrese.get(arg2).getOras());

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void getAdreseLivrareClient() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codClient", CreareComandaGed.codClientVar);
        operatiiClient.getAdreseLivrareClient(params);

    }

    private void setListenerRadioLista() {
        radioListAdrese.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (radioListAdrese.isChecked()) {
                    layoutListAdrese.setVisibility(View.VISIBLE);
                    layoutAdrOras.setVisibility(View.GONE);
                    layoutAdrStrada.setVisibility(View.GONE);
                }

            }
        });
    }

    private void setListenerRadioText() {
        radioTextAdrese.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (radioTextAdrese.isChecked()) {
                    layoutListAdrese.setVisibility(View.GONE);
                    layoutAdrOras.setVisibility(View.VISIBLE);
                    layoutAdrStrada.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    private void setupSpinnerIndoire() {

        String[] indoireValues = {"Tip prelucrare fier-beton 6 m", "TAIERE", "INDOIRE"};

        ArrayAdapter<String> adapterIndoire = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, indoireValues);
        adapterIndoire.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndoire.setAdapter(adapterIndoire);

    }

    private void setupSpinnerDebitare() {

        String[] debitareValues = {"Tip prelucrare lemn", "DEBITARE"};

        ArrayAdapter<String> adapterDebitare = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, debitareValues);
        adapterDebitare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDebitare.setAdapter(adapterDebitare);

    }

    private void addAdresaLivrare() {

        radioAdresaSediu = (RadioButton) findViewById(R.id.radioAdresaSediu);
        radioAltaAdresa = (RadioButton) findViewById(R.id.radioAltaAdresa);
        radioAdresaObiectiv = (RadioButton) findViewById(R.id.radioAdresaObiectiv);

        TextView labelAdresa2 = (TextView) findViewById(R.id.labelAdresa2);

        if (DateLivrare.getInstance().getTipPersClient().equals("PF"))
            labelAdresa2.setText("Adresa de facturare");
        else
            labelAdresa2.setText("Adresa sediu social");

        addListenerRadioAdresaSediu();
        addListenerRadioAltaAdresa();
        addListenerRadioAdresaObiectiv();

        layoutAdrLivrare1 = (LinearLayout) findViewById(R.id.layoutAdrLivrare1);
        layoutAdrLivrare2 = (LinearLayout) findViewById(R.id.layoutAdrLivrare2);

        spinnerJudetLivrare = (Spinner) findViewById(R.id.spinnerJudetLivrare);
        spinnerJudetLivrare.setOnItemSelectedListener(new regionLivrareSelectedListener());

        layoutAdrLivrare1.setVisibility(View.VISIBLE);
        layoutAdrLivrare2.setVisibility(View.VISIBLE);
        addJudeteFacturare();

        if (!DateLivrare.getInstance().getOrasD().trim().isEmpty()) {
            radioAltaAdresa.setChecked(true);
            DateLivrare.getInstance().setAltaAdresa(true);
            textLocalitateLivrare.setText(DateLivrare.getInstance().getOrasD());
            textStradaLivrare.setText(DateLivrare.getInstance().getAdresaD());
            setJudetLivrare();
        } else {
            radioAdresaSediu.setChecked(true);
        }

    }

    private void setJudetLivrare() {

        if (DateLivrare.getInstance().getCodJudetD() != null && !DateLivrare.getInstance().getCodJudetD().trim().isEmpty()) {

            adapterJudeteLivrare = new SimpleAdapter(this, listJudeteLivrare, R.layout.rowlayoutjudete, new String[]{"numeJudet", "codJudet"}, new int[]{
                    R.id.textNumeJudet, R.id.textCodJudet});

            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("numeJudet", UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudetD()));
            temp.put("codJudet", DateLivrare.getInstance().getCodJudetD());
            listJudeteLivrare.add(temp);

            spinnerJudetLivrare.setAdapter(adapterJudeteLivrare);

            for (int i = 0; i < spinnerJudetLivrare.getAdapter().getCount(); i++) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> artMap = (HashMap<String, String>) spinnerJudetLivrare.getAdapter().getItem(i);
                if (artMap.get("codJudet").equals(DateLivrare.getInstance().getCodJudetD())) {
                    spinnerJudetLivrare.setSelection(i);
                    break;
                }

            }

        }

    }

    private void addJudeteFacturare() {

        ArrayList<HashMap<String, String>> listJudeteFacturare = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp;
        int i;

        int nrJud = 0;
        for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {

            temp = new HashMap<String, String>();
            temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
            temp.put("codJudet", UtilsGeneral.codJudete[i]);
            listJudeteFacturare.add(temp);

        }

        listJudeteLivrare = new ArrayList<HashMap<String, String>>(listJudeteFacturare);

        adapterJudeteLivrare = new SimpleAdapter(this, listJudeteLivrare, R.layout.rowlayoutjudete, new String[]{"numeJudet", "codJudet"}, new int[]{
                R.id.textNumeJudet, R.id.textCodJudet});

        spinnerJudetLivrare.setAdapter(adapterJudeteLivrare);
        spinnerJudetLivrare.setSelection(0);

        textStradaLivrare.setText("");


    }

    private void addJudetLivrare() {

        listJudeteLivrare = new ArrayList<HashMap<String, String>>(listJudete);

        adapterJudeteLivrare = new SimpleAdapter(this, listJudeteLivrare, R.layout.rowlayoutjudete, new String[]{"numeJudet", "codJudet"}, new int[]{
                R.id.textNumeJudet, R.id.textCodJudet});

        spinnerJudetLivrare.setAdapter(adapterJudeteLivrare);
        spinnerJudetLivrare.setSelection(0);

        textStradaLivrare.setText("");

    }

    private void addListenerRadioAltaAdresa() {

        radioAltaAdresa.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    layoutAdrLivrare1.setVisibility(View.VISIBLE);
                    layoutAdrLivrare2.setVisibility(View.VISIBLE);
                    DateLivrare.getInstance().setAltaAdresa(true);
                    addJudetLivrare();

                }

            }
        });


    }

    private void addListenerRadioAdresaSediu() {

        radioAdresaSediu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1) {

                    layoutAdrLivrare1.setVisibility(View.GONE);
                    layoutAdrLivrare2.setVisibility(View.GONE);
                    DateLivrare.getInstance().setCodJudetD(null);
                    DateLivrare.getInstance().setOrasD(null);
                    DateLivrare.getInstance().setAdresaD(null);
                    DateLivrare.getInstance().setAltaAdresa(false);

                }

            }
        });

    }

    private void addListenerRadioAdresaObiectiv() {
        radioAdresaObiectiv.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    layoutAdrLivrare1.setVisibility(View.GONE);
                    layoutAdrLivrare2.setVisibility(View.GONE);

                    DateLivrare.getInstance().setAdresaObiectiv(true);
                    DateLivrare.getInstance().setAltaAdresa(true);

                } else {
                    DateLivrare.getInstance().setAdresaObiectiv(false);

                }

            }
        });
    }

    private void performGetJudete() {

        if (isAdresaLivrareTCLI)
            getJudeteFilialaLivrare();
        else {
            fillJudeteClient(EnumJudete.getRegionCodes());
            addAdresaLivrare();
        }

    }

    private boolean isComandaClp() {
        return DateLivrare.getInstance().getCodFilialaCLP() != null && DateLivrare.getInstance().getCodFilialaCLP().length() == 4;
    }

    private boolean isComandaDl() {
        return DateLivrare.getInstance().getFurnizorComanda() != null && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().isEmpty()
                && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().length() > 4;
    }

    private void fillJudeteClient(String arrayJudete) {

        HashMap<String, String> temp;
        String numeJudSel = "";
        int i;
        temp = new HashMap<String, String>();
        temp.put("numeJudet", "Selectati judetul");
        temp.put("codJudet", "");
        listJudete.add(temp);

        int nrJud = 0;
        for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {

            if (arrayJudete.contains(UtilsGeneral.codJudete[i])) {
                temp = new HashMap<String, String>();
                temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
                temp.put("codJudet", UtilsGeneral.codJudete[i]);
                listJudete.add(temp);

                nrJud++;

                if (DateLivrare.getInstance().getCodJudet().equals(UtilsGeneral.codJudete[i])) {
                    posJudetSel = nrJud;
                    numeJudSel = UtilsGeneral.numeJudete[i];
                }
            }

        }

        spinnerJudet.setAdapter(adapterJudete);

        if (posJudetSel > 0) {
            DateLivrare.getInstance().setNumeJudet(numeJudSel);
            spinnerJudet.setSelection(posJudetSel);
        }

    }

    private void addListenerTermenPlata() {
        spinnerTermenPlata.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DateLivrare.getInstance().setTermenPlata(spinnerTermenPlata.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void addListenerTipPlata() {

        spinnerPlata.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                String rawTipPlataStr = spinnerPlata.getSelectedItem().toString();
                DateLivrare.getInstance().setTipPlata(rawTipPlataStr.substring(0, rawTipPlataStr.indexOf("-") - 1).trim());
                ((TextView) findViewById(R.id.tipPlataContract)).setVisibility(View.INVISIBLE);
                spinnerTermenPlata.setEnabled(true);

                if (spinnerPlata.getSelectedItem().toString().substring(0, 1).equals("N") || spinnerPlata.getSelectedItem().toString().substring(0, 1).equals("R")) {
                    spinnerTermenPlata.setSelection(0);
                    spinnerTermenPlata.setEnabled(false);
                }

                if (spinnerPlata.getSelectedItem().toString().substring(0, 1).equals("O")) {
                    spinnerTermenPlata.setSelection(0);
                    spinnerTermenPlata.setEnabled(false);
                }

                if (spinnerPlata.getSelectedItem().toString().substring(0, 1).equals("C")) {
                    spinnerTermenPlata.setSelection(0);
                    spinnerTermenPlata.setEnabled(false);
                }

                if (spinnerPlata.getSelectedItem().toString().substring(0, 1).equals("L")) {
                    spinnerTermenPlata.setSelection(adapterTermenPlata.getCount() - 1);
                    ((TextView) findViewById(R.id.tipPlataContract)).setVisibility(View.VISIBLE);
                }


                if (pos == 0 || pos == 1 || pos == 2 || pos == 3) {
                    if (spinnerTermenPlata != null)
                        spinnerTermenPlata.setVisibility(View.VISIBLE);
                }


                if (rawTipPlataStr.toLowerCase().contains("numerar") || rawTipPlataStr.toLowerCase().contains("ramburs")) {
                    checkAviz.setChecked(false);
                    checkAviz.setEnabled(false);
                } else
                    checkAviz.setEnabled(true);

                setFilialaPlataVisibility();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addListenerTipTransport() {
        spinnerTransp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos == 1) {
                    DateLivrare.getInstance().setValTransport(0);
                    DateLivrare.getInstance().setValTransportSAP(0);
                    DateLivrare.getInstance().setMasinaMacara(false);


                    if (spinnerPlata.getSelectedItem().toString().contains("E1"))
                        spinnerPlata.setSelection(0);


                }

                String tipTranspSel = UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString());
                setTipTranspOpt(tipTranspSel);

                setFilialaPlataVisibility();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setTipTranspOpt(String tipTransp) {

        if (!DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) && !DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE))
            return;

        if (isAdresaLivrareTCLI)
            return;


        if (tipTransp.equals("TCLI")) {
            layoutAdr1.setVisibility(View.GONE);
            layoutAdr2.setVisibility(View.GONE);
            layoutAdr21.setVisibility(View.GONE);
            layoutAdr3.setVisibility(View.GONE);

            if (ModificareComanda.selectedCmd.equals("")) {
                ((LinearLayout) findViewById(R.id.layoutFilLivrare)).setVisibility(View.VISIBLE);
                operatiiAdresa.getFilialeComenziTCLI();
            }

        } else {
            DateLivrare.getInstance().setFilialaLivrareTCLI(null);
            ((LinearLayout) findViewById(R.id.layoutFilLivrare)).setVisibility(View.GONE);
            layoutAdr1.setVisibility(View.VISIBLE);
            layoutAdr2.setVisibility(View.VISIBLE);
            layoutAdr21.setVisibility(View.VISIBLE);
            layoutAdr3.setVisibility(View.VISIBLE);

            if (!ModificareComanda.selectedCmd.equals("") && DateLivrare.getInstance().getTransport().equals("TCLI")) {
                performGetJudete();
                clearAdresaLivrare();
            }
        }

    }

    private void setSpinnerFilialeTCLIListener() {
        spinnerFilialeTCLI.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (arg2 > 0) {

                    BeanFilialaLivrare beanFilialaLivrare = (BeanFilialaLivrare) spinnerFilialeTCLI.getSelectedItem();
                    String filialaLivrareTCLI = beanFilialaLivrare.getUnitLog();
                    DateLivrare.getInstance().setFilialaLivrareTCLI(beanFilialaLivrare);


                    CreareComandaGed.filialaLivrareMathaus = filialaLivrareTCLI;
                    CreareComandaGed.filialeArondateMathaus = filialaLivrareTCLI;


                    if (!UtilsGeneral.isAceeasiFiliala(filialaLivrareTCLI, UserInfo.getInstance().getUnitLog())) {
                        DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_LIVRARE);
                        DateLivrare.getInstance().setCodFilialaCLP(filialaLivrareTCLI);
                    } else {
                        DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_VANZARE);
                        DateLivrare.getInstance().setCodFilialaCLP("");
                    }

                    HashMap<String, String> params = new HashMap<>();
                    params.put("filiala", filialaLivrareTCLI);
                    operatiiAdresa.getAdresaFiliala(params);

                    setFilialaPlataVisibility();
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void setFilialaLivrareTCLI() {

        if (spinnerFilialeTCLI.getAdapter() == null)
            return;

        if (DateLivrare.getInstance().getFilialaLivrareTCLI() == null)
            return;

        String numeLivrareTCLI = DateLivrare.getInstance().getFilialaLivrareTCLI().getNumeFiliala();

        for (int ii = 0; ii < spinnerFilialeTCLI.getAdapter().getCount(); ii++) {
            if (spinnerFilialeTCLI.getItemAtPosition(ii).toString().equals(numeLivrareTCLI)) {
                spinnerFilialeTCLI.setSelection(ii);
                break;
            }
        }

        if (UtilsComenzi.comandaAreArticole("20")) {
            spinnerTransp.setEnabled(false);
            spinnerFilialeTCLI.setEnabled(false);
        }
    }

    private void setAdresalivrareFiliala(String adresaFiliala) {
        String[] tokenAdresa = adresaFiliala.split("#");

        DateLivrare.getInstance().setNumeJudet(tokenAdresa[3]);
        DateLivrare.getInstance().setCodJudet(tokenAdresa[2]);
        DateLivrare.getInstance().setOras(tokenAdresa[1]);
        DateLivrare.getInstance().setStrada(tokenAdresa[0]);
        DateLivrare.getInstance().setCoordonateAdresa(new LatLng(Double.valueOf(tokenAdresa[4]), Double.valueOf(tokenAdresa[5])));

    }


    private void setFilialaPlataVisibility() {

        String tipTranspSel = UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString());
        String tipPlata = spinnerPlata.getSelectedItem().toString().split("-")[0].trim();

        if ((DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) || isComandaClp()) && tipTranspSel.equals("TCLI") && tipPlata.equals("N")) {
            ((LinearLayout) findViewById(R.id.layoutFilialaPlata)).setVisibility(View.VISIBLE);
            ((RadioButton) findViewById(R.id.radioPlataFilialaAg)).setText(EnumFiliale.getNumeFiliala(UserInfo.getInstance().getUnitLog()));
            ((RadioButton) findViewById(R.id.radioPlataFilialaLivrare)).setText(EnumFiliale.getNumeFiliala(UtilsGeneral.getUnitLogDistrib(DateLivrare.getInstance().getCodFilialaCLP())));

            String localFilialaPlata = DateLivrare.getInstance().getFilialaPlata() != null ? DateLivrare.getInstance().getFilialaPlata() : "";

            if (!localFilialaPlata.trim().isEmpty()) {
                if (localFilialaPlata.equals(DateLivrare.getInstance().getCodFilialaCLP()))
                    ((RadioButton) findViewById(R.id.radioPlataFilialaLivrare)).setChecked(true);
                else
                    ((RadioButton) findViewById(R.id.radioPlataFilialaAg)).setChecked(true);
            }
        } else
            ((LinearLayout) findViewById(R.id.layoutFilialaPlata)).setVisibility(View.GONE);

    }

    private void getJudeteFilialaLivrare() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("filiala", DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog());

        AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
        call.getCallResultsSyncActivity();

    }

    private void setListenerBtnCodPostal() {
        btnCodPostal.setOnClickListener(v -> {

            if (textLocalitate.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Completati localitatea.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (textStrada.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Completati strada.", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("codJudet", DateLivrare.getInstance().getCodJudet());
            params.put("localitate", textLocalitate.getText().toString().trim());
            params.put("strada", textStrada.getText().toString().trim());
            operatiiAdresa.getCoduriPostale(params);
        });

    }

    private void afisCoduriPostaleDialog(String result) {

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);

        List<BeanCodPostal> listCoduri = operatiiAdresa.deserializeCoduriPostale(result);

        if (listCoduri.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Nu exista inregistrari.", Toast.LENGTH_LONG).show();
            return;
        }

        CoduriPostaleDialog coduriPostaleDialog = new CoduriPostaleDialog(this, listCoduri);
        coduriPostaleDialog.getWindow().setLayout(width, height);
        coduriPostaleDialog.setCodPostalListener(this);
        coduriPostaleDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveAdrLivrBtn.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void dataLivrareSelected(String dataLivrare) {
        if (dataLivrare == null)
            return;

        String[] arrayData = dataLivrare.split("\\.");

        if (arrayData.length != 3)
            return;

        int selectedYear = Integer.parseInt(arrayData[2]);
        int selectedMonth = Integer.parseInt(arrayData[1]) - 1;
        int selectedDay = Integer.parseInt(arrayData[0]);

        validateDataLivrare(selectedYear, selectedMonth, selectedDay);

    }

    @Override
    public void codPostalSelected(String codPostal) {
        ((EditText) findViewById(R.id.textCodPostal)).setText(codPostal);
    }

    private class regionSelectedListener implements OnItemSelectedListener {

        @SuppressWarnings("unchecked")
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            if (spinnerJudet.getAdapter().getCount() == 1)
                return;

            if (spinnerJudet.getSelectedItemPosition() > 0) {

                HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
                DateLivrare.getInstance().setNumeJudet(tempMap.get("numeJudet"));
                DateLivrare.getInstance().setCodJudet(tempMap.get("codJudet"));

                HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
                params.put("codJudet", DateLivrare.getInstance().getCodJudet());
                operatiiAdresa.getAdreseJudet(params, EnumLocalitate.LOCALITATE_SEDIU);

            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            return;
        }

    }

    private void populateListLocSediu(BeanAdreseJudet listAdrese) {

        listAdreseJudet = listAdrese;

        textLocalitate.setVisibility(View.VISIBLE);
        textLocalitate.setText(DateLivrare.getInstance().getOras());

        String[] arrayLocalitati = listAdrese.getListStringLocalitati().toArray(new String[listAdrese.getListStringLocalitati().size()]);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

        textLocalitate.setThreshold(0);
        textLocalitate.setAdapter(adapterLoc);
        listLocalitatiSediu = listAdrese.getListStringLocalitati();
        setListenerTextLocalitate();

        String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
        ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

        textStrada.setThreshold(0);
        textStrada.setAdapter(adapterStrazi);
        textStrada.setText(UtilsAddress.getStreetFromAddress(DateLivrare.getInstance().getStrada()));
        textNrStr.setText(UtilsAddress.getStreetNumber(DateLivrare.getInstance().getStrada()));

        setListenerTextStrada();
        getFilialaLivrareMathaus(DateLivrare.getInstance().getCodJudet());

    }

    private void getFilialaLivrareMathaus(String codJudet) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codJudet", codJudet);
        operatiiAdresa.getFilialaLivrareMathaus(params);
    }

    private void setListenerTextLocalitate() {

        textLocalitate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DateLivrare.getInstance().setOras(textLocalitate.getText().toString().trim());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textLocalitate.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textLocalitate.setText(textLocalitate.getText().toString().trim().toUpperCase());
                    verificaLocalitate("SEDIU");

                }

            }
        });

    }

    private boolean verificaLocalitate(String tipLocalitate) {
        boolean locExist = false;
        List<String> listLocalitati = new ArrayList<String>();
        String localitateCurenta = "";
        String numeJudet = "";
        EditText textLoc = null;

        if (tipLocalitate.equals("SEDIU")) {
            textLoc = textLocalitate;
            localitateCurenta = textLoc.getText().toString().trim();
            listLocalitati = listLocalitatiSediu;

            @SuppressWarnings("unchecked")
            HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
            if (tempMap != null)
                numeJudet = tempMap.get("numeJudet");

        } else {
            textLoc = textLocalitateLivrare;
            localitateCurenta = textLoc.getText().toString().trim();
            listLocalitati = listLocalitatiLivrare;

            @SuppressWarnings("unchecked")
            HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudetLivrare.getSelectedItem();
            if (tempMap != null)
                numeJudet = tempMap.get("numeJudet");
        }

        if (listLocalitati != null)
            for (String localitate : listLocalitati) {
                if (localitate.trim().equalsIgnoreCase(localitateCurenta)) {
                    locExist = true;
                    break;
                }

            }
        else
            locExist = true;

        //nu se mai valideaza
        if (tipLocalitate.equals("LIVRARE"))
            locExist = true;

        if (!locExist && !localitateCurenta.isEmpty()) {
            String alert = "Localitatea " + localitateCurenta + " nu exista in judetul " + numeJudet + ". Completati alta localitate.";
            Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();

            textLoc.setText("");
            textLoc.setFocusableInTouchMode(true);

        }

        return locExist;
    }

    private void setListenerTextStrada() {

        textStrada.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DateLivrare.getInstance().setStrada(textStrada.getText().toString().trim());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void populateListLocLivrare(BeanAdreseJudet listAdrese) {

        listAlteAdrese = listAdrese;

        textLocalitateLivrare.setVisibility(View.VISIBLE);
        textLocalitateLivrare.setText(DateLivrare.getInstance().getOrasD().trim());

        String[] arrayLocalitati = listAdrese.getListStringLocalitati().toArray(new String[listAdrese.getListStringLocalitati().size()]);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

        textLocalitateLivrare.setThreshold(0);
        textLocalitateLivrare.setAdapter(adapterLoc);
        listLocalitatiLivrare = listAdrese.getListStringLocalitati();
        setListenerTextLocalitateLivrare();

        String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
        ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

        textStradaLivrare.setVisibility(View.VISIBLE);
        textStradaLivrare.setThreshold(0);
        textStradaLivrare.setAdapter(adapterStrazi);
        setListenerTextStradaLivrare();

    }

    private void setListenerTextLocalitateLivrare() {

        textLocalitateLivrare.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DateLivrare.getInstance().setOrasD(textLocalitateLivrare.getText().toString().trim());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textLocalitateLivrare.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textLocalitateLivrare.setText(textLocalitateLivrare.getText().toString().trim().toUpperCase());
                    verificaLocalitate("LIVRARE");

                }

            }
        });

    }

    private void setListenerTextStradaLivrare() {

        textStradaLivrare.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DateLivrare.getInstance().setAdresaD(textStradaLivrare.getText().toString().trim());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public class regionLivrareSelectedListener implements OnItemSelectedListener {
        @SuppressWarnings("unchecked")
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

            if (spinnerJudetLivrare.getSelectedItemPosition() > 0) {

                HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudetLivrare.getSelectedItem();
                codJudetLivrare = tempMap.get("codJudet");

                DateLivrare.getInstance().setCodJudetD(codJudetLivrare);

                if (!codJudetLivrare.trim().equals("")) {
                    HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
                    params.put("codJudet", DateLivrare.getInstance().getCodJudetD());

                    OperatiiAdresa operatiiAdresa1 = new OperatiiAdresaImpl(SelectAdrLivrCmdGed.this);
                    operatiiAdresa1.setOperatiiAdresaListener(SelectAdrLivrCmdGed.this);

                    operatiiAdresa1.getAdreseJudet(params, EnumLocalitate.LOCALITATE_LIVRARE);

                }
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            return;
        }
    }

    private boolean existaArticole() {
        return ListaArticoleComandaGed.getInstance().getListArticoleComanda() != null
                && ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0 && !DateLivrare.getInstance().getTransport().equals("TCLI");
    }

    public void addListenerSaveAdr() {
        saveAdrLivrBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                valideazaAdresaLivrare();

            }
        });

    }

    private void valideazaDateLivrare() {
        String adresa = "";
        String strada = "";
        String pers = "";
        String telefon = "";
        String observatii = "", obsPlata = " ", strMailAddr = " ";

        String nrStrada = "";
        DateLivrare dateLivrareInstance = DateLivrare.getInstance();

        if (textNrStr.getText().toString().trim().length() > 0)
            nrStrada = " NR " + textNrStr.getText().toString().trim();

        if (((LinearLayout) findViewById(R.id.layoutFilLivrare)).getVisibility() == View.VISIBLE) {

            if (spinnerFilialeTCLI.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Selectati filiala care livreaza.", Toast.LENGTH_LONG).show();
                return;
            }

        } else {
            strada = textStrada.getText().toString().trim() + " " + nrStrada;
            dateLivrareInstance.setStrada(strada);
        }

        dateLivrareInstance.setAdrLivrNoua(true);
        dateLivrareInstance.setAddrNumber(" ");

        pers = txtPers.getText().toString().trim();
        telefon = txtTel.getText().toString().trim();
        observatii = txtObservatii.getText().toString().trim();
        obsPlata = txtObsPlata.getText().toString().trim();
        strMailAddr = textMail.getText().toString().trim();

        if (observatii.trim().length() == 0)
            observatii = " ";

        if (obsPlata.trim().length() == 0)
            obsPlata = " ";

        if (strMailAddr.trim().length() == 0)
            strMailAddr = " ";

        if (!(layoutListAdrese.getVisibility() == View.VISIBLE) && !(DateLivrare.getInstance().isAltaAdresa()) && (((LinearLayout) findViewById(R.id.layoutFilLivrare)).getVisibility() == View.GONE)) {

            verificaLocalitate("SEDIU");

            if (dateLivrareInstance.getCodJudet().equals("")) {
                Toast.makeText(getApplicationContext(), "Selectati judetul!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (DateLivrare.getInstance().getOras().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Selectati localitatea!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strada.trim().equals("") && !hasCoordinates()) {
                Toast.makeText(getApplicationContext(), "Completati strada sau pozitionati adresa pe harta!", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()).toLowerCase().contains("tcli") && DateLivrare.getInstance().getTipPersClient().equals("PF")
                && !DateLivrare.getInstance().isFacturaCmd()) {
            if (pers.equals(""))
                pers = " ";

            if (telefon.equals(""))
                telefon = " ";

        } else {
            if (pers.equals("")) {
                Toast.makeText(getApplicationContext(), "Completati persoana de contact.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (telefon.equals("")) {
                Toast.makeText(getApplicationContext(), "Completati nr. de telefon.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (telefon.length() != 10) {
                Toast.makeText(getApplicationContext(), "Nr. de telefon invalid.", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (DateLivrare.getInstance().getDataLivrare().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Selectati data livrare!", Toast.LENGTH_LONG).show();
            return;
        }


        if (((LinearLayout) findViewById(R.id.layoutFilialaPlata)).getVisibility() == View.VISIBLE) {
            if (((RadioButton) findViewById(R.id.radioPlataFilialaAg)).isChecked())
                DateLivrare.getInstance().setFilialaPlata(UserInfo.getInstance().getUnitLog());
            else if (((RadioButton) findViewById(R.id.radioPlataFilialaLivrare)).isChecked())
                DateLivrare.getInstance().setFilialaPlata(DateLivrare.getInstance().getCodFilialaCLP());
            else {
                Toast.makeText(getApplicationContext(), "Selectati filiala in care se plateste.", Toast.LENGTH_LONG).show();
                return;
            }
        } else
            DateLivrare.getInstance().setFilialaPlata("");


        if (radioAltaAdresa.isChecked()) {

            verificaLocalitate("LIVRARE");

            if (DateLivrare.getInstance().getOrasD().trim().equals("")) {

                if (DateLivrare.getInstance().getTipPersClient().equals("PF"))
                    Toast.makeText(getApplicationContext(), "Completati adresa de facturare.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Completati adresa sediului social.", Toast.LENGTH_SHORT).show();

                return;
            }

        }

        if (spinnerProgramLivrare.getSelectedItemPosition() == 0 && UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()).toLowerCase().contains("trap")) {
            Toast.makeText(getApplicationContext(), "Selectati perioada de livrare", Toast.LENGTH_SHORT).show();
            return;
        }

        dateLivrareInstance.setPersContact(pers);
        dateLivrareInstance.setNrTel(telefon);

        dateLivrareInstance.setTransport(UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()));

        if (dateLivrareInstance.getTransport().equals("TCLI") && dateLivrareInstance.getTipPlata().equals("R")) {
            Toast.makeText(getApplicationContext(), "Pentru transport TCLI nu puteti selecta metoda de plata Ramburs.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isAdresaCorecta()) {
            Toast.makeText(getApplicationContext(), "Completati adresa corect sau pozitionati adresa pe harta.", Toast.LENGTH_LONG).show();
            return;
        }

        String cantar = "NU";

        dateLivrareInstance.setCantar("NU");

        String factRed = "NU";
        dateLivrareInstance.setRedSeparat(" ");

        String rawTipPlataStr = spinnerPlata.getSelectedItem().toString();

        if (rawTipPlataStr.startsWith("O") && (!strMailAddr.contains("@") || !strMailAddr.contains("."))) {
            Toast.makeText(getApplicationContext(), "Completati adresa de e-mail.", Toast.LENGTH_LONG).show();
            return;
        }

        dateLivrareInstance.setTipPlata(rawTipPlataStr.substring(0, rawTipPlataStr.indexOf("-") - 1).trim());

        if (dateLivrareInstance.getTransport().equals("TCLI") && dateLivrareInstance.getTipPlata().equals("E1")) {
            Toast.makeText(getApplicationContext(), "Pentru transport TCLI nu puteti selecta metoda de plata Numerar sofer.", Toast.LENGTH_LONG).show();
            return;
        }

        adresa = dateLivrareInstance.getNumeJudet() + " " + dateLivrareInstance.getOras() + " " + dateLivrareInstance.getStrada();

        if (layoutHeaderAdrese.getVisibility() == View.VISIBLE && radioListAdrese.isChecked()) {
            int selectedAdress = spinnerAdreseLivrare.getSelectedItemPosition();
            adresa = UtilsGeneral.getNumeJudet(listAdrese.get(selectedAdress).getCodJudet()) + " " + listAdrese.get(selectedAdress).getOras() + " "
                    + listAdrese.get(selectedAdress).getStrada();

            dateLivrareInstance.setCodJudet(listAdrese.get(selectedAdress).getCodJudet());
            dateLivrareInstance.setOras(listAdrese.get(selectedAdress).getOras());
            dateLivrareInstance.setStrada(listAdrese.get(selectedAdress).getStrada());

        }

        if (DateLivrare.getInstance().isAltaAdresa()) {

            dateLivrareInstance.setAdresaD(textStradaLivrare.getText().toString().trim());

        }

        dateLivrareInstance.setDateLivrare(adresa + "#" + pers + "#" + telefon + "#" + cantar + "#" + dateLivrareInstance.getTipPlata() + "#"
                + UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()) + "#" + factRed + "#");

        dateLivrareInstance.setTermenPlata(spinnerTermenPlata.getSelectedItem().toString());

        if (UtilsUser.isAgentOrSD() && !dateLivrareInstance.getTermenPlata().equals("C000") && dateLivrareInstance.getTipComandaGed().equals(TipCmdGed.ARTICOLE_COMANDA)) {
            Toast.makeText(getApplicationContext(), "Pentru comenzile AC/ZC trebuie sa existe un proces verbal de angajament semnat de client.", Toast.LENGTH_LONG).show();
        }

        dateLivrareInstance.setObsLivrare(observatii.replace("#", "-").replace("@", "-"));
        dateLivrareInstance.setObsPlata(obsPlata.replace("#", "-").replace("@", "-"));

        dateLivrareInstance.setMail(strMailAddr.replace("#", "-").replace("@", "~"));

        String tipDocInsot = "";
        if (checkFactura.isChecked() && checkAviz.isChecked())
            tipDocInsot = "3";
        else if (checkFactura.isChecked())
            tipDocInsot = "1";
        else if (checkAviz.isChecked())
            tipDocInsot = "2";

        dateLivrareInstance.setTipDocInsotitor(tipDocInsot);

        if (dateLivrareInstance.getTipDocInsotitor().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Selectati documentul insotitor!", Toast.LENGTH_LONG).show();
            return;
        }

        if (dateLivrareInstance.getTipDocInsotitor().contains("2") && dateLivrareInstance.getTipPlata().equals("E1")) {
            Toast.makeText(getApplicationContext(), "Pentru avizul de expeditie selectati alta metoda de plata.", Toast.LENGTH_LONG).show();
            return;
        }


        if (radioAltaAdresa.isChecked() && !DateLivrare.getInstance().isAltaAdresa()) {
            dateLivrareInstance.setDateLivrare(getAdrLivrareJSON());
        } else if (radioAdresaObiectiv.isChecked()) {
            setAdresaLivrareObiectiv();
        }


        if (spinnerIndoire.getVisibility() == View.VISIBLE && spinnerIndoire.getSelectedItemPosition() > 0) {
            dateLivrareInstance.setPrelucrare(spinnerIndoire.getSelectedItem().toString());
        } else
            dateLivrareInstance.setPrelucrare("-1");

        if (spinnerDebitare.getVisibility() == View.VISIBLE && spinnerDebitare.getSelectedItemPosition() > 0) {
            dateLivrareInstance.setPrelucrareLemn(spinnerDebitare.getSelectedItem().toString());
        } else
            dateLivrareInstance.setPrelucrareLemn("-1");

        if (spinnerMeseriasi.getSelectedItem() != null)
            dateLivrareInstance.setCodMeserias(((BeanClient) spinnerMeseriasi.getSelectedItem()).getCodClient());
        else
            dateLivrareInstance.setCodMeserias("0");

        dateLivrareInstance.setFactPaletSeparat(checkFactPaleti.isChecked());
        dateLivrareInstance.setCamionDescoperit(chkCamionDescoperit.isChecked());

        if (UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()).toLowerCase().contains("trap"))
            dateLivrareInstance.setProgramLivrare(String.valueOf(spinnerProgramLivrare.getSelectedItemPosition()));
        else
            dateLivrareInstance.setProgramLivrare("0");

        dateLivrareInstance.setCodPostal("");
        if (((LinearLayout) findViewById(R.id.layoutAdr21)).getVisibility() == View.VISIBLE)
            dateLivrareInstance.setCodPostal(((EditText) findViewById(R.id.textCodPostal)).getText().toString().trim());

        if (DateLivrare.getInstance().getCoordonateAdresa() != null)
            getDatePoligonLivrare();
        else
            finish();


    }

    private void getDatePoligonLivrare() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("coords", DateLivrare.getInstance().getCoordonateAdresa().latitude + "," + DateLivrare.getInstance().getCoordonateAdresa().longitude);
        operatiiAdresa.getDatePoligonLivrare(params);
    }

    private void setDatePoligonLivrare(String datePoligonLivrare) {
        DatePoligonLivrare poligonLivrare = operatiiAdresa.deserializePoligonLivrare(datePoligonLivrare);
        DateLivrare.getInstance().setDatePoligonLivrare(null);

        if (UtilsComenzi.isModifTCLIinTRAP(poligonLivrare)) {
            UtilsComenzi.showFilialaLivrareDialog(this, DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog());
            return;
        } else if (DateLivrare.getInstance().getTransport().equals("TRAP") && UtilsComenzi.isComandaClp()
                && !UtilsComenzi.getFilialaDistrib(DateLivrare.getInstance().getCodFilialaCLP()).equals(poligonLivrare.getFilialaPrincipala())
                && !UtilsComenzi.getFilialaDistrib(DateLivrare.getInstance().getCodFilialaCLP()).equals(poligonLivrare.getFilialaSecundara())) {
            UtilsComenzi.showFilialaLivrareDialog(this, DateLivrare.getInstance().getCodFilialaCLP());
            return;
        } else if (!UtilsComenzi.isAdresaUnitLogModifCmd(this, ulLivrareModifCmd, poligonLivrare)) {
            return;
        } else if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_AMOB)
                && !CreareComandaGed.filialaAlternativa.equals(poligonLivrare.getFilialaPrincipala())
                && !CreareComandaGed.filialaAlternativa.equals(poligonLivrare.getFilialaSecundara())) {
            UtilsComenzi.showFilialaLivrareDialog(this, CreareComandaGed.filialaAlternativa);
            return;
        } else {
            if (!poligonLivrare.getFilialaPrincipala().trim().isEmpty()) {
                CreareComandaGed.filialaLivrareMathaus = poligonLivrare.getFilialaPrincipala();
                CreareComandaGed.filialeArondateMathaus = poligonLivrare.getFilialaPrincipala();
                DateLivrare.getInstance().setDatePoligonLivrare(poligonLivrare);

                if (poligonLivrare.getLimitareTonaj().trim().isEmpty())
                    DateLivrare.getInstance().setTonaj("20");
                else {
                    DateLivrare.getInstance().setTonaj(poligonLivrare.getLimitareTonaj());
                    if (isCondInfoRestrictiiTonaj() && !poligonLivrare.isRestrictionat())
                        Toast.makeText(getApplicationContext(), "La aceasta adresa exista o limitare de tonaj de " + poligonLivrare.getLimitareTonaj() + " T.", Toast.LENGTH_LONG).show();
                }
            }

            if (DateLivrare.getInstance().getTransport().equals("TRAP") && poligonLivrare.isRestrictionat()) {
                Toast.makeText(this, Constants.ADRESA_ZONA_RESTRICTIONATA, Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

    private void setFilialeTCLI(String strFilialeTCLI) {
        List<BeanFilialaLivrare> listFilialeLivrare = operatiiAdresa.deserializeFilialeLivrare(strFilialeTCLI);

        BeanFilialaLivrare beanFilialaLivrare = new BeanFilialaLivrare();
        beanFilialaLivrare.setNumeFiliala("Selectati filiala");
        listFilialeLivrare.add(0, beanFilialaLivrare);
        AdapterFilialeLivrare adapterLivrare = new AdapterFilialeLivrare(getApplicationContext(), listFilialeLivrare);
        spinnerFilialeTCLI.setAdapter(adapterLivrare);

        setFilialaLivrareTCLI();

    }

    public boolean isCondInfoRestrictiiTonaj() {
        return !UtilsUser.isUserIP() && DateLivrare.getInstance().getTransport().equals("TRAP") && (
                DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE) ||
                        DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE) ||
                        DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE));
    }

    private boolean hasCoordinates() {
        if (DateLivrare.getInstance().getCoordonateAdresa() == null)
            return false;
        else if (DateLivrare.getInstance().getCoordonateAdresa().latitude == 0)
            return false;

        return true;
    }


    private void setAdresaLivrare(Address address) {

        textLocalitate.getText().clear();
        textStrada.getText().clear();
        textNrStr.getText().clear();

        int nrJudete = spinnerJudet.getAdapter().getCount();

        for (int j = 0; j < nrJudete; j++) {
            HashMap<String, String> artMapLivr = (HashMap<String, String>) this.adapterJudete.getItem(j);
            String numeJudet = artMapLivr.get("numeJudet").toString();

            if (address.getSector().equals(numeJudet)) {
                spinnerJudet.setSelection(j);
                break;
            }

        }

        if (address.getCity() != null && !address.getCity().isEmpty())
            textLocalitate.setText(address.getCity());

        if (address.getStreet() != null && !address.getStreet().isEmpty())
            textStrada.setText(address.getStreet());

        if (address.getNumber() != null && !address.getNumber().isEmpty())
            textNrStr.setText(address.getNumber());

    }

    private void valideazaAdresaLivrare() {

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codJudet", DateLivrare.getInstance().getCodJudet());
        params.put("localitate", DateLivrare.getInstance().getOras());

        operatiiAdresa.isAdresaValida(params, EnumLocalitate.LOCALITATE_SEDIU);

    }


    private String getAdrLivrareJSON() {
        String jsonData = "";

        HashMap<String, String> adrData = new HashMap<String, String>();
        adrData.put("codJudet", codJudetLivrare);
        adrData.put("oras", DateLivrare.getInstance().getOrasD().trim());
        adrData.put("strada", textStradaLivrare.getText().toString().trim());

        EncodeJSONData jsonAdrLivrare = new EncodeJSONData(this, adrData);

        jsonData = jsonAdrLivrare.encodeAdresaLivrareCV();

        return jsonData;

    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }


    public void onTaskComplete(String methodName, Object result) {
        if (methodName.equals(METHOD_NAME)) {
            fillJudeteClient((String) result);
        }


    }

    private void setListnerBtnPozitieAdresa() {
        btnPozitieAdresa.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();

                if (!isAdresaComplet())
                    return;


                MapAddressDialog mapDialog = new MapAddressDialog(getAddressFromFormPozitionare(), SelectAdrLivrCmdGed.this, fm);
                mapDialog.setMapListener(SelectAdrLivrCmdGed.this);
                mapDialog.show();


            }
        });
    }


    private void testMe() {

    }

    private Address getAddressFromFormPozitionare() {
        Address address = new Address();
        address.setCity(DateLivrare.getInstance().getOras());
        address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));
        return address;
    }

    private Address getAddressFromForm() {
        Address address = new Address();

        address.setCity(DateLivrare.getInstance().getOras());
        address.setStreet(DateLivrare.getInstance().getStrada());
        address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));

        return address;
    }

    private boolean isAdresaComplet() {
        if (DateLivrare.getInstance().getCodJudet().equals("") || DateLivrare.getInstance().getCodJudetD().equals("")) {
            Toast.makeText(this, "Selectati judetul", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DateLivrare.getInstance().getOras().equals("") || DateLivrare.getInstance().getOrasD().equals("")) {
            Toast.makeText(this, "Completati localitatea", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isAdresaCorecta() {
        if (DateLivrare.getInstance().getTransport().equals("TRAP") || UtilsComenzi.getSpinnerTipTransp(spinnerTransp.getSelectedItem().toString()).equals("TRAP"))
            return isAdresaGoogleOk();
        else
            return true;

    }

    private boolean isAdresaGoogleOk() {

        GeocodeAddress geoAddress = MapUtils.geocodeAddress(getAddressFromForm(), getApplicationContext());
        DateLivrare.getInstance().setCoordonateAdresa(geoAddress.getCoordinates());

        boolean isAdresaOk = geoAddress.isAdresaValida();

        if (!isAdresaOk)
            return false;

        String localitate = DateLivrare.getInstance().getOras();
        List<BeanLocalitate> listLocalitati = listAdreseJudet.getListLocalitati();

        BeanLocalitate beanLocalitate = HelperAdreseLivrare.getDateLocalitate(listLocalitati, localitate);

        if (beanLocalitate.isOras()) {
            isAdresaOk = HelperAdreseLivrare.isDistantaCentruOk(getApplicationContext(), beanLocalitate, geoAddress.getCoordinates());

        }

        return isAdresaOk;

    }

    private void fillAdreseLivrareClient(String adreseLivrare) {

        listAdrese = operatiiClient.deserializeAdreseLivrare(adreseLivrare);

        listAdreseLivrare = new ArrayList<HashMap<String, String>>();
        adapterAdreseLivrare = new SimpleAdapter(this, listAdreseLivrare, R.layout.simplerowlayout_1, new String[]{"rowText", "rowId"}, new int[]{
                R.id.textRowName, R.id.textRowId});

        if (listAdrese.size() > 0) {

            HashMap<String, String> temp;

            String strAdresa = "";
            for (int i = 0; i < listAdrese.size(); i++) {
                temp = new HashMap<String, String>();

                strAdresa = UtilsGeneral.getNumeJudet(listAdrese.get(i).getCodJudet()) + "; " + listAdrese.get(i).getOras() + "; "
                        + listAdrese.get(i).getStrada() + "; " + listAdrese.get(i).getNrStrada() + ";";

                temp.put("rowText", strAdresa);
                temp.put("rowId", listAdrese.get(i).getCodAdresa());
                listAdreseLivrare.add(temp);

            }

            spinnerAdreseLivrare.setAdapter(adapterAdreseLivrare);
            adapterAdreseLivrare.notifyDataSetChanged();

        }

    }

    private void fillSpinnerMeseriasi(String result) {
        List<BeanClient> listMeseriasi = operatiiClient.deserializeListClienti(result);

        if (listMeseriasi.isEmpty()) {
            ((LinearLayout) findViewById(R.id.layoutMeseriasi)).setVisibility(View.GONE);
            return;
        } else
            ((LinearLayout) findViewById(R.id.layoutMeseriasi)).setVisibility(View.VISIBLE);

        BeanClient client = new BeanClient();
        client.setNumeClient("Selectati un meserias");
        client.setCodClient("0");
        listMeseriasi.add(0, client);

        ArrayAdapter<BeanClient> spinnerArrayAdapter = new ArrayAdapter<BeanClient>(this, android.R.layout.simple_spinner_item, listMeseriasi);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeseriasi.setAdapter(spinnerArrayAdapter);

        if (!DateLivrare.getInstance().getCodMeserias().isEmpty()) {

            for (int i = 0; i < spinnerMeseriasi.getAdapter().getCount(); i++)
                if (((BeanClient) spinnerMeseriasi.getAdapter().getItem(i)).getCodClient().equals(DateLivrare.getInstance().getCodMeserias()))
                    spinnerMeseriasi.setSelection(i);
        }

    }

    public void operationComplete(EnumClienti methodName, Object result) {

        switch (methodName) {
            case GET_ADRESE_LIVRARE:
                fillAdreseLivrareClient((String) result);
                break;
            case GET_MESERIASI:
                fillSpinnerMeseriasi((String) result);
                break;
            default:
                break;

        }

    }

    private void valideazaAdresaResponse(String result) {

        valideazaDateLivrare();

    }

    private void loadDateLivrareClient(BeanDateLivrareClient dateLivrareClient) {

        this.dateLivrareClient = dateLivrareClient;

        textLocalitateLivrare.setText(dateLivrareClient.getLocalitate());
        DateLivrare.getInstance().setOrasD(dateLivrareClient.getLocalitate());

        if (!dateLivrareClient.getStrada().isEmpty() && !dateLivrareClient.getStrada().equals("null")) {
            textStradaLivrare.setText(dateLivrareClient.getStrada());
            DateLivrare.getInstance().setAdresaD(dateLivrareClient.getStrada());
        }

        DateLivrare.getInstance().setCodJudetD(dateLivrareClient.getCodJudet());
        addJudeteFacturare();
        setJudetLivrare();
        addAdresaLivrare();

        int nrJudete = spinnerJudetLivrare.getAdapter().getCount();

        for (int i = 0; i < nrJudete; i++) {

            @SuppressWarnings("unchecked")
            HashMap<String, String> artMap = (HashMap<String, String>) spinnerJudetLivrare.getAdapter().getItem(i);

            if (artMap.get("codJudet").equals(dateLivrareClient.getCodJudet())) {
                spinnerJudetLivrare.setSelection(i);
                break;
            }

        }

    }

    private void setDateLivrareClient() {

        if ((UtilsUser.isCGED() || UtilsUser.isSSCM() || CreareComandaGed.tipClient.equals("IP")) && dateLivrareClient != null) {
            textLocalitateLivrare.setText(dateLivrareClient.getLocalitate());


            if (!dateLivrareClient.getStrada().isEmpty() && !dateLivrareClient.getStrada().equals("null")) {
                textStradaLivrare.setText(dateLivrareClient.getStrada());
            }

            if (!dateLivrareClient.getNrStrada().isEmpty() && !dateLivrareClient.getNrStrada().equals("null")) {
                //textNrStr.setText(dateLivrareClient.getNrStrada());
            }

            if (!dateLivrareClient.getNumePersContact().isEmpty() && !dateLivrareClient.getNumePersContact().equals("null")) {
                txtPers.setText(dateLivrareClient.getNumePersContact());
            }

            if (!dateLivrareClient.getTelPersContact().isEmpty() && !dateLivrareClient.getTelPersContact().equals("null")) {
                txtTel.setText(dateLivrareClient.getTelPersContact());
            }

            if ((UtilsUser.isCGED() || UtilsUser.isSSCM()) && dateLivrareClient.getTermenPlata().trim().length() > 0) {
                String[] tokTermen = dateLivrareClient.getTermenPlata().split(";");
                for (int nrLivr = 0; nrLivr < tokTermen.length; nrLivr++) {
                    if (!tokTermen[nrLivr].equals("C000") && !tokTermen[nrLivr].equals("null"))
                        adapterTermenPlata.add(tokTermen[nrLivr]);
                }

            }

        }

    }

    public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {

        if (numeComanda == EnumOperatiiAdresa.IS_ADRESA_VALIDA) {
            valideazaAdresaResponse((String) result);
        } else if (numeComanda == EnumOperatiiAdresa.GET_DATE_LIVRARE_CLIENT) {
            loadDateLivrareClient(operatiiAdresa.deserializeDateLivrareClient((String) result));
        } else if (numeComanda == EnumOperatiiAdresa.GET_FILIALA_MATHAUS) {
            CreareComandaGed.filialaLivrareMathaus = ((String) result).split(",")[0];
            CreareComandaGed.filialeArondateMathaus = (String) result;
        } else if (numeComanda == EnumOperatiiAdresa.GET_ADRESA_FILIALA) {
            setAdresalivrareFiliala((String) result);
        } else if (numeComanda == EnumOperatiiAdresa.GET_DATE_POLIGON_LIVRARE) {
            setDatePoligonLivrare((String) result);
        } else if (numeComanda == EnumOperatiiAdresa.GET_FILIALE_TCLI) {
            setFilialeTCLI((String) result);
        } else if (numeComanda == EnumOperatiiAdresa.GET_ZILE_LIVRARE) {
            afisZileLivrare((String) result);
        } else if (numeComanda == EnumOperatiiAdresa.GET_CODURI_POSTALE) {
            afisCoduriPostaleDialog((String) result);
        } else {
            switch (tipLocalitate) {
                case LOCALITATE_SEDIU:
                    populateListLocSediu(operatiiAdresa.deserializeListAdrese(result));
                    setDateLivrareClient();
                    break;
                case LOCALITATE_LIVRARE:
                    populateListLocLivrare(operatiiAdresa.deserializeListAdrese(result));
                    break;
                default:
                    break;

            }
        }

    }

    @Override
    public void addressSelected(LatLng coord, android.location.Address address) {
        DateLivrare.getInstance().setCoordonateAdresa(coord);
        setAdresaLivrare(MapUtils.getAddress(address));

    }


    @Override
    public void obiectivSelected(ObiectivConsilier obiectiv) {
        textObiectivSelectat.setVisibility(View.VISIBLE);
        textObiectivSelectat.setText(obiectiv.getBeneficiar() + " / " + obiectiv.getAdresa());
        obiectivSelectat = obiectiv;
        radioAdresaObiectiv.setVisibility(View.VISIBLE);
        radioAdresaObiectiv.setChecked(true);
        btnStergeObiectiv.setVisibility(View.VISIBLE);
        DateLivrare.getInstance().setIdObiectiv(obiectivSelectat.getId());
        DateLivrare.getInstance().setObiectivConsilier(obiectiv);

    }

}
