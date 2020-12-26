package by.jrr.feedback.mappers;

import by.jrr.feedback.bean.Item;
import by.jrr.feedback.bean.Reviewable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewableMapper {
    ReviewableMapper OF = Mappers.getMapper(ReviewableMapper.class);
//
//    @Mapping(source = "", target = "Id")
//    @Mapping(source = "", target = "reviewedEntityId")
//    @Mapping(source = "", target = "reviewedItemType")
//    Item requestForReviewDtoToReviewable(Reviewable reviewable);

    default Item reviewableToItem(Reviewable reviewable) {
        Item item = new Item();
        item.setReviewedEntityId(reviewable.getId());
        item.setReviewedItemType(reviewable.getType());
        return item;
    }

}
