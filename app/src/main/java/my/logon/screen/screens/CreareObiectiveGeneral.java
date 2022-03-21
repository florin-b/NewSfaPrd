package my.logon.screen.screens;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.adapters.AdapterObiectiveGeneral;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import my.logon.screen.beans.BeanLinieObiectiv;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.enums.EnumNumeObiective;
import my.logon.screen.R;

public class CreareObiectiveGeneral extends Fragment {

	private TextView textCurrentPage;
	private ListView listViewObiectiveGeneral;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.obiective_general, container, false);

		textCurrentPage = (TextView) v.findViewById(R.id.textCurrentPage);
		textCurrentPage.setText("DATE GENERALE");
		listViewObiectiveGeneral = (ListView) v.findViewById(R.id.listObiectiveGeneral);

		List<BeanLinieObiectiv> liniiObiective = new ArrayList<BeanLinieObiectiv>();

		BeanLinieObiectiv obiectiv = null;

		for (EnumNumeObiective nume : EnumNumeObiective.values()) {
			obiectiv = new BeanLinieObiectiv();
			obiectiv.setIdObiectiv(nume.getCod());
			obiectiv.setNumeObiectiv(nume.getNume());
			liniiObiective.add(obiectiv);

		}

		AdapterObiectiveGeneral adapterObiective = new AdapterObiectiveGeneral(BeanObiectiveGenerale.getInstance(),
				getActivity(), liniiObiective);

		BeanObiectiveGenerale.getInstance().setAdapterObiective(adapterObiective);

		listViewObiectiveGeneral.setAdapter(adapterObiective);

		return v;
	}

	public static CreareObiectiveGeneral newInstance() {
		CreareObiectiveGeneral obGen = new CreareObiectiveGeneral();
		Bundle bdl = new Bundle();
		obGen.setArguments(bdl);

		return obGen;
	}

}
