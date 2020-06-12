package by.jrr.feedback.repository;

import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.ReviewResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRequestRepository extends PagingAndSortingRepository<ReviewRequest, Long> {
        List<ReviewRequest> findAllByRequesterProfileId(Long id);
}
