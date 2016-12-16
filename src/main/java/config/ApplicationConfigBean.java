package config;

public class ApplicationConfigBean {
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
