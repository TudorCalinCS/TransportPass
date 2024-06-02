package server;


import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.*;
import utils.HttpClientExample;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort = 55555;
    private static final Logger logger = LogManager.getLogger(StartObjectServer.class);

    public static void main(String[] args) throws SrvException, IOException {
        logger.traceEntry();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            logger.info("Server properties set.");
            serverProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find chatserver.properties " + e);
            return;
        }
        RepoUserDB repoUserDB = new RepoUserDB(serverProps);
        RepoClientDB repoClientDB = new RepoClientDB(serverProps);
        RepoBiletDB repoBiletDB = new RepoBiletDB(serverProps, repoClientDB);
        RepoAbonamentDB repoAbonamentDB = new RepoAbonamentDB(serverProps, repoClientDB);
        RepoControlorDB repoControlorDB = new RepoControlorDB(serverProps);
        RepoImagineDB repoImagineDB = new RepoImagineDB(serverProps);
        repoAbonamentDB.deleteAbonamente();
        repoBiletDB.deleteBilete();
        IServices chatServerImpl = new ServicesImpl(repoClientDB, repoControlorDB, repoUserDB, repoAbonamentDB, repoBiletDB, repoImagineDB);

        chatServerImpl.checkStudent(HttpClientExample.getImageData());

        int chatServerPort = defaultPort;

        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        } catch (NumberFormatException nef) {
            logger.error("Wrong  Port Number" + nef.getMessage());
            logger.info("Using default port " + defaultPort);
        }
        logger.info("Starting server on port: " + chatServerPort);
        AbstractServer server = new ChatObjectConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();

        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }

}
