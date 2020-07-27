package by.jrr.library.bean;

import by.jrr.feedback.bean.EntityType;
import by.jrr.feedback.bean.Reviewable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Reviewable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private String name;
    private String img;
    private String author;
    private String edition;
    private String publisher;
    private String published;
    private String isbn;
    private String description;

    @Override
    public EntityType getType() {
        return EntityType.BOOK;
    }
}
