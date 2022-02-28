package DatabaseInteracting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInteracting {
  Connection connection;

  /**
    * Constructor of DatabaseInteracting class.
   * @param url String - url of the database connection.
   * @param user String - name of user to login on the database.
   * @param password String - password to login on the database.
   * @throws SQLException - Exception of database
   */
  public DataBaseInteracting(String url, String user, String password) throws SQLException {
    DataBaseConnection dataBaseConnection = new DataBaseConnection(url, user, password);
    connection = dataBaseConnection.getConnection();
  }

  /**
   * Method to prepare the query.
   * @return preparedStatement of the query to list all products.
   * @throws SQLException - Exception of database.
   */
  public PreparedStatement createPrepareStatementToProductList() throws SQLException {
    String sql = "SELECT * FROM products";
    return connection.prepareStatement(sql);
  }

  /**
   * Method to add a product on the products table.
   * @param name  String - name of the product.
   * @param stock int - number of products on the stock.
   * @param price float - price of the product.
   * @throws SQLException - Exception of database.
   */
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

  /**
   * Method to get products by the id.
   * @param id int - id of the product.
   * @return a resultSet with the products.
   * @throws SQLException - Exception of database.
   */
  public ResultSet getProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("select * from products where id = ?");
    preparedStatement.setInt(1, id);
    return preparedStatement.executeQuery();
  }

  /**
  * Method to get a resultSet of products table passing a Prepared Statement.
  * @param preparedStatement PreparedStatement -
   *                          prepared statement of the query to list all products.
  * @return resultSet with data of the products table.
  * @throws SQLException - Exception of database.
  */
  public ResultSet createResultSet(PreparedStatement preparedStatement) throws SQLException {
    return preparedStatement.executeQuery();
  }

  /**
  * Method to return all products into a Products table.
  * @return list of products.
  * @throws SQLException Exception of database.
  */
  public List<DatabaseInteracting.Product> searchForAllProductsOnDatabase() throws SQLException {
    List<DatabaseInteracting.Product> list = new ArrayList<>();
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
  /**
   * Method to make a query into the shopping cart table.
   * @return a list of products from shopping cart table.
   * @throws SQLException - Exception of database.
   */

  public ResultSet selectProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("select * from products where id = ?");
    preparedStatement.setInt(1, id);
    return preparedStatement.executeQuery();
  }
}
