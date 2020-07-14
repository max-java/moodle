package by.jrr.telegram.bot.service;

import by.jrr.interview.bean.QAndA;
import by.jrr.interview.service.QAndAService;
import by.jrr.telegram.bot.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
                QAndA qAndA = qAndAService.getRandomQuestion()
                        .orElseGet(() -> QAndA.builder()
                                .question("Как подготовиться к собесу?")
                                .answer("Job Interview Questions на сайте JavaGuru")
                                .build());
                String text = qAndA.getQuestion();
                messageService.sendMessage(Chat.JG_ALUMNI, text);
                text = qAndA.getAnswer();
                messageService.sendMessage(Chat.JG_ALUMNI, text);
            }
            try {
                System.out.println("\"broadcast is sleeping\" = " + "broadcast is sleeping");
                Thread.sleep(28800000);
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
}
