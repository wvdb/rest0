package be.ictdynamic.rest0.dao;


import be.ictdynamic.rest0.ApplicationConfig;
import be.ictdynamic.rest0.config.MailConfig;
import be.ictdynamic.rest0.config.MongoConfig;
import be.ictdynamic.rest0.domain.Employee;
import be.ictdynamic.rest0.domain.Greeting;
import be.ictdynamic.rest0.service.MailService;
import com.mongodb.*;
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

public class EmployeeDaoImpl implements EmployeeDao {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Employee> getAllEmployees() throws UnknownHostException {
        LOGGER.debug("getAllEmployees() started");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        MongoConfig mongoConfig = applicationContext.getBean(MongoConfig.class);

        String mongoClientURIString = null;

        LOGGER.debug("Value of mongoClientURIProperty = " + applicationContext.getBean("MongoClientURIProperty"));

        LOGGER.debug("Value of applicationConfigBean = " + mongoConfig);
        if (mongoConfig != null) {
            mongoClientURIString = mongoConfig.getMongoClientURI();
        }

        LOGGER.debug("Value of mongoClientURI = " + mongoClientURIString);

        return getAllEmployees(mongoClientURIString);
    }

    @Override
    public Employee getEmployee(Integer id) throws UnknownHostException {
        LOGGER.debug(String.format("getEmployee(%03d) started", id));

        LOGGER.debug("Resultaat van greeting = " + getGreeting().getContent());

        List<Employee> employees = getAllEmployees("");

        if (id <= employees.size()) {
            List<String> communes = employees.stream().map(Employee::getCommune2).distinct().collect(Collectors.toList());
            // spelen met streams
//            return getAllEmployees(mongoClientURIString).get(id);
//            return (Employee) employees.stream().filter(e -> "Belgium".equals(e.getCountry1())).toArray()[0];

            // spelen met mailservice
            try {
                this.dummyEmail();
            } catch (Exception e) {
                LOGGER.error("Exception when sending email: {}", e.getMessage());
            }

            LOGGER.debug("Communes zijn: " + communes.stream().collect(Collectors.joining(", ")));
            communes.stream().sorted().forEach(LOGGER::debug);

            return employees.stream().filter(e -> "België".equals(e.getCountry1())).collect(Collectors.toList()).get(id);
        }
        else {
            return null;
        }
    }

    private void dummyEmail() {
        getMailService().sendMail("sir", "monday");
    }

    private MailService getMailService() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MailConfig.class);
        MailService mailService =  ctx.getBean(MailService.class);
        LOGGER.debug("mailService = " + mailService);
        return mailService;
    }

    private Greeting getGreeting() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MailConfig.class);
        return ctx.getBean(Greeting.class);
    }

    private List<Employee> getAllEmployees(String mongoClientURIString) throws UnknownHostException {
        MongoClientURI uri = new MongoClientURI("mongodb://user1:user15905@ds033599.mongolab.com:33599/ictdynamic");
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
