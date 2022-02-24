package DatabaseInteracting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInteracting {
  Connection connection;

  public DataBaseInteracting(String url, String user, String password) throws SQLException {
    DataBaseConnection dataBaseConnection = new DataBaseConnection(url, user, password);
    connection = dataBaseConnection.getConnection();
  }

  public PreparedStatement createPrepareStatementToProductList() throws SQLException {
    String sql = "SELECT * FROM products";
    return connection.prepareStatement(sql);
  }

  public void addProductToDatabase(String name, int stock, float price) throws SQLException {
    try {
      String sql = "INSERT INTO products (name, stock, price) VALUES (?,?,?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, stock);
      preparedStatement.setFloat(3, price);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ResultSet getProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("select * from products where id = ?");
    preparedStatement.setInt(1, id);
    return preparedStatement.executeQuery();
  }

  public ResultSet createResultSet(PreparedStatement preparedStatement) throws SQLException {
    return preparedStatement.executeQuery();
  }

  public List<Product> searchForAll() throws SQLException {
    List<Product> list = new ArrayList<>();
    try (PreparedStatement preparedStatement = createPrepareStatementToProductList();
         ResultSet aResultSet = createResultSet(preparedStatement);) {
      while (aResultSet.next()) {
        Product product = new Product();
        product.setId(aResultSet.getInt("id"));
        product.setName(aResultSet.getString("name"));
        product.setStock(aResultSet.getInt("stock"));
        product.setPrice(aResultSet.getFloat("price"));
        list.add(product);
      }
    }
    return list;
  }

  public ResultSet selectProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("select * from products where id = ?");
    preparedStatement.setInt(1, id);
    return preparedStatement.executeQuery();
  }
}
