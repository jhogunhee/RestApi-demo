package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "AttachFile")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileNo;         // 파일번호

    @ManyToOne
    @JoinColumn(name = "notice_no")
    @JsonBackReference           // 역참조하는 객체를 표시
    private Notice notice;       // 공지사항
    private String originalName; // 원본 이름
    private String saveName;     // 실제 저장 파일명
    private Long fileSize;       // 파일 크기

    @CreatedDate
    private LocalDateTime createDate;
}
