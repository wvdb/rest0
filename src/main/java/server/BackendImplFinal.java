package server;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import config.DummyBean1;
import config.MongoConfigBean;
import domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BackendImplFinal {

    // @Autowired
    // private Environment environment;

    // @Autowired
    // private DummyBean1 dummyBean1;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Employee> getEmployees() throws UnknownHostException {
        LOGGER.debug("getEmployees() started");

        // ApplicationContext context1 = new ClassPathXmlApplicationContext("file:src/main/resources/spring-config.xml");

        /* if (environment == null) {
           LOGGER.warn("Value of environment is null");
        }
        else  {
            LOGGER.debug("Value of environment is not null");
            LOGGER.debug("Value of environment.getProperty(\"mongoClientURI\") = " + environment == null ? null : environment.getProperty("mongoClientURI"));
        } */

        /* if (dummyBean1 != null) {
            dummyBean1.setDummyProp1("this is a test");
            LOGGER.debug("Value of dummyBean1.dummyProp1 = " + dummyBean1.getDummyProp1());
        }
        else {
            LOGGER.warn("Value of dummyBean1 is null");
        } */

        ApplicationContext context2 = new AnnotationConfigApplicationContext(config.ApplicationConfig.class);

        DummyBean1 dummyBean1 = context2.getBean(DummyBean1.class);
        LOGGER.debug(">>>Value of dummyBean1 = " + dummyBean1);

        MongoConfigBean mongoConfigBean = context2.getBean(MongoConfigBean.class);
        LOGGER.debug(">>>Value of mongoConfigBean = " + mongoConfigBean);

        String mongoClientURIString = null;

        if (mongoConfigBean != null) {
            mongoClientURIString = mongoConfigBean.getMongoClientURI();
        }

        LOGGER.debug("Value of mongoClientURIString = " + mongoClientURIString);

        MongoClientURI uri = new MongoClientURI(mongoClientURIString);
        MongoClient mongoClient = new MongoClient(uri);
        DB db = mongoClient.getDB(uri.getDatabase());
        List<Employee> employeeList = new ArrayList<>();

        DBCollection dbCollection = db.getCollection("employee");
        DBCursor dbCursor = dbCollection.find();
        try {
            while (dbCursor.hasNext()) {
                DBObject dbobject = dbCursor.next();

                Map<String, Integer> durationMap = new HashMap<>();
                durationMap.put("DurationCar", (Integer) dbobject.get("DurationCar"));
                durationMap.put("DurationBike", (Integer) dbobject.get("DurationBike"));
                durationMap.put("DurationPublicTransport", (Integer) dbobject.get("DurationPublicTransport"));

                Map<String, Integer> distanceMap = new HashMap<>();
                distanceMap.put("DistanceCar", (Integer) dbobject.get("DistanceCar"));
                distanceMap.put("DistanceBike", (Integer) dbobject.get("DistanceBike"));

                Employee employee = new Employee((String) dbobject.get("Commune1"), (String) dbobject.get("Address1"), (String) dbobject.get("Country1")
                        , (String) dbobject.get("Commune2"), (String) dbobject.get("Address2"), (String) dbobject.get("Country2")
                        , (Double) dbobject.get("Latitude"), (Double) dbobject.get("Longitude"), durationMap, distanceMap);

                employeeList.add(employee);
            }
        } finally {
            dbCursor.close();
        }

        mongoClient.close();

        return employeeList;
    }
}
