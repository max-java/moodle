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
            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(10, 00)) && now.isBefore(LocalTime.of(20, 00))) {
                QAndA qAndA = qAndAService.getRandomQuestion();
                String text = qAndA.getQuestion();
                messageService.sendMessage(Chat.JG_ALUMNI, text);
                text = qAndA.getAnswer();
                messageService.sendMessage(Chat.JG_ALUMNI, text);
            }
            try {
                Thread.sleep(28800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startBroadcasting() {
        if (!isBroadcasting) {
            isBroadcasting = true;
            broadcast();
        }
    }
}
