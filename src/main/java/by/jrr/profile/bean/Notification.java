package by.jrr.profile.bean;

import lombok.Getter;

public class Notification {

    @Getter
    public enum Type {
        TODAY_EVENT("Сегодня у тебя занятие!"),
        TOMORROW_EVENT("Завтра у тебя занятие!"),
        REDIRECTION_LINK("Ссылка на трансляцию");

        String subjectText;

        Type(String subjectText) {
            this.subjectText = subjectText;
        }

    }

    public enum Status {
        NEW, SENT, ERROR
    }
}
