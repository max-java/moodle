package by.jrr.telegram.bot.processor;

import org.springframework.stereotype.Service;

@Service
public class HelpProcessor implements Processor {

    @Override
    public String run() {
        return "Magic is happening";
    }
}
