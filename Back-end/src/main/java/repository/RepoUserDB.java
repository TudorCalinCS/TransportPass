package repository;

import domain.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class RepoUserDB implements IRepoUser {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(RepoUserDB.class);

    public RepoUserDB(Properties properties) {

        logger.info("Initializing RepoUserDB  with properties: {} ", properties );
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public User findOne(Integer aLong) {

        logger.traceEntry("Find user with id: {} ", aLong);

        if (aLong == null) {
            logger.error(new IllegalArgumentException("Id null"));
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("select * from User " +
                "where id = ?")) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");

                User user = new User(aLong, nume, prenume, email, parola, CNP);
                logger.traceExit(user);
                return user;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit("No user found with id: {}", aLong);
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("saving user: {}", entity);
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement prepStatement = con.prepareStatement("insert into User(id,nume,prenume,email,parola,CNP) " +
                "values (?,?,?,?,?,?)")) {
            prepStatement.setInt(1, entity.getId());
            prepStatement.setString(2, entity.getNume());
            prepStatement.setString(3, entity.getPrenume());
            prepStatement.setString(4, entity.getEmail());
            prepStatement.setString(5, encryptPassword(entity.getParola()));
            prepStatement.setString(6, entity.getCNP());

            int affectedRows = prepStatement.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error(e);
            System.out.println("Error from DataBase: " + e);
        }
        logger.traceExit();
    }

    public User findOneByUsernameAndPassword(String username, String password) {

        logger.traceEntry("Find user with username: {} and password: {}", username, password);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.error("Username or password is null or empty");
            throw new IllegalArgumentException("Error! Username and password cannot be null or empty!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM User WHERE email = ? AND parola = ?")) {
            statement.setString(1, username);
            statement.setString(2, encryptPassword(password));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                User user = new User(id, nume, prenume, email, parola, CNP);
                logger.traceExit(user);
                return user;
            }
            else{
                System.out.println("No user found with username: " + username + " and password: " + password);
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error("Error executing SQL query", e);
            throw new RuntimeException("Error executing SQL query", e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public void update(User entity) {
        logger.traceEntry("updating user: {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement prepStatement = con.prepareStatement("update User set parola=? where email=?")) {
            prepStatement.setString(1, entity.getNume());
            prepStatement.setString(2, entity.getPrenume());
            prepStatement.setString(3, entity.getEmail());
            prepStatement.setString(4, encryptPassword(entity.getParola()));
            prepStatement.setString(5, entity.getCNP());
            int result = prepStatement.executeUpdate();
            if(result == 0) logger.traceExit("could not update user: {}", entity);
            else logger.traceExit("updated user: {} ", entity);
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
            logger.error(ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public void update_password(String username, String newPassword) {
        logger.traceEntry("updating password for user: {} ", username);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement prepStatement = con.prepareStatement("update User set parola=? where email=?")) {
            prepStatement.setString(1, encryptPassword(newPassword));
            prepStatement.setString(2, username);
            int result = prepStatement.executeUpdate();
            if(result == 0){
                logger.traceExit("could not update password for user: {}", username);
                throw new RuntimeException("No such user exists or no update necessary.");
            }
            else logger.traceExit("updated password for user: {} ", username);
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
            logger.error(ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User entity) {

    }

    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }



}
