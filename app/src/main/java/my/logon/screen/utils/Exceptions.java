package my.logon.screen.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import my.logon.screen.R;
import android.content.Context;

public class Exceptions {

	public static List<String> getExceptionsMacara(Context context) {
		List<String> listArts = new ArrayList<String>();

		InputStream is = context.getResources().openRawResource(R.raw.artmacara);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String readLine = null;

		try {
			while ((readLine = br.readLine()) != null) {
				listArts.add(readLine.trim());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listArts;
	}

}
