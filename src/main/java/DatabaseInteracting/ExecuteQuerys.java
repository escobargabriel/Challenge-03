package DatabaseInteracting;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ExecuteQuerys {
  DataBaseInteracting dataBaseInteracting = new DataBaseInteracting();
  Scanner sc = new Scanner(System.in);

  public ExecuteQuerys() throws SQLException {
  }

  public List<Product> listReturn() throws SQLException {
    List<Product> list;
    try (PreparedStatement preparedStatement = dataBaseInteracting.createPrepareStatementToProductList();
         ResultSet aResultSet = dataBaseInteracting.createResultSet(preparedStatement);) {
      list = dataBaseInteracting.searchForAll(aResultSet);
    }
    return list;
  }

  public void insertIntoDataBase(String name, int stock, float price) {
    try{
    dataBaseInteracting.addProductToDatabase(name, stock, price);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ResultSet getById(int id){
    ResultSet resultSet = null;
    try{
      resultSet = dataBaseInteracting.getProductsById(id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

}
