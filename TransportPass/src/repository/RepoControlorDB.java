package repository;

import domain.Client;
import domain.Controlor;

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

public class RepoControlorDB implements IRepoControlor{
    private JdbcUtils jdbcUtils;
    public RepoControlorDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }
    @Override
    public Controlor findOne(Long aLong) {
        if(aLong == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from Controlor " +
                "where id = ?")){

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                String numarLegitimatie = resultSet.getString("legitimatie");

                Controlor controlor = new Controlor(aLong,nume,prenume,email,parola,CNP,numarLegitimatie);
                return controlor;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Controlor> findAll() {
        List<Controlor> controlors = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from Controlor")){

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                String CNP = resultSet.getString("CNP");
                String nrLegitimatie = resultSet.getString("legitimatie");

                Controlor controlor = new Controlor(id,nume,prenume,email,parola,CNP,nrLegitimatie);
                controlors.add(controlor);

            }
            return controlors;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Controlor entity) {
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement prepStatement = con.prepareStatement("insert into Controlor(nume,prenume,email,parola,CNP,legitimatie) " +
                "values (?,?,?,?,?,?)")){
            prepStatement.setString(1,entity.getNume());
            prepStatement.setString(2,entity.getPrenume());
            prepStatement.setString(3,entity.getEmail());
            prepStatement.setString(4, encryptPassword(entity.getParola()));
            prepStatement.setString(5,entity.getCNP());
            prepStatement.setString(6,entity.getNumarLegitimatie());

            int affectedRows = prepStatement.executeUpdate();
        }
        catch(SQLException | NoSuchAlgorithmException e){
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
