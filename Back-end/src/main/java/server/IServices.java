package server;


import domain.Abonament;
import domain.Bilet;
import domain.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface IServices {
    User login(String email, String parola, IObserver client) throws SrvException;

    void createClient(String nume, String prenume, String email, String parola, String CNP, String statut) throws SrvException;

    void buyTicket(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer id) throws SrvException;

    Abonament findAbonamentByClientId(Integer id);

    List<Bilet> getTicketsByClientId(Integer idClient);

    byte[] getQr(Integer id);

    void buyPass(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer idClient) throws SrvException;

    Boolean isClient(Integer userId);
}
