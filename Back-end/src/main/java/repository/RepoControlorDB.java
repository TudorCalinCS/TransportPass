package repository;

import domain.Client;
import domain.Controlor;
import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepoControlorDB implements IRepoControlor {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(RepoControlorDB.class);

    public RepoControlorDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
        logger.info("Initializing RepoControlorDB  with properties: {} ", properties );
    }

    @Override
    public Controlor findOne(Long aLong) {
        logger.traceEntry("Find controlor with id: {} ", aLong);
        if (aLong == null) {
            logger.error(new IllegalArgumentException("Id null"));
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
                    logger.traceExit(controlor);
                    return controlor;
                } else {
                    logger.traceExit();
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit("No controlor found with id: {}", aLong);
        return null;
    }

    @Override
    public List<Controlor> findAll() {
        logger.traceEntry("Finding all controlori");
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
            logger.traceExit(controlors);
            return controlors;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Controlor entity) {
        Connection con = jdbcUtils.getConnection();
        logger.traceEntry("saving controlor: {}", entity);

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
            logger.error(e);
            System.out.println("Error from DataBase: " + e);
        }
        logger.traceExit();

    }

    @Override
    public void update(Controlor entity) {

    }

    @Override
    public void delete(Controlor entity) {

    }
}
