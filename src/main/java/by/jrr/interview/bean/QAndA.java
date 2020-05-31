package by.jrr.interview.bean;

import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.EntityType;
import by.jrr.statistic.bean.Trackable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QAndA implements Trackable {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private String theme;
    @Column(columnDefinition = "TEXT")
    private String question;
    @Lob
    private String answer;
    public String getLink() { // TODO: 11/05/20 model should be divided from view
        return Endpoint.Q_AND_A+"/"+this.getId();
    }

    @Override
    public EntityType getType() {
        return EntityType.INTERVIEW_QUESTION;
    }

    @Override
    public String getName() {
        return ""; // TODO: 30/05/20 consider for names
    }
}
