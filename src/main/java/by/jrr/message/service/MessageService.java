package by.jrr.message.service;

import by.jrr.api.model.MessageDto;
import by.jrr.api.model.MessageStatus;
import by.jrr.api.model.MessageType;
import by.jrr.api.model.UserContactsDto;
import by.jrr.api.proxy.MessageProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageProxy messageProxy;

    public void sendMessageDtoWitContactData(UserContactsDto userContactsDto, Long profileId) {
        try {
            MessageDto mess = MessageDto.builder()
                    .messageText(objectMapper.writeValueAsString(userContactsDto))
                    .build();

            mess.setTelegramStatus(MessageStatus.NEW);
            mess.setType(MessageType.CONTACT_DATA);
            mess.setUserProfileId(profileId);
            messageProxy.postNewMessage(mess);
        } catch (JsonProcessingException e) {
            // TODO: 02/11/2020 consider to log and create backup strategy
        } catch (Exception e) {
            // TODO: 02/11/2020 consider to log and create backup strategy
        }
    }
}
