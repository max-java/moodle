package by.jrr.profile.mapper;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.model.SubscriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper( uses = SubscriptionParamMap.class )
public interface SubscriptionMapper {

    SubscriptionMapper OF = Mappers.getMapper(SubscriptionMapper.class);

    StreamAndTeamSubscriber getStreamTeamSubscriberFrom(SubscriptionDto.Request request);

    @Mappings({
            @Mapping(target = "subscriberProfileId", source = "paramMap", qualifiedBy = SubscriptionParamMap.UserProfileId.class),
            @Mapping(target = "streamTeamProfileId", source = "paramMap", qualifiedBy = SubscriptionParamMap.StreamTeamProfileId.class),
            @Mapping(target = "status", source = "paramMap", qualifiedBy = SubscriptionParamMap.Status.class),
            @Mapping(target = "notes", source = "paramMap", qualifiedBy = SubscriptionParamMap.Notes.class),
    })
    SubscriptionDto.Request getSubscriptionRequestFromMap(Map<String,String> paramMap);
}
