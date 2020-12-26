package by.jrr.feedback.elements;

import by.jrr.feedback.bean.EntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForReviewDto {
    public static final String ID = "id";
    public static final String ITEM_ID = "itemId";
    public static final String REVIEWED_ENTITY_ID = "reviewedEntityId";
    public static final String REVIEWED_ENTITY_TYPE = "reviewedEntityType";
    public static final String REQUESTER_NOTES = "requesterNotes";
    public static final String LINK = "link";

    private Long Id;
    private Long itemId;
    private Long reviewedEntityId;
    private EntityType reviewedEntityType;

    private String requesterNotes;
    private String link;
}
