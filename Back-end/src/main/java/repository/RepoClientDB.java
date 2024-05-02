package repository;

import domain.Client;
import domain.User;
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

    public RepoClientDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Client findOne(Long aLong) {
        if (aLong == null) {
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
                    return client;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from Client")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long userID = resultSet.getLong("userId");
                String statut = resultSet.getString("statut");

                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
                User user = repoUserDB.findOne(userID);


                Client client = new Client(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), statut);
                clients.add(client);

            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Client entity) throws SrvException {
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement prepStatement = con.prepareStatement("insert into Client(statut,userId) values (?,?)")) {

            RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());

            Random random = new Random();
            long randomLong = random.nextLong();

            repoUserDB.save(new User(randomLong, entity.getNume(), entity.getPrenume(), entity.getEmail(), entity.getParola(), entity.getCNP()));

            prepStatement.setString(1, entity.getStatut());
            prepStatement.setLong(2, randomLong);

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error from DataBase: " + e);
            throw new SrvException(e.getMessage());
        }

    }

    @Override
    public void update(Client entity) {

    }

    @Override
    public void delete(Client entity) {

    }

    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
