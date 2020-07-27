package by.jrr.profile.bean;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserProfileStatisticDTO implements Comparable<UserProfileStatisticDTO> {

    String userFirstAndLastName;
    List<String> lectures = new ArrayList<>();
    List<String> feedbacks = new ArrayList<>();
    List<LocalDate> standups = new ArrayList<>();

    @Override
    public int compareTo(UserProfileStatisticDTO o) {
        if ((lectures.size() - o.lectures.size()) == 0) {
            if ((standups.size() - o.standups.size()) == 0) {
                return feedbacks.size() - o.feedbacks.size();
            }
            return standups.size() - o.standups.size();
        }
        return lectures.size() - o.lectures.size();
    }
    public String printLectures() {
        return lectures.stream()
                .collect(Collectors.joining(", ", "", ""));
    }
    public String printFeedbacks() {
        return feedbacks.stream()
                .collect(Collectors.joining(", ", "", ""));
    }
    public String printStandups() {
        return standups.stream()
                .map(a -> a.toString())
                .collect(Collectors.joining(", ", "", ""));
    }
}
