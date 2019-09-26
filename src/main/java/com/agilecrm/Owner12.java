package com.agilecrm;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "owner")
@XmlAccessorType(XmlAccessType.FIELD)
public class Owner12 {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("pic")
    private String pic;

    @JsonProperty("schedule_id")
    private String schedule_id;

    @JsonProperty("calendar_url")
    private String calendar_url;

    @JsonProperty("calendarURL")
    private String calendarURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getCalendar_url() {
        return calendar_url;
    }

    public void setCalendar_url(String calendar_url) {
        this.calendar_url = calendar_url;
    }

    public String getCalendarURL() {
        return calendarURL;
    }

    public void setCalendarURL(String calendarURL) {
        this.calendarURL = calendarURL;
    }

    @Override
    public String toString() {
        return "com.agilecrm.Owner12{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", pic='" + pic + '\'' +
                ", schedule_id=" + schedule_id +
                ", calendar_url='" + calendar_url + '\'' +
                ", calendarURL='" + calendarURL + '\'' +
                '}';
    }
}
