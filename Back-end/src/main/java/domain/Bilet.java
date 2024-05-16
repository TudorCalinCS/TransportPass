package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Bilet extends Entity<Integer> {
    private LocalDateTime dataIncepere;
    private LocalDateTime dataExpirare;
    private Double pret;
    private String tip;
    private Client codClient;

    public Bilet(Integer id,LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Client codClient) {
        this.id = id;
        this.dataIncepere = dataIncepere;
        this.dataExpirare = dataExpirare;
        this.pret = pret;
        this.tip = tip;
        this.codClient = codClient;
    }
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getDataIncepere() {
        return dataIncepere;
    }

    public void setDataIncepere(LocalDateTime dataIncepere) {
        this.dataIncepere = dataIncepere;
    }

    public LocalDateTime getDataExpirare() {
        return dataExpirare;
    }

    public void setDataExpirare(LocalDateTime dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Client getCodClient() {
        return codClient;
    }

    public void setCodClient(Client codClient) {
        this.codClient = codClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bilet bilet = (Bilet) o;
        return Objects.equals(dataIncepere, bilet.dataIncepere) && Objects.equals(dataExpirare, bilet.dataExpirare) && Objects.equals(pret, bilet.pret) && Objects.equals(tip, bilet.tip) && Objects.equals(codClient, bilet.codClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataIncepere, dataExpirare, pret, tip, codClient);
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "dataIncepere=" + dataIncepere +
                ", dataExpirare=" + dataExpirare +
                ", pret=" + pret +
                ", tip='" + tip + '\'' +
                ", codClient=" + codClient +
                '}';
    }
}
