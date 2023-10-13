package com.reviews.service;


import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.ReviewDTO;
import com.reviews.repository.reviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class reviewService {

    @Autowired
    private reviewRepository reviewRepository;

    public List<ReviewByProductDTO> getReviewsByProductID(Long productID){
        List<ReviewByProductDTO> reviews=reviewRepository.findReviewsByProductID(productID);
        return reviews;
    }

    public List<ReviewByUserDTO> getReviewsByuserID(Long userID){
        List<ReviewByUserDTO> reviews=reviewRepository.findByReviewsByUserID(userID);
        return reviews;
    }

    public void createReview(String comment, Long productID){
        Long userID= 10L;
        ReviewDTO existingReview=reviewRepository.findByProductIDAndUserID(productID,userID);
        if (existingReview != null) {
            throw new RuntimeException("User already commented");
        }
        ReviewDTO review = new ReviewDTO();
        review.setComment(comment);
        review.setProductID(productID);
        review.setUserID(userID);
        review.setTimestamp(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public void editReview(String comment, Long productID){
        Long userID= 10L;
        ReviewDTO existingReview=reviewRepository.findByProductIDAndUserID(productID,userID);
        if (existingReview == null) {
            throw new RuntimeException("User comment does not exist");
        }
        existingReview.setComment(comment);
        existingReview.setTimestamp(LocalDateTime.now());

        reviewRepository.save(existingReview);
    }
}
