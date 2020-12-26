package by.jrr.feedback.mappers;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.elements.RequestForReviewDto;
import by.jrr.feedback.bean.Reviewable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestForReviewDtoMapper {
    RequestForReviewDtoMapper OF = Mappers.getMapper(RequestForReviewDtoMapper.class);

    default Reviewable requestFroReviewDtoToReviewable(RequestForReviewDto requestForReviewDto) {
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
}
