package server;


import domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.List;

public class BackendImpl {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Employee> getEmployees() throws UnknownHostException {
        LOGGER.debug("getEmployeesDummy() started");
        List<Employee> empList = null;

        // ApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/spring-config.xml");

        try {
            empList = new BackendImplFinal().getEmployees();
        } catch (Exception e) {
            LOGGER.debug("getEmployeesDummy() failed with exception + " + e);
        }
        LOGGER.debug("getEmployeesDummy() exited");
        return empList;
    }
}
