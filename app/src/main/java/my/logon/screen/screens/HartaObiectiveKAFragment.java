package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterObiectiveHarta;
import my.logon.screen.beans.BeanObiectivHarta;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.GPSTracker;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.model.ViewPagerCustomDuration;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsFormatting;

public class HartaObiectiveKAFragment extends Fragment implements ObiectiveListener {

	ViewPagerCustomDuration pager;
	ViewPager viewPager;

	private ListView listViewObiective;

	private OperatiiObiective operatiiObiective;
	private Button btnAfiseaza;
	private GPSTracker gps;
	private GoogleMap map;
	private List<BeanObiectivHarta> listObiective;
	private TextView textPozitie;

	private static View view;

	private Geocoder geocoder;
	private static int fenceRadiusKm = 4;
	private double latitude;
	private double longitude;
	private List<Address> currentPositionAddr = null;

	private String codJudetLocation;

	private enum EnumAsyuncOp {
		GET_POSITION, GET_DISTANCES;
	}

	private EnumAsyuncOp tipAsyncOp;
	private String strOpPosition = "Determinare pozitie curenta...";
	private String strOpDistances = "Calcurare distante...";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try {
			view = inflater.inflate(R.layout.harta_obiective_ka, container, false);
		} catch (InflateException e) {

		}

		geocoder = new Geocoder(getActivity(), Locale.getDefault());

		super.onCreate(savedInstanceState);

		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Obiective");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiObiective = new OperatiiObiective(getActivity());
		operatiiObiective.setObiectiveListener(this);

		setupLayout(view);

		btnAfiseaza = (Button) view.findViewById(R.id.btnAfis);
		setBtnAfisListener();

		getCurrentPositionRegionId();

		return view;

	}

	private void setupLayout(View v) {
		listViewObiective = (ListView) v.findViewById(R.id.listObiective);
		listViewObiective.setVisibility(View.INVISIBLE);

		textPozitie = (TextView) v.findViewById(R.id.textPozitie);
		textPozitie.setVisibility(View.INVISIBLE);

	}

	private void setBtnAfisListener() {
		btnAfiseaza.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				getCurrentPositionRegionId();

			}
		});
	}

	private void getAsyncPosition() {
		tipAsyncOp = EnumAsyuncOp.GET_POSITION;
		new StartAsyncOp(getActivity()).execute();

	}

	private void getCurrentPositionAddress() {
		Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			currentPositionAddr = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void getCurrentPositionRegionId() {

		gps = new GPSTracker(getActivity());

		if (gps.isCanGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getlongitude();

			getAsyncPosition();

		} else
			gps.showSettingsAlert();
		return;

	}

	private void showCurrentPositionAddress() {
		if (currentPositionAddr.size() > 0) {
			textPozitie.setVisibility(View.VISIBLE);
			textPozitie.setText(currentPositionAddr.get(0).getAddressLine(1) + ", " + currentPositionAddr.get(0).getAddressLine(0));
			if (UtilsFormatting.flattenToAscii(currentPositionAddr.get(0).getAdminArea()).toUpperCase().equals("BUCHAREST"))
				codJudetLocation = "40";
			else
				codJudetLocation = EnumJudete.getCodJudet(UtilsFormatting.flattenToAscii(currentPositionAddr.get(0).getAdminArea()).toUpperCase());

			getListObiective();

		} else
			Toast.makeText(getActivity(), "Modulul GPS nu este initializat, repetati operatiunea dupa 30 de secunde.", Toast.LENGTH_LONG).show();
	}

	private void initGoogleMap() {

		SupportMapFragment mapFrag = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setZoomControlsEnabled(true);
		uiSettings.setCompassEnabled(true);
		uiSettings.setMyLocationButtonEnabled(false);

	}

	private void getListObiective() {

		HashMap<String, String> params = new HashMap<String, String>();

		String localCodAgent = "";
		String localCodJudet = "";
		if (UserInfo.getInstance().getTipUser().equals("KA"))
			localCodAgent = UserInfo.getInstance().getCod();

		if (!UserInfo.getInstance().getTipUser().equals("KA"))
			localCodJudet = codJudetLocation;

		params.put("codAgent", localCodAgent);
		params.put("codJudet", localCodJudet);
		operatiiObiective.getObiectiveHarta(params);

	}

	private void displayListObiective(List<BeanObiectivHarta> listObiective) {
		this.listObiective = listObiective;

		tipAsyncOp = EnumAsyuncOp.GET_DISTANCES;
		new StartAsyncOp(getActivity()).execute();

	}

	private String getAsyncInfoMessage() {
		String strMessage = "";

		switch (tipAsyncOp) {
		case GET_DISTANCES:
			strMessage = strOpDistances;
			break;
		case GET_POSITION:
			strMessage = strOpPosition;
			break;
		default:
			break;

		}
		return strMessage;
	}

	public class StartAsyncOp extends AsyncTask<Void, Void, Void> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		public StartAsyncOp(Context context) {
			super();
			this.mContext = context;

		}

		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage(getAsyncInfoMessage());
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... url) {

			switch (tipAsyncOp) {
			case GET_DISTANCES:
				getCoordFromAddress();
				break;
			case GET_POSITION:
				getCurrentPositionAddress();
				break;
			default:
				break;
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void dummy) {

			try {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				switch (tipAsyncOp) {
				case GET_DISTANCES:
					displayObiectiveMarker();
					break;
				case GET_POSITION:
					showCurrentPositionAddress();
					break;
				default:
					break;
				}

			} catch (Exception e) {
				Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void getCoordFromAddress() {
		try {

			for (BeanObiectivHarta ob : listObiective) {
				List<Address> adr = geocoder.getFromLocationName(UtilsFormatting.formatAddress(ob.getAddress()), 1);

				if (adr.size() > 0) {
					ob.setLatitude(adr.get(0).getLatitude());
					ob.setLongitude(adr.get(0).getLongitude());
				}

			}

		} catch (Exception e) {

		}
	}

	private void displayObiectiveMarker() {

		List<BeanObiectivHarta> selectedObjectives = new ArrayList<BeanObiectivHarta>();

		initGoogleMap();

		for (BeanObiectivHarta obiect : listObiective) {

			double distantaKm = MapUtils.distanceXtoY(latitude, longitude, obiect.getLatitude(), obiect.getLongitude(), "K");

			if (distantaKm <= fenceRadiusKm) {
				selectedObjectives.add(obiect);
				map.addMarker(new MarkerOptions().position(new LatLng(obiect.getLatitude(), obiect.getLongitude())).icon(
						BitmapDescriptorFactory.fromBitmap(UtilsFormatting.writeTextOnDrawable(getActivity(), R.drawable.pointer,
								String.valueOf(selectedObjectives.size())))));
			}

		}

		map.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).radius(fenceRadiusKm * 1000).strokeColor(Color.BLACK).strokeWidth(1));
		LatLng currentPosition = new LatLng(latitude, longitude);

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentPosition, 13);
		map.animateCamera(update);

		boolean showNumeAgent = true;
		if (UserInfo.getInstance().getTipUser().equals("KA"))
			showNumeAgent = false;

		AdapterObiectiveHarta adapterOb = new AdapterObiectiveHarta(getActivity(), selectedObjectives, showNumeAgent);
		listViewObiective.setAdapter(adapterOb);
		listViewObiective.setVisibility(View.VISIBLE);

	}

	class ObiectivePagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public ObiectivePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {

		case GET_OBIECTIVE_HARTA:
			displayListObiective(operatiiObiective.deserializeListaObiectiveHarta((String) result));
			break;

		default:
			break;
		}

	}

}
