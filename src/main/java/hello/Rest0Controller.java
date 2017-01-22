package hello;

import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import domain.Employee;
import domain.Greeting;
import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@ResponseStatus(HttpStatus.OK)
public class Rest0Controller {

    private static final String template = "Hello, %s!";
    public static final int TEN_SECONDS = 10000;
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private CamelDummy camelDummy;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${country}")
    private String country;

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Greeting greeting(@RequestParam(value = "commune1", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable Integer id) {
        Employee employee = null;


        DateFormat dateFormat0 = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssz");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssZ");
        DateFormat dateFormat3a = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssXXX");

        DateFormat dateFormat3b = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssXXX");
        dateFormat3b.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        Date date = new Date();
        System.out.println("new SimpleDateFormat(\"yyyy/MM/dd'T'HH:mm:ss\")     " + dateFormat0.format(date));
        System.out.println("new SimpleDateFormat(\"yyyy/MM/dd'T'HH:mm:ssz\")    " + dateFormat1.format(date));
        System.out.println("new SimpleDateFormat(\"yyyy/MM/dd'T'HH:mm:ssZ\")    " + dateFormat2.format(date));
        System.out.println("new SimpleDateFormat(\"yyyy/MM/dd'T'HH:mm:ssXXX\")  " + dateFormat3a.format(date));
        System.out.println("new SimpleDateFormat(\"yyyy/MM/dd'T'HH:mm:ssXXX\") - timezone NY" + dateFormat3b.format(date));

        Instant instant = Instant.now();
        System.out.println(instant);

        LOGGER.debug("value van country = {}", country);

        try {
            EmployeeDao employeeDao = new EmployeeDaoImpl();
//            employee = employeeDao.getEmployee(id);
        }
        catch (Exception e) {
            LOGGER.error(">>>Fatal Error : " + e);
            e.printStackTrace();
        }
        return employee;
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployees() {
        List<Employee> employeeList = new ArrayList<>();

        try {
            EmployeeDao employeeDao = new EmployeeDaoImpl();
            employeeList = employeeDao.getAllEmployees();
        }
        catch (Exception e) {
            LOGGER.error(">>>Fatal Error : " + e);
            e.printStackTrace();
        }
        return employeeList;
    }

    @RequestMapping(value = "/testCamel1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCamel1() {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new RouteBuilder() {
                public void configure() {

                    from("file://C:/temp/camel_test_data/in?recursive=true&delete=false&delay=5000")
                            .routeId("route-wim-0")
                            .setHeader("header1", constant("value of header1"))
                            .setHeader("header2", constant("value of header2"))
                            .bean(camelDummy, "doSomething")
                            .log("body = " + "${body}")
                            .log("id = ${id}")
                            .log("header 1 = " + "${header.header1}")
                            .log("header 2 = ${header.header2}")
                            .to("file://C:/temp/camel_test_data/out")
                            .log("Message written to out directory");
                }

                private String method1(String dummy1) {
                    return "dit is al te gek";
                }
            });
            context.start();
            Thread.sleep(TEN_SECONDS);
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/testCamel2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCamel2() {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    from("file://C:/temp/camel_test_data/in?recursive=true&noop=true&delay=5000")
                            .to("test-jms:queue:test.queue")
                            .log("Message written in directory C:/temp/camel_test_data/out?showAll=true");
                }
            });
            context.start();

            // sleep for 10 seconds
            Thread.sleep(TEN_SECONDS);

            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/testCamel3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCamel3() {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    from("test-jms:queue:test.queue").to("file://C:/temp/camel_test_data/out");
                }
            });
            context.start();

            ProducerTemplate template = context.createProducerTemplate();
            for (int i = 0; i < 10; i++) {
                template.sendBody("test-jms:queue:test.queue", "Test Message: " + i);
            }

            // sleep for 10 seconds
            Thread.sleep(TEN_SECONDS);

            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
