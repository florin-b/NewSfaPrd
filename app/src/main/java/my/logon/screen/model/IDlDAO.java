package my.logon.screen.model;

import java.util.HashMap;

public interface IDlDAO {

	 void getListComenzi(HashMap<String, String> params);

	 void getArticoleComanda(HashMap<String, String> params);

	 void operatiiComanda(HashMap<String, String> params);

	 void salveazaComanda(HashMap<String, String> params);
	
	 void getArticoleComandaJSON(HashMap<String, String> params);

}
