package by.jrr.common.repository;

import by.jrr.common.bean.UrchinTracking;
import by.jrr.constant.Endpoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = Endpoint.URCHIN_TRACKING_REST)
public interface UrchinTrackingRepository extends PagingAndSortingRepository<UrchinTracking, Long> {
}
