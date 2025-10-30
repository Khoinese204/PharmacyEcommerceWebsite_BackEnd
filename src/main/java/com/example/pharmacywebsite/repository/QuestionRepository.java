package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.pharmacywebsite.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @EntityGraph(attributePaths = { "answers", "answers.user", "user" })
    @Query("SELECT q FROM Question q WHERE q.medicine.id = :medicineId")
    List<Question> findByMedicineIdFetchAll(@Param("medicineId") Integer medicineId);

    @EntityGraph(attributePaths = { "answers", "answers.user", "user" })
    @Query("SELECT q FROM Question q WHERE q.questionId = :qid")
    Optional<Question> findByIdFetchAll(@Param("qid") Integer qid);
}
