package com.reviews.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewByProductDTO {
    private Long userID;
    private LocalDateTime timestamp;
    private String comment;
}
