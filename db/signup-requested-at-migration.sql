-- 일반 회원가입의 승인 요청 시각을 별도로 보관하기 위한 마이그레이션입니다.
ALTER TABLE users ADD (signup_requested_at DATE);

-- 기존 승인 대기 데이터는 기존 계정 생성 시각을 요청 시각으로 초기 설정합니다.
UPDATE users
SET signup_requested_at = create_date
WHERE status = 'a1'
  AND signup_requested_at IS NULL;

COMMIT;
