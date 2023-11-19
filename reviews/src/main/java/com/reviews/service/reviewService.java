package com.reviews.service;


import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.ReviewDTO;
import com.reviews.module.commentDTO;
import com.reviews.repository.reviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class reviewService {

    @Autowired
    private reviewRepository reviewRepository;


    public double getProductRating(Long productID){
        return reviewRepository.findAverageRatingByProductID(productID);
    }
    public Page<ReviewByProductDTO> getReviewsByProductID(Long productID, Pageable pageable){
        Page<ReviewByProductDTO> reviews=reviewRepository.findReviewsByProductID(productID, pageable);
        return reviews;
    }

    public Page<ReviewByUserDTO> getReviewsByEmail(String email, Pageable pageable){
        Page<ReviewByUserDTO> reviews=reviewRepository.findByReviewsByEmail(email,pageable);
        return reviews;
    }

    public void createReview(commentDTO comment, Long productID, String email){
        ReviewDTO existingReview=reviewRepository.findByProductIDAndEmail(productID,email);
        if (existingReview != null) {
            throw new RuntimeException("User already commented");
        }
        ReviewDTO review = new ReviewDTO();
        review.setComment(comment.getComment());
        review.setProductID(productID);
        review.setEmail(email);
        review.setRating(comment.getRating());
        review.setTimestamp(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public void editReview(commentDTO comment, Long productID, String email){
        ReviewDTO existingReview=reviewRepository.findByProductIDAndEmail(productID,email);
        if (existingReview == null) {
            throw new RuntimeException("User comment does not exist");
        }
        existingReview.setComment(comment.getComment());
        existingReview.setTimestamp(LocalDateTime.now());
        existingReview.setRating(comment.getRating());

        reviewRepository.save(existingReview);
    }

    public void deleteReview(String userID, Long productID) {
        ReviewDTO existingReview=reviewRepository.findByProductIDAndEmail(productID,userID);
        if (existingReview == null) {
            throw new RuntimeException("Review does not exist");
        }
        reviewRepository.delete(existingReview);
    }
}
