package be.ictdynamic.server;

import be.ictdynamic.domain.Employee;
import be.ictdynamic.domain.Greeting;
import be.ictdynamic.serverImpl.BackendImplFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@ResponseStatus(HttpStatus.OK)
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BackendImplFinal backendImplFinal;

    @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Greeting greeting(@RequestParam(value = "commune1", defaultValue = "Hellow World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployee(@PathVariable Integer id) {
        List<Employee> employeeList = new ArrayList<>();

        try {
            // NIET DOEN : BackendImplFinal moet gewired zijn en moet een component zijn !!!
            // BackendImplFinal backendImplFinal = new BackendImplFinal();
            employeeList = backendImplFinal.getEmployees();
        }
        catch (Exception e) {
            LOGGER.error(">>>Fatal Error : " + e);
        }
        return employeeList;
    }

}
