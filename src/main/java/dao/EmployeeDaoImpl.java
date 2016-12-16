package dao;


import com.mongodb.*;
import config.ApplicationConfig;
import config.ApplicationConfigBean;
import config.MailConfig;
import domain.Employee;
import domain.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Repository;
import service.MailService;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Employee> getAllEmployees() throws UnknownHostException {
        LOGGER.debug("getAllEmployees() started");

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

    @Override
    public Employee getEmployee(Integer id) throws UnknownHostException {
        LOGGER.debug(String.format("getEmployee(%03d) started", id));

        LOGGER.debug("Resultaat van greeting = " + getGreeting().getContent());

        List<Employee> employees = getAllEmployees("");
        if (id <= employees.size()) {
            // spelen met streams
            this.dummyEmail();
            return employees.stream().filter(e -> "België".equals(e.getCountry1())).collect(Collectors.toList()).get(id);
        }
        else {
            return null;
        }
    }

    private void dummyEmail() {
        try {
            // use FakeSMTP to send an SMTP message : http://nilhcem.github.com/FakeSMTP/
            getJavaMailSender().sendMail("sir", "monday");
        } catch (MailException ex) {
            LOGGER.error("Exception when sending email: {}", ex.getMessage());
            throw ex;
        }
    }

    private MailService getJavaMailSender() {
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
