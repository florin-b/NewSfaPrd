package my.logon.screen.beans;

public class BeanConditiiHeader {
    private int id;
    private double conditiiCalit;
    private int nrFact;
    private String observatii;
    private String codAgent;

    public BeanConditiiHeader() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getConditiiCalit() {
        return conditiiCalit;
    }

    public void setConditiiCalit(double conditiiCalit) {
        this.conditiiCalit = conditiiCalit;
    }

    public int getNrFact() {
        return nrFact;
    }

    public void setNrFact(int nrFact) {
        this.nrFact = nrFact;
    }

    public String getObservatii() {
        if (observatii == null || observatii.equals("null"))
            return " ";
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public String getCodAgent() {
        return codAgent;
    }

    public void setCodAgent(String codAgent) {
        this.codAgent = codAgent;
    }

}
