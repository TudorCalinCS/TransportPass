//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class AbstractServer {
    private int port;
    private ServerSocket server = null;
    static final Logger logger = LogManager.getLogger(AbstractServer.class);


    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() throws ServerException {
        try {
            //this.server = new ServerSocket(this.port,50, InetAddress.getByName("192.168.221.189"));
            this.server = new ServerSocket(this.port,50, InetAddress.getByName("127.0.0.1"));

            while (true) {
                logger.info("ADRESA SERVERULUI ESTE : " +server.getInetAddress());
                logger.info("PORTUL ESTE : " + port);
                logger.info("Waiting for clients ...");
                Socket client = this.server.accept();
                logger.info("Client connected ...");
                this.processRequest(client);
            }
        } catch (IOException var5) {
            throw new ServerException("Starting server errror ", var5);
        } finally {
            this.stop();
        }
    }

    protected abstract void processRequest(Socket var1);

    public void stop() throws ServerException {
        try {
            this.server.close();
        } catch (IOException var2) {
            throw new ServerException("Closing server error ", var2);
        }
    }
}
