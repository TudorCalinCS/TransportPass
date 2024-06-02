package server;


import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.*;
import utils.QRCodeGenerator;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

import static utils.QRCodeGenerator.generateQRCode;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServicesImpl implements IServices {

    private RepoClientDB repoClientDB;
    private RepoControlorDB repoControlorDB;
    private RepoUserDB repoUserDB;
    private RepoAbonamentDB repoAbonamentDB;
    private RepoBiletDB repoBiletDB;
    private Map<String, IObserver> loggedClients;
    private RepoImagineDB repoImagineDB;

    static final Logger logger = LogManager.getLogger(AbstractServer.class);
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
            logger.info("User logged in: " + user.getId());
            return user;

            //notifyFriendsLoggedIn(user);
        } else {
            logger.info("Authentication failed.");
            throw new SrvException("Authentication failed.");
        }
    }

    public synchronized void buyTicket(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer id) throws SrvException {
        try {
            Client client = repoClientDB.findOne(id);
            //logger.info("Client: " + client.getId() + " " + client.getNume() + " " + client.getPrenume() + " " + client.getEmail() + " " + client.getParola() + " " + client.getCNP() + " " + client.getStatut());
            Random random = new Random();
            int randomID = random.nextInt();
            Bilet bilet = new Bilet(randomID, dataIncepere, dataExpirare, pret, tip, client);
            repoBiletDB.save(bilet);
            //Generate QR
            byte[] image = generateQRCode(randomID);
            logger.info("byte[] " + Arrays.toString(image));
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

    public Abonament findAbonament(int id){
        return repoAbonamentDB.findOne(id);
    }
    public Bilet findBilet(int id){
        return repoBiletDB.findOne(id);
    }

    public boolean checkStudent(byte[] imageData) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:5000/process_image");

        ByteArrayBody byteArrayBody = new ByteArrayBody(imageData, ContentType.DEFAULT_BINARY, "image.jpg");

        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("file", byteArrayBody)
                .build();
        httpPost.setEntity(reqEntity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                System.out.println("Python Server response: " + result);
                if (Objects.equals(result, "Student"))
                    return true;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updatePassword(Integer id, String newPassword) {
        User user = repoUserDB.findOne(id);
        if (user != null) {
            user.setParola(newPassword);
            repoUserDB.update(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
    public void updateAbonament(Abonament abonament) {
        repoAbonamentDB.update(abonament);

    }

}
