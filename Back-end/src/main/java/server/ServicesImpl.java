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
            System.out.println("Client: " + client.getId() + " " + client.getNume() + " " + client.getPrenume() + " " + client.getEmail() + " " + client.getParola() + " " + client.getCNP() + " " + client.getStatut());
            Random random = new Random();
            int randomID = random.nextInt();
            Bilet bilet = new Bilet(randomID, dataIncepere, dataExpirare, pret, tip, client);
            repoBiletDB.save(bilet);
            //Generate QR
            byte[] image = generateQRCode(randomID);
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
        return repoImagineDB.findOne(Math.toIntExact(id));
    }

    public void buyPass(LocalDateTime dataIncepere, LocalDateTime dataExpirare, Double pret, String tip, Integer idClient) throws SrvException {
        try {
            Client client=repoClientDB.findOne(idClient);
            Random random = new Random();
            int randomID = random.nextInt();
            Abonament abonament=new Abonament(randomID,dataIncepere,dataExpirare,pret,tip,client);
            repoAbonamentDB.save(abonament);
            byte[] image = generateQRCode(randomID);
            repoImagineDB.save(String.valueOf(randomID), "abonament", image);
        } catch (SrvException e) {
            throw new SrvException("Buying pass failed.");
        }
    }
    public Boolean isClient(Integer userId){
        if(repoClientDB.findOne(userId)!=null)
            return true;
        return false;
    }
    public byte[] getOrar(String linie){
        return repoImagineDB.findOrar(linie);
    }
    private final int defaultThreadsNo = 5;
    /*
    private void notifyFriendsLoggedIn(User user) throws ChatException {
        Iterable<User> friends=userRepository.getFriendsOf(user);
        System.out.println("Logged "+friends);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(User us :friends){
            IChatObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId()+ "] friend ["+user.getId()+"] logged in.");
                        chatClient.friendLoggedIn(user);
                    } catch (ChatException e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }

    private void notifyFriendsLoggedOut(User user) throws ChatException {
        Iterable<User> friends=userRepository.getFriendsOf(user);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);


        for(User us :friends){
            IChatObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying ["+us.getId()+"] friend ["+user.getId()+"] logged out.");
                        chatClient.friendLoggedOut(user);
                    } catch (ChatException e) {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }

    public synchronized void sendMessage(Message message) throws ChatException {
        String id_receiver=message.getReceiver().getId();
        IChatObserver receiverClient=loggedClients.get(id_receiver);
        if (receiverClient!=null) {      //the receiver is logged in
            messageRepository.save(message);
            receiverClient.messageReceived(message);
        }
        else
            throw new ChatException("User "+id_receiver+" not logged in.");
    }

    public synchronized void logout(User user, IChatObserver client) throws ChatException {
        IChatObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ChatException("User "+user.getId()+" is not logged in.");
        notifyFriendsLoggedOut(user);
    }

    public synchronized User[] getLoggedFriends(User user) throws ChatException {
       Iterable<User> friends=userRepository.getFriendsOf(user);
        Set<User> result=new TreeSet<User>();
        System.out.println("Logged friends for: "+user.getId());
        for (User friend : friends){
            if (loggedClients.containsKey(friend.getId())){    //the friend is logged in
                result.add(new User(friend.getId()));
                System.out.println("+"+friend.getId());
            }
        }
        System.out.println("Size "+result.size());
        return result.toArray(new User[result.size()]);
    }*/
}
