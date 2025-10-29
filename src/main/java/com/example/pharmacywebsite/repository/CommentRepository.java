package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
