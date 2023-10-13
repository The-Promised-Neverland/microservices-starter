package com.reviews.controllers;


import com.reviews.module.ReviewByProductDTO;
import com.reviews.module.ReviewByUserDTO;
import com.reviews.module.commentDTO;
import com.reviews.service.reviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reviews")
public class reviewController {

    @Autowired
    private reviewService reviewService;

    @GetMapping("/user/{userID}")
    public ResponseEntity<?> userReviews(@PathVariable Long userID) {
        List<ReviewByUserDTO> reviews=reviewService.getReviewsByuserID(userID);
        if(reviews.size()==0){
            return new ResponseEntity<>("No review found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @GetMapping("/product/{productID}")
    public ResponseEntity<?> productReviews(@PathVariable Long productID) {
        List<ReviewByProductDTO> reviews=reviewService.getReviewsByProductID(productID);
        if(reviews.size()==0){
            return new ResponseEntity<>("No review found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @PutMapping("/create/{productID}")
    public ResponseEntity<?> createReview(@PathVariable Long productID, @RequestBody commentDTO commentDTO) {
        reviewService.createReview(commentDTO.getComment(),productID);
        return new ResponseEntity<>("Comment posted successfully",HttpStatus.CREATED);
    }

    @PutMapping("/edit/{productID}")
    public ResponseEntity<?> editReview(@PathVariable Long productID, @RequestBody commentDTO commentDTO) {
        reviewService.editReview(commentDTO.getComment(),productID);
        return new ResponseEntity<>("Comment edited successfully",HttpStatus.CREATED);
    }
}
