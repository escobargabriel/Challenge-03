package Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import databaseInteracting.DataBaseInteracting;
import databaseInteracting.Product;
import generated.AddProductRequest;
import generated.AddProductResponse;
import generated.AddProductsToShoppingCartRequest;
import generated.AddProductsToShoppingCartResponse;
import generated.CalculateTotalAmountRequest;
import generated.CalculateTotalAmountResponse;
import generated.CreateAShoppingCartRequest;
import generated.CreateAShoppingCartResponse;
import generated.DataChunk;
import generated.DownloadFileRequest;
import generated.ListByIdRequest;
import generated.ListByIdResponse;
import generated.ListProductRequest;
import generated.ListProductResponse;
import generated.ListShoppingCartProductsRequest;
import generated.ListShoppingCartProductsResponse;
import generated.productGrpc;
import io.grpc.stub.StreamObserver;


public class ProductService extends productGrpc.productImplBase {
  DataBaseInteracting dataBaseInteracting;


  public ProductService(String url, String user, String password) throws SQLException {
     dataBaseInteracting = new DataBaseInteracting(url, user, password);
  }

  @Override
  public void listById(ListByIdRequest request, StreamObserver<ListByIdResponse> responseObserver) {
    try {
      int id = request.getId();
      ResultSet resultSet = dataBaseInteracting.getProductsById(id);
      while (resultSet.next()) {
        String name = resultSet.getString(2);
        int stock = resultSet.getInt(3);
        float price = resultSet.getFloat(4);
        ListByIdResponse listByIdResponse = generated.ListByIdResponse.newBuilder()
            .setId(id).setName(name).setStock(stock).setPrice(price).build();
        responseObserver.onNext(listByIdResponse);
        responseObserver.onCompleted();
      }
      resultSet.close();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void addProduct(AddProductRequest request, StreamObserver<AddProductResponse> responseObserver) {
    try {
      String name = request.getName();
      int stock = request.getStock();
      float price = request.getPrice();
      dataBaseInteracting.addProductToDatabase(name, stock, price);
      AddProductResponse addProductResponse =
          generated.AddProductResponse.newBuilder().setName(name).setStock(stock).setPrice(price).build();
      responseObserver.onNext(addProductResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void listProduct(ListProductRequest request, StreamObserver<ListProductResponse> responseObserver) {
    try {
      List<Product> products = dataBaseInteracting.searchForAllProductsOnDatabase();
      for (Product product : products) {
        int id = product.getId();
        String name = product.getName();
        int stock = product.getStock();
        float price = product.getPrice();
        ListProductResponse listProductResponse =
            generated.ListProductResponse.newBuilder().setId(id).setName(name).setStock(stock).setPrice(price).build();
        responseObserver.onNext(listProductResponse);
      }
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void addProductsToShoppingCart(AddProductsToShoppingCartRequest request,
                                        StreamObserver<AddProductsToShoppingCartResponse> responseObserver) {
    try {
      int idShoppingCart = request.getIdShoppingCart();
      int idProduct = request.getIdProduct();
      int quantity = request.getQuantity();
      dataBaseInteracting.insertProductIntoAShoppingCartTable(idShoppingCart, idProduct, quantity);
      AddProductsToShoppingCartResponse addProductsToShoppingCartResponse =
          generated.AddProductsToShoppingCartResponse.newBuilder().setIdShoppingCart(idShoppingCart)
              .setIdProduct(idProduct).setQuantity(quantity).build();
      responseObserver.onNext(addProductsToShoppingCartResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void listShoppingCartProducts(ListShoppingCartProductsRequest request,
                                       StreamObserver<ListShoppingCartProductsResponse> responseObserver) {
    try {
      int cartId = request.getCartId();
      ResultSet resultSet = dataBaseInteracting.searchForAllProductsOnShoppingCart(cartId);
      while (resultSet.next()) {
        int idProduct = resultSet.getInt(1);
        String name = resultSet.getString(2);
        float price = resultSet.getFloat(3);
        int quantity = resultSet.getInt(4);
        ListShoppingCartProductsResponse listShoppingCartProductsResponse =
            generated.ListShoppingCartProductsResponse.newBuilder().setIdProduct(idProduct)
                .setName(name).setPrice(price).setQuantity(quantity).build();
        System.out.println(listShoppingCartProductsResponse.getName());
        responseObserver.onNext(listShoppingCartProductsResponse);
      }
      responseObserver.onCompleted();
      resultSet.close();
    } catch (SQLException e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void calculateTotalAmount(CalculateTotalAmountRequest request,
                                   StreamObserver<CalculateTotalAmountResponse> responseObserver) {
    try {
      int idShoppingCart = request.getIdShoppingCart();
      ResultSet resultSet = dataBaseInteracting.calculateTotalAmount(idShoppingCart);
      while (resultSet.next()) {
        float total =  resultSet.getFloat(1);
        CalculateTotalAmountResponse calculateTotalAmountResponse = generated.CalculateTotalAmountResponse.newBuilder()
                .setTotalAmount(total).build();
        responseObserver.onNext(calculateTotalAmountResponse);
        responseObserver.onCompleted();
      }
      resultSet.close();
    } catch (SQLException e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void createAShoppingCart(CreateAShoppingCartRequest request,
                                  StreamObserver<CreateAShoppingCartResponse> responseObserver) {
    try {
      String name = request.getName();
      dataBaseInteracting.createAShoppingCart(name);
      CreateAShoppingCartResponse createAShoppingCartResponse =
          generated.CreateAShoppingCartResponse.newBuilder().setName(name).build();
      responseObserver.onNext(createAShoppingCartResponse);
      responseObserver.onCompleted();
    } catch (SQLException e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void downloadFile(DownloadFileRequest request, StreamObserver<DataChunk> responseObserver) {
    DataChunk.Builder fileResponse = DataChunk.newBuilder();
    try {
      JSONArray jsonArray = new JSONArray();
      List<Product> products = dataBaseInteracting.searchForAllProductsOnDatabase();
      for (Product product : products) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("price", product.getPrice());
        jsonObject.put("stock", product.getStock());
        jsonObject.put("name", product.getName());
        jsonObject.put("id", product.getId());
        jsonArray.add(jsonObject);
      }
      String bytes = jsonArray.toJSONString();
      fileResponse.setData(bytes);
      fileResponse.setSize(jsonArray.size());
      responseObserver.onNext(fileResponse.build());
      System.out.println("Json file created");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
