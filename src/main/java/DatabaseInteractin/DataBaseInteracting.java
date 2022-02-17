package DatabaseInteractin;
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

  public PreparedStatement createPrepareStatement() throws SQLException {
    String sql = "SELECT * FROM products";
    return connection.prepareStatement(sql);
  }

  public void addProductToDatabase(String n, int s, float p) throws SQLException {
    try {
      String sql = "INSERT INTO products (name, stock, price) VALUES (?,?,?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, n);
      preparedStatement.setInt(2, s);
      preparedStatement.setFloat(3, p);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public ResultSet getProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement = null;
    String sql = null;
    try {
      sql = "select * from products where id =" + id;
      preparedStatement = connection.prepareStatement(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return preparedStatement.executeQuery(sql);
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
    return listProduct;
  }
}
