package by.jrr.feedback.bean;

public class EmptyReviewable implements Reviewable {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public ReviewedEntityType getType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
