package by.jrr.feedback.mappers;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.Reviewable;
import by.jrr.feedback.elements.RequestForReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

import static by.jrr.common.util.ConvertUtil.stringToLongOrNull;

@Mapper
public interface RequestForReviewDtoMapper {
    RequestForReviewDtoMapper OF = Mappers.getMapper(RequestForReviewDtoMapper.class);

    default Reviewable requestForReviewDtoToReviewable(RequestForReviewDto requestForReviewDto) {
        return new Reviewable() {
            @Override
            public Long getId() {
                return requestForReviewDto.getReviewedEntityId();
            }

            @Override
            public EntityType getType() {
                return requestForReviewDto.getReviewedEntityType();
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    ReviewRequest requestForReviewDtoToReviewRequest(RequestForReviewDto requestFroReviewDto);

    @Mapping(target = "reviewedEntityType", source = "item.reviewedItemType")
    RequestForReviewDto  reviewRequestToRequestForReviewDto(ReviewRequest reviewRequest);

    default RequestForReviewDto paramMapToRequestForReviewDto(Map<String, String> paramMap) {
        return RequestForReviewDto.builder()
                .Id(stringToLongOrNull(paramMap.get(RequestForReviewDto.ID)))
                .itemId(stringToLongOrNull(paramMap.get(RequestForReviewDto.ITEM_ID)))
                .reviewedEntityId(stringToLongOrNull(paramMap.get(RequestForReviewDto.REVIEWED_ENTITY_ID)))
                .reviewedEntityType(EntityType.valueOf(paramMap.get(RequestForReviewDto.REVIEWED_ENTITY_TYPE)))
                .requesterNotes(paramMap.get(RequestForReviewDto.REQUESTER_NOTES))
                .link(paramMap.get(RequestForReviewDto.LINK))
                .build();
    }
}



