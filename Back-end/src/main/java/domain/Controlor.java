package domain;

import java.util.Objects;

public class Controlor extends User{
    private String numarLegitimatie;
    public Controlor(Integer id,String nume, String prenume, String email, String parola, String CNP, String numarLegitimatie) {
        super(id,nume, prenume, email, parola, CNP);
        this.numarLegitimatie = numarLegitimatie;
    }

    public String getNumarLegitimatie() {
        return numarLegitimatie;
    }

    public void setNumarLegitimatie(String numarLegitimatie) {
        this.numarLegitimatie = numarLegitimatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Controlor controlor = (Controlor) o;
        return Objects.equals(numarLegitimatie, controlor.numarLegitimatie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numarLegitimatie);
    }

    @Override
    public String toString() {
        return "Controlor{" +
                "numarLegitimatie='" + numarLegitimatie + '\'' +
                '}';
    }
}
