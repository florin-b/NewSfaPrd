/**
 * @author florinb
 *
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
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanDatePersonale;
import my.logon.screen.beans.DetaliiClient;
import my.logon.screen.beans.FurnizorComanda;
import my.logon.screen.beans.InfoCredit;
import my.logon.screen.beans.PlatitorTva;
import my.logon.screen.dialogs.CautaClientDialog;
import my.logon.screen.dialogs.DatePersClientDialog;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.DatePersListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsCheck;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectClientCmdGed extends Activity implements OperatiiClientListener, CautaClientDialogListener, DatePersListener {

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

		layoutClientPersoana = (LinearLayout) findViewById(R.id.layoutClientPersoana);
		layoutClientPersoana.setVisibility(View.GONE);

		layoutClientDistrib = (LinearLayout) findViewById(R.id.layoutClientDistrib);
		layoutClientDistrib.setVisibility(View.VISIBLE);

		layoutDetaliiClientDistrib = (LinearLayout) findViewById(R.id.detaliiClientDistrib);
		layoutDetaliiClientDistrib.setVisibility(View.GONE);


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
		labelIDClient.setText("CNP");

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
		layoutRezervStocLabel.setVisibility(View.INVISIBLE);

		layoutRezervStocBtn = (LinearLayout) findViewById(R.id.layoutRezervStocBtn);
		layoutRezervStocBtn.setVisibility(View.INVISIBLE);

		textClientParavan = (TextView) findViewById(R.id.textClientParavan);

		clientBtn = (Button) findViewById(R.id.clientBtn);
		addListenerClientBtn();

		radioSelectAgent = (RadioGroup) findViewById(R.id.radio_select_agent);
		setRadioSelectClientListener();

		if (isCasiera()) {
			radioClPF.setVisibility(View.INVISIBLE);
			radioClPJ.setChecked(true);

			((LinearLayout) findViewById(R.id.layoutClientParavan)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutDateClient)).setVisibility(View.INVISIBLE);

			labelIDClient.setVisibility(View.INVISIBLE);

			((LinearLayout) findViewById(R.id.layoutLabelJ)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutTextJ)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutTipComanda)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutRadioTipComanda)).setVisibility(View.INVISIBLE);

			spinnerAgenti = ((Spinner) findViewById(R.id.spinnerAgenti));

			setSpinnerAgentiListener();

			if (CreareComandaGed.codClientVar.isEmpty())
				cautaClientDistributie("");
			else
				txtNumeClientGed.setText(CreareComandaGed.numeClientVar);

		}

	}

	private boolean isCasiera() {
		return UserInfo.getInstance().getTipUserSap().equals("CGED") || UtilsUser.isSSCM();
	}

	private void setRadioSelectClientListener() {

		radioSelectAgent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {

				case R.id.radio_ag_lista:
					break;
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
				pressedTVAButton = true;
				performVerificareTVA();

			}
		});
	}

	private void performVerificareTVA() {

		String strCui = "";

		if (radioClientInstPub.isChecked()) {
			strCui = codCuiIp;
		} else if (!txtCNPClient.getText().toString().isEmpty()) {
			strCui = txtCNPClient.getText().toString().trim();
		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cuiClient", strCui);
		operatiiClient.getStarePlatitorTva(params);

	}

	private void setListenerFacturaPF() {
		checkFacturaPF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkFacturaPF.isChecked())
					checkFacturaPF.setText("Se emite factura");
				else
					checkFacturaPF.setText("Nu se emite factura");

			}
		});
	}

	private void setListenerVerificaID() {

		verificaID.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkCNP(true);

			}
		});

	}

	private boolean checkCNP(boolean showValidMessage) {

		if (UtilsCheck.isCnpValid(txtCNPClient.getText().toString().trim())) {

			if (showValidMessage)
				Toast.makeText(getApplicationContext(), "CNP valid", Toast.LENGTH_SHORT).show();

			return true;
		} else {

			if (showValidMessage)
				Toast.makeText(getApplicationContext(), "CNP invalid", Toast.LENGTH_SHORT).show();
			return false;
		}

	}

	private void setListenerCautaClientBtn() {
		cautaClientBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (isNumeClientValid())
					getListaClienti();

			}
		});
	}

	private void setListenerCautaClientPFBtn() {
		cautaClientPFBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String textClient = txtNumeClientGed.getText().toString().trim();

				if (UtilsUser.isCGED() || UtilsUser.isSSCM() || (UtilsUser.isAgentOrSD() && radioClientNominal.isChecked())) {
					cautaClientDistributie(textClient);

				} else if (!textClient.isEmpty())
					cautaClientPF(textClient);

			}
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

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("numeClient", textClient);
		params.put("tipClient", getTipClient());
		operatiiClient.getCnpClient(params);

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

	private void updateStareTva(PlatitorTva platitorTva) {

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
			txtNumeClientGed.setText("");
			txtCodJ.setText("");
		} else {
			message = platitorTva.getNumeClient() + stare + " este platitor de tva.";
			txtNumeClientGed.setText(platitorTva.getNumeClient());
			txtCodJ.setText(platitorTva.getNrInreg());

			if (platitorTva.getCodJudet()!= null && !platitorTva.getCodJudet().equals("null") && !platitorTva.getCodJudet().trim().isEmpty()) {
				DateLivrare.getInstance().setCodJudetD(platitorTva.getCodJudet());
				DateLivrare.getInstance().setOrasD(platitorTva.getLocalitate());
				DateLivrare.getInstance().setAdresaD(platitorTva.getStrada());
			}

		}

		Toast.makeText(this, message, Toast.LENGTH_LONG).show();

		if (!pressedTVAButton)
			valideazaDateClient();

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

				}

			}
		});
	}

	private void addListenerRadioCLPF() {

		radioClPF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {

					labelIDClient.setVisibility(View.VISIBLE);
					((LinearLayout) findViewById(R.id.layoutDateClient)).setVisibility(View.VISIBLE);
					((LinearLayout) findViewById(R.id.layoutClientParavan)).setVisibility(View.VISIBLE);

					layoutLabelJ.setVisibility(View.GONE);
					layoutTextJ.setVisibility(View.GONE);
					checkPlatTva.setVisibility(View.INVISIBLE);
					verificaID.setVisibility(View.VISIBLE);
					verificaTva.setVisibility(View.GONE);
					checkFacturaPF.setVisibility(View.VISIBLE);
					labelIDClient.setText("CNP");
					setTextNumeClientEnabled(true);
					clearDateLivrare();


				}

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
		radioCmdNormala.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					CreareComandaGed.tipComanda = "N";
					layoutRezervStocLabel.setVisibility(View.INVISIBLE);
					layoutRezervStocBtn.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	public void addListenerRadioCmdSimulata() {
		radioCmdSimulata.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					CreareComandaGed.tipComanda = "S";
					layoutRezervStocLabel.setVisibility(View.VISIBLE);
					layoutRezervStocBtn.setVisibility(View.VISIBLE);
					radioRezervStocDa.setChecked(true);
				}

			}
		});
	}

	public void addListenerSave() {
		saveClntBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (radioClPJ.isChecked() && !pressedTVAButton && !UtilsUser.isCGED() && !UtilsUser.isSSCM()) {
					performVerificareTVA();
				} else
					valideazaDateClient();

			}
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

			if (radioClPF.isChecked() && hasCnp() && !checkCNP(false)) {
				Toast.makeText(getApplicationContext(), "CNP invalid", Toast.LENGTH_SHORT).show();
				return;
			}

			if (radioClPF.isChecked()) {
				CreareComandaGed.tipClient = "PF";
				DateLivrare.getInstance().setTipPersClient("PF");

				if (!checkFacturaPF.isChecked()) {

					if (UtilsUser.isConsWood()) {
						CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood_faraFact(UserInfo.getInstance().getUnitLog(), "PF");
					} else {
						if (UtilsUser.isUserExceptieCONSGED())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED_faraFactura(UserInfo.getInstance().getUnitLog(), "PF");
						else
							CreareComandaGed.codClientVar = InfoStrings.getClientGed_FaraFactura(UserInfo.getInstance().getUnitLog());

					}

					DateLivrare.getInstance().setFacturaCmd(false);

				} else {

					if (UtilsUser.isUserSite()) {

						if (hasCnp())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
						else
							CreareComandaGed.codClientVar = InfoStrings.getClientCVO_cuFact_faraCnp(UserInfo.getInstance().getUnitLog(), "PF");

					}

					else {
						if (UtilsUser.isConsWood())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PF");
						else {
							if (UtilsUser.isUserExceptieCONSGED())
								CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PF");
							else
								CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
						}
					}

					DateLivrare.getInstance().setFacturaCmd(true);

				}

				CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

			}

			if (radioClPJ.isChecked() && !UtilsUser.isCGED() && !UtilsUser.isSSCM()) {
				CreareComandaGed.tipClient = "PJ";
				DateLivrare.getInstance().setTipPersClient("PJ");

				CreareComandaGed.cnpClient = txtCNPClient.getText().toString().trim();

				if (UtilsUser.isConsWood())
					CreareComandaGed.codClientVar = InfoStrings.getClientGenericGedWood(UserInfo.getInstance().getUnitLog(), "PJ");
				else {
					if (checkPlatTva.isChecked()) {

						CreareComandaGed.cnpClient = "RO" + txtCNPClient.getText().toString().trim();

						if (UtilsUser.isUserExceptieCONSGED())
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed_CONSGED(UserInfo.getInstance().getUnitLog(), "PJ");
						else
							CreareComandaGed.codClientVar = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PJ");
					} else {
						if (UtilsUser.isUserExceptieCONSGED())
							CreareComandaGed.codClientVar = InfoStrings.gedPJNeplatitorTVA_CONSGED(UserInfo.getInstance().getUnitLog());
						else
							CreareComandaGed.codClientVar = InfoStrings.gedPJNeplatitorTVA(UserInfo.getInstance().getUnitLog());
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

			if (radioRezervStocDa.isChecked())
				CreareComandaGed.rezervStoc = true;
			else
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

		if (listDatePers.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
		} else {
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

		SimpleAdapter adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });

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
				} else if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE)){
				}
				else {
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
		if (datePersonale.getCodClient()!= null && !datePersonale.getCodClient().equals("-1"))
			CreareComandaGed.codClientCUI = datePersonale.getCodClient();


		DateLivrare.getInstance().setCodJudetD(datePersonale.getCodjudet());
		DateLivrare.getInstance().setOrasD(datePersonale.getLocalitate());
		DateLivrare.getInstance().setAdresaD(datePersonale.getStrada());

	}

	@Override
	public void datePersSelected(Object result) {
		populateDatePersonale((BeanDatePersonale) result);

	}

	private void setTermenPlataClient(String termenPlata) {
		List<String> listTermen = operatiiClient.deserializeTermenPlata(termenPlata);
		if (!listTermen.isEmpty())
			CreareComandaGed.listTermenPlata = listTermen;
	}

}