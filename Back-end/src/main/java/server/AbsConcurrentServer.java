//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

import java.net.Socket;

public abstract class AbsConcurrentServer extends AbstractServer {
    public AbsConcurrentServer(int port) {
        super(port);
        logger.info("Concurrent AbstractServer");
        
    }

    protected void processRequest(Socket client) {
        Thread tw = this.createWorker(client);
        tw.start();
    }
    protected abstract Thread createWorker(Socket var1);
}
