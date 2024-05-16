package domain;

import java.util.Objects;

public class Client extends User{
    private String statut;

    public Client(Integer id,String nume, String prenume, String email, String parola, String CNP, String statut) {
        super(id,nume, prenume, email, parola, CNP);
        this.statut = statut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(statut, client.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statut);
    }

    @Override
    public String toString() {
        return "Client{" +
                "statut='" + statut + '\'' +
                '}';
    }
}
