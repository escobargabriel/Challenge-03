package databaseInteracting;

import generated.AddProductsToShoppingCartRequest;
import generated.AddProductsToShoppingCartResponse;
import generated.CalculateTotalAmountRequest;
import generated.CalculateTotalAmountResponse;
import generated.CreateAShoppingCartRequest;
import generated.CreateAShoppingCartResponse;
import generated.ListShoppingCartProductsRequest;
import generated.ListShoppingCartProductsResponse;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ShoppingCartActionsWithDatabase implements ShoppingCartActions {
  Scanner scanner = new Scanner(System.in);
  productGrpc.productBlockingStub prodStub;
  ManagedChannel channel;

  public ShoppingCartActionsWithDatabase(ManagedChannel channel) {
    this.channel = channel;
    prodStub = productGrpc.newBlockingStub(channel);
  }

  @Override
  public void createAShoppingCart() {
    System.out.println("Type the client name");
    String name = scanner.nextLine();
    CreateAShoppingCartRequest createAShoppingCartRequest =
        generated.CreateAShoppingCartRequest.newBuilder().setName(name).build();
    CreateAShoppingCartResponse response = prodStub.createAShoppingCart(createAShoppingCartRequest);
    System.out.println("Cart id: " + response.getId() + "Owner Name: " + response.getName());
  }

  @Override
  public void addProductToShoppingCart() {
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
    System.out.println("Shopping Cart Id: " + response.getIdShoppingCart() + " Product ID: " + response.getIdProduct() +
        " Quantity: " + response.getQuantity());
  }

  @Override
  public void showShoppingCart() {
    System.out.println("Type your Shopping Cart ID: ");
    int id = scanner.nextInt();
    System.out.println("Your Shopping Cart has the products below:");
    try {
      ListShoppingCartProductsRequest request = ListShoppingCartProductsRequest.newBuilder().setCartId(id).build();
      Iterator<ListShoppingCartProductsResponse> listShoppingCartProductsResponseIterator =
          prodStub.listShoppingCartProducts(request);
      while (listShoppingCartProductsResponseIterator.hasNext()) {
        ListShoppingCartProductsResponse next = listShoppingCartProductsResponseIterator.next();
        System.out.printf("Product Id: %d Product Name: %s  Price: %.2f Quantity: %d \n", next.getIdProduct(),
            next.getName(), next.getPrice(), next.getQuantity());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void calculateTotal() {
    System.out.println("Type the id of Shopping Cart that you desire to calculate the amount");
    int shoppingCartId = scanner.nextInt();
    CalculateTotalAmountRequest calculateTotalAmountRequest =
        generated.CalculateTotalAmountRequest.newBuilder().setIdShoppingCart(shoppingCartId).build();
    CalculateTotalAmountResponse calculateTotalAmountResponse =
        prodStub.calculateTotalAmount(calculateTotalAmountRequest);
    float total = calculateTotalAmountResponse.getTotalAmount();
    System.out.println("Total Amount of sale: R$" + total);
  }
}
