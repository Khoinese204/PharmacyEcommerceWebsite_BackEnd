package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.ProductCrossSellRule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCrossSellRuleRepository extends JpaRepository<ProductCrossSellRule, Long> {

    @Query("""
                select r
                from ProductCrossSellRule r
                where r.productId = :productId
                order by r.id desc
            """)
    List<ProductCrossSellRule> findByProductId(@Param("productId") Integer productId, Pageable pageable);
}
