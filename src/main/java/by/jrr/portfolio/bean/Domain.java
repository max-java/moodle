package by.jrr.portfolio.bean;

import by.jrr.constant.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * This is Project class analog for user outer pages and articles
 * */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Domain {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private String name;
    private String description;
}
