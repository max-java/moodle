package by.jrr.crm.bean;

import by.jrr.crm.common.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteItem implements History {
    @Id
    @GeneratedValue
    Long id;
    Long profileId;
    LocalDateTime timestamp;
    @Column(columnDefinition = "TEXT")
    String text;
    @Enumerated(value = EnumType.STRING)
    HistoryType type;

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    @Override
    public LocalDateTime getDate() {
        return timestamp;
    }
}
