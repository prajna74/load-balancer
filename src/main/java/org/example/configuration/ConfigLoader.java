package org.example.configuration;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
    String pathToConfig;
    public ConfigLoader(String path){
        this.pathToConfig = path;
    }

    public Config getAppConfig() throws IOException {
            Constructor constructor = new Constructor(Config.class,new LoaderOptions());
            TypeDescription configDescription = new TypeDescription(Config.class);
            configDescription.addPropertyParameters("downstreams", DownstreamConfig.class);
            configDescription.addPropertyParameters("server", ServerConfig.class);
            constructor.addTypeDescription(configDescription);
            Yaml yaml = new Yaml(constructor);

            Config config = yaml.load(Files.readString(Path.of(pathToConfig)));
            System.out.println(config.downstreams);
            System.out.println(config.server.appPort);
            return config;
        }
}
