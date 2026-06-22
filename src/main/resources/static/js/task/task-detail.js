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