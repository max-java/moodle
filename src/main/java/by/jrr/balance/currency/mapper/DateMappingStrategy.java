package by.jrr.balance.currency.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateMappingStrategy {
    public LocalDate asLocalDate(LocalDateTime date) {
        return date != null ? LocalDate.from(date) : null;
    }

    public LocalDateTime asLocalDateTime(LocalDate date) {
        return date != null ? LocalDateTime.from(date) : null;
    }
}

