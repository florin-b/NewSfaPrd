package my.logon.screen.enums;
  
public enum EnumArticoleConcurenta {
   
	TIPART_0("Tip de actualizare", ""), TIPART_1("Hiper-sensibil", "01"), TIPART_2("Sensibil", "02"), TIPART_3("Reprezentativ", "03");

	private String listName;
	private String listCode;

	private EnumArticoleConcurenta(String listName, String listCode) {
		this.listName = listName;
		this.listCode = listCode;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListCode() {
		return listCode;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

}
