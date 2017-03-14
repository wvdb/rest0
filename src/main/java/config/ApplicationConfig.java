package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/config/application.properties")
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    public String MongoClientURIProperty() {
        return environment.getProperty("mongoClientURI");
    }

    @Bean
    @Profile("local")
    public ApplicationConfigBean applicationConfigBeanLocal() {
        String mongoClientURI;
        ApplicationConfigBean applicationConfigBean = new ApplicationConfigBean();
        if (environment != null) {
            mongoClientURI = environment.getProperty("mongoClientURI.local");
            applicationConfigBean.setMongoClientURI(mongoClientURI);
        }
        return applicationConfigBean;
    }

    @Bean
    @Profile("cloud")
    public ApplicationConfigBean applicationConfigBeanCloud() {
        String mongoClientURI;
        ApplicationConfigBean applicationConfigBean = new ApplicationConfigBean();
        if (environment != null) {
            mongoClientURI = environment.getProperty("mongoClientURI.cloud");
            applicationConfigBean.setMongoClientURI(mongoClientURI);
        }
        return applicationConfigBean;
    }
}
