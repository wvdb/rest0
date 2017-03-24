package be.ictdynamic.rest0.dao;

import be.ictdynamic.rest0.domain.Employee;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Class EmployeeDao.
 *
 * @author Wim Van den Brande
 * @since 12/12/2016 - 12:32
 */
public interface EmployeeDao {
    List<Employee> getAllEmployees() throws UnknownHostException;
    Employee getEmployee(Integer id) throws UnknownHostException;
}
