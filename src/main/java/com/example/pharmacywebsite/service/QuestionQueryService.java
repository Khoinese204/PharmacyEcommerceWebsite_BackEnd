package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.Answer;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.Question;
import com.example.pharmacywebsite.domain.User;
import com.example.pharmacywebsite.dto.AnswerCreateDto;
import com.example.pharmacywebsite.dto.QuestionCreateDto;
import com.example.pharmacywebsite.dto.QuestionListResponse;
import com.example.pharmacywebsite.dto.QuestionResponseDto;
import com.example.pharmacywebsite.repository.AnswerRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.repository.QuestionRepository;
import com.example.pharmacywebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {

    private final QuestionRepository questionRepo;
    private final MedicineRepository medicineRepo;
    private final UserRepository userRepo;
    private final AnswerRepository answerRepo;

    @Transactional
    public QuestionResponseDto createQuestion(Integer medicineId, QuestionCreateDto dto) {
        // ===== 1️⃣ Validate =====
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        if (dto.getContent() == null || dto.getContent().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "content is required");

        // ===== 2️⃣ Load User & Medicine =====
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        Medicine medicine = medicineRepo.findById(medicineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MEDICINE_NOT_FOUND"));

        // ===== 3️⃣ Kiểm tra role (chỉ CUSTOMER được phép đặt câu hỏi) =====
        String roleName = user.getRole() != null ? user.getRole().getName().toUpperCase() : null;
        if (!"CUSTOMER".equals(roleName)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ONLY_CUSTOMER_CAN_ASK");
        }

        // ===== 4️⃣ Lưu câu hỏi =====
        Question q = new Question();
        q.setMedicine(medicine);
        q.setUser(user);
        q.setContent(dto.getContent());
        q = questionRepo.save(q);

        // ===== 5️⃣ Tính toán quyền trả lời =====
        boolean canAnswer = roleName.equals("CUSTOMER") || roleName.equals("PHARMACIST") || roleName.equals("ADMIN");

        // ===== 6️⃣ Trả về response =====
        return QuestionResponseDto.builder()
                .questionId(q.getQuestionId())
                .userId(user.getId())
                .medicineId(medicineId)
                .content(q.getContent())
                .createdAt(q.getCreatedAt())
                .answers(Collections.emptyList())
                .capabilities(QuestionResponseDto.Capabilities.builder()
                        .canAnswer(canAnswer)
                        .build())
                .build();
    }

    /** 2.3 Trả lời câu hỏi (CUSTOMER/PHARMACIST/ADMIN) */
    @Transactional
    public QuestionResponseDto answerQuestion(Integer medicineId, Integer questionId, AnswerCreateDto dto) {
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        if (dto.getContent() == null || dto.getContent().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "content is required");

        // Người trả lời
        User replier = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        // Phân quyền: CUSTOMER / PHARMACIST / ADMIN được trả lời
        String role = replier.getRole() != null ? replier.getRole().getName().toUpperCase() : null;
        boolean allowed = "CUSTOMER".equals(role) || "PHARMACIST".equals(role) || "ADMIN".equals(role);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN_ROLE_TO_ANSWER");
        }

        // Load question + check medicine
        Question question = questionRepo.findByIdFetchAll(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND"));
        if (!question.getMedicine().getId().equals(medicineId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QUESTION_NOT_OF_MEDICINE");
        }

        // Lưu answer
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setUser(replier);
        answer.setContent(dto.getContent());
        answerRepo.save(answer);

        // Reload question kèm answers (để đảm bảo danh sách mới nhất)
        Question q2 = questionRepo.findByIdFetchAll(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND"));

        // Sort answers mới nhất trước
        var answers = q2.getAnswers().stream()
                .sorted(Comparator.comparing(Answer::getCreatedAt).reversed())
                .map(a -> QuestionResponseDto.AnswerDto.builder()
                        .answerId(a.getAnswerId())
                        .questionId(q2.getQuestionId())
                        .userId(a.getUser().getId())
                        .userRole(a.getUser().getRole() != null ? a.getUser().getRole().getName() : null)
                        .isPharmacist(a.getUser().getRole() != null &&
                                a.getUser().getRole().getName().equalsIgnoreCase("PHARMACIST"))
                        .content(a.getContent())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // Tính canAnswer cho actor hiện tại (replier)
        boolean canAnswer = true; // vì role đã pass ở trên

        return QuestionResponseDto.builder()
                .questionId(q2.getQuestionId())
                .userId(q2.getUser().getId())
                .medicineId(q2.getMedicine().getId())
                .content(q2.getContent())
                .createdAt(q2.getCreatedAt())
                .answers(answers)
                .capabilities(QuestionResponseDto.Capabilities.builder()
                        .canAnswer(canAnswer)
                        .build())
                .build();
    }

     @Transactional(readOnly = true)
    public QuestionListResponse listAll(Integer medicineId, String sort, Integer viewerId) {

        // 1️⃣ Lấy tất cả câu hỏi của medicine
        List<Question> all = questionRepo.findByMedicineIdFetchAll(medicineId);

        // 2️⃣ Sắp xếp theo newest | oldest (mặc định newest)
        if ("oldest".equalsIgnoreCase(sort)) {
            all.sort(Comparator.comparing(Question::getCreatedAt)); // cũ -> mới
        } else {
            all.sort(Comparator.comparing(Question::getCreatedAt).reversed()); // mới -> cũ
        }

        // 3️⃣ Xác định người xem (để tính quyền)
        User viewer = null;
        if (viewerId != null) {
            viewer = userRepo.findById(viewerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Viewer not found"));
        }
        final User finalViewer = viewer;

        // 4️⃣ Chuyển sang DTO
        var items = all.stream().map(q -> {
            boolean canAnswer = false;
            if (finalViewer != null && finalViewer.getRole() != null) {
                String role = finalViewer.getRole().getName().toUpperCase();
                canAnswer = role.equals("CUSTOMER") || role.equals("PHARMACIST") || role.equals("ADMIN");
            }

            // sort answer theo thời gian tăng dần
            var answers = q.getAnswers().stream()
                    .sorted(Comparator.comparing(Answer::getCreatedAt))
                    .map(a -> QuestionListResponse.AnswerDto.builder()
                            .answerId(a.getAnswerId())
                            .questionId(q.getQuestionId())
                            .userId(a.getUser().getId())
                            .userRole(a.getUser().getRole() != null ? a.getUser().getRole().getName() : null)
                           
                            .content(a.getContent()) // đảm bảo không bị null khi entity đúng
                            .createdAt(a.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

            return QuestionListResponse.Item.builder()
                    .questionId(q.getQuestionId())
                    .userId(q.getUser().getId())
                    .medicineId(q.getMedicine().getId())
                    .content(q.getContent())
                    .createdAt(q.getCreatedAt())
                    .answers(answers)
                    .capabilities(QuestionListResponse.Capabilities.builder()
                            .canAnswer(canAnswer)
                            .build())
                    .build();
        }).collect(Collectors.toList());

        return QuestionListResponse.builder().items(items).build();
    }

    
}
