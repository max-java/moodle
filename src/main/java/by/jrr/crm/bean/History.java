package by.jrr.crm.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface History {

    Long getId();

    void setId(Long id);

    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    String getText();

    void setText(String text);

    Long getProfileId();

    void setProfileId(Long profileId);

    LocalDateTime getDate();

    default String getDateFormatted(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm, EEEE", new Locale("ru"));
        return localDateTime.format(formatter);
    }

}
