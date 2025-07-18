/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareClientiAdapter;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanDatePersonale;
import my.logon.screen.beans.DateClientSap;
import my.logon.screen.beans.DetaliiClient;
import my.logon.screen.beans.FurnizorComanda;
import my.logon.screen.beans.GeocodeAddress;
import my.logon.screen.beans.InfoCredit;
import my.logon.screen.beans.PlatitorTva;
import my.logon.screen.beans.RaspunsClientSap;
import my.logon.screen.dialogs.CautaClientDialog;
import my.logon.screen.dialogs.CreareClientPFDialog;
import my.logon.screen.dialogs.CreareClientPJDialog;
import my.logon.screen.dialogs.DatePersClientDialog;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.helpers.DialogHelper;
import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.CreareClientPFListener;
import my.logon.screen.listeners.CreareClientPJListener;
import my.logon.screen.listeners.DatePersListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectClientCmdGed extends Activity implements OperatiiClientListener, CautaClientDialogListener, DatePersListener, CreareClientPJListener, CreareClientPFListener {

    Button cautaClientBtn, saveClntBtn;
    String filiala = "", nume = "", cod = "";
    String clientResponse = "";
    String codClient = "";
    String numeClient = "";
    String depart = "";
    String codClientVar = "";
    String numeClientVar = "";

    private EditText txtNumeClientGed, txtNumeClientDistrib, txtCNPClient, txtCodJ;

    RadioButton radioClDistrib, radioClPF, radioClPJ, radioCmdNormala, radioCmdSimulata, radioRezervStocDa, radioRezervStocNu;
    LinearLayout layoutRezervStocLabel, layoutRezervStocBtn, layoutDetaliiClientDistrib;
    private OperatiiClient operatiiClient;

    public SimpleAdapter adapterClienti;
    private LinearLayout layoutLabelJ, layoutTextJ;
    private LinearLayout layoutClientPersoana, layoutClientDistrib;
    private ListView listViewClienti;
    private BeanClient selectedClient;
    private TextView textNumeClientDistrib, textCodClientDistrib, textAdrClient, textLimitaCredit, textRestCredit, textTipClient, clientBlocat, filialaClient;

    private RadioButton radioClMeserias;
    private NumberFormat numberFormat;
    private CheckBox checkPlatTva, checkFacturaPF;
    private Button clientBtn, verificaID, verificaTva;
    private TextView textClientParavan, labelIDClient;

    private Button cautaClientPFBtn;

    private RadioButton radioClientInstPub, radioClientNominal;


    private enum EnumTipClient {
        MESERIAS, PARAVAN, DISTRIBUTIE;
    }

    private EnumTipClient tipClient;
    private boolean pressedTVAButton = false;
    private Spinner spinnerAgenti;
    private RadioGroup radioSelectAgent;
    private String codCuiIp;
    private String codCuiPJ;
    private CheckBox checkCustodie;
    private boolean clientCUIWithCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.selectclientcmd_ged_header);

        tipClient = EnumTipClient.MESERIAS;

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        operatiiClient = new OperatiiClient(this);
        operatiiClient.setOperatiiClientListener(this);

        checkFacturaPF = (CheckBox) findViewById(R.id.checkFacturaPF);
        setListenerFacturaPF();

        checkPlatTva = (CheckBox) findViewById(R.id.checkPlatTva);
        checkPlatTva.setVisibility(View.INVISIBLE);
        checkPlatTva.setEnabled(false);

        layoutClientPersoana = (LinearLayout) findViewById(R.id.layoutClientPersoana);
        layoutClientPersoana.setVisibility(View.GONE);

        layoutClientDistrib = (LinearLayout) findViewById(R.id.layoutClientDistrib);
        layoutClientDistrib.setVisibility(View.VISIBLE);

        layoutDetaliiClientDistrib = (LinearLayout) findViewById(R.id.detaliiClientDistrib);
        layoutDetaliiClientDistrib.setVisibility(View.GONE);

        checkCustodie = findViewById(R.id.checkCustodie);

        textNumeClientDistrib = (TextView) findViewById(R.id.textNumeClientDistrib);
        textCodClientDistrib = (TextView) findViewById(R.id.textCodClientDistrib);
        textAdrClient = (TextView) findViewById(R.id.textAdrClient);
        textLimitaCredit = (TextView) findViewById(R.id.textLimitaCredit);
        textRestCredit = (TextView) findViewById(R.id.textRestCredit);
        textTipClient = (TextView) findViewById(R.id.tipClient);
        clientBlocat = (TextView) findViewById(R.id.clientBlocat);
        filialaClient = (TextView) findViewById(R.id.filClient);

        this.saveClntBtn = (Button) findViewById(R.id.saveClntBtn);
        addListenerSave();

        cautaClientBtn = (Button) findViewById(R.id.cautaClientBtn);
        setListenerCautaClientBtn();

        cautaClientPFBtn = (Button) findViewById(R.id.cautaClientPFBtn);
        setListenerCautaClientPFBtn();

        listViewClienti = (ListView) findViewById(R.id.listClienti);
        setListViewClientiListener();

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Selectie client");
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtNumeClientGed = (EditText) findViewById(R.id.txtNumeClient);

        txtNumeClientDistrib = (EditText) findViewById(R.id.txtNumeClientDistrib);

        txtCNPClient = (EditText) findViewById(R.id.txtCNPClient);
        txtCodJ = (EditText) findViewById(R.id.txtCodJ);

        layoutLabelJ = (LinearLayout) findViewById(R.id.layoutLabelJ);
        layoutLabelJ.setVisibility(View.GONE);
        layoutTextJ = (LinearLayout) findViewById(R.id.layoutTextJ);
        layoutTextJ.setVisibility(View.GONE);

        labelIDClient = (TextView) findViewById(R.id.labelIdClient);
        labelIDClient.setText("Telefon client");

        verificaID = (Button) findViewById(R.id.verificaId);
        setListenerVerificaID();

        verificaTva = (Button) findViewById(R.id.verificaTva);
        verificaTva.setVisibility(View.GONE);
        setListenerVerificaTva();

        radioClDistrib = (RadioButton) findViewById(R.id.radioClDistrib);
        radioClPJ = (RadioButton) findViewById(R.id.radioClPJ);
        radioClPF = (RadioButton) findViewById(R.id.radioClPF);
        radioClMeserias = (RadioButton) findViewById(R.id.radioClMeserias);

        radioClientInstPub = (RadioButton) findViewById(R.id.radioClInstPub);
        setVisibilityRadioInstPublica(radioClientInstPub);

        radioClientNominal = (RadioButton) findViewById(R.id.radioClNominal);
        setVisibilityRadioClientNominal();

        setVisibilityRadioClMeserias(radioClMeserias);

        addListenerRadioClDistrib();
        addListenerRadioCLPF();
        addListenerRadioCLPJ();
        addListenerRadioMeseriasi();
        addListenerRadioInstPub();
        addListenerRadioClientNominal();

        radioClDistrib.setChecked(false);
        radioClDistrib.setVisibility(View.GONE);
        radioClPF.setChecked(true);

        if (UserInfo.getInstance().getTipUserSap().equals("KA3")) {
            radioClDistrib.setChecked(true);
            radioClDistrib.setVisibility(View.VISIBLE);
        }

        radioCmdNormala = (RadioButton) findViewById(R.id.radioCmdNormala);
        addListenerRadioCmdNormala();

        radioCmdSimulata = (RadioButton) findViewById(R.id.radioCmdSimulata);
        if (CreareComandaGed.tipComandaGed == TipCmdGed.COMANDA_LIVRARE)
            radioCmdSimulata.setVisibility(View.INVISIBLE);
        else
            radioCmdSimulata.setVisibility(View.VISIBLE);

        addListenerRadioCmdSimulata();

        radioRezervStocDa = (RadioButton) findViewById(R.id.radioRezervStocDa);

        radioRezervStocNu = (RadioButton) findViewById(R.id.radioRezervStocNu);

        layoutRezervStocLabel = (LinearLayout) findViewById(R.id.layoutRezervStocLabel);
        layoutRezervStocLabel.setVisibility(View.GONE);

        layoutRezervStocBtn = (LinearLayout) findViewById(R.id.layoutRezervStocBtn);
        layoutRezervStocBtn.setVisibility(View.GONE);

        textClientParavan = (TextView) findViewById(R.id.textClientParavan);

        clientBtn = (Button) findViewById(R.id.clientBtn);
        addListenerClientBtn();

        radioSelectAgent = (RadioGroup) findViewById(R.id.radio_select_agent);
        setRadioSelectClientListener();

        if (isCasiera()) {
            radioClPF.setVisibility(View.INVISIBLE);
            radioClPJ.setChecked(true);

            findViewById(R.id.layoutClientParavan).setVisibility(View.INVISIBLE);
            findViewById(R.id.layoutDateClient).setVisibility(View.INVISIBLE);

            labelIDClient.setVisibility(View.INVISIBLE);

            findViewById(R.id.layoutLabelJ).setVisibility(View.INVISIBLE);
            findViewById(R.id.layoutTextJ).setVisibility(View.INVISIBLE);
            findViewById(R.id.layoutTipComanda).setVisibility(View.INVISIBLE);
            findViewById(R.id.layoutRadioTipComanda).setVisibility(View.INVISIBLE);

            spinnerAgenti = ((Spinner) findViewById(R.id.spinnerAgenti));

            setSpinnerAgentiListener();

            if (CreareComandaGed.codClientVar.isEmpty())
                cautaClientDistributie("");
            else
                txtNumeClientGed.setText(CreareComandaGed.numeClientVar);

        }

        if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.LIVRARE_CUSTODIE)) {
            if (radioClPJ.getVisibility() == View.VISIBLE)
                radioClPJ.setChecked(true);

            if (radioClDistrib.getVisibility() == View.VISIBLE)
                radioClDistrib.setEnabled(false);

            if (radioClientInstPub.getVisibility() == View.VISIBLE)
                radioClientInstPub.setEnabled(false);

            if (radioClientNominal.getVisibility() == View.VISIBLE)
                radioClientNominal.setEnabled(false);

            if (radioClMeserias.getVisibility() == View.VISIBLE)
                radioClMeserias.setEnabled(false);

            if (radioCmdSimulata.getVisibility() == View.VISIBLE)
                radioCmdSimulata.setEnabled(false);

        }


        if (isConditiiCustodie() && (radioClPJ.isChecked() || radioClPF.isChecked()))
            checkCustodie.setVisibility(View.VISIBLE);
        else
            checkCustodie.setVisibility(View.INVISIBLE);

        if (UtilsUser.isUserCVOB()) {
            radioClPF.setVisibility(View.INVISIBLE);
            radioClPJ.setChecked(true);
        }

        setListenerCustodie();

    }

    private void setListenerCustodie() {
        checkCustodie.setOnCheckedChangeListener((buttonView, isChecked) -> DateLivrare.getInstance().setComandaCustodie(isChecked));
    }

    private boolean isConditiiCustodie() {
        return (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE)
                || DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE));
    }

    private boolean isCasiera() {
        return UserInfo.getInstance().getTipUserSap().equals("CGED") || UtilsUser.isSSCM();
    }

    private void setRadioSelectClientListener() {

        radioSelectAgent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radio_ag_det:
                        getAgentComanda();
                        break;
                    default:
                        break;

                }

            }
        });

    }

    private void getAgentComanda() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codClient", CreareComandaGed.codClientVar);
        params.put("filiala", UserInfo.getInstance().getUnitLog());
        operatiiClient.getAgentComanda(params);

    }

    private void setListenerVerificaTva() {
        verificaTva.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getListClientiCUI();

            }
        });
    }

    private void verificaTVAClientCUI() {
        pressedTVAButton = true;
        performVerificareTVA();
    }

    private void getListClientiCUI() {

        String strCui = "";

        if (radioClientInstPub.isChecked()) {
            strCui = codCuiIp;
        } else if (!txtCNPClient.getText().toString().isEmpty()) {
            strCui = txtCNPClient.getText().toString().trim();
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cuiClient", strCui);
        params.put("codAgent", UserInfo.getInstance().getCod());
        operatiiClient.getListClientiCUI(params);

    }

    private void performVerificareTVA() {

        String strCui = "";
        codCuiPJ = "";

        if (radioClientInstPub.isChecked()) {
            strCui = codCuiIp;
        } else if (!txtCNPClient.getText().toString().isEmpty()) {
            strCui = txtCNPClient.getText().toString().trim();
            codCuiPJ = strCui;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cuiClient", strCui);
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("depart", UserInfo.getInstance().getCodDepart());
        operatiiClient.getStarePlatitorTva(params);

    }

    private void setListenerFacturaPF() {
        checkFacturaPF.setOnClickListener(v -> {

            if (checkFacturaPF.isChecked())
                checkFacturaPF.setText("Se emite factura");
            else
                checkFacturaPF.setText("Nu se emite factura");

        });
    }

    private void setListenerVerificaID() {

        verificaID.setOnClickListener(v -> {
            String textTelClient = txtCNPClient.getText().toString().trim();

            if (!textTelClient.isEmpty()) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("strClient", textTelClient);
                params.put("criteriu", "TEL");
                operatiiClient.cautaClientPF(params);
            }
        });

    }


    private void setListenerCautaClientBtn() {
        cautaClientBtn.setOnClickListener(v -> {

            if (isNumeClientValid())
                getListaClienti();

        });
    }

    private void setListenerCautaClientPFBtn() {
        cautaClientPFBtn.setOnClickListener(v -> {

            String textClient = txtNumeClientGed.getText().toString().trim();

            if (UtilsUser.isCGED() || UtilsUser.isSSCM() || (UtilsUser.isAgentOrSD() && radioClientNominal.isChecked())) {
                cautaClientDistributie(textClient);

            } else if (!textClient.isEmpty())
                cautaClientPF(textClient);

        });
    }

    private void cautaClientDistributie(String numeClient) {
        tipClient = EnumTipClient.DISTRIBUTIE;
        CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
        clientDialog.setMeserias(false);
        clientDialog.setClientObiectivKA(false);
        clientDialog.setNumeClient(numeClient);
        clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
        clientDialog.show();
    }

    private void cautaClientPF(String textClient) {

        if (radioClPJ.isChecked())
            pressedTVAButton = false;

        String tipClientCautare = getTipClient();

        HashMap<String, String> params = new HashMap<String, String>();

        if (tipClientCautare.equals("PF")) {
            params.put("strClient", textClient);
            params.put("criteriu", "NUME");
            operatiiClient.cautaClientPF(params);
        } else {
            params.put("numeClient", textClient);
            params.put("tipClient", "PJ");
            params.put("codAgent", UserInfo.getInstance().getCod());
            params.put("depart", UserInfo.getInstance().getCodDepart());
            params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
            operatiiClient.getCnpClient(params);
        }

    }

    private String getTipClient() {
        String tipClient = " ";
        if (radioClPF.isChecked())
            tipClient = "PF";
        else if (radioClPJ.isChecked())
            tipClient = "PJ";

        return tipClient;
    }

    private boolean isNumeClientValid() {
        if (txtNumeClientDistrib.getText().toString().trim().length() > 0) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Introduceti nume client!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setVisibilityRadioInstPublica(RadioButton radioClientInstPub) {
        if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED") || UserInfo.getInstance().getTipUserSap().equals("CVR")
                || UserInfo.getInstance().getTipUserSap().equals("SDIP") || UserInfo.getInstance().getTipUserSap().equals("CVIP"))
            radioClientInstPub.setVisibility(View.VISIBLE);
        else
            radioClientInstPub.setVisibility(View.INVISIBLE);

    }

    private void setVisibilityRadioClientNominal() {

        if (UtilsUser.isAgentOrSD())
            radioClientNominal.setVisibility(View.VISIBLE);
        else
            radioClientNominal.setVisibility(View.INVISIBLE);

    }

    private void setVisibilityRadioClMeserias(RadioButton radioClMeserias) {
        if (UserInfo.getInstance().getTipUserSap().contains("CAG"))
            radioClMeserias.setVisibility(View.VISIBLE);
        else
            radioClMeserias.setVisibility(View.INVISIBLE);

    }

    private void addListenerClientBtn() {
        clientBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                tipClient = EnumTipClient.PARAVAN;
                CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
                clientDialog.setMeserias(false);
                clientDialog.setClientObiectivKA(false);
                clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
                clientDialog.show();
            }
        });
    }

    private void getListaClienti() {
        String numeClient = txtNumeClientDistrib.getText().toString().trim().replace('*', '%');

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("numeClient", numeClient);
        params.put("depart", "00");
        params.put("departAg", UserInfo.getInstance().getCodDepart());
        params.put("unitLog", UserInfo.getInstance().getUnitLog());

        operatiiClient.getListClienti(params);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void afisDateClientAnaf(String result) {

        DateClientSap dateClientSap = operatiiClient.deserializeDateClientANAF(result);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.81);

        CreareClientPJDialog clientPJDialog = new CreareClientPJDialog(dateClientSap, this);
        clientPJDialog.getWindow().setLayout(width, height);
        clientPJDialog.setCreareClientPJListener(this);
        clientPJDialog.show();

    }

    private void setCodClientNominalPJ(String result) {

        RaspunsClientSap raspunsClientSap = operatiiClient.deserializeRaspunsClient(result);

        if (!raspunsClientSap.getCodClient().trim().isEmpty() && !raspunsClientSap.getCodClient().trim().equals("null")) {
            CreareComandaGed.codClientCUI = raspunsClientSap.getCodClient();

            DateLivrare.getInstance().setDiviziiClient(raspunsClientSap.getDiviziiClient());
            valideazaDateClient();
        } else {
            setDateContactClientPF(null);
            Toast.makeText(getApplicationContext(), "Inregistrarea clientului in baza de date a esuat. Contactati departamentul IT.", Toast.LENGTH_LONG).show();
        }

    }

    private void updateStareTva(PlatitorTva platitorTva) {


        if (platitorTva.getStareInregistrare() != null && platitorTva.getStareInregistrare().toLowerCase().contains("radiere")) {
            pressedTVAButton = false;
            new DialogHelper().showInfoDialog(this, "\nAcest client este radiat.\n");
            return;
        }

        String stare = "";

        if (platitorTva.isPlatitor()) {
            checkPlatTva.setChecked(true);
        } else {
            checkPlatTva.setChecked(false);
            stare = " nu";
        }

        String message = "";

        if (platitorTva.getErrMessage().length() > 0) {
            message = platitorTva.getErrMessage();
            Toast.makeText(this, "Eroare citire date: " + message, Toast.LENGTH_LONG).show();
            txtNumeClientGed.setText("");
            txtCodJ.setText("");
            return;
        } else if (clientCUIWithCode) {
            message = txtNumeClientGed.getText().toString() + stare + " este platitor de tva.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            valideazaDateClient();
            return;
        } else {
            message = platitorTva.getNumeClient() + stare + " este platitor de tva.";
            txtNumeClientGed.setText(platitorTva.getNumeClient());
            txtCodJ.setText(platitorTva.getNrInreg());

            if (platitorTva.getCodJudet() != null && !platitorTva.getCodJudet().equals("null") && !platitorTva.getCodJudet().trim().isEmpty()) {
                DateLivrare.getInstance().setCodJudetD(platitorTva.getCodJudet());
                DateLivrare.getInstance().setOrasD(platitorTva.getLocalitate());
                DateLivrare.getInstance().setAdresaD(platitorTva.getStrada());
            }

            DateLivrare.getInstance().setDiviziiClient(platitorTva.getDiviziiClient());

        }

        CreareComandaGed.codClientCUI = "";
        if (platitorTva.getCodClientNominal() != null && !platitorTva.getCodClientNominal().isEmpty())
            CreareComandaGed.codClientCUI = platitorTva.getCodClientNominal();

        if (!isConditiiClientPJNou()) {
            return;
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        if (!pressedTVAButton)
            valideazaDateClient();

    }

    private boolean isConditiiClientPJNou() {

        String strCui = "";

        if (radioClPJ.isChecked() && !UtilsUser.isCGED() && !UtilsUser.isSSCM()
                && CreareComandaGed.codClientCUI.isEmpty()) {

            if (!txtCNPClient.getText().toString().isEmpty()) {
                strCui = txtCNPClient.getText().toString().trim();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("cui", strCui);
                operatiiClient.getDateClientANAF(params);
            }

            return false;

        } else
            return true;

    }

    private boolean isConditiiClientPFNou() {
        if (isCustodiePF() && CreareComandaGed.codClientCUI.isEmpty()) {
            afisCreareClientPFDialog();
            return false;
        }

        return true;
    }

    private boolean isCustodiePF() {
        return radioClPF.isChecked() && DateLivrare.getInstance().isComandaCustodie();
    }

    private void afisCreareClientPFDialog() {

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);

        DateClientSap dateClientSap = new DateClientSap();

        dateClientSap.setNumePersContact(txtNumeClientGed.getText().toString().trim());
        dateClientSap.setTelPersContact(txtCNPClient.getText().toString().trim());

        CreareClientPFDialog clientPJDialog = new CreareClientPFDialog(dateClientSap, this);
        clientPJDialog.getWindow().setLayout(width, height);
        clientPJDialog.setCreareClientPFListener(this);
        clientPJDialog.show();
    }

    private void addListenerRadioClDistrib() {
        radioClDistrib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutClientPersoana.setVisibility(View.GONE);
                    layoutClientDistrib.setVisibility(View.VISIBLE);
                    verificaID.setVisibility(View.GONE);
                    verificaTva.setVisibility(View.GONE);
                    labelIDClient.setText("CUI");
                    ScreenUtils.setCheckBoxVisibility(checkCustodie, false);

                    clearDateLivrare();

                } else {
                    layoutClientPersoana.setVisibility(View.VISIBLE);
                    layoutClientDistrib.setVisibility(View.GONE);

                }

            }
        });

    }

    private void addListenerRadioCLPJ() {
        radioClPJ.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {

                    ((LinearLayout) findViewById(R.id.layoutClientParavan)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.layoutDateClient)).setVisibility(View.VISIBLE);
                    labelIDClient.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.layoutLabelJ)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.layoutTextJ)).setVisibility(View.VISIBLE);

                    ScreenUtils.setCheckBoxVisibility(checkCustodie, isConditiiCustodie());

                    layoutLabelJ.setVisibility(View.VISIBLE);
                    layoutTextJ.setVisibility(View.VISIBLE);
                    checkPlatTva.setChecked(true);
                    checkPlatTva.setVisibility(View.VISIBLE);
                    verificaID.setVisibility(View.GONE);
                    checkFacturaPF.setVisibility(View.GONE);
                    verificaTva.setVisibility(View.VISIBLE);
                    labelIDClient.setText("CUI");
                    txtCodJ.setText("");
                    setTextNumeClientEnabled(true);
                    clearDateLivrare();


                }

            }
        });
    }

    private void addListenerRadioClientNominal() {
        radioClientNominal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UtilsUser.isAgentOrSD()) {

                    ((LinearLayout) findViewById(R.id.layoutClientParavan)).setVisibility(View.INVISIBLE);
                    ((LinearLayout) findViewById(R.id.layoutDateClient)).setVisibility(View.INVISIBLE);

                    labelIDClient.setVisibility(View.INVISIBLE);

                    ((LinearLayout) findViewById(R.id.layoutLabelJ)).setVisibility(View.INVISIBLE);
                    ((LinearLayout) findViewById(R.id.layoutTextJ)).setVisibility(View.INVISIBLE);

                    txtNumeClientGed.setText("");
                    ScreenUtils.setCheckBoxVisibility(checkCustodie, false);

                }

            }
        });
    }

    private void addListenerRadioCLPF() {

        radioClPF.setOnCheckedChangeListener((OnCheckedChangeListener) (arg0, arg1) -> {
            if (arg1) {

                labelIDClient.setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.layoutDateClient)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.layoutClientParavan)).setVisibility(View.VISIBLE);
                ScreenUtils.setCheckBoxVisibility(checkCustodie, isConditiiCustodie());

                layoutLabelJ.setVisibility(View.GONE);
                layoutTextJ.setVisibility(View.GONE);
                checkPlatTva.setVisibility(View.GONE);
                verificaID.setVisibility(View.VISIBLE);
                verificaTva.setVisibility(View.GONE);
                checkFacturaPF.setVisibility(View.VISIBLE);
                labelIDClient.setText("Telefon client");
                setTextNumeClientEnabled(true);
                clearDateLivrare();


            }

        });

    }

    private void addListenerRadioMeseriasi() {

        radioClMeserias.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                layoutLabelJ.setVisibility(View.GONE);
                layoutTextJ.setVisibility(View.GONE);
                checkPlatTva.setVisibility(View.INVISIBLE);

                verificaID.setVisibility(View.GONE);
                checkFacturaPF.setVisibility(View.GONE);
                labelIDClient.setText("COD");

                setTextNumeClientEnabled(false);
                ScreenUtils.setCheckBoxVisibility(checkCustodie, false);

                tipClient = EnumTipClient.MESERIAS;
                CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
                clientDialog.setMeserias(true);
                clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
                clientDialog.show();

                clearDateLivrare();

            }
        });

    }

    private void addListenerRadioInstPub() {
        radioClientInstPub.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                layoutLabelJ.setVisibility(View.GONE);
                layoutTextJ.setVisibility(View.GONE);
                checkPlatTva.setVisibility(View.INVISIBLE);

                verificaTva.setVisibility(View.VISIBLE);

                verificaID.setVisibility(View.GONE);
                checkFacturaPF.setVisibility(View.GONE);
                labelIDClient.setText("COD");
                txtCNPClient.setVisibility(View.VISIBLE);
                ScreenUtils.setCheckBoxVisibility(checkCustodie, false);
                setTextNumeClientEnabled(false);

                tipClient = EnumTipClient.MESERIAS;


                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);

                CautaClientDialog clientDialog = new CautaClientDialog(SelectClientCmdGed.this);
                clientDialog.setInstitPublica(true);
                clientDialog.getWindow().setLayout(width, height);
                clientDialog.setClientSelectedListener(SelectClientCmdGed.this);
                clientDialog.show();

                clearDateLivrare();

            }
        });
    }

    private void setTextNumeClientEnabled(boolean isEnabled) {

        txtNumeClientGed.setText("");
        txtCNPClient.setText("");

        if (!isEnabled) {
            txtNumeClientGed.setFocusable(false);
            txtCNPClient.setFocusable(false);
        } else {
            txtNumeClientGed.setFocusableInTouchMode(true);
            txtCNPClient.setFocusableInTouchMode(true);
            txtNumeClientGed.requestFocus();
        }

    }

    private void clearDateLivrare() {

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_DETERIORATE)
            return;

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_AMOB)
            return;

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.LIVRARE_CUSTODIE)
            return;

        String filialaClp = "";

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.COMANDA_LIVRARE) {
            filialaClp = DateLivrare.getInstance().getCodFilialaCLP();
        }

        String codFilialaFasonate = DateLivrare.getInstance().getCodFilialaFasonate();

        FurnizorComanda furnizorComanda = null;

        boolean localIsCmdACZC = DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA;

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.DISPOZITIE_LIVRARE || DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_COMANDA) {
            furnizorComanda = DateLivrare.getInstance().getFurnizorComanda();
        }

        if (ListaArticoleComandaGed.getInstance().getListArticoleComanda().isEmpty())
            DateLivrare.getInstance().resetAll();

        if (!filialaClp.isEmpty()) {
            DateLivrare.getInstance().setCodFilialaCLP(filialaClp);
            DateLivrare.getInstance().setTipComandaGed(TipCmdGed.COMANDA_LIVRARE);
        }

        DateLivrare.getInstance().setCodFilialaFasonate(codFilialaFasonate);

        if (furnizorComanda != null) {
            DateLivrare.getInstance().setFurnizorComanda(furnizorComanda);
            DateLivrare.getInstance().setTipComandaGed(TipCmdGed.DISPOZITIE_LIVRARE);

            if (localIsCmdACZC)
                DateLivrare.getInstance().setTipComandaGed(TipCmdGed.ARTICOLE_COMANDA);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addListenerRadioCmdNormala() {
        radioCmdNormala.setOnCheckedChangeListener((arg0, arg1) -> {
            if (arg1) {
                CreareComandaGed.tipComanda = "N";
                layoutRezervStocLabel.setVisibility(View.GONE);
                layoutRezervStocBtn.setVisibility(View.GONE);
                checkCustodie.setVisibility(View.VISIBLE);
                checkCustodie.setChecked(false);
            }

        });
    }

    public void addListenerRadioCmdSimulata() {
        radioCmdSimulata.setOnCheckedChangeListener((arg0, arg1) -> {
            if (arg1) {
                CreareComandaGed.tipComanda = "S";
                layoutRezervStocLabel.setVisibility(View.GONE);
                layoutRezervStocBtn.setVisibility(View.GONE);
                radioRezervStocDa.setChecked(true);
                checkCustodie.setVisibility(View.INVISIBLE);
                checkCustodie.setChecked(false);
            }

        });
    }

    public void addListenerSave() {
        saveClntBtn.setOnClickListener(v -> {

            if (radioClPJ.isChecked() && !pressedTVAButton && !UtilsUser.isCGED() && !UtilsUser.isSSCM()) {
                if (clientCUIWithCode || !pressedTVAButton)
                    performVerificareTVA();
                else
                    getListClientiCUI();
            } else
                valideazaDateClient();

        });

    }

    private void valideazaDateClient() {

        if (!radioClDistrib.isChecked() && !UtilsUser.isCGED() && !UtilsUser.isSSCM()) {
            if (txtNumeClientGed.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Completati numele clientului!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!radioClPF.isChecked() && txtCNPClient.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Completati CUI client!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isConditiiClientPJNou()) {
                return;
            }

            if (!isConditiiClientPFNou()) {
                return;
            }

            if (radioClPF.isChecked()) {
                CreareComandaGed.tipClient = "PF";
                DateLivrare.getInstance().setTipPersClient("PF");

                if (!checkFacturaPF.isChecked()) {

                    if (UtilsUser.isConsWood()) {
                        CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGedWood_faraFact(UserInfo.getInstance().getUnitLog(), "PF");
                    } else {
                        if (UtilsUser.isUserExceptieCONSGED())
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed_CONSGED_faraFactura(UserInfo.getInstance().getUnitLog(), "PF");
                        else
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGed_FaraFactura(UserInfo.getInstance().getUnitLog());

                    }

                    DateLivrare.getInstance().setFacturaCmd(false);

                } else {

                    if (UtilsUser.isUserSite()) {

                        if (hasCnp())
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
                        else
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientCVO_cuFact_faraCnp(UserInfo.getInstance().getUnitLog(), "PF");

                    } else {
                        if (UtilsUser.isConsWood())
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PF");
                        else {
                            if (UtilsUser.isUserExceptieCONSGED())
                                CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PF");
                            else
                                CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
                        }
                    }

                    DateLivrare.getInstance().setFacturaCmd(true);

                }

                CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

                if (CreareComandaGed.codClientCUI != null && !CreareComandaGed.codClientCUI.trim().isEmpty())
                    CreareComandaGed.codClientVar = CreareComandaGed.codClientCUI;


                if (UtilsComenzi.isComandaPFDep16())
                    DateLivrare.getInstance().setDiviziiClient("03;040;041;09;11");

            }

            if (radioClPJ.isChecked() && !UtilsUser.isCGED() && !UtilsUser.isSSCM()) {
                if (codCuiPJ != null && !codCuiPJ.isEmpty() && !codCuiPJ.equals(txtCNPClient.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "CUI client invalid!", Toast.LENGTH_SHORT).show();
                    return;
                }

                CreareComandaGed.tipClient = "PJ";
                DateLivrare.getInstance().setTipPersClient("PJ");

                CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

                if (UtilsUser.isConsWood()) {
                    if (checkPlatTva.isChecked())
                        CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.clientPJGenericWoodPlatitorTVA(UserInfo.getInstance().getUnitLog());
                    else
                        CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.clientPJGenericWoodNeplatitorTVA(UserInfo.getInstance().getUnitLog());
                } else {
                    if (checkPlatTva.isChecked()) {

                        CreareComandaGed.cnpClient = "RO" + txtCNPClient.getText().toString().trim();

                        if (UtilsUser.isUserExceptieCONSGED())
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PJ");
                        else
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PJ");
                    } else {
                        if (UtilsUser.isUserExceptieCONSGED())
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.gedPJNeplatitorTVA_CONSGED(UserInfo.getInstance().getUnitLog());
                        else
                            CreareComandaGed.codClientVar = ClientiGenericiGedInfoStrings.gedPJNeplatitorTVA(UserInfo.getInstance().getUnitLog());
                    }
                }

                if (CreareComandaGed.codClientCUI != null && !CreareComandaGed.codClientCUI.trim().isEmpty())
                    CreareComandaGed.codClientVar = CreareComandaGed.codClientCUI;
            }

            if (radioCmdNormala.isChecked())
                CreareComandaGed.tipComanda = "N";

            CreareComandaGed.numeClientVar = txtNumeClientGed.getText().toString().trim();

            if (layoutTextJ.getVisibility() == View.VISIBLE)
                CreareComandaGed.codJ = txtCodJ.getText().toString().trim();

        }

        if (radioClDistrib.isChecked()) {

            if (selectedClient == null) {
                Toast.makeText(getApplicationContext(), "Completati numele clientului!", Toast.LENGTH_SHORT).show();
                return;
            }

            CreareComandaGed.tipComanda = "N";
            CreareComandaGed.tipClient = "D";
            DateLivrare.getInstance().setTipPersClient("D");
            CreareComandaGed.numeClientVar = selectedClient.getNumeClient();
            CreareComandaGed.codClientVar = selectedClient.getCodClient();
            CreareComandaGed.cnpClient = " ";
            CreareComandaGed.codJ = " ";
            CreareComandaGed.rezervStoc = false;

        }

        if (radioClientInstPub.isChecked()) {

            CreareComandaGed.tipComanda = "N";
            CreareComandaGed.tipClient = "IP";
            DateLivrare.getInstance().setTipPersClient("IP");

        }

        if (radioCmdSimulata.isChecked()) {
            CreareComandaGed.tipComanda = "S";
            CreareComandaGed.rezervStoc = false;
        }

        if (radioClPJ.isChecked() && (UtilsUser.isCGED() || UtilsUser.isSSCM())) {
            if (spinnerAgenti.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Selectati un agent", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        DateLivrare.getInstance().setRefClient(((EditText) findViewById(R.id.textRefClient)).getText().toString().trim());

        finish();

    }

    private boolean hasCnp() {
        return !txtCNPClient.getText().toString().isEmpty();
    }

    private void setListViewClientiListener() {
        listViewClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedClient = (BeanClient) arg0.getAdapter().getItem(arg2);
                getDetaliiClient();

            }
        });
    }

    private void getDetaliiClient() {
        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codClient", selectedClient.getCodClient());
        params.put("depart", "00");
        operatiiClient.getDetaliiClient(params);
    }

    private void populateListViewClient(List<BeanClient> listClienti) {
        CautareClientiAdapter adapterClienti = new CautareClientiAdapter(this, listClienti);
        listViewClienti.setAdapter(adapterClienti);
    }

    private void afisDatePersSelectDialog(String strDatePersonale) {
        List<BeanDatePersonale> listDatePers = operatiiClient.deserializeDatePersonale(strDatePersonale);

        if (listDatePers.isEmpty() && isCustodiePF())
            afisCreareClientPFDialog();
        else if (listDatePers.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
        } else {
            DatePersClientDialog datePersDialog = new DatePersClientDialog(this, listDatePers);
            datePersDialog.setDatePersListener(this);
            datePersDialog.show();
        }

    }

    private void afisClientiCUI(String strClientiCUI) {
        List<BeanDatePersonale> listDatePers = operatiiClient.deserializeDatePersonale(strClientiCUI);
        clientCUIWithCode = false;

        if (listDatePers.isEmpty() || listDatePers.size() == 1) {
            verificaTVAClientCUI();
        } else {
            clientCUIWithCode = true;
            DatePersClientDialog datePersDialog = new DatePersClientDialog(this, listDatePers);
            datePersDialog.setDatePersListener(this);
            datePersDialog.show();
        }

    }

    private void listClientDetails(DetaliiClient detaliiClient) {

        layoutDetaliiClientDistrib.setVisibility(View.VISIBLE);

        textNumeClientDistrib.setText(selectedClient.getNumeClient());
        textCodClientDistrib.setText(selectedClient.getCodClient());

        textAdrClient.setText(detaliiClient.getOras() + " " + detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());

        textLimitaCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getLimitaCredit())));
        textRestCredit.setText(numberFormat.format(Double.valueOf(detaliiClient.getRestCredit())));
        textTipClient.setText(detaliiClient.getTipClient());
        DateLivrare.getInstance().setTermenPlata(detaliiClient.getTermenPlata());

        filialaClient.setText(detaliiClient.getFiliala());

        if (detaliiClient.getStare().equals("X")) {
            clientBlocat.setVisibility(View.VISIBLE);
            clientBlocat.setText("Blocat : " + detaliiClient.getMotivBlocare());
            saveClntBtn.setVisibility(View.INVISIBLE);
        } else {
            clientBlocat.setText("");
            textTipClient.setText(detaliiClient.getTipClient());
            saveClntBtn.setVisibility(View.VISIBLE);
        }

    }

    private void setSpinnerAgentiListener() {

        spinnerAgenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> artMap = (HashMap<String, String>) arg0.getSelectedItem();
                UserInfo.getInstance().setCod(artMap.get("codAgent"));

                if (!artMap.get("codAgent").isEmpty() && isCasiera()) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("codAgent", artMap.get("codAgent"));
                    params.put("codClient", CreareComandaGed.codClientVar);
                    operatiiClient.getTermenPlata(params);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void loadListAgenti(String agenti) {

        String[] tokAgenti = agenti.split("@");

        ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> agent = new HashMap<String, String>();
        agent.put("numeAgent", "Selectati un agent");
        agent.put("codAgent", "");

        listAgenti.add(agent);

        for (int i = 0; i < tokAgenti.length; i++) {
            agent = new HashMap<String, String>();

            agent.put("numeAgent", tokAgenti[i].split("#")[1]);
            agent.put("codAgent", tokAgenti[i].split("#")[0]);
            listAgenti.add(agent);
        }

        SimpleAdapter adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[]{"numeAgent", "codAgent"}, new int[]{
                R.id.textNumeAgent, R.id.textCodAgent});

        Spinner spinnerAgenti = ((Spinner) findViewById(R.id.spinnerAgenti));

        spinnerAgenti.setAdapter(adapterAgenti);
        spinnerAgenti.setVisibility(View.VISIBLE);

        if (tokAgenti.length == 1)
            spinnerAgenti.setSelection(1);

        radioSelectAgent.setVisibility(View.VISIBLE);

    }

    public void afisAgentComanda(String agent) {

        String[] tokAgent = agent.split("#");

        Spinner spinnerAgenti = ((Spinner) findViewById(R.id.spinnerAgenti));

        if (spinnerAgenti.getAdapter() == null)
            return;

        int nrAgenti = spinnerAgenti.getAdapter().getCount();

        if (tokAgent.length == 0 && nrAgenti > 1) {
            spinnerAgenti.setSelection(1);
            return;
        }

        boolean agentFound = false;
        for (int i = 0; i < nrAgenti; i++) {
            @SuppressWarnings("unchecked")
            HashMap<String, String> artMap = (HashMap<String, String>) spinnerAgenti.getAdapter().getItem(i);

            if (artMap.get("codAgent").equals(tokAgent[1])) {
                spinnerAgenti.setSelection(i);
                agentFound = true;
                break;
            }

        }

        if (!agentFound && nrAgenti > 1)
            spinnerAgenti.setSelection(1);

    }

    private void setInfoCreditClient(String result) {
        InfoCredit infoCredit = operatiiClient.deserializeInfoCreditClient(result);

        StringBuilder strInfo = new StringBuilder();
        strInfo.append("\n");
        strInfo.append("Limita credit: ");
        strInfo.append(infoCredit.getLimitaCredit());
        strInfo.append("\n");
        strInfo.append("Rest credit: ");
        strInfo.append(infoCredit.getRestCredit());

        if (infoCredit.isBlocat()) {
            strInfo.append("\n");
            strInfo.append("Client blocat. ");
            strInfo.append(infoCredit.getMotivBlocat());
            strInfo.append(".");
        }

        strInfo.append("\n");

        AlertDialog alertDialog = new AlertDialog.Builder(SelectClientCmdGed.this).create();
        alertDialog.setTitle("Info credit " + txtNumeClientGed.getText().toString());
        alertDialog.setMessage(strInfo);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public void onBackPressed() {
        finish();
        return;
    }

    public void operationComplete(EnumClienti methodName, Object result) {
        switch (methodName) {
            case GET_LISTA_CLIENTI:
                populateListViewClient(operatiiClient.deserializeListClienti((String) result));
                break;
            case GET_DETALII_CLIENT:
                listClientDetails(operatiiClient.deserializeDetaliiClient((String) result));
                break;
            case GET_STARE_TVA:
                updateStareTva(operatiiClient.deserializePlatitorTva((String) result));
                break;
            case GET_CNP_CLIENT:
            case CAUTA_CLIENT_PF:
                afisDatePersSelectDialog((String) result);
            case GET_AGENT_COMANDA:
                afisAgentComanda((String) result);
                break;
            case GET_TERMEN_PLATA:
                setTermenPlataClient((String) result);
                break;
            case GET_INFO_CREDIT:
                setInfoCreditClient((String) result);
                break;
            case GET_DATE_CLIENT_ANAF:
                afisDateClientAnaf((String) result);
                break;
            case CREEAZA_CLIENT_PJ:
            case CREEAZA_CLIENT_PF:
                setCodClientNominalPJ((String) result);
                break;
            case GET_LISTA_CLIENTI_CUI:
                afisClientiCUI((String) result);
                break;
            default:
                break;
        }

    }

    private void getInfoCreditClient(String codClient) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("codClient", codClient);
        operatiiClient.getInfoCredit(params);

    }

    public void clientSelected(BeanClient client) {

        if (tipClient == EnumTipClient.MESERIAS) {
            txtNumeClientGed.setText(client.getNumeClient());
            txtCNPClient.setText(client.getCodClient());
            CreareComandaGed.codClientVar = client.getCodClient();
            CreareComandaGed.tipClient = client.getTipClient();
            CreareComandaGed.tipClientIP = client.getTipClientIP();
            CreareComandaGed.tipPlataContract = client.getTipPlata();
            DateLivrare.getInstance().setClientBlocat(client.isClientBlocat());

            if (client.getTermenPlata() != null)
                CreareComandaGed.listTermenPlata = client.getTermenPlata();

            if (radioClientInstPub.isChecked()) {
                labelIDClient.setText(labelIDClient.getText() + "\t\t\t\t\t CUI: " + client.getCodCUI());
                codCuiIp = client.getCodCUI();

                txtNumeClientGed.setText(client.getNumeClient().replaceFirst("\\(.*", ""));

                if (DateLivrare.getInstance().getFurnizorComanda() != null) {
                    UserInfo.getInstance().setUnitLog(client.getFilialaClientIP());
                } else if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE)) {
                } else {
                    if (!client.getFilialaClientIP().equals(UserInfo.getInstance().getUnitLog()))
                        UserInfo.getInstance().setUnitLog(client.getFilialaClientIP());
                }

                getInfoCreditClient(client.getCodClient());
            }

        }
        if (tipClient == EnumTipClient.DISTRIBUTIE) {
            txtNumeClientGed.setText(client.getNumeClient());
            txtCNPClient.setText(client.getCodClient());
            CreareComandaGed.codClientVar = client.getCodClient();
            CreareComandaGed.numeClientVar = client.getNumeClient();
            CreareComandaGed.tipClient = client.getTipClient();

            DateLivrare.getInstance().setCodJudetD(client.getCodJudet());
            DateLivrare.getInstance().setOrasD(client.getLocalitate());
            DateLivrare.getInstance().setAdresaD(client.getStrada());
            DateLivrare.getInstance().setDiviziiClient(client.getDiviziiClient());

            CreareComandaGed.tipPlataContract = client.getTipPlata();
            DateLivrare.getInstance().setClientBlocat(client.isClientBlocat());

            CreareComandaGed.tipClient = "PJ";
            DateLivrare.getInstance().setTipPersClient("PJ");

            if (client.getTermenPlata() != null)
                CreareComandaGed.listTermenPlata = client.getTermenPlata();

            if (UtilsUser.isCGED() || UtilsUser.isSSCM())
                loadListAgenti(client.getAgenti());

        } else {
            CreareComandaGed.codClientParavan = client.getCodClient();
            CreareComandaGed.numeClientParavan = client.getNumeClient();
            textClientParavan.setText(CreareComandaGed.numeClientParavan);
        }
    }

    private void populateDatePersonale(BeanDatePersonale datePersonale) {

        CreareComandaGed.tipPlataContract = datePersonale.getTipPlata();
        DateLivrare.getInstance().setClientBlocat(datePersonale.isClientBlocat());

        if (datePersonale.getTermenPlata() != null)
            CreareComandaGed.listTermenPlata = datePersonale.getTermenPlata();

        txtNumeClientGed.setText(datePersonale.getNume());
        txtCNPClient.setText(datePersonale.getCnp());

        CreareComandaGed.codClientCUI = "";
        if (datePersonale.getCodClient() != null && !datePersonale.getCodClient().equals("-1"))
            CreareComandaGed.codClientCUI = datePersonale.getCodClient();


        DateLivrare.getInstance().setCodJudetD(datePersonale.getCodjudet());
        DateLivrare.getInstance().setOrasD(datePersonale.getLocalitate());
        DateLivrare.getInstance().setAdresaD(datePersonale.getStrada());
        DateLivrare.getInstance().setDiviziiClient(datePersonale.getDivizii());

        if (radioClPF.isChecked()) {
            DateLivrare.getInstance().setNrTel(datePersonale.getCnp());
            DateLivrare.getInstance().setPersContact(datePersonale.getNume());
        }

    }

    @Override
    public void datePersSelected(Object result) {
        populateDatePersonale((BeanDatePersonale) result);

    }

    private void setCoordonateAdresaANAF(DateClientSap dateClientSap) {

        Address address = new Address();
        address.setCity(dateClientSap.getLocalitate());
        address.setStreet(dateClientSap.getStrada() + " " + dateClientSap.getNumarStrada());
        address.setSector(dateClientSap.getJudet());

        GeocodeAddress geoAddress = MapUtils.geocodeAddress(address, getApplicationContext());
        dateClientSap.setCoordonateAdresa(geoAddress.getCoordinates().latitude + "," + geoAddress.getCoordinates().longitude);

    }

    @Override
    public void clientPJCreated(DateClientSap dateClientSap) {

        setCoordonateAdresaANAF(dateClientSap);
        String serParam = operatiiClient.serializeParamClientPJ(dateClientSap);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("dateClient", serParam);
        operatiiClient.creeazaClientPJ(params);

    }


    @Override
    public void clientPFCreated(DateClientSap dateClientSap) {
        setCoordonateAdresaANAF(dateClientSap);
        setDateContactClientPF(dateClientSap);
        String serParam = operatiiClient.serializeParamClientPF(dateClientSap);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("dateClient", serParam);
        operatiiClient.creeazaClientPF(params);

    }

    private void setDateContactClientPF(DateClientSap dateClientSap) {

        if (dateClientSap == null) {
            DateLivrare.getInstance().setCodJudetD("");
            DateLivrare.getInstance().setOrasD("");
            DateLivrare.getInstance().setAdresaD("");
            DateLivrare.getInstance().setNrTel("");
            DateLivrare.getInstance().setPersContact("");
        } else {
            DateLivrare.getInstance().setCodJudetD(EnumJudete.getCodJudet(dateClientSap.getJudet()));
            DateLivrare.getInstance().setOrasD(dateClientSap.getLocalitate());
            DateLivrare.getInstance().setAdresaD(dateClientSap.getStrada());
            DateLivrare.getInstance().setNrTel(dateClientSap.getTelPersContact());
            DateLivrare.getInstance().setPersContact(dateClientSap.getNumeCompanie());
        }

    }

    private void setTermenPlataClient(String termenPlata) {
        List<String> listTermen = operatiiClient.deserializeTermenPlata(termenPlata);
        if (!listTermen.isEmpty())
            CreareComandaGed.listTermenPlata = listTermen;
    }

}