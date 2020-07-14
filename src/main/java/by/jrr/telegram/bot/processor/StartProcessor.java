package by.jrr.telegram.bot.processor;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StartProcessor implements Processor {

    @Override
    public String run() {
        StringBuffer msg = new StringBuffer();
        Arrays.stream(BotCommand.values())
                .forEach(command -> msg.append(command.getCommand() + " - " + command.getDescription()+"\n"));
        msg.append("\n");
        msg.append("Как я был создан: https://moodle.jrr.by/topic/2896\n");
        msg.append("Как меня сделать лучше: https://moodle.jrr.by/project/2904\n");

        return msg.toString();
    }
}
