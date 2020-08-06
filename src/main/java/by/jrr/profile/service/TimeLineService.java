package by.jrr.profile.service;

import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.repository.TimeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class TimeLineService {

    @Autowired
    TimeLineRepository timeLineRepository;

    public void save(TimeLine timeLine) {
        timeLineRepository.save(timeLine);
    }

    public Map<LocalDate, List<TimeLine>> getTimeLineByStreamId(Long streamTeamProfileId) {
        List<TimeLine> timeLineList = timeLineRepository.findAllByStreamTeamProfileId(streamTeamProfileId);
        Map<LocalDate, List<TimeLine>> result = timeLineList.stream()
                .collect(Collectors.groupingBy(dt -> dt.getDateTime().toLocalDate(), Collectors.toList()));
        Map<LocalDate, List<TimeLine>> sortedResult = new TreeMap<>(result);
        return sortedResult;
    }
}
