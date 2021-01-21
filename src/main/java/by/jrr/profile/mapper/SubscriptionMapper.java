package by.jrr.profile.mapper;

import by.jrr.profile.bean.StreamAndTeamSubscriber;
import by.jrr.profile.model.SubscriptionDto;
import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.model.RedirectionLinkDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionMapper {

    SubscriptionMapper OF = Mappers.getMapper(SubscriptionMapper.class);

    StreamAndTeamSubscriber getStreamTeamSubscriberFrom(SubscriptionDto.Request request);
}
