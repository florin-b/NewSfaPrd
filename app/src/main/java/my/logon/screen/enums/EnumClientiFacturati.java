//Creat de Robert
package my.logon.screen.enums;

public enum EnumClientiFacturati {
	GETLISTA_CLIENTIFACTURATIKA("getClientiFacturatiKA"), GETLISTA_FACTURICLIENT("getDetaliiClFacturatiKA");
	
	private String numeWebService;
	
	EnumClientiFacturati(String numeWebService) {
		this.numeWebService = numeWebService;
	}
	
	public String getNume() {
		
		return numeWebService;
	}
}
