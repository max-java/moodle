package by.jrr.profile.bean;

import by.jrr.registration.bean.EventType;
import lombok.Data;

@Data
public class ChatButtonDto {
    private Long studentProfileId; //not present in from. When user click - it grabs in /l/ endpoint.
    private Long streamTeamProfileId; // streamTeamId in form
    private Long courseId;
    private String urlToRedirect; // link in form
    private EventType eventType;  // evenType on button in form
    private String eventName; // link name in form
}
