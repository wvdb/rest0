package domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Repository
public class Employee {

    @NotNull
    @Length(min=1)
    protected String commune1;

    @NotNull
    @Length(min=1)
    protected String address1;

    @NotNull
    @Length(min=1)
    protected String country1;

    @NotNull
    @Length(min=1)
    protected String commune2;

    @NotNull
    @Length(min=1)
    protected String address2;

    @NotNull
    @Length(min=1)
    protected String country2;

    @NotNull
    protected Double latitude;

    @NotNull
    protected Double longitude;

    @NotNull
    @Size(min = 1)
    protected Map<String, Integer> durationMap = new HashMap<>();

    @NotNull
    @Size(min = 1)
    protected Map<String, Integer> distanceMap = new HashMap<>();

    public String getCountry1() {
        return country1;
    }

    public void setCountry1(String country1) {
        this.country1 = country1;
    }

    public String getCommune2() {
        return commune2;
    }

    public void setCommune2(String commune2) {
        this.commune2 = commune2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCountry2() {
        return country2;
    }

    public void setCountry2(String country2) {
        this.country2 = country2;
    }

    public Employee(String commune1, String address1, String country1, String commune2, String address2, String country2, Double latitude, Double longitude, Map<String, Integer> durationMap, Map<String, Integer> distanceMap) {
        this.commune1 = commune1;
        this.address1 = address1;
        this.country1 = country1;
        this.commune2 = commune2;
        this.address2 = address2;
        this.country2 = country2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.durationMap = durationMap;
        this.distanceMap = distanceMap;
    }

    public String getCommune1() {
        return commune1;
    }

    public void setCommune1(String commune1) {
        this.commune1 = commune1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Map<String, Integer> getDurationMap() {
        return durationMap;
    }

    public void setDurationMap(Map<String, Integer> durationMap) {
        this.durationMap = durationMap;
    }

    public Map<String, Integer> getDistanceMap() {
        return distanceMap;
    }

    public void setDistanceMap(Map<String, Integer> distanceMap) {
        this.distanceMap = distanceMap;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "commune1='" + commune1 + '\'' +
                ", address1='" + address1 + '\'' +
                ", country1='" + country1 + '\'' +
                ", commune2='" + commune2 + '\'' +
                ", address2='" + address2 + '\'' +
                ", country2='" + country2 + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", durationMap=" + durationMap +
                ", distanceMap=" + distanceMap +
                '}';
    }
}
