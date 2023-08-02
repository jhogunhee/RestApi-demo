package com.example.demo.service;

import com.example.demo.entity.Attachment;
import com.example.demo.entity.Notice;
import com.example.demo.entity.QAttachment;
import com.example.demo.entity.QNotice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class NoticeService {
    @Value("${upload.directory}")
    private String uploadDirectory;

    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public NoticeService(JpaContext jpaContext) {
        this.entityManager = jpaContext.getEntityManagerByManagedType(Notice.class);
        this.queryFactory  = new JPAQueryFactory(this.entityManager);
    }
    @Transactional(rollbackOn = {IOException.class})
    public Notice createNotice(Notice notice, List<MultipartFile> files) throws IOException {
        if (files != null) {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                String saveName = UUID.randomUUID().toString() + "_" + originalName;
                long fileSize = file.getSize();

                File dest = new File(uploadDirectory + File.separator + saveName);
                if (!dest.exists()) {
                    dest.mkdirs();
                }

                file.transferTo(dest);

                Attachment attachment = new Attachment();
                attachment.setNotice(notice); // 공지사항과 첨부파일 연결
                attachment.setOriginalName(originalName);
                attachment.setSaveName(saveName);
                attachment.setFileSize(fileSize);

                notice.getAttachments().add(attachment); // attachments 리스트에 추가
            }
        }

        notice.setViewCnt(0L); // 조회수 0으로 설정
        entityManager.persist(notice); // 자식 엔티티들이 자동으로 저장됨
        return notice;
    }

    public List<Notice> getAllNotice() {
        QNotice notice = QNotice.notice;

        return queryFactory.selectFrom(notice)
                           .fetch();
    }

    @Transactional
    public Notice getNotice(Long noticeNo) {
        QNotice notice         = QNotice.notice;
        QAttachment attachment = QAttachment.attachment;

        queryFactory.update(notice)
                    .set(notice.viewCnt, notice.viewCnt.add(1))
                    .where(notice.noticeNo.eq(noticeNo))
                    .execute();

        return queryFactory.selectFrom(notice)
                           .join(notice.attachments, attachment)
                           .fetchJoin() // Notice와 Attachment를 함께 로딩
                           .where(notice.noticeNo.eq(noticeNo))
                           .fetchOne();
    }

    @Transactional
    public void deleteNotice(Long noticeNo) {
        Notice notice = entityManager.find(Notice.class, noticeNo);
        if (notice == null) {
            throw new EntityNotFoundException("Notice not found with noticeNo: " + noticeNo);
        }

        List<Attachment> attachments = notice.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                deleteAttachmentFile(attachment); // 첨부파일 삭제
            }
        }

        entityManager.remove(notice); // 게시물 삭제
    }

    @Transactional
    public Notice updateNotice(Long noticeNo, Notice updatedNotice, List<MultipartFile> updatedFiles) throws IOException {
        Notice notice = entityManager.find(Notice.class, noticeNo);
        if (notice == null) {
            throw new EntityNotFoundException("Notice not found with noticeNo: " + noticeNo);
        }

        // 공지사항 업데이트
        notice.setTitle(updatedNotice.getTitle());
        notice.setContent(updatedNotice.getContent());
        notice.setWriter(updatedNotice.getWriter());
        notice.setNtcStDt(updatedNotice.getNtcStDt());
        notice.setNtcEndDt(updatedNotice.getNtcEndDt());

        // 기존 첨부파일 삭제
        for (Attachment attachment : notice.getAttachments()) {
            deleteAttachmentFile(attachment);
        }
        notice.getAttachments().clear();

        // 첨부파일 추가
        List<Attachment> updatedAttachments = new ArrayList<>();
        for (MultipartFile file : updatedFiles) {
            String originalName = file.getOriginalFilename();
            String saveName = UUID.randomUUID().toString() + "_" + originalName;
            long fileSize = file.getSize();

            File dest = new File(uploadDirectory + File.separator + saveName);
            if (!dest.exists()) {
                dest.mkdirs();
            }

            file.transferTo(dest);

            Attachment attachment = new Attachment();
            attachment.setNotice(notice);
            attachment.setOriginalName(originalName);
            attachment.setSaveName(saveName);
            attachment.setFileSize(fileSize);

            updatedAttachments.add(attachment);
        }

        notice.getAttachments().addAll(updatedAttachments);

        return notice;
    }

    private void deleteAttachmentFile(Attachment attachment) {
        String saveName = attachment.getSaveName();
        File file = new File(uploadDirectory + File.separator + saveName);
        if (file.exists()) {
            file.delete();
        }
    }
}
