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

    @Override
    public EntityType getType() {
        return EntityType.INTERVIEW_QUESTION;
    }

    @Override
    public String getName() {
        return ""; // TODO: 30/05/20 consider for names
    }

    // TODO: 31/05/20 see QAndAService.class comment wich is  // TODO: 31/05/20 see QAndA.class comment
    public String getTheme() {
        if (this.theme == null) {
            theme = "";
        }
        return theme;
    }
}
