package by.jrr.statistic.bean;

import by.jrr.feedback.bean.EntityType;

public interface Trackable {

    Long getId();
    EntityType getType();
    String getName();

}
