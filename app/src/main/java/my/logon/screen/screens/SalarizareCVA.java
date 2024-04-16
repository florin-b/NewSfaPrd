package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.VenitBazaCvaAdapter;
import my.logon.screen.beans.BeanSalarizareCVA;
import my.logon.screen.dialogs.SelectLunaSalarizareDialog;
import my.logon.screen.enums.EnumOperatiiSalarizare;
import my.logon.screen.listeners.IntervalSalarizareListener;
import my.logon.screen.listeners.OperatiiSalarizareListener;
import my.logon.screen.model.OperatiiSalarizare;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsUser;

public class SalarizareCVA extends Activity implements OperatiiSalarizareListener, IntervalSalarizareListener {

    private ActionBar actionBar;
    private OperatiiSalarizare operatiiSalarizare;
    private String[] listLuni = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie",
            "Decembrie"};

    private String lunaSelect, anSelect;
    private NumberFormat nf;

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

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 0:
                SelectLunaSalarizareDialog intervalDialog = new SelectLunaSalarizareDialog(this);
                intervalDialog.setIntervalDialogListener(this);
                intervalDialog.showDialog();
                return true;

            case android.R.id.home:
                returnToMainMenu();
                return true;

        }
        return false;
    }


    private void afisSalarizareCVA(String result) {


        BeanSalarizareCVA salarizareCVA = operatiiSalarizare.deserializeSalarizareCVA(result);
        VenitBazaCvaAdapter venitBazaCvaAdapter = new VenitBazaCvaAdapter(salarizareCVA.getListSalarizareCVABaza(), this);
        ListView listDetBaza = findViewById(R.id.listDetaliiBaza);

        ((TextView) findViewById(R.id.antetVenitBaza)).setText(String.valueOf(salarizareCVA.getVenitBaza()));
        ((TextView) findViewById(R.id.antetNruf)).setText(String.valueOf(salarizareCVA.getNruf()));
        ((TextView) findViewById(R.id.antetCoef)).setText(String.valueOf(salarizareCVA.getCoef()));
        ((TextView) findViewById(R.id.venitNruf)).setText(String.valueOf(salarizareCVA.getVenitNruf()));
        ((TextView) findViewById(R.id.venitTcf)).setText(String.valueOf(salarizareCVA.getVenitTcf()));
        ((TextView) findViewById(R.id.corectIncas)).setText(String.valueOf(salarizareCVA.getCorectIncas()));
        ((TextView) findViewById(R.id.venitFinal)).setText(String.valueOf(salarizareCVA.getVenitFinal()));

        ((TextView) findViewById(R.id.textDataSalarizare)).setText(listLuni[Integer.parseInt(lunaSelect) - 1] + "  " + anSelect);

        listDetBaza.setAdapter(venitBazaCvaAdapter);
        setLayoutVisibility(true);

    }

    private void setLayoutVisibility(boolean isVisible) {

        if (!isVisible) {
            ((LinearLayout) findViewById(R.id.layoutVenitBaza)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutNruf)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutCoef)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitNruf)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitTcf)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutCorectIncas)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitFinal)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.headerDetaliiBaza)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.mainLayout)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.textDataSalarizare)).setVisibility(View.INVISIBLE);


        } else {
            ((LinearLayout) findViewById(R.id.layoutVenitBaza)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutNruf)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutCoef)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitNruf)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitTcf)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutCorectIncas)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.layoutVenitFinal)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.headerDetaliiBaza)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.mainLayout)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textDataSalarizare)).setVisibility(View.VISIBLE);
        }


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
                afisSalarizareCVA((String) result);
                break;
            default:
                break;
        }

    }

    @Override
    public void detaliiAgentSelected(String codAgent, String numeAgent) {

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

        operatiiSalarizare.getSalarizareCVA(params);

    }
}
