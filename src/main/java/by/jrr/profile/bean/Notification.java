package by.jrr.profile.bean;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Video;

public class Notification {

    @Getter
    public enum Type {
        TODAY_EVENT("Сегодня у тебя занятие!"),
        TOMORROW_EVENT("Завтра у тебя занятие!"),
        REDIRECTION_LINK("Ссылка на трансляцию."),
        FEEDBACK_EXTERNAL_FORM("We Agile! Нам важно твое мнение."),
        VIDEO_EVENT("Запись лекции доступна на платформе."),
        CUSTOM_FIRST_LECTURE("Первое занятие уже в Среду! Вы готовы!?");

        String subjectText;

        Type(String subjectText) {
            this.subjectText = subjectText;
        }

    }

    public enum Status {
        NEW, SENT, ERROR
    }
}
