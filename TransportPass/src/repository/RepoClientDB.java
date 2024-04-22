package repository;

import domain.Client;
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

public class RepoClientDB implements IRepoClient{
    private JdbcUtils jdbcUtils;
    public RepoClientDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }
    @Override
    public Client findOne(Long aLong) {
        if(aLong == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from Client " +
                "where id = ?")){

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                String statut = resultSet.getString("statut");

                Client client = new Client(aLong,nume,prenume,email,parola,CNP,statut);
                return client;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from Client")){

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                String statut = resultSet.getString("statut");

                Client client = new Client(id,nume,prenume,email,parola,CNP,statut);
                clients.add(client);

            }
            return clients;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Client entity) {
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement prepStatement = con.prepareStatement("insert into Client(nume,prenume,email,parola,CNP,statut) " +
                "values (?,?,?,?,?,?)")){
            prepStatement.setString(1,entity.getNume());
            prepStatement.setString(2,entity.getPrenume());
            prepStatement.setString(3,entity.getEmail());
            prepStatement.setString(4, encryptPassword(entity.getParola()));
            prepStatement.setString(5,entity.getCNP());
            prepStatement.setString(6,entity.getStatut());

            int affectedRows = prepStatement.executeUpdate();
        }
        catch(SQLException | NoSuchAlgorithmException e){
            System.out.println("Error from DataBase: " + e);
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
