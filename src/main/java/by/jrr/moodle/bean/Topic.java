package by.jrr.moodle.bean;

import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.feedback.bean.EntityType;
import by.jrr.statistic.bean.Trackable;
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
public class Topic implements Trackable {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private String title;
    private String subtitle;
    @Lob
    private String text;

    @Override
    public EntityType getType() {
        return EntityType.TOPIC;
    }

    @Override
    public String getName() {
        return title;
    }
}
