package com.products.module;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductID;

    private String name;
    private String description;
    private double price;
    private Long stock;

    //This will be used to calculate the actual ratings of any product...Rating=totalRatings/totalBuyers
    private Long totalBuyers=0L;
    private double totalRatings=0.0;

}
