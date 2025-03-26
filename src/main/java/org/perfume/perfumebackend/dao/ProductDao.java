package org.perfume.perfumebackend.dao;

import org.perfume.perfumebackend.config.DatabaseConnection;
import org.perfume.perfumebackend.entity.Product;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    public ProductDao() {}

    public Product save(Product product) {
        try(Connection connection= DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO product(name, description, price, brand_name,category_name,stock_quantity,image_url) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setString(4, product.getBrandName());
            preparedStatement.setString(5, product.getCategoryName());
            preparedStatement.setInt(6, product.getStockQuantity());
            preparedStatement.setString(7, product.getImageUrl());

            int row = preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                } else
                    throw new ResourceNotFoundException("Team not found");
            }

            if (row == 0){
                throw new ResourceNotFoundException("Insert product failed");
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return product;
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT * FROM product";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()){
                Product product = mapResultSetToProductList(rs);
                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public Optional<Product> findById(Long id) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT * FROM product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product= mapResultSetToProductList(resultSet);
                    return Optional.of(product);
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
            String sql = "DELETE FROM product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int deletedRow = preparedStatement.executeUpdate();

            if (deletedRow == 0) {
                throw new ResourceNotFoundException("Product not found with id: " + id);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void update(Product product) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "UPDATE product SET name=?, description=?, price=?, brand_name=?, category_name=?, stock_quantity=?, image_url =? WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setString(4, product.getBrandName());
            preparedStatement.setString(5, product.getCategoryName());
            preparedStatement.setInt(6, product.getStockQuantity());
            preparedStatement.setString(7, product.getImageUrl());
            preparedStatement.setLong(8, product.getId());

            int updatedRow = preparedStatement.executeUpdate();

            if (updatedRow == 0) {
                throw new ResourceNotFoundException("Product not found with id: " + product.getId());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Product mapResultSetToProductList(ResultSet rs) throws SQLException {
        return new Product(
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
