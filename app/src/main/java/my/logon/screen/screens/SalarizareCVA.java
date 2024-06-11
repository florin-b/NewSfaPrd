package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.SalarizareDepartConsAdapter;
import my.logon.screen.adapters.SalarizareStocNocivAdapter;
import my.logon.screen.adapters.VenitBazaCvaAdapter;
import my.logon.screen.beans.BeanSalarizareCVA;
import my.logon.screen.dialogs.SelectLunaSalarizareDialog;
import my.logon.screen.dialogs.SelectSituatiiSalarizareDialog;
import my.logon.screen.enums.EnumOperatiiSalarizare;
import my.logon.screen.enums.EnumSituatieSalarizare;
import my.logon.screen.listeners.IntervalSalarizareListener;
import my.logon.screen.listeners.OperatiiSalarizareListener;
import my.logon.screen.listeners.SituatieSalarizareListener;
import my.logon.screen.model.OperatiiSalarizare;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsUser;

public class SalarizareCVA extends Activity implements OperatiiSalarizareListener, IntervalSalarizareListener, SituatieSalarizareListener {

    private ActionBar actionBar;
    private OperatiiSalarizare operatiiSalarizare;
    private String[] listLuni = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie",
            "Decembrie"};

    private String lunaSelect, anSelect;
    private NumberFormat nf;
    private boolean isDetaliiAgent;
    private String numeAgentSelectat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        setTheme(R.style.LRTheme);
        setContentView(R.layout.salarizare_cva);

        actionBar = getActionBar();
        actionBar.setTitle("Salarizare");
        actionBar.setDisplayHomeAsUpEnabled(true);

        operatiiSalarizare = new OperatiiSalarizare(this);
        operatiiSalarizare.setListener(this);

        checkAccess();

        nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        initData();
        setLayoutVisibility(false);
        setDetaliiNocivVisibility(false);


    }


    private void checkAccess() {

        if (UtilsUser.isCVA() && UserInfo.getInstance().getIsMeniuBlocat()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Acces blocat. Folositi modulul Utilizator pentru deblocare.").setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            returnToMainMenu();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();

        }

    }

    private void initData() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -1);

        NumberFormat nf3 = new DecimalFormat("00");

        lunaSelect = nf3.format(c.get(Calendar.MONTH) + 1);
        anSelect = String.valueOf(c.get(Calendar.YEAR));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    private void CreateMenu(Menu menu) {

        MenuItem mnu1 = menu.add(0, 0, 0, "Luna");
        mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        if (UserInfo.getInstance().getTipUserSap().equals("SDCVA") || UserInfo.getInstance().getTipUserSap().equals("SDIP")) {
            MenuItem mnu2 = menu.add(0, 1, 1, "Salarizare");
            mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                SelectLunaSalarizareDialog intervalDialog = new SelectLunaSalarizareDialog(this);
                intervalDialog.setIntervalDialogListener(this);
                intervalDialog.showDialog();
                return true;

            case 1:
                SelectSituatiiSalarizareDialog situatieDialog = new SelectSituatiiSalarizareDialog(this);
                situatieDialog.setSituatieListener(this);
                situatieDialog.showDialog();
                return true;

            case android.R.id.home:
                returnToMainMenu();
                return true;

        }
        return false;
    }




    private void setDateGeneraleVisibility(boolean isVisible) {
        if (isVisible) {
            ((LinearLayout) findViewById(R.id.layoutDateGenerale)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.layoutDateGenerale)).setVisibility(View.GONE);
        }
    }

    private void setSalAgentiVisibility(boolean isVisible) {
        if (isVisible) {
            ((LinearLayout) findViewById(R.id.layoutListAgenti)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.headerSalAgenti)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.layoutListAgenti)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.headerSalAgenti)).setVisibility(View.GONE);

        }
    }

    private void afisSalarizareDepartCVA(String result) {

        setSalAgentiVisibility(true);
        setDateGeneraleVisibility(false);

        setLayoutVisibility(false);
        setDetaliiNocivVisibility(false);

        ((TextView) findViewById(R.id.textDataSalarizare)).setText(listLuni[Integer.parseInt(lunaSelect) - 1] + "  " + anSelect);
        List<BeanSalarizareCVA> listAgenti = operatiiSalarizare.deserializeSalarizareDepartCVA(result);

        ListView listViewAgenti = (ListView) findViewById(R.id.listAgenti);

        SalarizareDepartConsAdapter departAdapter = new SalarizareDepartConsAdapter(listAgenti, getApplicationContext());
        departAdapter.setSalarizareDepartListener(this);
        listViewAgenti.setAdapter(departAdapter);

        listViewAgenti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }

    private void afisSalarizareCVA(String result) {

        if (isDetaliiAgent) {

            ((TextView) findViewById(R.id.textAgentSelectat)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textAgentSelectat)).setText(numeAgentSelectat);
            setDateGeneraleVisibility(false);

        } else {

            ((TextView) findViewById(R.id.textAgentSelectat)).setVisibility(View.GONE);
            setDateGeneraleVisibility(true);
            setSalAgentiVisibility(false);

        }


        BeanSalarizareCVA salarizareCVA;

        if (UtilsUser.isUserIP())
            salarizareCVA = operatiiSalarizare.deserializeSalarizareCVIP(result);
        else
            salarizareCVA = operatiiSalarizare.deserializeSalarizareCVA(result);

        if (salarizareCVA.getListSalarizareCVABaza() == null) {
            Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
            setDateGeneraleVisibility(false);
            setLayoutVisibility(false);
            setDetaliiNocivVisibility(false);
            return;
        }

        VenitBazaCvaAdapter venitBazaCvaAdapter = new VenitBazaCvaAdapter(salarizareCVA.getListSalarizareCVABaza(), this);
        ListView listDetBaza = findViewById(R.id.listDetaliiBaza);

        ((TextView) findViewById(R.id.antetVenitBaza)).setText(nf.format(salarizareCVA.getVenitBaza()));
        ((TextView) findViewById(R.id.antetNruf)).setText(nf.format(salarizareCVA.getNruf()));
        ((TextView) findViewById(R.id.antetCoef)).setText(nf.format(salarizareCVA.getCoef()));
        ((TextView) findViewById(R.id.venitNruf)).setText(nf.format(salarizareCVA.getVenitNruf()));
        ((TextView) findViewById(R.id.venitTcf)).setText(nf.format(salarizareCVA.getVenitTcf()));
        ((TextView) findViewById(R.id.corectIncas)).setText(nf.format(salarizareCVA.getCorectIncas()));
        ((TextView) findViewById(R.id.venitFinal)).setText(nf.format(salarizareCVA.getVenitFinal()));
        ((TextView) findViewById(R.id.venitStocNociv)).setText(nf.format(salarizareCVA.getVenitStocNociv()));


        ((TextView) findViewById(R.id.textDataSalarizare)).setText(listLuni[Integer.parseInt(lunaSelect) - 1] + "  " + anSelect);

        listDetBaza.setAdapter(venitBazaCvaAdapter);
        listDetBaza.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        ListView listViewDetaliiStocNociv = findViewById(R.id.listStocNociv);

        SalarizareStocNocivAdapter stocNocivAdapter = new SalarizareStocNocivAdapter(salarizareCVA.getDetaliiVS(), getApplicationContext());
        listViewDetaliiStocNociv.setAdapter(stocNocivAdapter);
        listViewDetaliiStocNociv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        setLayoutVisibility(true);
        setDetaliiNocivVisibility(true);

    }

    private void setDetaliiNocivVisibility(boolean isVisible) {
        if (isVisible) {
            findViewById(R.id.containerStocNociv).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.containerStocNociv).setVisibility(View.GONE);
        }
    }

    private void setLayoutVisibility(boolean isVisible) {

        if (!isVisible) {

            ((LinearLayout) findViewById(R.id.headerDetaliiBaza)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.mainLayout)).setVisibility(View.INVISIBLE);

            ((LinearLayout) findViewById(R.id.layoutDetalii)).setVisibility(View.INVISIBLE);

        } else {

            ((LinearLayout) findViewById(R.id.headerDetaliiBaza)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.mainLayout)).setVisibility(View.VISIBLE);

            ((LinearLayout) findViewById(R.id.layoutDetalii)).setVisibility(View.VISIBLE);

        }


    }

    private void getSalarizareCVA() {
        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareCVA(params);
    }

    private void getDetaliiSalarizareCVA(String codAgent) {
        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", codAgent);
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareCVA(params);
    }

    private void getDetaliiSalarizareCVIP(String codAgent) {
        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", codAgent);
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareCVIP(params);
    }


    private void getSalarizareCVIP() {
        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareCVIP(params);
    }

    private void getSalarizareDepartCVA() {

        HashMap<String, String> params = new HashMap<>();

        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareDepartCVA(params);
    }

    private void getSalarizareDepartCVIP() {

        HashMap<String, String> params = new HashMap<>();

        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);
        operatiiSalarizare.getSalarizareDepartCVIP(params);
    }

    @Override
    public void onBackPressed() {
        returnToMainMenu();
        return;
    }

    private void returnToMainMenu() {
        UserInfo.getInstance().setParentScreen("");
        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(nextScreen);
        finish();
    }


    @Override
    public void operatiiSalarizareComplete(EnumOperatiiSalarizare numeComanda, Object result) {

        switch (numeComanda) {
            case GET_SALARIZARE_CVA:
            case GET_SALARIZARE_CVIP:
                afisSalarizareCVA((String) result);
                break;
            case GET_SALARIZARE_DEPART_CVA:
            case GET_SALARIZARE_DEPART_CVIP:
                afisSalarizareDepartCVA((String) result);
                break;
            default:
                break;
        }

    }

    @Override
    public void detaliiAgentSelected(String codAgent, String numeAgent) {
        isDetaliiAgent = true;
        numeAgentSelectat = numeAgent;

        if (UserInfo.getInstance().getTipUserSap().equals("SDCVA"))
            getDetaliiSalarizareCVA(codAgent);
        else if (UserInfo.getInstance().getTipUserSap().equals("SDIP"))
            getDetaliiSalarizareCVIP(codAgent);

    }

    @Override
    public void tipSituatieSalSelected(EnumSituatieSalarizare tipSituatie) {

        switch (tipSituatie) {
            case AGENTI:

                if (!UserInfo.getInstance().getInitUnitLog().equals(UserInfo.getInstance().getUnitLog())) {
                    Toast.makeText(getApplicationContext(), "Acest raport este accesibil doar pentru filiala " + UserInfo.getInstance().getInitUnitLog() + ".",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (UserInfo.getInstance().getTipUserSap().equals("SDCVA"))
                    getSalarizareDepartCVA();
                else if (UserInfo.getInstance().getTipUserSap().equals("SDIP"))
                    getSalarizareDepartCVIP();
                break;
            case SEF_DEP:
                if (UserInfo.getInstance().getTipUserSap().equals("SDIP")) {
                    isDetaliiAgent = false;
                    getSalarizareCVIP();
                }
                else if (UserInfo.getInstance().getTipUserSap().equals("SDCVA")) {
                    isDetaliiAgent = false;
                    getSalarizareCVA();
                }
                break;
            default:
                break;
        }


    }


    @Override
    public void intervalSalarizareSelected(String luna, String an) {

        lunaSelect = luna;
        anSelect = an;

        HashMap<String, String> params = new HashMap<>();
        params.put("codAgent", UserInfo.getInstance().getCod());
        params.put("unitLog", UserInfo.getInstance().getUnitLog());
        params.put("an", anSelect);
        params.put("luna", lunaSelect);

        if (UserInfo.getInstance().getTipUserSap().equals("CVIP"))
            getSalarizareCVIP();
        else if (UserInfo.getInstance().getTipUserSap().equals("CVA"))
            getSalarizareCVA();

    }
}
