package by.jrr.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @javax.persistence.Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role", unique = true)
    @Enumerated(value = EnumType.STRING)
    private UserRoles role;
}
