package be.ictdynamic.serverImpl;


import be.ictdynamic.config.MongoConfigBean;
import be.ictdynamic.domain.DummyBean1;
import be.ictdynamic.domain.DummyBean2;
import be.ictdynamic.domain.DummyEmployee;
import be.ictdynamic.domain.Employee;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public final class BackendImplFinal {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DummyBean1 dummyBean1;
    @Autowired
    private DummyBean2 dummyBean2;
    @Autowired
    private DummyEmployee dummyEmployee;
    @Autowired
    @Qualifier("LocalMongoConfigBean")
    private MongoConfigBean mongoConfigBean;

    public List<Employee> getEmployees() throws UnknownHostException {
        LOGGER.info("getEmployees() started");

        LOGGER.info("Value of dummyBean1 = " + dummyBean1);
        LOGGER.info("Value of dummyBean2 = " + dummyBean2);
        LOGGER.info("Value of dummyEmployee = " + dummyEmployee);
        LOGGER.info("Value of mongoConfigBean = " + mongoConfigBean);

        // ApplicationContext context1 = new ClassPathXmlApplicationContext("file:src/main/resources/spring-be.ictdynamic.config.xml");

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

//        ApplicationContext context2 = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//
//        DummyBean1 dummyBean1 = context2.getBean(DummyBean1.class);
//

        String mongoClientURIString = null;

        if (mongoConfigBean != null) {
            mongoClientURIString = mongoConfigBean.getMongoClientURI();
        }

        LOGGER.info("Value of mongoClientURIString = " + mongoClientURIString);

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
