package by.jrr.message.api;

import by.jrr.api.model.MessageDto;
import by.jrr.api.model.MessageStatus;
import by.jrr.api.model.MessageType;
import by.jrr.api.proxy.MessageProxy;
import by.jrr.constant.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class MessageController {

    @Autowired(required = false)
    MessageProxy messageProxy;

    @PostMapping(value = Endpoint.CRM_MESSAGES, consumes = "application/json")
    public void sendMessage(@RequestBody MessageDto messageDto) {
        messageDto.setTelegramStatus(MessageStatus.NEW);
        messageDto.setMessageType(MessageType.MESSAGE);
        messageProxy.postNewMessage(messageDto);
    }

    @GetMapping(value = Endpoint.CRM_MESSAGES+"/{chatToken}", produces = "application/json")
    public List<MessageDto> getMessagesByChatToken(@PathVariable String chatToken) {
        try {
            return messageProxy.getMessagesByChatToken(chatToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
}
