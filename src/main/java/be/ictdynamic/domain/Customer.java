package be.ictdynamic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
public class Customer {
    // private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Id
    private String id;
    private String fname;
    private String lname;

    public Customer() {
//        LOGGER.info(">>>Customer empty constructor");
    }

    public Customer(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                '}';
    }
}
