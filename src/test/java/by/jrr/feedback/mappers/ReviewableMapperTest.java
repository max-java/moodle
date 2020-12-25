package by.jrr.feedback.mappers;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Reviewable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewableMapperTest {

    @Test
    void reviewableToItem() {
        Reviewable reviewable = makeReviewable();
        Item expectedItem = makeItem();
        Item actualIatem = ReviewableMapper.OF.reviewableToItem(reviewable);

        assertEquals(expectedItem, actualIatem);
    }

    private Reviewable makeReviewable() {
        return new Reviewable() {
            @Override
            public Long getId() {
                return 11L;
            }

            @Override
            public EntityType getType() {
                return EntityType.PRACTICE_QUESTION;
            }

            @Override
            public String getName() {
                return "Practice question name";
            }
        };
    }

    private Item makeItem() {
        return Item.builder()
                .reviewedEntityId(11L)
                .reviewedItemType(EntityType.PRACTICE_QUESTION)
                .build();
    }
}
