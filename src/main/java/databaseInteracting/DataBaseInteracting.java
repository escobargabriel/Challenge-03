package databaseInteracting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInteracting {
  Connection connection;

  /**
   * Constructor of databaseInteracting class.
   *
   * @param url      String - url of the database connection.
   * @param user     String - name of user to login on the database.
   * @param password String - password to login on the database.
   * @throws SQLException - Exception of database
   */
  public DataBaseInteracting(String url, String user, String password) throws SQLException {
    DataBaseConnection dataBaseConnection =
        new databaseInteracting.DataBaseConnection(url, user, password);
    connection = dataBaseConnection.getConnection();
  }

  /**
   * Method to prepare the query.
   *
   * @return preparedStatement of the query to list all products.
   * @throws SQLException - Exception of database.
   */
  public PreparedStatement createPrepareStatementToProductList() throws SQLException {
    String sql = "SELECT * FROM products";
    return connection.prepareStatement(sql);
  }

  /**
   * Method to add a product on the products table.
   *
   * @param name  String - name of the product.
   * @param stock int - number of products on the stock.
   * @param price float - price of the product.
   * @throws SQLException - Exception of database.
   */
  public void addProductToDatabase(String name, int stock, float price) throws SQLException {
    String sql = "INSERT INTO products (name, stock, price) VALUES (?,?,?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
      preparedStatement.setString(1, name);
      preparedStatement.setInt(2, stock);
      preparedStatement.setFloat(3, price);
      preparedStatement.executeUpdate();
    }
  }

  /**
   * Method to get products by the id.
   *
   * @param id int - id of the product.
   * @return a resultSet with the products.
   * @throws SQLException - Exception of database.
   */
  public ResultSet getProductsById(int id) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("select * from products where id = ?");
    preparedStatement.setInt(1, id);
    return preparedStatement.executeQuery();
  }

  /**
   * Method to get a resultSet of products table passing a Prepared Statement.
   *
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
   *
   * @return list of products.
   * @throws SQLException Exception of database.
   */
  public List<databaseInteracting.Product> searchForAllProductsOnDatabase() throws SQLException {
    List<databaseInteracting.Product> list = new ArrayList<>();
    try (PreparedStatement preparedStatement = createPrepareStatementToProductList();
         ResultSet aResultSet = createResultSet(preparedStatement)) {
      prepareAndExecuteMethod(aResultSet, list);
    }
    return list;
  }
  /*
  /**
   * Method to make a query into the shopping cart table.
   * @return a list of products from shopping cart table.
   * @throws SQLException - Exception of database.
  */
  public ResultSet searchForAllProductsOnShoppingCart(int cartId) throws SQLException {
    String sql = "SELECT p.id, p.name, p.price, l.quantity FROM products p," +
        " shoppinglist l, shoppingcart c where p.id = l.idproduct AND l.idcart = ?;";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1,cartId);
      return preparedStatement.executeQuery();
  }

  /**
   * Method to calculate the total amount of the sale.
   *
   * @param shoppingCartId int - id of the shopping cart.
   * @return result set with a float value of total amount of sale.
   * @throws SQLException - Exception related of database.
   */
  public ResultSet calculateTotalAmount(int shoppingCartId) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT SUM (p.price * l.quantity) AS totalSum FROM  products p," +
            " shoppinglist l, shoppingcart c WHERE l.idProduct = p.id AND c.idShoppingCart = ?;");
    preparedStatement.setInt(1, shoppingCartId);
    return preparedStatement.executeQuery();
  }

  /**
   * Method to insert a product into a shopping cart.
   *
   * @param idProduct int - id of product selected by the client.
   * @param quantity  int - quantity of the product selected by the client.
   * @throws SQLException - Exception to database.
   */
  public void insertProductIntoAShoppingCartTable(int idCart, int idProduct, int quantity) throws SQLException {
    String sql = "INSERT INTO shoppinglist (idcart, idproduct, quantity) values (?, ?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setInt(1, idCart);
    preparedStatement.setInt(2, idProduct);
    preparedStatement.setInt(3, quantity);
    preparedStatement.executeUpdate();
    preparedStatement.close();
  }

  /**
   * Method to create a shopping cart.
   * @param name String - client name.
   * @throws SQLException - exception related to database.
   */
  public void createAShoppingCart(String name) throws SQLException {
    String sql = "INSERT INTO shoppingCart(clientName) values(?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
      preparedStatement.setString(1, name);
      preparedStatement.executeUpdate();
    }
  }

  /**
   * Method to populate the list using a prepared statement.
   *
   * @param resultSet ResultSet - resultSet of the query.
   * @param list      List - List with data of the resultSet.
   * @throws SQLException - Exception of database.
   */
  private void prepareAndExecuteMethod(ResultSet resultSet,
                                       List<Product> list) throws SQLException {
    while (resultSet.next()) {
      Product product = new Product();
      product.setId(resultSet.getInt("id"));
      product.setName(resultSet.getString("name"));
      product.setStock(resultSet.getInt("stock"));
      product.setPrice(resultSet.getFloat("price"));
      list.add(product);
    }
  }
}
