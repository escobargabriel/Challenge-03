package DatabaseInteractin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainClass {
  public static void main(String[] args) throws SQLException {
    Scanner sc = new Scanner(System.in);
    int option = 0;
    int response = 0;
    DataBaseInteracting dataBaseInteracting = new DataBaseInteracting();
    do {
      System.out.println("Select one option:");
      System.out.println("[1] - List all products");
      System.out.println("[2] - Search a product");
      System.out.println("[3] - add a product");
      System.out.println("[4] - Finish execution");
      option = sc.nextInt();
      if (option == 1) {
        try (PreparedStatement preparedStatement = dataBaseInteracting.createPrepareStatement();
             ResultSet aResultSet = dataBaseInteracting.createResultSet(preparedStatement);) {
          List<Product> list = dataBaseInteracting.searchForAll(aResultSet);
          for (Product product : list) {
            System.out.println(product);
          }
        }
      }
      if (option == 2) {
        int id = 0;
        try (PreparedStatement preparedStatement = dataBaseInteracting.createPrepareStatement();
             ResultSet aResultSet = dataBaseInteracting.createResultSet(preparedStatement);) {
          List<Product> list = dataBaseInteracting.searchForAll(aResultSet);
          System.out.println("Type product id: ");
          id = sc.nextInt();
          for (Product product : list) {
            if (product.getId() == id) {
              System.out.println(product);
            }
          }
        }
      }
      if (option == 3) {
        String name;
        int stock;
        float price;
        sc.nextLine();
        System.out.println("Type the product name");
        name = sc.nextLine();
        System.out.println("Type the number of product inserted on stock");
        stock = sc.nextInt();
        System.out.println("Type the price of the product");
        price = sc.nextFloat();
        try{
          dataBaseInteracting.addProductToDatabase(name, stock, price);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }

      if (option == 4) {
        System.out.println("End of execution");
      }
      if (option < 1 || option > 4) {
        System.out.println("Invalid option!");
      }
      do {
        System.out.println("Do you want to run the application again? \n1 - Yes - 2 - No");
        response = sc.nextInt();
        if(response == 2) {
          System.out.println("End of execution.\nBye!");
        }
      }while (response < 1 || response > 2);
    }while(response == 1);
    sc.close();
  }
}
