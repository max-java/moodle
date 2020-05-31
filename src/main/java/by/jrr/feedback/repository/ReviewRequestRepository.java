package by.jrr.feedback.repository;

import by.jrr.feedback.bean.ReviewRequest;
import by.jrr.feedback.bean.ReviewResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRequestRepository extends PagingAndSortingRepository<ReviewRequest, Long> {

}
