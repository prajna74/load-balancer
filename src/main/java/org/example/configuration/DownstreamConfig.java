package org.example.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownstreamConfig {
    public int port;
    public String host;
    public String url;
    public String getUrl(){
        return "http://"+host+":"+port+"/";
    }
}
