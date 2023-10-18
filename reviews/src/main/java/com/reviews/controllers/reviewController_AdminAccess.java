package com.reviews.controllers;

import com.reviews.module.ReviewByUserDTO;
import com.reviews.service.reviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
public class reviewController_AdminAccess {
    @Autowired
    private reviewService reviewService;

    @GetMapping("/email/{userID}")
    public ResponseEntity<Page<ReviewByUserDTO>> userReviews(@PathVariable("userID") String userID, Pageable pageable) {
        Page<ReviewByUserDTO> reviews=reviewService.getReviewsByUserID(userID,pageable);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }
}
