package by.jrr.moodle.bean;

import by.jrr.feedback.bean.EntityType;
import by.jrr.statistic.bean.Trackable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecture implements Trackable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private String title;
    private String subtitle;
    @Lob
    private String text;
    @ManyToMany
    private List<PracticeQuestion> practiceQuestions;

    @Override
    public EntityType getType() {
        return EntityType.LECTURE;
    }

    @Override
    public String getName() {
        return title;
    }

    public String getFullName() {
        return String.format("%s - %s", title, subtitle);
    }
}
