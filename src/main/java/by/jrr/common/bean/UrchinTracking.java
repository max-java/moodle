package by.jrr.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrchinTracking {

    @Id
    @GeneratedValue
    Long id;
    private LocalDateTime timestamp;
    @ElementCollection
    @OrderColumn
    @Column(columnDefinition = "TEXT")
    private Map<String,String> allParams = new HashMap();

}
