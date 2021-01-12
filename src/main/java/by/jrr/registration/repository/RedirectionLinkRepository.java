package by.jrr.registration.repository;

import by.jrr.registration.bean.RedirectionLink;
import by.jrr.registration.bean.RedirectionLinkStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface RedirectionLinkRepository extends
        CrudRepository<RedirectionLink, String>,
        JpaSpecificationExecutor<RedirectionLink> {

    //    @Query("update RedirectionLink set status = :status where uuid = :uuid")
    @Modifying
    @Query(value = "UPDATE `redirection_link` SET `status` = :status WHERE `uuid` = :uuid",
            nativeQuery = true)
    void updateStatus(@Param("status") String status, @Param("uuid") String uuid);

    Iterable<RedirectionLink> findAllByStudentProfileIdOrderByTimestamp(Long id);
    Iterable<RedirectionLink> findAllByStreamTeamProfileIdOrderByTimestamp(Long id);
}
