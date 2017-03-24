package be.ictdynamic.rest0.config;

public class MongoConfig {
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
