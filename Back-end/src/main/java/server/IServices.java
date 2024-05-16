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

    void buyTicket(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, long id) throws SrvException;

    Abonament findAbonamentByClientId(Long id);

    List<Bilet> getTicketsByClientId(Long idClient);

    byte[] getQr(Long id);

    void buyPass(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Long idClient) throws SrvException;

}
