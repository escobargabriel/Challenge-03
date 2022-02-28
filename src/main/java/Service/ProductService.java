package Service;

import DatabaseInteracting.DataBaseInteracting;
import DatabaseInteracting.Product;
import generated.addProductRequest;
import generated.addProductResponse;
import generated.addProductsToShoppingCartRequest;
import generated.addProductsToShoppingCartResponse;
import generated.calculateTotalAmountRequest;
import generated.calculateTotalAmountResponse;
import generated.listProductRequest;
import generated.listProductResponse;
import generated.listShoppingCartProductsRequest;
import generated.listShoppingCartProductsResponse;
import io.grpc.stub.StreamObserver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductService extends productGrpc.productImplBase {
  DataBaseInteracting dataBaseInteracting;


  public ProductService(String url, String user, String password) throws SQLException {
     dataBaseInteracting = new DataBaseInteracting(url, user, password);
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
      responseObserver.onError(e);
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
      responseObserver.onError(e);
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
      responseObserver.onError(e);
    }
  }

  @Override
  public void addProductsToShoppingCart(addProductsToShoppingCartRequest request,
                                        StreamObserver<addProductsToShoppingCartResponse> responseObserver) {
    try {
      int idShoppingCart = request.getIdShoppingCart();
      int idProduct = request.getIdProduct();
      int quantity = request.getQuantity();
      dataBaseInteracting.insertProductIntoAShoppingCartTable(idProduct, quantity);
      addProductsToShoppingCartResponse addProductsToShoppingCartResponse =
          generated.addProductsToShoppingCartResponse.newBuilder().setIdShoppingCart(idShoppingCart)
              .setIdProduct(idProduct).setQuantity(quantity).build();
      responseObserver.onNext(addProductsToShoppingCartResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  /*
  @Override
  public void listShoppingCartProducts(listShoppingCartProductsRequest request,
                                       StreamObserver<listShoppingCartProductsResponse> responseObserver) {
    try{
      List<Product> products = dataBaseInteracting.searchForAllProductsOnShoppingCart();
      for(Product product: products) {
        int idShoppingCart = product.getId();
        String idProduct = product.getName();
        float price = product.getPrice();
        listShoppingCartProductsResponse listShoppingCartProductsResponse =
            generated.listShoppingCartProductsResponse.newBuilder().setId(id).setName(name).setPrice(price).build();

      }
    } catch (SQLException e) {
      responseObserver.onError(e);
    }
  }*/

  @Override
  public void calculateTotalAmount(calculateTotalAmountRequest request,
                                   StreamObserver<calculateTotalAmountResponse> responseObserver) {
    try {
      int idShoppingCart = request.getIdShoppingCart();
      ResultSet resultSet = dataBaseInteracting.calculateTotalAmount(idShoppingCart);
      while (resultSet.next()) {
        float total =  resultSet.getFloat(1);
        calculateTotalAmountResponse calculateTotalAmountResponse = generated.calculateTotalAmountResponse.newBuilder().
            setTotalAmount(total).build();
        responseObserver.onNext(calculateTotalAmountResponse);
        responseObserver.onCompleted();
      }
    } catch (SQLException e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createAShoppingCart(createAShoppingCartRequest request,
                                  StreamObserver<createAShoppingCartResponse> responseObserver) {
    try{
      String name = request.getName();
      dataBaseInteracting.createAShoppingCart(name);
      createAShoppingCartResponse createAShoppingCartResponse =
          generated.createAShoppingCartResponse.newBuilder().setName(name).build();
      responseObserver.onNext(createAShoppingCartResponse);
      responseObserver.onCompleted();
    } catch (SQLException e) {
      responseObserver.onError(e);
    }

  }
}
