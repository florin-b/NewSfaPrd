package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import my.logon.screen.R;
import my.logon.screen.beans.BeanTipClient;
import my.logon.screen.beans.DateClientSap;
import my.logon.screen.listeners.CreareClientPJListener;


public class CreareClientPJDialog extends Dialog {

    private ImageButton btnSave, btnCancel;
    private CreareClientPJListener listener;
    private DateClientSap dateClientSap;
    private String codClientNominal;
    private Context context;
    private EditText textPrenumePersContact, textNumePersContact, textTelContact, textEmail;
    private Spinner spinnerTipClient;

    public CreareClientPJDialog(DateClientSap dateClientSap, Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.creare_client_pj_dialog);
        setCancelable(false);
        this.dateClientSap = dateClientSap;
        this.context = context;
        fillData(dateClientSap);
        setupLayout();

    }


    private void fillData(DateClientSap dateClientSap) {

        ((TextView) findViewById(R.id.textInfo)).setText("Acest client nu exista in baza de date. Completati datele solicitate mai jos pentru a continua comanda. " +
                "Campurile completate deja au fost preluate de la ANAF.");

        ((TextView) findViewById(R.id.textNumePJ)).setText(dateClientSap.getNumeCompanie());
        ((TextView) findViewById(R.id.textCUI)).setText(dateClientSap.getCui());
        ((TextView) findViewById(R.id.textJudet)).setText(dateClientSap.getJudet());
        ((TextView) findViewById(R.id.textLocalitate)).setText(dateClientSap.getLocalitate());
        ((TextView) findViewById(R.id.textStrada)).setText(dateClientSap.getStrada() + " " + dateClientSap.getNumarStrada());
        ((TextView) findViewById(R.id.textCodJ)).setText(dateClientSap.getCodJ());
        ((TextView) findViewById(R.id.textPlatTVA)).setText(dateClientSap.getPlatitorTVA() ? "Da" : "Nu");

        textPrenumePersContact = findViewById(R.id.textPrenumePersContact);
        textNumePersContact = findViewById(R.id.textNumePersContact);
        textTelContact = findViewById(R.id.textTelContact);
        textEmail = findViewById(R.id.textEmail);
        spinnerTipClient = findViewById(R.id.spinnerTipClient);

        BeanTipClient tipClient0 = new BeanTipClient();
        tipClient0.setCod("00");
        tipClient0.setNume("Selectati tipul de client");
        dateClientSap.getListTipClient().add(0, tipClient0);

        ArrayAdapter<BeanTipClient> adapter = new ArrayAdapter<>(context, R.layout.simple_drop_down_1, dateClientSap.getListTipClient());
        spinnerTipClient.setAdapter(adapter);

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
                listener.clientPJCreated(dateClientSap);
                dismiss();
            }

        });
    }

    private void fillDateClientSap(DateClientSap dateClientSap) {
        dateClientSap.setPrenumePersContact(textPrenumePersContact.getText().toString().trim());
        dateClientSap.setNumePersContact(textNumePersContact.getText().toString().trim());
        dateClientSap.setTelPersContact(textTelContact.getText().toString().trim());
        dateClientSap.setEmailCompanie(textEmail.getText().toString().trim());
        dateClientSap.setTipClientSelect(((BeanTipClient) spinnerTipClient.getSelectedItem()).getCod());
    }

    private void setListenerCancelButton() {
        btnCancel.setOnClickListener(v -> dismiss());
    }

    public void setCreareClientPJListener(CreareClientPJListener listener) {
        this.listener = listener;
    }

    private boolean isDateValide() {

        if (textPrenumePersContact.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati prenumele persoanei de contact.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textNumePersContact.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati numele persoanei de contact.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textTelContact.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati numarul de telefon.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textTelContact.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Numar de telefon invalid.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (textEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Completati adresa de email.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (spinnerTipClient.getSelectedItemPosition() == 0) {
            Toast.makeText(context, "Selectati tipul de client.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
