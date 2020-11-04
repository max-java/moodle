package by.jrr.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String uuid;
    private LocalDateTime timeStamp;
    private LocalDateTime lastUpdate;

    String chatToken;
    Long userProfileId;


    private MessageStatus telegramStatus;
    private MessageStatus eMailStatus;

    private String messageText;
    private MessageType messageType;

}
