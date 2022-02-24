package DatabaseInteracting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
  private static String url;
  private static String user;
  private static String password;

  public DataBaseConnection(String url, String user, String password){
    this.url = url;
    this.password = password;
    this.user = user;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }
}
