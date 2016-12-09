package server;


import com.mongodb.*;
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
import java.util.stream.Collectors;

public class BackendImpl {

//    @Resource
//    private Environment environment;

//
//    @Autowired
//    private ApplicationConfigBean applicationConfigBean;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Employee> getAllEmployees() throws UnknownHostException {
        LOGGER.debug("getAllEmployees() started");

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

        return getAllEmployees(mongoClientURIString);
    }

    public Employee getEmployee(Integer id) throws UnknownHostException {
        LOGGER.debug(String.format("getEmployee(%s) started", id));

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

        //TODO : java 8 and filtering on id
        List<Employee> employees = getAllEmployees(mongoClientURIString);
        if (id <= employees.size()) {
            // spelen met streams
//            return getAllEmployees(mongoClientURIString).get(id);
//            return (Employee) employees.stream().filter(e -> "Belgium".equals(e.getCountry1())).toArray()[0];
            return employees.stream().filter(e -> "Belgium".equals(e.getCountry1())).collect(Collectors.toCollection(ArrayList::new)).get(id);
        }
        else {
            return null;
        }
    }

    private List<Employee> getAllEmployees(String mongoClientURIString) throws UnknownHostException {
        MongoClientURI uri = new MongoClientURI(mongoClientURIString);
        MongoClient mongoClient = new MongoClient(uri);
        DB db = mongoClient.getDB(uri.getDatabase());
        List<Employee> employeeList = new ArrayList<>();

        DBCollection dbCollection = db.getCollection("employee");
        try (DBCursor dbCursor = dbCollection.find()) {
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
        }

        mongoClient.close();

        return employeeList;
    }

}
