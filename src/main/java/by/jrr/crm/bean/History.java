package by.jrr.crm.bean;

import java.time.LocalDateTime;

public interface History {

    Long getId();

    void setId(Long id);

    LocalDateTime getTimestamp();

    void setTimestamp(LocalDateTime timestamp);

    String getText();

    void setText(String text);

    Long getProfileId();

    void setProfileId(Long profileId);

}
