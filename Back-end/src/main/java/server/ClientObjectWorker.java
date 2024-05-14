//package server;
//
//
//import domain.User;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.*;
//import java.net.Socket;
//import java.time.LocalDateTime;
//
//
//public class ClientObjectWorker implements Runnable, IObserver {
//    private IServices server;
//    private Socket connection;
//    private ObjectInputStream input;
//    private ObjectOutputStream output;
//    private volatile boolean connected;
//
//    private User currentUser;
//
//    public ClientObjectWorker(IServices server, Socket connection) {
//        this.server = server;
//        this.connection = connection;
//        try {
//            output = new ObjectOutputStream(connection.getOutputStream());
//            output.flush();
//            input = new ObjectInputStream(connection.getInputStream());
//            connected = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
////
//
////    public void run() {
////        while (connected) {
////            try {
////                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////                String jsonString = reader.readLine();
////                JSONObject jsonRequest = new JSONObject(jsonString);
////
////                JSONObject response = handleRequest(jsonRequest);
////                sendResponse(response);
////            } catch (IOException e) {
////                e.printStackTrace();
////            } catch (JSONException | SrvException e) {
////                e.printStackTrace();
////            }
////            try {
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
////        try {
////            input.close();
////            output.close();
////            connection.close();
////        } catch (IOException e) {
////            System.out.println("Error " + e);
////        }
////    }
//
//    public void run() {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            while (connected) {
//                String jsonString = reader.readLine();
//                if (jsonString == null) {
//                    // Handle end of stream
//                    break;
//                }
//                JSONObject jsonRequest = new JSONObject(jsonString);
//                JSONObject response = handleRequest(jsonRequest);
//                sendResponse(response);
//
//                // Sleep for some time before processing the next request
//                Thread.sleep(1000);
//            }
//        } catch (IOException e) {
//            // Log or handle the IOException
//            e.printStackTrace();
//        } catch (JSONException | SrvException e) {
//            // Log or handle the JSONException or SrvException
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            // Restore the interrupted status of the thread
//            Thread.currentThread().interrupt();
//            // Log or handle the InterruptedException
//            e.printStackTrace();
//        } finally {
//            try {
//                if (input != null) input.close();
//                if (output != null) output.close();
//                connection.close();
//            } catch (IOException e) {
//                // Log or handle the IOException
//                System.out.println("Error " + e);
//            }
//        }
//    }
//
//
//
//    private JSONObject handleRequest(JSONObject request) throws SrvException {
//        JSONObject response = new JSONObject();
//        String type = request.getString("type");
//        if (type.equals("CreateClient")) {
//            System.out.println("Create Client request...");
//            //String nume, String prenume, String email, String parola, String CNP, String statut
//            String nume = request.getString("nume");
//            String prenume = request.getString("prenume");
//            String email = request.getString("email");
//            String parola = request.getString("parola");
//            String CNP = request.getString("CNP");
//            String statut = request.getString("statut");
//            try {
//                server.createClient(nume, prenume, email, parola, CNP, statut);
//                response.put("type", "OkResponse");
//            } catch (SrvException e) {
//                response.put("type", "ErrorResponse");
//                response.put("message", e.getMessage());
//            }
//
//        }
//        if (type.equals("LoginClient")) {
//            System.out.println("Login Client request...");
//            String email = request.getString("email");
//            String parola = request.getString("parola");
//            try {
//                this.currentUser = server.login(email, parola, this);
//                response.put("type", "OkResponse");
//
//            } catch (SrvException e) {
//                connected = false;
//                response.put("type", "ErrorResponse");
//                response.put("message", e.getMessage());
//            }
//        }
//        if (type.equals("BuyTicket")) {
//            System.out.println("Buy Ticket request...");
//            LocalDateTime dataIncepere = LocalDateTime.parse(request.getString("dataIncepere"));
//            LocalDateTime dataExpirare = LocalDateTime.parse(request.getString("dataExpirare"));
//            Double pret = request.getDouble("pret");
//            String tip = request.getString("tip");
//            Long idClient = Long.valueOf(request.getString("codClient"));
//            try {
//                server.buyTicket(dataIncepere, dataExpirare, pret, tip, idClient);
//                response.put("type", "OkResponse");
//
//            } catch (SrvException e) {
//                response.put("type", "ErrorResponse");
//                response.put("message", e.getMessage());
//            }
//        }
//        /*
//        if (request instanceof LogoutRequest) {
//            System.out.println("Logout request");
//            LogoutRequest logReq = (LogoutRequest) request;
//            UserDTO udto = logReq.getUser();
//            User user = DTOUtils.getFromDTO(udto);
//            try {
//                server.logout(user, this);
//                connected = false;
//                return new OkResponse();
//
//            } catch (ChatException e) {
//                return new ErrorResponse(e.getMessage());
//            }
//        }
//        if (request instanceof SendMessageRequest) {
//            System.out.println("SendMessageRequest ...");
//            SendMessageRequest senReq = (SendMessageRequest) request;
//            MessageDTO mdto = senReq.getMessage();
//            Message message = DTOUtils.getFromDTO(mdto);
//            try {
//                server.sendMessage(message);
//                return new OkResponse();
//            } catch (ChatException e) {
//                return new ErrorResponse(e.getMessage());
//            }
//        }
//
//        if (request instanceof GetLoggedFriendsRequest) {
//            System.out.println("GetLoggedFriends Request ...");
//            GetLoggedFriendsRequest getReq = (GetLoggedFriendsRequest) request;
//            UserDTO udto = getReq.getUser();
//            User user = DTOUtils.getFromDTO(udto);
//            try {
//                User[] friends = server.getLoggedFriends(user);
//                UserDTO[] frDTO = DTOUtils.getDTO(friends);
//                return new GetLoggedFriendsResponse(frDTO);
//            } catch (ChatException e) {
//                return new ErrorResponse(e.getMessage());
//            }
//        }
//
//         */
//        return response;
//    }
//
//    private void sendResponse(JSONObject response) throws IOException {
//        System.out.println("sending response " + response.toString());
//        synchronized (output) {
//            output.writeObject(response);
//            output.flush();
//        }
//    }
//}

package server;

import domain.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;

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
        System.out.println("SUNTEM IN HANDLE REQUEST");
        JSONObject response = new JSONObject();
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
                server.createClient(nume, prenume, email, parola, CNP, statut);
                response.put("type", "OkResponse");
            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }

        } else if (type.equals("LoginClient")) {
            System.out.println("Login Client request...");
            String email = request.getString("email");
            String parola = request.getString("parola");
            try {
                this.currentUser = server.login(email, parola, this);
                response.put("type", "OkResponse");

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
            if(Objects.equals(tip, "URBAN TICKET") || Objects.equals(tip, "NIGHT TICKET"))
                dataExpirare = dataIncepere.plusMinutes(60);
            else if(Objects.equals(tip, "ONE DAY TICKET"))
                dataExpirare = dataIncepere.plusHours(24);
            else
                dataExpirare = dataIncepere.plusHours(78);
            try {
                System.out.println("ID CLIENT: " + this.currentUser.getId());
                server.buyTicket(dataIncepere, dataExpirare, pret, tip,this.currentUser.getId());
                response.put("type", "OkResponse");

            } catch (SrvException e) {
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        }
        return response;
    }
}

