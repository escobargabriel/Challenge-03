package databaseInteracting;

import generated.productGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Client {
  /**
   * Method to show the menu on the execution.
   *
   * @param scanner scanner value to select the option.
   * @return int option.
   */
  public static int printMenu(Scanner scanner) {
    System.out.println("Select one option:");
    System.out.println("[1] - List all products");
    System.out.println("[2] - Search a product");
    System.out.println("[3] - Add a product");
    System.out.println("[4] - Create a Shopping Cart");
    System.out.println("[5] - Add a product to Shopping Cart");
    System.out.println("[6] - Show the Shopping Cart");
    System.out.println("[7] - Calculate the total purchase amount.");
    System.out.println("[8] - Export products to a file.");
    System.out.println("[9] - Import products from a file.");
    System.out.println("[10] - Finish execution.");
    return scanner.nextInt();
  }

  /**
   * Main Method of class.
   * Class with a menu to interact with the database and the server side.
   *
   * @param args Array of String to pass parameters.
   */
  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
    String channelName = args[0];
    int portNumber = Integer.parseInt(args[1]);
    String jsonImportFileName = args[2];
    String jsonExportFileName = args[3];
    String xmlImportFileName = args[5];
    String xmlExportFileName = args[4];
    ManagedChannel channel = ManagedChannelBuilder.forAddress(channelName, portNumber).usePlaintext().build();
    productGrpc.productBlockingStub prodStub = productGrpc.newBlockingStub(channel);
    int option;
    int aux;
    FileAccess fileAccessJsonFile = new FileAccessJsonFile(channel);
    FileAccess fileAccessXmlFile = new FileAccessXmlFile(channel);
    ProductActions productActions = new ProductActionsWithDatabase(channel);
    ShoppingCartActions shoppingCartActions = new ShoppingCartActionsWithDatabase(channel);
    Scanner scanner = new Scanner(System.in);
    do {
      option = printMenu(scanner);
      if (option == 1) {
        productActions.listProduct();
      }
      if (option == 2) {
        productActions.listProductById();
      }
      if (option == 3) {
        productActions.addProductToDatabase();
      }
      if (option == 4) {
        shoppingCartActions.createAShoppingCart();
      }
      if (option == 5) {
        shoppingCartActions.addProductToShoppingCart();
      }
      if (option == 6) {
        shoppingCartActions.showShoppingCart();
      }
      if (option == 7) {
        shoppingCartActions.calculateTotal();
      }
      if (option == 8) {
        System.out.println("Exporting products from a file");
        int exportOption = 0;
        do {
          System.out.println("Select the type of file you desire:");
          System.out.println("[1] - Json file. ");
          System.out.println("[2] - XML  file. ");
          exportOption = scanner.nextInt();
          if (exportOption == 1) {
            System.out.println("Exporting data to a json file");
            fileAccessJsonFile.exportFile(jsonExportFileName);
          } else if (exportOption == 2) {
            System.out.println("Exporting data to a xml file");
            fileAccessXmlFile.exportFile(xmlExportFileName);
          } else {
            System.out.println("Invalid option");
          }
        } while (exportOption < 1 || exportOption > 2);
      }
      if (option == 9) {
        System.out.println("Import products from a file");
        int importOption = 0;
        do {
          System.out.println("Select the type of file you desire:");
          System.out.println("[1] - Json file. ");
          System.out.println("[2] - XML file. ");
          importOption = scanner.nextInt();
          if (importOption == 1) {
            System.out.println("Importing data from a json file...");
            fileAccessJsonFile.importFile(jsonImportFileName);
          } else if (importOption == 2) {
            System.out.println("Importing data from a xml file...");
            fileAccessXmlFile.importFile(xmlImportFileName);
          } else {
            System.out.println("Invalid option");
          }
        } while (importOption < 1 || importOption > 2);
      }
      if (option == 10) {
        System.out.println("End of execution");
      }
      if (option < 1 || option > 10) {
        System.out.println("Invalid option!");
      }
      do {
        System.out.println("Do you want to run the application again? \n1 - Yes - 2 - No");
        aux = scanner.nextInt();
        if (aux == 2) {
          System.out.println("End of execution.\nBye!");
        }
      } while (aux < 1 || aux > 2);
    } while (aux == 1);
  }
}
