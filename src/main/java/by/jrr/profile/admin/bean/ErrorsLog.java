package by.jrr.profile.admin.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CollectionType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ErrorsLog {

    @Id
    @GeneratedValue
    Long id;
    private LocalDateTime timestamp;
    private String userDto;
    private String exName;
    private String exMessage;
    @ElementCollection
    @OrderColumn
    @Column(columnDefinition = "TEXT")
    private List<String> cause = new ArrayList<>();
    @ElementCollection
    @OrderColumn
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private StackTraceElement[] exStacktrace;

}
