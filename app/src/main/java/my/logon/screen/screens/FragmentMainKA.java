/**
 * @author florinb
 *
 */
package my.logon.screen.screens;




import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import my.logon.screen.R;

public class FragmentMainKA extends Fragment {

	TextView textView1;

	public static Fragment newInstance(Context context) {
		FragmentMainKA f = new FragmentMainKA();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_main_ka, null);

		return root;
	}

}