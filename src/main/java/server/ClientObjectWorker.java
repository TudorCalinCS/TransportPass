package server;


import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientObjectWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                JSONObject jsonRequest = new JSONObject(request.toString());

                JSONObject response = handleRequest(jsonRequest);
                sendResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }


    private JSONObject handleRequest(JSONObject request) {
        JSONObject response = new JSONObject();
        String type = request.getString("type");
        if (type.equals("CreateClient")) {
            System.out.println("Create Client request...");
            //String nume, String prenume, String email, String parola, String CNP, String statut
            String nume = request.getString("nume");
            String prenume = request.getString("prenume");
            String email = request.getString("email");
            String parola = request.getString("parola");
            String CNP = request.getString("CNP");
            String statut = request.getString("statut");

            server.createClient(nume, prenume, email, parola, CNP, statut);
            response.put("type", "OkResponse");

        }
        if (type.equals("LoginClient")) {
            System.out.println("Login Client request ...");
            String email = request.getString("email");
            String parola = request.getString("parola");
            try {
                server.login(email, parola, this);
                response.put("type", "OkResponse");
            } catch (ChatException e) {
                connected = false;
                response.put("type", "ErrorResponse");
                response.put("message", e.getMessage());
            }
        }
        /*
        if (request instanceof LogoutRequest) {
            System.out.println("Logout request");
            LogoutRequest logReq = (LogoutRequest) request;
            UserDTO udto = logReq.getUser();
            User user = DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected = false;
                return new OkResponse();

            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof SendMessageRequest) {
            System.out.println("SendMessageRequest ...");
            SendMessageRequest senReq = (SendMessageRequest) request;
            MessageDTO mdto = senReq.getMessage();
            Message message = DTOUtils.getFromDTO(mdto);
            try {
                server.sendMessage(message);
                return new OkResponse();
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof GetLoggedFriendsRequest) {
            System.out.println("GetLoggedFriends Request ...");
            GetLoggedFriendsRequest getReq = (GetLoggedFriendsRequest) request;
            UserDTO udto = getReq.getUser();
            User user = DTOUtils.getFromDTO(udto);
            try {
                User[] friends = server.getLoggedFriends(user);
                UserDTO[] frDTO = DTOUtils.getDTO(friends);
                return new GetLoggedFriendsResponse(frDTO);
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

         */
        return response;
    }

    private void sendResponse(JSONObject response) throws IOException {
        System.out.println("sending response " + response.toString());
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
