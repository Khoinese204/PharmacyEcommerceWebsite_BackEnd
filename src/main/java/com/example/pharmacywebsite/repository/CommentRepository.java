package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("""
               select c from Comment c
               left join fetch c.user u
               left join fetch c.parent p
               where c.review.reviewId = :reviewId
               order by c.createdAt asc
            """)
    List<Comment> findAllByReviewIdWithUser(@Param("reviewId") Integer reviewId);

}
