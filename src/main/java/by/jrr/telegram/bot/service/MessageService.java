package by.jrr.telegram.bot.service;

import by.jrr.telegram.bot.JavaQuestionBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageService {

    @Autowired
    JavaQuestionBot javaQuestionBot;


    public void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            javaQuestionBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);
        try {
            javaQuestionBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
