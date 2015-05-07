package be.ictdynamic.serverImpl;

import be.ictdynamic.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFname(String fname);

    public List<Customer> findByLname(String lname);

}