package com.products.controllers;

import com.products.module.ProductDTO;
import com.products.services.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products/admin")
public class productController_AdminAccess {
    @Autowired
    private productService productService;

    @PutMapping("/delete/{ProductID}")
    public ResponseEntity<String> deleteProduct(@PathVariable("ProductID") Long productID) {
        productService.deleteProduct(productID);
        return new ResponseEntity<>("Product: " + productID + " deleted successfully!!!",HttpStatus.OK);
    }

    @PutMapping("/modify/{productID}")
    public ResponseEntity<String> modifyProduct(@PathVariable Long productID, @RequestBody Map<String, Object> updates) {
        ProductDTO existingProduct = productService.showProduct(productID);

        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        if (updates.containsKey("stock")) {
            Long newStock = (Long) updates.get("stock");
            existingProduct.setStock(existingProduct.getStock() + newStock);
        }
        if (updates.containsKey("name")) {
            String newName = (String) updates.get("name");
            existingProduct.setName(newName);
        }
        if (updates.containsKey("description")) {
            String newDescription = (String) updates.get("description");
            existingProduct.setDescription(newDescription);
        }
        if (updates.containsKey("price")) {
            Long newPrice = (Long) updates.get("description");
            existingProduct.setPrice(newPrice);
        }
        productService.saveProduct(existingProduct);
        return new ResponseEntity<>("Product modified successfully",HttpStatus.OK);
    }

    @PutMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return new ResponseEntity<>("Product created successfully", HttpStatus.OK);
    }


}
