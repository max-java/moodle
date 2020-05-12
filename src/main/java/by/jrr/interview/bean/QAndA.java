package by.jrr.interview.bean;

import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QAndA {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private String question;
    @Lob
    private String answer;
    public String getLink() { // TODO: 11/05/20 model should be divided from view
        return Endpoint.Q_AND_A+"/"+this.getId();
    }
}
