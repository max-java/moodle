package by.jrr.telegram.bot.processor.hello;

import by.jrr.telegram.bot.Chat;
import by.jrr.telegram.bot.bean.NerdTermLibrary;
import by.jrr.telegram.bot.processor.Processor;
import by.jrr.telegram.bot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class SayHelloNewUserProcessor implements Processor {

    @Autowired
    SayHelloNewUserService sayHelloNewUserService;
    @Autowired
    MessageService messageService;

    @Override
    public String run() {
        return "Давай пойдем с Максом в гугл!";
    }

    public void process(Update update) {
        StringBuffer response = new StringBuffer("Привет, ");
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.getNewChatMembers() != null && message.getNewChatMembers().size() > 0) {
                for (User user : message.getNewChatMembers()) {
                    response.append(user.getFirstName() + "!\n");
                }
            }
            if(!update.getMessage().getChatId().equals(Chat.JG_ALUMNI)) {
                response.append("\nОсновой чат, где зависают все - Alumni: https://t.me/joinchat/CxUOGRRNPKd3vDxzt9YI4A\n");
                response.append("Если хочешь разобраться что и как у нас в комьюнити, получить отзыв о курсах из первых уст - спрашивай в Alumni, не стесняйся!\n");
            }
            response.append("\nЯ помогу тебе сориентироваться. Если ты услышишь от ребят в чате или на лекции какой-нибудь непонятный термин,");
            response.append("например \"запушить в гит\", просто напиши \n");
            response.append("\n/whatIs запушить в гит\n");
            response.append("\nЕсли я не знаю ответ - я спрошу у Макса и расскажу тебе позже )");
            response.append("\n");
            response.append("Смотри наш челлендж - дорога в google!\n");
            response.append("Подробности здесь: https://www.instagram.com/mikas.sh/\n");

            messageService.sendMessage(message, response.toString());
        }
    }
}
