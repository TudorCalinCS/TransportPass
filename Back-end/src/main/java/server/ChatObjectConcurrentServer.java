package server;

import java.net.Socket;


public class ChatObjectConcurrentServer extends AbsConcurrentServer {
    private IServices chatServer;

    public ChatObjectConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker = new ClientObjectWorker(chatServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }


}
