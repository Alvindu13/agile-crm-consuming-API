package com.agilecrm;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("map")
    @JsonIgnore
    private String map;

    @JsonProperty("allDay")
    private Boolean allDay;

    @JsonProperty("color")
    private String color;

    @JsonProperty("contacts")
    private String[] contacts;

    @JsonProperty("created_time")
    private Integer created_time;

    @JsonProperty("end")
    private Integer end;

    @JsonProperty("is_event_starred")
    private Boolean is_event_starred;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("title")
    private String title;

    private Owner12 owners = new Owner12();

    public Event() {
    }

    public Event(Long id, String map, Boolean allDay, String color, String[] contacts, Integer created_time, Integer end, Boolean is_event_starred, Integer start, String title, Owner12 owners) {
        this.id = id;
        this.map = map;
        this.allDay = allDay;
        this.color = color;
        this.contacts = contacts;
        this.created_time = created_time;
        this.end = end;
        this.is_event_starred = is_event_starred;
        this.start = start;
        this.title = title;
        this.owners = owners;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(String[] contacts) {
        this.contacts = contacts;
    }

    public Integer getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Integer created_time) {
        this.created_time = created_time;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Boolean getIs_event_starred() {
        return is_event_starred;
    }

    public void setIs_event_starred(Boolean is_event_starred) {
        this.is_event_starred = is_event_starred;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Owner12 getOwners() {
        return owners;
    }

    public void setOwners(Owner12 owners) {
        this.owners = owners;
    }

    @Override
    public String toString() {
        return "com.agilecrm.Event{" +
                "id=" + id +
                ", map='" + map + '\'' +
                ", allDay=" + allDay +
                ", color='" + color + '\'' +
                ", contacts=" + Arrays.toString(contacts) +
                ", created_time=" + created_time +
                ", end=" + end +
                ", is_event_starred=" + is_event_starred +
                ", start=" + start +
                ", title='" + title + '\'' +
                '}';
    }
}

