package by.jrr.moodle.bean;

import by.jrr.constant.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private String title;
    private String subtitle;
    @Lob
    private String text;
    public String getLink() { // TODO: 11/05/20 model should be divided from view
        return View.TOPIC+"/"+this.getId();
    }
}
