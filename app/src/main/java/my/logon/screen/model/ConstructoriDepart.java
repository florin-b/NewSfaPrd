package my.logon.screen.model;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.BeanObiectiveConstructori;

public class ConstructoriDepart implements ICriteriuDepart {

	public List<BeanObiectiveConstructori> constructoriDepart(List<BeanObiectiveConstructori> listConstructori,
			String codDepart) {

		List<BeanObiectiveConstructori> list = new ArrayList<BeanObiectiveConstructori>();

		for (BeanObiectiveConstructori constr : listConstructori) {

			if (constr == null || constr.getCodDepart() == null)
				continue;

			if (constr.getCodDepart().equals(codDepart) || constr.getCodDepart().equals("00"))
				list.add(constr);
		}

		return list;
	}

}
