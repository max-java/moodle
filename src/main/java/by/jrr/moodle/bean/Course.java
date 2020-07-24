package by.jrr.moodle.bean;

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
public class Course {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private String title;
    private String subtitle;
    @Lob
    private String text;
    @Transient
    private List<CourseToLecture> courseToLectureIds;
    private String imgSrc;

    public String getImgSrc() {
        if (imgSrc.isEmpty()) {
            imgSrc = "/dist/img/courseDefault.jpg";
        }
        return this.imgSrc;
    }



}
