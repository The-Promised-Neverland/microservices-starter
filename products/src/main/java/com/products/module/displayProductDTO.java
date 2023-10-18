package com.products.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class displayProductDTO {
    private Long ProductID;
    private String name;
    private double price;
    private double ratings=0;
}
