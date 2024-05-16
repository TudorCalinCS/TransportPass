package repository;

import domain.Bilet;
import domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoBiletDB implements IRepoBilet {
    private JdbcUtils jdbcUtils;
    private IRepoClient repoClient;
    private static final Logger logger = LogManager.getLogger(RepoBiletDB.class);

    public RepoBiletDB(Properties properties, IRepoClient repoClient) {
        logger.info("Initializing RepoBiletDB  with properties: {} ", properties );
        this.jdbcUtils = new JdbcUtils(properties);
        this.repoClient = repoClient;
    }

    @Override
    public Bilet findOne(Long aLong) {
        logger.traceEntry("Find bilet with id: {} ", aLong);
        if (aLong == null) {
            logger.error(new IllegalArgumentException("Id null"));
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("select * from Bilet " +
                "where id = ?")) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                LocalDateTime dataIncepere = resultSet.getTimestamp("dataIncepere").toLocalDateTime();
                LocalDateTime dataExpirare = resultSet.getTimestamp("dataExpirare").toLocalDateTime();
                Double pret = resultSet.getDouble("pret");
                String tip = resultSet.getString("tip");
                Long idClient = resultSet.getLong("idClient");
                Client client = repoClient.findOne(idClient);
                Bilet bilet = new Bilet(aLong, dataIncepere, dataExpirare, pret, tip, client);
                logger.traceExit(bilet);
                return bilet;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit("No bilet found with id: {}", aLong);
        return null;
    }


    @Override
    public List<Bilet> findAll() {
        logger.traceEntry("Finding all bilete");
        Connection con = jdbcUtils.getConnection();
        List<Bilet> tickets = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from Bilet")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    LocalDateTime dataIncepere = result.getTimestamp("dataIncepere").toLocalDateTime();
                    LocalDateTime dataExpirare = result.getTimestamp("dataExpirare").toLocalDateTime();
                    Double pret = result.getDouble("pret");
                    String tip = result.getString("tip");
                    Long idClient = result.getLong("idClient");
                    Client client = repoClient.findOne(idClient);
                    Bilet bilet = new Bilet(id, dataIncepere, dataExpirare, pret, tip, client);
                    tickets.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public void save(Bilet entity) {
        Connection con = jdbcUtils.getConnection();
        logger.traceEntry("saving bilet: {}", entity);

        try (PreparedStatement prepStatement = con.prepareStatement("insert into Bilet(dataIncepere, dataExpirare, pret, tip, idClient) " +
                "values (?,?,?,?,?)")) {
            prepStatement.setString(1, entity.getDataIncepere().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setString(2, entity.getDataExpirare().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setDouble(3, entity.getPret());
            prepStatement.setString(4, entity.getTip());
            prepStatement.setLong(5, entity.getCodClient().getId());

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error from DataBase: " + e);
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Bilet entity) {

    }

    @Override
    public void delete(Bilet entity) {
        Connection con = jdbcUtils.getConnection();
        logger.traceEntry("deleting bilet: {} ", entity);
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Bilet WHERE id = ?")) {
            preStmt.setLong(1, entity.getId());
            int rowsAffected = preStmt.executeUpdate();
            if(rowsAffected == 0) logger.traceExit("could not delete: {}", entity);
            else logger.traceExit("deleted bilet: {} ", entity);
        } catch (SQLException ex) {
            System.err.println("Eroare în baza de date: " + ex.getMessage());
            logger.error(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Eroare la închiderea conexiunii: " + e.getMessage());
            }
        }
    }
    public List<Bilet> findAllByUser(Long idClient) {
        logger.traceEntry("Finding all bilete");
        Connection con = jdbcUtils.getConnection();
        List<Bilet> tickets = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from Bilet where idClient = ?")) {
            try (ResultSet result = statement.executeQuery()) {
                statement.setLong(1,idClient);
                while (result.next()) {
                    Long id = result.getLong("id");
                    LocalDateTime dataIncepere = result.getTimestamp("dataIncepere").toLocalDateTime();
                    LocalDateTime dataExpirare = result.getTimestamp("dataExpirare").toLocalDateTime();
                    Double pret = result.getDouble("pret");
                    String tip = result.getString("tip");
                    Client client = repoClient.findOne(idClient);
                    Bilet bilet = new Bilet(id, dataIncepere, dataExpirare, pret, tip, client);
                    tickets.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(tickets);
        return tickets;
    }
}
