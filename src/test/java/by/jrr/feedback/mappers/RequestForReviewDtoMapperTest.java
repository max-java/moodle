package by.jrr.feedback.mappers;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.elements.RequestForReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestForReviewDtoMapperTest {

    @Test
    void paramMapToRequestForReviewDto() {
        Map<String, String> paramMap = makeParamMap();
        RequestForReviewDto expectedRequestForReviewDto = makeRequestForReviewDto();

        RequestForReviewDto actualRequestForReviewDto = RequestForReviewDtoMapper.OF.paramMapToRequestForReviewDto(paramMap);

        assertEquals(expectedRequestForReviewDto, actualRequestForReviewDto);
    }

    private Map<String, String>  makeParamMap() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestForReviewDto.ID, "60");
        paramMap.put(RequestForReviewDto.ITEM_ID, "50");
        paramMap.put(RequestForReviewDto.REVIEWED_ENTITY_ID, "40");
        paramMap.put(RequestForReviewDto.REVIEWED_ENTITY_TYPE, "PRACTICE_QUESTION");
        paramMap.put(RequestForReviewDto.REQUESTER_NOTES, "requesterNotes");
        paramMap.put(RequestForReviewDto.LINK, "link");
        return paramMap;
    }

    private RequestForReviewDto makeRequestForReviewDto() {
        return RequestForReviewDto.builder()
                .Id(60L)
                .itemId(50L)
                .reviewedEntityId(40L)
                .reviewedEntityType(EntityType.PRACTICE_QUESTION)
                .requesterNotes("requesterNotes")
                .link("link")
                .build();
    }
}
