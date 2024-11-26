package my.logon.screen.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.net.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterPozaArtRetur;
import my.logon.screen.adapters.ArticoleReturComandaAdapter;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.ArticolCalculDesc;
import my.logon.screen.beans.ArticolDescarcare;
import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.beans.BeanComandaRetur;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.PozaArticol;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumMotivRespArticol;
import my.logon.screen.enums.EnumRetur;
import my.logon.screen.enums.EnumTipRetur;
import my.logon.screen.helpers.HelperCostDescarcare;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.ListaArtReturListener;
import my.logon.screen.listeners.OperatiiReturListener;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.OperatiiReturMarfa;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsGeneral;

public class ArticoleReturComanda extends Fragment implements ListaArtReturListener, OperatiiReturListener, ComenziDAOListener {

    protected static final int PICKFILE_RESULT_CODE = 0;
    TextView textDocument;
    ListView listArticoleRetur;
    TextView textNumeArticol, textCodArticol, textCantArticol, textUmArticol;
    Button saveArtRetur, cancelArtRetur, saveReturBtn;
    EditText textReturArticol;
    List<BeanArticolRetur> listArticole;
    BeanArticolRetur selectedArticol;
    LinearLayout layoutRetur;

    String[] tipTransport = {"Selectati tip transport", "TRAP - Transport Arabesque", "TCLI - Transport client", "TERT - Transport curier"};
    ProgressBar saveReturProgress;
    private int progressVal = 0;
    private Timer myTimer;
    private Handler myHandler = new Handler();
    private String nrDocument, codClient, numeClient;
    private OperatiiReturMarfa opRetur;
    TextView selectIcon;
    private Spinner spinnerMotivRetur;
    private CheckBox checkboxInlocuire;
    private Button btnAdaugaPoze;
    private ListView listViewPoze;
    private AdapterPozaArtRetur adapterPoze;

    private Button macaraBtn;
    private ComenziDAO comandaDAO;
    private static final String codArtUzuraPalet = "88888888";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.articole_retur_comanda, container, false);

        opRetur = new OperatiiReturMarfa(getActivity());
        opRetur.setOperatiiReturListener(this);

        layoutRetur = (LinearLayout) v.findViewById(R.id.layoutRetur);
        layoutRetur.setVisibility(View.GONE);

        textDocument = (TextView) v.findViewById(R.id.textDocument);
        listArticoleRetur = (ListView) v.findViewById(R.id.listArticoleRetur);
        addListenerListArticole();

        textNumeArticol = (TextView) v.findViewById(R.id.textNumeArticol);
        textCodArticol = (TextView) v.findViewById(R.id.textCodArticol);
        textCantArticol = (TextView) v.findViewById(R.id.textCantArticol);
        textUmArticol = (TextView) v.findViewById(R.id.textUmArticol);

        saveArtRetur = (Button) v.findViewById(R.id.saveArtRetur);
        addListenerSaveArt();

        cancelArtRetur = (Button) v.findViewById(R.id.cancelArtRetur);
        addListenerCancelArt();

        textReturArticol = (EditText) v.findViewById(R.id.textReturArticol);

        saveReturBtn = (Button) v.findViewById(R.id.saveReturBtn);
        saveReturBtn.setVisibility(View.INVISIBLE);
        addListenerSaveReturBtn();

        selectIcon = (TextView) v.findViewById(R.id.selectIcon);
        selectIcon.setVisibility(View.INVISIBLE);

        saveReturProgress = (ProgressBar) v.findViewById(R.id.progress_bar_retur);
        saveReturProgress.setVisibility(View.INVISIBLE);

        comandaDAO = ComenziDAO.getInstance(getActivity());
        comandaDAO.setComenziDAOListener(this);

        macaraBtn = (Button) v.findViewById(R.id.macaraBtn);
        macaraBtn.setVisibility(View.INVISIBLE);
        setListenerMacaraBtn();

        spinnerMotivRetur = (Spinner) v.findViewById(R.id.spinnerMotivRetur);
        populateSpinnerMotivRetur();
        setListenerSpinnerRetur();

        checkboxInlocuire = (CheckBox) v.findViewById(R.id.checkboxInlocuire);
        setListenerCheckInlocuire();

        btnAdaugaPoze = (Button) v.findViewById(R.id.btnAdaugaPoze);
        setListenerAdaugaPoze();

        listViewPoze = (ListView) v.findViewById(R.id.listViewPoze);
        adapterPoze = new AdapterPozaArtRetur(new ArrayList<PozaArticol>(), getActivity());

        return v;
    }

    private void setListenerMacaraBtn() {
        macaraBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                verificaPretMacara();

            }
        });
    }

    private void setListenerAdaugaPoze() {
        btnAdaugaPoze.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {

            if (selectedArticol.getPozeArticol().size() < 3) {

                PozaArticol pozaArticol = new PozaArticol();
                getImageMetaData(data.getData(), pozaArticol);
                pozaArticol.setStrData(fileToString(data.getData()));
                List<PozaArticol> tempListPoze = selectedArticol.getPozeArticol();

                tempListPoze.add(pozaArticol);
                selectedArticol.setPozeArticol(tempListPoze);
                adapterPoze.setPozeArticol(tempListPoze);
                listViewPoze.setAdapter(adapterPoze);

            }

        }

    }

    private String fileToString(Uri uri) {

        Bitmap bitmap;
        String base64 = "";
        try {

            InputStream fis = getActivity().getContentResolver().openInputStream(uri);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap = BitmapFactory.decodeStream(fis);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = fis.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
            fis.close();

            base64 = Base64.encodeBase64String(outputStream.toByteArray());

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return base64;

    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }

    public void getImageMetaData(Uri uri, PozaArticol pozaArticol) {

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        try {

            if (cursor != null && cursor.moveToFirst()) {

                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                pozaArticol.setNume(displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }

                pozaArticol.setDim(Long.valueOf(size));

            }
        } finally {
            cursor.close();
        }
    }

    private void populateSpinnerMotivRetur() {
        List<String> listMotive = EnumMotivRespArticol.getStringList();
        ArrayAdapter<String> adapterMotive = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listMotive);
        listMotive.add(0, "Selectati motiv retur");
        spinnerMotivRetur.setAdapter(adapterMotive);

    }

    private void setListenerSpinnerRetur() {
        spinnerMotivRetur.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String codMotiv = EnumMotivRespArticol.getCodRetur(spinnerMotivRetur.getSelectedItem().toString());

                    selectedArticol.setMotivRespingere(codMotiv);

                    if (codMotiv.equals("5") || codMotiv.equals("6")) {
                        checkboxInlocuire.setChecked(false);
                        checkboxInlocuire.setEnabled(false);
                    } else
                        checkboxInlocuire.setEnabled(true);

                } else
                    selectedArticol.setMotivRespingere("");
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setListenerCheckInlocuire() {
        checkboxInlocuire.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (textReturArticol.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), "Completati cantitatea de retur.", Toast.LENGTH_LONG).show();
                        checkboxInlocuire.setChecked(false);
                        return;
                    }

                    getStocReturAvansat();
                }

                selectedArticol.setInlocuire(isChecked);

            }
        });
    }

    private void getStocReturAvansat() {
        HashMap<String, String> params = new HashMap<>();
        params.put("nrDocument", nrDocument);
        params.put("codArticol", selectedArticol.getCod());
        params.put("um", selectedArticol.getUm());
        opRetur.getStocReturAvansat(params);
    }

    private void validateStocRetur(String stocAvansat) {

        double cantRetur = Double.valueOf(textReturArticol.getText().toString().trim());
        double cantStoc = Double.valueOf(stocAvansat);

        if (cantStoc < cantRetur) {
            Toast.makeText(getContext(), "Stoc insuficient pentru inlocuire.", Toast.LENGTH_LONG).show();
            checkboxInlocuire.setChecked(false);
            selectedArticol.setInlocuire(false);
        }
    }

    public static ArticoleReturComanda newInstance() {
        ArticoleReturComanda frg = new ArticoleReturComanda();
        Bundle bdl = new Bundle();
        frg.setArguments(bdl);
        return frg;
    }

    private void addListenerSaveArt() {
        saveArtRetur.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (isCantValid() && isMotivReturSelected() && isMotivReturValid()) {
                    selectedArticol.setCantitateRetur(Double.valueOf(textReturArticol.getText().toString()));
                    updateListArticole(selectedArticol);
                    showPanel("selectItem");
                }
            }
        });
    }

    private boolean isMotivReturSelected() {
        if (spinnerMotivRetur.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Selectati motivul de retur.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isMotivReturValid() {

        try {

            Date dataLivrare = new SimpleDateFormat("yyyyMMdd").parse(DateLivrareReturComanda.dataLivrareComanda);
            int dateDiff = UtilsDates.dateDiffinDays2(dataLivrare);

            String codMotiv = EnumMotivRespArticol.getCodRetur(spinnerMotivRetur.getSelectedItem().toString());

            if ((codMotiv.equals("4") || codMotiv.equals("6") || codMotiv.equals("7")) && dateDiff > 30) {
                showInfoMotivReturDialog(30);
                return false;
            } else if ((codMotiv.equals("1") || codMotiv.equals("2") || codMotiv.equals("3") || codMotiv.equals("5")) && dateDiff > 10) {
                showInfoMotivReturDialog(10);
                return false;
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public void showInfoMotivReturDialog(int nrZile) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                "\nPentru acest motiv de retur livrarea trebuia facuta cu cel mult " + nrZile + " zile in urma. \nAceasta comanda s-a livrat in data de "
                        + UtilsDates.formatDateFromSap(DateLivrareReturComanda.dataLivrareComanda) + ".\n").setCancelable(false)
                .setPositiveButton("Inchide", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

    }

    private void addListenerCancelArt() {
        cancelArtRetur.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showPanel("selectItem");
            }
        });
    }

    private void addListenerSaveReturBtn() {
        saveReturBtn.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isValidInput()) {

                            saveReturProgress.setVisibility(View.VISIBLE);
                            saveReturProgress.setProgress(0);
                            progressVal = 0;
                            myTimer = new Timer();
                            myTimer.schedule(new UpdateProgress(), 40, 15);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (saveReturProgress.getVisibility() == View.VISIBLE) {
                            myTimer.cancel();
                            saveReturProgress.setVisibility(View.INVISIBLE);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    private boolean isValidInput() {
        return isDateReturValide() && hasArticolReturCant();
    }

    private boolean isDateReturValide() {

        if (DateLivrareReturComanda.dataRetur.length() == 0) {
            Toast.makeText(getActivity(), "Selectati data retur", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DateLivrareReturComanda.tipTransport.length() == 0) {
            Toast.makeText(getActivity(), "Selectati tipul de transport", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DateLivrareReturComanda.adresaCodJudet.length() == 0) {
            Toast.makeText(getActivity(), "Selectati judetul", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DateLivrareReturComanda.adresaOras.length() == 0) {
            Toast.makeText(getActivity(), "Selectati orasul", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAdresaCorecta()) {
            Toast.makeText(getActivity(), "Completati adresa corect sau pozitionati adresa pe harta.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isAdresaCorecta() {
        if (DateLivrareReturComanda.tipTransport.toUpperCase().equals("TRAP") && DateLivrareReturComanda.isAltaAdresa)
            return isAdresaGoogleOk();
        else
            return true;

    }

    private boolean isAdresaGoogleOk() {
        return MapUtils.geocodeAddress(getAddressFromForm(), getActivity()).isAdresaValida();

    }

    private Address getAddressFromForm() {
        Address address = new Address();

        address.setCity(DateLivrareReturComanda.adresaOras);
        address.setStreet(DateLivrareReturComanda.adresaStrada);
        address.setSector(UtilsGeneral.getNumeJudet(DateLivrareReturComanda.adresaCodJudet));

        return address;
    }

    private boolean hasArticolReturCant() {
        boolean retCant = false;

        for (int i = 0; i < listArticole.size(); i++)
            if (listArticole.get(i).getCantitateRetur() > 0)
                retCant = true;

        if (!retCant)
            Toast.makeText(getActivity(), "Adaugati o cantitate de retur", Toast.LENGTH_SHORT).show();
        return retCant;
    }

    class UpdateProgress extends TimerTask {
        public void run() {
            progressVal++;
            if (saveReturProgress.getProgress() == 50) {
                myHandler.post(new Runnable() {
                    public void run() {

                        performSaveRetur();
                    }
                });
                myTimer.cancel();
            } else {
                saveReturProgress.setProgress(progressVal);
            }
        }
    }

    private BeanArticolRetur getUzuraPalet() {

        BeanArticolRetur uzuraPaleti = new BeanArticolRetur();
        uzuraPaleti.setCod(codArtUzuraPalet);
        uzuraPaleti.setNume("TAXA UZURA PALET");
        uzuraPaleti.setCantitate(1);
        uzuraPaleti.setCantitateRetur(1);
        uzuraPaleti.setUm("BUC");
        uzuraPaleti.setMotivRespingere(selectedArticol.getMotivRespingere());

        return uzuraPaleti;

    }

    private void removeArticolUzura(){

        Iterator<BeanArticolRetur> listIterator = listArticole.iterator();

        while (listIterator.hasNext()) {
            BeanArticolRetur artRetur = listIterator.next();

            if (artRetur.getCod().equals(codArtUzuraPalet))
                listIterator.remove();
        }

    }

    private void checkTaxaUzuraPaleti(){

        double localTaxaUzuraPaleti = UtilsComenzi.getTaxaUzuraPaleti(listArticole);

        if (localTaxaUzuraPaleti > 0)
            showInfoTaxaUzuraPaleti(localTaxaUzuraPaleti);
        else
            performSaveRetur();

    }

    public void showInfoTaxaUzuraPaleti(double taxaUzuraPaleti) {

        NumberFormat nf = new DecimalFormat("#0.00");

        StringBuilder alertMsg = new StringBuilder();
        alertMsg.append("\n");
        alertMsg.append("Pentru aceasta comanda se va percepe o taxa de uzura paleti in valoare de " + nf.format(taxaUzuraPaleti) + " lei.");
        alertMsg.append("\n");
        alertMsg.append("\n");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMsg.toString()).setCancelable(false).setPositiveButton("Salveaza", (dialog, id) -> performSaveRetur()).
                setNegativeButton("Anuleaza", (dialog, id) -> dialog.cancel()).setTitle("Atentie!").setIcon(R.drawable.warning96).setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean isComandaPaleti(){
        for (BeanArticolRetur artRetur : listArticole){
            if (artRetur.getCantitateRetur() > 0 && artRetur.getCategMat() != null &&
                    !artRetur.getCategMat().equals("PA") && !artRetur.getCategMat().equals("AM"))
                return false;
        }

        return true;
    }

    private void performSaveRetur() {

        if (isComandaPaleti()) {
            Snackbar.make(getView(), "Comanda de retur nu poate sa contina doar paleti.", Snackbar.LENGTH_LONG).show();
            return;
        }

        BeanComandaRetur comandaRetur = new BeanComandaRetur();
        comandaRetur.setNrDocument(nrDocument);
        comandaRetur.setDataLivrare(DateLivrareReturComanda.dataRetur);
        comandaRetur.setTipTransport(DateLivrareReturComanda.tipTransport);
        comandaRetur.setCodAgent(UserInfo.getInstance().getCod());
        comandaRetur.setTipAgent(UserInfo.getInstance().getTipUserSap());
        comandaRetur.setMotivRespingere(" ");
        comandaRetur.setNumePersContact(DateLivrareReturComanda.numePersContact);
        comandaRetur.setTelPersContact(DateLivrareReturComanda.telPersContact);
        comandaRetur.setAdresaCodJudet(DateLivrareReturComanda.adresaCodJudet);
        comandaRetur.setAdresaOras(DateLivrareReturComanda.adresaOras);
        comandaRetur.setAdresaStrada(DateLivrareReturComanda.adresaStrada);
        comandaRetur.setAdresaCodAdresa(DateLivrareReturComanda.adresaCodAdresa);
        comandaRetur.setListArticole(opRetur.serializeListArticole(listArticole));
        comandaRetur.setObservatii(DateLivrareReturComanda.observatii);
        comandaRetur.setCodClient(codClient);
        comandaRetur.setNumeClient(numeClient);

        if (DateLivrareReturComanda.tipTransport.equals("TRAP"))
            comandaRetur.setTransBack(DateLivrareReturComanda.transBack);
        else
            comandaRetur.setTransBack(false);

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("dateRetur", opRetur.serializeComandaRetur(comandaRetur));
        params.put("tipRetur", EnumTipRetur.COMANDA.getTipRetur());

        opRetur.saveComandaRetur(params);

    }

    private void verificaPretMacara() {

        List<ArticolCalculDesc> artCalcul = HelperCostDescarcare.getDateCalculDescarcareRetur(listArticole);
        String listArtSer = comandaDAO.serializeArtCalcMacara(artCalcul);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("codClient", codClient);
        params.put("listArt", listArtSer);

        comandaDAO.getCostMacara(params);

    }

    private void trateazaPretMacara(String result) {

        CostDescarcare costDescarcare = HelperCostDescarcare.deserializeCostMacara(result);

        if (costDescarcare.getSePermite() && costDescarcare.getValoareDescarcare() > 0) {

            for (ArticolDescarcare artDesc : costDescarcare.getArticoleDescarcare()) {

                Iterator<BeanArticolRetur> iterator = listArticole.iterator();

                while (iterator.hasNext()) {

                    BeanArticolRetur articol = iterator.next();
                    if (articol.getCod().equals(artDesc.getCod().replaceFirst("^0*", "")))
                        iterator.remove();
                }
            }

            for (ArticolDescarcare artDesc : costDescarcare.getArticoleDescarcare()) {

                BeanArticolRetur artRetur = new BeanArticolRetur();
                artRetur.setCod(artDesc.getCod().replaceFirst("^0*", ""));
                artRetur.setNume("PREST.SERV.DESCARCARE PALET DIV " + artDesc.getDepart());
                artRetur.setCantitateRetur(artDesc.getCantitate());
                artRetur.setCantitate(artDesc.getCantitate());
                artRetur.setUm("BUC");
                listArticole.add(artRetur);

            }

            populateListArticole(listArticole);
            Toast.makeText(getActivity(), "Serviciul de descarcare a fost adaugat.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(), "Pentru aceste articole serviciul de descarcare nu este acceptat.", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isCantValid() {
        if (textReturArticol.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Cantitate invalida", Toast.LENGTH_LONG).show();
            return false;
        }

        double cantitate = Double.valueOf(textCantArticol.getText().toString());
        double retur = Double.valueOf(textReturArticol.getText().toString().trim());

        if (retur > cantitate) {
            Toast.makeText(getActivity(), "Cantitate invalia", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void updateListArticole(BeanArticolRetur articol) {

        int artPosition = 0;
        for (int i = 0; i < listArticole.size(); i++) {
            if (articol == listArticole.get(i)) {
                artPosition = i;
                break;
            }
        }

        listArticole.set(artPosition, articol);
        populateListArticole(listArticole);
        listArticoleRetur.setSelection(artPosition);
        clearArtFields();

    }

    private void clearArtFields() {
        textNumeArticol.setText("");
        textCodArticol.setText("");
        textCantArticol.setText("");
        textUmArticol.setText("");
        textReturArticol.setText("");
    }

    private void addListenerListArticole() {
        listArticoleRetur.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BeanArticolRetur articol = (BeanArticolRetur) arg0.getAdapter().getItem(arg2);
                selectedArticol = articol;
                showArticolData(articol);
                showPanel("cantitateRetur");

            }
        });
    }

    private void showPanel(String panel) {
        if (panel.equals("cantitateRetur")) {

            layoutRetur.setVisibility(View.VISIBLE);
            selectIcon.setVisibility(View.GONE);
        }

        if (panel.equals("selectItem")) {
            layoutRetur.setVisibility(View.GONE);
            selectIcon.setVisibility(View.VISIBLE);
        }

    }

    private void emptyScreen() {
        layoutRetur.setVisibility(View.GONE);
        selectIcon.setVisibility(View.GONE);
        saveReturBtn.setVisibility(View.INVISIBLE);
        populateListArticole(new ArrayList<BeanArticolRetur>());

    }

    private void showArticolData(BeanArticolRetur articol) {
        textNumeArticol.setText(articol.getNume());
        textCodArticol.setText(articol.getCod());
        textCantArticol.setText(String.valueOf(articol.getCantitate()));
        textUmArticol.setText(articol.getUm());

        if (articol.getMotivRespingere() == null)
            spinnerMotivRetur.setSelection(0);
        else {
            int nrItems = spinnerMotivRetur.getAdapter().getCount();

            for (int i = 0; i < nrItems; i++) {

                String codMotiv = EnumMotivRespArticol.getCodRetur(spinnerMotivRetur.getAdapter().getItem(i).toString());
                if (codMotiv.equals(articol.getMotivRespingere())) {
                    spinnerMotivRetur.setSelection(i);
                    break;
                }

            }
        }

        checkboxInlocuire.setChecked(articol.isInlocuire());

        adapterPoze.setPozeArticol(articol.getPozeArticol());
        listViewPoze.setAdapter(adapterPoze);

        if (articol.getCantitateRetur() > 0)
            textReturArticol.setText(String.valueOf(articol.getCantitateRetur()));

    }

    public void setListArtRetur(String nrDocument, List<BeanArticolRetur> listArticole, String codClient, String numeClient) {
        this.listArticole = listArticole;
        this.nrDocument = nrDocument;
        this.codClient = codClient;
        this.numeClient = numeClient;

        saveReturBtn.setVisibility(View.VISIBLE);
        showPanel("selectItem");
        textDocument.setText("Articole");
        populateListArticole(listArticole);
    }

    @Override
    public void setDocumentReturPaletiInstance(DocumenteReturPaleti instance) {

    }

    private void populateListArticole(List<BeanArticolRetur> listArticole) {
        ArticoleReturComandaAdapter adapter = new ArticoleReturComandaAdapter(getActivity(), listArticole);
        listArticoleRetur.setAdapter(adapter);
    }

    private void showCmdStatus(String response) {
        if (response.equals("0")) {
            Toast.makeText(getActivity(), "Date salvate", Toast.LENGTH_SHORT).show();
            emptyScreen();
        } else {
            Toast.makeText(getActivity(), "Eroare: " + response, Toast.LENGTH_LONG).show();
        }

    }

    public void operationReturComplete(EnumRetur methodName, Object result) {
        switch (methodName) {
            case SAVE_COMANDA_RETUR:
                showCmdStatus((String) result);
                break;
            case GET_STOC_RETUR_AVANSAT:
                validateStocRetur((String) result);
                break;
            default:
                break;
        }

    }

    @Override
    public void operationComenziComplete(EnumComenziDAO methodName, Object result) {

        switch (methodName) {
            case GET_COST_MACARA:
                trateazaPretMacara((String) result);
                break;
            default:
                break;
        }

    }

}
