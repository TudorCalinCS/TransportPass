package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RepoImagineDB {
    private JdbcUtils jdbcUtils;

    public RepoImagineDB(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    public byte[] findOne(int id) {
        Connection con = jdbcUtils.getConnection();
        String sql = "SELECT continut_imagine FROM imagini WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBytes("continut_imagine");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void save(String nume,String linie, byte[] continut_imagine) {
        Connection con = jdbcUtils.getConnection();
        String sql = "INSERT INTO imagini (nume,linie, continut_imagine) VALUES (?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, nume);
            statement.setString(2, linie);
            statement.setBytes(3, continut_imagine);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}