package repository;
import domain.Abonament;
import domain.Bilet;
import domain.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepoAbonamentDB implements IRepoAbonament {
    private JdbcUtils jdbcUtils;
    private IRepoClient repoClient;
    public RepoAbonamentDB(Properties properties,IRepoClient repoClient) {
        this.jdbcUtils = new JdbcUtils(properties);
        this.repoClient=repoClient;
    }

    @Override
    public Abonament findOne(Long aLong) {
        if(aLong == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from Abonament " +
                "where id = ?")){

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                LocalDateTime dataIncepere = resultSet.getTimestamp("dataIncepere").toLocalDateTime();
                LocalDateTime dataExpirare = resultSet.getTimestamp("dataExpirare").toLocalDateTime();
                Double pret = resultSet.getDouble("pret");
                String tip = resultSet.getString("tip");
                Long idClient=resultSet.getLong("idClient");
                Client client=repoClient.findOne(idClient);
                Abonament abonament=new Abonament(aLong,dataIncepere,dataExpirare,pret,tip,client);
                return abonament;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public List<Abonament> findAll() {
        Connection con = jdbcUtils.getConnection();
        List<Abonament> abonamente = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement("select * from Abonament")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    LocalDateTime dataIncepere = result.getTimestamp("dataIncepere").toLocalDateTime();
                    LocalDateTime dataExpirare = result.getTimestamp("dataExpirare").toLocalDateTime();
                    Double pret = result.getDouble("pret");
                    String tip = result.getString("tip");
                    Long idClient=result.getLong("idClient");
                    Client client=repoClient.findOne(idClient);
                    Abonament abonament=new Abonament(id,dataIncepere,dataExpirare,pret,tip,client);
                    abonamente.add(abonament);
                }
            }
        }catch(SQLException e){
            System.err.println("Error DB " + e);
        }
        return abonamente;
    }

    @Override
    public void save(Abonament entity) {
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement prepStatement = con.prepareStatement("insert into Abonament(dataIncepere, dataExpirare, pret, tip, idClient) " +
                "values (?,?,?,?,?)")){
            prepStatement.setString(1, entity.getDataIncepere().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setString(2, entity.getDataExpirare().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setDouble(3,entity.getPret());
            prepStatement.setString(4,entity.getTip());
            prepStatement.setLong(5, entity.getCodClient().getId());

            int affectedRows = prepStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Error from DataBase: " + e);
        }
    }

    @Override
    public void update(Abonament entity) {
        Connection con= jdbcUtils.getConnection();
        try(PreparedStatement prepStatement=con.prepareStatement("update Bilet set dataIncepere=?, dataExpirare=? where id=?"))
        {
            prepStatement.setString(1, entity.getDataIncepere().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setString(2, entity.getDataExpirare().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            prepStatement.setDouble(3,entity.getPret());
            prepStatement.setString(4,entity.getTip());
            prepStatement.setLong(5, entity.getCodClient().getId());
            int result = prepStatement.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
    }

    @Override
    public void delete(Abonament entity) {
        Connection con = jdbcUtils.getConnection();
        LocalDateTime today = LocalDateTime.now();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Abonament WHERE dataExpirare < ?")) {
            preStmt.setObject(1, today);
            int rowsAffected = preStmt.executeUpdate();
            System.out.println(rowsAffected + " abonament expirat au fost șterse.");
        } catch (SQLException ex) {
            System.err.println("Eroare în baza de date: " + ex.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Eroare la închiderea conexiunii: " + e.getMessage());
            }
        }
    }
}

