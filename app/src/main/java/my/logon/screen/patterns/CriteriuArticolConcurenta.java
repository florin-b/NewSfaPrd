package my.logon.screen.patterns;

import java.util.List;

import my.logon.screen.beans.BeanArticolConcurenta;

public interface CriteriuArticolConcurenta {
	public List<BeanArticolConcurenta> gasesteArticole(List<BeanArticolConcurenta> listArticole, String pattern);
}
