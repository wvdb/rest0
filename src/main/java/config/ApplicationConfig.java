package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/config/application.properties")
public class ApplicationConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Environment environment;

    public ApplicationConfig() {
        LOGGER.debug(">>>Object ApplicationConfig instantiated");
    }

    @Bean
    @Profile("local")
    public MongoConfigBean applicationConfigBeanLocal() {
        LOGGER.info(">>>executing applicationConfigBeanLocal");

        LOGGER.info(">>>Value of local environment.getProperty(\"mongoClientURI\") = " + environment.getProperty("mongoClientURI.local"));
        String mongoClientURI = environment.getProperty("mongoClientURI.local");
        MongoConfigBean mongoConfigBean = new MongoConfigBean();
        mongoConfigBean.setMongoClientURI(mongoClientURI);

        return mongoConfigBean;
    }

    @Bean
    @Profile("cloud")
    public MongoConfigBean applicationConfigBeanCloud() {
        String mongoClientURI = null;

        LOGGER.info(">>>Value of cloud environment.getProperty(\"mongoClientURI\") = " + environment.getProperty("mongoClientURI.cloud"));
        mongoClientURI = environment.getProperty("mongoClientURI.cloud");
        MongoConfigBean mongoConfigBean = new MongoConfigBean(mongoClientURI);
        mongoConfigBean.setMongoClientURI(mongoClientURI);

        return mongoConfigBean;
    }

    @Bean
    public DummyBean1 dummy() {
        LOGGER.info(">>>instantiating DummyBean1");
        DummyBean1 dummyBean1 = new DummyBean1("a b c");
        return dummyBean1;
    }

}
