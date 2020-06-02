package by.jrr.portfolio.bean;

import by.jrr.constant.Endpoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

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
    @GeneratedValue
    private Long Id;
    private String name;
    private String description;
}
