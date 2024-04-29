package repository;

import domain.Client;
import domain.Controlor;
import domain.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepoControlorDB implements IRepoControlor {
    private JdbcUtils jdbcUtils;

    public RepoControlorDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Controlor findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("select * from Controlor " +
                "where userId = ?")) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                String numarLegitimatie = resultSet.getString("legitimatie");

                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
                User user = repoUserDB.findOne(aLong);

                if (user != null) {
                    Controlor controlor = new Controlor(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), numarLegitimatie);
                    return controlor;
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
    public List<Controlor> findAll() {
        List<Controlor> controlors = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from Controlor")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String numarLegitimatie = resultSet.getString("legitimatie");

                RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());
                Long userID = resultSet.getLong("userId");

                User user = repoUserDB.findOne(userID);

                if (user != null) {
                    Controlor controlor = new Controlor(user.getId(), user.getNume(), user.getPrenume(), user.getEmail(), user.getParola(), user.getCNP(), numarLegitimatie);
                    controlors.add(controlor);

                }

            }
            return controlors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Controlor entity) {
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement prepStatement = con.prepareStatement("insert into Controlor(legitimatie,userId) " +
                "values (?,?)")) {

            RepoUserDB repoUserDB = new RepoUserDB(jdbcUtils.getJdbcProps());

            Random random = new Random();
            long randomLong = random.nextLong();

            repoUserDB.save(new User(randomLong, entity.getNume(), entity.getPrenume(), entity.getEmail(), entity.getParola(), entity.getCNP()));

            prepStatement.setString(1, entity.getNumarLegitimatie());
            prepStatement.setLong(2, randomLong);

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error from DataBase: " + e);
        }

    }

    @Override
    public void update(Controlor entity) {

    }

    @Override
    public void delete(Controlor entity) {

    }

    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
