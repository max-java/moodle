package by.jrr.feedback.service;

import by.jrr.feedback.bean.Review;
import by.jrr.feedback.repository.ReviewRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ProfileService profileService;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findAllByReviewRequestId(Long id) {
        return reviewRepository.findByReviewRequestId(id).stream()
                .map(review -> setReviewerProfileToReviewByProfileId(review))
                .collect(Collectors.toList());
    }

    private Review setReviewerProfileToReviewByProfileId(Review review) {
        if (review.getReviewerProfileId() != null) {
            review.setReviewerProfile(profileService.findProfileByProfileId(review.getReviewerProfileId()).orElseGet(Profile::new));
        }
        return review;
    }
}
