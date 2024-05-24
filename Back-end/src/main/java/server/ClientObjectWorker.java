package server;

import domain.Abonament;
import domain.Bilet;
import domain.Client;
import domain.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.System.in;

public class ClientObjectWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;
    private volatile boolean connected;

    private User currentUser;

    public ClientObjectWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        connected = true; // Assume connected until proven otherwise
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {

            while (connected) {
                String jsonString = reader.readLine();
                if (jsonString == null) {
                    System.out.println("End of stream");
                    break;
                }
                System.out.println("Received request: " + jsonString);
                JSONObject jsonRequest = new JSONObject(jsonString);
                JSONObject response = handleRequest(jsonRequest);
                writer.write(response.toString() + "\n");
                writer.flush();

                // Sleep for some time before processing the next request
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            // Log or handle the IOException
            e.printStackTrace();
        } catch (JSONException | SrvException e) {
            // Log or handle the JSONException or SrvException
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Restore the interrupted status of the thread
            Thread.currentThread().interrupt();
            // Log or handle the InterruptedException
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                // Log or handle the IOException
                System.out.println("Error " + e);
            }
        }
    }

    private JSONObject handleRequest(JSONObject request) throws SrvException {
        JSONObject response = new JSONObject();
        System.out.println("REQUEST ESTE : " + request.toString());
        String type = request.getString("type");
        if (type.equals("CreateClient")) {
            System.out.println("Create Client request...");
            // String nume, String prenume, String email, String parola, String CNP, String statut
            String nume = request.getString("nume");
            String prenume = request.getString("prenume");
            String email = request.getString("email");
            String parola = request.getString("parola");
            String CNP = request.getString("CNP");
            String statut = request.getString("statut");
            try {
                if (server.alreadyExists(email, CNP)) {
                    response.put("type", "ErrorResponse");
                    System.out.println("EROARE LA CREATE CLIENT");
                    response.put("message", "Client already exists.");
                } else {
                    server.createClient(nume, prenume, email, parola, CNP, statut);
                    response.put("type", "OkResponse");
                }
            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }

        } else if (type.equals("Login")) {
            System.out.println("Login request...");
            String email = request.getString("email");
            String parola = request.getString("parola");
            try {
                this.currentUser = server.login(email, parola, this);
                if (server.isClient(currentUser.getId()))
                    response.put("type", "ClientResponse");
                else response.put("type", "ControlorResponse");

            } catch (SrvException e) {
                connected = false;
                System.out.println("EROARE LA LOGIN CLIENT");
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }

        } else if (type.equals("BuyTicket")) {
            System.out.println("Buy Ticket request...");
            LocalDateTime dataIncepere = LocalDateTime.now();
            LocalDateTime dataExpirare;
            Double pret = request.getDouble("pret");
            String tip = request.getString("tip");
            if (Objects.equals(tip, "URBAN TICKET") || Objects.equals(tip, "NIGHT TICKET"))
                dataExpirare = dataIncepere.plusMinutes(60);
            else if (Objects.equals(tip, "ONE DAY TICKET"))
                dataExpirare = dataIncepere.plusHours(24);
            else
                dataExpirare = dataIncepere.plusHours(78);
            try {
                System.out.println("ID CLIENT: " + this.currentUser.getId());
                server.buyTicket(dataIncepere, dataExpirare, pret, tip, this.currentUser.getId());
                response.put("type", "OkResponse");

            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        } else if (type.equals("GetTickets")) {
            System.out.println("ID-UL ESTE : " + currentUser.getId());
            System.out.println("Get tickets request...");
            List<Bilet> list = server.getTicketsByClientId(this.currentUser.getId());
            System.out.println("SIZE : " + list.size());
            response.put("size", list.size());
            for (int i = 0; i < list.size(); i++) {
                Bilet bilet = list.get(i);
                response.put("id" + i, bilet.getId());
                response.put("dataIncepere" + i, bilet.getDataIncepere().toString());
                response.put("dataExpirare" + i, bilet.getDataExpirare().toString());
                response.put("pret" + i, bilet.getPret());
                response.put("tip" + i, bilet.getTip());
                byte[] qr = server.getQr(bilet.getId());
                System.out.println("QR : " + Arrays.toString(qr));
                response.put("qr" + i, qr);
            }
        } else if (type.equals("BuyPass")) {
            System.out.println("Buy Pass request...");
            LocalDateTime dataIncepere = LocalDateTime.now();
            LocalDateTime dataExpirare = dataIncepere.plusMonths(1);
            Double pret = request.getDouble("pret");
            String tip = request.getString("tip");

            try {
                System.out.println("ID CLIENT: " + this.currentUser.getId());
                Abonament existingAbonament = server.findAbonamentByClientId(this.currentUser.getId());
                if (existingAbonament != null) {
                    response.put("type", "ErrorResponse");
                    response.put("message", "Client already has an active pass.");
                } else {
                    server.buyPass(dataIncepere, dataExpirare, pret, tip, this.currentUser.getId());
                    response.put("type", "OkResponse");
                }

            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        } else if (type.equals("VerifyPass")) {
            System.out.println("Verify Pass request...");
            try {
                System.out.println("ID CLIENT: " + this.currentUser.getId());
                Abonament existingAbonament = server.findAbonamentByClientId(this.currentUser.getId());
                if (existingAbonament != null) {
                    response.put("type", "ErrorResponse");
                    response.put("message", "Client already has an active pass.");
                } else {
                    response.put("type", "OkResponse");
                    response.put("message", "Client hasn't an active pass.");
                }
            } catch (Exception e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        } else if (type.equals("ShowPass")) {
            System.out.println("Show Pass request...");
            try {
                Abonament abonament = server.findAbonamentByClientId(this.currentUser.getId());
                if (abonament == null) {
                    System.out.println("No pass found for this client");
                    throw new SrvException("No pass found for this client");
                }
                System.out.println("ABONAMENT : " + abonament.getId() + " " + abonament.getDataIncepere() + " " + abonament.getDataExpirare() + " " + abonament.getPret() + " " + abonament.getTip());
                response.put("type", "AbonamentResponse");
                response.put("dataIncepere", abonament.getDataIncepere().toString());
                response.put("dataExpirare", abonament.getDataExpirare().toString());
                response.put("pret", abonament.getPret());
                response.put("tip", abonament.getTip());
                byte[] qr = server.getQr(abonament.getId());
                response.put("qr", qr);
            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        } else if (type.equals("OrarRequest")) {
            System.out.println("Orar img request...");
            String linie = request.getString("linie");
            byte[] img = server.getOrar(linie);
            System.out.println("IMAGINEA ESTE : " + img);
            response.put("type", "OrarResponse");
            response.put("imagine", img);

        } else if (type.equals("QRInfo")) {
            response.put("type","QRInfo");
            System.out.println("Qr Info request...");
            int id = request.getInt("id");
            Abonament abonament = server.findAbonament(id);
            String nume;
            String dataExpirare;
            String tip;
            if (abonament != null) {
                Client client = abonament.getCodClient();
                nume = client.getNume() + " " + client.getPrenume();
                dataExpirare = abonament.getDataExpirare().toString();
                tip = abonament.getTip();
            }
            else{
                Bilet bilet=server.findBilet(id);
                Client client=bilet.getCodClient();
                nume = client.getNume() + " " + client.getPrenume();
                dataExpirare=bilet.getDataExpirare().toString();
                tip=bilet.getTip();
            }
            response.put("nume",nume);
            response.put("dataExpirare",dataExpirare);
            response.put("tip",tip);
        }
        return response;
    }
}

