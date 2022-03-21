package my.logon.screen.model;

import java.util.HashMap;

public interface IClpDAO {

	void getListComenzi(HashMap<String, String> params);

	void getArticoleComanda(HashMap<String, String> params);

	void operatiiComanda(HashMap<String, String> params);

	void salveazaComanda(HashMap<String, String> params);

}
