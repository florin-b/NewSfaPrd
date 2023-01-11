package my.logon.screen.beans;

import java.util.Objects;

public class TranspComenzi {
    private String filiala;
    private String transport;

    public String getFiliala() {
        return filiala;
    }

    public void setFiliala(String filiala) {
        this.filiala = filiala;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranspComenzi that = (TranspComenzi) o;
        return filiala.equals(that.filiala) &&
                transport.equals(that.transport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filiala, transport);
    }

    @Override
    public String toString() {
        return "TranspComenzi{" +
                "filiala='" + filiala + '\'' +
                ", transport='" + transport + '\'' +
                '}';
    }
}
