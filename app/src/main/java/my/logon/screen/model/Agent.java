package my.logon.screen.model;

public class Agent {

    private String nume;
    private String cod;
    private String depart;

    public Agent() {

    }

    public Agent(String nume, String cod) {
        super();
        this.nume = nume;
        this.cod = cod;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String toString() {
        return nume;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }
}