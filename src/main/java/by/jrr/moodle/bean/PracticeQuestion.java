package by.jrr.moodle.bean;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.Reviewable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticeQuestion implements Reviewable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private String name;
    private String summary;
    @Lob
    private String description;
    @Lob
    private String reproSteps;

    @Override
    public EntityType getType() {
        return EntityType.PRACTICE_QUESTION;
    }
}
