package my.logon.screen.enums;

public enum EnumOperatiiAdresa {

    GET_LOCALITATI_JUDET("getLocalitatiJudet"), GET_ADRESE_JUDET("getAdreseJudet"), IS_ADRESA_VALIDA("isAdresaValid"),
    GET_DATE_LIVRARE("getCmdDateLivrare"), GET_ADRESE_LIVR_CLIENT(
            "getAdreseLivrareClient"), GET_LOCALITATI_LIVRARE_RAPIDA("getLocalitatiLivrareRapida"),
    GET_DATE_LIVRARE_CLIENT("getDateLivrareClient"), GET_FILIALA_MATHAUS("getFilialaJudetLivrare"),
    GET_ADRESA_FILIALA("getAdresaFiliala"), GET_DATE_POLIGON_LIVRARE("getDatePoligonLivrare");
    private String numeComanda;

    EnumOperatiiAdresa(String numeComanda) {
        this.numeComanda = numeComanda;
    }

    public String getNumeComanda() {
        return numeComanda;
    }

}
