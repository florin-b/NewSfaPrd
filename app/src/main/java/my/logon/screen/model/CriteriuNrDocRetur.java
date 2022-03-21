package my.logon.screen.model;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.BeanDocumentRetur;
import my.logon.screen.model.CriteriuDocumentRetur;

public class CriteriuNrDocRetur implements CriteriuDocumentRetur {

	public List<BeanDocumentRetur> indeplinesteCriteriul(List<BeanDocumentRetur> listDocumente, String nrDocument) {

		List<BeanDocumentRetur> listDocRetur = new ArrayList<BeanDocumentRetur>();

		for (BeanDocumentRetur docRet : listDocumente) {
			if (docRet.getNumar().startsWith(nrDocument)) {
				listDocRetur.add(docRet);
			}
		}

		return listDocRetur;
	}

}
