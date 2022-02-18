package Service;
import DatabaseInteractin.DataBaseInteracting;
import DatabaseInteractin.ExecuteQuerys;
import DatabaseInteractin.Product;
import generated.addProductRequest;
import generated.addProductResponse;
import generated.listByIdRequest;
import generated.listByIdResponse;
import generated.listProductRequest;
import generated.listProductResponse;
import generated.productGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductService extends productGrpc.productImplBase {
  Scanner sc = new Scanner(System.in);
  ExecuteQuerys executeQuerys = new ExecuteQuerys();
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
      executeQuerys.insertIntoDataBase(name, stock, price);
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
      List<Product> products = executeQuerys.listReturn();
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
}
