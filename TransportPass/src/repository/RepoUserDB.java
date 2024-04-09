package repository;

import domain.User;
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

public class RepoUserDB implements IRepoUser{

    private JdbcUtils jdbcUtils;
    public RepoUserDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }
    @Override
    public User findOne(Long aLong) {

        if(aLong == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from User " +
                "where id = ?")){

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");

                User user = new User(aLong,nume,prenume,email,parola,CNP);
                return user;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from User")){

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");

                User user = new User(id,nume,prenume,email,parola,CNP);
                users.add(user);

            }
            return users;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User entity) {
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement prepStatement = con.prepareStatement("insert into User(nume,prenume,email,parola,CNP) " +
                "values (?,?,?,?,?)")){
            prepStatement.setString(1,entity.getNume());
            prepStatement.setString(2,entity.getPrenume());
            prepStatement.setString(3,entity.getEmail());
            prepStatement.setString(4, encryptPassword(entity.getParola()));
            prepStatement.setString(5,entity.getCNP());

            int affectedRows = prepStatement.executeUpdate();
        }
        catch(SQLException | NoSuchAlgorithmException e){
            System.out.println("Error from DataBase: " + e);
        }

    }
    public User findOneByUsernameAndPassword(String username, String password) {

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Error! Username and password cannot be null or empty!");
        }

        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM User WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, encryptPassword(password));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                User user = new User(id, nume, prenume, email, parola, CNP);
                return user;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error executing SQL query", e);
        }
        return null;
    }


    @Override
    public void update(User entity) {
    }
    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
