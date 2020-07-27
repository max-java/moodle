package by.jrr.telegram.bot.processor;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class Authority {

    public boolean check(Message msg) {
        return msg.getFrom().getUserName() != null && msg.getFrom().getUserName().equals("jrrby");
    }

}
