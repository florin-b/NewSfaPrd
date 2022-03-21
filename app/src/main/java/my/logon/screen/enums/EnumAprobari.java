package my.logon.screen.enums;

public enum EnumAprobari {
	GET_APROBARI_COMENZI_KA("getAprobariNecesare");
	
	private String numeAprobari;
	
	 EnumAprobari(String numeAprobari) {
		this.numeAprobari=numeAprobari;
	}
	
	 public String getAprobari(){
		 return numeAprobari;
	 }
	 
}
