package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class LoadBalancer {
    int port;
    RequestHandler requestHandler;
    public LoadBalancer(int port,RequestHandler requestHandler){
        this.port = port;
        this.requestHandler = requestHandler;
    }

    public HttpServer initialiseServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port),0);
        httpServer.createContext("/",requestHandler);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        return httpServer;
    }
}
