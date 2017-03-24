package be.ictdynamic.rest0;

import be.ictdynamic.rest0.config.MongoConfig;
import be.ictdynamic.rest0.domain.Dummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    public String MongoClientURIProperty() {
        return environment.getProperty("mongoClientURI");
    }

    @Bean
    public Dummy dummy() {
        return new Dummy();
    }

    @Bean
    @Profile("local")
    public MongoConfig applicationConfigBeanLocal() {
        String mongoClientURI;
        MongoConfig mongoConfig = new MongoConfig();
        if (environment != null) {
            mongoClientURI = environment.getProperty("mongoClientURI.local");
            mongoConfig.setMongoClientURI(mongoClientURI);
        }
        return mongoConfig;
    }

    @Bean
    @Profile("cloud")
    public MongoConfig applicationConfigBeanCloud() {
        String mongoClientURI;
        MongoConfig mongoConfig = new MongoConfig();
        if (environment != null) {
            mongoClientURI = environment.getProperty("mongoClientURI.cloud");
            mongoConfig.setMongoClientURI(mongoClientURI);
        }
        return mongoConfig;
    }

}
