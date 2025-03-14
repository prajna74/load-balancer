package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.configuration.Config;
import org.example.configuration.ConfigLoader;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
      Config config = new ConfigLoader("src/main/resources/config.yaml").getAppConfig();
      ClientListener clientListener = new ClientListener(config);
      RequestHandler requestHandler = new RequestHandler(config,clientListener);
      LoadBalancer loadBalancer = new LoadBalancer(config.server.appPort,requestHandler);
        HttpServer server = loadBalancer.initialiseServer();
      server.start();
      ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      System.out.println("Scheduling health checks...");

      scheduledExecutorService.scheduleAtFixedRate(() -> {
        try {
          System.out.println("Executing health check...at "+System.currentTimeMillis());
          clientListener.runHealthChecks();
        } catch (Exception e) {
          System.err.println("Health check error: " + e.getMessage());
        }
      }, 0, 30, TimeUnit.SECONDS);
    }
}