package DatabaseInteracting;

import Service.ProductService;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Server {
  public static final Logger logger = Logger.getLogger(Server.class.getName());

  public static void main(String[] args) throws IOException, InterruptedException, SQLException {
    int port = Integer.parseInt(args[0]);
    String url = args[1];
    String user = args[2];
    String password = args[3];

    io.grpc.Server server = ServerBuilder.forPort(port).addService(new ProductService(url, user, password)).build();

    server.start();

    logger.info("Server started on" + server.getPort());

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      server.shutdown();
    }));

    server.awaitTermination();

  }

}