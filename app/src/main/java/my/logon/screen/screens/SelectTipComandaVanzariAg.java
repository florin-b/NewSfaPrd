/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import my.logon.screen.model.VanzariAgenti;
import my.logon.screen.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class SelectTipComandaVanzariAg extends Fragment {

	private RadioButton radioEmiseBtn, radioAnulateBtn;

	public static final SelectTipComandaVanzariAg newInstance() {

		SelectTipComandaVanzariAg f = new SelectTipComandaVanzariAg();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_tip_cmd_vanz_ag, container, false);

		try {

			radioEmiseBtn = (RadioButton) v.findViewById(R.id.radioEmise);
			addListenerRadioEmise();

			radioAnulateBtn = (RadioButton) v.findViewById(R.id.radioAnulate);
			addListenerRadioAnulate();

			if (VanzariAgenti.getInstance().tipComanda.equals("E"))
				radioEmiseBtn.setChecked(true);
			else
				radioAnulateBtn.setChecked(true);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	public void addListenerRadioEmise() {
		radioEmiseBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					VanzariAgenti.getInstance().tipComanda = "E";

				}

			}
		});
	}

	public void addListenerRadioAnulate() {
		radioAnulateBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					VanzariAgenti.getInstance().tipComanda = "A";

				}

			}
		});
	}

}
