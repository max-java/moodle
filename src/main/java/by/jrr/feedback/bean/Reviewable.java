package by.jrr.feedback.bean;

/***
 * Interface just to mark entity as reviewable
 */

public interface Reviewable {

    Long getId();
    EntityType getType();
    String getName();

}
