package by.jrr.telegram.bot.processor.term;

import by.jrr.telegram.bot.bean.NerdTermLibrary;
import by.jrr.telegram.bot.processor.Authority;
import by.jrr.telegram.bot.processor.Processor;
import by.jrr.telegram.bot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SaveTermProcessor implements Processor {

    @Autowired
    NerdTermService nerdTermService;
    @Autowired
    MessageService messageService;
    @Autowired
    Authority authority;

    @Override
    public String run() {
        return "y меня завелись жуки";
    }

    public void process(Update update) {
        String response = "Я не запомнил. Либо ты не сказал, что это, либо у меня завелись жуки. Я запоминаю так:\n /запомни: Мир - это то, что вокруг нас.";
        Message msg = update.getMessage();
        if(authority.check(msg)) {
            if (msg.hasText()) {
                final String text = msg.getText();
                NerdTermLibrary nerdTerm = new NerdTermLibrary();
                nerdTerm.setTerm(text.substring(text.indexOf(":")+":".length(), text.indexOf("- это")).trim());
                nerdTerm.setDefinition(text.substring(text.indexOf("- это")+"- это".length()).trim());
                nerdTermService.saveLearned(nerdTerm);
                response = "Запомнил: "+nerdTerm.getTerm()+"\n"+nerdTerm.getDefinition();

            }
        } else {
            response = randomText();
        }

        messageService.sendMessage(msg, response);
    }
    private String randomText() {
        List<String> msg = Arrays.asList(
                "Не думаю, что это так.",
                "Твой опыт еще не прокачан, я все еще умнее тебя.",
                "Сначала научи себя, потом разрешу тебе учить меня.",
                "Давай спросим еще кого-нибудь, что бы убедиться в правильности определения.");
        int index = ThreadLocalRandom.current().nextInt(0, msg.size() - 1);
        return msg.get(index);
    }
}
