package my.logon.screen.screens;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanDocumentRetur;
import my.logon.screen.enums.EnumRetur;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.enums.EnumTipOp;
import my.logon.screen.listeners.DocumentReturListener;
import my.logon.screen.listeners.ListaArtReturListener;
import my.logon.screen.listeners.ListaDocReturListener;
import my.logon.screen.listeners.OperatiiReturListener;
import my.logon.screen.model.ClientReturListener;
import my.logon.screen.model.OperatiiReturMarfa;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class ReturComanda extends FragmentActivity implements ClientReturListener, OperatiiReturListener, DocumentReturListener {

    private ViewPager viewPager;

    private OperatiiReturMarfa opRetur;
    private ListaDocReturListener docReturListener;
    private ListaArtReturListener artReturListener;

    private ClientReturComanda clientiReturMarfa;
    private DocumenteReturMarfa documenteReturMarfa;
    private DateLivrareReturComanda dateLivrareReturComanda;
    private ArticoleReturComanda articoleReturComanda;
    private String numeClient, codClient;
    private String nrDocument;
    private BeanDocumentRetur docSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.LRTheme);
        setContentView(R.layout.retur_marfa);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        opRetur = new OperatiiReturMarfa(this);
        opRetur.setOperatiiReturListener(this);

        clientiReturMarfa = ClientReturComanda.newInstance();
        documenteReturMarfa = DocumenteReturMarfa.newInstance();
        dateLivrareReturComanda = DateLivrareReturComanda.newInstance();
        articoleReturComanda = ArticoleReturComanda.newInstance();

        docReturListener = documenteReturMarfa;
        artReturListener = articoleReturComanda;

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle("Retur comenzi");
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<Fragment> fragments = getFragments();
        viewPager = (ViewPager) findViewById(R.id.returviewpager);
        final ReturMarfaPagerAdapter returAdapter = new ReturMarfaPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(returAdapter);
        viewPager.setOffscreenPageLimit(4);

    }

    private void CreateMenu(Menu menu) {
        MenuItem mnu1 = menu.add(0, 0, 0, "");
        mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        mnu1.setIcon(R.drawable.arrow_left);

        MenuItem mnu2 = menu.add(0, 1, 1, "");
        mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        mnu2.setIcon(R.drawable.arrow_right);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                return true;
            case 1:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                return true;

            case android.R.id.home:
                returnToHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(clientiReturMarfa);
        fragmentList.add(documenteReturMarfa);
        fragmentList.add(dateLivrareReturComanda);
        fragmentList.add(articoleReturComanda);

        return fragmentList;

    }

    class ReturMarfaPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;

        public ReturMarfaPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        public int getCount() {
            return this.fragments.size();
        }
    }

    private void returnToHome() {
        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(nextScreen);
        finish();
    }

    public void clientSelected(String codClient, String numeClient, String interval, EnumTipComanda tipComanda) {
        this.numeClient = numeClient;
        this.codClient = codClient;

        String codDepart = UserInfo.getInstance().getCodDepart();
        if (UtilsUser.isUserGed() || UtilsUser.isUserIP() || tipComanda == EnumTipComanda.GED)
            codDepart = "11";

        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("codClient", codClient);
        params.put("codDepartament", codDepart);
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("tipDocument", "CMD");
        params.put("interval", interval);
        params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
        opRetur.getDocumenteClient(params);
    }

    public void documentSelected(BeanDocumentRetur documentRetur) {

        dateLivrareReturComanda.setTipTransportComanda(documentRetur.getTipTransport());
        dateLivrareReturComanda.setDataLivrareComanda(documentRetur.getDataLivrare());

        if (documentRetur.isCmdACZC()) {
            Toast.makeText(getApplicationContext(), "Comanda AC/ZC. Trebuie sa existe un proces verbal de angajament semnat de client.", Toast.LENGTH_LONG).show();
        }

        this.nrDocument = documentRetur.getNumar();
        this.docSelected = documentRetur;
        HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
        params.put("nrDocument", nrDocument);
        params.put("tipDocument", "CMD");
        opRetur.getArticoleDocument(params);

    }

    @Override
    public void documentSelected(String nrDocument, EnumTipOp tipOp) {

    }

    private void displayDocumenteRetur(List<BeanDocumentRetur> listaDocumente) {
        if (listaDocumente.size() > 0) {
            docReturListener.setListDocRetur(numeClient, listaDocumente);
            viewPager.setCurrentItem(1, true);
        } else {
            Toast.makeText(this, "Nu exista documente", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {

        UserInfo.getInstance().setParentScreen("");
        Intent nextScreen = new Intent(this, MainMenu.class);
        startActivity(nextScreen);

        finish();
        return;
    }

    public void operationReturComplete(EnumRetur methodName, Object result) {
        switch (methodName) {
            case GET_LISTA_DOCUMENTE:
                displayDocumenteRetur(opRetur.deserializeListDocumente((String) result));
                break;
            case GET_ARTICOLE_DOCUMENT:
                artReturListener.setListArtRetur(nrDocument, opRetur.deserializeListArticole((String) result), codClient, numeClient);
                dateLivrareReturComanda.setListAdreseLivrare(docSelected.getListAdrese());
                dateLivrareReturComanda.setPersoaneContact(docSelected.getListPersoane());
                viewPager.setCurrentItem(2, true);
                break;
            default:
                break;
        }

    }

    @Override
    public void documentSelected(String nrDocument) {


    }
}
