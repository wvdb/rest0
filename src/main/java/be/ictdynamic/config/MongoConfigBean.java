package be.ictdynamic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoConfigBean {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String mongoClientURI;

    public MongoConfigBean(String mongoClientURI) {
        LOGGER.info(">>>MongoConfigBean mongoClientURI = " + mongoClientURI);
        this.mongoClientURI = mongoClientURI;
    }

    public MongoConfigBean() {
        LOGGER.info(">>>MongoConfigBean empty constructor");
    }

    public String getMongoClientURI() {
        return mongoClientURI;
    }

    public void setMongoClientURI(String mongoClientURI) {
        this.mongoClientURI = mongoClientURI;
    }

    @Override
    public String toString() {
        return "MongoConfigBean{" +
                "mongoClientURI = '" + mongoClientURI + '\'' +
                '}';
    }
}
