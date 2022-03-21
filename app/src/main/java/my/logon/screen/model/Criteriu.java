package my.logon.screen.model;

import java.util.List;

import my.logon.screen.beans.BeanComandaCreata;

public interface Criteriu {
	public List<BeanComandaCreata> indeplinesteCriteriu(List<BeanComandaCreata> listaComenzi, String divizie);
}
