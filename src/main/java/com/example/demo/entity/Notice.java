package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Notice")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long noticeNo;          // 공지사항 번호

    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100) // 길이를 100으로 설정
    private String title;           // 제목

    @Lob
    @Size(min = 10, max = 100)
    @Column(nullable = false, length = 1000) // 길이를 1000으로 설정
    private String content;         // 내용

    @Column(nullable = false)
    private LocalDate ntcStDt;  // 공지시작일시

    @Column(nullable = false)
    private LocalDate ntcEndDt; // 공지종료일시

    private Long viewCnt;           // 조회수

    @Column(nullable = false)
    private String writer;          // 작성자

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 직렬화할 때 참조하는 객체를 표시
    private List<Attachment> attachments = new ArrayList<>(); // 게시글 1 : 첨부파일 N
}
