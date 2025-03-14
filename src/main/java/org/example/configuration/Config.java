package org.example.configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Config {

    public ServerConfig server;
    public List<DownstreamConfig> downstreams;
}
