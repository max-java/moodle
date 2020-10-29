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
public class ServiceMessage {
    private UUID uuid;
    private LocalDateTime timeStamp;
    private String messageText;
    private MessageStatus telegramStatus;
    private MessageStatus eMailStatus;
}

