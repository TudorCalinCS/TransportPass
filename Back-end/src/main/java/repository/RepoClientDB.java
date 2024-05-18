package repository;

import domain.Client;
import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.SrvException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.Random;


public class RepoClientDB implements IRepoClient {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(RepoClientDB.class);

    public RepoClientDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
        logger.info("Initializing RepoClientDB  with properties: {} ", properties );
    }

    @Override
    public Client findOne(Integer aLong) {
        logger.traceEntry("Find client with email: {} ", aLong);
        if (aLong == null) {
            logger.error(new IllegalArgumentException("Id null"));
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("select * from Client where userId = ?")) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                String statut = resultSet.getString("statut");

                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
                User user = repoUserDB.findOne(aLong);

                if (user != null) {
                    Client client = new Client(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), statut);
                    logger.traceExit(client);
                    return client;
                } else {
                    logger.traceExit();
                    return null;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit("No client found with id: {}", aLong);
        return null;
    }

    public Client findOneByEmailAndCNP(String email, String CNP) {
        logger.traceEntry("Find client with email: {} and CNP: {}", email, CNP);

        if (email == null || CNP == null) {
            logger.error(new IllegalArgumentException("Email or CNP is null"));
            throw new IllegalArgumentException("Error! Email and CNP cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM User WHERE email = ? OR CNP = ?")) {
            statement.setString(1, email);
            statement.setString(2, CNP);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Integer userId = resultSet.getInt("id");
                //String statut = resultSet.getString("statut");

//                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
//                 user = repoUserDB.findOne(userId);
                Client client = findOne(userId);

                if (client != null) {
                    //Client client = new Client(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), statut);
                    logger.traceExit(client);
                    return client;
                }
                else {
                    logger.traceExit();
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        logger.traceExit();
        return null;
    }


    @Override
    public List<Client> findAll() {
        logger.traceEntry("Finding all clienti");
        List<Client> clients = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from Client")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer userID = resultSet.getInt("userId");
                String statut = resultSet.getString("statut");

                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
                User user = repoUserDB.findOne(userID);


                Client client = new Client(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), statut);
                clients.add(client);

            }
            logger.traceExit(clients);
            return clients;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Client entity) throws SrvException {
        Connection con = jdbcUtils.getConnection();
        logger.traceEntry("saving client: {}", entity);

        try (PreparedStatement prepStatement = con.prepareStatement("insert into Client(statut,userId) values (?,?)")) {

            RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());



            repoUserDB.save(new User(entity.getId(), entity.getNume(), entity.getPrenume(), entity.getEmail(), entity.getParola(), entity.getCNP()));

            prepStatement.setString(1, entity.getStatut());
            prepStatement.setInt(2, entity.getId());

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error from DataBase: " + e);
            logger.error(e);
            throw new SrvException(e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Client entity) {

    }

    @Override
    public void delete(Client entity) {

    }


}
