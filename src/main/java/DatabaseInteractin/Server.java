package DatabaseInteractin;

import Service.ProductService;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;



public class Server {
  public static final Logger logger = Logger.getLogger(Server.class.getName());

  public static void main(String[] args) throws IOException, InterruptedException, SQLException {
    io.grpc.Server server = ServerBuilder.forPort(50051).addService(new ProductService()).build();

    server.start();

    logger.info("Server started on" + server.getPort());

    Runtime.getRuntime().addShutdownHook(new Thread(() -> { server.shutdown();}));

    server.awaitTermination();

  }

}