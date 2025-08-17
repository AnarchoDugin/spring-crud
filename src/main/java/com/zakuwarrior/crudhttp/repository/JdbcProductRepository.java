package com.zakuwarrior.crudhttp.repository;

import com.zakuwarrior.crudhttp.exception.DatabaseException;
import com.zakuwarrior.crudhttp.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    public JdbcProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String errorMessage = "Failed to fetch all products";
        String sql = "SELECT * FROM products";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                products.add(mapRowToProduct(resultSet));
            }
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new DatabaseException(errorMessage, e);
        }
        return products;
    }

    @Override
    public List<Product> findByPage(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String errorMessage = "Failed to fetch products by page";
        String sql = "SELECT * FROM products ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRowToProduct(resultSet));
                }
            } catch (SQLException e) {
                log.error(errorMessage, e);
                throw new DatabaseException(errorMessage, e);
            }
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new DatabaseException(errorMessage, e);
        }
        return products;
    }

    @Override
    public long countAll() {
        String errorMessage = "Failed to count products";
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new DatabaseException(errorMessage, e);
        }
        return 0;
    }

    @Override
    public Optional<Product> findById(long id) {
        String errorMessage = "Failed to fetch a product";
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToProduct(resultSet));
                }
            } catch (SQLException e) {
                log.error("Failed to fetch a product with id: {}", id, e);
                throw new DatabaseException(errorMessage, e);
            }
        } catch (SQLException e) {
            log.error("Failed to fetch a product with id: {}", id, e);
            throw new DatabaseException(errorMessage, e);
        }
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            return insert(product);
        }
        return update(product);
    }

    @Override
    public void deleteById(long id) {
        String errorMessage = "Failed to delete a product";
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("no rows affected after updating a table");
            }
        } catch (SQLException e) {
            log.error("Failed to delete a product with id: {}", id, e);
            throw new DatabaseException(errorMessage, e);
        }
    }

    private Product insert(Product product) {
        String errorMessage = "Failed to insert a product";
        String sql = "INSERT INTO products (name, description, price, quantity, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.setString(5, product.getCategory());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("no rows affected after updating a table");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                    return product;
                }
                throw new SQLException("no keys returned after updating a table");
            } catch (SQLException e) {
                log.error("Failed to insert a product: {}", product, e);
                throw new DatabaseException(errorMessage, e);
            }

        } catch (SQLException e) {
            log.error("Failed to insert a product: {}", product, e);
            throw new DatabaseException(errorMessage, e);
        }
    }

    private Product update(Product product) {
        String errorMessage = "Failed to update a product";
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ?, category = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.setString(5, product.getCategory());
            preparedStatement.setLong(6, product.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("no rows affected after updating a table");
            }
        } catch (SQLException e) {
            log.error("Failed to update a product: {}", product, e);
            throw new DatabaseException(errorMessage, e);
        }
        return product;
    }

    private Product mapRowToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        product.setCategory(resultSet.getString("category"));

        return product;
    }
}
