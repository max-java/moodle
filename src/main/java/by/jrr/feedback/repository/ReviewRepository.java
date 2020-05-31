package by.jrr.feedback.repository;

import by.jrr.feedback.bean.Review;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
    List<Review> findByReviewRequestId(Long id);
}
