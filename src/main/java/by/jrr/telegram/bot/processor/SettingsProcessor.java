package by.jrr.telegram.bot.processor;

import org.springframework.stereotype.Service;

@Service
public class SettingsProcessor implements Processor {

    @Override
    public String run() {
        return "magic is happening";
    }
}
