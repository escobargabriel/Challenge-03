package databaseInteracting;

import generated.AddProductRequest;
import generated.AddProductResponse;
import generated.AddProductsToShoppingCartRequest;
import generated.AddProductsToShoppingCartResponse;
import generated.CalculateTotalAmountRequest;
import generated.CalculateTotalAmountResponse;
import generated.CreateAShoppingCartRequest;
import generated.CreateAShoppingCartResponse;
import generated.ListByIdRequest;
import generated.ListByIdResponse;
import generated.ListProductRequest;
import generated.ListProductResponse;
import generated.ListShoppingCartProductsRequest;
import generated.ListShoppingCartProductsResponse;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.Scanner;

public class Client {
  /**
   * Main Method of class.
   * Class with a menu to interact with the database and the server side.
   *
   * @param args Array of String to pass parameters.
   */
  public static void main(String[] args) {
    String channelName = args[0];
    int portNumber = Integer.parseInt(args[1]);
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(channelName, portNumber).usePlaintext().build();
    productGrpc.productBlockingStub prodStub = productGrpc.newBlockingStub(channel);
    Scanner scanner = new Scanner(System.in);
    int option;
    int aux;
    do {
      System.out.println("Select one option:");
      System.out.println("[1] - List all products");
      System.out.println("[2] - Search a product");
      System.out.println("[3] - Add a product");
      System.out.println("[4] - Create a Shopping Cart");
      System.out.println("[5] - Add a product to Shopping Cart");
      System.out.println("[6] - Show the Shopping Cart");
      System.out.println("[7] - Calculate the total purchase amount.");
      System.out.println("[8] - Import products from a file.");
      System.out.println("[9] - Export products to a file.");
      System.out.println("[10] - Finish execution");
      option = scanner.nextInt();
      if (option == 1) {
        try {
          ListProductRequest request = ListProductRequest.newBuilder().build();
          Iterator<ListProductResponse> listProductResponseIterator = prodStub.listProduct(request);
          while (listProductResponseIterator.hasNext()) {
            ListProductResponse next = listProductResponseIterator.next();
            System.out.printf("Id: %d Name: %s Stock: %d Price: %.2f\n",
                next.getId(), next.getName(), next.getStock(), next.getPrice());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (option == 2) {
        System.out.println("Type product id: ");
        int id = scanner.nextInt();
        ListByIdRequest request = ListByIdRequest.newBuilder().setId(id).build();
        ListByIdResponse response = prodStub.listById(request);
        System.out.println(response);
      }
      if (option == 3) {
        scanner.nextLine();
        System.out.println("Type the product name");
        String name = scanner.nextLine();
        System.out.println("Type the number of product inserted on stock");
        int stock = scanner.nextInt();
        System.out.println("Type the price of the product");
        float price = scanner.nextFloat();
        AddProductRequest request =
            AddProductRequest.newBuilder().setName(name).setStock(stock).setPrice(price).build();
        AddProductResponse response = prodStub.addProduct(request);
        System.out.println(response.getName());
      }
      if (option == 4) {
        scanner.nextLine();
        System.out.println("Type the client name");
        String name = scanner.nextLine();
        CreateAShoppingCartRequest createAShoppingCartRequest =
            generated.CreateAShoppingCartRequest.newBuilder().setName(name).build();
        CreateAShoppingCartResponse response = prodStub.createAShoppingCart(createAShoppingCartRequest);
        System.out.println("Cart id: " + response.getId() + "Owner Name: " + response.getName());
      }

      if (option == 5) {
        System.out.println("Type the id of shopping cart");
        int idCart = scanner.nextInt();
        System.out.println("Type the id of desired product:");
        int idProduct = scanner.nextInt();
        System.out.println("Type the quantity of this product:");
        int quantity = scanner.nextInt();
        AddProductsToShoppingCartRequest request =
            AddProductsToShoppingCartRequest.newBuilder().setIdShoppingCart(idCart).setIdProduct(idProduct)
                .setQuantity(quantity).build();
        AddProductsToShoppingCartResponse response = prodStub.addProductsToShoppingCart(request);
        System.out.println("Shopping Cart Id: " + response.getIdShoppingCart() + " Product ID: "
            + response.getIdProduct() + " Quantity: " + response.getQuantity());
      }
      if (option == 6) {
        System.out.println("Type your Shopping Cart ID: ");
        int id = scanner.nextInt();
        System.out.println("Your Shopping Cart has the products below:");
         try {
          ListShoppingCartProductsRequest request =
              ListShoppingCartProductsRequest.newBuilder().setCartId(id).build();
          Iterator<ListShoppingCartProductsResponse> listShoppingCartProductsResponseIterator =
              prodStub.listShoppingCartProducts(request);
          while (listShoppingCartProductsResponseIterator.hasNext()) {
            ListShoppingCartProductsResponse next = listShoppingCartProductsResponseIterator.next();
            System.out.printf("Product Id: %d Product Name: %s  Price: %.2f Quantity: %d \n",
                next.getIdProduct(), next.getName(), next.getPrice(), next.getQuantity());
            }
          } catch (Exception e) {
          e.printStackTrace();
          }
      }
      if (option == 7) {
        System.out.println("Type the id of Shopping Cart that you desire to calculate the amount");
        int shoppingCartId = scanner.nextInt();
        CalculateTotalAmountRequest calculateTotalAmountRequest =
            generated.CalculateTotalAmountRequest.newBuilder()
                .setIdShoppingCart(shoppingCartId).build();
        CalculateTotalAmountResponse calculateTotalAmountResponse =
            prodStub.calculateTotalAmount(calculateTotalAmountRequest);
        float total = calculateTotalAmountResponse.getTotalAmount();
        System.out.println("Total Amount of sale: R$" + total);
      }
      if (option == 8) {
        System.out.println("Importing products from a file");
        System.out.println("Under construction");
      }
      if (option == 9) {
        System.out.println("Exporting of execution to a file");
        System.out.println("Under construction");
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
    scanner.close();
  }
}
