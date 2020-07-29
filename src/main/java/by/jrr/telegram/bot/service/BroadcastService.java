package by.jrr.telegram.bot.service;

import by.jrr.interview.bean.QAndA;
import by.jrr.interview.service.QAndAService;
import by.jrr.telegram.bot.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BroadcastService {

    @Autowired
    QAndAService qAndAService;
    @Autowired
    MessageService messageService;

    private boolean isBroadcasting = false;

    public void broadcast() {
        while (true) {
            System.out.println("\"while true cycling\" = " + "while true cycling"); // TODO: 14/07/20 replace with logs
            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(10, 00)) && now.isBefore(LocalTime.of(20, 00))) {
                System.out.println("broadcast - it is right time to send message");
                if (new Random().nextBoolean()) {
                    QAndA qAndA = qAndAService.getRandomQuestion()
                            .orElseGet(() -> QAndA.builder()
                                    .question("Как подготовиться к собесу?")
                                    .answer("Job Interview Questions на сайте JavaGuru")
                                    .build());

                    String text = qAndA.getQuestion();
                    messageService.sendMessage(Chat.JG_ALUMNI, text);
                    text = qAndA.getAnswer();
                    messageService.sendHtmlMessage(Chat.JG_ALUMNI, text);
                } else {
                    messageService.sendHtmlMessage(Chat.JG_ALUMNI, getRandomText());
                }
            }
            try {
                System.out.println("\"broadcast is sleeping\" = " + "broadcast is sleeping");
                Thread.sleep(28800000 / 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startBroadcasting() {
        if (!isBroadcasting) {
            isBroadcasting = true;
            System.out.println("\"start broadcasting\" = " + "start broadcasting");
            broadcast();
        }
    }

    private String getRandomText() {
        List<String> msg = Arrays.asList(
                "какие планы на выходные?",
                "Кто уже поставил цель по SMART?",
                "В курсе, что Макс может подготовить к собесу?",
                "Найди ментора. Не тупи.",
                "Требую минутку обнимания!",
                "Я хочу поблагодарить себя за веру в себя.",
                "Самое важное, что вы можете сделать для себя - быть счастливыми",
                "Если захочется найти человека, который поможет преодолеть любую проблему, посмотри в зеркало и скажи \"Привет\"",
                "Скучно. Поговори со мной...");
        int index = ThreadLocalRandom.current().nextInt(0, msg.size() - 1);
        return msg.get(index);
    }


}
