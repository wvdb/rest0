package hello;

import org.springframework.stereotype.Component;

/**
 * Created by wvdbrand on 22/01/2017.
 */
@Component
public class CamelDummy {
    public void doSomething() {
        System.out.println("Dit is de methode die werd ge-invoked door Camel");
    }
}
