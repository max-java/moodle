package by.jrr.telegram.bot.processor.hello;

import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import by.jrr.registration.bean.StudentActionToLog;
import by.jrr.registration.service.StudentActionToLogService;
import by.jrr.telegram.bot.Chat;
import by.jrr.telegram.bot.processor.Processor;
import by.jrr.telegram.bot.repository.TgUserRepository;
import by.jrr.telegram.bot.service.MessageService;
import by.jrr.telegram.model.TgUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SayHelloNewUserProcessor implements Processor {

    @Autowired
    SayHelloNewUserService sayHelloNewUserService;
    @Autowired
    MessageService messageService;
    @Autowired
    StudentActionToLogService studentActionToLogService;
    @Autowired
    ProfileService profileService;
    @Autowired
    TgUserRepository tgUserRepository;

    @Override
    public String run() {
        return "Давай пойдем с Максом в гугл!";
    }

    public void process(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            findLatestTelegramChatActionAndBindProfile(message);
            Optional<TgUser> tgUserOptional = getTgUserFromMessageFromNewChatMember(message);
            if (tgUserOptional.isPresent()) {
                if (update.getMessage().getChatId().equals(Chat.JG_ALUMNI)) {
                    String response = getHelloMessageWhenUserJoinsAlumniChat(tgUserOptional.get());
                    messageService.sendMessage(Chat.JG_ALUMNI, response);
                } else {
                    TgUser tgUser = tgUserOptional.get();
                    String response = getHelloMessageWhenUserJoinsStreamChat(tgUser);
                    if(!tgUser.getIsBot()) {
                        messageService.sendMessage(tgUser, response);
                    }
                    messageService.sendMessage(Chat.JRR_BY, getNotificationMessageForAdmin(tgUser, message)); // TODO: 29/07/20 Just for test purpouses
                    messageService.sendMessage(Chat.CURATOR_JG_MINSK, getNotificationMessageForAdmin(tgUser, message)); // TODO: 29/07/20 Just for test purpouses
                    sendIntroductionMessage(message, tgUser);
                }
            }
        }
    }

    private void findLatestTelegramChatActionAndBindProfile(Message message) { // TODO: 29/07/20 consider to publish this as a broadcasting event
        LocalDateTime messageTimestamp =
                Instant.ofEpochMilli(message.getDate())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
        Optional<StudentActionToLog> studentActionToLog = studentActionToLogService.findLastActionBeforeTimestamp(messageTimestamp);
        if (studentActionToLog.isPresent()) {
            Long id = studentActionToLog.get().getStudentProfileId();
            if (!isTgUserIsPresentForProfileId(id).isPresent()) {
                TgUser tgUser = getTgUserFromMessageFromNewChatMember(message).orElseGet(TgUser::new);
                tgUser.setProfileId(id);
                tgUserRepository.save(tgUser);
            }
        }
    }

    private Optional<TgUser> getTgUserFromMessageFromNewChatMember(Message message) {
        if (message.getNewChatMembers() != null && message.getNewChatMembers().size() > 0) {
            for (User user : message.getNewChatMembers()) {
                return Optional.of(new TgUser(user));
            }
        }
        return Optional.empty();
    }

    private Optional<Profile> isTgUserIsPresentForProfileId(Long id) {
        return profileService.findProfileByProfileId(id);
    }

    private String getHelloMessageWhenUserJoinsStreamChat(TgUser user) {
        StringBuffer response = new StringBuffer("Привет, " + user.getFirstName());
        response.append("\nОсновой чат, где зависают все - Alumni: https://t.me/joinchat/CxUOGRRNPKd3vDxzt9YI4A\n");
        response.append("Если хочешь разобраться что и как у нас в комьюнити, получить отзыв о курсах из первых уст - спрашивай в Alumni, не стесняйся!\n");

        response.append("\nЯ помогу тебе сориентироваться. Если ты услышишь от ребят в чате или на лекции какой-нибудь непонятный термин,");
        response.append("например \"запушить в гит\", просто напиши \n");
        response.append("\n/whatIs запушить в гит\n");
        response.append("\nЕсли я не знаю ответ - я спрошу у Макса и расскажу тебе позже )");
        response.append("\n");
        response.append("Смотри наш челлендж - дорога в google!\n");
        response.append("Подробности здесь: https://www.instagram.com/mikas.sh/\n");
        return response.toString();
    }

    private String getHelloMessageWhenUserJoinsAlumniChat(TgUser user) {
        StringBuffer response = new StringBuffer("Hello, " + user.getFirstName());
        response.append("\nI'm the BOSS here. The first point to escalate.\n");
        response.append("Alumni on the way to google: https://www.instagram.com/mikas.sh/\n");
        return response.toString();
    }

    private String getRandomIntroductionMessage(TgUser user) {
        StringBuffer response = new StringBuffer("Привет, " + user.getFirstName()+"\n");
        List<String> msg = Arrays.asList(
                "Расскажи немного о себе? Например, сколько лет, чем занимаешься?",
                "Тебе понравилася наш рекламый пост?",
                "У нас на сайте есть проект, на которых можно сразу работать пока учишься.\nучиться работать, так сказать )",
                "Советую подписаться на инстик Макса. https://www.instagram.com/mikas.sh/\n",
                "Я надеюсь, что ты за движуху. Потому что тут движуха!\n",
                "расскажи, какие ожидания от курса?",
                "На мудл можно поменять аватарку )",
                "Здесь мы друг-друга поддерживаем и друг-другу помогаем. Но не забывай поддерживать других, что бы получить поддержку самому.",
                "Меня можно спрашивать про непонятные слова. например \"мудл\", просто напиши \n\n/whatIs мудл\n", // TODO: 29/07/20 обрабатывать знаки вопроса
                "как настроение? Будем покорять гугОл?");
        int index = ThreadLocalRandom.current().nextInt(0, msg.size() - 1);
        response.append(msg.get(index));
        return response.toString();
    }
    private void sendIntroductionMessage(Message message, TgUser tgUser) {
        new Thread(() -> {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageService.sendMessage(message, getRandomIntroductionMessage(tgUser));
        }).start();
    }

    private String getNotificationMessageForAdmin(TgUser tgUser, Message message) {
        return "first: " + tgUser.getFirstName()
                + "\n last: " + tgUser.getLastName()
                + "\n nick: " + tgUser.getUserName()
                + "\n joined chat - " + message.getChat().getTitle();
    }

}
