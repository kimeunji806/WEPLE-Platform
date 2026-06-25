function openTab(evt, tabName) {

	// 1. 모든 탭 콘텐츠 영역 숨기기

	const tabContents = document.getElementsByClassName("tab-content");

	for (let i = 0; i < tabContents.length; i++) {

		tabContents[i].style.display = "none";

	}
	// 2. 모든 탭 버튼 활성화 스타일 초기화
	const tabButtons = document.getElementsByClassName("tab-button");

	for (let i = 0; i < tabButtons.length; i++) {

		tabButtons[i].classList.remove("active");

		tabButtons[i].style.background = "#f5f5f5";

		tabButtons[i].style.color = "#666";

		tabButtons[i].style.border = "1px solid #e0e0e0";

		tabButtons[i].style.borderBottom = "none";

	}



	// 3. 사용자가 클릭한 특정 탭 콘텐츠 노출

	document.getElementById(tabName).style.display = "block";



	// 4. 클릭된 버튼에 활성화(Active) CSS 적용

	evt.currentTarget.classList.add("active");

	evt.currentTarget.style.background = "#1a73e8";

	evt.currentTarget.style.color = "#fff";

	evt.currentTarget.style.border = "none";

}

document.addEventListener("DOMContentLoaded", function() {

	const taskDeleteDialog = document.getElementById('taskDeleteDialog');
	const taskDeleteForm = document.getElementById('taskDeleteForm');
	const taskDeleteName = document.getElementById('taskDeleteName');
	const taskDeletePrompt = document.getElementById('taskDeletePrompt');
	const confirmInput = document.getElementById('taskDeleteConfirmationName');
	const submitBtn = document.getElementById('taskDeleteSubmitBtn');

	if (!taskDeleteDialog) return;

	// 1. 삭제 버튼 클릭 시 모달 오픈 및 데이터 세팅
	document.querySelectorAll('.task-delete-trigger').forEach(function(button) {
		button.addEventListener('click', function() {
			const taskId = button.dataset.taskId;
			const taskTitle = button.dataset.taskTitle;
			const projectId = button.dataset.projectId;

			// 텍스트 가이드 변경
			taskDeleteName.textContent = taskTitle;
			taskDeletePrompt.textContent = '삭제하려면 ' + taskTitle + '을(를) 입력하세요.';
			confirmInput.value = '';

			// 버튼 비활성화 초기화
			submitBtn.disabled = true;

			// ⭐️ 핵심: 기존 컨트롤러가 사용하는 PathVariable과 QueryParam 주소 동적 세팅


			taskDeleteForm.action = '/project/task/delete/soft?projectId=' + projectId + '&tId=' + taskId;

			// 실시간 입력값 비교 체크 (일감명이 정확히 일치할 때만 버튼 활성화)
			confirmInput.oninput = function() {
				if (this.value.trim() === taskTitle.trim()) {
					submitBtn.disabled = false;
				} else {
					submitBtn.disabled = true;
				}
			};

			taskDeleteDialog.showModal();
		});
	});

	// 2. 모달 닫기 처리
	document.querySelectorAll('[data-task-delete-close]').forEach(function(button) {
		button.addEventListener('click', function() {
			taskDeleteDialog.close();
		});
	});
});

function toggleReplyForm(parentId, targetUser) {
	const formDiv = document.getElementById('reply-form-' + parentId);
	const textarea = document.getElementById('reply-content-' + parentId);

	if (formDiv.style.display === 'none') {
		formDiv.style.display = 'block';
		if (targetUser) {
			textarea.value = '@' + targetUser + ' ';
		}
		textarea.focus();
	} else {
		formDiv.style.display = 'none';
		textarea.value = '';
	}
}


// [댓글 전송 로직] - task-detail.js 내부 수정
function submitComment(parentId) {
    const contentId = parentId ? 'reply-content-' + parentId : 'mainCommentContent';
    const content = document.getElementById(contentId).value.trim();

    if (!content) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    // 🛠️ 외부 js 파일에서도 에러 없이 안전하게 일감 ID를 가져오는 방식
    const container = document.getElementById('taskDetailContainer');
    const currentTaskId = container ? container.dataset.taskId : '';

    const requestData = {
        taskId: currentTaskId,
        parentCommentId: parentId,
        taskComment: content
    };

    // 시큐리티 CSRF 토큰 설정
    const csrfToken = document.querySelector('input[name="_csrf"]').value;
    const csrfHeader = document.querySelector('input[name="_csrf_header"]')?.value || 'X-CSRF-TOKEN';

    fetch('/project/task/comment/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
        } else {
            alert('댓글 등록에 실패했습니다: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    });
}