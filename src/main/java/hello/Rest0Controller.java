package hello;

import dao.EmployeeDao;
import dao.EmployeeDaoImpl;
import domain.Employee;
import domain.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@ResponseStatus(HttpStatus.OK)
public class Rest0Controller {

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
}
