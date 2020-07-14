package by.jrr.telegram.bot.processor.term;

import by.jrr.telegram.bot.bean.NerdTermLibrary;
import by.jrr.telegram.bot.processor.Processor;
import by.jrr.telegram.bot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class NerdTermProcessor implements Processor {

    @Autowired
    NerdTermService nerdTermService;
    @Autowired
    MessageService messageService;

    @Override
    public String run() {
        return "Nothing I can help you now, sorry...";
    }

    public void process(Update update) {
        Message msq = update.getMessage();
        System.out.println("msq.getChatId() = " + msq.getChatId());
        String response = "я ничего не понял";
        if (msq.hasText()) {
            final String text = msq.getText();
            NerdTermLibrary nerdTerm = nerdTermService.getByTermIfPresent(text).orElseGet(() -> nerdTermService.saveToLearn(text));
            response = nerdTerm.getTerm()+"\n"+nerdTerm.getDefinition();

        }
        messageService.sendMessage(msq, response);
    }
}
