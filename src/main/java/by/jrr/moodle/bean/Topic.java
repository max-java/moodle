package by.jrr.moodle.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Topic {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;
    private String title;
    private String subtitle;
    @Lob
    private String text;
}
