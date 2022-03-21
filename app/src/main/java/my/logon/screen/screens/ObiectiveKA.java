package my.logon.screen.screens;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.dialogs.SelectObiectivDialog;
import my.logon.screen.enums.EnumMeniuObiectiv;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.listeners.SelectObiectivListener;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;

public class ObiectiveKA extends FragmentActivity implements SelectObiectivListener, ObiectiveListener {

	private OperatiiObiective operatiiObiective;
	private static EnumMeniuObiectiv meniuObiectiv;
	private SelectObiectivDialog dialog;

	private static final int CREARE = 1;
	private static final int MODIFICARE = 2;
	private static final int URMARIRE = 3;
	private static final int AFISARE = 4;
	private static final int HARTA = 5;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.obiective_new);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Obiective");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiObiective = new OperatiiObiective(this);
		operatiiObiective.setObiectiveListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu subMenu = menu.addSubMenu(0, Menu.NONE, 0, "Optiuni");

		subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		if (UserInfo.getInstance().getTipUser().equals("KA")) {
			subMenu.add(0, CREARE, 0, "Creare");
			subMenu.add(0, MODIFICARE, 1, "Modificare");
			subMenu.add(0, URMARIRE, 2, "Urmarire");
		}
		subMenu.add(0, AFISARE, 3, "Afisare");
		subMenu.add(0, HARTA, 4, "Harta");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		dialog = new SelectObiectivDialog(this);
		dialog.setObiectivSelectedListener(this);

		FragmentTransaction tx = null;
		BeanObiectiveGenerale.getInstance().clearInstanceData();
		switch (item.getItemId()) {
		case CREARE: {
			meniuObiectiv = EnumMeniuObiectiv.CREARE;
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "my.logon.screen.Obiective2"));
			tx.commit();
			break;
		}
		case MODIFICARE: {
			meniuObiectiv = EnumMeniuObiectiv.MODIFICARE;
			dialog.setTipOperatie(meniuObiectiv);
			dialog.show();
			break;
		}
		case URMARIRE: {
			meniuObiectiv = EnumMeniuObiectiv.URMARIRE;
			dialog.setTipOperatie(meniuObiectiv);
			dialog.show();
			break;
		}
		case AFISARE: {
			meniuObiectiv = EnumMeniuObiectiv.AFISARE;
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "my.logon.screen.AfiseazaObiectiveKAFragment"));
			tx.commit();
			break;
		}
		case HARTA: {
			meniuObiectiv = EnumMeniuObiectiv.HARTA;
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "my.logon.screen.HartaObiectiveKAFragment"));
			tx.commit();
			break;

		}

		case android.R.id.home: {
			returnToHome();

		}
		default:
			break;
		}
		return false;
	}

	private void returnToHome() {
		BeanObiectiveGenerale.getInstance().clearInstanceData();
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	public void onBackPressed() {
		returnToHome();
		return;
	}

	private void getDetaliiModificare(String idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv);
		operatiiObiective.getDetaliiObiectiv(params);

	}

	private void getDetaliiUrmarire(String idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv);
		operatiiObiective.getClientiObiectiv(params);
	}

	public static EnumMeniuObiectiv getSelectedMeniuObiectiv() {
		return meniuObiectiv;
	}

	public void obiectivSelected(BeanObiectivAfisare obiectiv) {
		if (meniuObiectiv == EnumMeniuObiectiv.MODIFICARE)
			getDetaliiModificare(obiectiv.getId());

		if (meniuObiectiv == EnumMeniuObiectiv.URMARIRE) {
			BeanObiectiveGenerale.getInstance().setNumeObiectiv(obiectiv.getNume());
			BeanObiectiveGenerale.getInstance().setId(obiectiv.getId());
			getDetaliiUrmarire(obiectiv.getId());

		}

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		FragmentTransaction tx;
		switch (numeComanda) {
		case GET_DETALII_OBIECTIV:
			operatiiObiective.deserializeDetaliiObiectiv((String) result);
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "my.logon.screen.Obiective2"));
			tx.commit();
			break;
		case GET_CLIENTI_OBIECTIV:
			BeanObiectiveGenerale.getInstance().setListConstructori(operatiiObiective.deserializeListConstructori((String) result));
			tx = getSupportFragmentManager().beginTransaction();
			tx.replace(R.id.main, Fragment.instantiate(ObiectiveKA.this, "my.logon.screen.UrmarireObiective"));
			tx.commit();
			break;
		default:
			break;
		}

	}

}
