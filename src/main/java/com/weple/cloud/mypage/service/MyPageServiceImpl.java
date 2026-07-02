package com.weple.cloud.mypage.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.weple.cloud.file.S3Service;
import com.weple.cloud.mypage.mapper.MyPageMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

    private final MyPageMapper myPageMapper;
    private final S3Service s3Service;

    @Override
    public MyPageVO findMyPage(String userCode) {
        validateUserCode(userCode);
        MyPageVO myPage = myPageMapper.selectMyPage(userCode);
        if (myPage == null) {
            throw new IllegalArgumentException("조회할 사용자를 찾을 수 없습니다.");
        }
        return myPage;
    }

    @Override
    @Transactional
    public void updateNotificationSetting(MyPageNotificationUpdateVO request) {
        validateUserCode(request.getUserCode());

        // 관리-사용자등록(UserManagementServiceImpl.toYn)과 동일하게 hidden+checkbox 조합으로
        // "N,Y" 형태로 바인딩되어도 안전하게 Y/N만 남긴다.
        request.setWebNotificationYn(toYn(request.getWebNotificationYn()));
        request.setEmailNotificationYn(toYn(request.getEmailNotificationYn()));

        if (!NotificationAreaType.isValid(request.getNotificationArea())) {
            request.setNotificationArea(NotificationAreaType.DEFAULT);
        }

        if (myPageMapper.updateNotificationSetting(request) != 1) {
            throw new IllegalStateException("알림 설정을 저장할 수 없습니다.");
        }
    }

    @Override
    public MyPageProfileVO findMyPageProfile(String userCode) {
        validateUserCode(userCode);
        MyPageProfileVO profile = myPageMapper.selectMyPageProfile(userCode);
        if (profile == null) {
            throw new IllegalArgumentException("조회할 사용자를 찾을 수 없습니다.");
        }
        return profile;
    }

    @Override
    @Transactional
    public String updateProfileImage(String userCode, MultipartFile file) {
        validateUserCode(userCode);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("등록할 사진을 선택해주세요.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일은 5MB 이하만 등록할 수 있습니다.");
        }

        String extension = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("이미지 파일(jpg, jpeg, png, gif)만 등록할 수 있습니다.");
        }

        // 교체 전 사진이 있었으면 S3에서 정리 (없거나 이미 없어도 조용히 무시됨)
        MyPageProfileVO beforeProfile = myPageMapper.selectMyPageProfile(userCode);
        String previousImageUrl = beforeProfile != null ? beforeProfile.getProfileImage() : null;

        String savedName = "profile/" + UUID.randomUUID() + "." + extension;
        String profileImageUrl;
        try {
            profileImageUrl = s3Service.uploadFile(file, savedName);
        } catch (IOException e) {
            throw new IllegalStateException("프로필 사진 저장 중 오류가 발생했습니다.", e);
        }

        if (myPageMapper.updateProfileImage(userCode, profileImageUrl) != 1) {
            throw new IllegalStateException("프로필 사진을 저장할 수 없습니다.");
        }

        deleteFromS3IfPresent(previousImageUrl);
        return profileImageUrl;
    }

    @Override
    @Transactional
    public void deleteProfileImage(String userCode) {
        validateUserCode(userCode);
        MyPageProfileVO profile = myPageMapper.selectMyPageProfile(userCode);
        myPageMapper.deleteProfileImage(userCode);
        if (profile != null) {
            deleteFromS3IfPresent(profile.getProfileImage());
        }
    }

    // 내부 유틸

    private void validateUserCode(String userCode) {
        if (userCode == null || userCode.isBlank()) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    // S3 URL에서 key(경로)만 뽑아내 기존 파일을 정리. 로컬 저장 시절의 옛 경로("/uploads/..")나
    // null인 경우는 S3 키가 아니므로 건드리지 않는다.
    private void deleteFromS3IfPresent(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.contains("amazonaws.com/")) {
            return;
        }
        String key = imageUrl.substring(imageUrl.indexOf("amazonaws.com/") + "amazonaws.com/".length());
        try {
            s3Service.deleteFile(key);
        } catch (Exception e) {
            // 이전 파일 정리 실패는 치명적이지 않으므로 로그만 남기고 넘어간다.
            e.printStackTrace();
        }
    }

    // 관리-사용자등록(UserManagementServiceImpl.toYn)과 동일한 정규화 로직
    private String toYn(String value) {
        return value != null && value.contains("Y") ? "Y" : "N";
    }
}