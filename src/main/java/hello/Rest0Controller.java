package hello;

import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import domain.Employee;
import domain.Greeting;
import org.apache.camel.CamelContext;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@ResponseStatus(HttpStatus.OK)
public class Rest0Controller {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String TEMPLATE = "You are from %s!";
    public static final int TEN_SECONDS = 10000;
    private static final AtomicLong COUNTER = new AtomicLong();

    @Autowired
    private CamelDummy camelDummy;

    @Value("${country}")
    private String country;

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Greeting greeting(@RequestParam(value = "commune", defaultValue = "Edegem") String commune) {
        return new Greeting(COUNTER.incrementAndGet(), String.format(TEMPLATE, commune));
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Employee getEmployee(@PathVariable Integer id) {
        Employee employee = new Employee();

        LOGGER.debug("value van country = {}", country);

        try {
            EmployeeDao employeeDao = new EmployeeDaoImpl();
            employee = employeeDao.getEmployee(id);
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
                            .log("Processing file [${file:onlyname}]")
                            .setHeader("header1", constant("value of header1"))
                            .setHeader("header2", constant("value of header2"))
                            .bean(camelDummy, "doSomething")
                            .log(">>>Starting processing of message with id ${id}")
                            .log("body = " + "${body}")
                            .log("id = ${id}")
                            .log("header 1 = " + "${header.header1}")
                            .log("header 2 = ${header.header2}")
                            .choice()
                                .when(body().contains("out1")).to("file://C:/temp/camel_test_data/out/out1")
                                .when(body().contains("out2")).to("file://C:/temp/camel_test_data/out/out2")
                                .otherwise().to("file://C:/temp/camel_test_data/out/other")
                            .log("<<<Exiting processing of message with id ${id}");
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
