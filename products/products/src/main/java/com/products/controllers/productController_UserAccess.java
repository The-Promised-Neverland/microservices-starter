package com.products.controllers;


import com.products.module.ProductDTO;
import com.products.module.displayProductDTO;
import com.products.services.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class productController_UserAccess {

    @Autowired
    private productService productService;


    /**
     * url - {{DOMAIN}}/api/products/?page={PAGE_NUMBER}&size={PAGE_SIZE}&sort={SORTING_ON},{asc/desc}
     * GET - NAME, PRICE, RATINGS
     * UNSECURED
     */
    @GetMapping
    public ResponseEntity<Page<displayProductDTO>> displayProducts(Pageable pageable, @RequestParam(name = "search", defaultValue = "") String search) {
        Page<displayProductDTO> products = productService.displayProducts(pageable, search);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * url - {{DOMAIN}}/api/products/{productID}
     * GET - NAME, PRICE, RATINGS
     * UNSECURED
     */
    @GetMapping("/{productID}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productID") Long productID) {
        ProductDTO product = productService.showProduct(productID);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
