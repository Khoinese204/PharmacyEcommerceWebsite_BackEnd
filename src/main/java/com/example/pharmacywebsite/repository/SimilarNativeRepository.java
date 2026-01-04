package com.example.pharmacywebsite.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SimilarNativeRepository {

    private final EntityManager em;

    /**
     * returns rows: [id(Integer), score(Double)]
     */
    public List<Object[]> findSimilarByTrigram(Integer productId, int limit) {
        return em.createNativeQuery("""
                    SELECT m2.id AS id,
                           similarity(m1.search_text, m2.search_text) AS score
                    FROM medicines m1
                    JOIN medicines m2 ON m2.id <> m1.id
                    WHERE m1.id = :id
                      AND m2.search_text IS NOT NULL
                      AND m2.search_text <> ''
                    ORDER BY score DESC
                    LIMIT :k
                """)
                .setParameter("id", productId)
                .setParameter("k", limit)
                .getResultList();
    }
}