package databaseInteracting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
  private static String url;
  private static String user;
  private static String password;

  /**
   * Method constructor of the class to receive the parameters of database connection.
   * @param url String - URL of database.
   * @param user String - User name of database.
   * @param password String - Password of database.
   */
  public DataBaseConnection(String url, String user, String password) {
    this.url = url;
    this.password = password;
    this.user = user;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }
}
