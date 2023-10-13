package com.reviews.repository;

import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface reviewRepository extends JpaRepository<ReviewDTO,Long> {
    @Query("SELECT NEW com.reviews.module.ReviewByProductDTO(r.userID, r.timestamp, r.comment) FROM ReviewDTO r WHERE r.productID = ?1")
    List<ReviewByProductDTO> findReviewsByProductID(Long productID);

    @Query("SELECT NEW com.reviews.module.ReviewByUserDTO(r.productID, r.timestamp, r.comment) FROM ReviewDTO r WHERE r.userID = ?1")
    List<ReviewByUserDTO> findByReviewsByUserID(Long userID);

    ReviewDTO findByProductIDAndUserID(Long productID, Long userID);
}
