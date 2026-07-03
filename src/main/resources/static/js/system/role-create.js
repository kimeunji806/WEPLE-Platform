/* 상단 전체선택/전체해제 (모든 그룹 대상) */
document.getElementById("selectAll").addEventListener("click", function () {
    document.querySelectorAll('input[name="permissionCodes"]').forEach(cb => cb.checked = true);
    document.querySelectorAll('.perm-group').forEach(updateGroupButtonLabel);
});
document.getElementById("deselectAll").addEventListener("click", function () {
    document.querySelectorAll('input[name="permissionCodes"]').forEach(cb => cb.checked = false);
    document.querySelectorAll('.perm-group').forEach(updateGroupButtonLabel);
});

/* 그룹별 "전체 선택"/"전체 해제" 버튼 클릭 */
function toggleGroup(btn) {
    const group = btn.closest('.perm-group');
    const checks = group.querySelectorAll('input[name="permissionCodes"]');
    const allChecked = [...checks].every(cb => cb.checked);
    checks.forEach(cb => { cb.checked = !allChecked; });
    updateGroupButtonLabel(group);
}

/* 그룹 내 체크 상태에 맞춰 버튼 라벨 갱신
   - 전부 체크 → "전체 해제"
   - 하나라도 해제 → "전체 선택" */
function updateGroupButtonLabel(group) {
    const checks = group.querySelectorAll('input[name="permissionCodes"]');
    const btn = group.querySelector('.group-all-btn');
    if (!btn || checks.length === 0) return;
    const allChecked = [...checks].every(cb => cb.checked);
    btn.textContent = allChecked ? '전체해제' : '전체선택';
}

/* 개별 체크박스를 직접 클릭했을 때도 그룹 버튼 라벨 동기화 */
document.querySelectorAll('input[name="permissionCodes"]').forEach(cb => {
    cb.addEventListener('change', () => {
        updateGroupButtonLabel(cb.closest('.perm-group'));
    });
});

/* 수정 모드 등 페이지 로드시 이미 체크되어 있는 그룹은 처음부터 "전체 해제"로 표시 */
document.querySelectorAll('.perm-group').forEach(updateGroupButtonLabel);