package by.jrr.api.proxy;

import by.jrr.api.model.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "${message.service}")
public interface MessageProxy {

    @PostMapping("/messages")
    MessageDto postNewMessage(MessageDto message);

    @GetMapping("/api/messages/{chatToken}")
    List<MessageDto> getMessagesByChatToken(@PathVariable String chatToken);

}
