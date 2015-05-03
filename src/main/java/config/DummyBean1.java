package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyBean1 {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String dummyProp1;

    public DummyBean1(String dummyProp1) {
        LOGGER.info(">>>DummyBean1 dummyProp1 = " + dummyProp1);
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
        return "DummyBean1{" +
                "dummyProp1='" + dummyProp1 + '\'' +
                '}';
    }
}
