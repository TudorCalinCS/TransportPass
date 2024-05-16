package domain;

import java.util.Objects;


public class User extends Entity<Integer> {
    private String nume;
    private String prenume;
    private String email;
    private String parola;
    private String CNP;

    public User(Integer id, String nume, String prenume, String email, String parola, String CNP) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.parola = parola;
        this.CNP = CNP;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(nume, user.nume) && Objects.equals(prenume, user.prenume) && Objects.equals(email, user.email) && Objects.equals(parola, user.parola) && Objects.equals(CNP, user.CNP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume, prenume, email, parola, CNP);
    }

    @Override
    public String toString() {
        return "User{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", email='" + email + '\'' +
                ", parola='" + parola + '\'' +
                ", CNP='" + CNP + '\'' +
                '}';
    }
}
