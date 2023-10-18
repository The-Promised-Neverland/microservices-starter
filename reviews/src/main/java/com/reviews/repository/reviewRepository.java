package com.reviews.repository;

import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface reviewRepository extends JpaRepository<ReviewDTO,Long> {
    @Query("SELECT NEW com.reviews.module.ReviewByProductDTO(r.email, r.timestamp, r.comment, r.rating) FROM ReviewDTO r WHERE r.productID = ?1")
    Page<ReviewByProductDTO> findReviewsByProductID(Long productID, Pageable pageable);

    @Query("SELECT NEW com.reviews.module.ReviewByUserDTO(r.productID, r.timestamp, r.comment, r.rating) FROM ReviewDTO r WHERE r.email = ?1")
    Page<ReviewByUserDTO> findByReviewsByEmail(String email, Pageable pageable);

    ReviewDTO findByProductIDAndEmail(Long productID, String email);
}
