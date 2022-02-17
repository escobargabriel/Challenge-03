package Service;
import DatabaseInteractin.ExecuteQuerys;
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
import java.util.Scanner;

public class ProductService extends productGrpc.productImplBase {
  Scanner sc = new Scanner(System.in);
  ExecuteQuerys executeQuerys = new ExecuteQuerys();


  public ProductService() throws SQLException {
  }

  @Override
  public void listById(listByIdRequest request, StreamObserver<listByIdResponse> responseObserver) {
    try{
      int id = request.getId();
      executeQuerys.getById(id);
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
  }


}