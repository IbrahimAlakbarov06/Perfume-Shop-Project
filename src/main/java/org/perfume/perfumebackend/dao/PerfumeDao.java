package org.perfume.perfumebackend.dao;

import org.perfume.perfumebackend.config.DatabaseConnection;
import org.perfume.perfumebackend.entity.Perfume;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PerfumeDao {

    public PerfumeDao() {}

    public Perfume save(Perfume perfume) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO perfumes (name, description, price, brand_name, category_name, stock_quantity, image_url) VALUES (?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, perfume.getName());
            preparedStatement.setString(2, perfume.getDescription());
            preparedStatement.setBigDecimal(3, perfume.getPrice());
            preparedStatement.setString(4, perfume.getBrandName());
            preparedStatement.setString(5, perfume.getCategoryName());
            preparedStatement.setInt(6, perfume.getStockQuantity());
            preparedStatement.setString(7, perfume.getImageUrl());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Set the auto-generated ID to the perfume
                        long generatedId = generatedKeys.getLong(1);
                        perfume.setId(generatedId);
                    }
                }
            }

            return perfume;
        } catch (SQLException e) {
            System.out.println("Error saving perfume: " + e.getMessage());
            throw new RuntimeException("Error saving perfume", e);
        }
    }

    public List<Perfume> findAll() {
        List<Perfume> perfumes = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT * FROM perfumes";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()){
                Perfume perfume = mapResultSetToPerfumeList(rs);
                perfumes.add(perfume);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return perfumes;
    }

    public Optional<Perfume> findById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT * FROM perfumes WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Perfume perfume= mapResultSetToPerfumeList(resultSet);
                    return Optional.of(perfume);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "DELETE FROM perfumes WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int deletedRow = preparedStatement.executeUpdate();

            if (deletedRow == 0) {
                throw new ResourceNotFoundException("Perfume not found with id: " + id);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void update(Perfume perfume) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "UPDATE perfumes SET name=?, description=?, price=?, brand_name=?, category_name=?, stock_quantity=?, image_url =? WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, perfume.getName());
            preparedStatement.setString(2, perfume.getDescription());
            preparedStatement.setBigDecimal(3, perfume.getPrice());
            preparedStatement.setString(4, perfume.getBrandName());
            preparedStatement.setString(5, perfume.getCategoryName());
            preparedStatement.setInt(6, perfume.getStockQuantity());
            preparedStatement.setString(7, perfume.getImageUrl());
            preparedStatement.setLong(8, perfume.getId());

            int updatedRow = preparedStatement.executeUpdate();

            if (updatedRow == 0) {
                throw new ResourceNotFoundException("Perfume not found with id: " + perfume.getId());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Perfume mapResultSetToPerfumeList(ResultSet rs) throws SQLException {
        return new Perfume(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getString("brand_name"),
                rs.getString("category_name"),
                rs.getInt("stock_quantity"),
                rs.getString("image_url")
        );
    }
}
