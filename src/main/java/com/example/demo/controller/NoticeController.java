package com.example.demo.controller;

import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 모든 공지사항 조회 API
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotice() {
        List<Notice> notice = noticeService.getAllNotice();
        return ResponseEntity.ok(notice);
    }

    // 특정 공지사항 조회 API
    @GetMapping("/{notice_no}")
    public Notice getNotice(@PathVariable("notice_no") Long noticeNo) {
        Notice notice = noticeService.getNotice(noticeNo);
        return notice;
    }

    // 공지사항 등록 API
    @PostMapping
    public ResponseEntity<Notice> createNotice(Notice notice, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws IOException {
        try {
            Notice createdNotice = noticeService.createNotice(notice, files);
            // 등록된 공지글과 함께 201 Created 상태코드를 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    // 공지사항 삭제 API
    @DeleteMapping("/{notice_no}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("notice_no") Long noticeNo) {
        noticeService.deleteNotice(noticeNo);
        return ResponseEntity.noContent().build();
    }

    // 공지사항 수정 API
    @PutMapping("/{notice_no}")
    public ResponseEntity<Notice> updateNotice(@PathVariable("notice_no") Long noticeNo, Notice notice
                                             , @RequestPart(name = "files", required = false) List<MultipartFile> files) throws IOException {
        Notice respNotice = noticeService.updateNotice(noticeNo, notice, files);
        return new ResponseEntity<>(respNotice, HttpStatus.OK);
    }
}
