package Service;

import DatabaseInteracting.DataBaseInteracting;
import DatabaseInteracting.Product;
import generated.addProductRequest;
import generated.addProductResponse;
import generated.addShoppingCartRequest;
import generated.addShoppingCartResponse;
import generated.listByIdRequest;
import generated.listByIdResponse;
import generated.listProductRequest;
import generated.listProductResponse;
import generated.productGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductService extends productGrpc.productImplBase {

  DataBaseInteracting dataBaseInteracting = new DataBaseInteracting();

  public ProductService() throws SQLException {
  }

  @Override
  public void listById(listByIdRequest request, StreamObserver<listByIdResponse> responseObserver) {
    try {
      int id = request.getId();
      ResultSet resultSet = dataBaseInteracting.getProductsById(id);
      while (resultSet.next()) {
        String name = resultSet.getString(2);
        int stock = resultSet.getInt(3);
        float price = resultSet.getFloat(4);
        listByIdResponse listByIdResponse = generated.listByIdResponse.newBuilder().
            setId(id).setName(name).setStock(stock).setPrice(price).build();
        responseObserver.onNext(listByIdResponse);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addProduct(addProductRequest request, StreamObserver<addProductResponse> responseObserver) {
    try {
      String name = request.getName();
      int stock = request.getStock();
      float price = request.getPrice();
      dataBaseInteracting.addProductToDatabase(name, stock, price);
      addProductResponse addProductResponse =
          generated.addProductResponse.newBuilder().setName(name).setStock(stock).setPrice(price).build();
      responseObserver.onNext(addProductResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void listProduct(listProductRequest request, StreamObserver<listProductResponse> responseObserver) {
    try {
      List<Product> products = dataBaseInteracting.searchForAll();
      for (Product product : products) {
        int id = product.getId();
        String name = product.getName();
        int stock = product.getStock();
        float price = product.getPrice();
        listProductResponse listProductResponse =
            generated.listProductResponse.newBuilder().setId(id).setName(name).setStock(stock).setPrice(price).build();
        responseObserver.onNext(listProductResponse);
      }
      responseObserver.onCompleted();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addShoppingCart(addShoppingCartRequest request,
                              StreamObserver<addShoppingCartResponse> responseObserver) {
    try {
      int id = request.getId();
      ResultSet resultSet = dataBaseInteracting.selectProductsById(id);
      while (resultSet.next()) {
        String name = resultSet.getString(2);
        int stock = resultSet.getInt(3);
        float price = resultSet.getFloat(4);
        addShoppingCartResponse
            addShoppingCartResponse =
            generated.addShoppingCartResponse.newBuilder().setId(id).setName(name).setStock(stock).setPrice(price)
                .build();
        responseObserver.onNext(addShoppingCartResponse);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
