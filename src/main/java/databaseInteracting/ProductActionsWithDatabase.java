package databaseInteracting;

import generated.AddProductRequest;
import generated.AddProductResponse;
import generated.ListByIdRequest;
import generated.ListByIdResponse;
import generated.ListProductRequest;
import generated.ListProductResponse;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ProductActionsWithDatabase implements ProductActions {
  Scanner scanner = new Scanner(System.in);
  productGrpc.productBlockingStub prodStub;
  ManagedChannel channel;

  public ProductActionsWithDatabase(ManagedChannel channel) {
    this.channel = channel;
    prodStub = productGrpc.newBlockingStub(channel);
  }

  @Override
  public void listProduct() {
    try {
      ListProductRequest request = ListProductRequest.newBuilder().build();
      Iterator<ListProductResponse> listProductResponseIterator = prodStub.listProduct(request);
      while (listProductResponseIterator.hasNext()) {
        ListProductResponse next = listProductResponseIterator.next();
        System.out.printf("Id: %d Name: %s Stock: %d Price: %.2f\n", next.getId(), next.getName(), next.getStock(),
            next.getPrice());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addProductToDatabase() {
    System.out.println("Type the product name");
    String name = scanner.next();
    System.out.println("Type the number of product inserted on stock");
    int stock = scanner.nextInt();
    System.out.println("Type the price of the product");
    float price = scanner.nextFloat();
    AddProductRequest request = AddProductRequest.newBuilder().setName(name).setStock(stock).setPrice(price).build();
    AddProductResponse response = prodStub.addProduct(request);
    System.out.println(response.getName());
  }

  @Override
  public void listProductById() {
    System.out.println("Type product id: ");
    int id = scanner.nextInt();
    ListByIdRequest request = ListByIdRequest.newBuilder().setId(id).build();
    ListByIdResponse response = prodStub.listById(request);
    System.out.println(response);
  }
}
