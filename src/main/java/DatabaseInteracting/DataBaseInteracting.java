package DatabaseInteracting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInteracting {
  private final Connection connection = DataBaseConnection.getConnection();

  public DataBaseInteracting() throws SQLException {
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
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
      String sql = "select * from products where id =";
      preparedStatement.setString(1, sql);
      preparedStatement.setInt(2, id);
      preparedStatement = connection.prepareStatement(String.valueOf(preparedStatement));
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

  public ResultSet createResultSet(PreparedStatement preparedStatement) throws SQLException {
    return preparedStatement.executeQuery();
  }

  public List<Product> searchForAll(ResultSet resultSet) throws SQLException {
    List<Product> listProduct = new ArrayList<Product>();
    while (resultSet.next()) {
      Product product = new Product();
      product.setId(resultSet.getInt("id"));
      product.setName(resultSet.getString("name"));
      product.setStock(resultSet.getInt("stock"));
      product.setPrice(resultSet.getFloat("price"));
      listProduct.add(product);
    }
    return list;
  }

  public ResultSet selectProductsById(int id) throws SQLException {
    ResultSet resultSet = null;
    try {
      String sql = "select * from products where id =" + id;
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

}
