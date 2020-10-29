package by.jrr.api.proxy;

import by.jrr.api.model.ServiceMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${message.service}")
public interface MessageProxy {

    @PostMapping("/messages")
    ServiceMessage postNewMessage(ServiceMessage message);
}
