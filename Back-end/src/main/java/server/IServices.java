package server;


import domain.Abonament;
//<<<<<<< HEAD
//=======
import domain.Bilet;
//>>>>>>> f9ce205bf8bebe26e0abf5707a61776ac4b6a541
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

    Boolean isStudent(Integer userId);

    byte[] getOrar(String linie);

    Boolean alreadyExists(String email, String cnp);

    Abonament findAbonament(int id);

    Bilet findBilet(int id);

    boolean checkStudent(byte[] imageData);

    public void updatePassword(String username, String newPassword);
    public void updateAbonament(Abonament abonament);


}
