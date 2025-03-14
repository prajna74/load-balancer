package org.example;

import org.example.configuration.Config;
import org.example.configuration.DownstreamConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ClientListener {

    List<DownstreamConfig> activeServers;

    int loadBalancedRequest;
    Config config;
    ClientListener(Config config)
    {
        this.config = config;
        this.activeServers=new ArrayList<>(config.downstreams);
        this.loadBalancedRequest = 0;
    }

    public void runHealthChecks(){
      for(DownstreamConfig downstream:this.config.downstreams){
              HttpClient client = HttpClient.newHttpClient();
              HttpRequest request = HttpRequest.newBuilder(URI.create(downstream.getUrl()+"healthcheck")).GET().build();
                  client.sendAsync(request,HttpResponse.BodyHandlers.ofString())
                                  .thenAccept(response -> {
                                      if(response!= null && response.statusCode()==200){
                                          if(!this.activeServers.contains(downstream)){
                                              this.activeServers.add(downstream);
                                          }
                                      }else if(response != null && response.statusCode()>300){
                                          if(activeServers.contains(downstream)) {
                                              this.activeServers.remove(downstream);
                                          }
                                      }
                                  }).exceptionally(e ->{
                              this.activeServers.remove(downstream);
                              return null;
                          });
          }
    }

    public DownstreamConfig getServer(){
        loadBalancedRequest++;
        var index = loadBalancedRequest%(this.activeServers.size());
        return this.activeServers.get(index);
    }
}
