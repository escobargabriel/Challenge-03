package DatabaseInteractin;

import generated.addProductRequest;
import generated.addProductResponse;
import generated.addShoppingCartRequest;
import generated.addShoppingCartResponse;
import generated.listByIdRequest;
import generated.listByIdResponse;
import generated.listProductRequest;
import generated.listProductResponse;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws SQLException {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
    productGrpc.productBlockingStub prodStub = productGrpc.newBlockingStub(channel);
    Scanner sc = new Scanner(System.in);
    int option = 0;
    int aux = 0;
    do {
      System.out.println("Select one option:");
      System.out.println("[1] - List all products");
      System.out.println("[2] - Search a product");
      System.out.println("[3] - Add a product");
      System.out.println("[4] - Add a Shopping Cart");
      System.out.println("[5] - Finish execution");
      option = sc.nextInt();
      if (option == 1) {
        try {
          listProductRequest request = listProductRequest.newBuilder().build();
          Iterator<listProductResponse> listProductResponseIterator = prodStub.listProduct(request);
          while (listProductResponseIterator.hasNext()) {
            listProductResponse next = listProductResponseIterator.next();
            System.out.printf("Id: %d Name: %s Stock: %d Price: %.2f\n", next.getId(), next.getName(), next.getStock(),
                next.getPrice());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (option == 2) {
        System.out.println("Type product id: ");
        int id = sc.nextInt();
        listByIdRequest request = listByIdRequest.newBuilder().setId(id).build();
        listByIdResponse response = prodStub.listById(request);
        System.out.println(response);
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
        addProductRequest request =
            addProductRequest.newBuilder().setName(name).setStock(stock).setPrice(price).build();
        addProductResponse response = prodStub.addProduct(request);
        System.out.println(response.getName());
      }
      if (option == 4) {
        System.out.println("Type product id: ");
        int id = sc.nextInt();
        addShoppingCartRequest request = addShoppingCartRequest.newBuilder().setId(id).build();
        addShoppingCartResponse response = prodStub.addShoppingCart(request);
        System.out.println(response);
        System.out.println("Select how many products you desire");
        System.out.println("Attention! Don't select a number above that available.");
        int quantity = sc.nextInt();
        if (quantity < 1) {
          System.out.println("You need to select a quantity above 0!!!");
        }
      }
      if (option == 5) {
        System.out.println("End of execution");
      }
      if (option < 1 || option > 5) {
        System.out.println("Invalid option!");
      }
      do {
        System.out.println("Do you want to run the application again? \n1 - Yes - 2 - No");
        aux = sc.nextInt();
        if (aux == 2) {
          System.out.println("End of execution.\nBye!");
        }
      } while (aux < 1 || aux > 2);
    } while (aux == 1);
    sc.close();
  }
}
