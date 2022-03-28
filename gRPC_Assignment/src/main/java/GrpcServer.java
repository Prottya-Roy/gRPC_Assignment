import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.UserService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(1111).addService(new UserService()).build();

        server.start();

        logger.info("Server Started at port no : "+server.getPort());
        server.awaitTermination(60, TimeUnit.SECONDS);
    }
}
