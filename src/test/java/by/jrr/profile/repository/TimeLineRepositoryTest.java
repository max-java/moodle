package by.jrr.profile.repository;

import by.jrr.profile.bean.TimeLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

class TimeLineRepositoryTest {

    @Autowired
    TimeLineRepository timeLineRepository;

    @Test
    public void timestampShouldBeGenerated() throws Exception {
        TimeLine persistedTimeline = timeLineRepository.save(new TimeLine());

        Instant timestamp = persistedTimeline.getTimeStamp();
        Long id = persistedTimeline.getId();

        persistedTimeline.setNotes("Some Notes");

        TimeLine updatedTimeline = timeLineRepository.save(persistedTimeline);
        TimeLine newPersistedTimeline = timeLineRepository.findById(id).get();

        assertThat(timestamp).isNotNull();
        assertThat(persistedTimeline.getTimeStamp().toEpochMilli()).isEqualTo(timestamp.toEpochMilli());
        assertThat(updatedTimeline.getTimeStamp().toEpochMilli()).isEqualTo(timestamp.toEpochMilli());
        assertThat(newPersistedTimeline.getTimeStamp().toEpochMilli()).isEqualTo(timestamp.toEpochMilli());
    }

    @Test
    public void justProving() {
        TimeLine timeLineNew = new TimeLine();
        TimeLine timeLineOld = new TimeLine();
        TimeLine timeLineNull = new TimeLine();

        timeLineNew.setTimeStamp(Instant.now());
        timeLineOld.setTimeStamp(Instant.MIN);
        timeLineOld.setTimeStamp(Instant.MIN);

        List<TimeLine> events = new ArrayList<>();
        events.add(timeLineNew);
        events.add(timeLineOld);
        events.add(timeLineNull);

        for (TimeLine timeLine : events) {
            if(timeLine.getTimeStamp() == null) {
                timeLine.setTimeStamp(Instant.MIN);
            }
        }

        List<TimeLine> filteredEvents = events.stream()
                .filter(timeLine -> timeLine.getTimeStamp().isBefore(Instant.now().minusSeconds(3600)))
                .collect(Collectors.toList());

        assertThat(filteredEvents.get(0).getTimeStamp()).isEqualTo(Instant.MIN);
        assertThat(filteredEvents.get(1).getTimeStamp()).isEqualTo(Instant.MIN);
        assertThat(filteredEvents.size()).isEqualTo(2);
    }

}
