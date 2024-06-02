package repository;

import domain.Abonament;
import domain.Bilet;
import domain.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.SrvException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepoAbonamentDB implements IRepoAbonament {
    private JdbcUtils jdbcUtils;
    private IRepoClient repoClient;
    private static final Logger logger = LogManager.getLogger(RepoAbonamentDB.class);

    public RepoAbonamentDB(Properties properties, IRepoClient repoClient) {
        logger.info("Initializing RepoAbonamentDB  with properties: {} ", properties);
        this.jdbcUtils = new JdbcUtils(properties);
        this.repoClient = repoClient;
    }

    @Override
    public Abonament findOne(Integer aLong) {
        logger.traceEntry("Find abonament with id: {} ", aLong);
        if (aLong == null) {
            logger.error(new IllegalArgumentException("Id null"));
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("select * from Abonament " +
                "where id = ?")) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                LocalDateTime dataIncepere = resultSet.getTimestamp("dataIncepere").toLocalDateTime();
                LocalDateTime dataExpirare = resultSet.getTimestamp("dataExpirare").toLocalDateTime();
                Double pret = resultSet.getDouble("pret");
                String tip = resultSet.getString("tip");
                Integer idClient = resultSet.getInt("idClient");
                Client client = repoClient.findOne(idClient);
                Abonament abonament = new Abonament(aLong, dataIncepere, dataExpirare, pret, tip, client);
                logger.traceExit(abonament);
                return abonament;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit("No abonament found with id: {}", aLong);
        return null;
    }


    @Override
    public List<Abonament> findAll() {
        logger.traceEntry("Finding all abonamente");
        Connection con = jdbcUtils.getConnection();
        List<Abonament> abonamente = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from Abonament")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    LocalDateTime dataIncepere = result.getTimestamp("dataIncepere").toLocalDateTime();
                    LocalDateTime dataExpirare = result.getTimestamp("dataExpirare").toLocalDateTime();
                    Double pret = result.getDouble("pret");
                    String tip = result.getString("tip");
                    Integer idClient = result.getInt("idClient");
                    Client client = repoClient.findOne(idClient);
                    Abonament abonament = new Abonament(id, dataIncepere, dataExpirare, pret, tip, client);
                    abonamente.add(abonament);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(abonamente);
        return abonamente;
    }

    @Override
    public void save(Abonament entity) {
        logger.traceEntry("saving abonament: {}", entity);
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement prepStatement = con.prepareStatement("insert into Abonament(id,dataIncepere, dataExpirare, pret, tip, idClient) " +
                "values (?,?,?,?,?,?)")) {
            prepStatement.setInt(1, entity.getId());
            prepStatement.setString(2, entity.getDataIncepere().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setString(3, entity.getDataExpirare().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setDouble(4, entity.getPret());
            prepStatement.setString(5, entity.getTip());
            prepStatement.setLong(6, entity.getCodClient().getId());

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error from DataBase: " + e);
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Abonament entity) {
        logger.traceEntry("updating abonament: {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement prepStatement = con.prepareStatement("UPDATE Abonament SET dataIncepere=?, dataExpirare=? WHERE id=?")) {
            LocalDateTime newStartDate = entity.getDataIncepere().plusMonths(1);
            LocalDateTime newEndDate = entity.getDataExpirare().plusMonths(1);
            prepStatement.setString(1, newStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setString(2, newEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setDouble(3, entity.getPret());
            prepStatement.setString(4, entity.getTip());
            prepStatement.setLong(5, entity.getCodClient().getId());

            int result = prepStatement.executeUpdate();
            if (result == 0) {
                logger.traceExit("could not update abonament: {}", entity);
            } else {
                logger.traceExit("updated abonament: {} ", entity);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
    }


    @Override
    public void delete(Abonament entity) {

    }

    public void deleteAbonamente() {
        logger.traceEntry("deleting abonamente: {} ");
        Connection con = jdbcUtils.getConnection();
        LocalDateTime today = LocalDateTime.now();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Abonament WHERE dataExpirare < ?")) {
            preStmt.setObject(1, today);
            int rowsAffected = preStmt.executeUpdate();
            if (rowsAffected == 0) logger.traceExit("could not delete: {}");
            else logger.traceExit("deleted: {} ");
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

    public Abonament findAbonamentByUser(Integer idUser) {
        logger.traceEntry("finding Pass by User's id: {} ", idUser);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * from Abonament " + "where idClient = ?")) {

            statement.setInt(1, Math.toIntExact(idUser));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer idPass = resultSet.getInt("id");
                LocalDateTime dataIncepere = resultSet.getTimestamp("dataIncepere").toLocalDateTime();
                LocalDateTime dataExpirare = resultSet.getTimestamp("dataExpirare").toLocalDateTime();
                Double pret = resultSet.getDouble("pret");
                String tip = resultSet.getString("tip");
                Integer idClient = resultSet.getInt("idClient");
                Client client = repoClient.findOne(idClient);
                Abonament abonament = new Abonament(idPass, dataIncepere, dataExpirare, pret, tip, client);
                logger.traceExit(abonament);
                return abonament;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);

        }
        return null;
    }
}

