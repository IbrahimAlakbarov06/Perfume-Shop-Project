package org.perfume.perfumebackend.dao;

import org.perfume.perfumebackend.config.DatabaseConnection;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BrandDao {

    public Brand save(Brand brand) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO brands (name, description, country, founded_year, logo_url) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, brand.getName());
            preparedStatement.setString(2, brand.getDescription());
            preparedStatement.setString(3, brand.getCountry());
            preparedStatement.setString(4, brand.getFoundedYear());
            preparedStatement.setString(5, brand.getLogoUrl());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long generatedId = generatedKeys.getLong(1);
                        brand.setId(generatedId);
                    }
                }
            }

            return brand;
        } catch (SQLException e) {
            System.out.println("Error saving brand: " + e.getMessage());
            throw new RuntimeException("Error saving brand", e);
        }
    }

    public List<Brand> findAll() {
        List<Brand> brands = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM brands";
            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                Brand brand = mapResultSetToBrand(rs);
                brands.add(brand);
            }

        } catch (SQLException e) {
            System.out.println("Error finding brands: " + e.getMessage());
        }
        return brands;
    }

    public Optional<Brand> findById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM brands WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Brand brand = mapResultSetToBrand(resultSet);
                    return Optional.of(brand);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding brand: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void update(Brand brand) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE brands SET name=?, description=?, country=?, founded_year=?, logo_url=? WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, brand.getName());
            preparedStatement.setString(2, brand.getDescription());
            preparedStatement.setString(3, brand.getCountry());
            preparedStatement.setString(4, brand.getFoundedYear());
            preparedStatement.setString(5, brand.getLogoUrl());
            preparedStatement.setLong(6, brand.getId());

            int updatedRow = preparedStatement.executeUpdate();

            if (updatedRow == 0) {
                throw new ResourceNotFoundException("Brand not found with id: " + brand.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating brand: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM brands WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int deletedRow = preparedStatement.executeUpdate();

            if (deletedRow == 0) {
                throw new ResourceNotFoundException("Brand not found with id: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting brand: " + e.getMessage());
        }
    }

    private Brand mapResultSetToBrand(ResultSet rs) throws SQLException {
        return new Brand(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("country"),
                rs.getString("founded_year"),
                rs.getString("logo_url")
        );
    }
}