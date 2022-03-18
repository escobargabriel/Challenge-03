package databaseInteracting;

import com.fasterxml.jackson.databind.ObjectMapper;
import generated.AddProductRequest;
import generated.AddProductResponse;
import generated.DataChunk;
import generated.DownloadFileRequest;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileAccessJsonFile implements FileAccess {
  productGrpc.productBlockingStub prodStub;
  ManagedChannel channel;

  /**
   * Constructor method of class
   * @param channel channel to connect with server
   */
  public FileAccessJsonFile(ManagedChannel channel) {
    this.channel = channel;
    prodStub = productGrpc.newBlockingStub(channel);
  }

  /**
   * Method to import data from a JSON file to database.
   * @param jsonImportFileName String - name of data file.
   * @throws IOException exception throw when I/O problems occurs.
   */
  @Override
  public void importFile(String jsonImportFileName) throws IOException {
    System.out.println("Reading data from the JSON file and store on the database.");
    byte[] mapData = Files.readAllBytes(Paths.get(jsonImportFileName));
    Product[] productsArray = null;
    ObjectMapper objectMapper = new ObjectMapper();
    productsArray = objectMapper.readValue(mapData, Product[].class);
    Product[] productList = productsArray;
    for (Product product : productList) {
      String name = product.getName();
      int stock = product.getStock();
      float price = product.getPrice();
      AddProductRequest request = AddProductRequest.newBuilder().setName(name).setStock(stock).setPrice(price).build();
      AddProductResponse response = prodStub.addProduct(request);
      System.out.println(response.getName());
    }

  }

  /**
   * Method to export data from the database to a JSON file.
   * @param jsonExportFileName String - name of file with exported data.
   * @throws IOException exception throw when I/O problems occurs.
   */
  @Override
  public void exportFile(String jsonExportFileName) throws IOException {
    System.out.println("Downloading data from data base...");
    DownloadFileRequest downloadFileRequest = DownloadFileRequest.newBuilder().setFileName(jsonExportFileName).build();
    Iterator<DataChunk> dataChunkIterator = prodStub.downloadFile(downloadFileRequest);
    while (dataChunkIterator.hasNext()) {
      DataChunk next = dataChunkIterator.next();
      FileWriter fileWriter = new FileWriter(jsonExportFileName);
      fileWriter.write(next.getData());
      fileWriter.flush();
      fileWriter.close();
      break;
    }
  }
}
