package hello;

import domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import server.BackendImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@ResponseStatus(HttpStatus.OK)
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Greeting greeting(@RequestParam(value = "commune1", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable Integer id) {
        Employee employee = null;

        try {
            BackendImpl backendImpl = new BackendImpl();
            employee = backendImpl.getEmployee(id);
        }
        catch (Exception e) {
            LOGGER.error(">>>Fatal Error : " + e);
            e.printStackTrace();
        }
        return employee;
    }

    @RequestMapping(value = "/employees/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployees(@PathVariable Integer id) {
        List<Employee> employeeList = new ArrayList<>();

        try {
            BackendImpl backendImpl = new BackendImpl();
            employeeList = backendImpl.getAllEmployees();
        }
        catch (Exception e) {
            LOGGER.error(">>>Fatal Error : " + e);
            e.printStackTrace();
        }
        return employeeList;
    }
}
