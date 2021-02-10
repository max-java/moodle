package by.jrr.profile.repository;

import java.time.LocalDate;

public interface SubscriptionsStatisticViewDto {

    Long getProfile_id();
    String getTitle();
    String getName();
    String getLast_name();
    LocalDate getDate_start();
    LocalDate getDate_end();
    Boolean getOpen_for_enroll();
    Integer getTOTAL_REQUESTED();
    Integer getTOTAL_APPROVED();
    Integer getTOTAL_REJECTED();
    Integer getTOTAL_CANCELED();

}
