package server;


import domain.Abonament;
import domain.User;

import java.time.LocalDateTime;

public interface IServices {
    User login(String email, String parola, IObserver client) throws SrvException;

    void createClient(String nume, String prenume, String email, String parola, String CNP, String statut) throws SrvException;

    void buyTicket(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip,long id) throws SrvException;

    void buyPass(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip,long id) throws SrvException;

    Abonament findAbonamentByClientID(long id) throws SrvException;
}
