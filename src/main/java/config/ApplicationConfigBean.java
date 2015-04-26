package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfigBean {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String mongoClientURI;

    public String getMongoClientURI() {
        return mongoClientURI;
    }

    public void setMongoClientURI(String mongoClientURI) {
        this.mongoClientURI = mongoClientURI;
    }

    @Override
    public String toString() {
        return "ApplicationConfigBean{" +
                "mongoClientURI='" + mongoClientURI + '\'' +
                '}';
    }
}
