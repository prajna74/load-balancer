package org.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.configuration.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHandler implements HttpHandler {
    Config config;
    ClientListener clientListener;
    RequestHandler(Config config, ClientListener clientListener){
        this.config = config;
        this.clientListener = clientListener;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        if(clientListener.activeServers.isEmpty())
        {
            System.out.println("No healthy hosts to serve the request");
            String errorMessage = "Something went wrong, please try again later";
            exchange.sendResponseHeaders(500,errorMessage.length());
            os.write(errorMessage.getBytes());
            return;
        }
        var clientServer = clientListener.getServer();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(clientServer.getUrl())).GET().build();
        client.sendAsync(request,HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response ->{
                            try {
                                exchange.sendResponseHeaders(response.statusCode(),response.body().length());
                                os.write(response.body().getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

    }
}
