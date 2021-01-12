package by.jrr.registration.mapper;

import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.model.CreateRedirectionLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RedirectionLinkMapper {

    RedirectionLinkMapper OF = Mappers.getMapper(RedirectionLinkMapper.class);

    @Mapping(target = "expirationMinutes", source = "expirationMinutes")
    RedirectionLink getRedirectionLinkFromRequest(CreateRedirectionLink.Request request);

    @Mapping(target = "timestamp", ignore = true)
    StudentActionToLog getStudentActionToLogFromRedirectionLink(RedirectionLink redirectionLink);
}
