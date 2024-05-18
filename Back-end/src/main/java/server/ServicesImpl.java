package server;


import domain.Abonament;
import domain.Bilet;
import domain.Client;
import domain.User;
import repository.*;
import utils.QRCodeGenerator;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static utils.QRCodeGenerator.generateQRCode;


public class ServicesImpl implements IServices {

    private RepoClientDB repoClientDB;
    private RepoControlorDB repoControlorDB;
    private RepoUserDB repoUserDB;
    private RepoAbonamentDB repoAbonamentDB;
    private RepoBiletDB repoBiletDB;
    private Map<String, IObserver> loggedClients;
    private RepoImagineDB repoImagineDB;


    public ServicesImpl(RepoClientDB repoClientDB, RepoControlorDB repoControlorDB, RepoUserDB repoUserDB, RepoAbonamentDB repoAbonamentDB, RepoBiletDB repoBiletDB, RepoImagineDB repoImagineDB) {
        this.repoClientDB = repoClientDB;
        this.repoControlorDB = repoControlorDB;
        this.repoUserDB = repoUserDB;
        this.repoAbonamentDB = repoAbonamentDB;
        this.repoBiletDB = repoBiletDB;
        this.loggedClients = new ConcurrentHashMap<>();
        this.repoImagineDB = repoImagineDB;
    }

    public synchronized void createClient(String nume, String prenume, String email, String parola, String CNP, String statut) throws SrvException {
        try {
            Random random = new Random();
            int randomID = random.nextInt();
            Client client = new Client(randomID, nume, prenume, email, parola, CNP, statut);
            repoClientDB.save(client);
        } catch (SrvException e) {
            throw new SrvException(e.getMessage());
        }
    }

    public synchronized User login(String email, String parola, IObserver client) throws SrvException {
        User user = repoUserDB.findOneByUsernameAndPassword(email, parola);
        if (user != null) {
            loggedClients.put(user.getId().toString(), client);
            System.out.println("User logged in: " + user.getId());
            return user;

            //notifyFriendsLoggedIn(user);
        } else {
            System.out.println("Authentication failed.");
            throw new SrvException("Authentication failed.");
        }
    }

    public synchronized void buyTicket(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer id) throws SrvException {
        try {
            Client client = repoClientDB.findOne(id);
            //System.out.println("Client: " + client.getId() + " " + client.getNume() + " " + client.getPrenume() + " " + client.getEmail() + " " + client.getParola() + " " + client.getCNP() + " " + client.getStatut());
            Random random = new Random();
            int randomID = random.nextInt();
            Bilet bilet = new Bilet(randomID, dataIncepere, dataExpirare, pret, tip, client);
            repoBiletDB.save(bilet);
            //Generate QR
            byte[] image = generateQRCode(randomID);
            System.out.println("byte[] " + Arrays.toString(image));
            repoImagineDB.save(String.valueOf(randomID), "bilet", image);
        } catch (SrvException e) {
            throw new SrvException("Buying ticket failed.");
        }
    }

    public Abonament findAbonamentByClientId(Integer id) {
        return repoAbonamentDB.findAbonamentByUser(id);
    }

    public synchronized List<Bilet> getTicketsByClientId(Integer idClient) {
        return repoBiletDB.findAllByUser(idClient);
    }

    public byte[] getQr(Integer id) {
        return repoImagineDB.findOne(id);
    }

    public void buyPass(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer idClient) throws SrvException {
        try {
            Client client = repoClientDB.findOne(idClient);
            Random random = new Random();

            int randomID = random.nextInt();
            Abonament abonament = new Abonament(randomID, dataIncepere, dataExpirare, pret, tip, client);
            repoAbonamentDB.save(abonament);
            byte[] image = generateQRCode(randomID);
            repoImagineDB.save(String.valueOf(randomID), "abonament", image);
        } catch (SrvException e) {
            throw new SrvException("Buying pass failed.");
        }
    }

    public Boolean isClient(Integer userId) {
        if (repoClientDB.findOne(userId) != null)
            return true;
        return false;
    }
    public Boolean alreadyExists(String email,String cnp){
        if (repoClientDB.findOneByEmailAndCNP(email,cnp) != null)
            return true;
        return false;
    }

    public byte[] getOrar(String linie) {
        return repoImagineDB.findOrar(linie);
    }

}
