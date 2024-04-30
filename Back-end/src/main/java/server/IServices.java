package server;


public interface IServices {
    void login(String email,String parola, IObserver client) throws ChatException;

   void createClient(String nume, String prenume, String email, String parola, String CNP, String statut);
}
