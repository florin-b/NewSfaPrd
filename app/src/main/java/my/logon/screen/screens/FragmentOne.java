/**
 * @author florinb
 *
 */
package my.logon.screen.screens;




import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import my.logon.screen.R;

public class FragmentOne extends Fragment {

	TextView textView1;

	public FragmentOne() {

	}

	public static final FragmentOne newInstance() {

		FragmentOne f = new FragmentOne();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_one, null);

		return root;
	}

}