package by.jrr.telegram.bot.processor.term;

import by.jrr.telegram.bot.bean.NerdTermLibrary;
import by.jrr.telegram.bot.processor.Authority;
import by.jrr.telegram.bot.processor.Processor;
import by.jrr.telegram.bot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class DeleteTermProcessor implements Processor {

    @Autowired
    NerdTermService nerdTermService;
    @Autowired
    MessageService messageService;
    @Autowired
    Authority authority;

    @Override
    public String run() {
        return "Nothing I can help you now, sorry...";
    }

    public void process(Update update) {
        String response = "Это невозможно забыть ...";
        Message msg = update.getMessage();
        if(authority.check(msg)) {
            if (nerdTermService.deleteByTermAndDefinition(
                    msg.getReplyToMessage().getText().split("\n")[0],
                    msg.getReplyToMessage().getText().split("\n")[1]
            )) {
                response = "я забыл, что " + msg.getReplyToMessage().getText().split("\n")[0] + " - это " + msg.getReplyToMessage().getText().split("\n")[1];
            }
        }
        messageService.sendMessage(msg, response);
    }
}
