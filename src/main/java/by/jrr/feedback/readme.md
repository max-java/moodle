ReviewRequest - user ask code review for item
Review - code reviewer adds review

Item - container based on reviewedEntityId
        private Long reviewedEntityId;          id of any reviewable
        private EntityType reviewedItemType;    type of reviewable
reviewed Id + Type = composite key.

Based on Id+Type ItemId creates, which sets to Review and ReviewRequest. That way I could have several reviewRequests
for single reviewed entity.

Reviewable interface makes unified access to Id, Type and String representations of item that should be reviewed.


user adds Review to ReviewRequest. 
When RequestForReview creates, int the same time user Pocess creates with reviewRequestId as 

To form works use requestForReviewDto on requestedForReview Entity controller (page). 
