package by.jrr.profile.service;

import by.jrr.profile.bean.TimeLine;
import by.jrr.registration.bean.EventType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Sql("/profile.sql")
@SpringBootTest
class TimeLineNotificationServiceTest {

    @Autowired
    TimeLineNotificationService timeLineNotificationService;

    @Test
    void findTimeLineItemsByEventDay() {
        List actualTimeLines = timeLineNotificationService.findTimeLineItemsByEventDay(EventType.LECTURE, LocalDate.parse("2020-08-08"));
        Assertions.assertThat(makeTimeLine()).containsSequence(actualTimeLines);
    }

    private List makeTimeLine() {
        List<TimeLine> timeLines = new ArrayList<>();
        timeLines.add(TimeLine.builder()
                .Id(1512L)
                .timelineUUID("2b2d6ecb-9919-4cc2-917c-448e9fdc000d")
                .dateTime(LocalDateTime.parse("2020-08-08T19:00:00.000000"))
                .streamTeamProfileId(1324L)
                .courseId(1286L)
                .lectureId(1265L)
                .urlToRedirect("https://us02web.zoom.us/j/88181036762")
                .eventType(EventType.LECTURE)
                .eventName("Lecture 2")
                .notes("это лекция 2")
                .build());
        return timeLines;
    }
}
