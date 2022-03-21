package my.logon.screen.model;

import java.util.List;

import my.logon.screen.beans.BeanDocumentRetur;

public interface CriteriuDocumentRetur {
	public List<BeanDocumentRetur> indeplinesteCriteriul(List<BeanDocumentRetur> listDocumente, String nrDocument);
}
