package com.products.services;


import com.products.module.ProductDTO;
import com.products.module.displayProductDTO;
import com.products.repository.productRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class productService {

    @Autowired
    private productRepository productRepository;

    public Page<displayProductDTO> displayProducts(Pageable pageable, String keyword) {
        return productRepository.findByKeyword(pageable, keyword);
    }

    public ProductDTO showProduct(Long productID){
        ProductDTO product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + productID + " not found"));
        return product;
    }

    public void saveProduct(ProductDTO productDTO){
        productRepository.save(productDTO);
    }

    public void deleteProduct(Long productID){
        productRepository.deleteById(productID);
    }
}
