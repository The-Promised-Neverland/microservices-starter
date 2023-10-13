package com.products.repository;

import com.products.module.ProductDTO;
import com.products.module.displayProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface productRepository extends JpaRepository<ProductDTO, Long> {

    @Query("SELECT NEW com.products.module.displayProductDTO(p.name, p.price, COALESCE(p.totalRatings / NULLIF(p.totalBuyers, 0), 0)) FROM ProductDTO p WHERE LOWER(p.name) LIKE %:searchKeyword%")
    Page<displayProductDTO> findByKeyword(Pageable pageable, String searchKeyword);
}
