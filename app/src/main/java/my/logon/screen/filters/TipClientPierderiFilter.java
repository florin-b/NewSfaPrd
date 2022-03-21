package my.logon.screen.filters;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.PierdereTipClient;

public class TipClientPierderiFilter {

	public ArrayList<PierdereTipClient> getPierderiTipClient(List<PierdereTipClient> listPierderi, String codTipClient) {
		ArrayList<PierdereTipClient> newListPierderi = new ArrayList<PierdereTipClient>();

		for (PierdereTipClient p : listPierderi) {
			if (p.getCodTipClient().equals(codTipClient))
				newListPierderi.add(p);

		}

		return newListPierderi;

	}

}
