package by.jrr.message.service;

import by.jrr.api.model.MessageDto;
import by.jrr.api.model.MessageStatus;
import by.jrr.api.model.MessageType;
import by.jrr.api.model.UserContactsDto;
import by.jrr.api.proxy.MessageProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired (required = false)
    MessageProxy messageProxy;

    Logger log = LoggerFactory.getLogger(MessageService.class); // TODO: 04/11/2020 moveto bean

    public void sendMessageDtoWitContactData(UserContactsDto userContactsDto, Long profileId) {
        log.info("trying to send userContactsDto: {}", userContactsDto.toString());
        try {
            MessageDto mess = MessageDto.builder()
                    .messageText(objectMapper.writeValueAsString(userContactsDto))
                    .build();

            mess.setTelegramStatus(MessageStatus.NEW);
            mess.setMessageType(MessageType.CONTACT_DATA);
            mess.setUserProfileId(profileId);
            messageProxy.postNewMessage(mess);
            log.info("MessageDtoWitContactData has been sent: {}", mess.toString());
        } catch (JsonProcessingException e) {
            log.info(JsonProcessingException.class.toString());
            e.printStackTrace();
            // TODO: 02/11/2020 consider to log and create backup strategy
        } catch (Exception e) {
            log.info(Exception.class.toString());
            e.printStackTrace();
            // TODO: 02/11/2020 consider to log and create backup strategy
        }
    }
}
