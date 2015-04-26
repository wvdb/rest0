package server;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import config.ApplicationConfig;
import config.ApplicationConfigBean;
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

public class BackendImpl {

//    @Resource
//    private Environment environment;

//
//    @Autowired
//    private ApplicationConfigBean applicationConfigBean;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Employee> getEmployees() throws UnknownHostException {
        LOGGER.debug("getEmployees() started");

//        if (environment == null) {
//            LOGGER.debug("Value of environment is null");
//        }
//        else  {
//            LOGGER.debug("Value of environment is not null");
//            LOGGER.debug("Value of environment.getProperty(\"mongoClientURI\") = " + environment == null ? null : environment.getProperty("mongoClientURI"));
//        }

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);

        ApplicationConfigBean applicationConfigBean = ctx.getBean(ApplicationConfigBean.class);

        String mongoClientURIString = null;

        LOGGER.debug("Value of mongoClientURIProperty = " + ctx.getBean("MongoClientURIProperty"));

        LOGGER.debug("Value of applicationConfigBean = " + applicationConfigBean);
        if (applicationConfigBean != null) {
            mongoClientURIString = applicationConfigBean.getMongoClientURI();
        }

        LOGGER.debug("Value of mongoClientURI = " + mongoClientURIString);

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

                Employee employee = new Employee(     (String) dbobject.get("Commune1"), (String) dbobject.get("Address1"), (String) dbobject.get("Country1")
                                                    , (String) dbobject.get("Commune2"), (String) dbobject.get("Address2"), (String) dbobject.get("Country2")
                                                    , (Double) dbobject.get("Latitude"), (Double) dbobject.get("Longitude"), durationMap, distanceMap);

                employeeList.add(employee);
            }
        }
        finally {
            dbCursor.close();
        }

        mongoClient.close();

        return employeeList;
    }
}
