package com.example.demo.unitTest;

import com.example.demo.controller.NoticeController;
import com.example.demo.entity.Notice;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class NoticeUnitTest {

    @InjectMocks
    private NoticeController noticeController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("공지글 + 첨부파일 INSERT 테스트")
    public void createNoticeWithFile() throws IOException {
        Notice notice = new Notice();
        notice.builder()
              .title("제목 테스트")
              .content("내용 테스트")
              .ntcStDt(LocalDate.now())
              .ntcEndDt(LocalDate.now().plusDays(3L))
              .writer("작성자");

        // 테스트용 MultipartFile 객체 생성
        MultipartFile file1 = new MockMultipartFile("file1.txt", "file1.txt", "text/plain", "Test file 1".getBytes());
        MultipartFile file2 = new MockMultipartFile("file2.txt", "file2.txt", "text/plain", "Test file 2".getBytes());

        // NoticeController의 createNotice 메서드 호출
        ResponseEntity<Notice> response = noticeController.createNotice(notice, Arrays.asList(file1, file2));

        // 테스트 결과 확인
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("공지글 INSERT 테스트")
    public void createNoticeWithNoFiles() throws IOException {
        // 입력 데이터
        Notice notice = new Notice();
        notice.builder()
              .title("제목 테스트")
              .content("내용 테스트")
              .ntcStDt(LocalDate.now())
              .ntcEndDt(LocalDate.now().plusDays(3L))
              .writer("작성자");

        // NoticeController의 createNotice 메서드 호출
        ResponseEntity<Notice> response = noticeController.createNotice(notice, null);

        // 테스트 결과 확인
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}