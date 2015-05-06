package be.ictdynamic.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DummyBean2 {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String dummyProp1;

    public DummyBean2() {
        LOGGER.info(">>>DummyBean2 empty constructor");
    }

    public DummyBean2(String dummyProp1) {
        LOGGER.info(">>>DummyBean2 dummyProp1 = " + dummyProp1);
        this.dummyProp1 = dummyProp1;
    }

    public String getDummyProp1() {
        return dummyProp1;
    }

    public void setDummyProp1(String dummyProp1) {
        this.dummyProp1 = dummyProp1;
    }

    @Override
    public String toString() {
        return "DummyBean2{" +
                "dummyProp1='" + dummyProp1 + '\'' +
                '}';
    }
}
