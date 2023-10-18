package com.reviews.controllers;


import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.commentDTO;
import com.reviews.service.reviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reviews")
public class reviewController_UserAccess {

    @Autowired
    private reviewService reviewService;

    @GetMapping("/noauth/product/{productID}")
    public ResponseEntity<Page<ReviewByProductDTO>> productReviews(Pageable pageable, @PathVariable("productID") Long productID) {
        Page<ReviewByProductDTO> reviews=reviewService.getReviewsByProductID(productID, pageable);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @PutMapping("/create/product/{productID}")
    public ResponseEntity<?> createReview(@PathVariable("productID") Long productID, @RequestBody commentDTO commentDTO, @RequestHeader("user") String email) {
        reviewService.createReview(commentDTO,productID,email);
        return new ResponseEntity<>("Comment posted successfully",HttpStatus.CREATED);
    }

    @PutMapping("/edit/product/{productID}")
    public ResponseEntity<?> editReview(@PathVariable("productID") Long productID, @RequestHeader("user") String email, @RequestBody commentDTO commentDTO) {
        reviewService.editReview(commentDTO,productID,email);
        return new ResponseEntity<>("Comment edited successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/product/{productID}")
    public ResponseEntity<?> deleteReview(@PathVariable("productID") Long productID, @RequestHeader("user") String email) {
        reviewService.deleteReview(email,productID);
        return new ResponseEntity<>("Comment edited successfully",HttpStatus.CREATED);
    }

}
