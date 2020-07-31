package by.jrr.telegram.bot.service;

import by.jrr.auth.bean.User;
import by.jrr.telegram.bot.Chat;
import by.jrr.telegram.bot.JavaQuestionBot;
import by.jrr.telegram.bot.bean.TgContact;
import by.jrr.telegram.model.TgUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputContactMessageContent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

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
    public void sendMessage(TgUser tgUser, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(tgUser.getId().toString());
        sendMessage.setText(text);
        try {
            javaQuestionBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendHtmlMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);
        try {
            javaQuestionBot.execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendContactDataToAdministrator(String email, String firstName, String lastName, String userPhone) {
        System.out.println("I'm in sendContactDataToAdministrator ");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(email + " "+ firstName+" "+lastName + " " + userPhone);

        for(Long id : Arrays.asList(Chat.JRR_BY, Chat.CURATOR_JG_MINSK)){
            sendMessage.setChatId(id);
            try {
                javaQuestionBot.execute(sendMessage);
                System.out.println("javaQuestionBot.execute(sendMessage)");
            }
            catch (TelegramApiException e) {
                System.out.println("e.printStackTrace();");
                // TODO: 30/07/20 TelegramApiRequestException: Error sending message: [403] Forbidden: bot can't initiate conversation with a user
                e.printStackTrace();
            }
        }
        sendTgContactToAdministrator( email,  firstName,  lastName,  userPhone);
    }

    public void sendTgContactToAdministrator(String email, String firstName, String lastName, String userPhone) {
        System.out.println("I'm in send TgContact To Administrator ");
        TgContact contact = createContact( email,  firstName,  lastName,  userPhone);
        SendContact sendContact = new SendContact();
        sendContact.setPhoneNumber(contact.getPhoneNumber());
        sendContact.setFirstName(contact.getFirstName());
        sendContact.setLastName(contact.getLastName());
        sendContact.setPhoneNumber(contact.getPhoneNumber());
        sendContact.setvCard(contact.getVCard());


        for(Long id : Arrays.asList(Chat.JRR_BY, Chat.CURATOR_JG_MINSK)){
            sendContact.setChatId(id);
            try {
                javaQuestionBot.execute(sendContact);
                System.out.println("javaQuestionBot.execute(sendMessage)");
            }
            catch (TelegramApiException e) {
                System.out.println("e.printStackTrace();");
                // TODO: 30/07/20 TelegramApiRequestException: Error sending message: [403] Forbidden: bot can't initiate conversation with a user
                e.printStackTrace();
            }
        }
    }

    public TgContact createContact(String email, String firstName, String lastName, String userPhone) {
        return TgContact.builder()
                .phoneNumber(userPhone)
                .firstName(firstName)
                .lastName(lastName)
                .userID(null)
                .vCard("BEGIN:VCARD\n" +
                        "VERSION:3.0\n" +
                        "FN:"+firstName+"\n" +
                        "N:"+lastName+"\n" +
                        "ORG:"+lastName+"\n" +
                        "TEL;MOBILE:"+userPhone+"\n" +
                        "EMAIL;TYPE=INTERNET:"+email+"\n" +
                        "END:VCARD").build();
    }
}
