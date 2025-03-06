package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.DateClientSap;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.listeners.CreareClientPFListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;


public class CreareClientPFDialog extends Dialog implements OperatiiAdresaListener {

    private ImageButton btnSave, btnCancel;
    private CreareClientPFListener listener;
    private DateClientSap dateClientSap;
    private String codClientNominal;
    private Context context;
    private EditText textNumePF, textTelefon, textTelContact, textEmail;
    private AutoCompleteTextView textLocalitate, textStrada;
    private Spinner spinnerJudet;
    private OperatiiAdresa operatiiAdresa;

    public CreareClientPFDialog(DateClientSap dateClientSap, Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.creare_client_pf_dialog);
        setCancelable(false);
        this.dateClientSap = dateClientSap;
        this.context = context;

        operatiiAdresa = new OperatiiAdresaImpl(context);
        operatiiAdresa.setOperatiiAdresaListener(this);

        fillData(dateClientSap);
        setupLayout();

    }


    private void fillData(DateClientSap dateClientSap) {

        ((TextView) findViewById(R.id.textInfo)).setText("Acest client nu exista in baza de date. Completati datele solicitate mai jos pentru a continua comanda.");

        textNumePF = findViewById(R.id.textNumePF);
        textTelefon = findViewById(R.id.textTelefon);
        textEmail = findViewById(R.id.textEmail);
        spinnerJudet = findViewById(R.id.spinnerJudet);
        textLocalitate = findViewById(R.id.textLocalitate);
        textStrada = findViewById(R.id.textStrada);

        textNumePF.setText(dateClientSap.getNumePersContact());

        if (UtilsFormatting.isNumeric(dateClientSap.getTelPersContact()))
            textTelefon.setText(dateClientSap.getTelPersContact());

        fillSpinnerJudete();
        setSpinnerJudetListener();

    }


    private void fillSpinnerJudete() {

        ArrayList<HashMap<String, String>> listJudete = new ArrayList<HashMap<String, String>>();
        SimpleAdapter adapterJudete = new SimpleAdapter(context, listJudete, R.layout.row_layout, new String[]{"rowText"}, new int[]{R.id.textNumeItem});

        HashMap<String, String> temp;

        temp = new HashMap<>();
        temp.put("rowText", "Selectati un judet");
        listJudete.add(temp);

        for (EnumJudete enumJ : EnumJudete.values()) {
            temp = new HashMap<>();
            temp.put("rowText", enumJ.name());
            listJudete.add(temp);
        }

        spinnerJudet.setAdapter(adapterJudete);

    }

    private void setSpinnerJudetListener() {
        spinnerJudet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerJudet.getSelectedItemPosition() > 0) {

                    HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();

                    HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
                    params.put("codJudet", EnumJudete.getCodJudet(tempMap.get("rowText")));
                    operatiiAdresa.getAdreseJudet(params, EnumLocalitate.LOCALITATE_SEDIU);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupLayout() {

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        setListenerOkButton();
        setListenerCancelButton();


    }

    private void setListenerOkButton() {
        btnSave.setOnClickListener(v -> {

            if (!isDateValide())
                return;

            fillDateClientSap(dateClientSap);

            if (listener != null) {
                listener.clientPFCreated(dateClientSap);
                dismiss();
            }

        });
    }

    private void fillDateClientSap(DateClientSap dateClientSap) {
        dateClientSap.setNumeCompanie(textNumePF.getText().toString().trim());
        dateClientSap.setTelPersContact(textTelefon.getText().toString().trim());
        dateClientSap.setJudet(((HashMap<String, String>) spinnerJudet.getSelectedItem()).get("rowText"));
        dateClientSap.setLocalitate(textLocalitate.getText().toString().trim());
        dateClientSap.setStrada(textStrada.getText().toString().trim());
        dateClientSap.setEmailCompanie(textEmail.getText().toString().trim());

    }

    private void setListenerCancelButton() {
        btnCancel.setOnClickListener(v -> dismiss());
    }

    public void setCreareClientPFListener(CreareClientPFListener listener) {
        this.listener = listener;
    }

    private boolean isDateValide() {

        if (textNumePF.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati numele clientului.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textTelefon.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati numarul de telefon.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textTelefon.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Numar de telefon invalid.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (spinnerJudet.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Selectati judetul.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textLocalitate.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati localitatea.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void populateListLocalitati(BeanAdreseJudet listAdrese) {

        textLocalitate.setText("");
        textStrada.setText("");

        String[] arrayLocalitati = listAdrese.getListStringLocalitati().toArray(new String[listAdrese.getListStringLocalitati().size()]);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

        textLocalitate.setThreshold(0);
        textLocalitate.setAdapter(adapterLoc);

        String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
        ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

        textStrada.setThreshold(0);
        textStrada.setAdapter(adapterStrazi);

    }

    @Override
    public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {
        populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));

    }
}
